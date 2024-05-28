package sd.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
@Entity
@Table(name = "recipe_under_review")
public class RecipeUnderReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String image;

    private String status;
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RecipeUnderReviewIngredient> ingredients = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RecipeUnderReviewStep> instructions = new ArrayList<>();

    public void setIngredients(List<RecipeUnderReviewIngredient> ingredients) {
        this.ingredients = (List<RecipeUnderReviewIngredient>) ingredients;
    }
}
