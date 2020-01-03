package com.applicate.nifiui.dbmanager.dao.dboperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.applicate.nifiui.dbmanager.dao.beans.Connection;

public interface ConnectionDAO extends CrudRepository<Connection, String>{
	
	@Query("SELECT c from Connection c WHERE c.lob =:lob")
	public List<Connection> getConnectionBasedOnLob(@Param("lob") String lob);
	
	@Query("SELECT c from Connection c WHERE c.active =:active")
	public List<Connection> getConnectionBasedOnActive(@Param("active") String active);
	
	public default List<Connection> getConnectionIfPersent(ConnectionDAO dao,String id) {
		List<Connection> ls = new ArrayList<Connection>();
		Optional<Connection> findById = dao.findById(id);
		if(findById.isPresent()) {
			ls.add(findById.get());
		}
		return ls;
	}
	
	
}
