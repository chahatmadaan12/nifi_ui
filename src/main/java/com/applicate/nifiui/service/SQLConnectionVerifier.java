package com.applicate.nifiui.service;

import static com.applicate.nifiui.config.constants.ConnectionConstants.*;

import java.sql.DriverManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

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
			data.put(HOST, host);
			data.put(PORT, port);
			data.put(DATABASE, database);
			return data;
		}

	}

	@Override
	public JSONObject verify(JSONObject connection) throws NumberFormatException, JSchException {
		String type = connection.getString(TYPE),dburl = null;
		JSONObject response = new JSONObject();
		type = type.toUpperCase();
		switch (SqlType.valueOf(type)) {
		case MSSQL:
            dburl = SqlType.MSSQL.createDbUrl(connection.getString(HOST), connection.getString(PORT), connection.getString(DB_NAME), false, null);
            response.put(DRIVER_JAR_KEY, "mssql_driver_jar_name")
		            .put(DB_URL, dburl)
					.put(DRIVER_CLASS, SqlType.valueOf(type).getDriverClass());
			break;
		case POSTGRES:
			dburl = SqlType.POSTGRES.createDbUrl(connection.getString(HOST), connection.getString(PORT), connection.getString(DB_NAME), false, null);
			response.put(DRIVER_JAR_KEY, "postgres_driver_jar_name")
					.put(DB_URL, dburl)
					.put(DRIVER_CLASS, SqlType.valueOf(type).getDriverClass());
			break;
		case MYSQL:
			dburl = SqlType.MYSQL.createDbUrl(connection.getString(HOST), connection.getString(PORT), connection.getString(DB_NAME), false, null);
			response.put(DRIVER_JAR_KEY, "mysql_driver_jar_name")
					.put(DB_URL, dburl)
					.put(DRIVER_CLASS, SqlType.valueOf(type).getDriverClass());
			break;
		case CLICKHOUSE:
			dburl = SqlType.CLICKHOUSE.createDbUrl(connection.getString(HOST), connection.getString(PORT), connection.getString(DB_NAME), false, null);
			response.put(DRIVER_JAR_KEY, "clickhouse_driver_jar_name")
					.put(DB_URL, dburl)
					.put(DRIVER_CLASS, SqlType.valueOf(type).getDriverClass());
			break;
		default:
			break;
		}
		try(java.sql.Connection con = DriverManager.getConnection(dburl,connection.getString(USER_NAME),connection.getString(PASSWORD))){
			response.put("verify", true);
			return response;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return response.put("verify", false);
	}

}
