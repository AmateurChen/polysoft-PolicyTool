package com.polysoft.policy.xml;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.polysoft.policy.imp.OperationXmlInfoImp;
import com.polysoft.xml.XmlManipulateDom;
import com.polysoft.xml.XmlManipulateDomImp;
import com.polysoft.xml.XmlManipulateImp;

public class Md5XmlParser implements XmlManipulateDomImp {

	private Map<String, OperationXmlInfoImp> valueMap = new TreeMap<String, OperationXmlInfoImp>();
	
	public static void main(String[] args) {
		String filePath = "C:\\Users\\Thinkpad\\Desktop\\md5_2.0.xml";
		XmlManipulateImp imp = new XmlManipulateDom();
		try {
			Map<?, ?> result = (Map<?, ?>) imp.parser(new Md5XmlParser(), new File(filePath));
			Iterator<?> iterator = result.keySet().iterator();
			while(iterator.hasNext()) {
				Object key = iterator.next();
				Object value = result.get(key);
				System.out.println(value);
			}
			System.out.println("count is ===> " + result.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public Object manipulate(Document document) {
		// TODO Auto-generated method stub
		Element rootElement = document.getDocumentElement();
		NodeList childNodes = rootElement.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node item = childNodes.item(i);
			String tag = item.getNodeName();
			if(MD5XmlUtil.XML_NODE_CHILD.equals(tag)) {
				this.parserFileNode(item);
			}
		}
		
		return this.valueMap;
	}

	@Override
	public Object getManipulateObj() {
		// TODO Auto-generated method stub
		return null;
	}

	
	private void parserFileNode(Node fileNode) {
		Element element = (Element) fileNode;
		
		String path = MD5XmlUtil.getAttrPath(element);
		OperationXmlInfoImp imp = null;
		if(path.endsWith(".apk")) {
			imp = new Md5XmlApkInfo();
		} else {
			imp = new Md5XmlInfo();
		}
		
		imp.operationValue(element, path);
		valueMap.put(path, imp);
	}
	
	
}
