package com.applicate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.applicate.nifiui.dbmanager.dao.beans.Connection;
import com.applicate.nifiui.dbmanager.dao.dboperation.ConnectionDAO;
import com.applicate.nifiui.service.ConnectionVerificationService;
import com.applicate.nifiui.service.ConnectionVerifierFactory;

@SpringBootTest
class NifiApplicationTests {

	@Autowired
	private ConnectionDAO conDao;
	
	@Autowired
	private ApplicationContextProvider context;
	
	@Autowired
	private ConnectionVerifierFactory factory;

//	@Test
    public void testCreateConn() {
//	    List resultList = conDao.createNativeQuery("select * from Connection where lob=?").setParameter(1, "ABC").getResultList();
//        
//		Connection connection = conDao.findById("12nasdj").get();
//		ConnectionVerificationService connectionVerifier = factory.getConnectionVerifier(connection.getType());
//		try {
//			System.out.println(connectionVerifier.verify(connection));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(conDao.getConnectionBasedOnLob("ABC"));
		
//		Connection con = context.getContext().getBean(Connection.class);
//    	con.setId("12n67634");
//		con.setType("sftp");
//		con.setParam1("a");
//		con.setParam2("a");
//		con.setParam3("a");
//		con.setParam4("a");
//		con.setParam5("a");
//		con.setParam6("a");
//		con.setParam7("a");
//		con.setLob("ABC");
//		System.out.println(conDao.save(con));
    }
	@Test
    public void testCreateConn1() {
		conDao.findAll().forEach(k->System.out.println(k.getId()+" "+k.getType()+" "+k.getParam1()));
    }

}
