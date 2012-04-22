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


public class MuServerTest {
	private final static Logger log = LoggerFactory
	.getLogger(MuServerTest.class);

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
			Handler h = new Handler(sk);
			Thread t2 = new Thread(h);
			t2.start();
//			h.r1();
			
		}
	}
}
class Handler implements Runnable{
	private final static Logger log = LoggerFactory
	.getLogger(Handler.class);
	
	private final MessageCodec mcc = new MessageCodec("/sgip.xml");
	
	private static BlockingQueue<ReportMsg> flows = new  LinkedBlockingQueue<ReportMsg>();
	
	static{
//		flows.
	}
	
	Socket sk = null;
	
	public Handler(Socket sk){
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
					
					ByteBuffer buffer=mcc.encode(srm);
					byte[]data = new byte[buffer.limit()];
					buffer.get(data);
				
					log.debug("submitResponse data.length=" + data.length);
					out.write(data);
//					Reporter.sendPort(rm);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:", e);
		}finally{
			log.info("finish=" + sk.getRemoteSocketAddress());
		}
	}
	void r2(){
		try {
			InputStream in = sk.getInputStream();
			OutputStream out = sk.getOutputStream();
		
			int cnt = 1;
			int b = 0;
			while((b = in.read())!=-1){
				System.out.print("," + b);
				if((cnt % 20) == 0){
					System.out.println();
				}
				cnt++;
			}
			
			in.close();
			out.close();
			log.info("finish=" + sk.getRemoteSocketAddress());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error:", e);
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
class ReportMsg{
	private int[] flow;
	private String mdn;
	public int[] getFlow() {
		return flow;
	}
	public void setFlow(int[] flow) {
		this.flow = flow;
	}
	public String getMdn() {
		return mdn;
	}
	public void setMdn(String mdn) {
		this.mdn = mdn;
	}
	
}
