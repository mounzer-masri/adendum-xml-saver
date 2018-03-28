package gev.writer;
import org.apache.http.Consts;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.CoreProtocolPNames;
import org.springframework.stereotype.Service;
import utils.ConstantUtils;
import utils.LogUtils;
import utils.PropertyReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * Created by Imona Andoid on 23.11.2017.
 */

@Service(value = "ImonaGevObjectSaverBean")
public class ImonaGevObjectSaverBean implements IGevObjectSaver {


    @Override
    public String saveObject(String entityName, String sirkretNo, String veriTarihi, String objAsJson) throws Exception {
        String serverDomain = PropertyReader.getAppProperty(ConstantUtils.SERVER_DOMAIN);
        String serverOrgName = PropertyReader.getAppProperty(ConstantUtils.SERVER_ORG_NAME);
        String url = serverDomain + "/platform/rest/publicService/" + serverOrgName + "/eyf/gev_v2_" + entityName + "_xml_yukleme?veri_tarihi=" + veriTarihi + "&sirket_no=" + sirkretNo ;

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        StringEntity entity = new StringEntity(objAsJson, "UTF-8" );
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
//        httpPost.setHeader("Content-type", "application/json;charset=utf-8");
        httpPost.setHeader("Content-type", "application/json");
//        httpPost.setHeader(CoreProtocolPNames.HTTP_CONTENT_CHARSET, Consts.UTF_8.name());
        System.out.println("Sending Request : url :" + url + "; Body >> " + objAsJson);
        CloseableHttpResponse response = client.execute(httpPost);

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer responseContent = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            responseContent.append(line);
        }
        //todo lggging
        if(!responseContent.toString().equals("true")){
            String res = "Response Recieved url >> " + url + ";; Body >> " + objAsJson + " ;; response code >> " + response.getStatusLine().getStatusCode() + ";; respons content : " + responseContent.toString();
            LogUtils.info("Error While Writing Batch : " + res );
            System.out.println("Error While Writing Batch : " + res );
//            throw new Exception("error");
        } else {
            String res = "Response Recieved url >> " + url + ";; Body >> " + objAsJson + " ;; response code >> " + response.getStatusLine().getStatusCode() + ";; respons content : " + responseContent.toString();
            System.out.println("Request Done Successfully : " + res );
        }
        client.close();
        return "";
    }
}
