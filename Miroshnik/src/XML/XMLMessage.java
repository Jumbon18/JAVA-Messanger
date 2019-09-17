package XML;

public class XMLMessage {
	
	public XMLMessage() {}
	public XMLMessage(String from,
					  String to,
					  String message) {
		this.from = from;
		this.to = to;
		this.message = message;
	}

	public String from;
	public String to;
	public String message;
}
