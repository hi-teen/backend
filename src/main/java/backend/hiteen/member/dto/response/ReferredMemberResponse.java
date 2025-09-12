package backend.hiteen.member.dto.response;

import backend.hiteen.member.entity.Member;
import lombok.Getter;

@Getter
public class ReferredMemberResponse {
    private final String name;

    public ReferredMemberResponse(String name) {
        this.name = name;
    }

    public static ReferredMemberResponse from(Member m) {
        return new ReferredMemberResponse(m.getName());
    }
}
