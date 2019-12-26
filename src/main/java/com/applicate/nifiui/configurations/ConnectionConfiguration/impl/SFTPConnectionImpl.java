package com.applicate.nifiui.configurations.ConnectionConfiguration.impl;

import com.applicate.nifiui.configurations.ConnectionConfiguration.Connections;

public class SFTPConnectionImpl implements Connections{
	private String id,type="sftp",param1,param2,param3,param4,param5,param6,param7;

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	@Override
	public void setId(String id) {
		this.id=id;
	}

	public String getParam1() {
		return param1;
	}

	@Override
	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public String getParam2() {
		return param2;
	}

	@Override
	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public String getParam3() {
		return param3;
	}

	@Override
	public void setParam3(String param3) {
		this.param3 = param3;
	}

	public String getParam4() {
		return param4;
	}

	@Override
	public void setParam4(String param4) {
		this.param4 = param4;
	}

	public String getParam5() {
		return param5;
	}

	@Override
	public void setParam5(String param5) {
		this.param5 = param5;
	}

	public String getParam6() {
		return param6;
	}

	@Override
	public void setParam6(String param6) {
		this.param6 = param6;
	}

	public String getParam7() {
		return param7;
	}

	@Override
	public void setParam7(String param7) {
		this.param7 = param7;
	}

	@Override
	public String getConnection() {
		return "{'id':"+getId()+",'type':"+getType()+",'param1':"+getParam1()+",'param2':"+getParam2()+",'param3':"+getParam3()+",'param4':"+getParam4()+",'param5':"+getParam5()+",'param6':"+getParam6()+",'param7':"+getParam7()+"}";
	}
}
