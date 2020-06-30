package net.oopscraft.application.user;

import org.springframework.beans.factory.annotation.Autowired;

import net.oopscraft.application.test.ApplicationTestRunner;


public class UserServiceTest extends ApplicationTestRunner {
	
	private static final String USER_ID = "JUnit";
	private static final String USER_PASSWORD = "1234";
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepository;
	
	public UserServiceTest() throws Exception {
		super();
	}
//
//	@Test
//	public void testGetUsers() throws Exception {
//		UserService.SearchCondition searchCondition = userService.new SearchCondition();
//		pagination pagination = new pagination(1,20,true);
//		List<User> users = userService.getUsers(searchCondition, pagination);
//		System.out.println(new TextTable(users));
//		System.out.println(new TextTable(pagination));
//	}
//	
//	@Test
//	public void testGetUser() throws Exception {
//		User user = userService.getUser(USER_ID);
//		System.out.println(new TextTable(user));
//	}
//	
//	@Test
//	public void testSaveUser() throws Exception {
//		User user = userService.getUser(USER_ID);
//		userService.saveUser(user);
//	}
//	
//	@Test
//	public void testIsValidPassword() throws Exception {
//		
//		String password = "1111";
//		String wrongPassword = "1212";
//		
//		// sets password and save new user.
//		User user = userService.removeUser(USER_ID);
//		user.setPassword(password);
//		userService.saveUser(user);
//		
//		// checks correct password
//		if(userService.isValidPassword(user.getId(), password) == false) {
//			assert(false);
//		}
//		
//		// checks incorrect password
//		if(userService.isValidPassword(user.getId(), wrongPassword) == true) {
//			assert(false);
//		}
//		assert(true);
//	}
//	
//	@Test
//	public void testChangePassword() throws Exception {
//		String currentPassword = "1111";
//		String newPassword = "2222";
//		
//		// sets password and save new user.
//		User user = userService.removeUser(USER_ID);
//		user.setPassword(currentPassword);
//		userService.saveUser(user);
//		
//		// changes password
//		userService.changePassword(user.getId(), currentPassword, newPassword);
//		if(userService.isValidPassword(user.getId(), newPassword) == false) {
//			assert(false);
//		}
//	}
//	
//	@Test
//	public void testRemoveUser() throws Exception {
//		User user = userService.getUser(USER_ID);
//		userService.removeUser(user.getId());
//	}
//	

}
