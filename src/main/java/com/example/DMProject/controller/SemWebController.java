package com.example.DMProject.controller;

import com.example.DMProject.dto.AdminLoginRequest;
import com.example.DMProject.dto.ApplicantLoginRequest;
import com.example.DMProject.dto.CompanyLoginRequest;
import com.example.DMProject.service.SemWebService;
import jakarta.validation.Valid;
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

    @PostMapping("/loginAdmin")
    public ResponseEntity<String> loginAdmin(@RequestBody @Valid AdminLoginRequest request) {
        String response = SemWebServiceObj.loginAdmin(request);
        if(!response.equals("Login Failed"))
            return ResponseEntity.ok("Logged in successfully as Admin\nToken: admin");
        else
            return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/loginApplicant")
    public ResponseEntity<String> loginApplicant(@RequestBody @Valid ApplicantLoginRequest request) {
        String response = SemWebServiceObj.loginApplicant(request);
        if(!response.equals("Login Failed"))
            return ResponseEntity.ok("Logged in successfully as Applicant\nToken: applicant");
        else
            return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/loginCompany")
    public ResponseEntity<String> loginCompany(@RequestBody @Valid CompanyLoginRequest request) {
        String response = SemWebServiceObj.loginCompany(request);
        if(!response.equals("Login Failed"))
            return ResponseEntity.ok("Logged in successfully as Company\nToken: company");
        else
            return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/link")
    public ResponseEntity<String> linkOntologies(@RequestParam String filePathA, @RequestParam String filePathB, @RequestHeader(value = "x-access-token") String token) throws Exception {
        String response = SemWebServiceObj.linkOntologies(filePathA, filePathB, token);
        if(response.equalsIgnoreCase("Successfully linked and reasoned the ontologies."))
            return ResponseEntity.ok(response);
        else
            return ResponseEntity.badRequest().body(response);

    }
}
