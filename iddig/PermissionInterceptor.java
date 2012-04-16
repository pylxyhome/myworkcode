package cn.gzjp.idigg.server.interceptor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.gzjp.idigg.server.common.annotation.PermissionBean;
import cn.gzjp.idigg.server.model.Permission;
import cn.gzjp.idigg.server.model.User;
import cn.gzjp.idigg.server.service.UserService;
import cn.gzjp.idigg.server.tx.TransactionContext;
import cn.gzjp.idigg.server.util.Configure;
/**
 * 权限拦截器
 * @description
 * @author panyl
 * @date 2012-2-13
 */
public class PermissionInterceptor extends HandlerInterceptorAdapter {
	private static Log log = LogFactory.getLog(PermissionInterceptor.class);
	@Resource
	private UserService userService;
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		String url = request.getRequestURL().toString();
		response.setCharacterEncoding("utf-8");
		if(url.contains("/ws/")){ //对外部提供的接口
			String clientIp=getClientIp(request);
//			log.info("clientIp:"+clientIp);
			if(Configure.get().isAllowIp(clientIp)){
				String username=request.getHeader("username");
				String password=request.getHeader("password");
				if(!StringUtils.isBlank(username)&&!StringUtils.isBlank(password)){
					User user=new User(username,password);
					Long userid=userService.isUser(user);
					if(userid!=null){
//						log.info("user:"+user.getUsername()+" login in");
						request.setAttribute("userid", userid);
						return true;
					}
				}else{
//					log.info("----username or password illegality-----");
					printErrorMsg(response,"username or password illegality");
					return false;
				}
			}
//			log.info("----illegality ip call-----");
			printErrorMsg(response,"ip illegality:"+clientIp);
			return false;
		}
		if (!url.endsWith("isUser.do") && !url.endsWith("logout.do")
				&& !url.endsWith("interface.do")) {
			HttpSession session = request.getSession();
			Map<String,Permission> permissionValues = (Map<String,Permission>) session.getAttribute("permissionValues");
			String simpleName = url.substring(url.lastIndexOf("/") + 1);
			String methodName = simpleName.substring(0,simpleName.lastIndexOf("."));
//			log.info("methodName:" + methodName);
			if(TransactionContext.getCurrentUser()==1l){
				return true;
			}
			Method currentMethod = null;
			Method[] methods = handler.getClass().getMethods();
			for (Method m : methods) {
				if (m.getName().equals(methodName)) {
					currentMethod = m;
					break;
				}
			}
			if (currentMethod != null
					&& currentMethod.isAnnotationPresent(PermissionBean.class)) {
				PermissionBean permissionBean = currentMethod
						.getAnnotation(PermissionBean.class);
//				log.info("model:" + permissionBean.model());
//				log.info("value:" + permissionBean.privilegeValue());
				if(permissionValues.containsKey(permissionBean.privilegeValue())){
//					log.info("执行模块:"+permissionValues.get(permissionBean.privilegeValue()).getName());
					return true;
				}
				else{
//					log.info("没有该模块权限,请联系管理员！"+permissionBean.privilegeValue());
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * 获取Ip地址
	 * @return
	 */
	protected String getClientIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	private void printErrorMsg(HttpServletResponse response,String msg) throws IOException{
		response.setCharacterEncoding("utf-8");
		response.getWriter().print(msg);
	}
}
