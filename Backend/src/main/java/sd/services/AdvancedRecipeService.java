package sd.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import sd.entities.SearchFoodOptions;
import sd.entities.User;
import sd.repositories.RecipeRepository;
import sd.repositories.UserRepository;

import java.time.LocalDateTime;

@Service
public class AdvancedRecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    //private final String spoonacularApiKey = "d79ab01d2eff47d081feed07a650ff00";
    private final String spoonacularApiKey = "da3a95ad9e794dc3b5d43cf1f0f8cf60";
    //private final String spoonacularApiKey = "e0f0174758e74ee18fe3567c329272b5";

    private final String searchApiUrl = "https://api.spoonacular.com/recipes/complexSearch";
    private final String nutritionWidgetUrl = "https://api.spoonacular.com/recipes/{id}/nutritionWidget.json";


    private boolean canPerformSearch(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneWeekAgo = now.minusWeeks(1);

        if (user.getLastRequestTime() == null || user.getLastRequestTime().isBefore(oneWeekAgo)) {
            user.setWeeklyRequestCount(0);
            user.setLastRequestTime(now);
        }

        if ("premium".equals(user.getRole()) || "admin".equals(user.getRole())) {
            user.setLastRequestTime(now);
            userRepository.save(user);
            return true;
        }

        if (user.getWeeklyRequestCount() < 3) {
            user.setWeeklyRequestCount(user.getWeeklyRequestCount() + 1);
            user.setLastRequestTime(now);
            userRepository.save(user);
            return true;
        }

        return false;
    }
    public ResponseEntity<String> searchFoodAdvanced(SearchFoodOptions options, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!canPerformSearch(user)) {
            return ResponseEntity.status(403).body("Search limit exceeded. Please make a payment.");
        }
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(searchApiUrl)
                    .queryParam("apiKey", spoonacularApiKey)
                    .queryParam("query", options.getQuery())
                    .queryParam("number", 21);

            if (options.getDiet() != null && !options.getDiet().isEmpty()) {
                builder.queryParam("diet", options.getDiet());
            }
            if (options.getIntolerances() != null && !options.getIntolerances().isEmpty()) {
                builder.queryParam("intolerances", options.getIntolerances());
            }
            if (options.getIncludeIngredients() != null && !options.getIncludeIngredients().isEmpty()) {
                builder.queryParam("includeIngredients", options.getIncludeIngredients());
            }
            if (options.getExcludeIngredients() != null && !options.getExcludeIngredients().isEmpty()) {
                builder.queryParam("excludeIngredients", options.getExcludeIngredients());
            }
            if (options.getMaxReadyTime() != 0) {
                builder.queryParam("maxReadyTime", options.getMaxReadyTime());
            }
            if (options.getMinCarbs() != 0) {
                builder.queryParam("minCarbs", options.getMinCarbs());
            }
            if (options.getMaxCarbs() != 0) {
                builder.queryParam("maxCarbs", options.getMaxCarbs());
            }
            if (options.getMinProtein() != 0) {
                builder.queryParam("minProtein", options.getMinProtein());
            }
            if (options.getMaxProtein() != 0) {
                builder.queryParam("maxProtein", options.getMaxProtein());
            }
            if (options.getMinCalories() != 0) {
                builder.queryParam("minCalories", options.getMinCalories());
            }
            if (options.getMaxCalories() != 0) {
                builder.queryParam("maxCalories", options.getMaxCalories());
            }
            if (options.getMinFat() != 0) {
                builder.queryParam("minFat", options.getMinFat());
            }
            if (options.getMaxFat() != 0) {
                builder.queryParam("maxFat", options.getMaxFat());
            }
            if (options.getMinCholesterol() != 0) {
                builder.queryParam("minCholesterol", options.getMinCholesterol());
            }
            if (options.getMaxCholesterol() != 0) {
                builder.queryParam("maxCholesterol", options.getMaxCholesterol());
            }
            if (options.getMinSugar() != 0) {
                builder.queryParam("minSugar", options.getMinSugar());
            }
            if (options.getMaxSugar() != 0) {
                builder.queryParam("maxSugar", options.getMaxSugar());
            }

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(builder.toUriString(), String.class);

            return responseEntity;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing the request.");
        }
    }

    public ResponseEntity<String> getRecipeNutrition(int id) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(nutritionWidgetUrl)
                    .queryParam("apiKey", spoonacularApiKey);

            String apiUrl = builder.buildAndExpand(id).toUriString();

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);

            String responseBody = responseEntity.getBody();
            System.out.println("Nutrition Widget Response Body: " + responseBody);

            return responseEntity;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing the request.");
        }
    }
}
