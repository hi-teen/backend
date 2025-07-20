package backend.hiteen.comment.exception;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class CommentNotOwnerException extends BusinessException {
    public CommentNotOwnerException() {
        super(ErrorCode.COMMENT_PERMISSION_DENIED);
    }
}
