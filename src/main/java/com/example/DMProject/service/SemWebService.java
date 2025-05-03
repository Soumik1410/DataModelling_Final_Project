package com.example.DMProject.service;

import com.example.DMProject.dto.AdminLoginRequest;
import com.example.DMProject.dto.ApplicantLoginRequest;
import com.example.DMProject.dto.CompanyLoginRequest;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.QuerySolution;

import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

@Service
public class SemWebService {

    public String linkOntologies(String filePathA, String filePathB, String token) throws Exception{
        try {
            if(!token.equals("admin"))
                return "Invalid token";

            // Load models
            OntModel modelA = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
            OntModel modelB = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

            try (InputStream inA = new FileInputStream(filePathA);
                 InputStream inB = new FileInputStream(filePathB)) {
                modelA.read(inA, null, "RDF/XML");
                modelB.read(inB, null, "RDF/XML");
            }


            // Merge models
            OntModel mergedModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
            mergedModel.add(modelA).add(modelB);

            String ns1 = "http://www.semanticweb.org/soumik/ontologies/2025/3/naukri-version-1#";
            String ns2 = "http://www.semanticweb.org/ak960/ontologies/2025/4/monstor_v5#";

            // Class equivalences
            OntClass applicantsA = mergedModel.getOntClass(ns1 + "Applicants");
            OntClass applicantsB = mergedModel.getOntClass(ns2 + "Applicant");
            if (applicantsA != null && applicantsB != null) {
                mergedModel.add(applicantsA, OWL.equivalentClass, applicantsB);
            }
            OntClass companyA = mergedModel.getOntClass(ns1 + "Companies");
            OntClass companyB = mergedModel.getOntClass(ns2 + "Company");
            if (companyA != null && companyB != null) {
                mergedModel.add(companyA, OWL.equivalentClass, companyB);
            }
            OntClass jobPostingsA = mergedModel.getOntClass(ns1 + "Job_Postings");
            OntClass jobPostingsB = mergedModel.getOntClass(ns2 + "Job_Posting");
            if (jobPostingsA != null && jobPostingsB != null) {
                mergedModel.add(jobPostingsA, OWL.equivalentClass, jobPostingsB);
            }
            OntClass educationA = mergedModel.getOntClass(ns1 + "Educations");
            OntClass educationB = mergedModel.getOntClass(ns2 + "Education");
            if (educationA != null && educationB != null) {
                mergedModel.add(educationA, OWL.equivalentClass, educationB);
            }
            OntClass employmentA = mergedModel.getOntClass(ns1 + "Employments");
            OntClass employmentB = mergedModel.getOntClass(ns2 + "Experience");
            if (employmentA != null && employmentB != null) {
                mergedModel.add(employmentA, OWL.equivalentClass, employmentB);
            }

            // Printing equivalent classes
            StmtIterator iter = mergedModel.listStatements(null, OWL.equivalentClass, (RDFNode) null);
            while (iter.hasNext()) {
                Statement stmt = iter.nextStatement();
                Resource subject = stmt.getSubject();
                RDFNode object = stmt.getObject();
                if (subject.isURIResource() && object.isURIResource()) {
                    System.out.println("Equivalent classes: " + subject.getURI() + " ≡ " + object.asResource().getURI());
                }
            }

            // Property Equivalences
            OntProperty propA, propB;

            propA = mergedModel.getOntProperty(ns1 + "Applicants_Applied_To");
            propB = mergedModel.getOntProperty(ns2 + "appliedFor");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Applicants_Education_History");
            propB = mergedModel.getOntProperty(ns2 + "hasEducation");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Applicants_Employment_History");
            propB = mergedModel.getOntProperty(ns2 + "hasExperience");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Applicants_Age");
            propB = mergedModel.getOntProperty(ns2 + "Applicant_age");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Applicants_Email");
            propB = mergedModel.getOntProperty(ns2 + "Applicant_email");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Applicants_Gender");
            propB = mergedModel.getOntProperty(ns2 + "Applicant_gender");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Applicants_Key_Skills");
            propB = mergedModel.getOntProperty(ns2 + "Applicant_skillsList");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Applicants_Experience_In_Years");
            propB = mergedModel.getOntProperty(ns2 + "Applicant_totalWorkExperience");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Applicants_Mobile");
            propB = mergedModel.getOntProperty(ns2 + "Applicant_mobileNumber");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Applicants_Name");
            propB = mergedModel.getOntProperty(ns2 + "Applicant_name");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Applicants_Preferred_Locations");
            propB = mergedModel.getOntProperty(ns2 + "Applicant_preferredLocation");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Applicants_Surrogate_ID");
            propB = mergedModel.getOntProperty(ns2 + "Applicant_id");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }


            propA = mergedModel.getOntProperty(ns1 + "Educations_College_or_School_Name");
            propB = mergedModel.getOntProperty(ns2 + "Education_university");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Educations_Degree");
            propB = mergedModel.getOntProperty(ns2 + "Education_degree");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Educations_Percentage");
            propB = mergedModel.getOntProperty(ns2 + "Education_percent");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Educations_Surrogate_ID");
            propB = mergedModel.getOntProperty(ns2 + "Education_id");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Educations_Year");
            propB = mergedModel.getOntProperty(ns2 + "Education_graduationYear");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }


