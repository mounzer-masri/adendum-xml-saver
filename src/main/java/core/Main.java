package core;

import gev.xml.processor.ManualTester;
import net.contentobjects.jnotify.JNotify;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import utils.ConstantUtils;
import utils.LogUtils;
import utils.PropertyReader;
import utils.RestClient;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Objects;

@ComponentScan(basePackages = "gev")
public class Main {
    public static void main(String[] args) throws Exception {
        prep();
        System.out.println("Program Started" + PropertyReader.getAppProperty(ConstantUtils.GEV_FILES_PATH));
        LogUtils.info("Program Started" + PropertyReader.getAppProperty(ConstantUtils.GEV_FILES_PATH));

        long startTime = System.currentTimeMillis();
        addDirWatcher();
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("totalTime >>> " + totalTime);
    }
    private static  void prep(){
        try {
        System.setProperty("file.encoding","UTF-8");
        Field charset = null;
            charset = Charset.class.getDeclaredField("defaultCharset");
            charset.setAccessible(true);
            charset.set(null,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addDirWatcher() throws Exception {
        int mask = JNotify.FILE_CREATED;
        boolean watchSubtree = false;
        int watchID = JNotify.addWatch(PropertyReader.getAppProperty(ConstantUtils.GEV_FILES_PATH), mask, watchSubtree, new DirectoryListener());
        Thread.sleep(10000000);
    }

    private static void test2(){
        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);


        ManualTester p = context.getBean(ManualTester.class);
        String fullPath = "D:\\adendum\\sample\\ERHES2009123103.xml";
        p.goIt(fullPath);
    }

}
