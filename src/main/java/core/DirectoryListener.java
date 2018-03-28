package core;

import gev.xml.processor.GevXmlProcessorAbstract;
import gev.xml.processor.GevXmlProcessorProxy;
import gev.xml.processor.IGevXmlProcessor;
import gev.xml.processor.ManualTester;
import gev.xml.processor.manual.ManualGevXmlProcessor;
import net.contentobjects.jnotify.JNotifyListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import utils.LogUtils;

import java.io.File;

/**
 * Created by Imona Andoid on 13.6.2017.
 */
public class DirectoryListener  implements JNotifyListener {
    private IGevXmlProcessor gevXmlProcessorAbstract;

    private void prepareProcessor(){
        if(gevXmlProcessorAbstract == null) {
            ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
            gevXmlProcessorAbstract = context.getBean(GevXmlProcessorProxy.class);
        }
    }

    public void fileRenamed(int wd, String rootPath, String oldName,
                            String newName) {
        print("renamed " + rootPath + " : " + oldName + " -> " + newName);
    }
    public void fileModified(int wd, String rootPath, String name) {
        print("modified " + rootPath + " : " + name);
    }
    public void fileDeleted(int wd, String rootPath, String name) {
        print("deleted " + rootPath + " : " + name);
    }


    public void fileCreated(int wd, String rootPath, String name) {
        prepareProcessor();
        LogUtils.info("fileCreated : " + name);

        try {
            System.out.println("Waiting Period starting ...");
            Thread.sleep(10000);
            System.out.println("Waiting Period Ended.");
            //TODO : wait 10 mins beforer starting the process .
            print("File has been Found " + rootPath + " : " + name);
            String fullPath = rootPath + "/" + name;
            File xmlFile = new File(fullPath);
            gevXmlProcessorAbstract.processXmlFile(xmlFile);
        }catch (Exception e){

        }
    }

    private static void processFileManually(String filePath){
        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        ManualTester p = context.getBean(ManualTester.class);
        p.goIt(filePath);
    }

    void print(String msg) {
        System.err.println(msg);
    }


}