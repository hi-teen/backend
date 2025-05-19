package backend.hiteen.externalapi.meal.exception;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class MealFetchFailedException extends BusinessException {
    public MealFetchFailedException() {
        super(ErrorCode.MEAL_FETCH_FAILED);
    }
}
