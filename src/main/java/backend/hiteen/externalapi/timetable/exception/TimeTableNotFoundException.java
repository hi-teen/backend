package backend.hiteen.externalapi.timetable.exception;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class TimeTableNotFoundException extends BusinessException {
    public TimeTableNotFoundException() {
        super(ErrorCode.TIMETABLE_NOT_FOUND);
    }
}
