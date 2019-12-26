package com.applicate.nifiui.service;

import org.springframework.stereotype.Component;

import com.applicate.nifiui.dbmanager.dao.beans.Connection;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
@Component
public class SftpConnectionVerifier implements ConnectionVerificationService{

	@Override
	public boolean verify(Connection connection) throws NumberFormatException, JSchException {
		JSch jSch = new JSch();
	    Session sftpSession = jSch.getSession(connection.getParam1(), connection.getParam2(), Integer.parseInt(connection.getParam3()));
	    sftpSession.setConfig("StrictHostKeyChecking","no");
	    sftpSession.setPassword(connection.getParam4());
	    sftpSession.connect();
	    //sftpSession.openChannel(connection.getType());
		return sftpSession.isConnected();
	}

}
