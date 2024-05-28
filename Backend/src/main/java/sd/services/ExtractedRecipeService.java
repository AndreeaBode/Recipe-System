package sd.services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import sd.dtos.ExtractedDTO;
import sd.dtos.ExtractedRecipeDTO;
import sd.dtos.builders.ExtractedRecipeBuilder;
import sd.entities.ExtractedRecipe;
import sd.entities.Ingredient;
import sd.entities.Step;
import sd.repositories.ExtractedRecipeRepository;
import sd.repositories.IngredientRepository;
import sd.repositories.RecipeUnderReviewRepository;
import sd.repositories.StepRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExtractedRecipeService {


    @Autowired
    private final ExtractedRecipeRepository extractedRecipeRepository;
    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private NewsletterService newsletterService;

    @Autowired
    private StepRepository stepRepository;

   // private final String spoonacularApiKey = "d79ab01d2eff47d081feed07a650ff00";
    private final String spoonacularApiKey = "da3a95ad9e794dc3b5d43cf1f0f8cf60";
    public ExtractedRecipeService(ExtractedRecipeRepository extractedRecipeRepository) {
        this.extractedRecipeRepository = extractedRecipeRepository;
    }

    public ResponseEntity<String> extractRecipe(String url) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("https://api.spoonacular.com/recipes/extract")
                    .queryParam("url", url)
                    .queryParam("forceExtraction", true)
                    .queryParam("analyze", true)
                    .queryParam("includeNutrition", true)
                    .queryParam("includeTaste", true)
                    .queryParam("apiKey", spoonacularApiKey);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(builder.toUriString(), String.class);

            String responseBody = responseEntity.getBody();
            JSONObject jsonObject = new JSONObject(responseBody);

            String title = jsonObject.getString("title");
            String image = jsonObject.getString("image");

            System.out.println(jsonObject);

            JSONArray extendedIngredientsArray = jsonObject.getJSONArray("extendedIngredients");

            List<String> originalIngredientsList = new ArrayList<>();

            for (int i = 0; i < extendedIngredientsArray.length(); i++) {
                JSONObject ingredientObject = extendedIngredientsArray.getJSONObject(i);
                String originalIngredient = ingredientObject.getString("original");
                originalIngredientsList.add(originalIngredient);
            }

            System.out.println(originalIngredientsList);

            //processExtractedRecipe(extractedRecipe);

            JSONArray analyzedInstructionsArray = jsonObject.getJSONArray("analyzedInstructions");
            List<String> instructionsList = new ArrayList<>();
            for (int i = 0; i < analyzedInstructionsArray.length(); i++) {
                JSONObject instructionObject = analyzedInstructionsArray.getJSONObject(i);
                JSONArray stepsArray = instructionObject.getJSONArray("steps");
                for (int j = 0; j < stepsArray.length(); j++) {
                    JSONObject stepObject = stepsArray.getJSONObject(j);
                    String stepInstruction = stepObject.getString("step");
                    instructionsList.add(stepInstruction);
                }
            }

            saveExtractedRecipe(title, image, originalIngredientsList, instructionsList);

            return responseEntity;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing the request.");
        }
    }

    public void saveExtractedRecipe(String title, String image, List<String> originalIngredientsList, List<String> instructionsList) {
        try {
            ExtractedRecipe extractedRecipe = new ExtractedRecipe();
            extractedRecipe.setTitle(title);
            extractedRecipe.setImage(image);

            extractedRecipe = extractedRecipeRepository.save(extractedRecipe);

            for (String originalIngredient : originalIngredientsList) {
                Ingredient ingredient = new Ingredient();
                ingredient.setIngredient(originalIngredient);
                ingredient.setRecipe(extractedRecipe);
                ingredientRepository.save(ingredient);
            }

            for (String instruction : instructionsList) {
                Step step = new Step();
                step.setInstruction(instruction);
                step.setRecipe(extractedRecipe);
                stepRepository.save(step);
            }

            extractedRecipeRepository.save(extractedRecipe);
            newsletterService.sendEmail(extractedRecipe.getTitle());

            extractedRecipe.setIngredients(ingredientRepository.findAllByRecipe(extractedRecipe));
            extractedRecipe.setSteps(stepRepository.findAllByRecipe(extractedRecipe));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving extracted recipe: " + e.getMessage());
        }
    }

    public ExtractedDTO getDetailDisghen(int id) {
        Optional<ExtractedRecipe> extractedRecipeOptional = extractedRecipeRepository.findById(id);
        if (extractedRecipeOptional.isPresent()) {
            ExtractedRecipe extractedRecipe = extractedRecipeOptional.get();
            extractedRecipe.getIngredients().size();
            extractedRecipe.getSteps().size();
            return new ExtractedDTO(extractedRecipe);
        } else {
            throw new RuntimeException("Recipe not found with id: " + id);
        }
    }


    public List<ExtractedRecipeDTO> getAllRecipes() {
        List<ExtractedRecipe> extractedRecipes = extractedRecipeRepository.findAll();
        return extractedRecipes.stream()
                .map(ExtractedRecipeBuilder::buildDTO)
                .collect(Collectors.toList());
    }
}
