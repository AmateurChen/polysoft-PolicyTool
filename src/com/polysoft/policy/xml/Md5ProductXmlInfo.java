package com.polysoft.policy.xml;

import java.io.File;

import org.w3c.dom.Element;

import com.polysoft.policy.imp.OperationXmlInfoImp;
import com.polysoft.utils.MD5FileUtil;
import com.polysoft.utils.RegexUtil;

public class Md5ProductXmlInfo implements OperationXmlInfoImp {

	private String path;
	private String md5;
	private String isUpdate;
	
	@Override
	public void operationValue(Element element, String path) {
		// TODO Auto-generated method stub
		this.path = path;
		this.md5 = MD5XmlUtil.getAttrMd5(element);
		this.isUpdate = MD5XmlUtil.getAttrIsUpdate(element);
	}

	@Override
	public void operationValue(MD5FileUtil md5Util, File file) {
		// TODO Auto-generated method stub
		String absolutePath = file.getAbsolutePath();
		this.md5 = md5Util.getFileMD5String(file);
		this.isUpdate = RegexUtil.fileIsUpdate(absolutePath);
		this.path = RegexUtil.replaceMd5Path(absolutePath);
	}

	@Override
	public void addXmlAttrValue(Element element) {
		// TODO Auto-generated method stub
		element.setAttribute(MD5XmlUtil.XML_ATTR_PATH, this.path);
		element.setAttribute(MD5XmlUtil.XML_ATTR_MD5, this.md5);
		element.setAttribute(MD5XmlUtil.XML_ATTR_ISUPDATA, this.isUpdate);
	}

	@Override
	public void replace(OperationXmlInfoImp xmlInfoImp) {
		// TODO Auto-generated method stub
		Md5ProductXmlInfo info = (Md5ProductXmlInfo) xmlInfoImp;
		this.isUpdate = info.getIsUpdate();
		this.path = info.getPath();
		this.md5 = info.getMd5();
	}

	
	public String getPath() {
		return path;
	}

	public String getMd5() {
		return md5;
	}

	public String getIsUpdate() {
		return isUpdate;
	}
}
