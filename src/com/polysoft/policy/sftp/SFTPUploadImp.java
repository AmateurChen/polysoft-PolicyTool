package com.polysoft.policy.sftp;

import com.jcraft.jsch.SftpException;

public interface SFTPUploadImp {

	public void uploadFiles(SFTPOperatonImp sftpImp) throws SftpException;
}
