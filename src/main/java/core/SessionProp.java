package core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Imona Andoid on 28.11.2017.
 */
public class SessionProp {
    public static  Map sessionMap = new HashMap<String, Integer>();


    public static void setValue(String key, Integer value){
        SessionProp.sessionMap.put(key, value);
    }

    public static void resetSession(){
        SessionProp.sessionMap =  new HashMap<String, Integer>();
        SessionProp.setValue("FAILED_BATCHES",0);
    }

    public static Integer getValue(String key){
        return  (Integer)SessionProp.sessionMap.get(key);
    }
}
