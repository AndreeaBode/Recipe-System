import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { LikeService } from '../services/like.service';
import { RecipeService } from '../services/recipe.service';

@Component({
  selector: 'app-favorite',
  templateUrl: './favorite.component.html',
  styleUrls: ['./favorite.component.scss']
})
export class FavoriteComponent   implements OnInit {
  recipes: any[] = [];

  constructor(private recipeService: RecipeService, private router : Router, private authService: AuthService,private likeService: LikeService) { }

  ngOnInit(): void {
    this.loadFavoriteRecipes();
  }

  loadFavoriteRecipes() {
    const userId = this.authService.userId();
    console.log("id", userId);
    if (userId) {
      console.log("aa")
      this.recipeService.getFavoriteRecipes(userId).subscribe(
        (data: any[]) => {
          this.recipes = data;
          console.log("RRRR5", this.recipes);
          this.recipes.forEach(recipe => {
            console.log("C", recipe);
            if (recipe) {
                this.checkIfLiked(recipe);
            }
        });
        
          console.log("RRRR", data);
        },
        (error: any) => { 
          console.log('Error fetching favorite recipes:', error);
        }
      );
    }
  }
  
  showRecipeDetails(id: number): void {
    if (id) {
      this.router.navigate(['/details', id, 'spoonacular']);
    } else {
      console.error('ID-ul rețetei este nedefinit.');
    }
  }
  

  toggleLike(recipe: any): void {

    console.log("RR3", recipe);
    const userId = this.authService.userId();
    if (!userId) {
     
      return;
    }

    const recipeId = recipe.recipeId;
    const name = recipe.name;
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
    const recipeId = recipe.recipeId;
    const name = recipe.name;
    console.log("name", name);

 
    console.log("U", userId);
    console.log("R",  recipe.recipeId);
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