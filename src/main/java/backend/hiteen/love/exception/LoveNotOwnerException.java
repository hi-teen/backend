package backend.hiteen.love.exception;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class LoveNotOwnerException extends BusinessException {
    public LoveNotOwnerException() {
        super(ErrorCode.NO_PERMISSION_TO_LOVE_BOARD);
    }
}
