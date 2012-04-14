package cn.gzjp.idigg.server.mvc;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller 
@RequestMapping("/marketingEffect")   
public class MarketingEffectController extends BaseController{
	private final static String TERMINAL_UPDATE_DIR="/3g/marketing/terminalupdate/"; 
	private final static String NETWORK_UPDATE_DIR="/3g/marketing/networkupdate/";  
	private final static String TERMINAL_UPDATE_DETAIL_DIR="/3g/marketing/terminalupdate/detail/";
	private final static String NETMEAL_UPDATE_DIR="/3g/marketing/netmealupdate/";
	private final static String NETMEAL_UPDATE_DETAIL_DIR="/3g/marketing/netmealupdate/detail/";
	private final static String NETWORK_UPDATE_DETAIL_DIR="/3g/marketing/networkupdate/detail/";
	@RequestMapping(value="/getTerminalUpdateData.do", method = RequestMethod.POST)   
	@ResponseBody
	//@PermissionBean(model="terminalInfo", privilegeValue="getTerminalInfoCityUser2GList")
	public void getTerminalUpdateData(@RequestBody Map<String,String> parm,HttpServletResponse response)throws Exception{
		readReportJsonFile(parm, response,TERMINAL_UPDATE_DIR);
	}
	@RequestMapping(value="/getNetworkUpdateData.do", method = RequestMethod.POST)   
	@ResponseBody
	//@PermissionBean(model="terminalInfo", privilegeValue="getTerminalInfoCityUser2GList")
	public void getNetworkUpdateData(@RequestBody Map<String,String> parm,HttpServletResponse response)throws Exception{
		readReportJsonFile(parm, response,NETWORK_UPDATE_DIR);
	}
	
	@RequestMapping(value="/getTerminalUpdateDetailData.do", method = RequestMethod.POST)   
	@ResponseBody
	//@PermissionBean(model="terminalInfo", privilegeValue="getTerminalInfoCityUser2GList")
	public void getTerminalUpdateDetailData(@RequestBody Map<String,String> parm,HttpServletResponse response)throws Exception{
		readReportJsonFile(parm, response,TERMINAL_UPDATE_DETAIL_DIR);
	}
	@RequestMapping(value="/getNetmealUpdateData.do", method = RequestMethod.POST)   
	@ResponseBody
	//@PermissionBean(model="terminalInfo", privilegeValue="getTerminalInfoCityUser2GList")
	public void getNetmealUpdateData(@RequestBody Map<String,String> parm,HttpServletResponse response)throws Exception{
		readReportJsonFile(parm, response,NETMEAL_UPDATE_DIR);
	}
	@RequestMapping(value="/getNetmealDetailUpdateData.do", method = RequestMethod.POST)   
	@ResponseBody
	//@PermissionBean(model="terminalInfo", privilegeValue="getTerminalInfoCityUser2GList")
	public void getNetmealDetailUpdateData(@RequestBody Map<String,String> parm,HttpServletResponse response)throws Exception{
		readReportJsonFile(parm, response,NETMEAL_UPDATE_DETAIL_DIR);
	}
	@RequestMapping(value="/getNetworkUpdateDetailData.do", method = RequestMethod.POST)   
	@ResponseBody
	//@PermissionBean(model="terminalInfo", privilegeValue="getTerminalInfoCityUser2GList")
	public void getNetworkUpdateDetailData(@RequestBody Map<String,String> parm,HttpServletResponse response)throws Exception{
		readReportJsonFile(parm, response,NETWORK_UPDATE_DETAIL_DIR);
	}
	
}
