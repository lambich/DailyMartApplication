package ca.sheridancollege.fangyux.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import ca.sheridancollege.fangyux.Utils.ImageOperation;
import ca.sheridancollege.fangyux.Utils.ResultEntity;
import ca.sheridancollege.fangyux.beans.CartGroup;
import ca.sheridancollege.fangyux.beans.Event;
import ca.sheridancollege.fangyux.beans.SchoolGroup;
import ca.sheridancollege.fangyux.beans.User;
import ca.sheridancollege.fangyux.repository.CartGroupRepository;
import ca.sheridancollege.fangyux.repository.EventRepository;
import ca.sheridancollege.fangyux.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ca.sheridancollege.fangyux.repository.GroupRepository;
import ca.sheridancollege.fangyux.repository.UserRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class GroupController {

	@Autowired
	private CartGroupServices cartGroupServices;
	@Autowired
	private UserService userService;
	@Autowired
	private GroupService groupService;

	private GroupRepository groupRepo;

	private CartGroupRepository cartgroupRepo;

	private UserRepository userRepo;

	//private User user;
	@Autowired
	private EventService eventService;


	@GetMapping("/goTrackGroups/{id}")
	public String goTrackGroup(@PathVariable (value = "id") Long id, Model model) throws
			IOException {
		Long hostId = groupRepo.getUserIdByGroupId(id);
		User host = userService.getUserById(hostId);
		host = ImageOperation.attatchBase64ToUser(host);

		SchoolGroup group = groupService.getGroupById(id);

		List<User> users = new ArrayList<>();
		group= ImageOperation.attatchBase64ToGroup(group);

		for(int i = 0; i < cartgroupRepo.selectAllUserIdByGroupId(id).size();i++)
		{
			User user = new User();
			user = userRepo.getUserByUserId(cartgroupRepo.selectAllUserIdByGroupId(id).get(i));
			user=ImageOperation.attatchBase64ToUser(user);
			users.add(user);
		}

		//set group as a model
		model.addAttribute("groups",group);
		model.addAttribute("users",users);
		model.addAttribute("hosts",host);
		return "trackGroups.html";
	}
	@GetMapping("/goTrackMyGroups/{id}")
	public String goTrackMyGroup(@PathVariable (value = "id") Long id, Model model, @AuthenticationPrincipal Authentication authentication) throws
			IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User host = userRepo.findByEmail(auth.getName());
		host = ImageOperation.attatchBase64ToUser(host);

		//Get Group Info
		SchoolGroup group = groupService.getGroupById(id);

		List<User> users = new ArrayList<>();
		group= ImageOperation.attatchBase64ToGroup(group);

		for(int i = 0; i < cartgroupRepo.selectAllUserIdByGroupId(id).size();i++)
		{
			User user = new User();
			user = userRepo.getUserByUserId(cartgroupRepo.selectAllUserIdByGroupId(id).get(i));
			user=ImageOperation.attatchBase64ToUser(user);
			users.add(user);
		}

		//set group as a model
		model.addAttribute("groups",group);
		model.addAttribute("users",users);
		model.addAttribute("hosts",host);

		return "trackMyGroups.html";
	}

	@RequestMapping("/eventOfGroup/paginated")
	@ResponseBody
	public ResultEntity<List<Event>> getEventsOfGroup(
			@RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "3")Integer pageSize,
			@RequestParam(value = "groupId")String groupId
	) throws IOException {
		//Get the list of events of the current group
		Long id=Long.parseLong(groupId);
		Page<Event> eventPage = eventService.getrEventsOfGroup(pageNum, pageSize, id);

		List<Event> events = new ArrayList<>();

		eventPage.forEach(entity -> events.add(entity));

		for (int i = 0; i < events.size(); i++) {
			events.set(i, ImageOperation.attatchBase64ToEvent(events.get(i)));
		}

		Long totalRecords=eventPage.getTotalElements();

		return ResultEntity.successWithtDataAndTotalRecoreds(events, totalRecords);
	}

	@GetMapping("/viewGroupFromCart")
	public String viewGroupFromCart(Model model, @AuthenticationPrincipal Authentication authentication) throws IOException{

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepo.findByEmail(auth.getName());


		model.addAttribute("originalUser", user);

			List<CartGroup> cartGroups = cartGroupServices.listCartGroups(user);
			model.addAttribute("user",user);
			model.addAttribute("cartGroups",cartGroups);
		return "viewGroups.html";
	}

	@GetMapping("/viewMyGroupFromCart")
	public String viewMyGroupFromCart(Model model, @AuthenticationPrincipal Authentication authentication) throws IOException{

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepo.findByEmail(auth.getName());


		model.addAttribute("originalUser", user);

		List<SchoolGroup> schoolGroups = groupService.listCartMyGroups(user.getId());
		model.addAttribute("user",user);
		model.addAttribute("schoolGroups",schoolGroups);
		return "viewMyGroups.html";
	}

	@GetMapping("/addGroupToCart/{groupId}")
	public String addEventToCart(@PathVariable("groupId") Long groupId, @AuthenticationPrincipal Authentication authentication){
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User user = userRepo.findByEmail(auth.getName());
			Integer updatedParticipants = cartGroupServices.addGroup(groupId, user);
			return "redirect:/viewGroupFromCart";
		} catch(UsernameNotFoundException ex){
			System.out.println("You must login to add this group to cart");
			return "You must login to add this group to cart";
		}
	}

	@GetMapping("/addGroup")
	public String loadAddGroup(Model model) {
		model.addAttribute("group", new SchoolGroup());
		model.addAttribute("users", userRepo.findAll());
		return "addGroup.html";
	}
	
	@Secured("ROLE_USER")
	@PostMapping("/addGroup")
	public String saveGroup(@ModelAttribute("group") SchoolGroup group, Model model, @RequestParam(value="image", required=true) MultipartFile 
			file, @AuthenticationPrincipal Authentication authentication) throws IOException{
		
		Blob blob = null;
	    byte[] blobAsBytes=null;
	    try {
	        blob = new SerialBlob(file.getBytes());
	        
	        int blobLength = (int) blob.length();  
	        blobAsBytes = blob.getBytes(1, blobLength);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    group.setPhoto(blobAsBytes);
	    
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepo.findByEmail(auth.getName());
	    group.setAdmins(userRepo.findByEmail(auth.getName()).getFirstName());
		group.setUserId(user.getId());
		groupRepo.save(group);
		return "redirect:/";
	}
	
	@GetMapping("/viewGroups")
	public String viewGroups(Model model) throws UnsupportedEncodingException {
		List<SchoolGroup> groups=groupRepo.findAll();
		
		for(int i=0;i<groups.size();i++) {
			byte[] encodeBase64 = Base64.getEncoder().encode(groups.get(i).getPhoto());
			String base64Encoded = new String(encodeBase64, "UTF-8");
			groups.get(i).setBase64Encoded(base64Encoded);
		}
		
		model.addAttribute("schoolgroups", groups);
		return "viewGroups.html";
	}
	
	@GetMapping("/editGroup/{id}")
	public String goEditGroup(@PathVariable Long id, Model model) {
		Optional<SchoolGroup> group = groupRepo.findById(id);
		model.addAttribute("users", userRepo.findAll());
		if(group.isPresent()) {
			SchoolGroup selectedGroup = group.get();
			model.addAttribute("group", selectedGroup);
			return "editGroup.html";
		} else {
			return "redirect:/viewGroups";
		}
	}
	
	@PostMapping("/modifyGroup")
	public String editGroup(@ModelAttribute SchoolGroup group, Model model) {
		groupRepo.save(group);
		return "redirect:/viewGroups";
	}
	
	@GetMapping("/deleteGroup/{id}")
	public String deleteGroup(@PathVariable Long id, Model model) {
		groupRepo.deleteById(id);
		return "redirect:/viewGroups";
	}
	@GetMapping("/leaveGroup/{groupId}")
	public String leaveGroup(@PathVariable (value = "groupId") long groupId, Model model,
							 @AuthenticationPrincipal Authentication authentication) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepo.findByEmail(auth.getName());

		cartgroupRepo.deleteByUserAndGroup(user.getId(),groupId);
		return "redirect:/viewGroups";
	}
	@GetMapping("/viewUsers")
	public String viewUsers(Model model) {
		model.addAttribute("users", userRepo.findAll());
		return "viewUsers.html";
	}
	
	@GetMapping("/sortById")
	public String sortById(Model model) {
		model.addAttribute("schoolgroups", groupRepo.findByOrderByIdAsc());
		return "viewGroups.html";
	}
	
	@GetMapping("/sortByName")
	public String sortByName(Model model) {
		model.addAttribute("schoolgroups", groupRepo.findByOrderByNameAsc());
		return "viewGroups.html";
	}
	
	@GetMapping("/sortByCategory")
	public String sortByCategory(Model model) {
		model.addAttribute("schoolgroups", groupRepo.findByOrderByCategoryAsc());
		return "viewGroups.html";
	}
	
	@GetMapping("/sortByStudy")
	public String sortByStudy(Model model) {
		model.addAttribute("schoolgroups", groupRepo.findByOrderByStudyAsc());
		return "viewGroups.html";
	}
	
	@GetMapping("/sortByDescription")
	public String sortByDescription(Model model) {
		model.addAttribute("schoolgroups", groupRepo.findByOrderByDescriptionAsc());
		return "viewGroups.html";
	}

	@PostMapping("/searchGroups")
	@ResponseBody
	public ResultEntity<List<SchoolGroup>> getGroupsByName(
			@RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "3")Integer pageSize,
			@RequestParam("groupName")String groupName
	) throws IOException {

		Page<SchoolGroup> groupPage = groupService.getGroupsByName(pageNum, pageSize, groupName);

		List<SchoolGroup> groups = new ArrayList<>();

		groupPage.forEach(entity -> groups.add(entity));

		for (int i = 0; i < groups.size(); i++) {
			groups.set(i, ImageOperation.attatchBase64ToGroup(groups.get(i)));
		}

		Long totalRecords=groupPage.getTotalElements();

		return ResultEntity.successWithtDataAndTotalRecoreds(groups, totalRecords);
	}

}
