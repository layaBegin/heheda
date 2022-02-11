package com.tanhua.manage.controller;

import com.tanhua.manage.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class AnalysisController {


    @Autowired
    private AnalysisService analysisService;

    @GetMapping("/summary")
    public ResponseEntity<Object> getSummary(){

        return analysisService.getSummary();
    }
}
