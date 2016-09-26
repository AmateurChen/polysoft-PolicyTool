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
		// ��������
		info.setEnvironment("17�������Ի���");
		// �����汾
		info.setVersion("2.001.20160926001");
		// ��������
		info.setContent("11");
		// ����Ŀ¼
		info.setBackupsFileDir("E:/cxtest/backups");
		// �����������Ŀ¼
		info.setReleaseServerDir("/cxtest/server/transferServer1/deploy/ROOT.war/file/update/all2.0");
		// �����ļ�Ŀ¼
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
		System.out.println("==> " + "��ʼ�����ļ�");
		//����
		this.sftpImp.downloadFile(new SFTPDownLocalyPathFile(info));
		System.out.println("==> " + "�����ļ����");
		System.out.println("==> " + "��ʼ�ϴ��ļ�");
		//�ϴ��ļ�
		this.sftpImp.uploadFile(new SFTPUpload(info));
		System.out.println("==> " + "�ϴ��ļ����");
		
		MD5FileUtil md5util = new MD5FileUtil();
		String md5XmlConfigMd5 = null;
		String productMd5ConfigMd5 = null;
		
		System.out.println("==> " + "��ʼ��ȡ�����ļ���md5ֵ");
		// ��ʼ���������ļ�
		ReleaseFile releaseFile = new ReleaseFile(info.getReleaseFileDir());
		System.out.println("==> " + "�����ļ���md5ֵ��ȡ���");
		Map<String, OperationXmlInfoImp> md5Map = releaseFile.getMd5Map();
		if(!md5Map.isEmpty()) {// ����md5�����ļ�
			Map<String, OperationXmlInfoImp> serverMd5Map = this.serverConfig.getMd5Map();
			this.mergeXmlInfo(serverMd5Map, md5Map);
			
			String xmlOutPath = info.getReleaseFileDir() + "/" + this.serverConfig.getMd5ConfigName();
			
			ServerConfigInfo configInfo = this.serverConfig.getServerConfigInfo();
			String serverPath = new File(configInfo.getMd5FilePath()).getParent();
			
			this.uploadMd5Config(serverMd5Map, xmlOutPath, serverPath);
			md5XmlConfigMd5 = md5util.getFileMD5String(new File(xmlOutPath));
		}
		
		Map<String, OperationXmlInfoImp> productMd5Map = releaseFile.getProductMd5Map();
		if(!productMd5Map.isEmpty()) {// ���� productMd5�����ļ�
			Map<String, OperationXmlInfoImp> serverProductMd5Map = this.serverConfig.getProductMd5Map();
			this.mergeXmlInfo(serverProductMd5Map, productMd5Map);
			
			String xmlOutPath = info.getReleaseFileDir() + "/" + this.serverConfig.getProductMd5ConfigName();
			
			ServerConfigInfo configInfo = this.serverConfig.getServerConfigInfo();
			String serverPath = new File(configInfo.getProductMd5FilePath()).getParent();
			
			this.uploadMd5Config(serverProductMd5Map, xmlOutPath, serverPath);
			productMd5ConfigMd5 = md5util.getFileMD5String(new File(xmlOutPath));
		}
		
		if(!TextUtil.isEmpty(md5XmlConfigMd5) || !TextUtil.isEmpty(productMd5ConfigMd5)) {
			//�޸�Md5�����ļ�
			this.serverConfig.setMd5Config(md5XmlConfigMd5, productMd5ConfigMd5);
			String filePath = this.serverConfig.saveMd5Config(info.getReleaseFileDir());
			String md5ConfigFilePath = this.serverConfig.getServerConfigInfo().getMd5ConfigFilePath();
			String serverPath = new File(md5ConfigFilePath).getParent().replaceAll("[\\\\]+", "/");
			// �ϴ������ļ�
			this.sftpImp.uploadFile(serverPath, filePath);
		}
		
		String serverVersion = this.serverConfig.getServerVersion();
		if(!info.getVersion().startsWith(serverVersion)) {// �����˰汾��һ��
			//�޸İ汾�����ļ� ���ϴ�
			this.uploadVersionConfig(info.getVersion(), info.getReleaseFileDir());
			this.uploadUpdateVersionConfig(info.getVersion(), info.getReleaseFileDir());
		}
		
		this.serverConfig.backupsDownloadConfigFile(info.getBackupsFileDir());
		File compressFiles = new File(info.getBackupsFileDir());
		ZipUtil.compressFiles(compressFiles, compressFiles.getParentFile(), compressFiles.getName(), "polysoft");
		System.out.println("==> " + "ѹ���ļ����");
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
			sb.append("��������δѡ��" + "\n");
		} 
		if(TextUtil.isEmpty(info.getContent())) {
			sb.append("��������δ��д" + "\n");
		}
		if(TextUtil.isEmpty(info.getVersion())) {
			sb.append("�����汾����δ��д" + "\n");
		}
		
		String releaseServerDir = info.getReleaseServerDir();
		if(TextUtil.isEmpty(releaseServerDir)) {
			sb.append("�����������Ŀ¼δ��д" + "\n");
		} else if(FileUtil.isFile(releaseServerDir)) {
			sb.append("�����������Ŀ¼����Ϊ�ļ���ַ������ΪĿ¼��ַ" + "\n");
		}
		String backupsFileDir = info.getBackupsFileDir();
		if(TextUtil.isEmpty(backupsFileDir)) {
			sb.append("����Ŀ¼δ��д" + "\n");
		} else if(FileUtil.isFile(backupsFileDir)) {
			sb.append("����Ŀ¼����Ϊ�ļ���ַ������ΪĿ¼��ַ" + "\n");
		}
		
		String releaseFileDir = info.getReleaseFileDir();
		if(TextUtil.isEmpty(releaseFileDir)) {
			sb.append("�����ļ�Ŀ¼δ��д" + "\n");
		} else if(FileUtil.isFile(releaseFileDir)) {
			sb.append("�����ļ�Ŀ¼����Ϊ�ļ���ַ������ΪĿ¼��ַ" + "\n");
		}
		
		return sb.toString();
	}
	
}
