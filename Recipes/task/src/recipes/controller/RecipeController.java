package recipes.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipes.model.Recipe;
import recipes.service.RecipeService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RecipeController {

    private final RecipeService recipeService;
    private Recipe latest = new Recipe();
    Map<Long, Recipe> recipes = new HashMap<>();

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/api/recipe")
    public ResponseEntity<Void> addRecipe(@RequestBody Recipe recipe) {
        latest = recipe;
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/recipe")
    public ResponseEntity<Recipe> getRecipe() {
        return ResponseEntity.ok(latest);
    }

    @PostMapping("/api/recipe/new")
    public ResponseEntity<?> addNewRecipe(@Valid @RequestBody Recipe recipe) {
        System.out.println("Enter NEW POST API");
//        latest = new Recipe(latest.getId(), recipe);
//
//        status.put("id", latest.getId());
//        recipes.put(latest.getId(), latest);
//        return ResponseEntity.ok(status);
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
}
