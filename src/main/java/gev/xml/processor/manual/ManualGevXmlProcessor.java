package gev.xml.processor.manual;

import core.SessionProp;
import gev.xml.processor.GevXmlProcessorAbstract;
import org.springframework.stereotype.Service;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;

/**
 * Created by Imona Andoid on 23.11.2017.
 */
@Service(value = "ManualGevXmlProcessor")
public class ManualGevXmlProcessor extends GevXmlProcessorAbstract {

    @Override
    public void processXmlFile(File xmlFile) throws Exception {
        long startTime = System.currentTimeMillis();
        SessionProp.resetSession();

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        String entityName = parseEntityNameFromFileName(xmlFile.getName());

        DefaultHandler handler = new ManualSaxHandlerImpl(entityName);

        saxParser.parse(xmlFile.getPath(), handler);

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Processing XML file ended : file-name >> " + xmlFile.getName() + ";; total-time >>" + totalTime +
                ";; " + "faile-batches >>" + SessionProp.getValue("FAILED_BATCHES"));

        SessionProp.resetSession();
    }
}
