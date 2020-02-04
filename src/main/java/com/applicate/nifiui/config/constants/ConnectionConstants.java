package com.applicate.nifiui.config.constants;

import java.util.Arrays;
import java.util.List;

public interface ConnectionConstants {
	
	List<String> SQL_TYPE= Arrays.asList("MYSQL","CLICKHOUSE","MSSQL");
    String USER_NAME = "userName";
    String DB_NAME = "dbName";
	String PASSWORD = "password";
	String HOST = "host";
	String PORT = "port";
	String TYPE = "type";
	String DRIVER_CLASS = "driverClassName";
	String DB_URL = "dbUrl";
	String DATABASE = "database";
	String DRIVER_JAR_KEY = "driverJar";
	String DRIVER_LOCATION = "driverLoaction";
	String FILE_PATH = "filePath";
	
}
