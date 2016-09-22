package com.polysoft.xml;

import java.io.File;
import java.io.InputStream;

public interface XmlManipulateImp {
	
	public Object parser(XmlManipulateDomImp domImp, InputStream inputXml) throws Exception ;
	
	public Object parser(XmlManipulateDomImp domImp, File xmlFile) throws Exception ;
	
	public Object parser(XmlManipulateDomImp domImp, String xmlInfo) throws Exception ;
	
	public Object createXml(XmlManipulateDomImp domImp, String outputFilePath) throws Exception;
}
