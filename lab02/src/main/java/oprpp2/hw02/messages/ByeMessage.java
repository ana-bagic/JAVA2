package oprpp2.hw02.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Razred predstavlja poruku koja se koristi za prekid rada s poslužiteljem.
 * 
 * @author Ana Bagić
 *
 */
public class ByeMessage extends Message {

	/** Kod poruke. */
	public static final byte CODE = (byte) 3;
	
	/** User ID pošiljatelja poruke. */
	private long UID;

	/**
	 * Konstruktor stvara novu poruku zadavanjem rednog broja i user ID-a.
	 * 
	 * @param number redni broj poruke
	 * @param UID user ID pošiljatelja poruke
	 */
	public ByeMessage(long number, long UID) {
		super(number);
		this.UID = UID;
	}
	
	/**
	 * Konstruktor stvara novu poruku iz primljenog polja byteova. 
	 * 
	 * @param message polje byteova
	 */
	public ByeMessage(byte[] message) {
		ByteArrayInputStream bis = new ByteArrayInputStream(message);
		DataInputStream dis = new DataInputStream(bis);
		try {
			dis.readByte();
			number = dis.readLong();
			UID = dis.readLong();
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

	@Override
	public byte[] pack() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeByte(CODE);
			dos.writeLong(number);
			dos.writeLong(UID);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}
}
