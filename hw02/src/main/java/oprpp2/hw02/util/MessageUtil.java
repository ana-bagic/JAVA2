package oprpp2.hw02.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import oprpp2.hw02.messages.AckMessage;
import oprpp2.hw02.messages.ByeMessage;
import oprpp2.hw02.messages.HelloMessage;
import oprpp2.hw02.messages.Message;
import oprpp2.hw02.server.User;

/**
 * Pomoćni razred za slanje poruka s klijenta na poslužitelj i obrnuto.
 * 
 * @author Ana Bagić
 *
 */
public class MessageUtil {
	
	/**
	 * Metoda šalje poruku potvrde na zadanu adresu.
	 * 
	 * @param number redni broj poruke
	 * @param UID user ID primatelja
	 * @param address adresa na koju treba poslati poruku
	 * @param socket socket s kojeg se šalje poruka
	 */
	public static void sendAck(long number, long UID, SocketAddress address, DatagramSocket socket) {
		AckMessage m = new AckMessage(number, UID);
		byte[] buf = m.pack();
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		packet.setSocketAddress(address);
		
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Šalje i čeka odgovor za hello odnosno bye poruku prema poslužitelju.
	 * 
	 * @param hello <code>true</code> ako se želi poslati hello poruka, <code>false</code> ako se želi posalti bye poruka
	 * @param user user za kojega se šalje poruka
	 * @param socket socket na koji se šalje poruka
	 * @return poruka potvrde
	 */
	public static AckMessage sendHelloBye(boolean hello, User user, DatagramSocket socket) {
		Message m;
		if(hello) {
			m = new HelloMessage(0, user.getUsername(), user.getKey());
		} else {
			m = new ByeMessage(user.getNextNumber(), user.getUID());
		}
		
		byte[] buf = m.pack();
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		packet.setAddress(user.getAddress());
		packet.setPort(user.getPort());
		
		for(int r = 0; r < 10; r++) {
			try {
				socket.send(packet);
			} catch (IOException e) {
				return null;
			}
			
			AckMessage ack = null;
			if(hello) {
				byte[] buf2 = new byte[50];
				DatagramPacket packet2 = new DatagramPacket(buf2, buf2.length);
				
				try {
					socket.setSoTimeout(5000);
				} catch (SocketException e) {}
				
				try {
					socket.receive(packet2);
				} catch (SocketTimeoutException e) {
					continue;
				} catch (IOException e) {
					continue;
				}
				
				ack = new AckMessage(packet2.getData());
			} else {
				ack = user.nextAckMessage();
			}
			
			if(ack != null && ack.getNumber() == m.getNumber()) {
				return ack;
			}
		}
	
		socket.close();
		return null;
	}

}
