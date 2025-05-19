package backend.hiteen.message.exception;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class MessageRoomNotFoundException extends BusinessException {
    public MessageRoomNotFoundException() {
        super(ErrorCode.MESSAGE_ROOM_NOT_FOUND);
    }
}
