package com.example.DMProject.controller;

import com.example.DMProject.service.SemWebService;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.vocabulary.OWL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.net.URL;

@RestController
@RequestMapping("/api/ontology")
public class SemWebController {

    @Autowired
    private SemWebService SemWebServiceObj;

    @GetMapping("/link")
    public ResponseEntity<String> linkOntologies(@RequestParam String filePathA, @RequestParam String filePathB) throws Exception {
        // Call the service method to link the ontologies
        String response = SemWebServiceObj.linkOntologies(filePathA, filePathB);
        if(response.equalsIgnoreCase("Successfully linked and reasoned the ontologies."))
            return ResponseEntity.ok(response);
        else
            return ResponseEntity.badRequest().body(response);

    }
}
