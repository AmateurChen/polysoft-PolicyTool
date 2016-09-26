package com.polysoft.policy.xml;

import java.util.Map;

import com.polysoft.policy.imp.OperationXmlInfoImp;
import com.polysoft.policy.release.PathTool;
import com.polysoft.policy.release.ReleaseFile;
import com.polysoft.policy.release.ReleaseInfo;
import com.polysoft.policy.release.ServerConfigInfo;
import com.polysoft.policy.sftp.SFTPDownLocalyPathFile;
import com.polysoft.policy.sftp.SFTPOperation;
import com.polysoft.policy.sftp.SFTPOperatonImp;
import com.polysoft.policy.sftp.SFTPUpload;
import com.polysoft.utils.FileUtil;
import com.polysoft.xml.XmlManipulateDom;
import com.polysoft.xml.XmlManipulateDomImp;
import com.polysoft.xml.XmlManipulateImp;

public class Md5XmlManager {

	public static void newMd5Config(String releaseFileDir, String configOutPath) {
		ReleaseFile releaseFile = new ReleaseFile(releaseFileDir);
		Map<String, OperationXmlInfoImp> md5Map = releaseFile.getMd5Map();
		Map<String, OperationXmlInfoImp> productMd5Map = releaseFile.getProductMd5Map();
		
		if(!md5Map.isEmpty()) {//有数据
			String outFilePath = PathTool.getOutMd5FilePath(configOutPath);
			FileUtil.deleteFile(outFilePath);
			createXml(new Md5XmlCreate(md5Map), outFilePath);
		} 
		if(!productMd5Map.isEmpty()) {
			String outFilePath = PathTool.getOutMd5ProductFilePath(configOutPath);
			FileUtil.deleteFile(outFilePath);
			createXml(new Md5XmlCreate(productMd5Map), outFilePath);
		}
	}
	
	public static void incrementalRelease(ReleaseInfo info) {
		String backupsPath = info.getBackupsFileDir() + "/" + info.getVersion();
		String releaseFilesPath = info.getReleaseFileDir();
		String serverPath = info.getReleaseServerDir();
		
		ReleaseFile releaseFile = new ReleaseFile(releaseFilesPath);
		Map<String, OperationXmlInfoImp> md5Map = releaseFile.getMd5Map();
		Map<String, OperationXmlInfoImp> productMd5Map = releaseFile.getProductMd5Map();
		if(md5Map.isEmpty() || productMd5Map.isEmpty()) {
			System.err.println("需要发布的文件为空==> "+ releaseFilesPath);
			return ;
		}
		
		String environment = info.getEnvironment();
		ServerConfigInfo serverConfig = PathTool.getServerConfig(environment);
		SFTPOperatonImp sftpImp = new SFTPOperation(serverConfig);
		//下载文件备份
		sftpImp.downloadFile(new SFTPDownLocalyPathFile(serverPath, releaseFilesPath, backupsPath));
		
		// 上传文件
		sftpImp.uploadFile(new SFTPUpload(serverPath, releaseFilesPath));
		
		sftpImp.close();
	}
	
	
	private static void createXml(XmlManipulateDomImp domImp, String outFilePath) {
		try {
			XmlManipulateImp xmlManipulateImp = new XmlManipulateDom();
			xmlManipulateImp.createXml(domImp, outFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
