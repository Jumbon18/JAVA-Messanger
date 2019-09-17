package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

import javax.swing.JTextArea;
import javax.swing.JTextPane;

public class Server {
	
	private ServerSocket serverSocket;
	private List<String> allUsers;
	private Map<String, List<String>> convertations;
	private static JTextArea events, queryes;
	
	public void connections() {
		try
        {
            while(true)
            {
            	try {		
            		Socket sockets = serverSocket.accept();
            		InputStream inputStream = sockets.getInputStream();
                    OutputStream outputStream = sockets.getOutputStream();
                    
                    byte storage[] = new byte[64*1024];
                    String ourMessage = new String(storage, 0, inputStream.read(storage));
                    
                    System.out.println(ourMessage);
                    
                    process(ourMessage, outputStream);
                    sockets.close();
            	} catch (Exception e) {
           		
            	}
           }
       } catch(Exception e) {
       	
       }
   }
	
	public Server(JTextArea el, JTextArea ql) throws Exception {
		events = el;
		queryes = ql;
		new Server();
	}
	
public void process(String message, OutputStream outputStream) throws IOException {
		
		XML.XMLResponse xml = XML.XML.parse(message);
		
		if (xml.type.equals("getUsers") || xml.type.equals("getMessages")) {
			queryes.append(message + "\n");
		} else {
			events.append("<" + ((new SimpleDateFormat("[d.MM.Y HH:mm:ss ] ").format(Calendar.getInstance().getTime()))) + "> " + message + "\n");
		}
		
		if (xml.type.equals("login")) {
			for (String user : allUsers) {
				if (xml.name.equals(user)) {
					outputStream.write("ERROR".getBytes());
					return;
				}
			}
			for (Map.Entry<String, List<String>> entry : convertations.entrySet())
			{
				if (entry.getKey().contains(xml.name)) {
					entry.getValue().add(xml.name + " подключился.");
				}
			}
			allUsers.add(xml.name);
			outputStream.write("OK".getBytes());
		}
		
		if (xml.type.equals("logout")) {
			for (int i = 0; i < allUsers.size(); ++i) {
				if (xml.name.equals(allUsers.get(i))) {
					allUsers.remove(i);
					break;
				}
			}
			for (Map.Entry<String, List<String>> entry : convertations.entrySet())
			{
				if (entry.getKey().contains(xml.name)) {
					entry.getValue().add(xml.name + " отключился.");
				}
			}
			outputStream.write("OK".getBytes());
		}
		
		if (xml.type.equals("getUsers")) {
			outputStream.write(XML.XML.users(allUsers).getBytes());
		}
		
		if (xml.type.equals("send")) {
			if (!xml.ourMessage.isEmpty()) {
				String who = xml.ourMessage.get(0).from;
				String whom = xml.ourMessage.get(0).to;
				String s;
				if (who.compareTo(whom) > 0) {
					s = who + "&" + whom;
				} else {
					s =whom + "&" + who;
				}
				if (!convertations.containsKey(s)) {
					convertations.put(s, new ArrayList<>());
				}

				for(XML.XMLMessage m : xml.ourMessage) {
					convertations.get(s).add(who + ":  " + m.message);
				}
			}
			outputStream.write("OK".getBytes());
		}
		
		if (xml.type.equals("getMessages")) {
			String who = xml.ourMessage.get(0).from;
			String whom = xml.ourMessage.get(0).to;
			String s;
			if (who.compareTo(whom) > 0) {
				s = who + "&" + whom;
			} else {
				s = whom + "&" + who;
			}
			
			outputStream.write(XML.XML.messages(convertations.get(s)).getBytes());
		}
	}
	
	public Server() throws Exception {
		serverSocket = new ServerSocket(9876, 0, InetAddress.getByName("localhost"));
		allUsers = new ArrayList<>();
	   	convertations = new HashMap<String, List<String>>();
	   	connections();
	}
	
	

}
