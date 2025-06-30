package backend.hiteen.externalapi.school.exception;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class SchoolNotFoundException extends BusinessException {
    public SchoolNotFoundException() {
        super(ErrorCode.SCHOOL_NOT_FOUND);
    }
}
