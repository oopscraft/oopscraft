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
import net.oopscraft.application.message.Message;
import net.oopscraft.application.message.MessageI18n;
import net.oopscraft.application.message.MessageService;

@PreAuthorize("hasAuthority('ADMN_MESG')")
@Controller
@RequestMapping("/admin/message")
public class MessageController {

	@Autowired
	MessageService messageService;

	/**
	 * Forwards page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView modelAndView = new ModelAndView("admin/message.html");
		return modelAndView;
	}
	
	/**
	 * Returns messages
	 * @param role
	 * @param pagination
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getMessages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Message> getMessages(@ModelAttribute Message message, Pagination pagination, HttpServletResponse response) throws Exception {
		pagination.setEnableTotalCount(true);
		List<Message> messages = messageService.getMessages(message, pagination);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pagination.getContentRange());
		return messages;
	}
	
	/**
	 * Returns message
	 * @param message
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getMessage", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Message getMessage(@ModelAttribute Message message) throws Exception {
		return messageService.getMessage(message.getId());
	}

	/**
	 * Saves message
	 * @param message
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('ADMN_MESG_EDIT')")
	@RequestMapping(value = "saveMessage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional
	public Message saveMessage(@RequestBody Message message) throws Exception {
		return messageService.saveMessage(message);
	}
	
	/**
	 * Deletes message
	 * @param message
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('ADMN_MESG_EDIT')")
	@RequestMapping(value = "deleteMessage", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional
	public void deleteMessage(@ModelAttribute Message message) throws Exception {
		messageService.deleteMessage(message);
	}
	
	/**
	 * getMessageI18ns
	 * @param message
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('ADMN_MESG_EDIT')")
	@RequestMapping(value = "getMessageI18ns", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<MessageI18n> getMessageI18ns(@ModelAttribute Message message) throws Exception {
		return messageService.getMesageI18ns(message.getId());
	}
	
	/**
	 * saveMessageI18ns
	 * @param message
	 * @param messageI18ns
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('ADMN_MESG_EDIT')")
	@RequestMapping(value = "saveMessageI18ns", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional
	public List<MessageI18n> saveMessageI18ns(@ModelAttribute Message message, @RequestBody List<MessageI18n> messageI18ns) throws Exception {
		return messageService.saveMessageI18ns(message.getId(), messageI18ns);
	}

}