package backend.hiteen.externalapi.meal.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class MealDto {
    private final List<String> menus;
    private final String calories;
    private final String nutrients;

    public MealDto(List<String> menus, String calories, String nutrients) {
        this.menus = menus;
        this.calories = calories;
        this.nutrients = nutrients;
    }
}
