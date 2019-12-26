package com.applicate.nifiui.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.applicate.nifiui.configurations.ConnectionConfiguration.Connections;
import com.applicate.nifiui.configurations.ConnectionConfiguration.impl.SFTPConnectionImpl;

@Configuration
public class BeanConfig{
	
	@Bean("sftp")
	@Scope("prototype")
	public Connections getSFTPConnectionsConfig() {
		return new SFTPConnectionImpl();
	}
	
}