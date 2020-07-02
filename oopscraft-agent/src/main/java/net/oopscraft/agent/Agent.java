package net.oopscraft.agent;

import java.util.Properties;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import net.oopscraft.core.PasswordBasedEncryptor;

public class Agent {
	
	static AgentConfig agentConfig;
	static SpringApplication springApplication;
	static ApplicationContext applicationContext;
	
	/**
	 * starts spring application
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		// creates spring application
		SpringApplication springApplication = new SpringApplication(AgentContext.class);
		springApplication.setBannerMode(Banner.Mode.OFF);
		
		// sets configuration
		Properties configProperties = loadConfigProperties();
		springApplication.setDefaultProperties(configProperties);
		ConfigurationPropertySource propertySource = new MapConfigurationPropertySource(configProperties);
		Binder binder = new Binder(propertySource);
		agentConfig = binder.bind("agent", AgentConfig.class).get();

		// starts
		applicationContext = springApplication.run(args);
	}
	
	/**
	 * loads configuration properties
	 * @return
	 * @throws Exception
	 */
	private static Properties loadConfigProperties() throws Exception {
		FileSystemResource resource = new FileSystemResource(String.format("conf/%s/agent.properties", AgentConfig.getProfile()));
		Properties configProperties = PropertiesLoaderUtils.loadProperties(resource);
		PasswordBasedEncryptor pbEncryptor = new PasswordBasedEncryptor();
		for(String propertyName : configProperties.stringPropertyNames()) {
			String value = configProperties.getProperty(propertyName);
			configProperties.put(propertyName, pbEncryptor.decryptIdentifiedValue(value));
		}
		return configProperties;
	}
	

}
