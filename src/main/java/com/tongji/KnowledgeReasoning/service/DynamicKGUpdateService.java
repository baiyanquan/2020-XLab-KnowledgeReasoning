package com.tongji.KnowledgeReasoning.service;

import com.sun.xml.fastinfoset.util.StringArray;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import com.tongji.KnowledgeReasoning.entity.DynamicDataEntity;
import com.tongji.KnowledgeReasoning.util.Operations;
import javassist.bytecode.ByteArray;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.rdf.model.impl.ResourceImpl;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.VCARD;
import org.hibernate.result.Output;

import java.io.*;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.Date;
import java.util.Vector;

/**
 * @program: 2020-XLab-KnowledgeReasoning
 * @description:
 * @author: 1754060 Zhe Zhang
 * @create: 2020/04/13
 **/
public class DynamicKGUpdateService {
    public static void WriteDynamicDataToModel(Model model, DynamicDataEntity dynamicDataEntity){
        Resource dynamicDataClass = new ResourceImpl(dynamicDataEntity.getType());
        Resource dynamicData = model.createResource("http://10.60.38.181/pod/" + dynamicDataEntity.getName(), dynamicDataClass);
        dynamicData.addProperty(new PropertyImpl("http://10.60.38.181/pod_rel/startTime"), dynamicDataEntity.getStartTime())
                .addProperty(new PropertyImpl("http://10.60.38.181/pod_rel/endTime"), dynamicDataEntity.getEndTime())
                .addProperty(new PropertyImpl("http://10.60.38.181/pod_rel/name"), dynamicDataEntity.getName())
                .addProperty(new PropertyImpl("http://10.60.38.181/pod_rel/phase"), dynamicDataEntity.getStatus())
                .addProperty(new PropertyImpl("http://10.60.38.181/pod_rel/contains"), new ResourceImpl(DynamicDataEntity.getContainsInfo()))
                .addProperty(new PropertyImpl("http://10.60.38.181/pod_rel/provides"), new ResourceImpl(DynamicDataEntity.getProvideInfo()))
                .addProperty(new PropertyImpl("http://10.60.38.181/pod_rel/deploy_in"), new ResourceImpl(dynamicDataEntity.getDeploy_inInfo()));
    }

    public static String RefactorSyntax(ByteArrayOutputStream outputStream) throws IOException {
        Vector<String> strVec = new Vector<String>();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String str = null;
        while((str = bufferedReader.readLine()) != null)
        {
            if(!str.contains("<http://") && (str.contains("<") && str.contains(">"))){
                if(str.contains(" a ")){
                    str = str.replace("<", ":");
                } else {
                    str = str.replace("<", "");
                }
                str = str.replace(">", "");
            }
            strVec.add(str);
        }

        return StringUtils.join(strVec, '\n');
    }

    public static void main(String[] args) throws IOException {
        Model model = ModelFactory.createDefaultModel();
        model.setNsPrefix("pod_rel", "http://10.60.38.181/pod_rel/");
        model.setNsPrefix("pod", "http://10.60.38.181/pod/");
        model.setNsPrefix("", "http://10.60.38.181/");

        //模拟接受json数据（已转换格式）
        DynamicDataEntity sample = DynamicDataEntity.getSample();

        //写入空model
        WriteDynamicDataToModel(model, sample);

        //处理syntax
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        model.write(outputStream, "TURTLE");
        String refactoredStr = RefactorSyntax(outputStream);

        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("data/O&M_KG.ttl", true)
            ));
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            out.write("\n\n# Fresh knowledge [" + formatter.format(date) + "]\n");
            out.write(refactoredStr);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        model.write(new FileOutputStream("data/new.ttl"), "TURTLE");
    }
}
