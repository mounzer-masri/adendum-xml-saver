package gev.xml.processor.manual;

import gev.writer.IGevObjectSaver;
import gev.writer.pool.ExecuterServiceRequestPoolSaver;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import utils.ConstantUtils;
import utils.PropertyReader;

/**
 * Created by Imona Andoid on 23.11.2017.
 */
public class ManualSaxHandlerImpl extends DefaultHandler {

    private IGevObjectSaver manualSaverProxy;

    final int FIRST_NODE = 0;
    final Integer STEP =  Integer.valueOf(PropertyReader.getAppProperty(ConstantUtils.DATA_BATCH_SIZE));

    StringBuilder resultJsonString = new StringBuilder("[");

    String currentElement = "";
    String entityName = "";
    String nodeStarterKeyword = "";
    String sirketNo = "";
    String veriTarihi = "";
    Integer nodeCounter = 0;
    Integer currentNodeElementIndex = 0;
    Integer batchCounter = 0;

    public ManualSaxHandlerImpl() {
        System.out.println("WWW");
    }

    public ManualSaxHandlerImpl(String nodeStarterKeyword) {
        this.nodeStarterKeyword = nodeStarterKeyword;
        this.entityName = nodeStarterKeyword + "_grp";

//        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
//        manualSaverProxy = context.getBean(DirectManualSaver.class);
        manualSaverProxy = new ExecuterServiceRequestPoolSaver();
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //System.out.println("Start Element :" + qName);
        if (qName.equals(nodeStarterKeyword)) {
            currentNodeElementIndex = 0;
            if (nodeCounter == FIRST_NODE) {
                resultJsonString.append("{");
            } else {
                resultJsonString.append(",{");
            }
            processAttributes(attributes);
        } else if (qName.equals(entityName)) {
            processMainAttributes(attributes);
            initializeJsonBatch();
        } else {
            if (currentNodeElementIndex > 0) {
                resultJsonString.append(",");
            }
            currentNodeElementIndex++;
        }

        currentElement = qName;
    }

    private void processAttributes(Attributes attributes) {
        int length = attributes.getLength();
        for (int i = 0; i < length; i++) {
            String name = attributes.getQName(i);
//            System.out.println("Name:" + name);
            String value = attributes.getValue(i);
//            System.out.println("Value:" + value);

            switch (name) {
                case "no":
                    addKeyValue(name, value).append(",");
                    break;
                case "sir":
                    //addKeyValue(name, value);
                    sirketNo = value;
                    break;
                case "vt":
                    //addKeyValue(name, value);
                    veriTarihi = value;
                    break;
            }
        }
    }
    private void processMainAttributes(Attributes attributes) {
        int length = attributes.getLength();
        for (int i = 0; i < length; i++) {
            String name = attributes.getQName(i);
//            System.out.println("Name:" + name);
            String value = attributes.getValue(i);
//            System.out.println("Value:" + value);

            switch (name) {
                case "sir":
                    //addKeyValue(name, value);
                    sirketNo = value;
                    break;
                case "vt":
                    //addKeyValue(name, value);
                    veriTarihi = value;
                    break;
            }
        }
    }
    private StringBuilder addKeyValue(String key, String value) {
        resultJsonString.append("\"").append(key).append("\"").append(":").append("\"").append(value).append("\"");
        return resultJsonString;
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        //System.out.println("End Element : " + "</" + qName + ">");
        if (qName.equals(nodeStarterKeyword)) {
            resultJsonString.append("}");
            increaseCounter();
        } else if (qName.equals(entityName)) {
            closeJsonBatch();
        }
    }

    private void increaseCounter() {
        nodeCounter++;
        if (nodeCounter.equals(STEP)) {
            //close tag of batch .
            closeJsonBatch();
            //call trigger.
            //reset string builder .
            batchCounter++;
            System.out.println(batchCounter + ">>" + resultJsonString.toString() + "**********************");

            initializeJsonBatch();
        }
    }

    private void initializeJsonBatch() {
        nodeCounter = 0;
        resultJsonString = new StringBuilder("{\"").append(entityName).append("\":{\"").append(nodeStarterKeyword).append("\":[");
    }

    private void closeJsonBatch() {
        resultJsonString.append("],\"sir\":").append(sirketNo).append(",\"vt\":").append(veriTarihi).append("}}");
//        resultJsonString.append("]").append("}}");
        try {
            System.out.println("manualSaverProxy.saveObject");
            manualSaverProxy.saveObject(nodeStarterKeyword, sirketNo, veriTarihi, resultJsonString.toString());
//            Thread.sleep(5000);
//            IGevObjectSaver.saveObject(nodeStarterKeyword, sirketNo, veriTarihi, resultJsonString.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void characters(char ch[], int start, int length) throws SAXException {
        String value = new String(ch, start, length);
        //System.out.println("First Name : " + value);
        addKeyValue(currentElement, value);
    }
}
