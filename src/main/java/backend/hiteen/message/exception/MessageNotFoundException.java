package backend.hiteen.message.exception;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class MessageNotFoundException extends BusinessException {
    public MessageNotFoundException() {
        super(ErrorCode.MESSAGE_NOT_FOUND);
    }
}
