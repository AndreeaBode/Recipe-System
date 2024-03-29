import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SearchFoodOptions } from '../search-food-options';
import { HttpClient, HttpParams, HttpHeaders} from '@angular/common/http';
import { Recipe } from '../recipe';




@Injectable({
  providedIn: 'root'
})
export class RecipeService {
  private backendUrl = 'http://localhost:8080'; // Replace with your actual backend URL

  constructor(private http: HttpClient) {}

  searchFood(query: string): Observable<any> {
    return this.http.get<any>(`${this.backendUrl}/searchFood?query=${query}`);
  }

  searchFoodAdvanced(options: SearchFoodOptions): Observable<any> {
    let params = new HttpParams();
  
    Object.entries(options).forEach(([key, value]) => {
      if (value !== null && value !== undefined && value !== '') {
        params = params.append(key, value);
      }
    });
  
    return this.http.get<any>(`${this.backendUrl}/searchFoodAdvanced`, { params });
  }
  

  getRandomRecipes(): Observable<any> {
    return this.http.get<any>(`${this.backendUrl}/randomRecipes`);
  }

  getRecipeInformation(recipeId: number, includeNutrition: boolean): Observable<any> {
    const url = `${this.backendUrl}/recipe/${recipeId}/information?includeNutrition=${includeNutrition}`;
    return this.http.get<any>(url);
  }

  extractRecipe(url: string): Observable<any> {
    return this.http.get<any>(`${this.backendUrl}/extractRecipe?url=${encodeURIComponent(url)}`);
  }

  findRecipesByIngredients(ingredients: string, ranking: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.backendUrl}/recipes/findByIngredients`, {
      params: {
        ingredients,
        ranking: ranking.toString()
      }
    });
  }

  searchMenuItem(query: string): Observable<any> {
    return this.http.get<any>(`${this.backendUrl}/menuItems?query=${query}`);
  }

  getRecipeNutritionWidget(recipeId: number) {
    return this.http.get(`${this.backendUrl}/recipe/${recipeId}/nutrition`);
  }
  
  getAllRecipes(): Observable<any> {
    return this.http.get(`${this.backendUrl}/recipes`);
  }

  getRecipeDishgenDetails(id: number){
    return this.http.get(`${this.backendUrl}/dishgen-detail/${id}`);
  }

  addRecipe(recipe: Recipe): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
  
    // Construim structura dorită a obiectului JSON
    const payload = {
      title: recipe.title,
      image: recipe.image,
      ingredients: recipe.ingredients.map(ingredient => ingredient.ingredient),
      instructions: recipe.instructions.map(instruction => instruction.instruction)
    };
  
    console.log("z" + payload);
    // Trimitem cererea POST către backend
    return this.http.post<any>(`${this.backendUrl}/add`, payload, { headers });
  }
  
  
  saveLike(userId: number, recipeId: number, name: string): Observable<any> {
    console.log("Da2");
    const likeData = { userId, recipeId, name};
    console.log(`${this.backendUrl}/love/likes`, likeData);
    return this.http.post<any>(`${this.backendUrl}/love/likes`, likeData);
  }

  

  deleteLike(userId: number, recipeId: number, name: string): Observable<any> {
    console.log("Nu2");
    return this.http.delete<any>(`${this.backendUrl}/love/likes/${userId}/${recipeId}/${name}`);
  }

  checkIfLiked(userId: number, recipeId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.backendUrl}/love/isLiked/${userId}/${recipeId}`);
  }
  

  getAddedRecipeDetails() {
    console.log("aaaaaaa");
    return this.http.get(`${this.backendUrl}/love/added-recipe-detail`);
  }
  
  getAddeddRecipeDetails(id: number) {
    return this.http.get(`${this.backendUrl}/added-detail/${id}`);
  }


  getFavoriteRecipes(userId: number): Observable<any[]> {
    console.log("idd", userId);
    const url = `${this.backendUrl}/love/favorite/${userId}`; 
    return this.http.get<any[]>(url);
  }
}