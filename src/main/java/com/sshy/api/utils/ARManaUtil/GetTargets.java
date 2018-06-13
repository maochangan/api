package com.sshy.api.utils.ARManaUtil;

import com.sshy.api.utils.ConstantInterface;
import org.asynchttpclient.*;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class GetTargets {

    public static Map getTargets() throws ExecutionException, InterruptedException {
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        JSONObject params = new JSONObject();
        Auth.signParam(params, ConstantInterface.AR_APP_KEY, ConstantInterface.AR_SECRET_KEY);
        Future<Map> f = client.prepareGet(ConstantInterface.AR_SERVER_URL + "/targets/")
                .setQueryParams(toParams(params))
                .execute(new AsyncCompletionHandler<Map>() {
                    @Override
                    public Map onCompleted(Response response) throws Exception {
                        System.out.println(response.getResponseBody());
                        client.close();
                        return new JSONObject(response.getResponseBody()).toMap();
                    }
                });
        return f.get();
    }

    private static List<Param> toParams(JSONObject jso) {
        return jso.keySet().stream()
                .map(key -> new Param(key, jso.getString(key)))
                .collect(Collectors.toList());
    }



}
