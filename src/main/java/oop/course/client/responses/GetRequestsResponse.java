package oop.course.client.responses;

import java.util.*;

public final class GetRequestsResponse implements Response {
    private final Response response;

    public GetRequestsResponse(Response response) {
        this.response = response;
    }

    public List<List<String>> requests() {
        var ids = response.values("id");
        var numbers = response.values("accountNumber");
        var amounts = response.values("amount");
        var types = response.values("type");
        var statuses = response.values("status");
        List<List<String>> res = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            res.add(Arrays.asList(ids[i], numbers[i], amounts[i], types[i], statuses[i]));
        }
        return res;
    }

    @Override
    public boolean isSuccess() {
        return response.isSuccess();
    }

    @Override
    public int statusCode() {
        return response.statusCode();
    }

    @Override
    public String message() {
        return response.message();
    }

    @Override
    public String value(String key) {
        return response.value(key);
    }

    @Override
    public String[] values(String key) {
        return response.values(key);
    }

    @Override
    public String body() {
        return response.body();
    }
}
