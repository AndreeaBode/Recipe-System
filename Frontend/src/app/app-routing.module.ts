import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { AuthGuard } from './auth.guard';
import { HomePageComponent } from './home-page/home-page.component';
import { RecipeComponent } from './recipe/recipe.component';
import { RecipeDetailComponent } from './recipe/recipe-detail/recipe-detail.component';
import { ExtractRecipeComponent } from './extract-recipe/extract-recipe.component';
import { SearchIngredientsComponent } from './search-ingredients/search-ingredients.component';
import { MenuItemComponent } from './menu-item/menu-item.component';
import { AdvancedSearchComponent } from './advanced-search/advanced-search.component';
import { AdvancedDetailComponent } from './advanced-search/advanced-detail/advanced-detail.component';
import { RecipeDishgenComponent } from './recipe-dishgen/recipe-dishgen.component';
import { DishgenDetailComponent } from './recipe-dishgen/dishgen-detail/dishgen-detail.component';
import { AddRecipeComponent } from './add-recipe/add-recipe.component';

const routes: Routes = [
  { path: '', redirectTo: '/home-page', pathMatch: 'full' },
  { path: 'login', component: LoginComponent},
  { path: 'home-page', component: HomePageComponent},
  { path: 'recipes', component: RecipeComponent },
  { path: 'recipe-detail/:id', component: RecipeDetailComponent },
  { path: 'extract-recipe', component: ExtractRecipeComponent },
  { path: 'search-ingredients', component: SearchIngredientsComponent },
  { path: 'menu-items', component: MenuItemComponent },
  { path: 'advanced-search', component: AdvancedSearchComponent },
  { path: 'advanced-detail/:id', component: AdvancedDetailComponent },
  { path: 'dishgen', component: RecipeDishgenComponent },
  { path: 'dishgen-detail/:id', component: DishgenDetailComponent },
  { path: 'add', component: AddRecipeComponent },

];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
