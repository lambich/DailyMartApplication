package lambich.dailyshop.repository;

import lambich.dailyshop.beans.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //public User findByUsername(String username);
    @Query(value="SELECT * FROM user WHERE username=:username",nativeQuery=true)
    User findByUsername(@Param("username")String username);
}
