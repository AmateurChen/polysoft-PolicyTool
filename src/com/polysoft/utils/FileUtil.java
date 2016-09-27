package com.polysoft.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
	
	public static boolean isFile(String filePath) {
		return new File(filePath).isFile();
	}
	
	
	public static boolean isDirectory(String filePath) {
		return !isFile(filePath);
	}
	
	public static File getDirectoryFile(File file) {
		if(file.isFile()) {
			return file.getParentFile();
		}
		return file;
	}
	
	public static File getDirectoryFile(String filePath) {
		return getDirectoryFile(new File(filePath));
	}
	
	
	public static boolean createDirectory(String filePath) {
		File file = new File(filePath);
		if(file.isFile()) {
			return file.getParentFile().mkdirs();
		} else {
			return file.mkdirs();
		}
	}
	
	public static boolean isExists(String filePath) {
		return  new File(filePath).exists();
	}
	
	public static boolean reNameFile(File file, File reNameFile) {
		return file.renameTo(reNameFile);
	}
	
	public static boolean reNameFile(String filePath, File reNameFile) {
		return reNameFile(new File(filePath), reNameFile);
	}
	
	public static boolean reNameFile(File file, String reNamefilePath) {
		return reNameFile(file, new File(reNamefilePath));
	}
	
	public static boolean reNameFile(String filePath, String reNamefilePath) {
		return reNameFile(new File(filePath), reNamefilePath);
	}
	
	public static void createFile(File file, boolean isDelete) throws IOException {
		
		createDirectory(file.getAbsolutePath());
		if(isDelete) {
			file.delete();
			file.createNewFile();
		} else if(!file.exists()){
			file.createNewFile();
		}
	}
	
	public static File createFile(String filePath, boolean isDelete) throws IOException {
		File file = new File(filePath);
		createFile(file, isDelete);
		return file;
	}
	
	
	public static File[] getFiles(String filePath) {
		return new File(filePath).listFiles();
	}
	
	public static void deleteFile(File file) {
		if(file.isFile()) {
			file.delete();
		} else {
			File[] listFiles = file.listFiles();
			for (int i = 0; i < listFiles.length; i++) {
				deleteFile(listFiles[i]);
			}
			file.delete();
		}
	}
	
	public static void deleteFileChild(File file) {
		if(file.isFile()) {
			file.delete();
		} else {
			File[] listFiles = file.listFiles();
			for (int j = 0; j < listFiles.length; j++) {
				deleteFile(listFiles[j]);
			}
		}
	}
	
	public static void deleteFileChild(String filePath) {
		deleteFileChild(new File(filePath));
	}
	
	public static void deleteFile(String filePath) {
		deleteFile(new File(filePath));
	}

	public static boolean copyFile(File file, File copyFile) {
		if(!file.exists())
			return false;
		
		if(copyFile.exists())
			copyFile.delete();
		
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(file);
			fos = new FileOutputStream(copyFile);
			byte[] buffer = new byte[1024 * 1024];
			int length = 0;
			while((length = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, length);
			} 
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if(null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if(null != fos) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	public static boolean moveFile(File file, File fileDirectory) {
		if(!file.exists())
			return false;
		else if(!file.isFile()) {
			File[] listFiles = file.listFiles();
			File outPutFileDir = getDirectoryFile(fileDirectory);
			for (int i = 0; i < listFiles.length; i++) {
				moveFile(listFiles[i], outPutFileDir);
			}
			return true;
		}
		if(fileDirectory.isFile()) {
			fileDirectory = fileDirectory.getParentFile();
		}
		if(!fileDirectory.exists()) {
			fileDirectory.mkdirs();
		} 
		
		File moveFile = new File(fileDirectory.getAbsolutePath() + File.separator + file.getName());
		if(moveFile.exists())
			deleteFile(moveFile);
		
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(file);
			fos = new FileOutputStream(moveFile);
			//当文件大�?2G时，超出int �?大�??
//			int available = fis.available();
			byte[] buffer = new byte[1024 * 1024];
			int length = 0;
			while((length = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, length);
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			
			if(fos != null)
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			
		}
		
		return false;
	}
	
}
