package com.applicate.nifiui.dbmanager.dao.beans;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Regex")
public class Regex {
	@Id
	private String id;
	private String lob, name, regex,message,description;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLob() {
		return lob;
	}

	public void setLob(String lob) {
		this.lob = lob;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\", \"lob\":\"" + lob + "\", \"name\":\"" + name + "\", \"regex\":\"" + regex + "\",\"message\":\""+message+"\",\"description\":\""+description+"\"}";
	}

	
}
