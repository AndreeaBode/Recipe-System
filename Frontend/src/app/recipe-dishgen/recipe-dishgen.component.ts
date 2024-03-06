import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { LikeService } from '../services/like.service';
import { RecipeService } from '../services/recipe.service';

@Component({
  selector: 'app-recipe-dishgen',
  templateUrl: './recipe-dishgen.component.html',
  styleUrls: ['./recipe-dishgen.component.scss']
})
export class RecipeDishgenComponent implements OnInit {
  recipes: any[] = [];

  constructor(
    private recipeService: RecipeService,
    private router: Router, 
    private authService: AuthService, 
    private likeService: LikeService
    ) { }

  ngOnInit(): void {
    this.loadRecipes();
  }

  loadRecipes(): void {
    this.recipeService.getAllRecipes().subscribe(
      (data: any[]) => {
        this.recipes = data;

        this.recipes.forEach(recipe => {
          console.log("C", recipe);
          if (recipe) {
              this.checkIfLiked(recipe);
          }
      });

      },
      (error) => {
        console.log(error);
      }
    );
  }


  showRecipeDetails(id: number): void {
    this.router.navigate(['/details', id, 'dishgen']);
  }
  

  toggleLike(recipe: any): void {
    const userId = this.authService.userId();
    if (!userId) {
      // Nu putem adăuga sau șterge like-ul fără un utilizator autentificat
      return;
    }

    const recipeId = recipe.id;
    const name = "extracted_recipes";
    console.log(name);
    recipe.isLoved = !recipe.isLoved;

    console.log("Lo", recipe.isLoved);
    this.likeService.toggleLike(userId, recipeId, recipe.isLoved, name).subscribe(
      response => {
        console.log('Succes');
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
    const name = "extracted_recipe";
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
