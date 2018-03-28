package gev.xml.processor;

import gev.xml.processor.manual.ManualGevXmlProcessor;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by Imona Andoid on 24.11.2017.
 */
@Component
public class ManualTester {
    private GevXmlProcessorAbstract gevXmlProcessorAbstract = new ManualGevXmlProcessor();

    public void goIt(String filePath) {
        try {
            File xmlFile = new File(filePath);
            gevXmlProcessorAbstract.processXmlFile(xmlFile);
        }catch (Exception e){

        }
    }
}
