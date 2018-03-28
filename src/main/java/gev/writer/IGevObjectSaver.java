package gev.writer;

import org.springframework.stereotype.Component;

/**
 * Created by Imona Andoid on 23.11.2017.
 */
public interface IGevObjectSaver {
    public String saveObject(String entityName, String sirkretNo, String veriTarihi, String objAsJson) throws Exception;
}
