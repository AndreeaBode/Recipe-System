package sd.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sd.entities.ExtractedRecipe;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExtractedRecipeRepository extends JpaRepository<ExtractedRecipe, Integer> {

    List<ExtractedRecipe> findAll();

    @EntityGraph(attributePaths = {"ingredients", "steps"})
    Optional<ExtractedRecipe> findById(Integer id);
}
