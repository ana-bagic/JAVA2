package oprpp2.hw02.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.SwingUtilities;

import oprpp2.hw02.messages.AckMessage;
import oprpp2.hw02.messages.InMessage;
import oprpp2.hw02.server.ClientHandler;
import oprpp2.hw02.server.User;
import oprpp2.hw02.util.MessageUtil;

/**
 * Razred modelira korisničku stranu chat aplikacije.
 * 
 * @author Ana Bagić
 *
 */
public class Client {
	
	/**
	 * Pokretanje chat aplikacije.
	 */
	public static void main(String[] args) {
		if(args.length != 3) {
			System.out.println("Potrebna su tri argumenta: IP-adresa i port poslužitelja i ime korisnika.");
			return;
		}
		
		InetAddress address;
		int port;
		DatagramSocket socket;
		try {
			address = InetAddress.getByName(args[0]);
			port = Integer.parseInt(args[1]);
			socket = new DatagramSocket();
		} catch (UnknownHostException e) {
			System.out.println("Ne može se razrješiti zadana adresa: " + args[0]);
			return;
		} catch (NumberFormatException e) {
			System.out.println("Ne može se razrješiti zadani port: " + args[1]);
			return;
		} catch (SocketException e) {
			System.out.println("Ne mogu otvoriti pristupnu točku!");
			return;
		}
		
		String name = args[2];
		long randKey = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
		User user = new User(address, port, randKey, name);
		
		AckMessage ack = MessageUtil.sendHelloBye(true, user, socket);
		if(ack == null) {
			System.out.println("Veza s poslužiteljem nije uspjela.");
			return;
		} else {
			System.out.println("Uspješno spojen s poslužiteljem. Dobiveni UID je: " + ack.getUID());
			user.setUID(ack.getUID());
		}
		
		Thread thread = new Thread(new ClientHandler(socket, user));
		user.setThread(thread);
		thread.start();
		
		SwingUtilities.invokeLater(() -> new ChatWindow(user, socket).setVisible(true));
		
		long lastNrFromServer = -1;
		while(true) {
			byte[] buf = new byte[4000];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			
			try {
				socket.receive(packet);
			} catch (IOException e) {
				continue;
			}
			
			byte[] data = packet.getData();
			System.out.println("Dobio sam paket od: " + packet.getSocketAddress() + ", s porukom: " + data[0]);
			switch (data[0]) {
			case 2: {
				user.addAckMessage(new AckMessage(data));
				break;
			}
			case 5: {
				InMessage m1 = new InMessage(data);
				
				if(lastNrFromServer < m1.getNumber()) {
					user.addPacketToDisplay(packet);
					lastNrFromServer = m1.getNumber();
				}
				
				AckMessage m2 = new AckMessage(m1.getNumber(), user.getUID());
				byte[] buf2 = m2.pack();
				DatagramPacket packet2 = new DatagramPacket(buf2, buf2.length);
				packet2.setSocketAddress(packet.getSocketAddress());
				
				try {
					socket.send(packet2);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			default: // zanemari
			}
		}
	}
}
