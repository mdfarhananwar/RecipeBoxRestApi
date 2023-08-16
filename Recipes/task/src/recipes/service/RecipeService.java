package recipes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import recipes.model.Recipe;
import recipes.repository.RecipeRepository;
import java.time.LocalDateTime;

import java.util.*;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
//    LocalDateTime date = LocalDateTime.now();

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    // Method to add a new recipe to the database
    public ResponseEntity<?> addRecipeNew(Recipe recipe) {
        // Save the recipe to the database
        recipe.setDate(String.valueOf(LocalDateTime.now()));

        Recipe savedRecipe = recipeRepository.save(recipe);
        System.out.println(savedRecipe);
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

    public List<Recipe> searchRecipesByCategory(String category) {
        System.out.println("RECIPES BY CATEGORY");
        if (recipeRepository.existsByCategoryIgnoreCase(category)) {
            Sort sort = Sort.by(Sort.Direction.DESC, "date");
            return recipeRepository.findByCategoryIgnoreCase(category, sort);
        } else {
            return Collections.emptyList();
        }
    }

    public List<Recipe> searchRecipesByName(String name) {
        System.out.println("RECIPESBY NAME");
        if (recipeRepository.existsByNameIgnoreCase(name)) {
            //Sort sort = Sort.by(Sort.Direction.DESC, "date");
            return recipeRepository.findByNameIgnoreCaseContainsOrderByDateDesc(name);
        } else {
            return Collections.emptyList();
        }

    }

    public ResponseEntity<?> updateRecipe(Recipe recipe, Long id) {
        if (recipeRepository.existsById(id)) {
            Optional<Recipe> byId = recipeRepository.findById(id);
            Recipe checkRecipe = byId.orElseThrow();
            //System.out.println(recipe.getDate());
            checkRecipe.setDate(String.valueOf(LocalDateTime.now()));
            checkRecipe.setCategory(recipe.getCategory());
            checkRecipe.setName(recipe.getName());
            checkRecipe.setDescription(recipe.getDescription());
            checkRecipe.setDirections(recipe.getDirections());
            checkRecipe.setIngredients(recipe.getIngredients());
            Recipe save = recipeRepository.save(checkRecipe);
            System.out.println("Update recipe");
            System.out.println(save);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
