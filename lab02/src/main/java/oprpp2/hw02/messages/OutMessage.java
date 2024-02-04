package oprpp2.hw02.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Razred predstavlja poruku koja se koristi za slanje poruke ostalim sudionicima chat-a.
 * 
 * @author Ana Bagić
 *
 */
public class OutMessage extends Message {

	/** Kod poruke. */
	public static final byte CODE = (byte) 4;
	
	/** User ID pošiljatelja poruke. */
	private long UID;
	/** Tekst poruke. */
	private String text;

	/**
	 * Kostruktor stvara novu poruku zadavanjem rednog broja, user ID-a pošiljatelja i teksta poruke.
	 * 
	 * @param number redni broj poruke
	 * @param UID user ID pošiljatelja poruke
	 * @param text tekst poruke
	 */
	public OutMessage(long number, long UID, String text) {
		super(number);
		this.UID = UID;
		this.text = text;
	}
	
	/**
	 * Konstruktor stvara novu poruku iz primljenog polja byteova. 
	 * 
	 * @param message polje byteova
	 */
	public OutMessage(byte[] message) {
		ByteArrayInputStream bis = new ByteArrayInputStream(message);
		DataInputStream dis = new DataInputStream(bis);
		try {
			dis.readByte();
			number = dis.readLong();
			UID = dis.readLong();
			text = dis.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return user ID pošiljatelja poruke
	 */
	public long getUID() {
		return UID;
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
			dos.writeLong(UID);
			dos.writeUTF(text);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}
}
