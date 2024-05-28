package sd.dtos;

import sd.entities.ExtractedRecipe;
import sd.entities.Ingredient;
import sd.entities.Step;

import java.util.List;

public class ExtractedDTO {
    private int id;
    private String title;
    private String image;
    private List<Ingredient> ingredients;
    private List<Step> instructions;

    public ExtractedDTO(ExtractedRecipe extractedRecipe) {
        this.id = extractedRecipe.getId();
        this.title = extractedRecipe.getTitle();
        this.image = extractedRecipe.getImage();
        this.ingredients = extractedRecipe.getIngredients();
        this.instructions = extractedRecipe.getSteps();
    }

    public ExtractedDTO() {

    }

    public ExtractedDTO(int id, String title, String image, List<Ingredient> ingredients, List<Step> instructions) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<Step> instructions) {
        this.instructions = instructions;
    }

    @Override
    public String toString() {
        return "ExtractedDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

}
