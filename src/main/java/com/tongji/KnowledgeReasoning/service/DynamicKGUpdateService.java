package com.tongji.KnowledgeReasoning.service;

import com.tongji.KnowledgeReasoning.entity.DynamicDataEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.rdf.model.impl.ResourceImpl;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * @program: 2020-XLab-KnowledgeReasoning
 * @description: 将动态数据的知识更新进知识图谱
 * @author: 1754060 Zhe Zhang
 * @create: 2020/04/13
 **/
public class DynamicKGUpdateService {
    public static OutputStream WriteDynamicDataToModel(DynamicDataEntity dynamicDataEntity){
        /**
         * @description: 将动态数据写入空的Mode
         *
         * @param dynamicDataEntity : 动态数据（已将json数据转化为实体对象）
         *
         * @return : java.io.OutputStream
         **/
        Model model = ModelFactory.createDefaultModel();
        model.setNsPrefix("pod_rel", "http://10.60.38.181/pod_rel/");
        model.setNsPrefix("pod", "http://10.60.38.181/pod/");
        model.setNsPrefix("", "http://10.60.38.181/");

        Resource dynamicDataClass = new ResourceImpl(dynamicDataEntity.getType());
        Resource dynamicData = model.createResource("http://10.60.38.181/pod/" + dynamicDataEntity.getName(), dynamicDataClass);
        dynamicData.addProperty(new PropertyImpl("http://10.60.38.181/pod_rel/startTime"), dynamicDataEntity.getStartTime())
                .addProperty(new PropertyImpl("http://10.60.38.181/pod_rel/endTime"), dynamicDataEntity.getEndTime())
                .addProperty(new PropertyImpl("http://10.60.38.181/pod_rel/name"), dynamicDataEntity.getName())
                .addProperty(new PropertyImpl("http://10.60.38.181/pod_rel/phase"), dynamicDataEntity.getStatus())
                .addProperty(new PropertyImpl("http://10.60.38.181/pod_rel/contains"), new ResourceImpl(DynamicDataEntity.getContainsInfo()))
                .addProperty(new PropertyImpl("http://10.60.38.181/pod_rel/provides"), new ResourceImpl(DynamicDataEntity.getProvideInfo()))
                .addProperty(new PropertyImpl("http://10.60.38.181/pod_rel/deploy_in"), new ResourceImpl(dynamicDataEntity.getDeploy_inInfo()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        model.write(outputStream, "TURTLE");
        return outputStream;
    }

    public static String RefactorSyntax(ByteArrayOutputStream outputStream) throws IOException {
        /**
         * @description: 修改直接写入model带来的符号错误 (<, >, : .etc)
         *
         * @param outputStream : 写入model后的输出流
         *
         * @return : java.lang.String
         **/
        Vector<String> strVec = new Vector<String>();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String str = null;
        while((str = bufferedReader.readLine()) != null)
        {
            if(str.contains("<http://") || str.isEmpty()){
                continue;
            }
            if(str.contains("<") && str.contains(">")){
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

    public static void UpdateDynamicData2KG(String originFilename, String str){
        /**
         * @description: 将动态数据写入原始数据
         *
         * @param originFilename : 原始数据文件名
         * @param str : 待写入的信息
         *
         * @return : void
         **/
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(originFilename, true)
            ));
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            out.write("\n\n# Fresh knowledge [" + formatter.format(date) + "]\n");
            out.write(str);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        //模拟接受json数据（已转换格式）
        DynamicDataEntity sample = DynamicDataEntity.getSample();

        //写入空model
        OutputStream outputStream = WriteDynamicDataToModel(sample);

        //处理syntax
        String refactoredStr = RefactorSyntax((ByteArrayOutputStream) outputStream);

        //更新进原数据位置
        UpdateDynamicData2KG("data/O&M_KG.ttl", refactoredStr);
    }
}
