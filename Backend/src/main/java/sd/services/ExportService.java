package sd.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sd.dtos.AddedRecipeDTO;
import sd.dtos.ExtractedDTO;
import sd.entities.*;
import sd.repositories.AddedRecipeRepository;
import sd.repositories.ExtractedRecipeRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
@Service
public class ExportService {

    @Autowired
    private ExtractedRecipeRepository extractedRecipeRepository;

    @Autowired
    private AddedRecipeRepository addedRecipeRepository;

    public void exportToCSV() throws IOException {
        List<ExtractedRecipe> extractedRecipes = extractedRecipeRepository.findAll();
        List<AddedRecipe> addedRecipes = addedRecipeRepository.findAll();
        String csvFile = "exported_recipes.csv";

        try (
                Writer writer = new FileWriter(csvFile);
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("Type", "ID", "Title", "Image", "Ingredients", "Steps"))
        ) {
            for (ExtractedRecipe recipe : extractedRecipes) {
                ExtractedDTO dto = new ExtractedDTO(recipe);
                csvPrinter.printRecord("Extracted", dto.getId(), dto.getTitle(), dto.getImage(), dto.getIngredients(), dto.getInstructions());
            }

            for (AddedRecipe recipe : addedRecipes) {
                AddedRecipeDTO dto = new AddedRecipeDTO(recipe);
                csvPrinter.printRecord("Added", dto.getId(), dto.getTitle(), dto.getImage(), dto.getIngredients(), dto.getInstructions());
            }

            csvPrinter.flush();
        }
    }
}
