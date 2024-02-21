import { Ingredient } from "./ingredient";
import { Instruction } from "./instruction";


export class Recipe {
    title: string;
    image: string;
    ingredients: Ingredient[] = [];
    instructions: Instruction[] = [];

    constructor(title: string, image: string, ingredients: Ingredient[], instructions: Instruction[]) {
        this.title = title;
        this.image = image;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }
}
