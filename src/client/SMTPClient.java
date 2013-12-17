package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class SMTPClient {
	private Selector selector;
	private Client client;
	private SocketChannel sc;
	public void initClient(String ip,int port,String clientName) throws IOException{
		client = new Client(clientName);
		SocketChannel sc = SocketChannel.open();
		 sc = SocketChannel.open();
		sc.configureBlocking(false);
		
		this.selector = Selector.open();
		// InetSockterAddress 此类实现IP 套接字地址 
		sc.connect(new InetSocketAddress(ip,port));
		
		sc.register(selector, SelectionKey.OP_CONNECT);
		
		
		
	}
	public void listen() throws IOException{
		while(true){
			selector.select();
			
			Iterator it = this.selector.selectedKeys().iterator();
			
			while(it.hasNext()){
				SelectionKey key = (SelectionKey)it.next();
				it.remove();
				if(key.isConnectable()){
					SocketChannel channel = (SocketChannel) key.channel();
					
					if(channel.isConnectionPending()){
						channel.finishConnect();
					}
					
					String order = client.createMail();
					channel.write(ByteBuffer.wrap(order.getBytes()));
					channel.register(this.selector, SelectionKey.OP_READ);
				}else if(key.isReadable()){
					read(key);
				}
			}
		}
	}
	private void read(SelectionKey key) throws IOException {
		// TODO Auto-generated method stub
		SocketChannel channel = (SocketChannel) key.channel();
		// 分配一个10个字节的缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(1000);
		
		channel.read(buffer);
		
		byte[] data = buffer.array();
		
		String msg = new String(data).trim();
		
		System.out.println("----->" + msg);
		String[] temp =msg.split("\\s{1,}");
		String order;
		if(temp[0].equals("221")){
			return;
		}else{
			order = client.doSomething();
			//order += client.getMyDomain();
		}
//		// wrap 将byte 数组包装到缓冲区
		ByteBuffer outBuffer = ByteBuffer.wrap(order.getBytes());
		
		channel.write(outBuffer);
//		sc.close();
	}
//	public static void main(String[] args) throws IOException {
//		SMTPClient client = new SMTPClient();
//		client.initClient("localhost", 7890);
//		client.listen();
//	}
	
}
