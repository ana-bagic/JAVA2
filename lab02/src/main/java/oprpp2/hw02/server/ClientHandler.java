package oprpp2.hw02.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import oprpp2.hw02.messages.AckMessage;
import oprpp2.hw02.messages.Message;

/**
 * Razred modelira posao koji obavlja pomoćna dretva koja se bavi upravljanjem poruka korisnika.
 * 
 * @author Ana Bagić
 *
 */
public class ClientHandler implements Runnable {

	/** Socket s kojeg se šalju poruke. */
	private DatagramSocket socket;
	/** Korisnik za koga radi ovaj posao. */
	private User user;
	
	/**
	 * Konstruktor stvara novi posao zadavanjem socketa i korisnika.
	 * 
	 * @param socket socket s kojega se šalju poruke
	 * @param user korisnika za koga radi ovaj posao
	 */
	public ClientHandler(DatagramSocket socket, User user) {
		this.socket = socket;
		this.user = user;
	}
	
	@Override
	public void run() {
		while(true) {
			Message m = user.nextMessage();
			byte[] buf = m.pack();
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			packet.setAddress(user.getAddress());
			packet.setPort(user.getPort());
			
			for(int r = 0; r < 10; r++) {
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				AckMessage ack = user.nextAckMessage();
				if(ack != null && ack.getNumber() == m.getNumber()) break;
			}
		}
	}
	
}
