import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TokenInterceptor } from './token-interceptor'; 
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MessagesComponent } from './messages/messages.component';
import { LoginComponent } from './login/login.component';
import { TitlecasePipe } from './titlecase.pipe';
import { MatDialogModule } from '@angular/material/dialog';
import { HomePageComponent } from './home-page/home-page.component';
import { RecipeComponent } from './recipe/recipe.component';
import { CommonModule } from '@angular/common';
import { RecipeDetailComponent } from './recipe/recipe-detail/recipe-detail.component';
import { ExtractRecipeComponent } from './extract-recipe/extract-recipe.component';
import { SearchIngredientsComponent } from './search-ingredients/search-ingredients.component';
import { MenuItemComponent } from './menu-item/menu-item.component';
import { AdvancedSearchComponent } from './advanced-search/advanced-search.component';
import { AdvancedDetailComponent } from './advanced-search/advanced-detail/advanced-detail.component';
import { RecipeDishgenComponent } from './recipe-dishgen/recipe-dishgen.component';
import { DishgenDetailComponent } from './recipe-dishgen/dishgen-detail/dishgen-detail.component';
import { AddRecipeComponent } from './add-recipe/add-recipe.component';
import { ToastrModule } from 'ngx-toastr';

@NgModule({
  imports: [
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule, 
    MatInputModule, 
    MatSelectModule,
    MatTableModule,
    MatIconModule,
    MatPaginatorModule,
    MatSortModule,
    MatDialogModule,
    CommonModule,
    BrowserModule,
    ToastrModule.forRoot({
      timeOut: 10000,
      positionClass: 'toast-bottom-right',
      preventDuplicates: true,
    }),

  ],
  declarations: [
    AppComponent,
    MessagesComponent,
    LoginComponent,
    TitlecasePipe,
    HomePageComponent,
    RecipeComponent,
    RecipeDetailComponent,
    ExtractRecipeComponent,
    SearchIngredientsComponent,
    MenuItemComponent,
    AdvancedSearchComponent,
    AdvancedDetailComponent,
    RecipeDishgenComponent,
    DishgenDetailComponent,
    AddRecipeComponent,

  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true,
    },
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
