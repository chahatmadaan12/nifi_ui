package com.applicate.nifiui.dbmanager.dao.dboperation;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.applicate.nifiui.dbmanager.dao.beans.NiFiSchema;

public interface NiFiSchemaDAO extends CrudRepository<NiFiSchema, NiFiSchema>{

	@Query("SELECT s from NiFiSchema s WHERE s.lob =:lob order by schemaName")
	public List<NiFiSchema> getNiFiSchemaBasedOnLob(@Param("lob") String lob);
	
	@Query("SELECT s from NiFiSchema s WHERE s.lob =:lob and s.schemaName =:schemaName order by schemaName")
	public List<NiFiSchema> getNiFiSchemaBasedOnSchemaNameAndLob(@Param("lob") String lob,@Param("schemaName") String schemaName);
	
	@Query("SELECT distinct s.schemaName from NiFiSchema s WHERE s.lob =:lob order by schemaName")
	public List<String> getTableNames(@Param("lob") String lob);

}
