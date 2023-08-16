package recipes.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipes.model.Recipe;
import recipes.service.RecipeService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/api/recipe/new")
    public ResponseEntity<?> addNewRecipe(@Valid @RequestBody Recipe recipe) {
        System.out.println("Enter NEW POST API");
        return recipeService.addRecipeNew(recipe);
    }

    @GetMapping("/api/recipe/{id}")
    public ResponseEntity<?> getRecipeWithId(@PathVariable Long id) {
        return recipeService.getRecipeWithId(id);
    }

    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<?> deleteRecipeById(@PathVariable Long id) {
        return recipeService.deleteRecipeById(id);
    }

    @GetMapping("/api/recipe/search")
    public ResponseEntity<List<Recipe>> searchRecipes(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name) {

        //@param are invalid
        if (category == null && name == null) {
            return ResponseEntity.badRequest().build();
        }
        List<Recipe> recipes;

        if (category != null) {
            // Search by category
            recipes = recipeService.searchRecipesByCategory(category);
        } else {
            // Search by name
            recipes = recipeService.searchRecipesByName(name);
        }
        // Recipe is Empty
        if (recipes.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(recipes);
    }

    @PutMapping("/api/recipe/{id}")
    public ResponseEntity<?> updateRecipe(@Valid @RequestBody Recipe recipe, @PathVariable Long id) {
        return recipeService.updateRecipe(recipe, id);
    }
}
