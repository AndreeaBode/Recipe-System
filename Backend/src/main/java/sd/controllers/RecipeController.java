package sd.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sd.dtos.AddedRecipeDTO;
import sd.dtos.ExtractedDTO;
import sd.dtos.ExtractedRecipeDTO;
import sd.services.RecipeService;

import java.util.List;

@RestController
@RequestMapping(value = "/")
public class RecipeController {

    private final RecipeService recipeService;
    @Autowired
    private ObjectMapper objectMapper;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/searchFood")
    public ResponseEntity<String> searchFood(@RequestParam String query) {
        ResponseEntity<String> responseEntity = recipeService.searchFood(query);
        String responseBody = responseEntity.getBody();
        System.out.println(responseBody);
        return ResponseEntity.ok(responseBody);
    }


    @GetMapping("/randomRecipes")
    public ResponseEntity<String> getRandomRecipes() {
        ResponseEntity<String> responseEntity = recipeService.getRandomRecipes();
        String responseBody = responseEntity.getBody();
        System.out.println(responseBody);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/recipe/{id}/information")
    public ResponseEntity<String> getRecipeInformation(@PathVariable int id, @RequestParam(defaultValue = "false") boolean includeNutrition) {
        ResponseEntity<String> responseEntity = recipeService.getRecipeInformation(id, includeNutrition);
        String responseBody = responseEntity.getBody();

        String cleanResponseBody = responseBody.replaceAll("<[^>]+>", "");

        System.out.println(cleanResponseBody);
        return ResponseEntity.ok(cleanResponseBody);
    }

    @GetMapping("/extractRecipe")
    public ResponseEntity<String> extractRecipe(@RequestParam String url) {
        ResponseEntity<String> responseEntity = recipeService.extractRecipe(url);
        String responseBody = responseEntity.getBody();
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/recipes/findByIngredients")
    public ResponseEntity<String> findRecipesByIngredients(
            @RequestParam("ingredients") String ingredients,
            @RequestParam("ranking") int ranking) {
        ResponseEntity<String> responseEntity = recipeService.findByIngredients(ingredients, ranking);
        String responseBody = responseEntity.getBody();
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/menuItems")
    public ResponseEntity<String> searchMenuItems(
            @RequestParam String query) {
        ResponseEntity<String> responseEntity = recipeService.searchMenuItems(query);
        String responseBody = responseEntity.getBody();
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/recipes")
    public List<ExtractedRecipeDTO> getAllRecipes() {
        List<ExtractedRecipeDTO> recipes = recipeService.getAllRecipes();
        System.out.println("Recipe" + recipes);
        return recipes;
    }

    @GetMapping("/recipes-detail")
    public List<ExtractedRecipeDTO> getRecipesDetail() {
        List<ExtractedRecipeDTO> recipes = recipeService.getAllRecipes();
        System.out.println("Recipe" + recipes);
        return recipes;
    }

    @GetMapping("/dishgen-detail/{id}")
    public ResponseEntity<ExtractedDTO> getDetailDisghen(@PathVariable("id") int id) {
        ExtractedDTO extractedDTO = recipeService.getDetailDisghen(id);
        return new ResponseEntity<>(extractedDTO, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<AddedRecipeDTO> addRecipe(@RequestBody String payload) {
        try {
            AddedRecipeDTO recipeDTO = objectMapper.readValue(payload, AddedRecipeDTO.class);
            System.out.println("Received Recipe DTO: " + recipeDTO);
            AddedRecipeDTO addedRecipeDTO = recipeService.addRecipe(recipeDTO);
            return ResponseEntity.ok(addedRecipeDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/added-detail/{id}")
    public ResponseEntity<AddedRecipeDTO> getAddedDetailRecipe(@PathVariable("id") int id) {
        AddedRecipeDTO addedRecipeDTO = recipeService.getAddedDetailRecipe(id);
        return new ResponseEntity<>(addedRecipeDTO,HttpStatus.OK);
    }
}

