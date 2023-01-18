package hr.fer.zemris.java.webserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;
import hr.fer.zemris.java.webserver.util.RequestProcessingUtil;

/**
 * Class models Http server.
 * 
 * @author Ana Bagić
 *
 */
public class SmartHttpServer {

	/** Address on which the server listens. */
	private String address;
	/** Domain name of the server, */
	private String domainName;
	/** Port on which the server listens. */
	private int port;
	/** Number of threads in thread pool. */
	private int workerThreads;
	/** Duration of user sessions in seconds. */
	private int sessionTimeout;
	/** Mapping for extensions to mime-types. */
	private Map<String,String> mimeTypes = new HashMap<>();
	/** Main thread of the server. */
	private ServerThread serverThread;
	/** Thread pool used to serve clients. */
	private ExecutorService threadPool;
	/** Path to the root directory from where the files are served. */
	private Path documentRoot;
	/** Mapping for worker names to their implementation. */
	private Map<String, IWebWorker> workersMap = new HashMap<>();
	/** Mapping for session IDs to their descriptor. */
	private Map<String, SessionMapEntry> sessions = new HashMap<>();
	/** Randomizer to get session IDs. */
	private Random sessionRandom = new Random();
	
	/**
	 * Constructor initializes Http server from the given config file.
	 * 
	 * @param configFileName path to the config file
	 */
	public SmartHttpServer(String configFileName) {
		Properties properties = new Properties();
		Properties mimeProperties = new Properties();
		Properties workersProperties = new Properties();
		
		try(InputStream is = Files.newInputStream(Paths.get(configFileName))) {
			properties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		address = properties.getProperty("server.address");
		domainName = properties.getProperty("server.domainName");
		port = Integer.parseInt(properties.getProperty("server.port"));
		workerThreads = Integer.parseInt(properties.getProperty("server.workerThreads"));
		sessionTimeout = Integer.parseInt(properties.getProperty("session.timeout"));
		documentRoot = Paths.get(properties.getProperty("server.documentRoot"));
		
		try(InputStream is = Files.newInputStream(Paths.get(properties.getProperty("server.mimeConfig")))) {
			mimeProperties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(var e : mimeProperties.entrySet()) {
			mimeTypes.put(e.getKey().toString(), e.getValue().toString());
		}
		
		try(InputStream is = Files.newInputStream(Paths.get(properties.getProperty("server.workers")))) {
			workersProperties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(var e : workersProperties.entrySet()) {
			String path = e.getKey().toString();
			String fqcn = e.getValue().toString();
			
			Class<?> referenceToClass = null;
			try {
				referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
				continue;
			}
			try {
				workersMap.put(path, (IWebWorker) referenceToClass.getDeclaredConstructor().newInstance());
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * Starts server thread and initializes thread pool.
	 */
	protected synchronized void start() {
		threadPool = Executors.newFixedThreadPool(workerThreads);
		Thread cookieManagement = new SessionManagementThread();
		cookieManagement.setDaemon(true);
		cookieManagement.start();
		serverThread = new ServerThread();
		serverThread.run();
	}
	
	/**
	 * Shuts down server thread and thread pool.
	 */
	protected synchronized void stop() {
		serverThread.interrupt();
		threadPool.shutdown();
	}
	
	/**
	 * Runs {@link SmartHttpServer}.
	 * 
	 * @param args first argument is path to server.properties
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Expected one argument: path to server.properties!");
			return;
		}
		
		SmartHttpServer server = new SmartHttpServer(args[0]);
		server.start();
	}
	
	/**
	 * Thread used to act as a garbage collector for non valid sessions.
	 * 
	 * @author Ana Bagić
	 *
	 */
	protected class SessionManagementThread extends Thread {
		
		@Override
		public void run() {
			while(true) {
				sessions.entrySet().removeIf(e -> e.getValue().validUntil < Instant.now().getEpochSecond());
				
				try {
					Thread.sleep(300000);
				} catch (InterruptedException e1) {}
			}
		}
	}
	
	/**
	 * Class models the main thread of the server.
	 * 
	 * @author Ana Bagić
	 *
	 */
	protected class ServerThread extends Thread {
		
		@SuppressWarnings("resource")
		@Override
		public void run() {
			ServerSocket serverSocket = null;
			try {
				serverSocket = new ServerSocket();
				serverSocket.bind(new InetSocketAddress(address, port));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("Server is listening on address: " + address + " and port: " + port);
			
			while(true) {
				Socket client = null;
				try {
					client = serverSocket.accept();
				} catch (IOException e) {
					e.printStackTrace();
				}
				threadPool.submit(new ClientWorker(client));
			}
		}
	}
	
	/**
	 * Class models a job that client thread runs.
	 * 
	 * @author Ana Bagić
	 *
	 */
	private class ClientWorker implements Runnable, IDispatcher {
		
		/** Socket on which the client thread listens. */
		private Socket csocket;
		/** Input stream to read requests from. */
		private InputStream istream;
		/** Output stream to send the responses to. */
		private OutputStream ostream;
		/** Version of the protocol. */
		private String version;
		/** Method of the request. */
		private String method;
		/** Host of the server. */
		private String host;
		/** Parameters map of the request. */
		private Map<String,String> params = new HashMap<>();
		/** Temporary parameters. */
		private Map<String,String> tempParams = new HashMap<>();
		/** Persistent parameters. */
		private Map<String,String> perParams = new HashMap<>();
		/** Output cookies. */
		private List<RCCookie> outputCookies = new ArrayList<>();
		/** Session ID. */
		private String SID;
		/** Request context used to send response to client. */
		private RequestContext context = null;
		
		/**
		 * Constructor creates new client worker with given socket.
		 * 
		 * @param csocket socket to use for this worker
		 */
		public ClientWorker(Socket csocket) {
			this.csocket = csocket;
		}
		
		@Override
		public void run() {
			try {
				istream = new BufferedInputStream(csocket.getInputStream());
				ostream = new BufferedOutputStream(csocket.getOutputStream());
				
				List<String> headers = RequestProcessingUtil.extractHeaders(istream);
				if(headers.isEmpty()) {
					sendEmptyResponse(400, "Bad request");
				}
				
				String[] firstLine = headers.get(0).split(" ");
				if(firstLine.length != 3) {
					sendEmptyResponse(400, "Bad request");
					return;
				}

				method = firstLine[0].toUpperCase();				
				version = firstLine[2].toUpperCase();
				if(!method.equals("GET") || (!version.equals("HTTP/1.1") && !version.equals("HTTP/1.1"))) {
					sendEmptyResponse(400, "Bad request");
					return;
				}
				host = RequestProcessingUtil.extractHost(headers, domainName);
				
				checkSession(headers);
				outputCookies.add(new RCCookie("sid", SID, null, host, "/", true));
				
				String[] pathParams = firstLine[1].split("\\?");
				String path = pathParams[0];
				if(pathParams.length != 1) {
					RequestProcessingUtil.fillParameters(params, pathParams[1]);
				}
				
				internalDispatchRequest(path, true);
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println("Error: " + e.getMessage());
			} finally {
				try {
					csocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void dispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);
		}
		
		/**
		 * Method analyzes the given path and determines how to process it.
		 * 
		 * @param urlPath path to process
		 * @param directCall <code>true</code> if call is direct, otherwise <code>false</code>
		 * @throws Exception if error occurs while processing path
		 */
		private void internalDispatchRequest(String urlPath, boolean directCall) throws Exception {
			Path reqPath = documentRoot.resolve(urlPath.substring(1)).toAbsolutePath().normalize();
			if(!reqPath.startsWith(documentRoot)) {
				sendEmptyResponse(403, "Forbidden");
				return;
			}
			
			if(directCall && (urlPath.equals("/private") || urlPath.startsWith("/private/"))) {
				sendEmptyResponse(404, "Not Found");
				return;
			}
			
			if(urlPath.startsWith("/ext")) {
				String fqcn = "hr.fer.zemris.java.webserver.workers." + urlPath.substring(5);
				
				Class<?> referenceToClass = null;
				try {
					referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
				} catch (ClassNotFoundException e1) {}
				if(referenceToClass != null) {
					IWebWorker w = (IWebWorker) referenceToClass.getDeclaredConstructor().newInstance();
					setContext(200, "OK");
					w.processRequest(context);
					return;
				}
			}
			
			IWebWorker worker = workersMap.get(urlPath);
			if(worker != null) {
				setContext(200, "OK");
				worker.processRequest(context);
				return;
			}
			
			if(!Files.exists(reqPath) || !Files.isRegularFile(reqPath) || !Files.isReadable(reqPath)) {
				sendEmptyResponse(404, "Not Found");
				return;
			}
			
			String ext = RequestProcessingUtil.extractExtension(reqPath.getFileName().toString());
			String mime = mimeTypes.get(ext);
			if(mime == null) {
				mime = "application/octet-stream";
			}
			
			byte[] data = Files.readAllBytes(reqPath);
			if(ext.equals("smscr")) {
				sendSmartScriptResponse(200, "OK", mime, data);
				return;
			}
			
			sendResponseWithData(200, "OK", mime, data);
		}
		
		/**
		 * Helper method that checks if session exists for this request, if not it creates a new one.
		 * 
		 * @param headers list of header parameters to extract session ID from
		 */
		private synchronized void checkSession(List<String> headers) {
			String sidCandidate = RequestProcessingUtil.extractSID(headers);
			SessionMapEntry entry = sessions.get(sidCandidate);
			
			if(entry == null || !entry.host.equals(host)) {
				createSession();
				return;
			}
			
			if(entry.validUntil < Instant.now().getEpochSecond()) {
				sessions.remove(sidCandidate);
				createSession();
				return;
			}
			
			entry.validUntil = Instant.now().getEpochSecond() + sessionTimeout;	
			perParams = entry.map;
			SID = sidCandidate;
		}
		
		/**
		 * Helper function that creates new session.
		 */
		private void createSession() {
			SID = RequestProcessingUtil.getRandomSID(sessionRandom);
			SessionMapEntry e = new SessionMapEntry();
			e.sid = SID;
			e.validUntil = Instant.now().getEpochSecond() + sessionTimeout;
			e.host = host;
			perParams = e.map;
			sessions.put(e.sid, e);
		}
		
		/**
		 * Helper method that creates new context if it doesn't exist yet and sets given parameters.
		 * 
		 * @param statusCode of the response
		 * @param statusText of the response
		 */
		private void setContext(int statusCode, String statusText) {
			context = context == null
					? new RequestContext(ostream, params, perParams, outputCookies, tempParams, this, SID)
					: context;
					
			context.setStatusCode(statusCode);
			context.setStatusText(statusText);
		}
		
		/**
		 * Creates {@link RequestContext} and using it sends response to the client.
		 * 
		 * @param statusCode of the response
		 * @param statusText of the response
		 * @param contentType of the response
		 * @param data to send to client
		 * @throws IOException if there is an error while writing data to output stream
		 */
		public void sendResponseWithData(int statusCode, String statusText, String contentType, byte[] data) throws IOException {
			setContext(statusCode, statusText);
			context.setMimeType(contentType);
			context.setContentLength((long) data.length);
			context.write(data);
		}
		
		/**
		 * Sends empty response to client - response with no body, only header.
		 * 
		 * @param statusCode of the response
		 * @param statusText of the response
		 * @throws IOException if there is an error while writing data to output stream
		 */
		public void sendEmptyResponse(int statusCode, String statusText) throws IOException {
			sendResponseWithData(statusCode, statusText, "text/plain;charset=UTF-8", new byte[0]);
		}
		
		/**
		 * Executes wanted SmartScript document and sends response to client.
		 * 
		 * @param statusCode of the response
		 * @param statusText of the response
		 * @param contentType of the response
		 * @param data smart script to execute
		 * @throws IOException if there is an error while writing data to output stream
		 */
		public void sendSmartScriptResponse(int statusCode, String statusText, String contentType, byte[] data) {
			setContext(statusCode, statusText);
			context.setMimeType(contentType);
			String docBody = new String(data, StandardCharsets.UTF_8);
			
			new SmartScriptEngine(new SmartScriptParser(docBody).getDocumentNode(), context).execute();
		}

	}
	
	/**
	 * Class models session descriptor.
	 * 
	 * @author Ana Bagić
	 *
	 */
	private static class SessionMapEntry {
		/** Session ID. */
		String sid;
		/** Domain name of the session. */
		String host;
		/** Time until session is not valid. */
		long validUntil;
		/** Map of session parameters. */
		Map<String,String> map = new ConcurrentHashMap<>();
	}

}
