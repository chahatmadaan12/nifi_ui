package com.applicate.nifiui.dbmanager.dao.beans;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="Connection")
public class Connection implements Serializable{
	
	@Id
	private String id;
	private String name,lob,type,param1,param2,param3,param4,param5,param6,param7,param8,param9;
	private boolean active;

	public String getParam9() {
		return param9;
	}

	public void setParam9(String param9) {
		this.param9 = param9;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLob() {
		return lob;
	}

	public void setLob(String lob) {
		this.lob = lob;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public String getParam3() {
		return param3;
	}

	public void setParam3(String param3) {
		this.param3 = param3;
	}

	public String getParam4() {
		return param4;
	}

	public void setParam4(String param4) {
		this.param4 = param4;
	}

	public String getParam5() {
		return param5;
	}

	public void setParam5(String param5) {
		this.param5 = param5;
	}

	public String getParam6() {
		return param6;
	}

	public void setParam6(String param6) {
		this.param6 = param6;
	}

	public String getParam7() {
		return param7;
	}

	public void setParam7(String param7) {
		this.param7 = param7;
	}

	public String getParam8() {
		return param8;
	}

	public void setParam8(String param8) {
		this.param8 = param8;
	}

	@Override
	public String toString() {
		return "{ \"id\":\"" + id + "\", \"lob\":\"" + lob + "\", \"type\":\"" + type + "\", \"param1\":\"" + param1 + "\", \"param2\":\"" + param2
				+ "\", \"param3\":\"" + param3 + "\", \"param4\":\"" + param4 + "\", \"param5\":\"" + param5 + "\", \"param6\":\"" + param6
				+ "\",\"param7\":\"" + param7 + "\",\"param8\":\"" + param8 + "\", \"active\":" + active + "}";
	}
	

}
