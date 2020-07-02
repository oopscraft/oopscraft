package net.oopscraft.agent;

public class AgentConfig {
	
	/**
	 * Returns environment
	 * @return
	 */
	public static String getProfile() {
		String profile = System.getProperty("agent.profile");
		if(profile == null || profile.trim() == "") {
			return "dev";
		}else {
			return profile.trim();
		}
	}

	class WebServer {
		int port = 8080;

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}
	}
	WebServer webServer = new WebServer();
	
	class EntityManagerFactoryConfig {
		String databasePlatform;
		String packagesToScan;
		public String getDatabasePlatform() {
			return databasePlatform;
		}
		public void setDatabasePlatform(String databasePlatform) {
			this.databasePlatform = databasePlatform;
		}
	}
	transient EntityManagerFactoryConfig entityManagerFactory = new EntityManagerFactoryConfig();
	
	
	public WebServer getWebServer() {
		return webServer;
	}
	public void setWebServer(WebServer webServer) {
		this.webServer = webServer;
	}
	public EntityManagerFactoryConfig getEntityManagerFactory() {
		return entityManagerFactory;
	}
	public void setEntityManagerFactory(EntityManagerFactoryConfig entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}
	
}
