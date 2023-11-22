package ca.sheridancollege.fangyux.web;

import java.io.IOException;
import java.sql.Blob;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.sql.rowset.serial.SerialBlob;

import ca.sheridancollege.fangyux.Utils.ImageOperation;
import ca.sheridancollege.fangyux.Utils.ResultEntity;
import ca.sheridancollege.fangyux.beans.SchoolGroup;
import ca.sheridancollege.fangyux.beans.User;
import ca.sheridancollege.fangyux.repository.CartGroupRepository;
import ca.sheridancollege.fangyux.repository.EventRepository;
import ca.sheridancollege.fangyux.repository.GroupRepository;
import ca.sheridancollege.fangyux.service.CartEventGroupServices;
import ca.sheridancollege.fangyux.service.CartEventServices;
import ca.sheridancollege.fangyux.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ca.sheridancollege.fangyux.beans.Event;
import ca.sheridancollege.fangyux.repository.UserRepository;
import ca.sheridancollege.fangyux.service.EventService;


@Controller
@AllArgsConstructor
public class EventController {

	@Autowired
	private CartEventServices cartEventServices;
	@Autowired
	private CartEventGroupServices cartEventGroupServices;
	@Autowired
	private EventService eventService;
	@Autowired
	private GroupService groupService;
	private UserRepository userRepo;

	@Autowired
	private EventRepository eventRepo;
	@Autowired
	private GroupRepository groupRepo;

	@Autowired
	private CartGroupRepository cartGroupRepo;
	
