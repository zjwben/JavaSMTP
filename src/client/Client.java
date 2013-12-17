package client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner;


// 定义客户端可用的操作
@SuppressWarnings("unused")
public class Client {
	private String myDomain;
	private String reversePath;
	private Mail mail;
	private String order;
	public String getMyDomain() {
		return myDomain;
	}
	public void setMyDomain(String myDomain) {
		this.myDomain = myDomain;
	}
	public Client(String _myDomain ){
		myDomain = _myDomain;
	}
	public Client(){
		myDomain = "localhost";
	}
	// 判断Order是否符合要求
	public boolean isCorrectOrder(String str){
		if(str.equals("HALO") || str.equals("MAIL") ||
			str.equals("RCPT") ||str.equals("DATA") ||
			str.equals("QUIT")){
			return true;
		}
		return false;
	}
	public String createMail(){
		String str = "HALO localhost";
		
		String content[] = str.split("\\s{1,}");
		if(isCorrectOrder(content[0])){
			order = content[0];
			return opOrder();
		}else{
			System.out.println("命令错误");
			return "";
		}
	}
	private String opOrder(){
		if(order.equals("HALO")){
			 return doHalo();
		}else if(order.equals("MAIL")){
			 return doMail();
		}else if(order.equals("RCPT")){
			 return doRept();
		}else if(order.equals("DATA")){
			 return doData();
		}else if(order.equals(".")){
			 return doReadFile();
		}else if(order.equals("QUIT")){
			 return doQuit();
		}
		return "";
		
	}
	private String doReadFile() {
		// TODO Auto-generated method stub
		String mailContent = "1111";
		FileInputStream fin = null;
		try {
			fin = new FileInputStream("zjwmail.txt");
			FileChannel fc = fin.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			fc.read(buffer);
			byte[] data = buffer.array();
			mailContent = new String(data).trim();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mailContent;
	}
	private String doQuit() {
		// TODO Auto-generated method stub
		return order;
	}
	private String doData() {
		// TODO Auto-generated method stub
		return order;
	}
	private String doRept() {
		// TODO Auto-generated method stub
		return order + " TO: <friend@example.com>";
	}
	private String doMail() {
		// TODO Auto-generated method stub
		return order + " FROM: <sender@mydomain.com>";
	}
	private String doHalo() {
		// TODO Auto-generated method stub
		return order + " " + myDomain;
	}
	public String doSomething() {
		// TODO Auto-generated method stub
		if(order.equals("HALO"))
			order = "MAIL";
		else if(order.equals("MAIL"))
			order = "RCPT";
		else if(order.equals("RCPT"))
			order = "DATA";
		else if(order.equals("DATA"))
			order = ".";
		else if(order.equals("."))
			order = "QUIT";
		return opOrder();
	}
	
}
