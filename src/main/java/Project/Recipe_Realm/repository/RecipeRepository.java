package Project.Recipe_Realm.repository;

import Project.Recipe_Realm.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Override
    Recipe getReferenceById(Long aLong);

    Optional<Recipe> findById(Long id);

    @Query(value = "SELECT * FROM recipes WHERE " +
            "recipes.title LIKE %:title% OR " +
            "recipes.description LIKE %:description% OR " +
            "recipes.category LIKE %:category%", nativeQuery = true)
    List<Recipe> findByTitleOrDescriptionOrCategory(String title, String description, String category);

}
