package com.example.mygame;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class NBR {
    private HashMap<String, String> map;

    NBR() {
        this.map = new HashMap<>();
    }

    static NBR read(Reader in) throws IOException {
        try {
            String contentString = "";

            char c;

            String ints = "";
            while ((c = (char) in.read()) != '#') {
                ints += c;
                contentString += c;
            }
            contentString += '#';
            int amount = Integer.parseInt(ints);
            int count = 0;
            do {
                c = (char) in.read();
                contentString += c;
                if (c == ';') {
                    count++;
                }
            } while (count != amount);

            return NBR.parseString(contentString);
        } catch (Exception e) {
            return new NBR().put("error", e.toString());
        }

    }
// 3#request:startGame;gameCode:1234;
    static NBR parseString(String s) {
        int amount = Integer.parseInt(s.substring(0, s.indexOf('#')));
        NBR result = new NBR();

        String remainder = s.substring(s.indexOf('#') + 1);
        for (int i = 0; i < amount; i++) {
            String key = remainder.substring(0, remainder.indexOf(':'));
            String value = remainder.substring(remainder.indexOf(':') + 1, remainder.indexOf(';'));

            remainder = remainder.substring(remainder.indexOf(';') + 1);
            result.put(key, value);
        }
        return result;
    }

    NBR put(String key, String value) {
        map.put(key, value);
        return this;
    }

    String get(String key) {
        return map.get(key);
    }

    @Override
    public String toString() {
        String result = "";
        result += map.size() + "#";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            result += entry.getKey() + ':' + entry.getValue() + ';';
        }
        return result;
    }
}
