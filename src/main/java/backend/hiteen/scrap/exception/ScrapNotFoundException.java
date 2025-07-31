package backend.hiteen.scrap.exception;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class ScrapNotFoundException extends BusinessException {
    public ScrapNotFoundException() {
        super(ErrorCode.SCRAP_NOT_FOUND);
    }
}
