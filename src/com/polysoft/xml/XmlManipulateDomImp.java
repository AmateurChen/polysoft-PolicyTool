package com.polysoft.xml;

import org.w3c.dom.Document;

public interface XmlManipulateDomImp {

	/** XML�����ĸ��ڵ�*/
	public Object manipulate(Document document);
	
	/** ��ȡXML��Ҫ�����Ķ���������� �ļ���·�����ļ����ļ��������������迴ʵ����*/
	public Object getManipulateObj();
	
}
