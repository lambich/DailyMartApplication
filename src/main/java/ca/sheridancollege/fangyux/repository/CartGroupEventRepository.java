package ca.sheridancollege.fangyux.repository;

import ca.sheridancollege.fangyux.beans.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartGroupEventRepository extends JpaRepository<CartEventGroup, Long> {

    public CartEventGroup findByUserAndEventAndGroup(User user, Event event, SchoolGroup group);
}
