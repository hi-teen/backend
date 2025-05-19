package backend.hiteen.externalapi.timetable.exception;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class TimeTableFetchFailedException extends BusinessException {
    public TimeTableFetchFailedException() {
        super(ErrorCode.TIMETABLE_FETCH_FAILED);
    }
}
