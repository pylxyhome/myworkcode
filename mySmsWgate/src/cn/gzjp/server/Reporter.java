package cn.gzjp.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.gzjp.codec.DecodeException;
import cn.gzjp.codec.EncodeException;
import cn.gzjp.codec.MessageCodec;
import cn.gzjp.push2.sgip.message.ReportMessage;
import cn.gzjp.util.Configure;
import cn.gzjp.util.StrUtil;

public class Reporter implements Runnable{
	private final static Logger log = LoggerFactory
	.getLogger(Reporter.class);
	
	private static final MessageCodec mcc = new MessageCodec("/sgip.xml");
	
	static String rhost = Configure.get().getValue("reportHost");
	static int rport = Configure.get().getValueInt("reportPort");	

	static Socket sk = null;
	static OutputStream out = null;
	static InputStream in = null;
	static boolean isConn = true;
	
	@Override
	public void run() {
		boolean isConn = true;
		
		while(true){
			
			try {
				ReportMsg flow = Handler.getReportMsg();
				log.debug("get Flow=" + StrUtil.getFlowIdStr(flow.getFlow()) + " mdn=" + flow.getMdn());
								
				if(isConn){
					sk = new Socket(rhost, rport);
					sk.setSoTimeout(2000);
					out = sk.getOutputStream();
					in = sk.getInputStream();
				}
				try{
					ReportMessage rm = new ReportMessage();
					
					rm.setSubmitFlowId(flow.getFlow());
					rm.setErrorCode((byte)0);
					rm.setState((byte)0);
					rm.setFlowId(new int[3]);
					rm.setUsernumber(flow.getMdn());
	
					ByteBuffer buffer=mcc.encode(rm);
					byte[]data = new byte[buffer.limit()];
					buffer.get(data);
					
					out.write(data);
					out.flush();
					
					log.debug("get data start...");
					Object response = mcc.decode(read(in));
					log.debug("get data finish..." + response.getClass().getName());
					
					isConn = false;
				}catch(IOException e2){
					e2.printStackTrace();
					isConn = true;
				}

			}  catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	synchronized public static void sendPort(ReportMsg flow) {
		boolean isGo = true;
		while(isGo){
			
			try {
				log.debug("Flow=" + StrUtil.getFlowIdStr(flow.getFlow()) 
						+ " mdn=" + flow.getMdn());
				if(isConn){
					log.debug("isConn=" + isConn);
					sk = new Socket(rhost, rport);
					sk.setSoTimeout(1000);
					out = sk.getOutputStream();
					in = sk.getInputStream();
				}
				try{
					ReportMessage rm = new ReportMessage();
					
					rm.setSubmitFlowId(flow.getFlow());
					rm.setErrorCode((byte)0);
					rm.setState((byte)0);
					rm.setFlowId(new int[3]);
					rm.setUsernumber(flow.getMdn());
	
					ByteBuffer buffer=mcc.encode(rm);
					byte[]data = new byte[buffer.limit()];
					buffer.get(data);
					
					out.write(data);
					out.flush();
					
					log.debug("get data start...");
					Object response = mcc.decode(read(in));
					log.debug("get data finish..." + response.getClass().getName());
					
					isConn = false;
					isGo = false;
					log.debug("finish Flow=" + StrUtil.getFlowIdStr(flow.getFlow()) 
							+ " mdn=" + flow.getMdn());
				}catch(IOException e2){
					e2.printStackTrace();
					isConn = true;
				}
			}  catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) throws DecodeException, Exception{
			sk = new Socket("172.31.36.158", 8802);
			sk.setSoTimeout(1000);
			out = sk.getOutputStream();
			in = sk.getInputStream();
			
			int sencnt = Integer.MAX_VALUE;
			for(int i = 1; i <= sencnt; ++i){
				log.debug("i=" + i);
				ReportMessage rm = new ReportMessage();
				
				rm.setSubmitFlowId(new int[3]);
				rm.setErrorCode((byte)0);
				rm.setState((byte)0);
				rm.setFlowId(new int[3]);
				rm.setUsernumber("8613012345678");

				ByteBuffer buffer=mcc.encode(rm);
				byte[]data = new byte[buffer.limit()];
				buffer.get(data);
				log.debug("send data strart...");
				
				out.write(data);
				out.flush();
				log.debug("send data finish...");
				
				log.debug("get data start...");
				Object response = mcc.decode(read(in));
				log.debug("get data finish..." + response.getClass().getName());
			}
			
			sk.close();
	}
	private static ByteBuffer read(InputStream in) throws Exception {
		log.debug("read message data from socket...");
		byte[] data = new byte[4];
		int messageLength = in.read(data);
		if (messageLength == -1)
			throw new Exception("read response message return -1.");
		int length = ByteBuffer.wrap(data).getInt();
		log.debug("read length=" + length);
		data = new byte[length - 4];
		in.read(data);
		ByteBuffer buffer = ByteBuffer.allocate(length);
		buffer.putInt(length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}