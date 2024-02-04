package oprpp2.hw02.messages;

/**
 * Apstraktni razred predstavlja poruku razmjenjenu između klijenta i poslužitelja chat aplikacije.
 * 
 * @author Ana Bagić
 *
 */
public abstract class Message {
	
	/** Redni broj poruke */
	protected long number;
	
	/**
	 * Stvara novu poruku i dodjeljuje joj redni broj.
	 * 
	 * @param number redni broj poruke
	 */
	public Message(long number) {
		this.number = number;
	}
	
	/**
	 * Defaultni konstruktor.
	 */
	public Message() {
	}

	/**
	 * @return redni broj poruke
	 */
	public long getNumber() {
		return number;
	}
	
	/**
	 * Pakira poruku u byte polje spremno za slanje.
	 * 
	 * @return  poruka zapakirana u byte polje
	 */
	abstract public byte[] pack();

}
