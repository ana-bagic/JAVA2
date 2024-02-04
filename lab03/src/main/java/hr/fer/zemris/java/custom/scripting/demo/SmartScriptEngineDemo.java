package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Class tests the functionality of class {@link SmartScriptEngine}.
 * 
 * @author Ana Bagić
 *
 */
public class SmartScriptEngineDemo {

	public static void main(String[] args) {
		script1();
		script2();
		script3();
		script4();
		script5();
	}
	
	/**
	 * Tests script 1.
	 */
	private static void script1() {
		String docBody = null;
		try {
			docBody = new String(Files.readAllBytes(Paths.get("webroot/scripts/osnovni.smscr")), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Map<String,String> parameters = new HashMap<String, String>();
		Map<String,String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		
		new SmartScriptEngine(
				new SmartScriptParser(docBody).getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters, cookies)
		).execute();
	}
	
	/**
	 * Tests script 2.
	 */
	private static void script2() {
		String docBody = null;
		try {
			docBody = new String(Files.readAllBytes(Paths.get("webroot/scripts/zbrajanje.smscr")), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Map<String,String> parameters = new HashMap<String, String>();
		Map<String,String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		parameters.put("a", "4");
		parameters.put("b", "2");

		new SmartScriptEngine(
				new SmartScriptParser(docBody).getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters, cookies)
		).execute();
	}
	
	/**
	 * Tests script 3.
	 */
	private static void script3() {
		String docBody = null;
		try {
			docBody = new String(Files.readAllBytes(Paths.get("webroot/scripts/brojPoziva.smscr")), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Map<String,String> parameters = new HashMap<String, String>();
		Map<String,String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		persistentParameters.put("brojPoziva", "3");
		
		RequestContext rc = new RequestContext(System.out, parameters, persistentParameters, cookies);
		new SmartScriptEngine(new SmartScriptParser(docBody).getDocumentNode(), rc).execute();
		
		System.out.println("Vrijednost u mapi: " + rc.getPersistentParameter("brojPoziva"));

	}
	
	/**
	 * Tests script 4.
	 */
	private static void script4() {
		String docBody = null;
		try {
			docBody = new String(Files.readAllBytes(Paths.get("webroot/scripts/fibonacci.smscr")), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Map<String,String> parameters = new HashMap<String, String>();
		Map<String,String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		
		new SmartScriptEngine(
				new SmartScriptParser(docBody).getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters, cookies)
		).execute();
	}
	
	/**
	 * Tests script 5.
	 */
	private static void script5() {
		String docBody = null;
		try {
			docBody = new String(Files.readAllBytes(Paths.get("webroot/scripts/fibonaccih.smscr")), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Map<String,String> parameters = new HashMap<String, String>();
		Map<String,String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();

		new SmartScriptEngine(
				new SmartScriptParser(docBody).getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters, cookies)
		).execute();
	}
}
