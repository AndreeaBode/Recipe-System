import { Component } from '@angular/core';
import { RecipeService } from '../services/recipe.service';
import { Recipe } from '../recipe';
import { Ingredient } from '../ingredient';
import { Instruction } from '../instruction';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-add-recipe',
  templateUrl: './add-recipe.component.html',
  styleUrls: ['./add-recipe.component.scss']
})
export class AddRecipeComponent {
  recipe: Recipe = new Recipe('', '', [], []); 
  newIngredient: string = ''; 
  newInstruction: string = '';

  constructor(private toastr: ToastrService,private recipeService: RecipeService) {}
  
  submitRecipe(): void {
    // Verifică dacă există titlu și imagine pentru rețetă
    if (!this.recipe.title || !this.recipe.image) {
        alert('Te rugăm să completezi titlul și imaginea rețetei.');
        return;
    }

    // Adaugă ingredientele și pașii din câmpurile noi în lista de ingrediente și pași
    if (this.newIngredient.trim() !== '') {
        this.recipe.ingredients.push(new Ingredient(this.newIngredient.trim()));
        this.newIngredient = ''; // Resetăm câmpul de input pentru noul ingredient
    }

    if (this.newInstruction.trim() !== '') {
        this.recipe.instructions.push(new Instruction(this.newInstruction.trim()));
        this.newInstruction = ''; // Resetăm câmpul de input pentru noul pas
    }
    const recipe = new Recipe(this.recipe.title, this.recipe.image, this.recipe.ingredients, this.recipe.instructions);
  console.log("A",recipe);

    this.recipeService.addRecipe(recipe)
        .subscribe(
            () => {
                //alert('Rețetă adăugată cu succes!');
                this.toastr.success('Rețetă adăugată cu succes!', 'Succes');
                this.resetForm();
            },
            (error: any) => {
                console.error('Eroare la adăugarea rețetei:', error);
                this.toastr.error('Eroare la adăugarea rețetei. Te rugăm să încerci din nou mai târziu.', 'Eroare');
            }
        );
}

resetForm(): void {
    this.recipe = new Recipe('', '', [], []); 
}

addFieldIngredient(): void {
    this.recipe.ingredients.push(new Ingredient('')); 
}

addFieldStep(): void {
    this.recipe.instructions.push(new Instruction('')); 
}

}
