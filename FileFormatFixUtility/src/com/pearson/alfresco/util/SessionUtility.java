package com.pearson.alfresco.util;

import java.io.IOException;

import com.documentum.fc.client.DfClient;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.DfLoginInfo;
import com.documentum.fc.common.IDfLoginInfo;

public class SessionUtility 
{
	IDfSessionManager sessionManager = null;
	PropertyLoader idsInstance;
	
	public IDfSession getDocbaseSession() throws DfException, IOException {
		String docbaseName,userName,password;
		
		IDfSession session = null;
		idsInstance= PropertyLoader.getIdsInstance("session.properties");										
		docbaseName = idsInstance.getValue(MetadataConstants.DOCBASE_NAME);
		userName = idsInstance.getValue(MetadataConstants.USER_NAME);
		password = idsInstance.getValue(MetadataConstants.PASSWORD);
		IDfClient client = DfClient.getLocalClient();

		System.out.println("client "+client);
		sessionManager = client.newSessionManager();
	System.out.println("session manager "+sessionManager);
	IDfLoginInfo loginInfo = new DfLoginInfo();
	loginInfo.setUser(userName);
	loginInfo.setPassword(password);
	sessionManager.setIdentity(docbaseName, loginInfo);
	session = sessionManager.getSession(docbaseName);	
	DfLogger.info(this, "Session got", null,null);	
	return session;
	}

	public void relaseSession(IDfSession session) {
		sessionManager.release(session);
		DfLogger.info(this, "Session released", null, null);
	}

}
