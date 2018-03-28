package gev.writer.pool;

import gev.writer.IGevObjectSaver;
import org.springframework.stereotype.Service;
import utils.ConstantUtils;
import utils.PropertyReader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Imona Andoid on 12.12.2017.
 */
@Service(value = "ExecuterServiceRequestPoolSaver")
public class ExecuterServiceRequestPoolSaver implements IGevObjectSaver {
    private ExecutorService executor;

    public ExecuterServiceRequestPoolSaver(){
        this.executor  = Executors.newFixedThreadPool(Integer.valueOf(PropertyReader.getAppProperty(ConstantUtils.THREAD_MAX_SIZE)));
    }

    @Override
    public String saveObject(String entityName, String sirkretNo, String veriTarihi, String objAsJson) throws Exception {
        GevSaveRequestInput gevSaveRequestInput = new GevSaveRequestInput(entityName, sirkretNo, veriTarihi, objAsJson);
        this.executor.execute(new SaveRequestPerformer(gevSaveRequestInput));
        return  "";
    }
}
