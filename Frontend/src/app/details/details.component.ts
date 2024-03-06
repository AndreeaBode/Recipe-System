import { Component, OnInit } from '@angular/core';
import { RecipeService } from 'src/app/services/recipe.service';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from 'src/app/auth.service';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.scss']
})
export class DetailsComponent implements OnInit {
  recipe: any; 
  id: number = 0; 
  isLoved: boolean = false;
  loading: boolean = false;
  recippeId!: number;
  name: string = "";

  constructor(
    private recipeService: RecipeService,
    private route: ActivatedRoute,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const recipeId = +this.route.snapshot.paramMap.get('id')!;
    if (recipeId) {
  const sourcePage = this.route.snapshot.paramMap.get('sourcePage');

  console.log("SourcePage", sourcePage);

  this.name = sourcePage ?? '';

      console.log("SourcePage", sourcePage);
      this.loading = true;

      if (sourcePage === 'addedRecipe') {
        console.log("Da");
        this.recipeService.getAddeddRecipeDetails(recipeId).subscribe(
          (recipe: any) => {
            console.log("A", recipe);
            this.recipe = recipe;
            this.loading = false;
          },
          (error: any) => {
            console.error('Error loading added recipe:', error);
            this.loading = false;
          }
        );
      } else if (sourcePage === 'dishgen') {
        this.recipeService.getRecipeDishgenDetails(recipeId).subscribe(
          (recipe: any) => {
            console.log("D", recipe);
            this.recipe = recipe;
            this.loading = false;
          },
          (error: any) => {
            console.error('Error loading dishgen recipe:', error);
            this.loading = false;
          }
        );
      }  else if (sourcePage === 'spoonacular') {
        this.recipeService.getRecipeInformation(recipeId, false).subscribe(
          (response) => {
            this.recipe = response;
            console.log('Detaliile rețetei:', this.recipe);
          },
          (error) => {
            console.error('Eroare la obținerea detaliilor rețetei:', error);
          }
        );
      } 
      
    } else {
      console.error('No se pudo obtener el ID de la receta.');
    }
  }

  toggleLike(recipe: any): void {
    const userId = this.authService.userId();
    if (!userId) {
      // Nu putem adăuga sau șterge like-ul fără un utilizator autentificat
      return;
    }

    const recipeId = recipe.id;
    const name = this.name;

    console.log("Name Details", name);

    recipe.isLoved = !recipe.isLoved;

    if (recipe.isLoved) {
      this.saveLikeToDatabase(userId, recipeId, name); 
    } else { 
      this.deleteLikeFromDatabase(userId, recipeId, name); 
    }
  }

  saveLikeToDatabase(userId: number, recipeId: number, name: string): void {
    this.recipeService.saveLike(userId, recipeId, name).subscribe(
      response => {
        console.log('Datele au fost salvate cu succes în baza de date!');
      },
      error => {
        console.error('Eroare la salvarea datelor în baza de date:', error);
      }
    );
  }
  
  deleteLikeFromDatabase(userId: number, recipeId: number, name: string): void {
    this.recipeService.deleteLike(userId, recipeId, name).subscribe(
      response => {
        console.log('Datele au fost șterse cu succes din baza de date!');
      },
      error => {
        console.error('Eroare la ștergerea datelor din baza de date:', error);
      }
    );
  }
}
