package oprpp2.hw02.server;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import oprpp2.hw02.messages.AckMessage;
import oprpp2.hw02.messages.Message;

/**
 * Razred predstavlja korisnika chat aplikacije.
 * 
 * @author Ana Bagić
 *
 */
public class User {
	
	/** IP adresa računala na koje se šalju poruke za ovog korisnika. */
	private InetAddress address;
	/** Port računala. */
	private int port;
	/** Ključ korisnika. */
	private long key;
	/** User ID korisnika. */
	private long UID;
	/** Ime korisnika. */
	private String username;
	/** Pomoćna dretva koja se koristi za upravljanje korisnikovim porukama. */
	private Thread thread;
	/** Idući broj za numeriranje poruka od korisnika. */
	private long nextNumber;
	/** Red poruka koje korisnik dobiva/treba poslati. */
	private LinkedBlockingQueue<Message> messages;
	/** Red poruka potvrda korisnika. */
	private LinkedBlockingQueue<AckMessage> ackMessages;
	/** Red poruka korisnika koje se trebaju display-ati. */
	private LinkedBlockingQueue<DatagramPacket> packetToDisplay;
	
	/**
	 * Stvara novog korisnika zadavanjem IP adrese i porta računala na koje se šalju poruke za ovog korisnika, ključa i imena korisnika.
	 * 
	 * @param address IP adresa računala
	 * @param port port računala
	 * @param key ključ korisnika
	 * @param username ime korisnika
	 */
	public User(InetAddress address, int port, long key, String username) {
		this.address = address;
		this.port = port;
		this.key = key;
		this.username = username;
		this.nextNumber = 1;
		messages = new LinkedBlockingQueue<>();
		ackMessages =  new LinkedBlockingQueue<>();
		packetToDisplay = new LinkedBlockingQueue<>();
	}

	/**
	 * @return IP adresu računala na koje se treba poslati poruka
	 */
	public InetAddress getAddress() {
		return address;
	}

	/**
	 * @return port računala na koje se treba poslati poruka
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * @return ključ korisnika
	 */
	public long getKey() {
		return key;
	}
	
	/**
	 * @return ime korisnika
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return user ID korisnika
	 */
	public long getUID() {
		return UID;
	}

	/**
	 * Postavlja user ID korisnika na zadanu vrijednost.
	 * 
	 * @param UID user ID korisnika
	 */
	public void setUID(long UID) {
		this.UID = UID;
	}
	
	/**
	 * @return opisnik dretve zadužene za korisnika
	 */
	public Thread getThread() {
		return thread;
	}

	/**
	 * Postavlja opisnik dretve zadužene za korisnika na zadano.
	 * 
	 * @param thread opisnik dretve
	 */
	public void setThread(Thread thread) {
		this.thread = thread;
	}
	
	/**
	 * @return idući broj korišten za numeriranje poruka korisnika
	 */
	public long getNextNumber() {
		return nextNumber++;
	}
	
	/**
	 * Dodaje poruku u red pristiglih poruka korisnika.
	 * 
	 * @param message poruka za dodati u red
	 */
	public void addMessage(Message message) {
		while(true) {
			try {
				messages.put(message);
				return;
			} catch (InterruptedException e) {}
		}
	}
	
	/**
	 * Dohvaća poruku iz reda pristiglih poruka za korisnika.
	 * 
	 * @return poruka dohvaćena iz reda poruka
	 */
	public Message nextMessage() {
		while(true) {
			try {
				return messages.take();
			} catch (InterruptedException e) {}
		}
	}
	
	/**
	 * Dodaje poruku potvrde u red poruka potvrde korisnika.
	 * 
	 * @param message poruka potvrde za dodati u red
	 */
	public void addAckMessage(AckMessage message) {
		while(true) {
			try {
				ackMessages.put(message);
				return;
			} catch (InterruptedException e) {}
		}
	}
	
	/**
	 * Dohvaća poruku potvrde iz reda poruka potvrde korisnika, ako ne dođe u 5 sekundi vraća <code>null</code>.
	 * 
	 * @return poruka potvrde iz reda poruka potvrde ili <code>null</code>
	 */
	public AckMessage nextAckMessage() {
		try {
			return ackMessages.poll(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {}
		
		return null;
	}

	/**
	 * Dodaje poruku u red poruka korisnika koje se trebaju display-ati.
	 * 
	 * @param message poruka za dodati u red
	 */
	public void addPacketToDisplay(DatagramPacket packet) {
		while(true) {
			try {
				packetToDisplay.put(packet);
				return;
			} catch (InterruptedException e) {}
		}
	}
	
	/**
	 * Dohvaća poruku iz reda poruka za korisnika koje se trebaju display-ati.
	 * 
	 * @return poruka dohvaćena iz reda poruka
	 */
	public DatagramPacket nextPacketToDisplay() {
		while(true) {
			try {
				return packetToDisplay.take();
			} catch (InterruptedException e) {}
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + (int) (key ^ (key >>> 32));
		result = prime * result + port;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (key != other.key)
			return false;
		if (port != other.port)
			return false;
		return true;
	}

}
