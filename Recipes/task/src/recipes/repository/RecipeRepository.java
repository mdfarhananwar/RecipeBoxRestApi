package recipes.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import recipes.model.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Override
    @NotNull
    List<Recipe> findAll();

    @Override
    @NotNull
    Optional<Recipe> findById(@NotNull Long id);

    @Override
    boolean existsById(@NotNull Long id);
}
