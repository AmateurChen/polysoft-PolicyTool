package com.polysoft.policy.xml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.polysoft.policy.imp.OperationXmlInfoImp;
import com.polysoft.xml.XmlManipulateDomImp;

public class Md5XmlCreate implements XmlManipulateDomImp {

	Map<String, OperationXmlInfoImp> valueMap = new HashMap<String, OperationXmlInfoImp>();
	
	public Md5XmlCreate(Map<String, OperationXmlInfoImp> valueMap) {
		// TODO Auto-generated constructor stub
		this.valueMap = valueMap;
	}
	
	@Override
	public Object manipulate(Document document) {
		// TODO Auto-generated method stub
		Element rootElement = document.createElement(MD5XmlUtil.XML_NODE_ROOT);
		document.appendChild(rootElement);
		Iterator<Entry<String, OperationXmlInfoImp>> iterator = valueMap.entrySet().iterator();
		while(iterator.hasNext()) {
			Element itemElement = document.createElement(MD5XmlUtil.XML_NODE_CHILD);
			rootElement.appendChild(itemElement);
			OperationXmlInfoImp imp = iterator.next().getValue();
			imp.addXmlAttrValue(itemElement);
		}
		
		return true;
	}

	@Override
	public Object getManipulateObj() {
		// TODO Auto-generated method stub
		return null;
	}

}
