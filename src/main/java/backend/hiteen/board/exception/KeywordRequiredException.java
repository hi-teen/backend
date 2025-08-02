package backend.hiteen.board.exception;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class KeywordRequiredException extends BusinessException {
    public KeywordRequiredException () {super(ErrorCode.KEYWORD_REQUIRED);}
}
