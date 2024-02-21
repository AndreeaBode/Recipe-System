import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RecipeService } from 'src/app/services/recipe.service';

@Component({
  selector: 'app-recipe-detail',
  templateUrl: './recipe-detail.component.html',
  styleUrls: ['./recipe-detail.component.scss']
})
export class RecipeDetailComponent implements OnInit {
  recipeId!: number;
  recipe: any; 

  constructor(
    private route: ActivatedRoute, 
    private router: Router, // Injectăm serviciul Router
    private recipeService: RecipeService // Injectăm serviciul RecipeService
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.recipeId = +params['id']; // Convertim la număr
      this.recipeService.getRecipeInformation(this.recipeId, false).subscribe(
        (response) => {
          this.recipe = response;
          console.log('Detaliile rețetei:', this.recipe);
        },
        (error) => {
          console.error('Eroare la obținerea detaliilor rețetei:', error);
        }
      );
    });
  }

  // Metoda pentru a reveni înapoi la pagina principală și a menține rezultatele căutării
  goBackToRecipes(): void {
    this.router.navigate(['/recipes'], { queryParamsHandling: 'preserve' });
  }
}
