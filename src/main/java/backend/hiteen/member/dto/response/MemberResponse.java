package backend.hiteen.member.dto.response;

import backend.hiteen.externalapi.school.dto.SchoolResponse;
import backend.hiteen.member.entity.Member;
import lombok.Getter;

@Getter
public class MemberResponse {

    private final String email;
    private final String name;
    private final String nickname;
    private final int gradeNumber;
    private final int classNumber;
    private final SchoolResponse school;

    public MemberResponse(Member member){
        this.email=member.getEmail();
        this.name=member.getName();
        this.nickname=member.getNickname();
        this.gradeNumber=member.getGradeNumber();
        this.classNumber=member.getClassNumber();
        this.school = new SchoolResponse(member.getSchool());
    }

}
