package com.tongji.KnowledgeReasoning.util;

import org.apache.jena.rdf.model.*;

/**
 * @program: 2020-XLab-KnowledgeReasoning
 * @description:
 * @author: 1754060 Zhe Zhang
 * @create: 2020/04/13
 **/
public class Operations {
    public static void OutputAllTriple(Model model){
        StmtIterator iter = model.listStatements();
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();
            Resource subject = stmt.getSubject();
            Property predicate = stmt.getPredicate();
            RDFNode object = stmt.getObject();
            System.out.print(subject.toString()); System.out.print(" " + predicate.toString() + " ");

            if (object instanceof Resource) { System.out.print(object.toString());
            } else {
                System.out.print(" \"" + object.toString() + "\"");
            }
            System.out.println(" .");
        }
    }
}
