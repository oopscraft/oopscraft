package net.oopscraft.application;

import java.util.Set;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Implementation of javax.servlet.ServletContainerInitializer
 * @version 0.0.1
 * @see    
 */
public class ApplicationWebInitializer implements ServletContainerInitializer {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ApplicationWebInitializer.class);

	@Override
	public void onStartup(Set<Class<?>> c, ServletContext servletContext) throws ServletException {
		LOGGER.info("ApplicationWebXml start...");
		
		// invokes application context
		LOGGER.info("applicationContext:{}", Application.applicationContext);
		if(Application.applicationContext == null) {
			Application.applicationContext = new AnnotationConfigApplicationContext(ApplicationContext.class);
		}
		
		// adds application context
		AnnotationConfigWebApplicationContext webAapplicationContext = new AnnotationConfigWebApplicationContext();
		webAapplicationContext.setParent(Application.applicationContext);
        webAapplicationContext.register(ApplicationWebContext.class);
        servletContext.addListener(new ContextLoaderListener(webAapplicationContext));
        webAapplicationContext.setServletContext(servletContext);
        
        // add dispatcherServlet
        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcherServlet", new DispatcherServlet(webAapplicationContext));
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");
        
        // adds spring CharacterEncodingFilter
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        FilterRegistration.Dynamic encodingFilterDynamic = servletContext.addFilter("encodingFilter", encodingFilter);
        encodingFilterDynamic.addMappingForUrlPatterns(null, true, "/*");
        
        // add JPA transaction filter
        OpenEntityManagerInViewFilter openEntityManagerInViewFilter = new OpenEntityManagerInViewFilter();
        FilterRegistration.Dynamic openEntityManagerInViewFilterDynamic = servletContext.addFilter("openEntityManagerInViewFilter", openEntityManagerInViewFilter);
        openEntityManagerInViewFilterDynamic.addMappingForUrlPatterns(null, true, "/*");
        
        // adds spring security filter chain
        DelegatingFilterProxy springSecurityFilterChain = new DelegatingFilterProxy();
        FilterRegistration.Dynamic springSecurityFilterChainDynamic = servletContext.addFilter("springSecurityFilterChain", springSecurityFilterChain);
        springSecurityFilterChainDynamic.addMappingForUrlPatterns(null, true, "/*");
	}

}
