package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONTranslator implements Translator {

    private final Map<String, JSONObject> data = new HashMap<>();

    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray rawJsonArray = new JSONArray(jsonString);

            for (int i = 0; i < rawJsonArray.length(); i++) {
                JSONObject country = rawJsonArray.getJSONObject(i);

                country.remove("alpha2");
                country.remove("id");

                String code = country.getString("alpha3");
                country.remove("alpha3");

                data.put(code, country);
            }

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        JSONObject countryJson = data.get(country.toLowerCase().trim());

        if (countryJson == null) {
            return null;
        }

        return new ArrayList<>(countryJson.keySet());
    }

    @Override
    public List<String> getCountries() {
        return new ArrayList<>(data.keySet());
    }

    @Override
    public String translate(String country, String language) {
        String result = null;

        if (country != null && language != null) {
            JSONObject countryData = data.get(country.toLowerCase());

            if (countryData != null) {
                result = countryData.optString(language.toLowerCase(), null);
            }
        }

        return result;
    }
}