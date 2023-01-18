package oprpp2.hw02.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Razred predstavlja poruku koja se koristi za potvrditi primitak ostalih poruka.
 * 
 * @author Ana Bagić
 *
 */
public class AckMessage extends Message {
	
	/** Kod poruke. */
	public static final byte CODE = (byte) 2;
	
	/** User ID primatelja poruke. */
	private long UID;

	/**
	 * Konstruktor stvara novu poruku zadavanjem rednog broja i user ID-a.
	 * 
	 * @param number redni broj poruke
	 * @param UID user ID primatelja poruke
	 */
	public AckMessage(long number, long UID) {
		super(number);
		this.UID = UID;
	}
	
	/**
	 * Konstruktor stvara novu poruku iz primljenog polja byteova. 
	 * 
	 * @param message polje byteova
	 */
	public AckMessage(byte[] message) {
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
	 * @return user ID primatelja poruke
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
