package com.polysoft.policy.sftp;

import java.io.File;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

public class SFTPUpload extends SFTPOperation {

	private String upServerPathDir;
	private String upFilePath;
	
	public SFTPUpload(String upServerPathDir, String upFilePath) {
		// TODO Auto-generated constructor stub
		this.upFilePath = upFilePath;
		this.upServerPathDir = upServerPathDir;
	}
	
	
	public void upload(ChannelSftp sftp) {
		
		File[] listFiles = new File(this.upFilePath).listFiles();
		for (int i = 0; i < listFiles.length; i++) {
			this.upload(sftp, this.upServerPathDir, listFiles[i]);
		}
		
	}
	
	
	public void upload(ChannelSftp sftp, String upServerPathDir, File file) {
		if(file.isFile()) {
			this.upload(sftp, upServerPathDir, file.getAbsolutePath());
		} else {
			upServerPathDir += "/" + file.getName();
			boolean isExists = super.cdDirectory(sftp, upServerPathDir);
			if(!isExists) {
				if(!super.mkdirDirectory(sftp, upServerPathDir)) {
					//SFTP 创建文件夹失败
					return ;
				}
			}
			
			File[] listFiles = file.listFiles();
			for (int i = 0; i < listFiles.length; i++) {
				this.upload(sftp, upServerPathDir, listFiles[i]);
			}
		}
	}
	
	
	public void upload(ChannelSftp sftp, String upServerPathDir, String filePath) {
		try {
			super.uploadLocallyFile(sftp, upServerPathDir+"/", filePath.replaceAll("[\\\\]", "/"));
			System.out.println("上传成功=> "+ filePath);
		} catch (SftpException e) {
			System.err.println("上传失败=> "+ filePath);
		}
	}
}
