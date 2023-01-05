package org.easyit.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.easyit.demo.api.model.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        InputStream resourceAsStream = ClassLoader.getSystemResourceAsStream("common-agent/config");
        InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String s = bufferedReader.readLine();
        List<String> lines = new ArrayList<>();
        while (s != null) {
            System.out.println(s);
            lines.add(s);
            s = bufferedReader.readLine();
        }
        bufferedReader.close();
        for (String line : lines) {
            InputStream systemResourceAsStream = ClassLoader.getSystemResourceAsStream("common-agent/" + line);
            Config config = mapper.readValue(systemResourceAsStream, Config.class);
            System.out.println(config.getVersion());
        }

    }
}
