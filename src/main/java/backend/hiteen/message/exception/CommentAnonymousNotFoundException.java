package backend.hiteen.message.exception;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class CommentAnonymousNotFoundException extends BusinessException {
    public CommentAnonymousNotFoundException() {
        super(ErrorCode.COMMENT_ANONYMOUS_NOT_FOUND);
    }
}
