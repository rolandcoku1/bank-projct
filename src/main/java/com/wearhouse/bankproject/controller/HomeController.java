package com.wearhouse.bankproject.controller;

import com.wearhouse.bankproject.service.DatabaseCheckService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final DatabaseCheckService databaseCheckService;

    public HomeController(DatabaseCheckService databaseCheckService) {
        this.databaseCheckService = databaseCheckService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("operationalStatus", databaseCheckService.checkOperational());
        model.addAttribute("historicalStatus", databaseCheckService.checkHistorical());
        model.addAttribute("riskStatus", databaseCheckService.checkRisk());
        return "index";
    }
}
