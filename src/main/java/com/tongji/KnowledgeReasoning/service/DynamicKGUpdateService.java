package com.tongji.KnowledgeReasoning.service;

import com.tongji.KnowledgeReasoning.util.Operations;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;

import java.io.InputStream;

/**
 * @program: 2020-XLab-KnowledgeReasoning
 * @description:
 * @author: 1754060 Zhe Zhang
 * @create: 2020/04/13
 **/
public class DynamicKGUpdateService {
    public static Model model;

    public static void main(String[] args) {
        // create an empty model
        model = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open( "data/O&M_KG.ttl" );
        if (in == null) {
            throw new IllegalArgumentException( "File: " + "data/O&M_KG.ttl" + " not found"); }
        // read the RDF/XML file
        model.read(in, "","TURTLE");


        Operations.OutputAllTriple(model);
    }
}
