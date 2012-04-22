package cn.gzjp.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 文件工具类
 * @description
 * @author panyl
 * @date 2011-12-1
 */
public class FileUtil {
    private static Log log = LogFactory.getLog(FileUtil.class);
    private FileUtil() {}
    
    /**
     * 获取随机的文件名称
     * @param seed    随机种子
     * @return
     */
    public static String getRandomFileName(String seed) {
        byte[] ra = new byte[100];
        new Random().nextBytes(ra);
        StringBuilder build = new StringBuilder("");
        for (int i = 0; i < ra.length; i++) {
            build.append(Byte.valueOf(ra[i]).toString());
        }
        String currentDate = Long.valueOf(new Date().getTime()).toString();
        seed = seed + currentDate + build.toString();
        return seed.toLowerCase();
    }
    /**
     * 快速获取文本最后一行
     * @description
     * @author panyl
     * @date 2011-12-2
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String getLastLine(String filePath) throws IOException{
    	// 使用RandomAccessFile , 从后找最后一行数据
    	RandomAccessFile raf = new RandomAccessFile(filePath, "r");
    	long len = raf.length();
    	String lastLine = "";
    	if (len != 0L) {
    	  long pos = len - 1;
    	  while (pos > 0) { 
    	    pos--;
    	    raf.seek(pos);
    	    if (raf.readByte() == '\n') {
    	      lastLine = raf.readLine();
    	      if("".equals(lastLine))continue;
    	      break;
    	    }
    	  }
    	}
    	raf.close();
    	return lastLine;
    }
    /**
     * 判断文件是存在
     * @description
     * @author panyl
     * @date 2011-12-2
     * @param filePath
     * @return
     */
    public static boolean isExist(String filePath){
    	File file =new File(filePath);
    	return file.exists();
    }
    /**
     * 列出所有当前层的文件和目录
     * @description
     * @author panyl
     * @date 2011-12-2
     * @param dir
     * @return
     */
    public static File[] ls(String dir) {
        return new File(dir).listFiles();
    }
    
    /**
     * 根据需要创建文件夹
     * 
     * @param dirPath 文件夹路径
     * @param del    存在文件夹是否删除
     */
    public static void mkdir(String dirPath,boolean del) {
        File dir = new File(dirPath);
        if(dir.exists()) {
            if(del)
                dir.delete(); 
            else return;
        }
        if(!dir.getParentFile().exists()){
        	dir.getParentFile().mkdirs();
		}
    }
    
    /**
     * 删除文件和目录（删除目录之前要先删除目录下的所以文件）
     * @param path
     * @throws Exception
     */
    public static void rm(String path) throws Exception{

        log.debug("del file: " + path);
        File file = new File(path);
        if(!file.exists()) {
            if(log.isWarnEnabled())
                log.warn("file <" + path + ">not exist");
            return;
        }
        if(file.isDirectory()) {
            File[] fileList = file.listFiles();
            if(fileList == null || fileList.length == 0) {
                file.delete();
            } else {
                for (File _file : fileList) {
                    rm(_file.getAbsolutePath());
                }
            }
        file.delete();
        } else {
            file.delete();
        }
    }
    

    /**
     * 移动文件
     * @description
     * @author panyl
     * @date 2011-12-2
     * @param source
     * @param target
     * @throws Exception
     */
    public static void mv(String source,String target) throws Exception {
        if(source.trim().equals(target.trim()))
            return;
        InputStream fromFile = new FileInputStream(source);
        OutputStream toFile = new FileOutputStream(target);
        BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(toFile));
		BufferedReader rd = new BufferedReader(new InputStreamReader(fromFile));
		String str=null;
		while ((str = rd.readLine()) != null) {
			bw.write(str);
			bw.write("\n");
		}
		bw.flush();
		bw.close();
        toFile.flush();
        toFile.close();
        rd.close();
        fromFile.close();
        new File(source).deleteOnExit();
    }
    /**
     *  移动文件
     * @description
     * @author panyl
     * @date 2011-12-2
     * @param sourceFile
     * @param target
     * @throws Exception
     */
    public static void mv(File sourceFile,String target) throws Exception {
    	mkdir(target,true);  //创建目标文件路径
        InputStream fromFile = new FileInputStream(sourceFile);
        OutputStream toFile = new FileOutputStream(target);
        BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(toFile));
		BufferedReader rd = new BufferedReader(new InputStreamReader(fromFile));
		String str=null;
		while ((str = rd.readLine()) != null) {
			bw.write(str);
			bw.write("\n");
		}
		bw.flush();
		bw.close();
        toFile.flush();
        toFile.close();
        rd.close();
        fromFile.close();
        sourceFile.deleteOnExit();
    }
    /**
     * 把属性文件转换成Map
     * 
     * @param propertiesFile
     * @return
     * @throws Exception
     */
    public static final Map<String, String> getPropertiesMap(String propertiesFile) throws Exception{
        Properties properties = new Properties();
        FileInputStream inputStream = new FileInputStream(propertiesFile);
        properties.load(inputStream);
        Map<String, String> map = new HashMap<String, String>();
        Set<Object> keySet = properties.keySet();
        for (Object key : keySet) {
            map.put((String)key, properties.getProperty((String)key));
        }
        return map;
    }
    
    @SuppressWarnings("unchecked")
    public static final Map<String, String> getPropertiesMap(Class clazz,String fileName) throws Exception{
        Properties properties = new Properties();
        InputStream inputStream = clazz.getResourceAsStream(fileName);
        if(inputStream == null)
            inputStream = clazz.getClassLoader().getResourceAsStream(fileName);
        properties.load(inputStream);
        Map<String, String> map = new HashMap<String, String>();
        Set<Object> keySet = properties.keySet();
        for (Object key : keySet) {
            map.put((String)key, properties.getProperty((String)key));
        }
        return map;
    }
    
    /**
     * 把属性文件转换成Map
     * 
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static final Map<String, String> getPropertiesMap(InputStream inputStream) throws Exception{
        Properties properties = new Properties();
        properties.load(inputStream);
        Map<String, String> map = new HashMap<String, String>();
        Set<Object> keySet = properties.keySet();
        for (Object key : keySet) {
            map.put((String)key, properties.getProperty((String)key));
        }
        return map;
    }
    
    /**
     * 把文本文件转换成String
     * 
     * @param fullPath
     * @return
     * @throws Exception
     */
    public static String readFile(String fullPath) throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(fullPath));
        if(reader == null)
            return null;
        StringBuilder builder = new StringBuilder("");
        String line = null;
        while((line = reader.readLine()) != null) {
            builder.append(line + "\n");
        }
        return builder.toString();
    }
    public static String readFile(File file) throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(file));
        if(reader == null)
            return null;
        StringBuilder builder = new StringBuilder("");
        String line = null;
        while((line = reader.readLine()) != null) {
            builder.append(line + "\n");
        }
        reader.close();
        return builder.toString();
    }
    /**
     * 获取资源文件流
     * 
     * @param clazz
     * @param name
     * @return
     */
    @SuppressWarnings("unchecked")
    public static InputStream getResourceAsStream(Class clazz,String name) {
        try {
            InputStream inputStream = clazz.getResourceAsStream(name);
            if(inputStream == null) 
                inputStream = clazz.getClassLoader().getResourceAsStream(name);
            return inputStream;
        } catch (Exception e) {
            if(log.isWarnEnabled())
                log.warn("获取资源文件失败", e);
            return null;
        }
    }
   public static void main(String[] args) {
	try {
		String line=getLastLine("F:\\workspace\\webpush2\\finish\\20111201185505\\log.txt");
		System.out.println("line:"+line);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
