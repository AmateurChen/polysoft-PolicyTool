package com.polysoft.policy.sftp;

import com.polysoft.policy.release.ReleaseInfo;

public interface SFTPOperatonImp {

	
	public void downloadFile(ReleaseInfo info);
	
	public void downloadFile(String serverPathDir, String releaseFileDir, String outPath);
	
	public void downloadFile(String serverPathDir, String outPath);
	
	public void uploadFile(ReleaseInfo info);
	
	public void uploadFile(String ser);
}
