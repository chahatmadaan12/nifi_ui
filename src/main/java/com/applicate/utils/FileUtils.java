package com.applicate.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.imageio.IIOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class FileUtils {
	
	private static final String CONFIG_LOCATION="NIFI_CONFIG_LOCATION";
	
	private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
	
	public static String configPath1;
	
	public static void setCofigPath(String configPath) {
		configPath1 = configPath;
	}

	public static String formatFileName(String fileName) {
		if (fileName != null && !fileName.isEmpty()) {
			try {
				int lastindex = fileName.lastIndexOf('.');
				String name = fileName.substring(0, lastindex);
				name = name.replaceAll("\\s", "_");
				name = Normalizer.normalize(name, Normalizer.Form.NFD);
				String fname = name.replaceAll("[^\\x00-\\x7F]", "_");
				fname = fname.replaceAll("\\W", "_");
				String[] farr = fileName.split("\\.");
				String ext = farr[farr.length - 1];
				fileName = fname + "." + ext;
			} catch (Exception e) {
//System.err.println("FileName formatting error = " + e.getMessage());
				logger.warn("FileName formatting error = " + e.getMessage());
			}
		}
		return fileName;
	}

	public static String getFileExtention(String fileName) {
		int dotIndex = fileName.lastIndexOf(".");
		return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
	}

	public static String getFileNameWithoutExtension(String fileName) {
		return FilenameUtils.getBaseName(fileName);
	}

	public static String getFileNameWithoutExtension(File file) {
		return FilenameUtils.getBaseName(file.toString());
	}

	public static void writeFile(File file, String content) throws IOException {
		OutputStream out = new FileOutputStream(file);
		writeFile(out, content);
		IOUtils.closeQuietly(out);
	}

	public static void writeFile(OutputStream out, String content) throws IOException {
		IOUtils.write(content, out, Charset.defaultCharset());
	}

	public static String readOrFail(File file) throws IOException {
		return org.apache.commons.io.FileUtils.readFileToString(file, StandardCharsets.UTF_8);
	}

	public static void createParentDirs(File file) throws IOException {
		if (file.getParentFile() != null) {
			file.getParentFile().mkdirs();
		}
	}

	/**
	 * Method for getting the Temporary directory form the OS.
	 * 
	 * @return
	 * @throws IIOException
	 */
	public static File getTemporaryDirectoryPath(String folderName) throws IIOException {
		File tempDir = new File(System.getProperty("java.io.tmpdir", null), folderName);
		if (!tempDir.exists() && !tempDir.mkdir())
			throw new IIOException("Failed to createDefaultFileSystem temporary directory " + tempDir);
		return tempDir;
	}

	public static String getTemporaryDirAsString(String folderName) throws IIOException {
		return getTemporaryDirectoryPath(folderName).getAbsolutePath();
	}

	public static String getAbsolutePath(String relativePath) {
		String configLocation = StringUtils.isEmpty(configPath1)?System.getenv(CONFIG_LOCATION):configPath1;
		return configLocation+relativePath;
	}

	public static String getAbsoluteConfigurationPath(String relativePath,String key, String clientName) {
		if (!relativePath.startsWith("/")) {
			relativePath = "/" + relativePath;
		}
		JSONObject clientData = new JSONObject();
		clientData.put(key, clientName);
		try {
			String absolutePath = StringUtils.replaceDynamicValues(getAbsolutePath(relativePath), clientData);
			if (absolutePath.contains("{") || absolutePath.contains("}")) {
				throw new RuntimeException("file path contains { }");
			}
			return absolutePath;
		} catch (JSONException e) {
			throw e;
		}
	}
	
	public static String getPathWithReplaceValue(String relativePath,String key, String clientName) {
		if (!relativePath.startsWith("/")) {
			relativePath = "/" + relativePath;
		}
		JSONObject clientData = new JSONObject();
		clientData.put(key, clientName);
		try {
			String absolutePath = StringUtils.replaceDynamicValues(relativePath, clientData);
			if (absolutePath.contains("{") || absolutePath.contains("}")) {
				throw new RuntimeException("file path contains { }");
			}
			return absolutePath;
		} catch (JSONException e) {
			throw e;
		}
	}

	public static boolean isXmlFile(File file) {
		if (file.toString().endsWith(".xml")) {
			return true;
		}
		return false;
	}

	public static String concat(String baseFile, String destinationFile) {
		if (!baseFile.endsWith("/") && !baseFile.endsWith("\\")) {
			baseFile = baseFile + "/";
		}
		if (destinationFile.startsWith("/") || destinationFile.startsWith("\\")) {
			destinationFile = destinationFile.substring(1);
		}
		return baseFile + destinationFile;
	}

	public static void deleteFiles(String loc, List<String> fileNames) {
		for (String fileName : fileNames) {
			File file = new File(loc + "/" + fileName);
			if (file.exists()) {
				file.delete();
			}
		}
	}

}
