import { Component, OnInit } from '@angular/core';
import { RecipeService } from '../services/recipe.service';
import { RecipeUnderReview } from '../models/recipe-under-review';
import { RecipeUnderReviewService } from '../services/recipe-under-review.service';
import { Recipe } from '../models/recipe';
import { Ingredient } from '../models/ingredient';
import { Instruction } from '../models/instruction';

@Component({
  selector: 'app-recipe-under-review',
  templateUrl: './recipe-under-review.component.html',
  styleUrls: ['./recipe-under-review.component.scss']
})
export class RecipeUnderReviewComponent implements OnInit {
  recipes: any[] = [];

  constructor(private recipeUnderReviewService: RecipeUnderReviewService, private recipeService: RecipeService) { }

  ngOnInit(): void {
    this.loadRecipesUnderReview();
  }

  loadRecipesUnderReview(): void {
    this.recipeUnderReviewService.getRecipesUnderReview()
      .subscribe((data: RecipeUnderReview[]) => {
        this.recipes = data;
      });
  }
  

  acceptRecipe(recipeId: number): void {
    const recipeUnderReview = this.recipes.find(r => r.id === recipeId);
    if (recipeUnderReview) {
      console.log("RecipeUnderReview",recipeUnderReview);
      const recipeToSave: Recipe = this.convertToRecipe(recipeUnderReview); 
      console.log("recipeToSave", recipeToSave);
      this.recipeService.addRecipe(recipeToSave)
        .subscribe(() => {
          this.loadRecipesUnderReview();
          this.deleteRecipe(recipeId);
        });
    }
  }

  deleteRecipe(recipeId: number): void {
    // Facem cererea pentru a șterge rețeta folosind serviciul recipeService
    this.recipeUnderReviewService.deleteRecipe(recipeId)
      .subscribe(() => {
        // Dacă ștergerea a fost realizată cu succes, reîncărcăm lista de rețete în așteptare
        this.loadRecipesUnderReview();
      });
  }

  rejectRecipe(recipeId: number): void {
    this.recipeUnderReviewService.deleteRecipe(recipeId)
      .subscribe(() => {
        // Dacă ștergerea a fost realizată cu succes, reîncărcăm lista de rețete în așteptare
        this.loadRecipesUnderReview();
      });
  }


  private convertToRecipe(recipeUnderReview: RecipeUnderReview): Recipe {
    console.log("recipeUnderReview", recipeUnderReview);
    console.log("recipeUnderReview.ingredients", recipeUnderReview.ingredients);
    console.log("recipeUnderReview.instructions", recipeUnderReview.instructions);
  
    const ingredients: Ingredient[] = recipeUnderReview.ingredients ? recipeUnderReview.ingredients.map(ingredient => new Ingredient(ingredient.ingredient)) : [];
    const instructions: Instruction[] = recipeUnderReview.instructions ? recipeUnderReview.instructions.map(step => new Instruction(step.instruction)) : [];
    console.log("instructions", instructions);
    return new Recipe(recipeUnderReview.title, recipeUnderReview.image, ingredients, instructions);
  }
  
  
}
