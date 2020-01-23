package com.applicate.nifiui.dbmanager.dao.beans;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "NiFiSchema")
@IdClass(NiFiSchema.class)
public class NiFiSchema implements Serializable{
	@Id
	private String schemaName;
	@Id
  	private String lob;
	@Id
  	private String columnName;
	
	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getLob() {
		return lob;
	}

	public void setLob(String lob) {
		this.lob = lob;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	@Override
	public String toString() {
		return "NiFiSchema [schemaName=" + schemaName + ", lob=" + lob + ", columnName=" + columnName + "]";
	}


}
