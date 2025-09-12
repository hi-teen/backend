package backend.hiteen.member.exception.member;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class MemberUnauthorizedException extends BusinessException {
    public MemberUnauthorizedException() {super(ErrorCode.MEMBER_UNAUTHORIZED);}
}
