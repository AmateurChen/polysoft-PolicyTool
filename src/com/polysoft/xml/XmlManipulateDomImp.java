package com.polysoft.xml;

import org.w3c.dom.Document;

public interface XmlManipulateDomImp {

	/** XML操作的根节点*/
	public Object manipulate(Document document);
	
	/** 获取XML需要操作的对象，输入输出 文件的路径、文件、文件流。具体类型需看实现类*/
	public Object getManipulateObj();
	
}
