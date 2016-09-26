package com.polysoft.policy.ui;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import com.polysoft.policy.imp.OperationXmlInfoImp;
import com.polysoft.policy.release.PathTool;
import com.polysoft.policy.release.ReleaseFile;
import com.polysoft.policy.release.ReleaseInfo;
import com.polysoft.policy.release.ServerConfigInfo;
import com.polysoft.policy.release.ServerConfigOperation;
import com.polysoft.policy.sftp.SFTPDownLocalyPathFile;
import com.polysoft.policy.sftp.SFTPOperation;
import com.polysoft.policy.sftp.SFTPOperatonImp;
import com.polysoft.policy.sftp.SFTPUpload;
import com.polysoft.policy.xml.Md5XmlCreate;
import com.polysoft.utils.DateUtil;
import com.polysoft.utils.FileUtil;
import com.polysoft.utils.MD5FileUtil;
import com.polysoft.utils.TextUtil;
import com.polysoft.utils.ZipUtil;
import com.polysoft.xml.XmlManipulateDom;
import com.polysoft.xml.XmlManipulateImp;

public class ReleaseOperation {

	SFTPOperatonImp sftpImp;
	ServerConfigOperation serverConfig;
	
	public static void main(String[] args) {
		
		ReleaseInfo info = new ReleaseInfo();
		// 发布环境
		info.setEnvironment("17技术测试环境");
		// 发布版本
		info.setVersion("2.001.20160926001");
		// 发布内容
		info.setContent("11");
		// 备份目录
		info.setBackupsFileDir("E:/cxtest/backups");
		// 发布至服务端目录
		info.setReleaseServerDir("/cxtest/server/transferServer1/deploy/ROOT.war/file/update/all2.0");
		// 发布文件目录
		info.setReleaseFileDir("E:/cxtest/releaseDir");
		
		ReleaseOperation operation = new ReleaseOperation();
		String checkResult = operation.checkReleaseInfo(info);
		if(!TextUtil.isEmpty(checkResult)) {
			System.out.println(checkResult);
		} else {
			operation.initSftpImp(info.getEnvironment());
			operation.clickRelease(info);
		}
	}
	
	public ReleaseOperation() {
		// TODO Auto-generated constructor stub
	}
	
	private void clickRelease(ReleaseInfo info) {
		this.initBackupsFilePath(info);
		System.out.println("==> " + "开始备份文件");
		//备份
		this.sftpImp.downloadFile(new SFTPDownLocalyPathFile(info));
		System.out.println("==> " + "备份文件完成");
		System.out.println("==> " + "开始上传文件");
		//上传文件
		this.sftpImp.uploadFile(new SFTPUpload(info));
		System.out.println("==> " + "上传文件完成");
		
		MD5FileUtil md5util = new MD5FileUtil();
		String md5XmlConfigMd5 = null;
		String productMd5ConfigMd5 = null;
		
		System.out.println("==> " + "开始获取发布文件的md5值");
		// 开始生成配置文件
		ReleaseFile releaseFile = new ReleaseFile(info.getReleaseFileDir());
		System.out.println("==> " + "发布文件的md5值获取完成");
		Map<String, OperationXmlInfoImp> md5Map = releaseFile.getMd5Map();
		if(!md5Map.isEmpty()) {// 生成md5配置文件
			Map<String, OperationXmlInfoImp> serverMd5Map = this.serverConfig.getMd5Map();
			this.mergeXmlInfo(serverMd5Map, md5Map);
			
			String xmlOutPath = info.getReleaseFileDir() + "/" + this.serverConfig.getMd5ConfigName();
			
			ServerConfigInfo configInfo = this.serverConfig.getServerConfigInfo();
			String serverPath = new File(configInfo.getMd5FilePath()).getParent();
			
			this.uploadMd5Config(serverMd5Map, xmlOutPath, serverPath);
			md5XmlConfigMd5 = md5util.getFileMD5String(new File(xmlOutPath));
		}
		
		Map<String, OperationXmlInfoImp> productMd5Map = releaseFile.getProductMd5Map();
		if(!productMd5Map.isEmpty()) {// 生成 productMd5配置文件
			Map<String, OperationXmlInfoImp> serverProductMd5Map = this.serverConfig.getProductMd5Map();
			this.mergeXmlInfo(serverProductMd5Map, productMd5Map);
			
			String xmlOutPath = info.getReleaseFileDir() + "/" + this.serverConfig.getProductMd5ConfigName();
			
			ServerConfigInfo configInfo = this.serverConfig.getServerConfigInfo();
			String serverPath = new File(configInfo.getProductMd5FilePath()).getParent();
			
			this.uploadMd5Config(serverProductMd5Map, xmlOutPath, serverPath);
			productMd5ConfigMd5 = md5util.getFileMD5String(new File(xmlOutPath));
		}
		
		if(!TextUtil.isEmpty(md5XmlConfigMd5) || !TextUtil.isEmpty(productMd5ConfigMd5)) {
			//修改Md5配置文件
			this.serverConfig.setMd5Config(md5XmlConfigMd5, productMd5ConfigMd5);
			String filePath = this.serverConfig.saveMd5Config(info.getReleaseFileDir());
			String md5ConfigFilePath = this.serverConfig.getServerConfigInfo().getMd5ConfigFilePath();
			String serverPath = new File(md5ConfigFilePath).getParent().replaceAll("[\\\\]+", "/");
			// 上传配置文件
			this.sftpImp.uploadFile(serverPath, filePath);
		}
		
		String serverVersion = this.serverConfig.getServerVersion();
		if(!info.getVersion().startsWith(serverVersion)) {// 与服务端版本不一致
			//修改版本配置文件 并上传
			this.uploadVersionConfig(info.getVersion(), info.getReleaseFileDir());
			this.uploadUpdateVersionConfig(info.getVersion(), info.getReleaseFileDir());
		}
		
		this.serverConfig.backupsDownloadConfigFile(info.getBackupsFileDir());
		File compressFiles = new File(info.getBackupsFileDir());
		ZipUtil.compressFiles(compressFiles, compressFiles.getParentFile(), compressFiles.getName(), "polysoft");
		System.out.println("==> " + "压缩文件完成");
		compressFiles.delete();
	}
	
