import { Component } from '@angular/core';
import { RecipeService } from '../services/recipe.service';
import { Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-recipe',
  templateUrl: './recipe.component.html',
})
export class RecipeComponent {
  searchResult: any[] = [];
  randomRecipes: any;
  searchTerm: string = '';
  recipeUrl: string = '';
  extractedRecipe: any;
  recipeDetails: any = {};
  
  constructor(private recipeService: RecipeService, private router: Router) {}

  searchFood(query: string): void {
    this.recipeService.searchFood(query).subscribe(
      (response: any) => {
        this.searchResult = response.results; 
        console.log("Search results:", this.searchResult); 
      },
      (error: HttpErrorResponse) => {
        console.error('Error:', error);
      }
    );
  }
  
  

  getRandomRecipes(): void {
    this.recipeService.getRandomRecipes().subscribe(
      (response: any) => {
        this.randomRecipes = response.recipes; 
        console.log("random", this.randomRecipes);
      },
      (error: HttpErrorResponse) => {
        console.error('Error:', error);
      }
    );
  }

  showRecipeDetails(recipeId: number) {
    this.router.navigate(['/recipe-detail', recipeId]);
  }
}