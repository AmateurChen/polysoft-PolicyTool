package com.polysoft.policy.sftp;

import java.io.File;

import com.polysoft.policy.release.ReleaseInfo;

public class SFTPUpload implements SFTPUploadImp {

	private String upServerPathDir;
	private String upFilePath;
	
	public SFTPUpload(String upServerPathDir, String upFilePath) {
		// TODO Auto-generated constructor stub
		this.upFilePath = upFilePath;
		this.upServerPathDir = upServerPathDir;
	}
	
	public SFTPUpload(ReleaseInfo info) {
		this.upFilePath = info.getReleaseFileDir();
		this.upServerPathDir = info.getReleaseServerDir();
	}
	
	@Override
	public void uploadFiles(SFTPOperatonImp sftpImp) {
		// TODO Auto-generated method stub
		File[] listFiles = new File(this.upFilePath).listFiles();
		for (int i = 0; i < listFiles.length; i++) {
			this.upload(sftpImp, this.upServerPathDir, listFiles[i]);
		}
	}
	
	private void upload(SFTPOperatonImp sftpImp, String upServerPathDir, File file) {
		if(file.isFile()) {
			sftpImp.uploadFile(upServerPathDir, file.getAbsolutePath());
		} else {
			upServerPathDir += "/" + file.getName();
			boolean isExists = sftpImp.changeDirectory(upServerPathDir);
			if(!isExists) {
				if(!sftpImp.mkdirDirectory(upServerPathDir)) {
					//SFTP �����ļ���ʧ��
					return ;
				}
			}
			
			File[] listFiles = file.listFiles();
			for (int i = 0; i < listFiles.length; i++) {
				this.upload(sftpImp, upServerPathDir, listFiles[i]);
			}
		}
	}
	
}
