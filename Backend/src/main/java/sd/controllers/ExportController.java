package sd.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sd.services.ExportService;

import java.io.IOException;

@RestController
@RequestMapping("/")
public class ExportController {

    @Autowired
    private ExportService exportService;

    @GetMapping("/export")
    public String exportData() {
        try {
            exportService.exportToCSV();
            return "Export realizat cu succes!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Eroare în timpul exportului!";
        }
    }
}

