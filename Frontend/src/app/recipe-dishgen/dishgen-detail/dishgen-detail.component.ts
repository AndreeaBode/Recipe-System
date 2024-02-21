import { Component, OnInit } from '@angular/core';
import { RecipeService } from 'src/app/services/recipe.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-dishgen-detail',
  templateUrl: './dishgen-detail.component.html',
  styleUrls: ['./dishgen-detail.component.scss']
})
export class DishgenDetailComponent implements OnInit {
  recipe: any; 
  id: number = 0; // Atribuirea unei valori implicite de 0

  constructor(
    private recipeService: RecipeService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.id = +params['id'];
      
      // Apelăm metoda pentru a obține detaliile rețetei
      this.recipeService.getRecipeDetails(this.id).subscribe(
        (response) => {
          console.log(response)
          this.recipe = response;
          console.log('Detaliile rețetei:', this.recipe);
        },
        (error) => {
          console.error('Eroare la obținerea detaliilor rețetei:', error);
        }
      );
    });
  }
}
