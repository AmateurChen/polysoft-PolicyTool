package com.polysoft.utils;

import java.io.File;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class ZipUtil {

	public static void compressFile(String compressPath, String password) {
		compressFiles(FileUtil.getFile(compressPath), password);
	}
	
	public static void compressFiles(File compressFile, String password) {
		compressFiles(compressFile, compressFile, compressFile.getName(), password);
	}
	
	public static void compressFiles(String compressPath, String outDirPath, String password) {
		compressFiles(compressPath, outDirPath, null, password);
	}
	
	public static void compressFiles(String compressPath, String outDirPath, String fileName, String password) {
		compressFiles(FileUtil.getFile(compressPath), FileUtil.getFile(outDirPath), fileName,  password);
	}
	
	public static void compressFiles(File compressFile, File outDirectory, String fileName, String password) {
		if(!FileUtil.isExists(compressFile)) {
			System.err.println("需要压缩的文件未找到===>" + compressFile.getAbsolutePath());
			return ;
		} else if(!FileUtil.isDirectory(outDirectory)) {
			System.err.println("压缩输出地址必须为目录地址===>" + outDirectory.getAbsolutePath());
			return ;
		} else if(!FileUtil.isExists(outDirectory)) {
			if(!outDirectory.mkdirs()){
				System.err.println("压缩输出地址创建错误===>" + outDirectory.getAbsolutePath());
			}
			return ;
		} else if(TextUtil.isEmpty(fileName)) {
			fileName = compressFile.getName();
		}
		
		String outPath = outDirectory.getAbsolutePath() + "/" + appendFileSuffix(fileName);
		
		try {
			ZipFile zipFile = new ZipFile(outPath);
			compress(zipFile, getZipParameters(password), compressFile);
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}

	public static ZipParameters getZipParameters(String password) {
		ZipParameters parameters = new ZipParameters();
		
		//Deflate compression or store(no compression) can be set below
		parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		
		// Set the compression level. This value has to be in between 0 to 9
		// Several predefined compression levels are available
		// DEFLATE_LEVEL_FASTEST - Lowest compression level but higher speed of compression
		// DEFLATE_LEVEL_FAST - Low compression level but higher speed of compression
		// DEFLATE_LEVEL_NORMAL - Optimal balance between compression level/speed
		// DEFLATE_LEVEL_MAXIMUM - High compression level with a compromise of speed
		// DEFLATE_LEVEL_ULTRA - Highest compression level but low speed
		parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
		
		//This flag defines if the files have to be encrypted.
		//If this flag is set to false, setEncryptionMethod, as described below,
		//will be ignored and the files won't be encrypted
		if(TextUtil.isEmpty(password)){
			parameters.setEncryptFiles(false);
		} else {
			//Zip4j supports AES or Standard Zip Encryption (also called ZipCrypto)
			//If you choose to use Standard Zip Encryption, please have a look at example
			//as some additional steps need to be done while using ZipOutputStreams with
			//Standard Zip Encryption
			parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
			
			//If AES encryption is used, this defines the key strength
			parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
			
			//self descriptive
			parameters.setPassword(password);
			parameters.setEncryptFiles(true);
		}
		
		return parameters;
	}
	
	private static void compress(ZipFile zipFile, ZipParameters parameters, File file) throws ZipException {
		if(file.isFile()) {
			zipFile.addFile(file, parameters);
		} else if(!file.isFile()) {
			zipFile.addFolder(file, parameters);;
		}
	}

	private static String appendFileSuffix(String fileName) {
		if(fileName.matches(".*\\.zip"))
			return fileName;
		else 
			return fileName + ".zip";
	}
	
}
