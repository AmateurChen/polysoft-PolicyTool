package com.polysoft.policy.sftp;

import java.util.List;


public interface SFTPOperatonImp {

	public void downloadFile(String serverPath, String outPath);
	
	public void downloadFile(SFTPDownImp downImp);
	
	public void uploadFile(String serverPath, String uploadFilePath);
	
	public void uploadFile(SFTPUploadImp uploadImp);
	
	public boolean changeDirectory(String dirPath);
	
	public List<SFTPFile> getDirFiles(String dirPath);
	
	public boolean mkdirDirectory(String dirPath);
	
	public boolean close();
	
}
