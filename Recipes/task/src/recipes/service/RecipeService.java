package recipes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import recipes.model.Recipe;
import recipes.model.User;
import recipes.repository.RecipeRepository;
import recipes.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    // Method to add a new recipe to the database
    public ResponseEntity<?> addRecipeNew(Recipe recipe, UserDetails userDetails) {

        String email = userDetails.getUsername();
        // Save the recipe to the database
        User user = userRepository.findByEmailIgnoreCase(email);
        recipe.setDate(String.valueOf(LocalDateTime.now()));
        recipe.setUser(user);

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
    public ResponseEntity<?> deleteRecipeById(Long id, UserDetails userDetails) {

        String email = userDetails.getUsername();
        User user = userRepository.findByEmailIgnoreCase(email);
        if (recipeRepository.existsById(id)) {
            Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
            Recipe existingRecipe = optionalRecipe.orElseThrow();
            // Check if the authenticated user is the author of the existing recipe
            if (!existingRecipe.getUser().getEmail().equals(userDetails.getUsername())) {
                // Return a Forbidden status, as the user is not the author of the recipe
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            // If the recipe with the specified id exists, delete it from the database
            recipeRepository.deleteById(id);
            // Respond with a 204 (No Content) status code
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            // If the recipe with the specified id does not exist, respond with a 404 status code
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    // Method to search Recipes By Category
    public List<Recipe> searchRecipesByCategory(String category) {
        //Check if the Category of repository is valid
        if (recipeRepository.existsByCategoryIgnoreCase(category)) {
            Sort sort = Sort.by(Sort.Direction.DESC, "date");
            // Return List of Recipes sorted by Date in descending order
            return recipeRepository.findByCategoryIgnoreCase(category, sort);
        } else {
            // Return Empty list, as Category is not valid
            return Collections.emptyList();
        }
    }

    // Method to search Recipes By Name
    public List<Recipe> searchRecipesByName(String name) {
        //Check if the name of repository is valid
        if (recipeRepository.existsByNameIgnoreCase(name)) {
            // Return List of Recipes sorted by Date in descending order
            return recipeRepository.findByNameIgnoreCaseContainsOrderByDateDesc(name);
        } else {
            // Return Empty list, as name is not valid
            return Collections.emptyList();
        }

    }

    public ResponseEntity<?> updateRecipe(Recipe recipe, Long id, UserDetails userDetails) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        Recipe existingRecipe = optionalRecipe.orElse(null);

        if (existingRecipe != null) {
            System.out.println(existingRecipe);
            // Check if the authenticated user is the author of the existing recipe
            if (existingRecipe.getUser().getEmail().equals(userDetails.getUsername())) {
                // Proceed with the update
                existingRecipe.setDate(String.valueOf(LocalDateTime.now()));
                existingRecipe.setCategory(recipe.getCategory());
                existingRecipe.setName(recipe.getName());
                existingRecipe.setDescription(recipe.getDescription());
                existingRecipe.setDirections(recipe.getDirections());
                existingRecipe.setIngredients(recipe.getIngredients());
                Recipe save = recipeRepository.save(existingRecipe);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                // Return a Forbidden status, as the user is not the author of the recipe
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            // Return a Not Found status, as the recipe with the given id doesn't exist
            return ResponseEntity.notFound().build();
        }
    }

    // Method to add a new user to the database
    public ResponseEntity<?> registerUser(User user) {
        String email = user.getEmail();
        // Check if user is already Registered
        if (userRepository.existsByEmailIgnoreCase(email) || !isEmailValid(email)) {
            // Return a Bad Request status, as the user is already registered
            return ResponseEntity.badRequest().build();
        }
        //Encode the password, before saving user to the database
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        user = userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    //Check if the email format is valid
    public boolean isEmailValid(String email) {
        return email.endsWith(".com") && email.contains("@");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
