package cn.gzjp.server;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.gzjp.codec.MessageCodec;
import cn.gzjp.push2.sgip.message.BindMessage;
import cn.gzjp.push2.sgip.message.BindResponseMessage;
import cn.gzjp.push2.sgip.message.SubmitMessage;
import cn.gzjp.push2.sgip.message.SubmitResponseMessage;
import cn.gzjp.util.StrUtil;


public class MuServerTestAsy {
	private final static Logger log = LoggerFactory
	.getLogger(MuServerTestAsy.class);

	/**
	 * @description
	 * @author zhengmx
	 * @date 2011-12-8
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		t1();
	}
	
	static void t1() throws Exception{
		
		
		Reporter reporter = new Reporter();
		Thread t = new Thread(reporter);
		t.start();
		
		ServerSocket ss = new ServerSocket(8000);
		Socket sk = null;
		while((sk = ss.accept()) != null){
			System.out.println("host=" + sk.getRemoteSocketAddress());
			HandlerAsy h = new HandlerAsy(sk);
			Thread t2 = new Thread(h);
			t2.start();
			
			
//			h.r1();
			
		}
	}
}
class HandlerAsy implements Runnable{
	private final static Logger log = LoggerFactory
	.getLogger(Handler.class);
	
	public static final MessageCodec mcc = new MessageCodec("/sgip.xml");
	
	private static BlockingQueue<ReportMsg> flows = new  LinkedBlockingQueue<ReportMsg>();
	
	
	Socket sk = null;
	
	public HandlerAsy(Socket sk){
		this.sk = sk;
	}
	
	static public ReportMsg getReportMsg() throws InterruptedException{
		ReportMsg reportMsg = flows.take();
		log.debug("flowsQ take length=" + flows.size());
		return reportMsg;
	} 
	
	static public void add(ReportMsg rmsg) throws InterruptedException{
		flows.put(rmsg);
		log.debug("flowsQ add length=" + flows.size());
	} 
	@Override
	public void run() {
		r1();
	}
	
	void r1(){
		try {
			InputStream in = sk.getInputStream();
			OutputStream out = sk.getOutputStream();
			
			ResponsHandle rh = new ResponsHandle(out);
			Thread t = new Thread(rh);
			t.start();
			
			while(true){
				ByteBuffer buf = read(in);
				Object request =mcc.decode(buf);
				log.debug("request request.className=" 
						+ request.getClass().getName());
				
				if(request instanceof BindMessage){//bind
					BindMessage bind = (BindMessage)request;
					BindResponseMessage brm = new BindResponseMessage();
					log.debug("bindMessage username=" 
							+ bind.getLoginName() 
							+ " pwd" + bind.getLoginPassword());
					brm.setFlowId(new int[3]);
					brm.setResult((byte)0);
					write(brm, out);
				}else if(request instanceof SubmitMessage){//submit
					SubmitMessage subm = (SubmitMessage)request;
					log.debug("SubmitMessage mdn=" 
							+ subm.getUsernumbers()[0]
							+ " flow=" + StrUtil.getFlowIdStr(subm.getFlowId()));
					ReportMsg rm = new ReportMsg();
					rm.setFlow(subm.getFlowId());
					rm.setMdn(subm.getUsernumbers()[0]);
					add(rm);
					
					SubmitResponseMessage srm = new SubmitResponseMessage();
					srm.setResult((byte)0);
					srm.setFlowId(subm.getFlowId());
					rh.add(srm);
					log.debug("resps add length=" + rh.resps.size());

				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:", e);
		}finally{
			log.info("finish=" + sk.getRemoteSocketAddress());
		}
	}

	public static ByteBuffer read(InputStream in) throws Exception {
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
	public void write(Object obj, OutputStream out) throws Exception {	
		log.debug("write message data to socket...");
		ByteBuffer buffer=mcc.encode(obj);
		byte[]data = new byte[buffer.limit()];
		buffer.get(data);

		out.write(data);
		out.flush();
	}
}
class ResponsHandle implements Runnable{
	private final static Logger log = LoggerFactory
	.getLogger(ResponsHandle.class);
	
	BlockingQueue<SubmitResponseMessage> resps = new  LinkedBlockingQueue<SubmitResponseMessage>();
	
	private OutputStream out = null;
	public ResponsHandle(OutputStream outhandle){
		out = outhandle;
	}
	
	public SubmitResponseMessage getResp() throws InterruptedException{
		SubmitResponseMessage submitResponseMessage = resps.take();
		log.debug("resps take length=" + resps.size());
		return submitResponseMessage;
	} 
	public void add(SubmitResponseMessage submitResponseMessage){
		resps.add(submitResponseMessage);
	}
	
	@Override
	public void run() {
		int j=0;
		try{
			while(true){
				SubmitResponseMessage srm = getResp();
				j++;
				ByteBuffer buffer= HandlerAsy.mcc.encode(srm);
				byte[]data = new byte[buffer.limit()];
				buffer.get(data);
			
				log.debug("submitResponseAsy data.length=" + data.length +" srm.flow=" + StrUtil.getFlowIdStr(srm.getFlowId()) 
						+ " srm.result=" + srm.getResult());
				out.write(data);
				log.debug("j=="+j);
			}
		}catch(Exception excep){
			excep.printStackTrace();
		}
	}
	
}
