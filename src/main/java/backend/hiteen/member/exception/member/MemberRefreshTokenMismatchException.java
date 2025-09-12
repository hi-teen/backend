package backend.hiteen.member.exception.member;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class MemberRefreshTokenMismatchException extends BusinessException {
    public MemberRefreshTokenMismatchException() {super(ErrorCode.MEMBER_REFRESH_TOKEN_MISMATCH);}
}
