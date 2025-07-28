package backend.hiteen.message.exception;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class MessageRoomNotOwnerException extends BusinessException {
    public MessageRoomNotOwnerException() {
        super(ErrorCode.MESSAGE_ROOM_PERMISSION_DENIED);
    }
}
