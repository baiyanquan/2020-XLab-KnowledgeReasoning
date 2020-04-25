package com.tongji.KnowledgeReasoning.util;

import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @program: 2020-XLab-KnowledgeReasoning
 * @description:
 * @author: 1754060 Zhe Zhang
 * @create: 2020/04/13
 **/
public class Operations {
    public static Model model;

    public static void OutputAllTriple(Model model){
        StmtIterator iter = model.listStatements();
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();
            Resource subject = stmt.getSubject();
            Property predicate = stmt.getPredicate();
            RDFNode object = stmt.getObject();
            System.out.print(subject.toString());
            System.out.print(" " + predicate.toString() + " ");

            if (object instanceof Resource) { System.out.print(object.toString());
            } else {
                System.out.print(" \"" + object.toString() + "\"");
            }
            System.out.println(" .");
        }
    }

    public static void OutputAllTriple_to_ttl(Model model, String filename) throws FileNotFoundException {
        model.write(new FileOutputStream(filename),"TURTLE");
    }

    public static Model ReadModel(String filename){
        // create an empty model
        model = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open(filename);
        if (in == null) {
            throw new IllegalArgumentException( "File: " + filename + " not found"); }
        // read the RDF/XML file
        model.read(in, "","TURTLE");
        return model;
    }
}
