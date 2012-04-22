package ${package};
/**
*@author gzwenny
*Created:${now?string("yyyy-MM-dd HH:mm:ss")}
*/
public class ${message.className}{

	<#list message.types as type>
	private ${type.typeName} ${type.name};
	</#list>
	
	public ${message.className}(){}
	
	<#list message.types as type>
	public ${type.typeName} get${type.name?cap_first}(){
		return ${type.name};
	}
		
	public void set${type.name?cap_first}(${type.typeName} ${type.name}){
		this.${type.name}=${type.name};
	}
	
	</#list>
	public String toString(){
		StringBuffer sb=new StringBuffer();
		sb.append('[');
		<#list message.types as type>
		sb.append("${type.name}="+${type.name}+",");
		</#list>
		sb.deleteCharAt(sb.length()-1);
		sb.append(']');
		return sb.toString();
	}
}