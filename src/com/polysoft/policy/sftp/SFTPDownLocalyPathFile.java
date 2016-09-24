package com.polysoft.policy.sftp;

import java.io.File;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.polysoft.policy.release.ReleaseInfo;

public class SFTPDownLocalyPathFile extends SFTPOperation{

	private String outRootPath;
	private String releaseServerPath;
	private String releaseFileDir;
	
	public SFTPDownLocalyPathFile(ReleaseInfo info) {
		// TODO Auto-generated constructor stub
		this.outRootPath = info.getBackupsFileDir() + "/" + info.getVersion();
		this.releaseServerPath = info.getReleaseServerDir();
		this.releaseFileDir = info.getReleaseFileDir();
	}
	
	public SFTPDownLocalyPathFile(String serverRootPath, String releaseRootPath, String outRootPath) {
		// TODO Auto-generated constructor stub
		this.outRootPath = outRootPath;
		this.releaseServerPath = serverRootPath;
		this.releaseFileDir = releaseRootPath;
	}

	
	public void downFile(ChannelSftp sftp) {
		this.downFile(sftp, new File(this.releaseFileDir));
	}
	
	public void downFile(ChannelSftp sftp, File file) {
		if(file.isFile()) {
			String serverFilePath = getServerFilePath(file);
			String downFileOutPath = getDownFileOutPath(file);
			this.downFile(sftp, serverFilePath, downFileOutPath);
		} else {
			File[] listFiles = file.listFiles();
			for (int i = 0; i < listFiles.length; i++) {
				this.downFile(sftp, listFiles[i]);
			}
		}
	}
	
	private void downFile(ChannelSftp sftp, String filePath, String outPath) {
		try {
			File file = new File(outPath);
			if(file.exists()) file.delete();
			
			super.downServerFile(sftp, filePath, file.getParent());
			System.out.println("文件下载成功=> " + filePath);
		} catch (SftpException e) {
			System.err.println("文件下载失败==> " +filePath);
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
