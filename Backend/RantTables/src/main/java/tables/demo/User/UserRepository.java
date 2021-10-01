package tables.demo.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    User findById(int id);
    void deleteById(int id);

    @Query("SELECT t FROM User t WHERE t.email = ?1")
    User findByEmail(String email);
    /*
    * Method for determining whether or not email already exists in
    * the database.
    */
    boolean existsUserByEmail(String email);
}
