package backend.hiteen.member.exception.member;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class MemberRefreshTokenNotFoundException extends BusinessException {
    public MemberRefreshTokenNotFoundException() {super(ErrorCode.MEMBER_REFRESH_TOKEN_NOT_FOUND);}
}
