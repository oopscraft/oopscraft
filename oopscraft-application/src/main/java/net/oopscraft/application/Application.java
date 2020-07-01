package net.oopscraft.application;

import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import net.oopscraft.core.webserver.WebServer;
import net.oopscraft.core.webserver.WebServerBuilder;
import net.oopscraft.core.webserver.WebServerContext;

/**
 * Application Context Configuration
 * @version 0.0.1
 */
public class Application {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
	public static AnnotationConfigApplicationContext applicationContext = null;
	public static ConfigurableEnvironment environment = null;
	public static WebServer webServer = null;
	
	public static void main(String[] args) throws Exception {

		// setting log4j2 configuration path
		Configurator.initialize(null, String.format("conf/%s/log4j2.xml", ApplicationConfig.getProfile()));
		
		// loads application context
		applicationContext = new AnnotationConfigApplicationContext();
		applicationContext.register(ApplicationContext.class);
		applicationContext.refresh();
		for(String name : applicationContext.getBeanDefinitionNames()) {
			LOGGER.info("Bean:{}", name);
		}
		ApplicationConfig applicationConfig = ApplicationContext.applicationConfig;
		LOGGER.info("ApplicationConfig:{}", ApplicationContext.applicationConfig);
		
		// hooking kill signal
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
			public void run() {
				printInfo("Runtime.getRuntime().addShutdownHook...");
				try {
					applicationContext.close();
				}catch(Exception e){
					e.printStackTrace(System.err);
				}
				try {
					webServer.stop();
				}catch(Exception e){
					printError(e.getMessage(), e);
				}
			}
		}));

		// creates web server
		WebServerBuilder webServerBuilder = new WebServerBuilder(applicationConfig.getWebServer().getType());
		webServerBuilder.setPort(applicationConfig.getWebServer().getPort());
		webServerBuilder.setSsl(applicationConfig.getWebServer().isSsl());
		webServerBuilder.setKeyStorePath(applicationConfig.getWebServer().getKeyStorePath());
		webServerBuilder.setKeyStoreType(applicationConfig.getWebServer().getKeyStoreType());
		webServerBuilder.setKeyStorePass(applicationConfig.getWebServer().getKeyStorePass());
		WebServerContext context = new WebServerContext();
		context.setContextPath("/");
		context.setResourceBase("webapp");
		webServerBuilder.addContext(context);
		webServer = webServerBuilder.build();
		
		// starts web server
		webServer.start();
		
		// join main thread
		Thread.currentThread().join();
	}
	
	/**
	 * printInfo
	 * @param message
	 */
	private static void printInfo(Object message) {
		LOGGER.warn("{}", message);
		System.err.println(message);
	}
	
	/**
	 * printInfo
	 * @param message
	 * @param e
	 */
	private static void printError(String message, Exception e) {
		LOGGER.error(e.getMessage(), e);
		e.printStackTrace(System.err);
	}

}
