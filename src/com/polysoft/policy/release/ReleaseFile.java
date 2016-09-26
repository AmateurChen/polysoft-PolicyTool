package com.polysoft.policy.release;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import com.polysoft.policy.imp.OperationXmlInfoImp;
import com.polysoft.policy.xml.Md5ProductXmlInfo;
import com.polysoft.policy.xml.Md5XmlApkInfo;
import com.polysoft.policy.xml.Md5XmlInfo;
import com.polysoft.utils.MD5FileUtil;
import com.polysoft.utils.RegexUtil;

public class ReleaseFile {

	private Map<String, OperationXmlInfoImp> md5Map = new LinkedHashMap<String, OperationXmlInfoImp>();
	private Map<String, OperationXmlInfoImp> productMd5Map = new LinkedHashMap<String, OperationXmlInfoImp>();
	
	private MD5FileUtil md5util;
	private File releasefileDir;
	
	public ReleaseFile(String releaseDir) {
		// TODO Auto-generated constructor stub
		this.md5util = new MD5FileUtil();
		this.releasefileDir = new File(releaseDir);
		this.initFiles(releasefileDir);
	}
	
	private void initFiles(File file) {
		if(!file.isFile()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				initFiles(files[i]);
			}
		} else {
			String filePath = file.getAbsolutePath();
			if(RegexUtil.isProductZipPath(filePath)){
				Md5ProductXmlInfo info = new Md5ProductXmlInfo();
				info.operationValue(md5util, file);
				this.productMd5Map.put(info.getPath(), info);
			} else if(filePath.endsWith(".apk")) {
				Md5XmlApkInfo info = new Md5XmlApkInfo();
				info.operationValue(md5util, file);
				this.md5Map.put(info.getPath(), info);
			} else {
				Md5XmlInfo info = new Md5XmlInfo();
				info.operationValue(md5util, file);
				this.md5Map.put(info.getPath(), info);
			}
		}
	}

	
	public Map<String, OperationXmlInfoImp> getMd5Map() {
		return md5Map;
	}

	public Map<String, OperationXmlInfoImp> getProductMd5Map() {
		return productMd5Map;
	}

	public File getReleaseFileDir() {
		return this.releasefileDir;
	}


}
