package gev.xml.processor;

import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by Imona Andoid on 23.11.2017.
 */
public abstract class GevXmlProcessorAbstract implements IGevXmlProcessor {

    protected  String parseVeriTarihiFromFileName(String fileName) throws Exception {
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

    protected static String parseEntityNameFromFileName(String fileName) throws Exception {
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
}
