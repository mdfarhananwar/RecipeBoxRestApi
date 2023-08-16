package recipes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recipes.model.Recipe;
import recipes.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmailIgnoreCase(String email);

    User findByEmailIgnoreCase(String email);

    boolean existsByIdAndRecipesContaining(Long userId, Recipe recipe);
}
