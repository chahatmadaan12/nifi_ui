package com.applicate.nifiui.dbmanager.dao.beans;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TemplateDetails")
public class TemplateDetails {
	
	@Id
	private String id;
	
	private String lob,extractorId,extractorTable,mapping,loaderId,loaderTable,niFiTemplateId,niFiParentProcessedGroup,templateId;
	
	private double originY;
	
	private boolean templateCreated,templateUploaded;

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
	

	public String getNiFiTemplateId() {
		return niFiTemplateId;
	}

	public void setNiFiTemplateId(String niFiTemplateId) {
		this.niFiTemplateId = niFiTemplateId;
	}

	public String getNiFiParentProcessedGroup() {
		return niFiParentProcessedGroup;
	}

	public void setNiFiParentProcessedGroup(String niFiParentProcessedGroup) {
		this.niFiParentProcessedGroup = niFiParentProcessedGroup;
	}

	public boolean isTemplateCreated() {
		return templateCreated;
	}

	public void setTemplateCreated(boolean templateCreated) {
		this.templateCreated = templateCreated;
	}
	

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public boolean isTemplateUploaded() {
		return templateUploaded;
	}

	public void setTemplateUploaded(boolean templateUploaded) {
		this.templateUploaded = templateUploaded;
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
				+ ", niFiTemplateId=" + niFiTemplateId + ", niFiParentProcessedGroup=" + niFiParentProcessedGroup
				+ ", templateCreated=" + templateCreated + ", templateUploaded=" + templateUploaded + ", originY=" + originY +"]";
	}

}
