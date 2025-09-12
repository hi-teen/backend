package backend.hiteen.member.dto.response;

import lombok.Getter;

import java.util.List;
@Getter
public class ReferralListResponse {
    private int count;
    private final List<ReferredMemberResponse> members;

    public ReferralListResponse(int count, List<ReferredMemberResponse> members) {
        this.count = count;
        this.members = members;
    }
}
