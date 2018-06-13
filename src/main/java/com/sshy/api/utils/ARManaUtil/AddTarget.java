package com.sshy.api.utils.ARManaUtil;

import com.sshy.api.utils.ConstantInterface;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AddTarget {

    public static Map addTarget(String baseImage , String type , String name , String size , String meta) throws ExecutionException, InterruptedException {
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        JSONObject params = new JSONObject();
        params.put("image",baseImage);
        //Here is target required info in EasyAR SDK 2.0+
        params.put("type",type);
        params.put("name", name);
        params.put("size", size);
        params.put("meta", meta);  // This is customerized field to store AR content. e.x.: base64(2D picture) less than 2MB or URL of 3D model Object file
        Auth.signParam(params, ConstantInterface.AR_APP_KEY, ConstantInterface.AR_SECRET_KEY);
        Future<Map> f = client.preparePost(ConstantInterface.AR_SERVER_URL + "/targets/")
                .setBody(params.toString().getBytes())
                .execute(new AsyncCompletionHandler<Map>() {
                    @Override
                    public Map onCompleted(Response response) throws Exception {
                        client.close();
                        return new JSONObject(response.getResponseBody()).toMap();
                    }
                });
        return f.get();
    }

}
