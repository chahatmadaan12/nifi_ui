package com.applicate.nifiui.service;

import static com.applicate.nifiui.config.constants.ConnectionConstants.FILE_PATH;
import static com.applicate.nifiui.config.constants.ConnectionConstants.HOST;
import static com.applicate.nifiui.config.constants.ConnectionConstants.PASSWORD;
import static com.applicate.nifiui.config.constants.ConnectionConstants.PORT;
import static com.applicate.nifiui.config.constants.ConnectionConstants.TYPE;
import static com.applicate.nifiui.config.constants.ConnectionConstants.USER_NAME;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
@Component
public class SftpConnectionVerifier implements ConnectionVerificationService{

	@Override
	public JSONObject verify(JSONObject connection) throws Exception {
		JSch jSch = new JSch();
		boolean verification = false;
	    Session session = jSch.getSession(connection.getString(USER_NAME), connection.getString(HOST), Integer.parseInt(connection.getString(PORT)));
	    session.setConfig("StrictHostKeyChecking","no");
	    session.setPassword(connection.getString(PASSWORD));
	    session.connect();
	    if(connection.getString(TYPE).equalsIgnoreCase("sftp")) {
		    verification = sftpVerification(session.openChannel(connection.getString(TYPE)),connection.getString(FILE_PATH));
	    }else {
	    	ftpVerification(session.openChannel(connection.getString(TYPE)));
	    }
		return new JSONObject().put("verify",verification);
	}

	private RuntimeException ftpVerification(Channel openChannel) {
		return new RuntimeException("Not implement yet");
	}

	private boolean sftpVerification(Channel openChannel, String filePath) throws Exception {
		ChannelSftp sftp = (ChannelSftp)openChannel;
		sftp.connect();
		SftpATTRS lstat = sftp.lstat(filePath);
		return true;
	}

}
