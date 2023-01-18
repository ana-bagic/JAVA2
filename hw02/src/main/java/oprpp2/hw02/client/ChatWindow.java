package oprpp2.hw02.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import oprpp2.hw02.messages.AckMessage;
import oprpp2.hw02.messages.InMessage;
import oprpp2.hw02.messages.OutMessage;
import oprpp2.hw02.server.User;
import oprpp2.hw02.util.MessageUtil;

/**
 * Razred modelira prozor koji prikazuje chat aplikaciju.
 * 
 * @author Ana Bagić
 *
 */
public class ChatWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	
	/** User koji koristi ovaj chat. */
	private User user;
	/** Socket sa kojega se šalju poruke. */
	private DatagramSocket socket;
	/** TextArea za prikaz poruka. */
	private JTextArea textArea;
	
	/**
	 * Konstruktor stvara novi objekt razreda Client.
	 */
	public ChatWindow(User user, DatagramSocket socket) {
		this.user = user;
		this.socket = socket;
		
		addWindowListener(new DefaultWindowListener());
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(600, 400);
		setTitle("Chat client: " + user.getUsername());
		initGUI();
		
		Thread thread = new Thread(new Displayer(textArea, user));
		thread.start();
	}

	/**
	 * Pomoćna funkcija za postavljanje korisničkog sučelja.
	 */
	private void initGUI() {
		textArea = new JTextArea();
		
		JTextField text = new JTextField();
		text.addActionListener((e) -> {
			user.addMessage(new OutMessage(user.getNextNumber(), user.getUID(), text.getText()));
			text.setText("");
		});
		
		JScrollPane sp = new JScrollPane(textArea);
		
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(text, BorderLayout.PAGE_START);
		cp.add(sp, BorderLayout.CENTER);
	}
	
	/**
	 * Razred modelira implementaciju {@link WindowAdapter}-a.
	 */
	private class DefaultWindowListener extends WindowAdapter {

		@Override
		public void windowClosing(WindowEvent e) {
			AckMessage ack = MessageUtil.sendHelloBye(false, user, socket);
			if(ack == null) {
				System.out.println("Veza s poslužiteljem je izgubljena ali svejedno gasim aplikaciju.");
			} else {
				System.out.println("Uspješno odjavljen s poslužitelja. Gasim aplikaciju");
			}

			dispose();
		}
	}
	
	/**
	 * Razred modelira posao dretve zadužene za display poruka.
	 */
	private class Displayer implements Runnable {

		/** TextArea u kojemu se prikazuju poruke. */
		JTextArea area;
		/** User koji dobiva poruke. */
		User user;
		
		Displayer(JTextArea area, User user) {
			this.area = area;
			this.user = user;
		}
		
		@Override
		public void run() {
			while(true) {
				DatagramPacket packet = user.nextPacketToDisplay();
				InMessage m = new InMessage(packet.getData());	
				area.append("[" + packet.getAddress() + ":" + packet.getPort() + "] ");
				area.append("Poruka od korisnika: " + m.getUsername() + "\n");
				area.append(m.getText() + "\n\n");
			}
		}
		
	}

}