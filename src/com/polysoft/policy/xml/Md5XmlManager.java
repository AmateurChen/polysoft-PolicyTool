package com.polysoft.policy.xml;

import java.util.Map;

import com.jcraft.jsch.ChannelSftp;
import com.polysoft.policy.imp.OperationXmlInfoImp;
import com.polysoft.policy.release.PathTool;
import com.polysoft.policy.release.ReleaseFile;
import com.polysoft.policy.release.ReleaseInfo;
import com.polysoft.policy.release.ServerConfig;
import com.polysoft.policy.sftp.SFTPConnectParamter;
import com.polysoft.policy.sftp.SFTPManager;
import com.polysoft.utils.FileUtil;
import com.polysoft.utils.RegexUtil;
import com.polysoft.xml.XmlManipulateDom;
import com.polysoft.xml.XmlManipulateDomImp;
import com.polysoft.xml.XmlManipulateImp;

public class Md5XmlManager {

	public static void main(String[] args) {
		String filePath = "E:\\cxtest\\nci全量包\\pad2.0生产更新文件全量包\\";
		String configOutPath = "E:\\cxtest\\nci全量包\\";
		newMd5Config(filePath, configOutPath);
		
		System.out.println("====> 完成" );
	}
	
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
	
	public static void incrementalRelease(ReleaseInfo releaseInfo) {
		ReleaseFile releaseFile = new ReleaseFile(releaseInfo.getReleaseFileDir());
		Map<String, OperationXmlInfoImp> md5Map = releaseFile.getMd5Map();
		Map<String, OperationXmlInfoImp> productMd5Map = releaseFile.getProductMd5Map();
		
		String environment = releaseInfo.getEnvironment();
		ServerConfig serverConfig = PathTool.getServerConfig(environment);
		
		ChannelSftp sftp = SFTPManager.connect(serverConfig);
//		SFTPManager.downloadFile(sftp, fileList, outPath);
		
		
		if(!md5Map.isEmpty()) {
			
		}
		
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
