package com.wearhouse.bankproject.controller;

import com.wearhouse.bankproject.service.EtlService;
import com.wearhouse.bankproject.service.RiskAnalysisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class EtlController {


    private final EtlService etlService;
    private final RiskAnalysisService riskService;

    public EtlController(EtlService etlService, RiskAnalysisService riskService) {
        this.etlService = etlService;
        this.riskService = riskService;
    }

    @GetMapping("/run-etl")
    public String triggerEtl() {
        try {
            etlService.runEtlProcess();
            return "ETL Process executed successfully. Check Historical DB.";
        } catch (Exception e) {
            e.printStackTrace();
            return "ETL Failed: " + e.getMessage();
        }
    }


    @GetMapping("/run-full-pipeline")
    public String runPipeline() {
        // Step 1: Ops -> Warehouse
        etlService.runEtlProcess();

        // Step 2: Warehouse -> Risk Mart
        riskService.generateDailyRiskReport();

        return "Full Pipeline Complete. Check the Risk Mart (Port 5442).";
    }
}