package com.applicate.nifiui.dbmanager.dao.dboperation;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.applicate.nifiui.dbmanager.dao.beans.Connection;


public interface ConnectionDAO extends CrudRepository<Connection, String>{
	
	@Query("SELECT c from Connection c WHERE c.lob =:lob")
	public List<Connection> getConnectionBasedOnLob(@Param("lob") String lob);
	
}
