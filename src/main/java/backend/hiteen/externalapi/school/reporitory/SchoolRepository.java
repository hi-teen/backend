package backend.hiteen.externalapi.school.reporitory;

import backend.hiteen.externalapi.school.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface SchoolRepository extends JpaRepository<School, Long> {

    boolean existsBySchoolCode(String schoolCode);
    List<School> findBySchoolNameContainingAndKind(String schoolName, String kind);
}
