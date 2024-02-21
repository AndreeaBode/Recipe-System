import { Component } from '@angular/core';
import { RecipeService } from '../services/recipe.service';
import { Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-search-ingredients',
  templateUrl: './search-ingredients.component.html',
  styleUrls: ['./search-ingredients.component.scss']
})
export class SearchIngredientsComponent {

  searchResult: any[] = [];
  ingredients: string = '';
  ranking: number = 1;
  recipes: any[] = [];
  errorMessage: string = '';

  constructor(private recipeService: RecipeService, private router: Router){}

  searchByIngredients(): void {
    this.recipeService.findRecipesByIngredients(this.ingredients, this.ranking).subscribe(
      (response: any) => {
        this.searchResult = response;
        console.log("Search results:", response);
        this.recipes = this.searchResult;
        this.errorMessage = ''; 
      },
      (error) => {
        console.error('Error:', error);
        this.errorMessage = 'Error fetching recipes. Please try again.'; // Display error message to the user
        this.searchResult = []; // Clear search results array
      }
    );
  }
  
  showRecipeDetails(recipeId: number) {
    this.router.navigate(['/recipe-detail', recipeId]);
  }
}
