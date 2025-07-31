package backend.hiteen.scrap.exception;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class ScrapNotOwnerException extends BusinessException {
    public ScrapNotOwnerException() {
        super(ErrorCode.NO_PERMISSION_TO_SCRAP_BOARD);
    }
}
