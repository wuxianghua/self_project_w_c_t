package net.imoran.auto.morwechat.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * bean 对象 需要有 
 * @author xinhua.shi
 *
 */
public abstract class XmlUtils {

	/**
	 * bean 转换 xml
	 * @param obj
	 * @return
	 */
	 public static String beanToXml(Object obj) {
	        XStream xstream = new XStream(new DomDriver("utf8"));
	        xstream.processAnnotations(obj.getClass()); // 识别obj类中的注解
	        
	        /*
	         // 以压缩的方式输出XML
	         StringWriter sw = new StringWriter();
	         xstream.marshal(obj, new CompactWriter(sw));
	         return sw.toString();
	         */
	        
	        // 以格式化的方式输出XML
	        return xstream.toXML(obj);
	    }
	    
	 /**
	  * xml转换 bean
	  * @param xmlStr
	  * @param cls
	  * @return
	  */
    public static <T> T xmlToBean(String xmlStr, Class<T> cls) {
        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(cls);
        @SuppressWarnings("unchecked")
        T t = (T) xstream.fromXML(xmlStr);
        return t;
    } 
}
