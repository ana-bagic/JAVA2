package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Class models a context for the HTTP request.
 * After configuring header, it will be used to write data to {@link OutputStream}.
 * 
 * @author Ana Bagić
 *
 */
public class RequestContext {
	
	/** OutputStream to which the data should be written to.  */
	private OutputStream outputStream;
	/** Charset used to encode data. */
	private Charset charset;
	/** Encoding used is request. */
	public String encoding = "UTF-8";
	/** Status code of the request. */
	public int statusCode = 200;
	/** Status text of the request. */
	public String statusText = "OK";
	/** Mime type the the request. */
	public String mimeType = "text/html";
	/** Length of the request content. */
	public Long contentLength = null;
	/** Parameters of the request. */
	private Map<String, String> parameters;
	/** Temporary parameters of the request. */
	private Map<String, String> temporaryParameters = new HashMap<>();
	/** Persistent parameters of the request. */
	private Map<String, String> persistentParameters;
	/** List of output cookies used in request. */
	private List<RCCookie> outputCookies;
	/** Flag showing if the header is already generated. */
	private boolean headerGenerated = false;
	/** Dispatcher for the context. */
	private IDispatcher dispatcher;
	/** Session ID. */
	private String SID;
	
	/**
	 * Constructor creates a new instance of this class using given parameters.
	 * 
	 * @param outputStream output stream to which the data should be written to
	 * @param parameters parameters of the request
	 * @param persistentParameters persistent parameters of the request
	 * @param outputCookies list of output cookies of the request
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
				Map<String, String> persistentParameters, List<RCCookie> outputCookies) {
		this.outputStream = Objects.requireNonNull(outputStream, "OutputStream cannot be null");
		this.parameters = parameters == null ? new HashMap<>() : parameters;
		this.persistentParameters = persistentParameters == null ? new HashMap<>() : persistentParameters;
		this.outputCookies = outputCookies == null ? new LinkedList<>() : outputCookies;
	}

	/**
	 * Constructor creates a new instance of this class using given parameters.
	 * 
	 * @param outputStream output stream to which the data should be written to
	 * @param parameters parameters of the request
	 * @param persistentParameters persistent parameters of the request
	 * @param outputCookies list of output cookies of the request
	 * @param temporaryParameters temporary parameters of the request
	 * @param dispatcher dispatcher for the request
	 * @param SID session ID
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters, Map<String, String> persistentParameters, 
			List<RCCookie> outputCookies, Map<String, String> temporaryParameters, IDispatcher dispatcher, String SID) {
		this(outputStream, parameters, persistentParameters, outputCookies);
		this.temporaryParameters = temporaryParameters;
		this.dispatcher = dispatcher;
		this.SID = SID;
	}
	
	/**
	 * Sets the encoding to given.
	 * 
	 * @param encoding to set to
	 * @throws RuntimeException if the header is already generated
	 */
	public void setEncoding(String encoding) {
		if(headerGenerated)
			throw new RuntimeException("Header is already generated!");
		
		this.encoding = encoding;
	}

	/**
	 * Sets the status code to given.
	 * 
	 * @param statusCode to set to
	 * @throws RuntimeException if the header is already generated
	 */
	public void setStatusCode(int statusCode) {
		if(headerGenerated)
			throw new RuntimeException("Header is already generated!");
		
		this.statusCode = statusCode;
	}

	/**
	 * Sets the status text to given.
	 * 
	 * @param statusText to set to
	 * @throws RuntimeException if the header is already generated
	 */
	public void setStatusText(String statusText) {
		if(headerGenerated)
			throw new RuntimeException("Header is already generated!");
		
		this.statusText = statusText;
	}
	
	/**
	 * Sets the mime type to given.
	 * 
	 * @param mimeType to set to
	 * @throws RuntimeException if the header is already generated
	 */
	public void setMimeType(String mimeType) {
		if(headerGenerated)
			throw new RuntimeException("Header is already generated!");
		
		this.mimeType = mimeType;
	}

	/**
	 * Sets the content length to given.
	 * 
	 * @param contentLength to set to
	 * @throws RuntimeException if the header is already generated
	 */
	public void setContentLength(Long contentLength) {
		if(headerGenerated)
			throw new RuntimeException("Header is already generated!");
		
		this.contentLength = contentLength;
	}

	/**
	 * Gets the parameter value for the given name.
	 * If such value does not exist, the method returns <code>null</code>.
	 * 
	 * @param name of the value
	 * @return value for the given name
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}
	
	/**
	 * @return names of all the parameters as a read-only set
	 */
	public Set<String> getParameterNames() {
		return Collections.unmodifiableSet(parameters.keySet());
	}
	
