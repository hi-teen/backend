package backend.hiteen.message.exception;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class InvalidMessageTargetException extends BusinessException {
    public InvalidMessageTargetException() {
        super(ErrorCode.INVALID_MESSAGE_TARGET);
    }
}
