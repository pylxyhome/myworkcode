package cn.gzjp.codec.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import cn.gzjp.codec.type.MessageType;
import cn.gzjp.codec.type.MessagesType;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
/**
 * 
 * @author gzwenny
 * created:2010-7-6
 */
public class BeanBuilder {

	public static void main(String[] args) {
		
		try{
			if(args.length<2)usage();
			String dir=args[0];
			String xml=args[1];
			BeanBuilder builder=new BeanBuilder();		
			builder.coding(builder.parse(xml),dir);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	private static void usage(){
		System.out.println("BeanBuilder <source-dir> <message-bean.xml>");
	}
	
	private void coding(MessagesType messages,String dir)throws Exception {
		String packageName=messages.getPackageName();
		if(dir==null)dir="src";
		String path=dir+"/"+packageName.replace(".", "/");
		File dirs=new File(path);
		if(!dirs.exists())dirs.mkdirs();
		Configuration cfg=new Configuration();
		cfg.setClassForTemplateLoading(getClass(),"");
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		
		Template temp=cfg.getTemplate("message-bean.ftl");
		for(MessageType message:messages.getMessage()){
			Map<String,Object> root=new HashMap<String,Object>();
			root.put("package", messages.getPackageName());
			root.put("message", message);
			root.put("now", new Date());
			Writer out=new FileWriter(new File(path+"/"+message.getClassName()+".java"));
			temp.process(root, out);
		}
	}
	
	private MessagesType parse(String filename) throws Exception {
		JAXBContext jc = JAXBContext.newInstance(MessagesType.class);
		Unmarshaller um = jc.createUnmarshaller();
		Object o=um.unmarshal(new File(filename));
		return (MessagesType)o;
	}
}
