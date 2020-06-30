package net.oopscraft.agent;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class AgentContext {
	

	@Bean
	public DataSource dataSource() throws Exception {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/spring_jpa");
		dataSource.setUsername( "tutorialuser" );
		dataSource.setPassword( "tutorialmy5ql" );
		return dataSource;
	}
	
	@Bean
	@DependsOn({"dataSource"})
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws Exception {
	      LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
	      entityManagerFactory.setDataSource(dataSource());
	      entityManagerFactory.setPackagesToScan(new String[] { "com.baeldung.persistence.model" });
	      HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	      vendorAdapter.setDatabasePlatform(Agent.agentConfig.getEntityManagerFactory().getDatabasePlatform());
	      entityManagerFactory.setJpaVendorAdapter(vendorAdapter);
	      Properties jpaProperties = new Properties();
	      jpaProperties.setProperty(AvailableSettings.HBM2DDL_AUTO, "create-drop");
	      entityManagerFactory.setJpaProperties(jpaProperties);
	      return entityManagerFactory;
	}
	
	@Bean
	public ConfigurableServletWebServerFactory webServerFactory() {
		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
	    factory.setPort(Agent.agentConfig.webServer.port);
	    factory.setContextPath("");
	    return factory;
	}
	
}
