package gev.xml.processor;

import gev.xml.processor.automatic.AutoGevXMLProcessor;
import gev.xml.processor.manual.ManualGevXmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by Imona Andoid on 28.11.2017.
 */
@Service
public class GevXmlProcessorProxy implements IGevXmlProcessor {

    @Autowired
    @Qualifier("AutoGevXMLProcessor")
    private IGevXmlProcessor autoParser;

    @Autowired
    @Qualifier("ManualGevXmlProcessor")
    private IGevXmlProcessor manualBatchParser;


    @Override
    public void processXmlFile(File xmlFile) throws Exception {
        System.out.println("ok monzer");
        if(xmlFile.getName().toLowerCase().contains("hes")){
//            manualBatchParser.processXmlFile(xmlFile);
            autoParser.processXmlFile(xmlFile);
        } else {
            autoParser.processXmlFile(xmlFile);
        }
        throw new Exception();
    }
}
