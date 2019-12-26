package com.applicate.nifiui.service;

import java.sql.DriverManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.applicate.nifiui.dbmanager.dao.beans.Connection;
import com.applicate.utils.StringUtils;
import com.jcraft.jsch.JSchException;

@Component
public class SQLConnectionVerifier implements ConnectionVerificationService {

	static {
		try {
			for (SqlType sql : SqlType.values()) {
				Class.forName(sql.driverClass);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public enum SqlType {

		POSTGRES("jdbc:postgresql://{{host}}:{{port}}/{{database}}", "org.postgresql.Driver"),
		MYSQL("jdbc:mysql://{{host}}:{{port}}/{{database}}", "com.mysql.cj.jdbc.Driver"),
		CLICKHOUSE("jdbc:clickhouse://{{host}}:{{port}}/{{database}}", "ru.yandex.clickhouse.ClickHouseDriver"),
		MSSQL("jdbc:sqlserver://{{host}}:{{port}};databaseName={{database}}", "com.microsoft.sqlserver.jdbc.SQLServerDriver");

		private String dbUrl;

		private String driverClass;

		SqlType(String dbUrl, String driverClass) {
			this.dbUrl = dbUrl;
			this.driverClass = driverClass;
		}

		public String getDriverClass() {
			return this.driverClass;
		}

		public String createDbUrl(String host, String port, String database, boolean sslKey, String sslPropertyKey) {
			String dbURL = "";
			try {
				dbURL = StringUtils.replaceDynamicValues(this.dbUrl, createDataObject(host, port, database));
				if (sslKey) {
					dbURL = dbURL + sslPropertyKey;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return dbURL;
		}

		private JSONObject createDataObject(String host, String port, String database) throws JSONException {
			JSONObject data = new JSONObject();
			data.put("host", host);
			data.put("port", port);
			data.put("database", database);
			return data;
		}

	}

	@Override
	public boolean verify(Connection connection) throws NumberFormatException, JSchException {
		String type = connection.getType(),dburl = null;
		type = type.toUpperCase();
		switch (SqlType.valueOf(type)) {
		case MSSQL:
            dburl = SqlType.MSSQL.createDbUrl(connection.getParam2(), connection.getParam3(), connection.getParam5(), false, null);
			break;
		case POSTGRES:
			dburl = SqlType.POSTGRES.createDbUrl(connection.getParam2(), connection.getParam3(), connection.getParam5(), false, null);
			break;
		case MYSQL:
			dburl = SqlType.MYSQL.createDbUrl(connection.getParam2(), connection.getParam3(), connection.getParam5(), false, null);
			break;
		case CLICKHOUSE:
			dburl = SqlType.CLICKHOUSE.createDbUrl(connection.getParam2(), connection.getParam3(), connection.getParam5(), false, null);
			break;
		default:
			break;
		}
		try(java.sql.Connection con = DriverManager.getConnection(dburl,connection.getParam1(),connection.getParam4())){
			return true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