	@GetMapping("/events")
	public String home(Model model) {
		//-------------------------------------------Authentication
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepo.findByEmail(auth.getName());

		Long userId=user.getId();

		model.addAttribute("user", user.getFirstName());


		return findPaginated(userId,model);
	}
	@GetMapping("/addGroupEventToCart/{groupId}/{eventId}")
	public String addGroupEventToCart(@PathVariable("groupId") Long groupId,@PathVariable("eventId") Long eventId, @AuthenticationPrincipal Authentication authentication) throws AddressException, MessagingException, IOException{
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			//get event information
			Event event = eventService.getEventById(eventId);
			System.out.println(groupId);
			SchoolGroup group = groupService.getGroupById(groupId);
			String content;
			content =
					"<h3>You have joined successfully event !!!</h3>" +
							"<br>Event name: " + event.getEventName() +
							"<br>Description: " + event.getDescription() +
							"<br>Host name: " + event.getHostName() +
							"<br>Type of Event: " + event.getTypeOfEvent() +
							"<br>Category: " + event.getCategory() +
							"<br>Location: " + event.getLocation() +
							"<br>Date: " +event.getDate() +
							"<br>Time: " + event.getTime();
			//send email to user with event information
			sendmail(auth.getName(), content);

			User user = userRepo.findByEmail(auth.getName());
			Integer updatedQuantity = cartEventGroupServices.addGroupEvent(groupId, eventId, user);
			return "redirect:/events";
		} catch(UsernameNotFoundException ex){
			System.out.println("You must login to add this event to cart");
			return "You must login to add this event to cart";
		}
	}
	@GetMapping("/addEventToCart/{eventId}")
	public String addEventToCart(@PathVariable("eventId") Long eventId, @AuthenticationPrincipal Authentication authentication) throws AddressException, MessagingException, IOException{
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			//get event information
			Event event = eventService.getEventById(eventId);
			String content;
			content =
					"<h3>You have joined successfully event !!!</h3>" +
					"<br>Event name: " + event.getEventName() +
					"<br>Description: " + event.getDescription() +
					"<br>Host name: " + event.getHostName() +
					"<br>Type of Event: " + event.getTypeOfEvent() +
					"<br>Category: " + event.getCategory() +
					"<br>Location: " + event.getLocation() +
					"<br>Date: " +event.getDate() +
					"<br>Time: " + event.getTime();
			//send email to user with event information
			sendmail(auth.getName(), content);

			User user = userRepo.findByEmail(auth.getName());
			Integer updatedQuantity = cartEventServices.addEvent(eventId, user);
			return "redirect:/events";
		} catch(UsernameNotFoundException ex){
			System.out.println("You must login to add this event to cart");
			return "You must login to add this event to cart";
		}
	}
	//display list of event
	@GetMapping("/addEvent/{groupId}")
	public String showNewEventForm(Model model, @PathVariable("groupId")Long groupId) {

		List<String> typeOfEventList = Arrays.asList("Online","In Person (Indoor)","In Person (Outdoor)");
		List<String> categoriesList = Arrays.asList("Food", "Music", "Health", "Fashion", "Business", "Sport", "Education",
				"Art", "Party", "Entertainment", "Others");

		model.addAttribute("typeOfEventList",typeOfEventList);
		model.addAttribute("categoriesList",categoriesList);
		model.addAttribute("groupId",groupId);

		Event event = new Event();
		model.addAttribute("event", event);
		return "newEvent.html";
	}

	@PostMapping("/addEvent/{groupId}")
	public String addEvent(@PathVariable (value = "groupId") String groupId, @ModelAttribute("event") Event event, @RequestParam(value = "image", required = true)MultipartFile file, @AuthenticationPrincipal Authentication authentication) throws MessagingException, IOException {
//		String id = String.valueOf(UUID.randomUUID());

		Blob blob = null;
		byte[] blobAsBytes=null;
		try {
			blob = new SerialBlob(file.getBytes());

			int blobLength = (int) blob.length();
			blobAsBytes = blob.getBytes(1, blobLength);
		} catch (Exception e) {
			e.printStackTrace();
		}

		event.setEventImage(blobAsBytes);

		Long groupIdLong=Long.parseLong(groupId);
		event.setGroupId(groupIdLong);
		event.setRemindered("false");
		eventRepo.save(event);

		String content;
		content =
				"<h3>New Event just created in Group: " + groupRepo.getGroupNameByGroupId(groupIdLong) +
				"</h3><br>Event name: " + event.getEventName() +
				"<br>Description: " + event.getDescription() +
				"<br>Host name: " + event.getHostName() +
				"<br>Type of Event: " + event.getTypeOfEvent() +
				"<br>Category: " + event.getCategory() +
				"<br>Location: " + event.getLocation() +
				"<br>Date: " +event.getDate() +
				"<br>Time: " + event.getTime();
		;
		//send email to user with event information
		for(int i = 0; i < cartGroupRepo.selectAllUserIdByGroupId(groupIdLong).size();i++)
		{
			sendmail(userRepo.getUserEmailByUserId(cartGroupRepo.selectAllUserIdByGroupId(groupIdLong).get(i)), content);
		}


		return "viewGroups.html";
	}
	
	@GetMapping("/showFormForUpdate/{id}")
	public String showFormForUpdate(@PathVariable (value = "id") long id, Model model) {
		//get event from service
		Event event = eventService.getEventById(id);
		
		//set event as a model
		model.addAttribute("event",event);
		return "editEvent";
	}
	
	@GetMapping("/deleteEvent/{id}")
	public String deleteEvent(@PathVariable (value="id") long id) {
		//delete event from service
		//this.cartEventServices.
		this.eventService.deleteEventById(id);
		return "redirect:/";
	}
	
	@GetMapping("/page/{pageNo}")
	public String findPaginated(Long userId, Model model) {
		model.addAttribute("userId",userId);
		
		return "viewEvents.html";
	}

	@RequestMapping("/myEvent/paginated")
	@ResponseBody
	public ResultEntity<List<Event>> getEventPaginated(
			@RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "3")Integer pageSize,
			@RequestParam("userId")String uId//,
			//@RequestParam("groupId")String gId
	) throws IOException {
		//Get userId
		Long userId=Long.parseLong(uId);
		//Long groupId=Long.parseLong(gId);

		//Get Events IDs by userId from cart_events table
		//List<Long> eventIDs=eventRepo.getEventsIDsByUserId(userId);
		List<Long> eventIDs=eventRepo.getEventIdByUserIdAndGroupId(userId);

		Page<Event> eventPage = eventService.getEventsByIDs(pageNum, pageSize, eventIDs);

		List<Event> events = new ArrayList<>();

		eventPage.forEach(entity -> events.add(entity));

		for (int i = 0; i < events.size(); i++) {
			events.set(i, ImageOperation.attatchBase64ToEvent(events.get(i)));
		}

		Long totalRecords=eventPage.getTotalElements();

		return ResultEntity.successWithtDataAndTotalRecoreds(events, totalRecords);
	}

	@PostMapping("/searchEvents")
	@ResponseBody
	public ResultEntity<List<Event>> getEventsByName(
			@RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "3")Integer pageSize,
			@RequestParam("eventName")String eventName
	) throws IOException {

		Page<Event> eventPage = eventService.getEventsByName(pageNum, pageSize, eventName);

		List<Event> events = new ArrayList<>();

		eventPage.forEach(entity -> events.add(entity));

		for (int i = 0; i < events.size(); i++) {
			events.set(i, ImageOperation.attatchBase64ToEvent(events.get(i)));
		}

		Long totalRecords=eventPage.getTotalElements();

		return ResultEntity.successWithtDataAndTotalRecoreds(events, totalRecords);
	}

	private void sendmail(String email, String content) throws AddressException, MessagingException, IOException {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("applicationgroupnow@gmail.com", "xzmaodwcvgqoctpt");
			}
		});

		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("applicationgroupnow@gmail.com", false));

		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
		msg.setSubject("Event information email");
		msg.setContent(content, "text/html");
		msg.setSentDate(new Date());

//		MimeBodyPart messageBodyPart = new MimeBodyPart();
//		messageBodyPart.setContent(content, "text/html");
//
//		Multipart multipart = new MimeMultipart();
//		multipart.addBodyPart(messageBodyPart);
//		MimeBodyPart attachPart = new MimeBodyPart();

		Transport.send(msg);
	}
}
