package gev.writer.pool;

/**
 * Created by Imona Andoid on 24.11.2017.
 */
public class GevSaveRequestInput {
    private String entityName;
    private String sirketNo;
    private String veriTarihi;
    private String objAsJson;

    public GevSaveRequestInput(String entityName, String sirketNo, String veriTarihi, String objAsJson) {
        this.entityName = entityName;
        this.sirketNo = sirketNo;
        this.veriTarihi = veriTarihi;
        this.objAsJson = objAsJson;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getSirketNo() {
        return sirketNo;
    }

    public void setSirketNo(String sirketNo) {
        this.sirketNo = sirketNo;
    }

    public String getVeriTarihi() {
        return veriTarihi;
    }

    public void setVeriTarihi(String veriTarihi) {
        this.veriTarihi = veriTarihi;
    }

    public String getObjAsJson() {
        return objAsJson;
    }

    public void setObjAsJson(String objAsJson) {
        this.objAsJson = objAsJson;
    }


}
