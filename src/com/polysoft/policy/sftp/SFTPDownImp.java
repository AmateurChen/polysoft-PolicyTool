package com.polysoft.policy.sftp;

import com.jcraft.jsch.SftpException;

public interface SFTPDownImp {

	public void downloadFiles(SFTPOperatonImp sftpImp) throws SftpException;
}
