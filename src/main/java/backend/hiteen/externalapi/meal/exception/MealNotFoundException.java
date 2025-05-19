package backend.hiteen.externalapi.meal.exception;

import backend.hiteen.common.response.ErrorCode;
import backend.hiteen.global.exception.BusinessException;

public class MealNotFoundException extends BusinessException {
    public MealNotFoundException() {
        super(ErrorCode.MEAL_NOT_FOUND);
    }
}
