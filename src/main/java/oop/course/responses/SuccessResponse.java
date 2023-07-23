package oop.course.responses;

import oop.course.interfaces.*;
import oop.course.tools.*;

import java.io.*;
import java.util.*;

public class SuccessResponse implements Response {
    private final String body;

    public SuccessResponse(String json) {
        this.body = json;
    }

    public SuccessResponse(Collection<? extends JSON> jsons) throws Exception {
        ArrayList<String> jsonsAsString = new ArrayList<>();
        for (JSON json : jsons) {
            jsonsAsString.add(json.json());
        }
        this.body = "[\n" + String.join(",\n", jsonsAsString) + "\n]";
    }

    @Override
    public void print(PrintWriter out) {
        new BaseResponse(
                200, "OK",
                Map.ofEntries(
                        Map.entry("Content-Type", "application/json")
                ),
                this.body
        ).print(out);
    }
}
