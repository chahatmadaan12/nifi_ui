package com.applicate.nifiui.dbmanager.dao.dboperation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.applicate.nifiui.dbmanager.dao.beans.Regex;

public interface RegexDAO extends JpaRepository<Regex, String>{
	
	@Query("SELECT r from Regex r WHERE r.lob =:lob")
	public List<Regex> getRegexBasedOnLob(@Param("lob") String lob);
	
}
