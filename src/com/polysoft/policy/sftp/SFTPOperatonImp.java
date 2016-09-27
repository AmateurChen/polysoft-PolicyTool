package com.polysoft.policy.sftp;

import java.util.List;

import com.jcraft.jsch.SftpException;


public interface SFTPOperatonImp {

	public boolean isExists(String serverPath);
	
	public void downloadFile(String serverPath, String outPath) throws SftpException;
	
	public void downloadFile(SFTPDownImp downImp) throws SftpException;
	
	public void uploadFile(String serverPath, String uploadFilePath) throws SftpException;
	
	public void uploadFile(SFTPUploadImp uploadImp) throws SftpException;
	
	public boolean changeDirectory(String dirPath);
	
	public List<SFTPFile> getDirFiles(String dirPath) throws SftpException;
	
	public boolean mkdirDirectory(String dirPath);
	
	public boolean close();
	
}
