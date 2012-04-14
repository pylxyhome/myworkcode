import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;

public class SendData {

	/**
	 * @author panyl
	 * @date 2011-11-29
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) {
//		Socket sk = new Socket("61.242.169.22", 8003);
//
//		PrintWriter pw = new PrintWriter(sk.getOutputStream());
//		pw.println("POST /push106/ss HTTP/1.1");
//		pw.println("msg: " + java.net.URLEncoder.encode("ok", "utf-8"));
//		pw.println("push-url: "
//				+ java.net.URLEncoder.encode("http://3g.cn/", "utf-8"));
//		pw.println("host: 61.242.169.22:8003");
//		pw.println();
//		pw.println("1|13164626002");
//		pw.println("2|13164626002");
//		pw.println("3|13164626002");
//		pw.println("4|13164626002");
//
//		pw.flush();
//
//		BufferedReader br = new BufferedReader(new InputStreamReader(
//				sk.getInputStream()));
//
//		String line = "";
//		while ((line = br.readLine()) != null) {
//			System.out.println("line=" + line);
//		}
//		br.close();
//		pw.close();
//		sk.close();
		PrintWriter pw;
		try {
			pw = new PrintWriter(new FileOutputStream("D:/send_info.txt"));
		
		StringBuffer stringBuffer = new StringBuffer();
		for(int i=0;i<100000;i++){
			stringBuffer.append(String.valueOf(100000000+i)).append("|").append(String.valueOf(13012345678l+i));
			stringBuffer.append("\n");
		}
		pw.print(stringBuffer.toString());
		pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
