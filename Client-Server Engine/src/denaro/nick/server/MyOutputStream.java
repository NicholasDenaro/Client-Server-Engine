package denaro.nick.server;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.LinkedList;


public class MyOutputStream
{
	public MyOutputStream(OutputStream out)
	{
		this.out=out;
		messages=new LinkedList<Message>();
	}
	
	/**
	 * Adds a message to queue
	 * @param message - The message to add to the queue
	 */
	public void addMessage(Message message)
	{
		messages.add(message);
	}
	
	/**
	 * Flushes bytes into the output stream
	 * @param maxSize - the maximum amount of bytes to flush
	 * @throws IOException
	 */
	public void flush(int maxSize) throws IOException
	{
		int size=4;
		ByteBuffer buffer=ByteBuffer.allocate(maxSize+4);
		buffer.putInt(buffer.capacity());
		//System.out.println("buffer capacity: "+buffer.capacity());
		while(messages.peek()!=null&&size+messages.peek().size()<=buffer.capacity())
		{
			//System.out.println("Messgae written.");
			size+=messages.peek().size();
			buffer.put(messages.pop().bytes());
		}
		buffer.putInt(0,size-4);
		out.write(buffer.array(),0,size);
		//System.out.println("flush size: "+size);
		out.flush();
	}
	
	/**
	 * Closes the stream
	 * @throws IOException 
	 */
	public void close() throws IOException
	{
		out.close();
	}
	
	/** The queue for messages*/
	private LinkedList<Message> messages;
	
	/** The output stream to flush to*/
	private OutputStream out;
}
