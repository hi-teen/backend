package backend.hiteen.board.exception;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class BoardCreationFailedException extends BusinessException {
    public BoardCreationFailedException() {super(ErrorCode.BOARD_NOT_FOUND);}
}
