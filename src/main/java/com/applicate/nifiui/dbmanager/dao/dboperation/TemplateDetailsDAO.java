package com.applicate.nifiui.dbmanager.dao.dboperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.applicate.nifiui.dbmanager.dao.beans.TemplateDetails;

public interface TemplateDetailsDAO extends CrudRepository<TemplateDetails, String>{

	@Query("SELECT s from TemplateDetails s WHERE s.lob =:lob")
	public List<TemplateDetails> getTemplateDetailsBasedOnLob(@Param("lob") String lob);
	
	@Query("SELECT max(s.originY) from TemplateDetails s WHERE s.lob =:lob")
	public Double getMaxOriginY(@Param("lob") String lob);
	
	public default List<TemplateDetails> getTemplateDetailsIfPersent(TemplateDetailsDAO dao,String id) {
		List<TemplateDetails> ls = new ArrayList<TemplateDetails>();
		Optional<TemplateDetails> findById = dao.findById(id);
		if(findById.isPresent()) {
			ls.add(findById.get());
		}
		return ls;
	}
	
}
