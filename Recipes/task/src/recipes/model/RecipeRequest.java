package recipes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRequest {
    private String name;
    private String description;
    private List<String> ingredients;
    private List<String> directions;
}
