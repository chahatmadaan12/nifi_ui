package com.applicate.nifiui.dbmanager.dao.beans;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TemplateDetails")
public class TemplateDetails {
	
	@Id
	private String id;
	
	private String lob,extractorId,extractorTable,mapping,loaderId,loaderTable,templateId;
	
	private double originY;
	
	private boolean active;

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

	public String getExtractorId() {
		return extractorId;
	}

	public void setExtractorId(String extractorId) {
		this.extractorId = extractorId;
	}

	public String getExtractorTable() {
		return extractorTable;
	}

	public void setExtractorTable(String extractorTable) {
		this.extractorTable = extractorTable;
	}

	public String getMapping() {
		return mapping;
	}

	public void setMapping(String mapping) {
		this.mapping = mapping;
	}

	public String getLoaderId() {
		return loaderId;
	}

	public void setLoaderId(String loaderId) {
		this.loaderId = loaderId;
	}

	public String getLoaderTable() {
		return loaderTable;
	}

	public void setLoaderTable(String loaderTable) {
		this.loaderTable = loaderTable;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean getActive() {
		return this.active;
	}

	public double getOriginY() {
		return originY;
	}

	public void setOriginY(double originY) {
		this.originY = originY;
	}

	@Override
	public String toString() {
		return "TemplateDetails [id=" + id + ", lob=" + lob + ", extractorId=" + extractorId + ", extractorTable="
				+ extractorTable + ", mapping=" + mapping + ", loaderId=" + loaderId + ", loaderTable=" + loaderTable
				+ ", Active=" + active + ", originY=" + originY +"]";
	}

}
