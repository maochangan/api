package com.sshy.api.utils.ARManaUtil;

import com.sshy.api.utils.ConstantInterface;
import org.asynchttpclient.*;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class RemoveTarget {

    public static Map removeTarget(String targetId) throws ExecutionException, InterruptedException {
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        JSONObject params = new JSONObject();
        Auth.signParam(params, ConstantInterface.AR_APP_KEY, ConstantInterface.AR_SECRET_KEY);
        Future<Map> f = client.prepareDelete(ConstantInterface.AR_SERVER_URL + "/target/" + targetId)
                .setQueryParams(toParams(params))
                .execute(new AsyncCompletionHandler<Map>() {
                    @Override
                    public Map onCompleted(Response response) throws Exception {
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
