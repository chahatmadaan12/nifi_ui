package com.applicate;

import java.util.Arrays;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import com.applicate.nifiui.config.Test;
import com.applicate.nifiui.configuration.provider.GlobalConfigurationProvider;
import com.applicate.nifiui.mapper.ConnectionMapper;
import com.applicate.nifiui.service.ConnectionMapperService;

@SpringBootApplication
public class NifiApplication implements CommandLineRunner{
	
	@Autowired
    private ApplicationContext appContext;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(NifiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		DefaultConfigurationLoader bean2 = appContext.getBean(DefaultConfigurationLoader.class);
//		System.out.println(bean2.toString());
//		
        Test test = new Test();
        System.out.println(test.schemaServices+"   "+test);
        Test test1 = new Test();
        System.out.println(test1.schemaServices+"   "+test1);
        System.out.println(appContext.getBean(Test.class).schemaServices+"   "+appContext.getBean(Test.class)+"   "+appContext.getBean(Test.class));
//      System.out.println(appContext.getBean(DefaultConfigurationLoader.class).toString());
//		GlobalConfigurationProvider bean = appContext.getBean(GlobalConfigurationProvider.class);
//		System.out.println(bean.get("Ambuja").getConnectionParam("mysql"));
//		ConnectionMapperService bean = appContext.getBean(ConnectionMapperService.class);
//		System.out.println(bean.getUnWrappedConnection(new JSONObject().put("param1", "applicate").put("param4", "myApplicate"), "Ambuja", "sftp"));
//		System.out.println(bean.getWrappedConnection(new JSONObject().put("userName", "applicate").put("password", "myApplicate"), "Ambuja", "sftp"));
		//new FileUtils().printVal();
	}

}
