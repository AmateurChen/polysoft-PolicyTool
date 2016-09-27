package com.polysoft.policy.sftp;

import java.io.File;

import com.jcraft.jsch.SftpException;
import com.polysoft.policy.release.ReleaseInfo;

public class SFTPDownLocalyPathFile implements SFTPDownImp {

	private String outRootPath;
	private String releaseServerPath;
	private String releaseFileDir;
	
	public SFTPDownLocalyPathFile(ReleaseInfo info) {
		// TODO Auto-generated constructor stub
		this.outRootPath = info.getBackupsFileDir();
		this.releaseServerPath = info.getReleaseServerDir();
		this.releaseFileDir = info.getReleaseFileDir();
	}
	
	public SFTPDownLocalyPathFile(String serverRootPath, String releaseRootPath, String outRootPath) {
		// TODO Auto-generated constructor stub
		this.outRootPath = outRootPath;
		this.releaseServerPath = serverRootPath;
		this.releaseFileDir = releaseRootPath;
	}

	@Override
	public void downloadFiles(SFTPOperatonImp sftpImp) throws SftpException {
		// TODO Auto-generated method stub
		this.downFile(sftpImp, new File(this.releaseFileDir));
	}
	
	public void downFile(SFTPOperatonImp sftpImp, File file) throws SftpException {
		if(file.isFile()) {
			String serverFilePath = getServerFilePath(file);
			String downFileOutPath = getDownFileOutPath(file);
			
			File localyFile = new File(downFileOutPath);
			if(localyFile.exists()) localyFile.delete();
			if(sftpImp.isExists(serverFilePath)) {
				sftpImp.downloadFile(serverFilePath, downFileOutPath);
			} else {
				System.out.println("服务端无此文件，不备份===>" + serverFilePath);
			}
			
		} else {
			File[] listFiles = file.listFiles();
			for (int i = 0; i < listFiles.length; i++) {
				this.downFile(sftpImp, listFiles[i]);
			}
		}
	}
	
	private String getServerFilePath(File file) {
		String absolutePath = file.getAbsolutePath().replaceAll("[\\\\]", "/");
		//把本地的发布目录地址 替换 为服务地址，形成一个完成的服务文件地址
		return absolutePath.replaceAll(this.releaseFileDir, this.releaseServerPath);
	}
	
	private String getDownFileOutPath(File file) {
		String absolutePath = file.getAbsolutePath().replaceAll("[\\\\]", "/");
		String outFilePath = absolutePath.replaceAll(this.releaseFileDir, this.outRootPath);
		// 如果输出目录不存在，则创建目录
		File outFile = new File(outFilePath);
		if(!outFile.getParentFile().exists())
			outFile.getParentFile().mkdirs();
		
		return outFilePath;
	}

	
}