	/**
	 * Gets the persistent parameter value for the given name.
	 * If such value does not exist, the method returns <code>null</code>.
	 * 
	 * @param name of the value
	 * @return value for the given name
	 */
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}
	
	/**
	 * @return names of all the persistent parameters as a read-only set
	 */
	public Set<String> getPersistentParameterNames() {
		return Collections.unmodifiableSet(persistentParameters.keySet());
	}
	
	/**
	 * Stores a persistent parameter of a given name to the given value.
	 * 
	 * @param name of the parameter to be stored
	 * @param value of the parameter to be stored
	 */
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}
	
	/**
	 * Removes a persistent parameter with a given name.
	 * 
	 * @param name of the parameter to be removed
	 */
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}
	
	/**
	 * Gets the temporary parameter value for the given name.
	 * If such value does not exist, the method returns <code>null</code>.
	 * 
	 * @param name of the value
	 * @return value for the given name
	 */
	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}
	
	/**
	 * @return names of all the temporary parameters as a read-only set
	 */
	public Set<String> getTemporaryParameterNames() {
		return Collections.unmodifiableSet(temporaryParameters.keySet());
	}
	
	/**
	 * @return the dispatcher
	 */
	public IDispatcher getDispatcher() {
		return dispatcher;
	}
	
	/**
	 * Stores a temporary parameter of a given name to the given value.
	 * 
	 * @param name of the parameter to be stored
	 * @param value of the parameter to be stored
	 */
	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(name, value);
	}
	
	/**
	 * Removes a temporary parameter with a given name.
	 * 
	 * @param name of the parameter to be removed
	 */
	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}
	
	/**
	 * Adds given cookie to the list of output cookies.
	 * 
	 * @param cookie to be added
	 */
	public void addRCCookie(RCCookie cookie) {
		outputCookies.add(cookie);
	}
	
	/**
	 * @return current user session ID
	 */
	public String getSessionID() {
		return SID;
	}
	
	/**
	 * Writes given bytes to the output stream.
	 * 
	 * @param data data which should be written
	 * @return request context
	 * @throws IOException if exception occurs while writing data to stream
	 */
	public RequestContext write(byte[] data) throws IOException {
		return write(data, 0, data.length);
	}
	
	/**
	 * Writes len number of bytes to the output stream starting from the position offset.
	 * 
	 * @param data data which should be written
	 * @param offset starting position from which the data should be written
	 * @param len number of bytes that should be written from starting position
	 * @return request context
	 * @throws IOException if exception occurs while writing data to stream
	 */
	public RequestContext write(byte[] data, int offset, int len) throws IOException {
		checkForHeader();
		
		outputStream.write(data);
		outputStream.flush();
		
		return this;
	}
	
	/**
	 * Writes the text to the output stream using charset configured with encoding parameter.
	 * 
	 * @param text text that should be written
	 * @return request context
	 * @throws IOException if exception occurs while writing data to stream
	 */
	public RequestContext write(String text) throws IOException {
		charset = Charset.forName(encoding);
		byte[] data = text.getBytes(charset);
		return write(data, 0, data.length);
	}
	
	/**
	 * Helper method checks if the header is generated, and if not creates a new one.
	 * 
	 * @throws IOException if exception occurs while writing data to stream
	 */
	private void checkForHeader() throws IOException {
		if(headerGenerated) return;
		
		mimeType += mimeType.startsWith("text/") ? ("; charset=" + encoding) : "";
		
		StringBuilder sb = new StringBuilder();
		sb.append("HTTP/1.1 ").append(statusCode).append(" ").append(statusText).append("\r\n");
		sb.append("Content-Type: ").append(mimeType).append("\r\n");
		if(contentLength != null) {
			sb.append("Content-Length: ").append(contentLength).append("\r\n");
		}
		for(RCCookie c : outputCookies) {
			sb.append("Set-Cookie: ").append(c.getName()).append("=\"").append(c.getValue()).append("\"");
			if(c.getDomain() != null) {
				sb.append("; Domain=").append(c.getDomain());
			}
			if(c.getPath() != null) {
				sb.append("; Path=").append(c.getPath());
			}
			if(c.getMaxAge() != null) {
				sb.append("; Max-Age=").append(c.getMaxAge());
			}
			if(c.isHttpOnly()) {
				sb.append("; HttpOnly");
			}
			sb.append("\r\n");
		}
		sb.append("\r\n");
		
		outputStream.write((sb.toString()).getBytes(StandardCharsets.ISO_8859_1));
		
		headerGenerated = true;
	}

	/**
	 * Class represents a cookie used in a request.
	 * 
	 * @author Ana Bagić
	 *
	 */
	public static class RCCookie {
		/** Name of the property. */
		private String name;
		/** Value of the property. */
		private String value;
		/** Domain to which the cookie will be sent.  */
		private String domain;
		/** Path to resource for the cookie.  */
		private String path;
		/** Max time for what the cookie should be valid for. */
		private Integer maxAge;
		/** Flags is cookie is HttpOnly. */
		private boolean httpOnly;
		
		/**
		 * Constructor creates a new cookie using the given parameters.
		 * 
		 * @param name name of the property
		 * @param value value of the property
		 * @param maxAge max time for what the cookie should be valid for
		 * @param domain domain to which the cookie will be sent
		 * @param path path to resourse for the cookie
		 * @param httpOnly flags if cookie is HttpOnly
		 */
		public RCCookie(String name, String value, Integer maxAge, String domain, String path, boolean httpOnly) {
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
			this.httpOnly = httpOnly;
		}
		
		/**
		 * @return name of the property
		 */
		public String getName() {
			return name;
		}
		/**
		 * @return value of the property
		 */
		public String getValue() {
			return value;
		}
		/**
		 * @return domain to which the cookie will be sent
		 */
		public String getDomain() {
			return domain;
		}
		/**
		 * @return path to resource for the cookie
		 */
		public String getPath() {
			return path;
		}
		/**
		 * @return max time for what the cookie should be valid for
		 */
		public Integer getMaxAge() {
			return maxAge;
		}
		
		/**
		 * @return <code>true</code> if cookie is HttpOnly, otherwise <code>false</code>
		 */
		public boolean isHttpOnly() {
			return httpOnly;
		}
	}

}
