import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RecipeService } from 'src/app/services/recipe.service';

@Component({
  selector: 'app-advanced-detail',
  templateUrl: './advanced-detail.component.html',
  styleUrls: ['./advanced-detail.component.scss']
})
export class AdvancedDetailComponent implements OnInit {
 
  recipeId!: number;
  recipe: any;
  nutritionData: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private recipeService: RecipeService
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.recipeId = +params['id'];
      
      // Apelăm metoda pentru a obține detaliile rețetei
      this.recipeService.getRecipeInformation(this.recipeId, false).subscribe(
        (response) => {
          this.recipe = response;
          console.log('Detaliile rețetei:', this.recipe);
        },
        (error) => {
          console.error('Eroare la obținerea detaliilor rețetei:', error);
        }
      );

      // Apelăm metoda pentru a obține widget-ul de nutriție
      this.recipeService.getRecipeNutritionWidget(this.recipeId).subscribe(
        (response) => {
          this.nutritionData = response;
          console.log('Datele de nutriție:', this.nutritionData);
        },
        (error) => {
          console.error('Eroare la obținerea datelor de nutriție:', error);
        }
      );
    });
  }
}