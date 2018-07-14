package com.github.trayio;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.reactivex.Observable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkFlowReader {

    private final static java.util.logging.Logger logger = Logger.getLogger(WorkFlowReader.class.getName());

    public static Observable<WorkFlow> get(String filePath) {
        Gson gson = new Gson();
        BufferedReader br = null;
        WorkFlow wf = new WorkFlow("default", Collections.emptyList());

        try {
            br = new BufferedReader(new FileReader(filePath));
            Type type = new TypeToken<WorkFlow>(){}.getType();
            wf = gson.fromJson(br, type);
            br.close();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Cannot locate file: " + filePath);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return Observable.just(wf);
    }
}
