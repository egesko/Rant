package tables.demo.Rant;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RantRepository extends JpaRepository<Rant, Long> {
    Rant findById(int id);
}

