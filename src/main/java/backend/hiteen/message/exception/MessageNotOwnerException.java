package backend.hiteen.message.exception;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class MessageNotOwnerException extends BusinessException {
    public MessageNotOwnerException() {
        super(ErrorCode.MESSAGE_PERMISSION_DENIED);
    }
}
