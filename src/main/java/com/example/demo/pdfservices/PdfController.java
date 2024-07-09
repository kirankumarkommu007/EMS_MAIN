package com.example.demo.pdfservices;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PdfController {

    private final PdfGenerationService pdfGenerationService;

    public PdfController(PdfGenerationService pdfGenerationService) {
        this.pdfGenerationService = pdfGenerationService;
    }

    @GetMapping("/generatePdf")
    @ResponseBody
    public String generatePdf(@RequestParam(defaultValue = "Baeldung") String to) {
        try {
            pdfGenerationService.generatePdf(to);
            return "PDF generated successfully. Check your home directory.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating PDF: " + e.getMessage();
        }
    }
}
