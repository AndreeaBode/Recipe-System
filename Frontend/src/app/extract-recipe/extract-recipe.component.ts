import { Component } from '@angular/core';
import { RecipeService } from '../services/recipe.service';
import { Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-extract-recipe',
  templateUrl: './extract-recipe.component.html',
  styleUrls: ['./extract-recipe.component.scss']
})
export class ExtractRecipeComponent {
  searchResult: any[] = [];
  randomRecipes: any;
  searchTerm: string = '';
  recipeUrl: string = '';
  extractedRecipe: any;
  recipeDetails: any = {};
  
  constructor(private recipeService: RecipeService) {}

  extractRecipe(url: string): void {
    this.recipeService.extractRecipe(url).subscribe(
      (response: any) => {
        this.recipeDetails = response;
        console.log("Extracted Recipe:", this.recipeDetails);
      },
      (error: HttpErrorResponse) => {
        console.error('Error:', error);
      }
    );
  }
}
