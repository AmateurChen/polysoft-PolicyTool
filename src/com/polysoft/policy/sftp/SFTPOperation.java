package com.polysoft.policy.sftp;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.polysoft.policy.release.ServerConfigInfo;
import com.polysoft.utils.DateUtil;
import com.polysoft.utils.TextUtil;

public class SFTPOperation implements SFTPOperatonImp {

	ChannelSftp sftp;
	
	public SFTPOperation(ServerConfigInfo config) {
		// TODO Auto-generated constructor stub
		String username = config.getUsername();
		String password = config.getPassword();
		String host = config.getHost();
		int port = config.getPort();
		
		this.sftp = this.connect(host, port, username, password);
		this.setSftpEncoding(this.sftp, "GBK");
	}
	
	
	@Override
	public void downloadFile(String serverPath, String outPath) {
		// TODO Auto-generated method stub
		try {
			this.sftp.get(serverPath, outPath);
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void downloadFile(SFTPDownImp downImp) {
		// TODO Auto-generated method stub
		downImp.downloadFiles(this);
	}


	@Override
	public void uploadFile(String serverPath, String uploadFilePath) {
		// TODO Auto-generated method stub
		try {
			this.sftp.put(uploadFilePath, serverPath);
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void uploadFile(SFTPUploadImp uploadImp) {
		// TODO Auto-generated method stub
		uploadImp.uploadFiles(this);
	}

	@Override
	public boolean changeDirectory(String dirPath) {
		// TODO Auto-generated method stub
		try {
			this.sftp.cd(dirPath);
			return true;
		} catch (SftpException e) {
			return false;
		}
	}

	@Override
	public List<SFTPFile> getDirFiles(String dirPath) {
		// TODO Auto-generated method stub
		List<SFTPFile> result = new ArrayList<SFTPFile>();
		try {
			Vector<?> vector = this.sftp.ls(dirPath);
			if(vector.size() == 1 && !changeDirectory(dirPath)) {
				SFTPFile transFile = transFile((LsEntry)vector.get(0), dirPath);
				transFile.setParentPath(TextUtil.replaceEndStr(dirPath, transFile.getFileName(), ""));
				result.add(transFile);
			} else {
				Iterator<?> iterator = vector.iterator();
				while(iterator.hasNext()) {
					Object obj = iterator.next();
					if(obj instanceof LsEntry) {
						LsEntry ls = (LsEntry) obj;
						if(!ls.getFilename().matches("[\\.]*")) {
							SFTPFile transFile = transFile(ls, dirPath);
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

	@Override
	public boolean mkdirDirectory(String dirPath) {
		// TODO Auto-generated method stub
		try {
			this.sftp.mkdir(dirPath);
			return true;
		} catch (SftpException e) {
			return false;
		}
	}

	@Override
	public boolean close() {
		// TODO Auto-generated method stub
		if(!this.sftp.isClosed()) {
			this.sftp.disconnect();
		}
		return true;
	}
	
	private ChannelSftp connect(String host, int port, String username, String password) {
		ChannelSftp result = null;
		JSch jsch = new JSch();
		try {
			Session session = jsch.getSession(username, host, port);
			if(null == session) {
				System.err.println("connected result session is null");
				return result;
			}
			session.setPassword(password);
			Properties properties = new Properties();
			properties.put("StrictHostKeyChecking", "no");
			session.setConfig(properties);
			session.connect();
			Channel channel = session.openChannel("sftp");
			channel.connect();
			result = (ChannelSftp) channel;
		} catch (JSchException e) {
			e.printStackTrace();
			System.err.println("connect exception; msg =>" + e.getMessage());
		} 
		return result;
	}
	
	private SFTPFile transFile(LsEntry ls, String parentPath) {
		SFTPFile file = new SFTPFile();
		if(ls.getLongname().startsWith("d")) {// 目录
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

	private void setSftpEncoding(ChannelSftp sftp, String encoding) {
		try {
			Field f = sftp.getClass().getDeclaredField("server_version");
			f.setAccessible(true);  
			f.set(sftp, 2); 
			this.sftp.setFilenameEncoding("encoding");
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SftpException e) {
			e.printStackTrace();
		} 
	}
	

	

}
