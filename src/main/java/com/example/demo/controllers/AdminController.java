package com.example.demo.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Controller
@Tag(name = "Admin Controller", description = "Controller for managing employee data by admin")
public class AdminController {

   

    @Operation(summary = "View admin home page", description = "Displays the admin home page with a list of employees")
    @GetMapping("/admin/home")
    public String page() {     
        return "views/pages/homepage";
    }

   
}
