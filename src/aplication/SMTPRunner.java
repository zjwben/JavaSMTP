package aplication;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.SMTPServer;

import client.SMTPClient;

public class SMTPRunner {
	static Thread server = new Thread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			SMTPServer server = new SMTPServer();
			
			try {
				server.initServer(7890);
				server.listen();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	});
//	static Thread client = new Thread(new Runnable() {
//		
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			SMTPClient client = new SMTPClient();
//			try {
//				client.initClient("localhost", 7890,"client_1");
//				client.listen();
//				
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
//	});
	public static void main(String[] args) {
		server.start();
//		client.start();
		
		
		
		
	//	ExecutorService executor = Executors.newCachedThreadPool();
//		executor.execute(client);
		ExecutorService cacheThreadPool = Executors.newCachedThreadPool();
		
		for(int i = 0 ;i < 10 ;i ++){
			final int index = i; 
			cacheThreadPool.execute(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					SMTPClient client = new SMTPClient();
					try {
						client.initClient("localhost", 7890,"client_" + index);
						client.listen();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
		}
		
		
	}
}

