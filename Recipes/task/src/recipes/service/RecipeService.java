package recipes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import recipes.model.Recipe;
import recipes.repository.RecipeRepository;

import java.util.Map;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    // Method to add a new recipe to the database
    public ResponseEntity<?> addRecipeNew(Recipe recipe) {
        // Save the recipe to the database
        Recipe savedRecipe = recipeRepository.save(recipe);
        // Respond with the saved recipe's id
        return ResponseEntity.ok(Map.of("id", savedRecipe.getId()));
    }

    // Method to get a recipe by its id from the database
    public ResponseEntity<?> getRecipeWithId(Long id) {
        if (recipeRepository.existsById(id)) {
            // If the recipe with the specified id exists, retrieve it
            Optional<Recipe> byId = recipeRepository.findById(id);
            Recipe recipe = byId.orElse(null);
            // Respond with the retrieved recipe
            return ResponseEntity.ok(recipe);
        }
        // If the recipe with the specified id does not exist, respond with a 404 status code
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Method to delete a recipe by its id from the database
    public ResponseEntity<?> deleteRecipeById(Long id) {
        if (recipeRepository.existsById(id)) {
            // If the recipe with the specified id exists, delete it from the database
            recipeRepository.deleteById(id);
            // Respond with a 204 (No Content) status code
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        // If the recipe with the specified id does not exist, respond with a 404 status code
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
