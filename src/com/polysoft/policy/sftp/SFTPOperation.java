package com.polysoft.policy.sftp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.polysoft.utils.DateUtil;
import com.polysoft.utils.TextUtil;

public class SFTPOperation {

	public void uploadLocallyFile(ChannelSftp sftp, String upServerPath, String upFilePath) throws SftpException {
		sftp.put(upFilePath, upServerPath);
	}
	
	public void downServerFile(ChannelSftp sftp, String serverFilePath, String outPath) throws SftpException {
		try {
			sftp.get(serverFilePath, outPath);
		} catch (SftpException e) {
			throw e;
		}
	}
	
	public List<SFTPFile> getDirectoryFiles(ChannelSftp sftp, String path) {
		List<SFTPFile> result = new ArrayList<SFTPFile>();
		
		try {
			Vector<?> vector = sftp.ls(path);
			// 此时为文件路径
			if(vector.size() == 1 && !cdDirectory(sftp, path)) {
				SFTPFile transFile = transFile((LsEntry)vector.get(0), path);
				transFile.setParentPath(TextUtil.replaceEndStr(path, transFile.getFileName(), ""));
				result.add(transFile);
			} else {// 是一个文件夹路径，获取文件夹下文件
				Iterator<?> iterator = vector.iterator();
				while(iterator.hasNext()) {
					Object obj = iterator.next();
					if(obj instanceof LsEntry) {
						LsEntry ls = (LsEntry) obj;
						//出现以“..”命名的文件
						if(!ls.getFilename().matches("[\\.]*")) {
							SFTPFile transFile = transFile(ls, path);
							result.add(transFile);
						}
					}
				}
			}
		} catch (SftpException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean cdDirectory(ChannelSftp sftp, String path) {
		try {
			sftp.cd(path);
			return true;
		} catch (SftpException e) {
		}
		
		return false;
	}
	
	public boolean mkdirDirectory(ChannelSftp sftp, String path) {
		try {
			sftp.mkdir(path);
			return true;
		} catch (SftpException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private SFTPFile transFile(LsEntry ls, String parentPath) {
		SFTPFile file = new SFTPFile();
		if(ls.getLongname().startsWith("d")) {//目录
			file.setDirectory(true);
		} else {
			file.setDirectory(false);
		}
		
		file.setParentPath(parentPath);
		file.setFileName(ls.getFilename());
		
		SftpATTRS attrs = ls.getAttrs();
		file.setFileSize(attrs.getSize());
		
		int mTime = attrs.getMTime();
		String modifytime = DateUtil.transForm(mTime, DateUtil.PATTERN_1);
		file.setModifyTime(modifytime);
		
		return file;
	}
}
