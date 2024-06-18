package sd.services;

import org.json.JSONArray;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import sd.dtos.AddedRecipeDTO;
import sd.dtos.ExtractedDTO;
import sd.dtos.ExtractedRecipeDTO;
import sd.dtos.builders.ExtractedRecipeBuilder;
import sd.entities.*;
import sd.entities.RecipeUnderReview;
import sd.repositories.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private ExtractedRecipeRepository extractedRecipeRepository;
    @Autowired
    private RecipeUnderReviewRepository recipeUnderReviewRepository;

    @Autowired
    private AddedRecipeRepository addedRecipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private StepRepository stepRepository;

    //private final String spoonacularApiKey = "d79ab01d2eff47d081feed07a650ff00";
   //private final String spoonacularApiKey = "da3a95ad9e794dc3b5d43cf1f0f8cf60";
   private final String spoonacularApiKey = "e0f0174758e74ee18fe3567c329272b5";
   //private final String spoonacularApiKey = "08a193a93a2c4a05b3f79421651dd8a7";
    private final String searchApiUrl = "https://api.spoonacular.com/recipes/complexSearch";
    private final String randomApiUrl = "https://api.spoonacular.com/recipes/random";
    private final String recipeInformationApiUrl = "https://api.spoonacular.com/recipes/";
    private final String ingredientsSearchApiUrl = "https://api.spoonacular.com/recipes/findByIngredients";
    private final String menuItemsSearchApiUrl = "https://api.spoonacular.com/food/menuItems/search";

    private final String nutritionWidgetUrl = "https://api.spoonacular.com/recipes/{id}/nutritionWidget.json";

    public ResponseEntity<String> searchFood(String query) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(searchApiUrl)
                    .queryParam("apiKey", spoonacularApiKey)
                    .queryParam("query", query)
                    .queryParam("number", 21);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(builder.toUriString(), String.class);

           // parseResponseAndSaveToDatabase(responseEntity.getBody());
            String responseBody = responseEntity.getBody();
            System.out.println("Response Body: " + responseBody);

            return responseEntity;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing the request.");
        }
    }


    private void parseResponseAndSaveToDatabase(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonResponse);

            List<Recipe> recipes = new ArrayList<>();

            if (root.has("results")) {
                JsonNode resultsNode = root.get("results");
                for (JsonNode recipeNode : resultsNode) {
                    Recipe recipe = new Recipe();
                    recipe.setId(recipeNode.get("id").asInt());
                    recipe.setTitle(recipeNode.get("title").asText());
                    recipe.setImage(recipeNode.get("image").asText());
                    System.out.println(recipe.toString());
                    recipes.add(recipe);
                }
            }

            recipeRepository.saveAll(recipes);
        } catch (Exception e) {
            e.printStackTrace();
            // Tratează eroarea în funcție de necesități
        }
    }

    public ResponseEntity<String> getRandomRecipes() {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(randomApiUrl)
                    .queryParam("apiKey", spoonacularApiKey)
                    .queryParam("number", 100);

            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForEntity(builder.toUriString(), String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing the request.");
        }
    }

    public ResponseEntity<String> getRecipeInformation(int id, boolean includeNutrition) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(recipeInformationApiUrl + id + "/information")
                    .queryParam("apiKey", spoonacularApiKey)
                    .queryParam("includeNutrition", includeNutrition);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(builder.toUriString(), String.class);

            String responseBody = responseEntity.getBody();
            System.out.println("Response Body: " + responseBody);

            return responseEntity;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing the request.");
        }
    }

   /* public ResponseEntity<String> extractRecipe(String url) {
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

           // saveExtractedRecipe(title, image, originalIngredientsList, instructionsList);

            System.out.println(instructionsList);
            return responseEntity;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing the request.");
        }
    }

    public void saveExtractedRecipe(String title, String image, List<String> originalIngredientsList, List<String> instructionsList) {
        try {
            // Crearea unei instanțe de ExtractedRecipe
            ExtractedRecipe extractedRecipe = new ExtractedRecipe();
            extractedRecipe.setTitle(title);
            extractedRecipe.setImage(image);

            // Salvarea rețetei în baza de date
            extractedRecipe = extractedRecipeRepository.save(extractedRecipe);

            // Iterarea prin lista de ingrediente originale și crearea instanțelor de Ingredient asociate cu rețeta
            for (String originalIngredient : originalIngredientsList) {
                // Crearea unei instanțe de Ingredient
                Ingredient ingredient = new Ingredient();
                ingredient.setIngredient(originalIngredient);

                // Setarea rețetei asociate cu acest ingredient
                ingredient.setRecipe(extractedRecipe);

                // Salvarea ingredientului în baza de date
                ingredientRepository.save(ingredient);
            }

            // Iterarea prin lista de instrucțiuni și crearea instanțelor de Step asociate cu rețeta
            for (String instruction : instructionsList) {
                // Crearea unei instanțe de Step
                Step step = new Step();
                step.setInstruction(instruction);

                // Setarea rețetei asociate cu acest pas
                step.setRecipe(extractedRecipe);

                // Salvarea pasului în baza de date
                stepRepository.save(step);
            }

            // Salvarea din nou a rețetei pentru a actualiza asocierea
            extractedRecipeRepository.save(extractedRecipe);

            // Adăugarea ingredientelor și pașilor la lista de ingrediente și pași a rețetei extrase
            extractedRecipe.setIngredients(ingredientRepository.findAllByRecipe(extractedRecipe));
            extractedRecipe.setSteps(stepRepository.findAllByRecipe(extractedRecipe));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving extracted recipe: " + e.getMessage());
        }
    }
*/

    public ResponseEntity<String> findByIngredients(String ingredients, int ranking) {
        try {
            // Format ingredientele corect pentru a evita probleme de interpretare
            String formattedIngredients = Arrays.stream(ingredients.split(","))
                    .map(String::trim)
                    .collect(Collectors.joining(","));

            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(ingredientsSearchApiUrl)
                    .queryParam("apiKey", spoonacularApiKey)
                    .queryParam("ingredients", formattedIngredients)
                    .queryParam("number", 30)
                    .queryParam("limitLicense", true)
                    .queryParam("ranking", ranking)
                    .queryParam("ignorePantry", true); // Modificat pentru a ignora articolele tipice din cămară

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(builder.toUriString(), String.class);

            String responseBody = responseEntity.getBody();
            System.out.println("Response Body: " + responseBody);

            return responseEntity;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing the request.");
        }
    }


    public ResponseEntity<String> searchMenuItems(String query) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(menuItemsSearchApiUrl)
                    .queryParam("apiKey", spoonacularApiKey)
                    .queryParam("query", query)
                    .queryParam("number", 1); // Adjust the number as needed

            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForEntity(builder.toUriString(), String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing the request.");
        }
    }


