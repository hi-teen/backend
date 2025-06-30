package backend.hiteen.externalapi.school.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String schoolName;

    @Column(name = "school_code", unique = true, nullable = false)
    private String schoolCode;
    private String eduOfficeCode;
    private String eduOfficeName;
    private String kind;

    @Column(name = "school_url")
    private String schoolUrl;

}
