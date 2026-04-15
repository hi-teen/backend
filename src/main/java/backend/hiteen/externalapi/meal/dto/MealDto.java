package backend.hiteen.externalapi.meal.dto;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class MealDto implements Serializable {
    private final List<String> menus;
    private final String calories;
    private final String nutrients;

    public MealDto(List<String> menus, String calories, String nutrients) {
        this.menus = menus;
        this.calories = calories;
        this.nutrients = nutrients;
    }
}
