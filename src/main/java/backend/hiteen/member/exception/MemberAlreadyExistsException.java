package backend.hiteen.member.exception;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class MemberAlreadyExistsException extends BusinessException {
    public MemberAlreadyExistsException() {super(ErrorCode.MEMBER_ALREADY_EXISTS);}
}
