package recipes.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipes.model.Recipe;
import recipes.model.RecipeRequest;

import java.util.HashMap;
import java.util.Map;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@RestController
public class RecipeController {

    private Recipe latest;
    Map<String, Long> status = new HashMap<>();

    @PostMapping("/api/recipe")
    public ResponseEntity<Void> addRecipe(@RequestBody Recipe recipe) {
        latest = recipe;
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/recipe")
    public ResponseEntity<Recipe> getRecipe() {
        return ResponseEntity.ok(latest);
    }

    @PostMapping("POST /api/recipe/new")
    public ResponseEntity<?> addNewRecipe(@RequestBody RecipeRequest recipe) {
        latest = new Recipe(latest.getId(), recipe);

        status.put("status", latest.getId());
        return ResponseEntity.ok(status);
    }

    @GetMapping("GET /api/recipe/{id}")
    public ResponseEntity<?> getRecipeWithId(@PathVariable Long id) {
        if (status.containsValue(id)) {
            return ResponseEntity.ok(latest);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }
}
