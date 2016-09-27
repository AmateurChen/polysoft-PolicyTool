package com.polysfot.policy.apk;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.polysoft.xml.XmlManipulateDomImp;

public class AndroidManifestXml implements XmlManipulateDomImp {

	@Override
	public Object manipulate(Document document) {
		// TODO Auto-generated method stub
		NodeList childNodes = document.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node item = childNodes.item(i);
			if("manifest".equals(item.getNodeName())) {
				this.parserManifestNode(item);
			}
		}
		return null;
	}

	@Override
	public Object getManipulateObj() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void parserManifestNode(Node node) {
		Element element = (Element) node;
		String packageName = element.getAttribute("package");
		String versionCode = element.getAttribute("android:versionCode");
		String versionName = element.getAttribute("android:versionName");
		System.out.println("packageName = "+ packageName);
		System.out.println("versionCode = "+ versionCode);
		System.out.println("versionName = "+ versionName);
	}

}
