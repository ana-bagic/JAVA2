package oprpp2.hw02.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Razred predstavlja poruku koja se koristi za uspostavljanje veze s poslužiteljem.
 * 
 * @author Ana Bagić
 *
 */
public class HelloMessage extends Message {
	
	/** Kod poruke. */
	public static final byte CODE = (byte) 1;
	
	/** Ime pošiljatelja poruke. */
	private String username;
	/** Ključ pošiljatelja poruke. */
	private long key;

	/**
	 * Konstruktor stvara novu poruku zadavanjem rednog broja, imena i ključa pošiljatelja.
	 * 
	 * @param number redni broj poruke
	 * @param username ime pošiljatelja poruke
	 * @param key ključ pošiljatelja poruke
	 */
	public HelloMessage(long number, String username, long key) {
		super(number);
		this.username = username;
		this.key = key;
	}
	
	/**
	 * Konstruktor stvara novu poruku iz primljenog polja byteova. 
	 * 
	 * @param message polje byteova
	 */
	public HelloMessage(byte[] message) {
		ByteArrayInputStream bis = new ByteArrayInputStream(message);
		DataInputStream dis = new DataInputStream(bis);
		try {
			dis.readByte();
			number = dis.readLong();
			username = dis.readUTF();
			key = dis.readLong();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return ime pošiljatelja
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return ključ pošiljatelja
	 */
	public long getKey() {
		return key;
	}

	@Override
	public byte[] pack() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeByte(CODE);
			dos.writeLong(number);
			dos.writeUTF(username);
			dos.writeLong(key);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}
	
}
