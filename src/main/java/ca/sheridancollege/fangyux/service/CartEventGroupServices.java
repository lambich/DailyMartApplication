package ca.sheridancollege.fangyux.service;

import ca.sheridancollege.fangyux.beans.*;
import ca.sheridancollege.fangyux.repository.CartEventRepository;
import ca.sheridancollege.fangyux.repository.CartGroupEventRepository;
import ca.sheridancollege.fangyux.repository.EventRepository;
import ca.sheridancollege.fangyux.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartEventGroupServices {
    @Autowired
    private CartGroupEventRepository cartGroupEventRepository;
    @Autowired
    private EventRepository eventrepository;
    @Autowired
    private GroupRepository grouprepository;

    public Integer addGroupEvent(Long groupId, Long eventId, User user){

        Event event = eventrepository.getById(eventId);
        SchoolGroup group = grouprepository.getById(groupId);

        CartEventGroup cartEventGroup = cartGroupEventRepository.findByUserAndEventAndGroup(user, event, group);

        if(cartEventGroup != null) {
            System.out.println("No cart event group");
        } else{
            cartEventGroup = new CartEventGroup();
            cartEventGroup.setUser(user);
            cartEventGroup.setEvent(event);
            cartEventGroup.setGroup(group);
        }

        cartGroupEventRepository.save(cartEventGroup);

        return 0;

    }
}
