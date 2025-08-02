package backend.hiteen.scrap.exception;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

import java.nio.Buffer;

public class ScrapAlreadyExistsException extends BusinessException {
    public ScrapAlreadyExistsException() {super(ErrorCode.SCRAP_ALREADY_EXISTS);}
}
