package Server;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JButton;

import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerInterface extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerInterface frame = new ServerInterface();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ServerInterface() {
		setTitle("Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(1, 1, 1, 1));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTextArea logEvent = new JTextArea();
		JScrollPane el = new JScrollPane(logEvent); 
		logEvent.setEditable(false);
		el.setBounds(10, 40, 400, 300);
		contentPane.add(el);
		
		JTextArea queryLog = new JTextArea();
		JScrollPane ql = new JScrollPane(queryLog); 
		queryLog.setEditable(false);
		ql.setBounds(420, 40, 300, 300);
		contentPane.add(ql);
		
		JButton clearEventLog = new JButton("Clear Events Log");
		clearEventLog.setBounds(10, 350, 200, 25);
		contentPane.add(clearEventLog);
		
		JButton clearQueryLog = new JButton("Clear Queries Log");
		clearQueryLog.setBounds(420, 350, 200, 25);
		contentPane.add(clearQueryLog);
		
		JLabel label = new JLabel("Events log");
		label.setBounds(15, 10, 70, 20);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("Queries log");
		label_1.setBounds(420, 10, 70, 20);
		contentPane.add(label_1);
		
		clearEventLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				logEvent.setText("");
			}
		});
		
		clearQueryLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				queryLog.setText("");
			}
		});
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					new Server(logEvent, queryLog);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}}
		).start();
		
	}
}
