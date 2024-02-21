import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RecipeService } from '../services/recipe.service';

@Component({
  selector: 'app-recipe-dishgen',
  templateUrl: './recipe-dishgen.component.html',
  styleUrls: ['./recipe-dishgen.component.scss']
})
export class RecipeDishgenComponent implements OnInit {
  recipes: any[] = [];

  constructor(private recipeService: RecipeService, private router: Router) { }

  ngOnInit(): void {
    this.loadRecipes();
  }

  loadRecipes(): void {
    this.recipeService.getAllRecipes().subscribe(
      (data: any[]) => {
        this.recipes = data;
      },
      (error) => {
        console.log(error);
      }
    );
  }

  showRecipeDetails(recipeId: number) {console.log(recipeId);
    this.router.navigate(['/dishgen-detail', recipeId]);
    
  }
}
