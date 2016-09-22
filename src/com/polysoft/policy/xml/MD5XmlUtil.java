package com.polysoft.policy.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class MD5XmlUtil {

	public static final String XML_NODE_ROOT = "files";
	public static final String XML_NODE_CHILD = "file";
	
	public static final String XML_ATTR_ISUPDATA = "isUpdate";
	public static final String XML_ATTR_MD5 = "md5";
	public static final String XML_ATTR_PATH = "path";
	public static final String XML_ATTR_SIZE = "size";
	public static final String XML_ATTR_PACKAGENAME = "packageName";
	
	
	public static String getAttrIsUpdate(Element element) {
		return getAttrValue(element, XML_ATTR_ISUPDATA);
	}
	
	public static String getAttrMd5(Element element) {
		return getAttrValue(element, XML_ATTR_MD5);
	}
	
	public static String getAttrPath(Element element) {
		return getAttrValue(element, XML_ATTR_PATH);
	}
	public static String getAttrSize(Element element) {
		return getAttrValue(element, XML_ATTR_SIZE);
	}
	public static String getAttrPackageName(Element element) {
		return getAttrValue(element, XML_ATTR_PACKAGENAME);
	}
	
	
	public static String getAttrValue(Node node, String attrName) {
		return getAttrValue((Element) node, attrName);
	}
	
	public static String getAttrValue(Element element, String attrName) {
		return element.getAttribute(attrName);
	}
}
