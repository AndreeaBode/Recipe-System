package sd.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sd.entities.SearchFoodOptions;
import sd.services.AdvancedRecipeService;

@RestController
@RequestMapping(value = "/")
public class AdvancedRecipeController {
    private final AdvancedRecipeService advancedRecipeService;

    public AdvancedRecipeController(AdvancedRecipeService advancedRecipeService) {
        this.advancedRecipeService = advancedRecipeService;
    }

    @GetMapping("/searchFoodAdvanced")
    public ResponseEntity<String> searchFoodAdvanced(@ModelAttribute SearchFoodOptions options) {
        ResponseEntity<String> responseEntity = advancedRecipeService.searchFoodAdvanced(options);
        String responseBody = responseEntity.getBody();
        System.out.println(responseBody);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/recipe/{id}/nutrition")
    public ResponseEntity<String> getRecipeNutrition(@PathVariable int id) {

        ResponseEntity<String> responseEntity = advancedRecipeService.getRecipeNutrition(id);
        String responseBody = responseEntity.getBody();
        System.out.println(responseBody);
        return ResponseEntity.ok(responseBody);
    }
}
