import { Component } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { RecipeService } from '../services/recipe.service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';


@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.scss']
})


export class HomePageComponent {
  email: string = '';
  exportMessage: string = '';
  newsletterVisible = false;
  showContactForm: boolean = false;
  contactName: string = '';
  contactEmail: string = '';
  contactMessage: string = '';
  recipes: any[] = [];
  private unsubscribe$ = new Subject<void>();

  constructor(private recipeService: RecipeService, private toastr: ToastrService) { }

  capitalizeFirstLetter(text: string): string {
    return text.charAt(0).toUpperCase() + text.slice(1);
  }

  showNewsletterForm() {
    this.newsletterVisible = true;
  }


  showContact() {
   // this.showNewsletterForm = false;
    this.showContactForm = true;
  }
  subscribeEmail(): void {
    // Verificare validitate adresa de email
    if (!this.validateEmail(this.email)) {
      this.toastr.error('Please enter a valid email address!', 'Error');
      return;
    }
  
    this.recipeService.email(this.email)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          console.error('Full error response:', error);
          this.toastr.error('An error occurred while subscribing to the newsletter. Please try again later.', 'Error');
          return throwError(error);
        })
      )
      .subscribe(
        (response: string) => {
          console.log('Response from server:', response);
          if (response === 'Subscribed successfully') {
            this.toastr.success(response, 'Success');
            this.email = '';
          } else {
            this.toastr.error('An error occurred while subscribing to the newsletter. Please try again later.', 'Error');
          }
        },
        (error: any) => {
          console.error('Error subscribing to newsletter:', error);
          this.toastr.error('An error occurred while subscribing to the newsletter. Please try again later.', 'Error');
        }
      );
  }
  

  sendContactMessage() {
    console.log(`Name: ${this.contactName}, Email: ${this.contactEmail}, Message: ${this.contactMessage}`);
    this.toastr.success('Your message has been sent successfully!', 'Success');
    this.contactName = ''; 
    this.contactEmail = '';
    this.contactMessage = '';
  }

  exportData(): void {
    this.recipeService.exportData().subscribe(
      message => {
        this.exportMessage = message;
      },
      error => {
        console.error('Error exporting data:', error);
        this.toastr.error('An error occurred while exporting data. Please try again later.', 'Error');
      }
    );
  }

  // Funcție pentru validare adresa de email
  validateEmail(email: string): boolean {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
  }



  ngOnInit(): void {
    this.getTopRecipes();
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  getTopRecipes(): void {
    this.recipeService.getAllRecipes()
      .pipe(
        takeUntil(this.unsubscribe$)
      )
      .subscribe(recipes => this.recipes = recipes.slice(0, 20));
  }
}
