package backend.hiteen.love.exception;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class LoveAlreadyExistsException extends BusinessException {
    public LoveAlreadyExistsException() {super(ErrorCode.LOVE_ALREADY_EXISTS);}
}
