package com.wearhouse.bankproject.controller;

import com.wearhouse.bankproject.service.DataSeederService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final DataSeederService seederService;

    public AdminController(DataSeederService seederService) {
        this.seederService = seederService;
    }

    @GetMapping("/seed-data")
    public String seedData() {
        seederService.seedDatabase();
        return "Database Populated! Now run /run-full-pipeline to see the results.";
    }
}
