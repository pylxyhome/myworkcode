package junit;

import java.util.List;

import net.sf.json.JSONArray;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.im.bean.file.UploadFile;
import com.im.bean.user.User;
import com.im.service.file.IUploadService;
import com.im.service.init.IDataInitService;
import com.im.service.tree.IGroupUserTreeService;
import com.im.service.user.IUserService;
import com.im.util.base.UUIDUtil;

public class ImTest {
	private static ApplicationContext act = null;  
	private static IUploadService uploadService;
	private static IUserService userService;
	private static IDataInitService dataInitService;
	private static IGroupUserTreeService  groupUserTreeService;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		act = new ClassPathXmlApplicationContext("beans.xml");
		uploadService=(IUploadService)act.getBean("uploadService");
		//userService=(IUserService)act.getBean("userService");
		dataInitService=(IDataInitService)act.getBean("dataInitService");
		groupUserTreeService=(IGroupUserTreeService)act.getBean("groupUserTreeService");
	}  
	@Test
	public void user(){
		User user = new User("test");    
		//userService.save(user);
	}
	@Test
	public void file(){
		UploadFile file = new UploadFile("test");
		uploadService.save(file);
	}
	@Test
	public void find(){
		System.out.println(UUIDUtil.getRandomUUID());
	}
	@Test
	public void initData(){
		dataInitService.initImDatas("ImDatas.xml");
	}
	@Test
	public void testUserGroupTree(){
		System.out.println("用户与组树"+JSONArray.fromObject(groupUserTreeService.getGroupUserTreeList()));
	}
	@Test
	public void findAll(){
		List<UploadFile> list = uploadService.findAll();
		for(UploadFile file : list){
			System.out.println(file.getFileId()+":"+file.getFileName());
		}
	}
}
