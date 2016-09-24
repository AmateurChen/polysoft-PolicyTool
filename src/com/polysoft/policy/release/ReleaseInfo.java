package com.polysoft.policy.release;

public class ReleaseInfo {

	// ��������
	String environment;
	// �����汾
	String version;
	// ��������
	String content;
	// ���ط����ļ�Ŀ¼
	String releaseFileDir;
	// �ļ�����Ŀ¼�����ݷ������Ͻ����滻���ļ�
	String backupsFileDir;
	// �����ļ���Ӧ�������ϵ�Ŀ¼
	String releaseServerDir;
	
	public static ReleaseInfo getReleaseInfo() {
		String environment = "17�������Ի���";
		String version = "2.001.20160922001";
		String content = "�޸���������";
		String releaseFileDir = "E:/cxtest/releaseDir";
		String releaseServerDir = "/cxtest/server/transferServer1/deploy/ROOT.war/file/update/all2.0";
		String backupsFileDir = "E:/cxtest/backups";
		
		ReleaseInfo info = new ReleaseInfo();
		info.setEnvironment(environment);
		info.setVersion(version);
		info.setContent(content);
		info.setReleaseFileDir(releaseFileDir);
		info.setReleaseServerDir(releaseServerDir);
		info.setBackupsFileDir(backupsFileDir);
		
		return info;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReleaseFileDir() {
		return releaseFileDir;
	}

	public void setReleaseFileDir(String releaseFileDir) {
		this.releaseFileDir = releaseFileDir;
	}

	public String getBackupsFileDir() {
		return backupsFileDir;
	}

	public void setBackupsFileDir(String backupsFileDir) {
		this.backupsFileDir = backupsFileDir;
	}

	public String getReleaseServerDir() {
		return releaseServerDir;
	}

	public void setReleaseServerDir(String releaseServerDir) {
		this.releaseServerDir = releaseServerDir;
	}
	
}
