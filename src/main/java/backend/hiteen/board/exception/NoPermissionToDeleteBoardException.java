package backend.hiteen.board.exception;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class NoPermissionToDeleteBoardException extends BusinessException {
    public NoPermissionToDeleteBoardException() {super(ErrorCode.NO_PERMISSION_TO_DELETE_BOARD);}
}
