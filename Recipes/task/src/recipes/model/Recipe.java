package recipes.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor

public class Recipe {
    private static Long nextId = 1L;
    private Long id;
    private String name;
    private String description;
    private List<String> ingredients;
    private List<String> directions;

    public Recipe(Long id, String name, String description, List<String> ingredients, List<String> directions) {
        this.id = nextId++;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.directions = directions;
    }
    public Recipe(Long id,RecipeRequest recipeRequest) {
        this.id = nextId++;
        this.name = recipeRequest.getName();
        this.description = recipeRequest.getDescription();
        this.ingredients = recipeRequest.getIngredients();
        this.directions = recipeRequest.getDirections();
    }
}
