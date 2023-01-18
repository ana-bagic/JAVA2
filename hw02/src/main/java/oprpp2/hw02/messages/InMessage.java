package oprpp2.hw02.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Razred predstavlja poruku koja se koristi za dobivanje poruke od ostalih sudionika chat-a.
 * 
 * @author Ana Bagić
 *
 */
public class InMessage extends Message {

	/** Kod poruke. */
	public static final byte CODE = (byte) 5;
	
	/** Ime pošiljatelja poruke. */
	private String username;
	/** Tekst poruke. */
	private String text;

	
	/**
	 * Kostruktor stvara novu poruku zadavanjem rednog broja, imena pošiljatelja i teksta poruke.
	 * 
	 * @param number redni broj poruke
	 * @param username ime pošiljatelja poruke
	 * @param text tekst poruke
	 */
	public InMessage(long number, String username, String text) {
		super(number);
		this.username = username;
		this.text = text;
	}
	
	/**
	 * Konstruktor stvara novu poruku iz primljenog polja byteova. 
	 * 
	 * @param message polje byteova
	 */
	public InMessage(byte[] message) {
		ByteArrayInputStream bis = new ByteArrayInputStream(message);
		DataInputStream dis = new DataInputStream(bis);
		try {
			dis.readByte();
			number = dis.readLong();
			username = dis.readUTF();
			text = dis.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return ime pošiljatelja poruke
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * @return tekst poruke
	 */
	public String getText() {
		return text;
	}

	@Override
	public byte[] pack() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeByte(CODE);
			dos.writeLong(number);
			dos.writeUTF(username);
			dos.writeUTF(text);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}
}
