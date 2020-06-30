package net.oopscraft.application.security;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.servlet.LocaleResolver;

import net.oopscraft.application.ApplicationWebContext;
import net.oopscraft.application.user.User;
import net.oopscraft.application.user.UserLogin;
import net.oopscraft.application.user.UserLoginRepository;
import net.oopscraft.application.user.UserService;

public class AuthenticationHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler, AuthenticationEntryPoint, LogoutSuccessHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationHandler.class);
	
	@Autowired
	MessageSource messageSource;
	
	@Autowired
	LocaleResolver localeResolver;
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserLoginRepository userLoginRepository;
	
	@Autowired
	AuthenticationProvider authenticationProvider;
	
	/**
	 * On authentication is success.
	 * @param HttpServletRequest, HttpServletResponse, Authentication
	 * @return void
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		try {
	        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
			
			// issue JWT access token.
	        String accessToken = authenticationProvider.encodeAccessToken(userDetails);
			response.setHeader(ApplicationWebContext.ACCESS_TOKEN_HEADER_NAME, accessToken);
			Cookie accessTokenCookie = new Cookie(ApplicationWebContext.ACCESS_TOKEN_HEADER_NAME, accessToken);
			accessTokenCookie.setPath("/");
			accessTokenCookie.setHttpOnly(true);
			response.addCookie(accessTokenCookie);
			
			// issue JWT refresh token
			LOGGER.info("rememberMe:{}", request.getParameter("rememberMe"));
			if(request.getParameter("rememberMe") != null 
			&& Boolean.parseBoolean(request.getParameter("rememberMe").trim())){
		        String refreshToken = authenticationProvider.encodeRefreshToken(userDetails);
				response.setHeader(ApplicationWebContext.REFRESH_TOKEN_HEADER_NAME, refreshToken);
				Cookie refreshTokenCookie = new Cookie(ApplicationWebContext.REFRESH_TOKEN_HEADER_NAME, refreshToken);
				refreshTokenCookie.setPath("/");
				refreshTokenCookie.setHttpOnly(true);
				// sets max-age to persist token in browser.
				int maxAge = authenticationProvider.getRefreshTokenTimeout()*60;
				refreshTokenCookie.setMaxAge(maxAge);		
				response.addCookie(refreshTokenCookie);
			}
			
			// sets user default locale
			if(StringUtils.isNotEmpty(userDetails.getLanguage())){
				localeResolver.setLocale(request, response, Locale.forLanguageTag(userDetails.getLanguage()));
			}
			
			// Saves Login History
			saveUserLoginHistory(request, "Y", null);

			// sets response header
			response.setStatus(HttpServletResponse.SC_OK);
			
		}catch(Exception e) {
			LOGGER.warn(e.getMessage(), e);
			throw new ServletException(e);
		}
	}
	
	/**
	 * On authentication is failed.
	 * @param HttpServletRequest, HttpServletResponse, AuthenticationException
	 * @return void
	 */
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		String message = null;
		Locale locale = localeResolver.resolveLocale(request);
		message = messageSource.getMessage(exception.getMessage(), null, locale);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		OutputStream out = response.getOutputStream();
		out.write(message.getBytes());
		
		// Saves Login History
		if(exception instanceof BadCredentialsException) {
			saveUserLoginHistory(request, "N", message);
		}
	}
	
	/*
	 * Saves user login history
	 */
	private void saveUserLoginHistory(HttpServletRequest request, String successYn, String failReason) {
		try {
			User user = userService.getUserByEmail(request.getParameter("username"));
			if(user != null) {
				UserLogin userLogin = new UserLogin();
				userLogin.setUserId(user.getId());
				userLogin.setDate(new Date());
				userLogin.setSuccessYn(successYn);
				userLogin.setFailReason(failReason);
				userLogin.setIp(request.getRemoteAddr());
				userLogin.setAgent(request.getHeader("User-Agent"));
				userLogin.setReferer(request.getHeader("referer"));
				userLoginRepository.saveAndFlush(userLogin);
			}
		}catch(Exception ignore) {
			LOGGER.warn(ignore.getMessage(), ignore);
		}
	}
	
	/**
	 * logout
	 */
	@Override 
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		if(request.getCookies() != null) {
	        for(Cookie cookie : request.getCookies()) {
	        	if(ApplicationWebContext.ACCESS_TOKEN_HEADER_NAME.equals(cookie.getName())) {
	        		cookie.setPath("/");
	        		cookie.setValue("");
	        		cookie.setMaxAge(-1);
	        		response.addCookie(cookie);
	        	}
	        }
		}
	}

	/*
	 * AuthenticationEntryPoint.commence
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    	LOGGER.warn(authException.getMessage());
    	// in case of AJAX request
    	if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
        	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        	response.getWriter().flush();
    	} else {
    		response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
    		if(request.getRequestURI().startsWith("/admin")){
    			response.setHeader("Location", "/admin/login");
    		}else {
    			response.setHeader("Location", "/user/login");
    		}
    		response.getWriter().flush();
    	}
	}

}
