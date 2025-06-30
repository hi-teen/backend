package backend.hiteen.externalapi.school.dto;

import backend.hiteen.externalapi.school.entity.School;
import lombok.Getter;

@Getter
public class SchoolResponse {
    private final Long id;
    private final String schoolName;
    private final String schoolCode;
    private final String eduOfficeCode;
    private final String eduOfficeName;
    private final String schoolUrl;


    public SchoolResponse(School school) {
        this.id = school.getId();
        this.schoolName = school.getSchoolName();
        this.schoolCode = school.getSchoolCode();
        this.eduOfficeCode = school.getEduOfficeCode();
        this.eduOfficeName = school.getEduOfficeName();
        this.schoolUrl = school.getSchoolUrl();
    }

}
