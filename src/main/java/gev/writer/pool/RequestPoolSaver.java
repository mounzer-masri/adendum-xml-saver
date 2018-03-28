package gev.writer.pool;

import gev.writer.IGevObjectSaver;
import utils.ConstantUtils;
import utils.PropertyReader;

/**
 * Created by Imona Andoid on 24.11.2017.
 */
public class RequestPoolSaver implements IGevObjectSaver {
    private static int pool_cuurent_size = 0;
    private static final Integer pool_max_size = Integer.valueOf(PropertyReader.getAppProperty(ConstantUtils.THREAD_MAX_SIZE));

    private Object syncObject = new Object();

    public String saveObject(String entityName, String sirketNo, String veriTarihi, String objAsJson) {
        if (!isPoolAvailabe()) {
            if (!isPoolAvailabe()) {
                synchronized (syncObject) {
                    try {
                        // Calling wait() will block this thread until another thread
                        // calls notify() on the object.
                        System.out.println("*** going into wait *** ");
                        syncObject.wait();
                    } catch (InterruptedException e) {
                        // Happens if someone interrupts your thread.
                    }
                }
            }
        }


        GevSaveRequestInput gevSaveRequestInput = new GevSaveRequestInput(entityName, sirketNo, veriTarihi, objAsJson);
        Thread myThread = new Thread(new SaveRequestPerformer(gevSaveRequestInput));
        inc();
        System.out.println("starting new thread pool_cuurent_size " + pool_cuurent_size);
        myThread.start();

        return "";
    }

    public Boolean isPoolAvailabe() {
        return pool_cuurent_size < pool_max_size;
    }

    private synchronized void inc() {
        pool_cuurent_size++;
    }

    private synchronized void dec() {
        boolean wasPoolAvailabe = isPoolAvailabe();
        pool_cuurent_size--;
        if (!wasPoolAvailabe && isPoolAvailabe()) {
            // Do something
            // If the condition is true, do the following:
            synchronized (syncObject) {
                System.out.println("*** notify *** ");
                syncObject.notify();
            }
        }
    }




}
