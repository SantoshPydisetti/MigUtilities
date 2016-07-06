package com.pearson.alfresco.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.documentum.fc.common.DfLogger;

public class PropertyLoader {
	private static PropertyLoader configInstance = null;
	private static PropertyLoader idsInstance = null;
	private static PropertyLoader inputInstance = null;

	Properties props = new Properties();

	private PropertyLoader(String fileName) throws IOException {

		InputStream file = this.getClass().getClassLoader()
				.getResourceAsStream(fileName);
		if (file == null) {
			DfLogger.info(this, fileName + " file not found", null, null);			
		} else {
			props.load(file);
			DfLogger.info(this, fileName + " file loaded", null, null);				
		}
	}

	public static synchronized PropertyLoader getExportConfigInstance(
			String fileName) throws IOException {
		if (configInstance == null)
			configInstance = new PropertyLoader(fileName);
		return configInstance;
	}

	public static synchronized PropertyLoader getIdsInstance(String fileName)
			throws IOException {
		if (idsInstance == null)
			idsInstance = new PropertyLoader(fileName);
		return idsInstance;
	}

	public static synchronized PropertyLoader getinputInstance(String fileName)
			throws IOException {
		if (inputInstance == null)
			inputInstance = new PropertyLoader(fileName);
		return inputInstance;
	}

	public String getValue(String propKey) {
		return this.props.getProperty(propKey);
	}

}
