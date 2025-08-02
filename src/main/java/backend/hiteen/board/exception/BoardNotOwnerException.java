package backend.hiteen.board.exception;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class BoardNotOwnerException extends BusinessException {
    public BoardNotOwnerException() {
        super(ErrorCode.BOARD_PERMISSION_DENIED);
    }
}
