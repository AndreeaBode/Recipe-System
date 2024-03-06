import { Component } from '@angular/core';
import { RecipeService } from '../services/recipe.service';
import { Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { SearchFoodOptions } from '../search-food-options';
import { AuthService } from '../auth.service';
import { LikeService } from '../services/like.service';

@Component({
  selector: 'app-advanced-search',
  templateUrl: './advanced-search.component.html',
  styleUrls: ['./advanced-search.component.scss']
})
export class AdvancedSearchComponent {
  searchResult: any[] = [];
  randomRecipes: any;
  searchTerm: string = '';
  diet: string = '';
  intolerances: string = '';
  includeIngredients: string = '';
  excludeIngredients: string = '';
  maxReadyTime: number = 0;
  minCarbs: number = 0;
  maxCarbs: number = 0;
  minProtein: number = 0;
  maxProtein: number = 0;
  minCalories: number = 0;
  maxCalories: number = 0;
  minFat: number = 0;
  maxFat: number = 0;
  minCholesterol: number = 0;
  maxCholesterol: number = 0;
  minSugar: number = 0;
  maxSugar: number = 0;

  constructor(private recipeService: RecipeService, private router: Router,private authService: AuthService,private likeService: LikeService) {}

  searchFoodAdvanced(options: SearchFoodOptions): void {
    this.recipeService.searchFoodAdvanced(options).subscribe(
      (response: any) => {
        this.searchResult = response.results; 
        console.log("Search results:", this.searchResult); 
      },
      (error: HttpErrorResponse) => {
        console.error('Error:', error);
      }
    );
  }
  

  showRecipeDetails(recipeId: number) {
    this.router.navigate(['/advanced-detail', recipeId, 'advanced-detail']);
  }



  toggleLike(recipe: any): void {

    console.log("RR3", recipe);
    const userId = this.authService.userId();
    if (!userId) {
     
      return;
    }

    const recipeId = recipe.id;
    const name = "spoonacular";
    console.log(name);
    recipe.isLoved = !recipe.isLoved;

    console.log("ue", userId);
    console.log("re", recipeId);
    console.log("ne", name);
    console.log("Lo", recipe.isLoved);
    this.likeService.toggleLike(userId, recipeId,recipe.isLoved, name).subscribe(
      response => {
        console.log('Succes');
       // window.location.reload();
      },
      (error) =>{
        console.log('Esec', error);
      }
    )
  }



  checkIfLiked(recipe: any): void {
    console.log("Recipe object:", recipe); // Log the recipe object
    const userId = this.authService.userId();
    const recipeId = recipe.id;
    const name = "spoonacular";
    console.log("name", name);

 
    console.log("U", userId);
    console.log("R",  recipe.id);
    this.recipeService.checkIfLiked(userId, recipeId).subscribe(
      response => {
        recipe.isLoved = response; 
      },
      error => {
        console.error('Error checking if liked:', error);
      }
    );
  }
  
}
