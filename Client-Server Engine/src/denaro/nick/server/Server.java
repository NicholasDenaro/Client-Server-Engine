package denaro.nick.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;


public abstract class Server extends Thread
{
	public Server(String hostname, int port) throws IOException
	{
		server=new ServerSocket();
		
		if(hostname==null)
		{
			//hostname=InetAddress.getLocalHost().getHostAddress();
			System.out.print("hostname:port| ");
			hostname=getInput();
			port=new Integer(hostname.substring(hostname.indexOf(':')+1));
			hostname=hostname.substring(0,hostname.indexOf(':'));
		}
		server.setReuseAddress(true);
		server.bind(new InetSocketAddress(hostname,port));
		System.out.println("server bound to: "+server.getInetAddress());
		clients=new HashMap<Socket,Client>();
	}
	
	public static String getInput()
	{
		BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
		String command;
		try
		{
			command=in.readLine();
			return(command);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return(null);
	}
	
	public abstract Client newClient(Socket socket) throws IOException;
	
	@Override
	public void run()
	{
		System.out.println("Server started.");
		running=true;
		while(running)
		{
			try
			{
				Socket socket=server.accept();
				if(!clients.containsKey(socket))
				{
					Client client=newClient(socket);
					clients.put(socket,client);
					client.start();
				}
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
				running=false;
			}
		}
		System.out.println("Server shutting down.");
	}
	
	public ArrayList<Client> clients()
	{
		return (ArrayList<Client>)(clients.values());
	}
	
	private HashMap<Socket,Client> clients;
	private ServerSocket server;
	private boolean running;
}
