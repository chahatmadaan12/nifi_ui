package com.applicate.nifiui.service;

import com.applicate.nifiui.dbmanager.dao.beans.Connection;

public interface ConnectionVerificationService {
	
	boolean verify(Connection connection) throws Exception;

}
