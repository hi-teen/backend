package backend.hiteen.message.exception;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class MessageTargetNotSpecifiedException extends BusinessException {
    public MessageTargetNotSpecifiedException() {
        super(ErrorCode.MESSAGE_TARGET_NOT_SPECIFIED);
    }
}
