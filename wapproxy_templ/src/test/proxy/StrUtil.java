package test.proxy;

public class StrUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str = "1234567";
		String subStr = "AA";
		System.out.println(addSubStr(5,str, subStr));
	}
	
	public static String tnull(String str){
		if(str == null){
			str = "";
		}
		return str;
	}
	
	public static String addSubStr(int ix, String str, String subStr){
		str = tnull(str);
		subStr = tnull(subStr);
		String result = "";
		StringBuilder strbd = new StringBuilder();
		strbd.append(str.substring(0, ix));
		strbd.append(subStr);
		strbd.append(str.substring(ix, str.length()));
		result = strbd.toString();
		return result;
	}

}
