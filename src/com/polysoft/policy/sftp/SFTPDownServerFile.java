package com.polysoft.policy.sftp;

import java.io.File;
import java.util.List;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

public class SFTPDownServerFile extends SFTPOperation {

	private String serverDownPath;
	private String outRootPath;
	
	public SFTPDownServerFile(String serverDownPath, String outRootPath) {
		// TODO Auto-generated constructor stub
		this.serverDownPath = serverDownPath;
		this.outRootPath = outRootPath;
	}
	
	public void downFile(ChannelSftp sftp) {
		List<SFTPFile> files = super.getDirectoryFiles(sftp, this.serverDownPath);
		for (SFTPFile file : files) {
			this.downFile(sftp, file, this.outRootPath);
		}
	}
	
	public void downFile(ChannelSftp sftp, SFTPFile file, String outPath) {
		if(file.isDirectory()) {
			File outDirFile = new File(outPath +"/"+ file.getFileName());
			if(!outDirFile.exists()) outDirFile.mkdirs();
			
			List<SFTPFile> files = super.getDirectoryFiles(sftp, file.getFileName());
			for (SFTPFile sftpFile : files) {
				this.downFile(sftp, sftpFile, outDirFile.getAbsolutePath());
			}
		} else {
			this.downFile(sftp, file.getFileName(), outPath);
		}
	}
	
	private void downFile(ChannelSftp sftp, String filePath, String outPath) {
		try {
			File file = new File(outPath);
			if(file.exists()) file.delete();
			
			super.downServerFile(sftp, filePath, outPath);
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}
}
