package com.applicate.nifiui.configuration.provider;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.applicate.utils.FileUtils;
@Component
public class GlobalConfigurationProvider {
	
	@Value("${lob's}")
	private String runningClients;

//	@Value("${configPath}")
//	private String configPath;
	
    private static final Map<String, ConfigurationProvider> CONFIGURATION_PROVIDERS = new HashMap<>();
    
    @PostConstruct
    private void init() {
    	//FileUtils.setCofigPath(configPath);
    	String[] clients = runningClients.split(",");
    	for (String client : clients) {
			loadConfigurationProvider(client);
		}
    }
    
    public ConfigurationProvider get(String lob) {
        if (!CONFIGURATION_PROVIDERS.containsKey(lob)) {
            loadConfigurationProvider(lob);
        }
        return CONFIGURATION_PROVIDERS.get(lob);
    }
    
    private void loadConfigurationProvider(String clientName) {
        CONFIGURATION_PROVIDERS.put(clientName, new DefaultConfigurationProvider(clientName));
    }

}
