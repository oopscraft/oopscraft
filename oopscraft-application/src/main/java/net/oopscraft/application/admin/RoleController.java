package net.oopscraft.application.admin;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.core.Pagination;
import net.oopscraft.application.user.Role;
import net.oopscraft.application.user.RoleService;

@PreAuthorize("hasAuthority('ADMN_ROLE')")
@Controller
@RequestMapping("/admin/role")
public class RoleController {
	
	@Autowired
	RoleService roleService;
	
	/**
	 * Forwards view page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView modelAndView = new ModelAndView("admin/role.html");
		return modelAndView;
	}
	
	/**
	 * Returns roles
	 * @param role
	 * @param pagination
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getRoles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Role> getRoles(@ModelAttribute Role role, Pagination pagination, HttpServletResponse response) throws Exception {
		pagination.setEnableTotalCount(true);
		List<Role> roles = roleService.getRoles(role, pagination);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pagination.getContentRange());
		return roles;
	}
	
	/**
	 * Returns role
	 * @param role
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getRole", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Role getRole(@ModelAttribute Role role) throws Exception {
		return roleService.getRole(role.getId());
	}

	/**
	 * Saves role
	 * @param role
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('ADMN_ROLE_EDIT')")
	@RequestMapping(value = "saveRole", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional
	public Role saveRole(@RequestBody Role role) throws Exception {
		return roleService.saveRole(role);
	}
	
	/**
	 * Deletes role
	 * @param role
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('ADMN_ROLE_EDIT')")
	@RequestMapping(value = "deleteRole", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional
	public void deleteRole(@RequestBody Role role) throws Exception {
		roleService.deleteRole(role);
	}

}
