package com.applicate.nifiui.service;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import static com.applicate.nifiui.config.constants.ConnectionConstants.*;
@Component
public class SftpConnectionVerifier implements ConnectionVerificationService{

	@Override
	public JSONObject verify(JSONObject connection) throws NumberFormatException, JSchException {
		JSch jSch = new JSch();
	    Session sftpSession = jSch.getSession(connection.getString(USER_NAME), connection.getString(HOST), Integer.parseInt(connection.getString(PORT)));
	    sftpSession.setConfig("StrictHostKeyChecking","no");
	    sftpSession.setPassword(connection.getString(PASSWORD));
	    sftpSession.connect();
	    //sftpSession.openChannel(connection.getType());
		return new JSONObject().put("verify",sftpSession.isConnected());
	}

}
