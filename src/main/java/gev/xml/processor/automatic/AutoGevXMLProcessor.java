package gev.xml.processor.automatic;

import gev.writer.IGevObjectSaver;
import gev.xml.processor.GevXmlProcessorAbstract;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.json.XML;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import utils.LogUtils;

import javax.inject.Inject;
import java.io.File;
import java.util.List;
/**
 * Created by Imona Andoid on 23.11.2017.
 */
@Service(value = "AutoGevXMLProcessor")
public class AutoGevXMLProcessor extends GevXmlProcessorAbstract {
    @Inject
    @Qualifier(value = "ExecuterServiceRequestPoolSaver")
    private IGevObjectSaver iGevObjectSaver;

    @Override
    public void processXmlFile(File xmlFile) throws Exception {
        long startTime = System.currentTimeMillis();
        try {
            Element rootNode = parseXmlFile(xmlFile);
            String entityName = parseEntityNameFromFileName(xmlFile.getName());
            String veriTarihi = parseVeriTarihiFromFileName(xmlFile.getName());
            String sirkretNo = rootNode.getAttributeValue("sir");
            List list = rootNode.getChildren(entityName);
            Boolean done = true;
            int step = 500;
            for (int i = 0; i + step < list.size(); i = i + step) {
                String resultJsonString = prepareForPunchOfData(i, step, list, entityName);
                String response = iGevObjectSaver.saveObject(entityName, sirkretNo, veriTarihi, resultJsonString);
                if (!response.equals("true")) {
                    done = false;
//                    throw new Exception("response error");
                }
            }


            int max = (list.size() % step);
            int start = (list.size() / step) * step;
            String resultJsonString = prepareForPunchOfData(start, max, list, entityName);
            String response = iGevObjectSaver.saveObject(entityName, sirkretNo, veriTarihi, resultJsonString);

            if (done) {
                //xmlFile.renameTo(new File(utils.ConstantUtils.BACKUP_GEV_FILES_PATH + xmlFile.getName()));
            } else {
                //TODO : CALL ROLL BACLL

            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.warning("Error : Processing xmlFile" + xmlFile.getName() + ">" + e.getMessage(), xmlFile.getName());
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("totalTime >>> " + totalTime);
    }

    private static Element parseXmlFile(File xmlFile) throws Exception {
        SAXBuilder builder = new SAXBuilder();
        Document document = (Document) builder.build(xmlFile);
        Element rootNode = document.getRootElement();
        return rootNode;
    }

    private static String convertNodeToJson(Element node) {
        XMLOutputter outp = new XMLOutputter();
        String objAsXml = outp.outputString(node);
        // System.out.println(objAsXml);
        String objAsJson = XML.toJSONObject(objAsXml).toString();
        return objAsJson;
    }

    private static String prepareForPunchOfData(int start, int max, List list, String entityName) {
        StringBuilder resultJsonString = new StringBuilder("{\"" + entityName + "_grp\":{\"" + entityName + "\":[");
        for (int j = start; j < start + max; j++) {
            String objAsJson = convertNodeToJson((Element) list.get(j));
            objAsJson = objAsJson.substring(objAsJson.indexOf(":{") + 1, objAsJson.length() - 1);
            if (j == start) {
                resultJsonString.append(objAsJson);
            } else {
                resultJsonString.append(",").append(objAsJson);
            }
        }
        resultJsonString.append("],\"sir\":0,\"vt\":20170517}}");
        return resultJsonString.toString();
    }
}
