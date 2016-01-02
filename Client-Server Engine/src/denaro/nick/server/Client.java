package denaro.nick.server;
import java.io.IOException;
import java.net.Socket;


public abstract class Client
{
	public Client(Socket socket) throws IOException
	{
		this.socket=socket;
		out=new MyOutputStream(socket.getOutputStream());
		in=new MyInputStream(socket.getInputStream());
	}
	
	public abstract int maxMessageSize();
	
	/**
	 * Handles the messages
	 * @param in - the stream which to read from
	 * @param messageid - the id of the message to handle
	 * @throws IOException
	 */
	public abstract void handleMessages(MyInputStream in, int messageid) throws IOException;
	
	public void run()
	{
		try
		{
			in.read();
			while(!in.isEmpty())
			{
				byte messageid=in.readByte();
				handleMessages(in,messageid);
			}
		}
		catch(Exception ex)
		{
			//ex.printStackTrace();
			running=false;
		}
	}
	
	public void addMessage(Message message)
	{
		out.addMessage(message);
		if(out.size() > maxMessageSize())
		{
			sendMessages();
		}
	}
	
	public void sendMessages()
	{
		try
		{
			out.flush(maxMessageSize());
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	private boolean running;
	
	private Socket socket;
	private MyOutputStream out;
	private MyInputStream in;
}
