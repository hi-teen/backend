package backend.hiteen.love.exception;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class LoveNotFoundException extends BusinessException {
    public LoveNotFoundException() {super(ErrorCode.LOVE_NOT_FOUND);}
}
