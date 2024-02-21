import { Component } from '@angular/core';
import { RecipeService } from '../services/recipe.service';
import { Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { SearchFoodOptions } from '../search-food-options';

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

  constructor(private recipeService: RecipeService, private router: Router) {}

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
    this.router.navigate(['/advanced-detail', recipeId]);
  }
}
