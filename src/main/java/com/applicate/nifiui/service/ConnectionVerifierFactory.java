package com.applicate.nifiui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.applicate.ApplicationContextProvider;

@Component
public class ConnectionVerifierFactory {
	
	public enum ConnectionType{
		SFTP,MYSQL,MSSQL,CLICKHOUSE,POSTGRES
	}
	
	@Autowired
	private ApplicationContextProvider contextProvider;

	public ConnectionVerificationService getConnectionVerifier(String type){
	   type=type.toUpperCase();
	   switch (ConnectionType.valueOf(type)) {
	   case SFTP:
		  return contextProvider.getContext().getBean(SftpConnectionVerifier.class);
	   case MYSQL:
		  return contextProvider.getContext().getBean(SQLConnectionVerifier.class);
	   case MSSQL:
	      return contextProvider.getContext().getBean(SQLConnectionVerifier.class);
	   case POSTGRES:
		  return contextProvider.getContext().getBean(SQLConnectionVerifier.class);
	   case CLICKHOUSE:
		  return contextProvider.getContext().getBean(SQLConnectionVerifier.class);
   	   default:
			break;
		}
	return null;
	}
}
