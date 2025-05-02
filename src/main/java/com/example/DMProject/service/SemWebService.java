package com.example.DMProject.service;

import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.vocabulary.OWL;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

@Service
public class SemWebService {

    public String linkOntologies(String filePathA, String filePathB) throws Exception{
        try {
            // Load models
            OntModel modelA = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
            OntModel modelB = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

            try (InputStream inA = new FileInputStream(filePathA); // Assuming urlA is a file path
                 InputStream inB = new FileInputStream(filePathB)) { // Assuming urlB is a file path
                modelA.read(inA, null, "RDF/XML");
                modelB.read(inB, null, "RDF/XML");
            }


            // Merge models
            OntModel mergedModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
            mergedModel.add(modelA).add(modelB);

            // Assert equivalence for classes (example)
            String ns = "http://www.semanticweb.org/soumik/ontologies/2025/3/naukri-version-1#";
            OntClass jobPostingsA = mergedModel.getOntClass(ns + "Job_Postings");
            OntClass jobPostingsB = mergedModel.getOntClass(ns + "Job_Postings");
            if (jobPostingsA != null && jobPostingsB != null) {
                mergedModel.add(jobPostingsA, OWL.equivalentClass, jobPostingsB);
            }

            // Apply reasoning
            Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
            InfModel infModel = ModelFactory.createInfModel(reasoner, mergedModel);

            // Output RDF/XML to console (you can customize this step)
            //infModel.write(System.out, "RDF/XML");

            return "Successfully linked and reasoned the ontologies.";

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}