import { Component } from '@angular/core';
import { RecipeService } from '../services/recipe.service';

@Component({
  selector: 'app-menu-item',
  templateUrl: './menu-item.component.html',
  styleUrls: ['./menu-item.component.scss']
})
export class MenuItemComponent {
  query: string = '';
  searchResult: any[] = [];
  errorMessage: string = '';

  constructor(private recipeService: RecipeService) { }

  searchMenuItems(): void {
    if (this.query.trim() === '') {
      this.errorMessage = 'Please enter a search query.';
      return;
    }
    this.recipeService.searchMenuItem(this.query).subscribe(
      (response: any) => {
        this.searchResult = response; // Sau ajustați după structura răspunsului
        console.log("Search results:", this.searchResult);
        this.errorMessage = ''; // Curățați mesajul de eroare dacă este prezent
      },
      (error) => {
        console.error('Error:', error);
        this.errorMessage = 'Error fetching menu items. Please try again.'; // Afișați mesajul de eroare utilizatorului
        this.searchResult = []; // Curățați array-ul de rezultate de căutare
      }
    );
  }
}