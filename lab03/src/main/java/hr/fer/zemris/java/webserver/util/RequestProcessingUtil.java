package hr.fer.zemris.java.webserver.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

/**
 * Class with helper methods to process client requests.
 * 
 * @author Ana Bagić
 *
 */
public class RequestProcessingUtil {
	
	/**
	 * Extracts a list of header lines from given {@link InputStream} considering multiline parameters.
	 * 
	 * @param is {@link InputStream} to read the header from
	 * @return a list of header lines
	 * @throws IOException if there is an error while reading from {@link InputStream}
	 */
	public static List<String> extractHeaders(InputStream is) throws IOException {
		List<String> headers = new ArrayList<>();
		Optional<byte[]> request = readRequest(is);
		
		if(!request.isEmpty()) {
			String requestHeader = new String(request.get(), StandardCharsets.US_ASCII);
			
			String currentLine = null;
			for(String s : requestHeader.split("\n")) {
				if(s.isEmpty()) break;
				
				char c = s.charAt(0);
				// space or tab
				if(c == 9 || c == 32) {
					currentLine += s;
				} else {
					if(currentLine != null) {
						headers.add(currentLine);
					}
					currentLine = s;
				}
			}
			
			if(!currentLine.isEmpty())
				headers.add(currentLine);
		}
		
		return headers;
	}
	
	/**
	 * Method extracts host name from list of header parameters.
	 * 
	 * @param headers list of header parameters
	 * @param domainName default host name if one is not found in headers list
	 * @return host name if found in headers list, otherwise given domainName
	 */
	public static String extractHost(List<String> headers, String domainName) {
		for(String s : headers) {
			if(s.toUpperCase().startsWith("HOST:") && s.length() > 5) {
				return s.split(":")[1].trim();
			}
		}
		return domainName;
	}
	
	/**
	 * Method extracts extension of the given file name.
	 * 
	 * @param string file name to extract the extension from
	 * @return extension of the given file, or empty string if there is no extension
	 */
	public static String extractExtension(String string) {
		int p = string.lastIndexOf('.');
		if(p < 1) return "";
		return string.substring(p + 1).toLowerCase();
	}
	
	/**
	 * Method reads header from {@link InputStream} - all bytes before blank line.
	 * 
	 * @param is {@link InputStream} used to read data from
	 * @return byte array of header if header was found, otherwise empty {@link Optional}
	 * @throws IOException if header is incomplete
	 */
	private static Optional<byte[]> readRequest(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int state = 0;
		
l:			while(true) {
			int b = is.read();
			if(b == -1) {
				if(bos.size() != 0)
					throw new IOException("Incomplete header received.");
				return Optional.empty();
			}
			
			if(b != 13) bos.write(b);

			switch(state) {
			case 0: 
				if(b==13) { state=1; } else if(b==10) state=4;
				break;
			case 1: 
				if(b==10) { state=2; } else state=0;
				break;
			case 2: 
				if(b==13) { state=3; } else state=0;
				break;
			case 3: 
				if(b==10) { break l; } else state=0;
				break;
			case 4: 
				if(b==10) { break l; } else state=0;
				break;
			}
		}
		return Optional.of(bos.toByteArray());
	}
	
	/**
	 * Fills parameter map with names and values extracted from given query string.
	 * 
	 * @param parameterMap map to put parameters into
	 * @param queryString string containing pars of parameters
	 */
	public static void fillParameters(Map<String, String> parameterMap, String queryString) {
		String[] pairs = queryString.split("[&]");
		
		for(String pair : pairs) {
			String[] parts = pair.split("[=]", 2);
			parameterMap.put(parts[0], parts.length == 2 ? parts[1] : null);
		}
	}
	
	/**
	 * Generates random string of 20 uppercase letters using given randomizer.
	 * 
	 * @param random randomizer used to generate string
	 * @return random string of 20 uppercase letters
	 */
	public static String getRandomSID(Random random) {
		return random.ints(65, 91).limit(20)
			      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
			      .toString();
	}
	
	/**
	 * Method extracts SID from list of header parameters.
	 * 
	 * @param headers list of header parameters
	 * @return extracted SID, or <code>null</code> if SID is not found
	 */
	public static String extractSID(List<String> headers) {
		for(String s : headers) {
			if(s.toUpperCase().startsWith("COOKIE:") && s.length() > 7) {
				for(String c : s.substring(7).trim().split(";")) {
					String [] cookie = c.split("=");
					if(cookie[0].toUpperCase().equals("SID"))
						return cookie[1].substring(1, cookie[1].length() - 1);
				}
			}
		}
		return null;
	}

}
