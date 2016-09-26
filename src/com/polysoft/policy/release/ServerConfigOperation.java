package com.polysoft.policy.release;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.polysoft.policy.PropertiesInfo;
import com.polysoft.policy.imp.OperationXmlInfoImp;
import com.polysoft.policy.sftp.SFTPOperatonImp;
import com.polysoft.policy.xml.Md5XmlParser;
import com.polysoft.utils.FileUtil;
import com.polysoft.utils.TextUtil;
import com.polysoft.xml.XmlManipulateDom;
import com.polysoft.xml.XmlManipulateImp;

public class ServerConfigOperation {

	private ServerConfigInfo config;
	private SFTPOperatonImp sftpImp;
	
	private String tempFileDirPath;
	private PropertiesInfo versonProperties;
	private PropertiesInfo updateVerProperties;
	private PropertiesInfo md5Properties;
	
	private Map<String, OperationXmlInfoImp> md5Map;
	private Map<String, OperationXmlInfoImp> productMd5Map;
	private String md5ConfigFileName;
	private String productMd5ConfigFileName;
	
	public ServerConfigOperation(SFTPOperatonImp sftpImp, ServerConfigInfo config) {
		// TODO Auto-generated constructor stub
		this.sftpImp = sftpImp;
		this.config = config;
		
		File tempFileDir = new File("temp");
		if(tempFileDir.exists()) tempFileDir.delete();
		
		tempFileDir.mkdirs();
		this.tempFileDirPath = tempFileDir.getAbsolutePath().replaceAll("[\\\\]+", "/");
	}
	
	
	public String getServerVersion() {
		if(null == this.versonProperties) {
			this.versonProperties = initServerPropertiesConfig(config.getVersionConfigFilePath());
		}
		return this.versonProperties.getValue("maxVersion", "");
	}
	
	public void setServerVersion(String version) {
		if(null == this.versonProperties) {
			this.versonProperties = initServerPropertiesConfig(config.getVersionConfigFilePath());
		}
		this.versonProperties.setValue("maxVersion", version);
	}
	
	public String saveServerVersionConfig(String outPathDir) {
		return this.versonProperties.save(outPathDir);
	}
	
	public String getUpdateVersion() {
		if(null == this.updateVerProperties) {
			this.updateVerProperties = initServerPropertiesConfig(config.getUpdateContentsFilePath());
		}
		
		return this.updateVerProperties.getValue("version", "");
	}
	
	public void setUpdateVersion(String version, String releaseInfo) {
		if(null == this.updateVerProperties) {
			this.updateVerProperties = initServerPropertiesConfig(config.getUpdateContentsFilePath());
		}
		
		this.updateVerProperties.setValue("version", version);
		
		if(!TextUtil.isEmpty(releaseInfo)) {
			this.updateVerProperties.setValue("content-1", releaseInfo);
		}
	}
	
	public String saveUpdateVersionConfig(String outPathDir) {
		return this.updateVerProperties.save(outPathDir);
	}
	
	public void setMd5Config(String md5, String productMd5) {
		if(null == this.md5Properties) {
			this.md5Properties = initServerPropertiesConfig(config.getMd5ConfigFilePath());
		}
		if(!TextUtil.isEmpty(md5)) {
			this.md5Properties.setValue("md5", md5);
		}
		if(!TextUtil.isEmpty(productMd5)) {
			this.md5Properties.setValue("productMd5", productMd5);
		}
	}
	
	public String saveMd5Config(String outPathDir) {
		return this.md5Properties.save(outPathDir);
	}
	
	public Map<String, OperationXmlInfoImp> getMd5Map() {
		if(null == this.md5Map) {
			String filePath = downServerConfigFile(config.getMd5FilePath());
			this.md5Map = initMd5XmlConfig(filePath);
			this.md5ConfigFileName = new File(filePath).getName();
		}
		
		return this.md5Map;
	}
	
	public String getMd5ConfigName() {
		return this.md5ConfigFileName;
	}
	
	public Map<String, OperationXmlInfoImp> getProductMd5Map() {
		if(null == this.productMd5Map) {
			String filePath = downServerConfigFile(config.getProductMd5FilePath());
			this.productMd5Map = initMd5XmlConfig(filePath);
			this.productMd5ConfigFileName = new File(filePath).getName();
		}
		return this.productMd5Map;
	}
	
	public String getProductMd5ConfigName() {
		return this.productMd5ConfigFileName;
	}
	
	public ServerConfigInfo getServerConfigInfo() {
		return this.config;
	}
	
	public void backupsDownloadConfigFile(String outPathDir) {
		File file = new File(this.tempFileDirPath);
		File[] files = file.listFiles();
		File outPathFileDir = new File(outPathDir);
		for (int i = 0; i < files.length; i++) {
			FileUtil.moveFile(files[i], outPathFileDir);
		}
		file.delete();
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, OperationXmlInfoImp> initMd5XmlConfig(String xmlFilePath) {
		XmlManipulateImp imp = new XmlManipulateDom();
		try {
			return (Map<String, OperationXmlInfoImp>) imp.parser(new Md5XmlParser(), new File(xmlFilePath));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap<String, OperationXmlInfoImp>();
	}
	
	private PropertiesInfo initServerPropertiesConfig(String serverPath) {
		String filePath = this.downServerConfigFile(serverPath);
		return new PropertiesInfo(filePath);
	}
	
	private String downServerConfigFile(String serverPath) {
		String fileName = new File(serverPath).getName();
		String filePath = this.tempFileDirPath + "/" + fileName;
		this.sftpImp.downloadFile(serverPath, filePath);
		return filePath;
	}
}
