package cn.gzjp.codec.util;
/**
 * 
 * @author gzwenny
 * created:2010-7-6
 */
public class ByteUtils {

	public static String toString(byte[]data){
		int position=0;
		int length=data.length;
		for(int i=0;i<length;i++){
			if(data[i]=='\0')break;
			position++;
		}
		
		byte[]results=new byte[position];
		for(int i=0;i<position;i++)
			results[i]=data[i];
		return new String(results);
	}
	
	public static String toHex(byte[]data){
		StringBuffer buffer=new StringBuffer();
		for(int i=0;i<data.length;i++){
			byte ch=data[i];
			String hex=Integer.toHexString(ch);
			if(hex.length()==1)buffer.append('0').append(hex);
			if(hex.length()>=2)buffer.append(hex.substring(hex.length()-2,hex.length()));
			buffer.append('-');
		}
		buffer.deleteCharAt(buffer.length()-1);
		return buffer.toString();
	}
}
