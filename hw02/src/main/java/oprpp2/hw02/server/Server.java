package oprpp2.hw02.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

import oprpp2.hw02.messages.AckMessage;
import oprpp2.hw02.messages.ByeMessage;
import oprpp2.hw02.messages.HelloMessage;
import oprpp2.hw02.messages.InMessage;
import oprpp2.hw02.messages.OutMessage;
import oprpp2.hw02.util.MessageUtil;

/**
 * Razred modelira poslužitelja chat aplikacije.
 * 
 * @author Ana Bagić
 *
 */
public class Server {
	
	/**
	 * Pokretanje servera.
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Potreban je jedan argument: port poslužitelja.");
			return;
		}
		
		int port;
		try {
			port = Integer.parseInt(args[0]);
		} catch (Exception e) {
			System.out.println("Argument nije broj: " + args[0]);
			return;
		}
		
		if(port < 1 || port > 65536) {
			System.out.println("Port mora biti između 1 i 65536.");
			return;
		}
		
		DatagramSocket socket;
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			System.out.println("Nije moguće otvoriti pristupnu točku. Detaljnija poruka: " + e.getMessage());
			return;
		}
		
		System.out.println("Veza uspješno otvorena na portu: " + port);
		
		long nextUID = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
		Map<Long, User> clients = new HashMap<>();
		
		int failCounter = 0;
		while(failCounter < 10) {
			byte[] buf = new byte[4000];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			
			try {
				socket.receive(packet);
			} catch (IOException e) {
				failCounter++;
				continue;
			}
			failCounter = 0;
			
			byte[] data = packet.getData();
			System.out.println("Dobio sam paket od: " + packet.getSocketAddress() + ", s porukom: " + data[0]);
			switch (data[0]) {
			case 1: {
				HelloMessage m = new HelloMessage(data);
				User u = new User(packet.getAddress(), packet.getPort(), m.getKey(), m.getUsername());
				
				for (Entry<Long, User> entry : clients.entrySet()) {
					if (entry.getValue().equals(u)) {
						MessageUtil.sendAck(m.getNumber(), entry.getKey(), packet.getSocketAddress(), socket);
						break;
					}
				}
				
				u.setUID(nextUID);
				Thread thread = new Thread(new ClientHandler(socket, u));
				u.setThread(thread);
				clients.put(nextUID++, u);
				thread.start();
				
				MessageUtil.sendAck(m.getNumber(), u.getUID(), packet.getSocketAddress(), socket);

				break;
			}
			case 2: {
				AckMessage m = new AckMessage(data);
				User u = clients.get(m.getUID());
				u.addAckMessage(m);
				
				break;
			}
			case 3: {
				ByeMessage m = new ByeMessage(data);
				User u = clients.get(m.getUID());
				u.getThread().interrupt();
				clients.remove(m.getUID());
				
				MessageUtil.sendAck(m.getNumber(), m.getUID(), packet.getSocketAddress(), socket);
				
				break;
			}
			case 4: {
				OutMessage m = new OutMessage(data);
				User sender = clients.get(m.getUID());
				
				for(User u : clients.values()) {
					u.addMessage(new InMessage(u.getNextNumber(), sender.getUsername(), m.getText()));
				}
				
				MessageUtil.sendAck(m.getNumber(), m.getUID(), packet.getSocketAddress(), socket);
			}
			default: // zanemari
			}
		}
		
		socket.close();
	}

}
