package com.example.DMProject.controller;

import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.vocabulary.OWL;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.net.URL;

@RestController
@RequestMapping("/api/ontology")
public class SemWebController {

    @GetMapping("/link")
    public String linkOntologies(@RequestParam String urlA, @RequestParam String urlB) {
        try {
            OntModel modelA = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
            OntModel modelB = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

            try (InputStream inA = new URL(urlA).openStream();
                 InputStream inB = new URL(urlB).openStream()) {
                modelA.read(inA, null, "RDF/XML");
                modelB.read(inB, null, "RDF/XML");
            }

            OntModel merged = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
            merged.add(modelA).add(modelB);

            // Example: manually assert class equivalence
            String ns = "http://www.semanticweb.org/soumik/ontologies/2025/3/naukri-version-1#";
            OntClass c1 = merged.getOntClass(ns + "Job_Postings");
            OntClass c2 = merged.getOntClass(ns + "Job_Postings");
            if (c1 != null && c2 != null) {
                merged.add(c1, OWL.equivalentClass, c2);
            }

                    Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
            InfModel infModel = ModelFactory.createInfModel(reasoner, merged);

            // Print result to console
            infModel.write(System.out, "RDF/XML");

            return "Linked and reasoned RDF model created successfully.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
