package Client;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.DefaultListModel;

import java.awt.Dimension;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JPopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JList;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.DefaultComboBoxModel;

public class ClientInterface extends JFrame {

	private JPanel mainPage;
	private JTextField ourMessage;
	private JTextField username;
	private Client ourClient;
	private boolean processing;
	private JLabel userStatus;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientInterface userInterface = new ClientInterface();
					userInterface.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientInterface() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				if(userStatus.getText() == "Status: online") {
				ourClient.setOffline(username.getText());
				}
			}
		});
		setTitle("Miroshnik");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(150, 150, 750, 500);
		mainPage = new JPanel();
		mainPage.setBorder(new EmptyBorder(1, 1, 1, 1));
		setContentPane(mainPage);
		mainPage.setLayout(null);
		
		username = new JTextField();
		username.setBounds(620, 80, 100, 25);
		mainPage.add(username);
		username.setColumns(10);
		
		JLabel label = new JLabel("Your login:");
		label.setBounds(620, 50, 100, 16);
		mainPage.add(label);
		
		
		
		JButton send = new JButton("Send");

		send.setEnabled(false);
		send.setBounds(500, 400, 100, 25);
		mainPage.add(send);
		
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!ourMessage.getText().isEmpty()) {
					ourClient.sendTo(ourMessage.getText());
					ourMessage.setText("");
				}
			}
		});
		
		userStatus = new JLabel("Status: offline");
		userStatus.setBounds(620, 150, 100, 25);
		mainPage.add(userStatus);
		
		ourMessage = new JTextField();
		ourMessage.setEnabled(false);
		ourMessage.setBounds(10, 400, 300, 25);
		mainPage.add(ourMessage);
		ourMessage.setColumns(15);
		
		JComboBox<Object> allUser = new JComboBox<Object>();
		allUser.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (processing != true) {
					if (allUser.getSelectedIndex() != -1) {
						ourClient.conversation = allUser.getSelectedItem().toString();
					} else {
						ourClient.conversation = username.getText();
					}
			    }
			}
		});
		
		allUser.setEnabled(false);
		allUser.setBounds(320, 400, 160, 25);
		mainPage.add(allUser);
		
		JTextArea chat = new JTextArea();
		chat.setEditable(false);
		chat.setBounds(10, 10,600, 370);
		mainPage.add(chat);
		
		JButton apply = new JButton("\u004C\u006F\u0067\u0069\u006E");
		apply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (apply.getText().equals("Login")) {
					ourClient = new Client(username.getText());
					if (!ourClient.setOnline(username.getText())) {
						JOptionPane.showMessageDialog(mainPage,
			  					  					  "User already registered.",
			  					  					  "Error",
			  					  					  JOptionPane.ERROR_MESSAGE);
						return;
					}
					new Thread(new Runnable( ) {

						@Override
						public void run() {
							
							while(true) {
								if (apply.getText().equals("Log out")) {
									if (ourClient.getUsers()) {
										processing = true;
										XML.XMLResponse xml = XML.XML.parse(ourClient.users);
										allUser.removeAllItems();
										int chatUser = 0;
										for (int i = 0; i < xml.ourMessage.size(); ++i) {
											Object userPage = xml.ourMessage.get(i).from;
											allUser.addItem(userPage);
											if (xml.ourMessage.get(i).from.equals(ourClient.conversation)) {
												chatUser = i;
											}
										}
										processing = false;
										allUser.setSelectedIndex(chatUser);
									}
									
									if (ourClient.getMessages()) {
										XML.XMLResponse xml = XML.XML.parse(ourClient.allMessage);
										chat.setText("");
										if (xml != null && xml.ourMessage != null && !xml.ourMessage.isEmpty())
										for (XML.XMLMessage m : xml.ourMessage) {
											chat.append(m.message + "\n");
										}
									}
								}
								
								try {
									Thread.sleep(250);
								} catch (Exception e) {
									
								}
							}
						}	
					}).start();
					userStatus.setText("Status: online");
					send.setEnabled(true);
					ourMessage.setEnabled(true);
					username.setEnabled(false);
					allUser.setEnabled(true);
					apply.setText("Log out");
				} else {
					ourClient.setOffline(username.getText());
					userStatus.setText("Status: offline");
					username.setEnabled(true);
					send.setEnabled(false);
					ourMessage.setEnabled(false);
					allUser.setEnabled(false);
					apply.setText("Login");
				}

			}
		});
		apply.setBounds(620, 120, 100, 25);
		mainPage.add(apply);
		
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent eventm) {
				if (eventm.isPopupTrigger()) {
					showMenu(eventm);
				}
			}
			public void mouseReleased(MouseEvent eventm) {
				if (eventm.isPopupTrigger()) {
					showMenu(eventm);
				}
			}
			private void showMenu(MouseEvent eventm) {
				popup.show(eventm.getComponent(), eventm.getX(), eventm.getY());
			}
		});
	}
}