/*    public List<ExtractedRecipeDTO> getAllRecipes() {
        List<ExtractedRecipe> extractedRecipes = extractedRecipeRepository.findAll();
        return extractedRecipes.stream()
                .map(ExtractedRecipeBuilder::buildDTO)
                .collect(Collectors.toList());
    }*/

/*    public ExtractedDTO getDetailDisghen(int id) {
        Optional<ExtractedRecipe> extractedRecipeOptional = extractedRecipeRepository.findById(id);
        if (extractedRecipeOptional.isPresent()) {
            ExtractedRecipe extractedRecipe = extractedRecipeOptional.get();
            extractedRecipe.getIngredients().size();
            extractedRecipe.getSteps().size();
            return new ExtractedDTO(extractedRecipe);
        } else {
            throw new RuntimeException("Recipe not found with id: " + id);
        }
    }*/

/*    public AddedRecipeDTO addRecipe(AddedRecipeDTO recipeDTO) {
        try {

            String title = recipeDTO.getTitle();
            String image = recipeDTO.getImage();
            List<AddedIngredient> ingredients = recipeDTO.getIngredients();
            List<AddedStep> instructions = recipeDTO.getInstructions();


            saveRecipe(title, image, ingredients, instructions);

            return recipeDTO;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error adding recipe: " + e.getMessage());
        }
    }*/

   /* public RecipeUnderReview submitRecipeForApproval(RecipeUnderReview recipe) {
        try {

            String title = recipe.getTitle();
            String image = recipe.getImage();
            List<RecipeUnderReviewIngredient> ingredients = recipe.getIngredients();
            List<RecipeUnderReviewStep> instructions = recipe.getInstructions();


            saveRecipeUnderReview(title, image, ingredients, instructions);

            return recipe;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error adding recipe: " + e.getMessage());
        }
    }

    private void saveRecipeUnderReview(String title, String image, List<RecipeUnderReviewIngredient> ing, List<RecipeUnderReviewStep> instructions) {
        try {
            RecipeUnderReview recipeUnderReview = new RecipeUnderReview();
            recipeUnderReview.setTitle(title);
            recipeUnderReview.setImage(image);

            List<RecipeUnderReviewIngredient> ingredients = ing;

            if (!ingredients.isEmpty()) {
                RecipeUnderReviewIngredient ultimulElement = ingredients.get(ingredients.size() - 1);
                ingredients.remove(ingredients.size() - 1);
                ingredients.add(0, ultimulElement);
                System.out.println("Ing" + "  "+ingredients.toString());
            } else {
                System.out.println("Lista de ingrediente este goală.");
            }
            List<RecipeUnderReviewStep> step = instructions;
            System.out.println("STEP"+ step);

            if (!step.isEmpty()) {
                RecipeUnderReviewStep ultimulElement = step.get(step.size() - 1);
                step.remove(step.size() - 1);
                step.add(0, ultimulElement);
                System.out.println("Ing" + "  "+step.toString());
            } else {
                System.out.println("Lista de ingrediente este goală.");
            }
            for (RecipeUnderReviewIngredient ingredient : ingredients) {
                ingredient.setRecipe(recipeUnderReview);
            }

            for (RecipeUnderReviewStep instruction : step) {
                instruction.setRecipe(recipeUnderReview);
            }

            recipeUnderReview.setIngredients(ingredients);
            recipeUnderReview.setInstructions(step);
            recipeUnderReviewRepository.save(recipeUnderReview);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving recipe: " + e.getMessage());
        }
    }
*/
/*
    private void saveRecipe(String title, String image, List<AddedIngredient> ing, List<AddedStep> instructions) {
        try {
            AddedRecipe addedRecipe = new AddedRecipe();
            addedRecipe.setTitle(title);
            addedRecipe.setImage(image);

            List<AddedIngredient> ingredients = ing;

            List<AddedStep> steps = instructions;

            for (AddedIngredient ingredient : ingredients) {
                ingredient.setRecipe(addedRecipe);
            }

            for (AddedStep instruction : steps) {
                instruction.setRecipe(addedRecipe);
            }

            // Adăugăm lista de ingrediente și instrucțiuni la rețetă
            addedRecipe.setIngredients(ingredients);
            addedRecipe.setSteps(steps);

            // Salvăm rețeta în baza de date
            addedRecipeRepository.save(addedRecipe);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving recipe: " + e.getMessage());
        }
    }
*/



/*
    public AddedRecipeDTO getAddedDetailRecipe(int id) {
        Optional<AddedRecipe> addedRecipeOptional = addedRecipeRepository.findById(id);
        if (addedRecipeOptional.isPresent()) {
            AddedRecipe addedRecipe = addedRecipeOptional.get();
            addedRecipe.getIngredients().size();
            addedRecipe.getSteps().size();
            return new AddedRecipeDTO(addedRecipe);
        } else {
            throw new RuntimeException("Recipe not found with id: " + id);
        }
    }
*/

}

