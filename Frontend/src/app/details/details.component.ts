import { Component, OnInit } from '@angular/core';
import { RecipeService } from 'src/app/services/recipe.service';
import { ActivatedRoute, UrlSegment } from '@angular/router';
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
  comments: any[] = []; 
  newComment: string = ""; 
  reviewsUpdate: any[] = [];
  reviews: any[] = []; 
  newReview: any = {rating: 0 }; 
  isReviewed: boolean = false;


  constructor(
    private recipeService: RecipeService,
    private route: ActivatedRoute,
    private authService: AuthService,
  ) { }

  ngOnInit(): void {
    const recipeId = +this.route.snapshot.paramMap.get('id')!;
    if (recipeId) {
      const sourcePage = this.route.snapshot.paramMap.get('sourcePage');


      this.name = sourcePage ?? '';

      
      this.loading = true;

      if (sourcePage === 'addedRecipe') {

        this.recipeService.getAddeddRecipeDetails(recipeId).subscribe(
          (recipe: any) => {
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
           
            this.recipe = recipe;
            this.loading = false;
          },
          (error: any) => {
            console.error('Error loading dishgen recipe:', error);
            this.loading = false;
          }
        );
      } else if (sourcePage === 'spoonacular') {
        this.recipeService.getRecipeInformation(recipeId, false).subscribe(
          (response) => {
            this.recipe = response;
            
          },
          (error) => {
            console.error('Eroare la obținerea detaliilor rețetei:', error);
          }
        );
      }

    } else {
      console.error('No se pudo obtener el ID de la receta.');
    }
    this.getComments();
    this.getReviews();
    this.addOrUpdateReview();
  }

  toggleLike(recipe: any): void {
    const userId = this.authService.userId();
    if (!userId) {
      return;
    }

    const recipeId = recipe.id;
    const name = this.name;

    

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


  getComments() {
    const idParam = this.route.snapshot.paramMap.get('id');
    const id = idParam ? +idParam : 0; 
    const urlParts = this.route.snapshot.url.map(segment => segment.path);
    const username = this.authService.username();
    
  
    let additionalPath = '';
      
    if (urlParts.length > 1) {

      additionalPath = urlParts[2];
    }
    if (id) {
      this.recipeService.getCommentsByRecipeId(username, id, additionalPath)?.subscribe(comments => {
        this.comments = comments;
      });
    }
  }

  addComment() {
    const idParam = this.route.snapshot.paramMap.get('id');
    const id = idParam ? +idParam : 0;
    const userId = this.authService.userId();
    const urlParts = this.route.snapshot.url.map(segment => segment.path);
    const username = this.authService.username();
   

   let additionalPath = '';
    
    if (urlParts.length > 1) {
      
      additionalPath = urlParts[2];
    }
    

    if (id) {
      this.recipeService.addCommentToRecipe(id, userId,username, { content: this.newComment }, additionalPath)?.subscribe(comment => {
        this.comments.push(comment);
        this.newComment = '';
        location.reload();
    });
    
    }
  }

  getReviews(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    const id = idParam ? +idParam : 0;
  
    const userId = this.authService.userId();
    const urlParts = this.route.snapshot.url.map(segment => segment.path);
    const username = this.authService.username();

  
    let additionalPath = '';
      
    if (urlParts.length > 1) {
      additionalPath = urlParts[2];
    }
      

  
    if (id) {
      this.recipeService.getReviewsByRecipeId(username, id, additionalPath)?.subscribe(reviews => {
        this.reviews = reviews;
      });
    }
  }

  addReview(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    const id = idParam ? +idParam : 0;

    const userId = this.authService.userId();
    const urlParts = this.route.snapshot.url.map(segment => segment.path);
    const username = this.authService.username();
  

   let additionalPath = '';
    
    if (urlParts.length > 1) {

      additionalPath = urlParts[2];
    }
    

    if (id) {
      
      this.recipeService.addReviewToRecipe(id, this.newReview, userId, username, additionalPath )?.subscribe(review => {
        this.reviews.push(review);
        this.newReview = {rating: 0 }; 
        location.reload();
      });
    }
  }

  setRating(rating: number): void {
    this.newReview.rating = rating;
  }


getStarArray(rating: number): boolean[] {
    const starArray: boolean[] = [];
    for (let i = 0; i < 5; i++) {
        starArray.push(i < rating);
    }
    return starArray;
}


calculateAverageRating(): string {
  if (!this.reviews || this.reviews.length === 0) {
      return 'N/A';
  }

  const ratings = this.reviews
    .filter(review => review !== null) 
    .map(review => parseInt(review.rating, 10));

  const allRatingsValid = ratings.every(rating => !isNaN(rating) && Number.isInteger(rating));

  if (!allRatingsValid) {
      return 'N/A'; 
  }

  const sum = ratings.reduce((acc, rating) => acc + rating, 0);

  const average = sum / ratings.length;

  return average.toFixed(2); 
}



addOrUpdateReview() {
  const idParam = this.route.snapshot.paramMap.get('id');
  const id = idParam ? +idParam : 0;

  const userId = this.authService.userId();
  const urlParts = this.route.snapshot.url.map(segment => segment.path);
  const username = this.authService.username();



  let additionalPath = '';

  if (urlParts.length > 1) {
    additionalPath = urlParts[2];
  }

  if (id) {

    this.recipeService.checkReview(userId, id, additionalPath)?.subscribe(response => {
      if (response) {
        this.isReviewed = true;
        this.reviewsUpdate = [response];
      } else {
        
      }
    });
  }
  console.log("gggg", this.isReviewed);
}

updateRating(newRating: number) {
  if (this.reviewsUpdate.length > 0) {
    
    this.reviewsUpdate[0].rating = newRating;
    this.newReview.rating = newRating.toString();

  } 
}

updateReview(): void {
  const idParam = this.route.snapshot.paramMap.get('id');
  const id = idParam ? +idParam : 0;

  const userId = this.authService.userId();
  const urlParts = this.route.snapshot.url.map(segment => segment.path);
  const username = this.authService.username();

  let additionalPath = '';

  if (urlParts.length > 1) {
    additionalPath = urlParts[2];
  }


  if (id) {

    this.recipeService.updateReviewOfRecipe(id, this.newReview, userId, username, additionalPath)?.subscribe(updatedReview => {
      location.reload();
    });
  }
}



}  