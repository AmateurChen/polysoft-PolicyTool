package com.polysoft.policy.imp;

import java.io.File;

import org.w3c.dom.Element;

import com.polysoft.utils.MD5FileUtil;

public interface OperationXmlInfoImp {

	public void operationValue(Element element, String path);
	
	public void operationValue(MD5FileUtil md5Util, File file);
	
	public void addXmlAttrValue(Element element);
	
	public void replace(OperationXmlInfoImp xmlInfoImp); 
	
}
