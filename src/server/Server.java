package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

interface  StateIdInterface{
	int SERVER_READY = 220;
	int SERVER_CLOSE = 221;
	int OPTION_DONE	=250;
	int BEGIN_INPUTCONTENT = 354;
}
public class Server {
	private int stateId;// 用来表示服务器返回状态
	private String content;//
	public String opOrder(String _content){
		content = _content;
		String str[] = content.split("\\s{1,}");
		if(str[0].equals("HALO")){
			stateId = StateIdInterface.OPTION_DONE;
			return (stateId + " Hello " + str[1]);
		}else if(str[0].equals("MAIL")){
			stateId = StateIdInterface.OPTION_DONE;
			return (stateId + " Ok");
		}else if(str[0].equals("RCPT")){
			stateId = StateIdInterface.OPTION_DONE;
			return (stateId + " Ok");
		}else if(str[0].equals("DATA")){
			stateId = StateIdInterface.BEGIN_INPUTCONTENT;
			return (stateId + " End data with <CR><LF>.<CR><LF>");
		}else if(str[0].equals("QUIT")){
			stateId = StateIdInterface.SERVER_CLOSE;
			return (stateId + " Bye");
		}else{
			saveMail(content);
			return (StateIdInterface.OPTION_DONE + " Ok: queued as 12345");
		}
	}
	
	public void saveMail(String content){
		File f = new File("mails//" + getMailFileName() +".txt");
		try {
			FileOutputStream fout = new FileOutputStream(f);
			FileChannel fc = fout.getChannel();
			fc.write(ByteBuffer.wrap(content.getBytes("UTF-8")));
			fc.close();
			fout.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private String getMailFileName(){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(new Date()) +"_" +(int)(Math.random()*1000) ;
	}
	
}
