package backend.hiteen.message.exception;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class CannotSendMessageToSelfException extends BusinessException {
    public CannotSendMessageToSelfException() {
        super(ErrorCode.CANNOT_SEND_MESSAGE_TO_SELF);
    }
}
