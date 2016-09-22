package com.polysoft.policy.xml;

import java.io.File;

import org.w3c.dom.Element;

import com.polysoft.policy.imp.OperationXmlInfoImp;
import com.polysoft.utils.MD5FileUtil;
import com.polysoft.utils.RegexUtil;

public class Md5XmlInfo implements OperationXmlInfoImp {

	// 是否为必须升级1，0
	protected String isUpdate;
	//路径 /nci/...
	protected String path;
	//文件的md5
	protected String md5;
	// 文件的大小
	private long size;

	
	public String getIsUpdate() {
		return isUpdate;
	}

	public String getPath() {
		return path;
	}

	public String getMd5() {
		return md5;
	}

	public long getSize() {
		return size;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("isUpdate=" + this.isUpdate + "; ");
		sb.append("md5=" + this.md5 + "; ");
		sb.append("path=" + this.path + "; ");
		sb.append("size=" + this.size + "; ");
		
		return sb.toString();
	}

	@Override
	public void operationValue(Element element, String path) {
		// TODO Auto-generated method stub
		String isUpdate = MD5XmlUtil.getAttrIsUpdate(element);
		String md5 = MD5XmlUtil.getAttrMd5(element);
		String size = MD5XmlUtil.getAttrSize(element);
		
		this.isUpdate = isUpdate;
		this.path = path;
		this.md5 = md5;
		this.size = Long.parseLong(size);
	}

	@Override
	public void addXmlAttrValue(Element element) {
		// TODO Auto-generated method stub
		element.setAttribute(MD5XmlUtil.XML_ATTR_ISUPDATA, this.isUpdate);
		element.setAttribute(MD5XmlUtil.XML_ATTR_PATH, this.path);
		element.setAttribute(MD5XmlUtil.XML_ATTR_MD5, this.md5);
		element.setAttribute(MD5XmlUtil.XML_ATTR_SIZE, String.valueOf(this.size));
	}

	@Override
	public void operationValue(MD5FileUtil md5Util, File file) {
		// TODO Auto-generated method stub
		String filePath = RegexUtil.replaceMd5Path(file.getAbsolutePath());
		this.path = filePath;
		this.isUpdate = RegexUtil.fileIsUpdate(path);
		this.size = file.length();
		this.md5 = md5Util.getFileMD5String(file);
	}


	@Override
	public void replace(OperationXmlInfoImp xmlInfoImp) {
		// TODO Auto-generated method stub
		Md5XmlInfo info = (Md5XmlInfo) xmlInfoImp;
		this.path = info.getPath();
		this.md5 = info.getMd5();
		this.isUpdate = info.getIsUpdate();
		this.size = info.getSize();
	}


}
