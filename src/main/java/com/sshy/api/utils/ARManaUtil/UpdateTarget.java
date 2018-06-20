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

public class UpdateTarget {

    public static Map updateTarget(String targetId, String baseImage, String active, String name, String size, String meta) throws ExecutionException, InterruptedException {
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        JSONObject params = new JSONObject();
        params.put("image", baseImage);
        params.put("name", name);
        params.put("active", active);
        params.put("size", size);
        params.put("meta", meta);
        Auth.signParam(params, ConstantInterface.AR_APP_KEY, ConstantInterface.AR_SECRET_KEY);
        Future<Map> f = client.preparePut(ConstantInterface.AR_SERVER_URL + "/target/" + targetId)
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
