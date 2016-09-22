package com.polysoft.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
	
	public static boolean isFile(String filePath) {
		return isFile(getFile(filePath));
	}
	
	public static boolean isFile(File file) {
		return file.isFile();
	}
	
	public static boolean isDirectory(File file) {
		return !file.isFile();
	}
	
	public static boolean isDirectory(String fileDirectory) {
		return isDirectory(getFile(fileDirectory));
	}
	
	public static boolean createDirectory(File file) {
		if(!isDirectory(file))
			return false;
		
		if(!isExists(file))
			return file.mkdirs();
		
		return true;
	}
	
	public static boolean createDirectory(String fileDirectory) {
		return createDirectory(getFile(fileDirectory));
	}
	
	public static File getFile(String filePath) {
		return new File(filePath);
	}
	
	public static boolean isExists(String filePath) {
		File file = getFile(filePath);
		return  file.exists();
//		return isExists(getFile(filePath));
	}
	
	public static boolean isExists(File file) {
		return file.exists();
	}
	
	public static boolean reNameFile(File file, File reNameFile) {
		if(!isExists(file))
			return false;
		
		return file.renameTo(reNameFile);
	}
	
	public static boolean reNameFile(String filePath, File reNameFile) {
		return reNameFile(getFile(filePath), reNameFile);
	}
	
	public static boolean reNameFile(File file, String reNamefilePath) {
		return reNameFile(file, getFile(reNamefilePath));
	}
	
	public static boolean reNameFile(String filePath, String reNamefilePath) {
		return reNameFile(getFile(filePath), getFile(reNamefilePath));
	}
	
	public static boolean createFile(File file, boolean isDelete) throws IOException {
		
		if(!isExists(file.getParentFile())) {
			file.getParentFile().mkdirs();
		}
		
		if(!isExists(file)) {
			return file.createNewFile();
		} else {
			if(isDelete) {
				file.delete();
				return file.createNewFile();
			}
			return true;
		}
	}
	
	public static File createFile(String filePath, boolean isDelete) throws IOException {
		File file = getFile(filePath);
		boolean isSuccess = createFile(file, isDelete);
		return isSuccess ? file : null;
	}
	
	public static File[] getFiles(File file) {
		if(!isExists(file) || isFile(file)) {
			return new File[0];
		}
		
		return file.listFiles();
	}
	
	public static File[] getFiles(String filePath) {
		return getFiles(getFile(filePath));
	}
	
	public static boolean deleteFile(File file) {
		if(isExists(file))
			return file.delete();
		else
			return false;
	}
	
	public static boolean deleteFile(String filePath) {
		return deleteFile(getFile(filePath));
	}

	public static boolean copyFile(File file, File copyFile) {
		if(!isExists(file))
			return false;
		
		if(isExists(copyFile))
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
		if(!isExists(file))
			return false;
		
		if(!createDirectory(fileDirectory)) 
			return false;
		
		File moveFile = new File(fileDirectory.getAbsolutePath() + File.separator + file.getName());
		if(isExists(moveFile))
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
	
	public static void main(String[] args) {
		String filePath = "C:\\Users\\ThinkPad\\Desktop\\���²�Ʒ�����?.xlsx";
		String fileDirectory = "E:\\workSpaces\\Magnum Mobile\\�����ĵ�";
		boolean isMove = moveFile(getFile(filePath), getFile(fileDirectory));
		System.out.println("�ļ��ƶ�===> "+ isMove);
	}
	
}
