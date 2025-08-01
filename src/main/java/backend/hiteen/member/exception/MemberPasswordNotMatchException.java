package backend.hiteen.member.exception;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

import java.nio.Buffer;

public class MemberPasswordNotMatchException extends BusinessException {
    public MemberPasswordNotMatchException() {super(ErrorCode.MEMBER_PASSWORD_NOT_MATCH);}
}
