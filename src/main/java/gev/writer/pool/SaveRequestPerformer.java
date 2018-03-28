package gev.writer.pool;

import core.Main;
import core.SessionProp;
import gev.writer.IGevObjectSaver;
import gev.writer.ImonaGevObjectSaverBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by Imona Andoid on 12.12.2017.
 */
public class SaveRequestPerformer implements Runnable {
    private GevSaveRequestInput gevSaveRequestInput;
    private IGevObjectSaver gevObjectSaver;// = new ImonaGevObjectSaverBean();

    public SaveRequestPerformer(GevSaveRequestInput gevSaveRequestInput) {
        this.gevSaveRequestInput = gevSaveRequestInput;
        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);


        gevObjectSaver = context.getBean(ImonaGevObjectSaverBean.class);
    }

    @Override
    public void run() {
        try {
            String result = gevObjectSaver.saveObject(gevSaveRequestInput.getEntityName(), gevSaveRequestInput.getSirketNo(), gevSaveRequestInput.getVeriTarihi(), gevSaveRequestInput.getObjAsJson());
        } catch (Exception e) {
            e.printStackTrace();
            //todo : increase
            SessionProp.setValue("FAILED_BATCHES", SessionProp.getValue("FAILED_BATCHES") + 1);
        }
    }
}