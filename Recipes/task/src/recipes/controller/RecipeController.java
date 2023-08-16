package recipes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import recipes.model.Recipe;
import recipes.model.User;
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

    //Method to add New Recipe
    @PostMapping("/api/recipe/new")
    public ResponseEntity<?> addNewRecipe(@Valid @RequestBody Recipe recipe, @AuthenticationPrincipal UserDetails userDetails) {
        return recipeService.addRecipeNew(recipe, userDetails);
    }

    //Method to get specific Recipe having unique id
    @GetMapping("/api/recipe/{id}")
    public ResponseEntity<?> getRecipeWithId(@PathVariable Long id) {
        return recipeService.getRecipeWithId(id);
    }

    //Method to Delete specific Recipe having unique id
    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<?> deleteRecipeById(@PathVariable Long id,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        return recipeService.deleteRecipeById(id, userDetails);
    }

    //Search List of Recipes By Name Or Category
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

    //Method to update existing Recipe
    @PutMapping("/api/recipe/{id}")
    public ResponseEntity<?> updateRecipe(@Valid @RequestBody Recipe recipe, @PathVariable Long id,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        return recipeService.updateRecipe(recipe, id, userDetails);
    }

    //Method to add new user
    @PostMapping("/api/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        return recipeService.registerUser(user);
    }
}
