package core;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.json.XML;
import utils.ConstantUtils;
import utils.LogUtils;
import utils.PropertyReader;
import utils.RestClient;

import java.io.File;
import java.util.List;

public class XmlUtils {
    public static void processXmlFile(File xmlFile) throws Exception {
        long startTime = System.currentTimeMillis();
        try {
            Element rootNode = parseXmlFile(xmlFile);
            String entityName = parseEntityNameFromFileName(xmlFile.getName());
            String veriTarihi = parseVeriTarihiFromFileName(xmlFile.getName());
            String sirkretNo = rootNode.getAttributeValue("sir");
            List list = rootNode.getChildren(entityName);
            Boolean done = true;
            for (int i = 0; i < list.size(); i++) {
                String objAsJson = convertNodeToJson((Element) list.get(i));
                String response = saveObject(entityName, sirkretNo, veriTarihi, objAsJson);
                if (!response.equals("true")) {
                    done = false;
//                    throw new Exception("response error");
                }
            }
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

    public static void processXmlFileV2(File xmlFile) throws Exception {
        long startTime = System.currentTimeMillis();
        try {
            Element rootNode = parseXmlFile(xmlFile);
            String entityName = parseEntityNameFromFileName(xmlFile.getName());
            String veriTarihi = parseVeriTarihiFromFileName(xmlFile.getName());
            String sirkretNo = rootNode.getAttributeValue("sir");
            List list = rootNode.getChildren(entityName);
            Boolean done = true;
            String fileAsJson = convertNodeToJson(rootNode);
            String response = saveObject(entityName, sirkretNo, veriTarihi, fileAsJson);

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

    public static void processXmlFileV3(File xmlFile) throws Exception {
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
                String response = saveObject(entityName, sirkretNo, veriTarihi, resultJsonString);
                if (!response.equals("true")) {
                    done = false;
//                    throw new Exception("response error");
                }
            }


            int max = (list.size() % step);
            int start = (list.size() / step) * step;
            String resultJsonString = prepareForPunchOfData(start, max, list, entityName);
            String response = saveObject(entityName, sirkretNo, veriTarihi, resultJsonString);

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

    private static String saveObject(String entityName, String sirkretNo, String veriTarihi, String objAsJson) throws Exception {
       /* String serverDomain = PropertyReader.getAppProperty(ConstantUtils.SERVER_DOMAIN);
        String serverOrgName = PropertyReader.getAppProperty(ConstantUtils.SERVER_ORG_NAME);
        String url = serverDomain + "/platform/rest/publicService/" + serverOrgName + "/eyf/gev_v2_" + entityName + "_xml_yukleme?veri_tarihi=" + veriTarihi + "&sirket_no=" + sirkretNo;
        RestClient restClient = new RestClient(url);
        restClient.setBody(objAsJson);
        restClient.setUserName(PropertyReader.getAppProperty(ConstantUtils.SERVER_USER_NAME));
        restClient.setPassword(PropertyReader.getAppProperty(ConstantUtils.SERVER_PASSWORD));
        String response = restClient.execute(RestClient.POST);
        response = response.replaceAll("\n", "").replaceAll("\r", "");

        System.out.println(url + "; Body >> " + objAsJson + " ; response : " + response);
        LogUtils.info(url + "; Body >> " + objAsJson + " ; response : " + response);

        return response;*/
        return "";
    }

    private static String convertNodeToJson(Element node) {
        XMLOutputter outp = new XMLOutputter();
        String objAsXml = outp.outputString(node);
        // System.out.println(objAsXml);
        String objAsJson = XML.toJSONObject(objAsXml).toString();
        return objAsJson;
    }

    private static Element parseXmlFile(File xmlFile) throws Exception {
        SAXBuilder builder = new SAXBuilder();
        Document document = (Document) builder.build(xmlFile);
        Element rootNode = document.getRootElement();
        return rootNode;
    }

    private static String parseEntityNameFromFileName(String fileName) throws Exception {
        String entityName = "";
        if (Character.isLetter(fileName.charAt(5))) {
            entityName = fileName.substring(2, 6).toLowerCase();
        } else {
            entityName = fileName.substring(2, 5).toLowerCase();
        }
        if (entityName.isEmpty()) {
            throw new Exception("Error Reading File Name");
        }
        return entityName;
    }

    private static String parseSirketNoFromFileName(String fileName) throws Exception {
        return fileName.substring(0, 2);
    }

    private static String parseVeriTarihiFromFileName(String fileName) throws Exception {
        String veriTarihi = ""; //yyyyMMdd
        if (Character.isLetter(fileName.charAt(5))) {
            veriTarihi = fileName.substring(6, 14);
        } else {
            veriTarihi = fileName.substring(5, 13);
        }
        if (veriTarihi.isEmpty()) {
            throw new Exception("Error Reading File Name");
        }
        return veriTarihi;
    }
}
