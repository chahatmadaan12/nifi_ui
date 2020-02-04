package com.applicate.nifiui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ConnectionVerifierFactoryService {
	
	public enum ConnectionType{
		SFTP,MYSQL,MSSQL,CLICKHOUSE,POSTGRES,MONGO
	}
	
	@Autowired
	private ApplicationContext appContext;

	public ConnectionVerificationService getConnectionVerifier(String type){
	   type=type.toUpperCase();
	   switch (ConnectionType.valueOf(type)) {
	   case SFTP:
		  return appContext.getBean(SftpConnectionVerifier.class);
	   case MYSQL:
		  return appContext.getBean(SQLConnectionVerifier.class);
	   case MSSQL:
	      return appContext.getBean(SQLConnectionVerifier.class);
	   case POSTGRES:
		  return appContext.getBean(SQLConnectionVerifier.class);
	   case CLICKHOUSE:
		  return appContext.getBean(SQLConnectionVerifier.class);
	   case MONGO:
		  return appContext.getBean(MongoConnectionVerifier.class);
   	   default:
			break;
		}
	return null;
	}
}
