package sd.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
@Entity
@Table(name = "added_recipes")
public class AddedRecipe {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @Column(nullable = false)
        private String title;

        @Column(nullable = false)
        private String image;

        @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<AddedIngredient> ingredients = new ArrayList<>();

        @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<AddedStep> steps = new ArrayList<>();

        public void setIngredients(List<AddedIngredient> ingredients) {
            this.ingredients = (List<AddedIngredient>) ingredients;
        }
}
