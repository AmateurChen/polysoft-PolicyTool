/**
 * 该类用于获取文件的MD5值
 * @author cx
 */
package com.polysoft.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5FileUtil {
	protected  char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	protected MessageDigest messagedigest = null;

	public MD5FileUtil() {
		try {
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取文件的MD5值
	 * 
	 * @param file
	 *            文件的绝对路径
	 * @return 返回 MD5值 字符串形式
	 * @throws IOException
	 *             文件未找到抛异常
	 */
	public String getFileMD5StringWithUpdateVersion(File file) {
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			int length = 0;
			byte[] buffer = new byte[1024 * 1024];
			while ((length = in.read(buffer)) != -1) {
				messagedigest.update(buffer, 0, length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
					in = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return bufferToHex(messagedigest.digest());
	}

	/**
	 * 获取文件的MD5值
	 * 
	 * @param file
	 *            文件的绝对路径
	 * @return 返回 MD5值 字符串形式
	 * @throws IOException
	 *             文件未找到抛异常
	 */
	public String getFileMD5String(File file){
		String resultMd5 = null;
		FileInputStream in = null;
		MappedByteBuffer byteBuffer = null;
		try {
			in = new FileInputStream(file);
			FileChannel ch = in.getChannel();
			byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,file.length());
			messagedigest.update(byteBuffer);
			resultMd5 = bufferToHex(messagedigest.digest());
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(null != byteBuffer)
				byteBuffer.clear();
			
			if(null != in)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			
			byteBuffer = null;
			in = null;
		}
		return resultMd5;
	}

	public String getMD5String(String s) {
		return getMD5String(s.getBytes());
	}

	public String getMD5String(byte[] bytes) {
		messagedigest.update(bytes);
		return bufferToHex(messagedigest.digest());
	}

	private String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

	public boolean checkPassword(String password, String md5PwdStr) {
		String s = getMD5String(password);
		return s.equals(md5PwdStr);
	}
	
	public void clear(){
		messagedigest = null;
		hexDigits = null;
	}

}
