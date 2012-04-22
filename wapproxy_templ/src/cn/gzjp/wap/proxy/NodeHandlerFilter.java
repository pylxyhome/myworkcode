package cn.gzjp.wap.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;

import cn.gzjp.wap.proxy.html.handler.NodeHandler;

@SuppressWarnings("unchecked")
public class NodeHandlerFilter implements NodeFilter {

	private static final long serialVersionUID = 1L;
	private static Log log=LogFactory.getLog(NodeHandlerFilter.class);
	private Map<Class,Object> map=new HashMap<Class,Object>();

	@Override
	public boolean accept(Node node) {
		Class type=node.getClass();
		Object object=map.get(type);
		if(object==null)return false;
		if(object instanceof NodeHandler){
			NodeHandler handler=(NodeHandler)object;		
			try{
				//TODO 这里要做异常处理
				handler.handle(node);
			}catch(Exception ex){
				log.error("",ex);
			}
		}else{
			List<NodeHandler> list=(List<NodeHandler>)object;
			for(NodeHandler handler:list){
				try{
					//TODO 这里也要做异常处理
					handler.handle(node);
				}catch(Exception ex){
					log.error("",ex);
				}
			}
		}
		return false;
	}
	
	public void register(NodeHandler handle){
		Object object=map.get(handle.getType());
		if(object==null){
			map.put(handle.getType(), handle);
		}else{
			if(object instanceof NodeHandler){
				List<NodeHandler> items=new ArrayList();
				NodeHandler old=(NodeHandler)object;
				items.add(old);
				items.add(handle);
				map.put(handle.getType(), items);
			}else{
				List<NodeHandler> list=(List<NodeHandler>)object;
				list.add(handle);
			}
		}
	}
}
