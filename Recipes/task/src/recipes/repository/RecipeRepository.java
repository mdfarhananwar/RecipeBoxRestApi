package recipes.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import recipes.model.Recipe;

import javax.validation.constraints.NotEmpty;
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

    boolean existsByCategoryIgnoreCase(@NotEmpty String category);

    List<Recipe> findByCategoryIgnoreCase(@NotEmpty String category, Sort sort);
    List<Recipe> findByNameIgnoreCaseContainsOrderByDateDesc(String name);

    boolean existsByNameIgnoreCase(@NotEmpty String name);
    List<Recipe> findByNameIgnoreCase(@NotEmpty String name, Sort sort);
}
