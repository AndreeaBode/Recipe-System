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
export class FavoriteComponent implements OnInit {
  recipes: any[] = [];
  currentRecipes: any[] = [];
  pageSize: number = 6;
  currentPage: number = 0;

  constructor(private recipeService: RecipeService, private router: Router, private authService: AuthService, private likeService: LikeService) { }

  ngOnInit(): void {
    this.loadFavoriteRecipes();
  }

  loadFavoriteRecipes() {
    const userId = this.authService.userId();
    if (userId) {
      this.recipeService.getFavoriteRecipes(userId).subscribe(
        (data: any[]) => {
          this.recipes = data.map(recipe => {
            recipe.isLoved = true;
            return recipe;
          });
          this.updateDisplayedRecipes();
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
    const userId = this.authService.userId();
    if (!userId) {
      return;
    }

    const recipeId = recipe.recipeId;
    const name = recipe.name;
    recipe.isLoved = !recipe.isLoved;
    console.log("recipe.isLoved", recipe.isLoved);

    this.likeService.toggleLike(userId, recipeId, recipe.isLoved, name).subscribe(
      response => {
        console.log('Succes');
      },
      (error) => {
        console.log('Esec', error);
      }
    )
  }

  onPageChange(event: any): void {
    this.currentPage = event.pageIndex;
    this.updateDisplayedRecipes();
  }

  updateDisplayedRecipes(): void {
    const startIndex = this.currentPage * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    this.currentRecipes = this.recipes.slice(startIndex, endIndex);
  }
}
