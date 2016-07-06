package com.pearson.alfresco.util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.csvreader.CsvWriter;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLogger;

public class CsvFileWriter {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void exportToCsv(List resultList, String path) throws IOException,
			DfException {

		FileOutputStream output = null;
		output = new FileOutputStream(path);

		CsvWriter csvWriter = new CsvWriter(new BufferedWriter(
				new OutputStreamWriter(output, "UTF-8")), ',');

		int resultSize = resultList.size();
		DfLogger.info(this, "Result Size:" + resultSize, null, null);
	
		if (resultSize == 0) {
			DfLogger.info(this, "No results fetched to write to file", null,
					null);			
		} else {
			Map resultHeader = (Map) resultList.get(0);
			Set<String> keyHeader = resultHeader.keySet();
			for (String key : keyHeader) {
				csvWriter.write(key);
			}
			csvWriter.endRecord();

			for (int i = 0; i < resultSize; i++) {
				Map result = (Map) resultList.get(i);
				Set<String> keys = result.keySet();
				for (String key : keys) {				
					String value = result.get(key).toString();				
					csvWriter.write(value);
				}
				csvWriter.endRecord();
			}

			csvWriter.flush();
			csvWriter.close();
			DfLogger.info(this, "CSV file exported", null, null);
		}
	}
}
