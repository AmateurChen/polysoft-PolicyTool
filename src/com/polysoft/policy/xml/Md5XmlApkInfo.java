package com.polysoft.policy.xml;

import java.io.File;

import org.w3c.dom.Element;

import com.polysoft.policy.imp.OperationXmlInfoImp;
import com.polysoft.utils.ApkInfoTools;
import com.polysoft.utils.MD5FileUtil;
import com.polysoft.utils.RegexUtil;

public class Md5XmlApkInfo implements OperationXmlInfoImp {

	// �Ƿ�Ϊ��������1��0
	private String isUpdate;
	//·�� /nci/...
	private String path;
	//�ļ���md5
	private String md5;
	// �ļ��Ĵ�С
	private long size;
	// apk ����
	private String packageName;
	
	@Override
	public void operationValue(Element element, String path) {
		// TODO Auto-generated method stub
		String isUpdate = MD5XmlUtil.getAttrIsUpdate(element);
		String packageName = MD5XmlUtil.getAttrPackageName(element);
		String md5 = MD5XmlUtil.getAttrMd5(element);
		String size = MD5XmlUtil.getAttrSize(element);
		
		this.isUpdate = isUpdate;
		this.path = path;
		this.md5 = md5;
		this.size = Long.parseLong(size);
		this.packageName = packageName;
		
		
	}

	@Override
	public void operationValue(MD5FileUtil md5Util, File file) {
		// TODO Auto-generated method stub
		String absolutePath = file.getAbsolutePath();
		this.path = "/" + file.getName();
		this.isUpdate = RegexUtil.fileIsUpdate(path);
		this.size = file.length();
		this.md5 = md5Util.getFileMD5String(file);
		this.packageName = ApkInfoTools.getApkPackageName(absolutePath);
	}
	
	@Override
	public void addXmlAttrValue(Element element) {
		// TODO Auto-generated method stub
		element.setAttribute(MD5XmlUtil.XML_ATTR_PACKAGENAME, this.packageName);
		element.setAttribute(MD5XmlUtil.XML_ATTR_ISUPDATA, this.isUpdate);
		element.setAttribute(MD5XmlUtil.XML_ATTR_PATH, this.path);
		element.setAttribute(MD5XmlUtil.XML_ATTR_MD5, this.md5);
		element.setAttribute(MD5XmlUtil.XML_ATTR_SIZE, String.valueOf(this.size));
	}

	@Override
	public void replace(OperationXmlInfoImp xmlInfoImp) {
		// TODO Auto-generated method stub
		Md5XmlApkInfo info = (Md5XmlApkInfo) xmlInfoImp;
		this.path = info.getPath();
		this.md5 = info.getMd5();
		this.packageName = info.getPackageName();
		this.isUpdate = info.getIsUpdate();
		this.size = info.getSize();
	}

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

	public String getPackageName() {
		return packageName;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("isUpdate=" + this.isUpdate + "; ");
		sb.append("md5=" + this.md5 + "; ");
		sb.append("packageName=" + this.packageName + "; ");
		sb.append("path=" + this.path + "; ");
		sb.append("size=" + this.size + "; ");
		
		return sb.toString();
	}

	
}
