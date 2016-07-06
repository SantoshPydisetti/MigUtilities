package com.pearson.alfresco.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLogger;
import com.pearson.alfresco.util.MetadataConstants;
import com.pearson.alfresco.util.PropertyLoader;
import com.pearson.alfresco.util.CsvFileWriter;


public class FileFormatFixUtil {

	
	IDfSession session;
	PropertyLoader configInstance, inputInstance;

	

	@SuppressWarnings("rawtypes")
	private void startFix() {
		// TODO Auto-generated method stub
		
		String input, path;
		List<String> formattedIds=new ArrayList<String>();
		SessionUtility sessionObject = new SessionUtility();		
		List resultList = new ArrayList();
		CsvFileWriter writerObject = new CsvFileWriter();
		try {
			configInstance = PropertyLoader
					.getExportConfigInstance(MetadataConstants.CONFIG_PROPERTIES);
			session = sessionObject.getDocbaseSession();		
			input = getInputIds();
			formattedIds = formatInputIds(input);
			resultList=getData(formattedIds);
			path = configInstance.getValue(MetadataConstants.CSV_FILE_PATH);
			//System.out.println(resultList);
			writerObject.exportToCsv(resultList, path);
		} catch (DfException e) {
			DfLogger.error(this, "DF exception in Metadata export method" + e,
					null, null);
		} catch (IOException e) {
			DfLogger.error(this, "IO exception in Metadata export method" + e,
					null, null);
		} finally {
			sessionObject.relaseSession(session);
		}
			
	}

		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private List getData(List<String> formattedIds) throws DfException {
		
		List resultList = new ArrayList();
		int objectIdsCount,pageCount;
		IDfSysObject sysObject = null;
		String filePath, objectID,contentType,objectName;
		StringBuffer stringBuffer;
		objectIdsCount=formattedIds.size();
		Map resultMap;
		for(int i=0;i<objectIdsCount;i++){
			try
			{
			System.out.println("---"+formattedIds.get(i));
			DfLogger.debug(this,formattedIds.get(i), null, null);
			stringBuffer=new StringBuffer();
			resultMap = new HashMap();
			objectID=formattedIds.get(i);
			sysObject=(IDfSysObject) session.getObject(new DfId (objectID));
			pageCount=sysObject.getPageCount();
			for(int j=0;j<pageCount;j++)
			{
				stringBuffer.append(sysObject.getPath(j));
				stringBuffer.append(',');							
			}	
		//	System.out.println("StringBuffer:"+stringBuffer);
			filePath=stringBuffer.deleteCharAt(stringBuffer.length() - 1).toString();
		//	System.out.println("FilePAth"+filePath);
			objectName=sysObject.getObjectName();
			contentType=sysObject.getContentType();
			
			resultMap.put("ObjectID", objectID);
			resultMap.put("FilePath", filePath);
			resultMap.put("FileName", objectName);
			resultMap.put("ContentType", contentType);
			resultList.add(resultMap);
			DfLogger.debug(this, "---Success", null, null);
			}
			catch (Exception e)
			{
				DfLogger.debug(this, "---Failure", null, null);
			}
		}
	 return resultList;
	}

	

		private List<String> formatInputIds(String input) {
			String array[] = input.split(",");		
			ArrayList<String> objectIdsList = new ArrayList<String>(Arrays.asList(array));
			return objectIdsList;
	}


		private String getInputIds() throws IOException {
			String inputs;
			inputInstance = PropertyLoader
					.getinputInstance(MetadataConstants.INPUT_PROPERTIES);
			inputs = inputInstance.getValue(MetadataConstants.INPUT_IDS);
			return inputs;
	}


		/**
		 * @param args
		 */
		public static void main(String[] args) {
			// TODO Auto-generated method stub
			FileFormatFixUtil object=new FileFormatFixUtil();
			object.startFix();
				
		}
}
