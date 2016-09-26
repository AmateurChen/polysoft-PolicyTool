package com.polysoft.policy.sftp;

import java.io.File;
import java.util.List;

public class SFTPDownServerFile implements SFTPDownImp{

	private String serverDownPath;
	private String outRootPath;
	
	public SFTPDownServerFile(String serverDownPath, String outRootPath) {
		// TODO Auto-generated constructor stub
		this.serverDownPath = serverDownPath;
		this.outRootPath = outRootPath;
	}
	
	@Override
	public void downloadFiles(SFTPOperatonImp sftpImp) {
		// TODO Auto-generated method stub
		List<SFTPFile> files = sftpImp.getDirFiles(this.serverDownPath);
		for (SFTPFile file : files) {
			this.downFile(sftpImp, file, this.outRootPath);
		}
	}
	
	public void downFile(SFTPOperatonImp sftpImp, SFTPFile file, String outPath) {
		if(file.isDirectory()) {
			File outDirFile = new File(outPath +"/"+ file.getFileName());
			if(!outDirFile.exists()) outDirFile.mkdirs();
			List<SFTPFile> files = sftpImp.getDirFiles(file.getFilePath());
			for (SFTPFile sftpFile : files) {
				this.downFile(sftpImp, sftpFile, outDirFile.getAbsolutePath());
			}
		} else {
			File localyFile = new File(outPath);
			if(localyFile.exists()) localyFile.delete();
			sftpImp.downloadFile(file.getFilePath(), outPath);
		}
	}
	
}
