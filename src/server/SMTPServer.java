package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import client.Client;


public class SMTPServer {
	private Selector selector;
	private ServerSocketChannel ssc;
	private Server server;
	private ExecutorService cacheThreadPool; 
	public void initServer(int port) throws IOException {
		server = new Server();
		ssc = ServerSocketChannel.open();
		
		
		ssc.configureBlocking(false);
		
		ssc.socket().bind(new InetSocketAddress(port));
		
		this.selector = Selector.open();
		
		
		ssc.register(selector, SelectionKey.OP_ACCEPT);
		cacheThreadPool = Executors.newCachedThreadPool();
	}
	
	
	public void listen() throws IOException{
		System.out.println("服务器启动成功");
		
		while(true){
			selector.select();
			
			Iterator itr = this.selector.selectedKeys().iterator();
			while(itr.hasNext()){
				final SelectionKey key = (SelectionKey)itr.next();
				itr.remove();
				if (key.isAcceptable()){
						ServerSocketChannel server = (ServerSocketChannel)key.channel();
						
						SocketChannel channel = server.accept();
						
						channel.configureBlocking(false);
						
//						channel.write(ByteBuffer.wrap(new String("www.example.com ESMTP Postfix").getBytes()));
						
						channel.register(this.selector, SelectionKey.OP_READ);
						
				}else if(key.isReadable()){
					read(key);
				}
			}
		}
	}
	public void read(SelectionKey key ) throws IOException{
				SocketChannel channel = (SocketChannel) key.channel();
				// 分配一个100个字节的缓冲区
				ByteBuffer buffer = ByteBuffer.allocate(1000);
				
				channel.read(buffer);
				byte[] data = buffer.array();
				buffer.clear();
				String msg = new String(data).trim();
				System.out.println("<-----" + msg);
//				
				String str = server.opOrder(msg);
				Reader reader = new Reader(channel, buffer,str);
				cacheThreadPool.execute(reader);
//				String str = server.opOrder(msg);
//				ByteBuffer outBuffer = ByteBuffer.wrap(str.getBytes());
//				channel.write(outBuffer);
//				outBuffer.clear();
				
				// wrap 将byte 数组包装到缓冲区
				
		
	}
}
 class Reader extends Thread{
	private SocketChannel channel;
	private ByteBuffer buffer;
	private String str;
	public Reader(SocketChannel _channel,ByteBuffer _buffer,String _str){
		channel = _channel;
		buffer = _buffer;
		str = _str;
	}
	public void run(){
		try {
//			channel.read(buffer);
//			byte[] data = buffer.array();
//			//buffer.clear();
//			String msg = new String(data).trim();
////			if(msg.equals(""))
////				System.out.println("1111111111111111");
//			System.out.println("<-----" + msg);
//			String str = server.opOrder(msg);
			ByteBuffer outBuffer = ByteBuffer.wrap(str.getBytes());
			channel.write(outBuffer);
			//outBuffer.clear();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
