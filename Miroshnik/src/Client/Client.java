package Client;

import java.net.Socket;

public class Client extends Thread {
	
	private String name;
	public String users = "NULL";
	public String allMessage = "NULL";
	public String conversation;
	
	public boolean setOnline(String name) {
		 String whoMessage = sendMessage(XML.XML.login(name));
		 if (whoMessage.equals("OK")) {
			 return true;
		 } else {
			 return false;
		 }
	}
	public void setOffline(String name) {
		 sendMessage(XML.XML.logout(name));
	}
	
	public boolean getUsers() {
		String user = sendMessage(XML.XML.getUsers());
		if (!user.equals(users)) {
			users = user;
			return true;
		}
		return false;
	}
	
	public boolean getMessages() {
		String ourMessage = sendMessage(XML.XML.getMessages(name, conversation));
		if (!ourMessage.equals(allMessage)) {
			allMessage = ourMessage;
			return true;
		}
		return false;
	}
	public Client(String name) {
		this.name = name;
		conversation = name;
	}
	
	public String sendMessage(String message) {
    	byte storage[] = new byte[64 * 1024];
    	try {
        	Socket newSoket = new Socket("localhost", 9876);
        	newSoket.getOutputStream().write(message.getBytes());
			int l = newSoket.getInputStream().read(storage);
			String result = new String(storage, 0, l);
			newSoket.close();
			return result;
		} catch (Exception e) {
	    	return "Error";
		}
    }
	
	public void sendTo(String whom) {
		sendMessage(XML.XML.send(name, conversation, whom));
	}
	
}