	private void uploadUpdateVersionConfig(String releaseVersion, String releaseFileDir) {
		this.serverConfig.setUpdateVersion(releaseVersion, "");
		String filePath = this.serverConfig.saveUpdateVersionConfig(releaseFileDir);
		String updateContentsFilePath = this.serverConfig.getServerConfigInfo().getUpdateContentsFilePath();
		String serverPath = new File(updateContentsFilePath).getParent().replaceAll("[\\\\]+", "/");
		this.sftpImp.uploadFile(serverPath, filePath);
	}
	
	private void uploadVersionConfig(String releaseVersion, String releaseFileDir) {
		this.serverConfig.setServerVersion(releaseVersion);
		String filePath = this.serverConfig.saveServerVersionConfig(releaseFileDir);
		String versionConfigFilePath = this.serverConfig.getServerConfigInfo().getVersionConfigFilePath();
		String serverpath = new File(versionConfigFilePath).getParent().replaceAll("[\\\\]+", "/");
		this.sftpImp.uploadFile(serverpath, filePath);
	}
	
	private void uploadMd5Config(Map<String, OperationXmlInfoImp> md5Map, String xmlOutPath, String serverPath) {
		XmlManipulateImp imp = new XmlManipulateDom();
		try {
			imp.createXml(new Md5XmlCreate(md5Map), xmlOutPath);
			this.sftpImp.uploadFile(serverPath.replaceAll("[\\\\]+", "/"), xmlOutPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void mergeXmlInfo(Map<String, OperationXmlInfoImp> serverXmlMap, Map<String, OperationXmlInfoImp> releaseMap) {
		Iterator<String> iterator = releaseMap.keySet().iterator();
		while(iterator.hasNext()) {
			String key = iterator.next();
			OperationXmlInfoImp xmlInfoImp = serverXmlMap.get(key);
			if(null != xmlInfoImp) {
				xmlInfoImp.replace(releaseMap.get(key));
			} else {
				serverXmlMap.put(key, releaseMap.get(key));
			}
		}
	}
	
	private void initBackupsFilePath(ReleaseInfo info) {
		String backupsFileDir = info.getBackupsFileDir();
		long currentTimeMillis = System.currentTimeMillis();
		String dateStr = DateUtil.transForm(currentTimeMillis, "yyyy-MM-dd hh-mm-ss");
		info.setBackupsFileDir(backupsFileDir + "/" + info.getVersion() + "_" +dateStr);
	}
	
	private void initSftpImp(String environment) {
		ServerConfigInfo configInfo = PathTool.getServerConfig(environment);
		this.sftpImp = new SFTPOperation(configInfo);
		this.serverConfig = new ServerConfigOperation(sftpImp, configInfo);
		
		System.out.println("serverVersion===> " + this.serverConfig.getServerVersion());
		
	}
	
	
	public String checkReleaseInfo(ReleaseInfo info) {
		StringBuffer sb = new StringBuffer();
		if(TextUtil.isEmpty(info.getEnvironment())) {
			sb.append("发布环境未选择" + "\n");
		} 
		if(TextUtil.isEmpty(info.getContent())) {
			sb.append("发布内容未填写" + "\n");
		}
		if(TextUtil.isEmpty(info.getVersion())) {
			sb.append("发布版本内容未填写" + "\n");
		}
		
		String releaseServerDir = info.getReleaseServerDir();
		if(TextUtil.isEmpty(releaseServerDir)) {
			sb.append("发布至服务端目录未填写" + "\n");
		} else if(FileUtil.isFile(releaseServerDir)) {
			sb.append("发布至服务端目录不能为文件地址，必须为目录地址" + "\n");
		}
		String backupsFileDir = info.getBackupsFileDir();
		if(TextUtil.isEmpty(backupsFileDir)) {
			sb.append("备份目录未填写" + "\n");
		} else if(FileUtil.isFile(backupsFileDir)) {
			sb.append("备份目录不能为文件地址，必须为目录地址" + "\n");
		}
		
		String releaseFileDir = info.getReleaseFileDir();
		if(TextUtil.isEmpty(releaseFileDir)) {
			sb.append("发布文件目录未填写" + "\n");
		} else if(FileUtil.isFile(releaseFileDir)) {
			sb.append("发布文件目录不能为文件地址，必须为目录地址" + "\n");
		}
		
		return sb.toString();
	}
	
}