            propA = mergedModel.getOntProperty(ns1 + "Employments_Company");
            propB = mergedModel.getOntProperty(ns2 + "Experience_companyName");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Employments_Job_Role");
            propB = mergedModel.getOntProperty(ns2 + "Experience_title");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Employments_Surrogate_ID");
            propB = mergedModel.getOntProperty(ns2 + "Experience_id");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }


            propA = mergedModel.getOntProperty(ns1 + "Companies_About_Company");
            propB = mergedModel.getOntProperty(ns2 + "Company_aboutCompany");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Companies_Location");
            propB = mergedModel.getOntProperty(ns2 + "Company_headOfficeLocation");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Companies_Name");
            propB = mergedModel.getOntProperty(ns2 + "Company_name");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Companies_Surrogate_ID");
            propB = mergedModel.getOntProperty(ns2 + "Company_id");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }


            propA = mergedModel.getOntProperty(ns1 + "Jobs_Posting_Company");
            propB = mergedModel.getOntProperty(ns2 + "hasCompany");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Jobs_Posting_Applicant");
            propB = mergedModel.getOntProperty(ns2 + "hasApplicant");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Jobs_Department");
            propB = mergedModel.getOntProperty(ns2 + "Job_function");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Jobs_Industry");
            propB = mergedModel.getOntProperty(ns2 + "Job_industry");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Jobs_Job_Description");
            propB = mergedModel.getOntProperty(ns2 + "Job_jobDescription");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Jobs_Key_Skills");
            propB = mergedModel.getOntProperty(ns2 + "Job_skillList");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Jobs_Location");
            propB = mergedModel.getOntProperty(ns2 + "Job_location");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Jobs_Max_Experience_Years");
            propB = mergedModel.getOntProperty(ns2 + "Job_experienceMax");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Jobs_Min_Experience_Years");
            propB = mergedModel.getOntProperty(ns2 + "Job_experienceMin");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Jobs_Posting_Title");
            propB = mergedModel.getOntProperty(ns2 + "Job_jobTitle");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }
            propA = mergedModel.getOntProperty(ns1 + "Jobs_Surrogate_ID");
            propB = mergedModel.getOntProperty(ns2 + "Job_jobID");
            if (propA != null && propB != null) {
                mergedModel.add(propA, OWL.equivalentProperty, propB);
            }

            for (ExtendedIterator<OntProperty> it = mergedModel.listAllOntProperties(); it.hasNext(); ) {
                OntProperty prop = it.next();
                if (!prop.isAnon()) {
                    for (ExtendedIterator<? extends OntProperty> eqProps = prop.listEquivalentProperties(); eqProps.hasNext(); ) {
                        OntProperty eq = eqProps.next();
                        if (!eq.isAnon()) {
                            System.out.println("Equivalent properties: " + prop.getURI() + " ≡ " + eq.getURI());
                        }
                    }
                }
            }

            // Individual Equivalences
            Individual indivA, indivB;

            indivA = mergedModel.getIndividual(ns1 + "Paytm");
            indivB = mergedModel.getIndividual(ns2 + "Paytm");
            if (indivA != null && indivB != null) {
                mergedModel.add(indivA, OWL.sameAs, indivB);
            }
            indivA = mergedModel.getIndividual(ns1 + "JobPosting3");
            indivB = mergedModel.getIndividual(ns2 + "Job_109809283");
            if (indivA != null && indivB != null) {
                mergedModel.add(indivA, OWL.sameAs, indivB);
            }

            for (ExtendedIterator<Individual> it = mergedModel.listIndividuals(); it.hasNext(); ) {
                Individual ind = it.next();
                if (!ind.isAnon()) {
                    StmtIterator sameAsStmts = ind.listProperties(OWL.sameAs);
                    while (sameAsStmts.hasNext()) {
                        Statement stmt = sameAsStmts.nextStatement();
                        RDFNode obj = stmt.getObject();
                        if (obj.isResource() && !obj.asResource().isAnon()) {
                            System.out.println("Equivalent individuals: " + ind.getURI() + " ≡ " + obj.asResource().getURI());
                        }
                    }
                }
            }


            // Apply reasoning
            Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
            InfModel infModel = ModelFactory.createInfModel(reasoner, mergedModel);
            System.out.println("Inferred model created");

            /* Commented out for demo purposes as it takes too long
            FileOutputStream out = new FileOutputStream("inferred_model.rdf");
            infModel.write(out, "RDF/XML");
            System.out.println("Inferred model written to inferred_model.rdf");*/

            /*
            ValidityReport validity = infModel.validate();
            if (validity.isValid()) {
                System.out.println("The merged ontology is consistent.");
            } else {
                System.out.println("The merged ontology is INCONSISTENT. Issues:");
                for (Iterator<ValidityReport.Report> it = validity.getReports(); it.hasNext(); ) {
                    ValidityReport.Report report = it.next();
                    System.out.println("- " + report.getDescription());
                }
            }
            */
            //infModel.write(System.out, "RDF/XML");

            return "Successfully linked and reasoned the ontologies.";

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String loginAdmin(AdminLoginRequest request)
    {
        String username = request.name();
        String password = request.password();
        if(username.equals("admin") && password.equals("admin"))
            return "Login Successful";
        else
            return "Login Failed";
    }

    public String loginApplicant(ApplicantLoginRequest request) throws Exception
    {
        String username = request.name();
        String password = request.password();
        if((username.equals("Aman Bahuguna") && password.equals("password")) || (username.equals("Soumik Pal") && password.equals("password")))
        {
            Model model = ModelFactory.createDefaultModel();
            InputStream inA = new FileInputStream("C:\\IIITB MTech Sem 2\\DM\\FinalProject\\demo\\inferred_model.rdf");
            model.read(inA, null, "RDF/XML");

            String sparqlQuery =
                    "PREFIX ont: <http://www.semanticweb.org/soumik/ontologies/2025/3/naukri-version-1#>\n" +
                            "SELECT ?id WHERE {\n" +
                            "  ?applicant a ont:Applicants ;\n" +
                            "             ont:Applicants_Name \"" + username + "\" ;\n" +
                            "             ont:Applicants_Surrogate_ID ?id .\n" +
                            "}";

            Query query = QueryFactory.create(sparqlQuery);
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect();
                if (results.hasNext()) {
                    QuerySolution sol = results.nextSolution();
                    Literal idLiteral = sol.getLiteral("id");
                    return Integer.toString(idLiteral.getInt());
                } else {
                    return "Login Failed";
                }
            }
        }
        else
            return "Login Failed";
    }

    public List<Map<String, Object>> getAppliedJobs(String token) throws FileNotFoundException {
        Model model = ModelFactory.createDefaultModel();
        InputStream inA = new FileInputStream("C:\\IIITB MTech Sem 2\\DM\\FinalProject\\demo\\inferred_model.rdf");
        model.read(inA, null, "RDF/XML");

        int applicant_id = Integer.parseInt(token);

        String sparqlQuery = "PREFIX ont: <http://www.semanticweb.org/soumik/ontologies/2025/3/naukri-version-1#>\n" +
                "SELECT ?id " +
                "(GROUP_CONCAT(DISTINCT ?title; separator=\"|\") AS ?titles) " +
                "(GROUP_CONCAT(DISTINCT ?description; separator=\"|\") AS ?descriptions) " +
                "(GROUP_CONCAT(DISTINCT ?location; separator=\"|\") AS ?locations) " +
                "(GROUP_CONCAT(DISTINCT ?skills; separator=\"|\") AS ?skillsList) " +
                "(GROUP_CONCAT(DISTINCT ?department; separator=\"|\") AS ?departments) " +
                "(GROUP_CONCAT(DISTINCT ?industry; separator=\"|\") AS ?industries) \n" +
                "(GROUP_CONCAT(DISTINCT ?company; separator=\"|\") AS ?companies)\n" +
                "WHERE {\n" +
                "  ?applicant a ont:Applicants ;\n" +
                "             ont:Applicants_Surrogate_ID " + applicant_id + " ;\n" +
                "             ont:Applicants_Applied_To ?job .\n" +
                "  ?job a ont:Job_Postings ;\n" +
                "        ont:Jobs_Surrogate_ID ?id ;\n" +
                "        ont:Jobs_Posting_Title ?title ;\n" +
                "        ont:Jobs_Job_Description ?description ;\n" +
                "        ont:Jobs_Location ?location ;\n" +
                "        ont:Jobs_Key_Skills ?skills ;\n" +
                "        ont:Jobs_Department ?department ;\n" +
                "        ont:Jobs_Industry ?industry ;\n" +
                "        ont:Jobs_Posting_Company ?company .\n" +
                "}\n" +
                "GROUP BY ?id";

        Query query = QueryFactory.create(sparqlQuery);
        List<Map<String, Object>> jobDetails = new ArrayList<>();

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            while (results.hasNext()) {
                QuerySolution sol = results.nextSolution();
                Map<String, Object> job = new HashMap<>();
                Literal idLiteral = sol.getLiteral("id");
                try {
                    job.put("id", Integer.toString(idLiteral.getInt()));
                }
                catch(NumberFormatException e)
                {
                    job.put("id", sol.get("id").toString());
                }
                job.put("titles", Arrays.asList(sol.get("titles").toString().split("\\|")));
                job.put("descriptions", Arrays.asList(sol.get("descriptions").toString().split("\\|")));
                job.put("locations", Arrays.asList(sol.get("locations").toString().split("\\|")));
                job.put("skills", Arrays.asList(sol.get("skillsList").toString().split("\\|")));
                job.put("departments", Arrays.asList(sol.get("departments").toString().split("\\|")));
                job.put("industries", Arrays.asList(sol.get("industries").toString().split("\\|")));
                job.put("company", Arrays.asList(sol.get("companies").toString().split("\\|")));
                jobDetails.add(job);
            }
        }

        return jobDetails;
    }

    public List<Map<String, Object>> getAvailableJobs(String token) throws FileNotFoundException {
        Model model = ModelFactory.createDefaultModel();
        InputStream inA = new FileInputStream("C:\\IIITB MTech Sem 2\\DM\\FinalProject\\demo\\inferred_model.rdf");
        model.read(inA, null, "RDF/XML");

        int applicant_id = Integer.parseInt(token);

        String sparqlQuery = "PREFIX ont: <http://www.semanticweb.org/soumik/ontologies/2025/3/naukri-version-1#>\n" +
                "\n" +
                "SELECT ?id \n" +
                "       (GROUP_CONCAT(DISTINCT ?title; separator=\"|\") AS ?titles)\n" +
                "       (GROUP_CONCAT(DISTINCT ?description; separator=\"|\") AS ?descriptions)\n" +
                "       (GROUP_CONCAT(DISTINCT ?location; separator=\"|\") AS ?locations)\n" +
                "       (GROUP_CONCAT(DISTINCT ?skills; separator=\"|\") AS ?skillsList)\n" +
                "       (GROUP_CONCAT(DISTINCT ?department; separator=\"|\") AS ?departments)\n" +
                "       (GROUP_CONCAT(DISTINCT ?industry; separator=\"|\") AS ?industries)\n" +
                "       (GROUP_CONCAT(DISTINCT ?company; separator=\"|\") AS ?companies)\n" +
                "WHERE {\n" +
                "  ?job a ont:Job_Postings ;\n" +
                "       ont:Jobs_Surrogate_ID ?id ;\n" +
                "       ont:Jobs_Posting_Title ?title ;\n" +
                "       ont:Jobs_Job_Description ?description ;\n" +
                "       ont:Jobs_Location ?location ;\n" +
                "       ont:Jobs_Key_Skills ?skills ;\n" +
                "       ont:Jobs_Department ?department ;\n" +
                "       ont:Jobs_Industry ?industry ;\n" +
                "       ont:Jobs_Posting_Company ?company .\n" +
                "\n" +
                "  FILTER NOT EXISTS {\n" +
                "    ?applicant a ont:Applicants ;\n" +
                "               ont:Applicants_Surrogate_ID " + applicant_id + " ;\n" +
                "               ont:Applicants_Applied_To ?job .\n" +
                "  }\n" +
                "}\n" +
                "GROUP BY ?id\n";

        Query query = QueryFactory.create(sparqlQuery);
        List<Map<String, Object>> jobDetails = new ArrayList<>();

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            while (results.hasNext()) {
                QuerySolution sol = results.nextSolution();
                Map<String, Object> job = new HashMap<>();
                Literal idLiteral = sol.getLiteral("id");
                try {
                    job.put("id", Integer.toString(idLiteral.getInt()));
                }
                catch(NumberFormatException e)
                {
                    job.put("id", sol.get("id").toString());
                }
                job.put("titles", Arrays.asList(sol.get("titles").toString().split("\\|")));
                job.put("descriptions", Arrays.asList(sol.get("descriptions").toString().split("\\|")));
                job.put("locations", Arrays.asList(sol.get("locations").toString().split("\\|")));
                job.put("skills", Arrays.asList(sol.get("skillsList").toString().split("\\|")));
                job.put("departments", Arrays.asList(sol.get("departments").toString().split("\\|")));
                job.put("industries", Arrays.asList(sol.get("industries").toString().split("\\|")));
                job.put("company", Arrays.asList(sol.get("companies").toString().split("\\|")));
                jobDetails.add(job);
            }
        }

        return jobDetails;
    }

    public String loginCompany(CompanyLoginRequest request) throws FileNotFoundException {
        String username = request.name();
        String password = request.password();
        if((username.equals("Fortis") && password.equals("fortis"))
                || (username.equals("Mariott") && password.equals("mariott"))
                || (username.equals("Paytm") && password.equals("paytm"))
                || (username.equals("SAP") && password.equals("sap"))
                || (username.equals("Google") && password.equals("google"))
                || (username.equals("Amazon") && password.equals("amazon"))
                || (username.equals("Bajaj") && password.equals("bajaj"))
                || (username.equals("Infosys") && password.equals("infosys"))
                || (username.equals("Unacademy") && password.equals("unacademy"))){

            Model model = ModelFactory.createDefaultModel();
            InputStream inA = new FileInputStream("C:\\IIITB MTech Sem 2\\DM\\FinalProject\\demo\\inferred_model.rdf");
            model.read(inA, null, "RDF/XML");

            String sparqlQuery =
                    "PREFIX ont: <http://www.semanticweb.org/soumik/ontologies/2025/3/naukri-version-1#>\n" +
                            "SELECT ?id WHERE {\n" +
                            "  ?company a ont:Companies ;\n" +
                            "             ont:Companies_Name \"" + username + "\" ;\n" +
                            "             ont:Companies_Surrogate_ID ?id .\n" +
                            "}";

            Query query = QueryFactory.create(sparqlQuery);
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect();
                if (results.hasNext()) {
                    QuerySolution sol = results.nextSolution();
                    Literal idLiteral = sol.getLiteral("id");
                    return Integer.toString(idLiteral.getInt());
                } else {
                    return "Login Failed";
                }
            }
        }
        else
            return "Login Failed";
    }

    public List<Map<String, Object>> getPostedJobs(String token) throws FileNotFoundException {
        Model model = ModelFactory.createDefaultModel();
        InputStream inA = new FileInputStream("C:\\IIITB MTech Sem 2\\DM\\FinalProject\\demo\\inferred_model.rdf");
        model.read(inA, null, "RDF/XML");

        int company_id = Integer.parseInt(token);

        String sparqlQuery = "PREFIX ont: <http://www.semanticweb.org/soumik/ontologies/2025/3/naukri-version-1#>\n" +
                "SELECT ?id " +
                "(GROUP_CONCAT(DISTINCT ?title; separator=\"|\") AS ?titles) " +
                "(GROUP_CONCAT(DISTINCT ?description; separator=\"|\") AS ?descriptions) " +
                "(GROUP_CONCAT(DISTINCT ?location; separator=\"|\") AS ?locations) " +
                "(GROUP_CONCAT(DISTINCT ?skills; separator=\"|\") AS ?skillsList) " +
                "(GROUP_CONCAT(DISTINCT ?department; separator=\"|\") AS ?departments) " +
                "(GROUP_CONCAT(DISTINCT ?industry; separator=\"|\") AS ?industries) \n" +
                "(GROUP_CONCAT(DISTINCT ?companyName; separator=\"|\") AS ?companies)\n" +
                "WHERE {\n" +
                "  ?company a ont:Companies ;\n" +
                "             ont:Companies_Surrogate_ID " + company_id + " .\n" +
                "  ?job a ont:Job_Postings ;\n" +
                "        ont:Jobs_Surrogate_ID ?id ;\n" +
                "        ont:Jobs_Posting_Title ?title ;\n" +
                "        ont:Jobs_Job_Description ?description ;\n" +
                "        ont:Jobs_Location ?location ;\n" +
                "        ont:Jobs_Key_Skills ?skills ;\n" +
                "        ont:Jobs_Department ?department ;\n" +
                "        ont:Jobs_Industry ?industry ;\n" +
                "        ont:Jobs_Posting_Company ?company .\n" +
                "  ?company ont:Companies_Name ?companyName .\n" +
                "}\n" +
                "GROUP BY ?id";

        Query query = QueryFactory.create(sparqlQuery);
        List<Map<String, Object>> jobDetails = new ArrayList<>();

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            while (results.hasNext()) {
                QuerySolution sol = results.nextSolution();
                Map<String, Object> job = new HashMap<>();
                Literal idLiteral = sol.getLiteral("id");
                try {
                    job.put("id", Integer.toString(idLiteral.getInt()));
                }
                catch(NumberFormatException e)
                {
                    job.put("id", sol.get("id").toString());
                }
                job.put("titles", Arrays.asList(sol.get("titles").toString().split("\\|")));
                job.put("descriptions", Arrays.asList(sol.get("descriptions").toString().split("\\|")));
                job.put("locations", Arrays.asList(sol.get("locations").toString().split("\\|")));
                job.put("skills", Arrays.asList(sol.get("skillsList").toString().split("\\|")));
                job.put("departments", Arrays.asList(sol.get("departments").toString().split("\\|")));
                job.put("industries", Arrays.asList(sol.get("industries").toString().split("\\|")));
                job.put("company", Arrays.asList(sol.get("companies").toString().split("\\|")));
                jobDetails.add(job);
            }
        }

        return jobDetails;
    }

    public List<Map<String, Object>> getJobApplicants(int job_id, String token) throws FileNotFoundException {
        Model model = ModelFactory.createDefaultModel();
        InputStream inA = new FileInputStream("C:\\IIITB MTech Sem 2\\DM\\FinalProject\\demo\\inferred_model.rdf");
        model.read(inA, null, "RDF/XML");

        int company_id = Integer.parseInt(token);
        if(company_id < 1000 || company_id > 1999)
            return null;

        String sparqlQuery = "PREFIX ont: <http://www.semanticweb.org/soumik/ontologies/2025/3/naukri-version-1#>\n" +
                "SELECT ?id \n" +
                "(GROUP_CONCAT(DISTINCT ?name; separator=\"|\") AS ?names)\n" +
                "(GROUP_CONCAT(DISTINCT ?email; separator=\"|\") AS ?emails)\n" +
                "(GROUP_CONCAT(DISTINCT ?mobile; separator=\"|\") AS ?mobiles)\n" +
                "(GROUP_CONCAT(DISTINCT ?age; separator=\"|\") AS ?ages)\n" +
                "(GROUP_CONCAT(DISTINCT ?gender; separator=\"|\") AS ?genders)\n" +
                "(GROUP_CONCAT(DISTINCT ?experience; separator=\"|\") AS ?experiences)\n" +
                "WHERE {\n" +
                "  ?job a ont:Job_Postings ;\n" +
                "       ont:Jobs_Surrogate_ID ?jobIdVal .\n" +
                "\n" +
                "  ?applicant a ont:Applicants ;\n" +
                "             ont:Applicants_Surrogate_ID ?id ;\n" +
                "             ont:Applicants_Name ?name ;\n" +
                "             ont:Applicants_Email ?email ;\n" +
                "             ont:Applicants_Mobile ?mobile ;\n" +
                "             ont:Applicants_Age ?age ;\n" +
                "             ont:Applicants_Gender ?gender ;\n" +
                "             ont:Applicants_Experience_In_Years ?experience ;\n" +
                "             ont:Applicants_Applied_To ?job .\n" +
                "  FILTER(str(?jobIdVal) = \"" + job_id + "\")\n" +
                "}\n" +
                "GROUP BY ?id\n";

        Query query = QueryFactory.create(sparqlQuery);
        List<Map<String, Object>> applicantDetails = new ArrayList<>();

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            while (results.hasNext()) {
                QuerySolution sol = results.nextSolution();
                Map<String, Object> applicant = new HashMap<>();
                Literal idLiteral = sol.getLiteral("id");
                try {
                    applicant.put("id", Integer.toString(idLiteral.getInt()));
                }
                catch(NumberFormatException e)
                {
                    applicant.put("id", sol.get("id").toString());
                }
                applicant.put("name", Arrays.asList(sol.get("names").toString().split("\\|")));
                applicant.put("email", Arrays.asList(sol.get("emails").toString().split("\\|")));
                applicant.put("phone", Arrays.asList(sol.get("mobiles").toString().split("\\|")));
                applicant.put("age", Arrays.asList(sol.get("ages").toString().split("\\|")));
                applicant.put("gender", Arrays.asList(sol.get("genders").toString().split("\\|")));
                applicant.put("experience_in_years", Arrays.asList(sol.get("experiences").toString().split("\\|")));
                applicantDetails.add(applicant);
            }
        }

        return applicantDetails;
    }
}