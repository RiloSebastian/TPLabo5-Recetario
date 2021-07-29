package com.example.tplab5_appderecetas.modelos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Instruccion {
    public Integer number;
    public String step;

    public Instruccion(Integer number, String step) {
        this.number = number;
        this.step = step;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public static Instruccion parsearJson(String x) {
        try {
            JSONObject objetoJson = new JSONObject(x);
            return new Instruccion(objetoJson.getInt("number"), objetoJson.getString("step"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Instruccion> parsearArrayJson(JSONArray arrayJson) {
        List<Instruccion> x = new ArrayList<>();
        if(arrayJson.length() > 0) {
            for (int i = 0; i < arrayJson.length();i++){
                try {
                    x.add(Instruccion.parsearJson(arrayJson.getJSONObject(i).toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return x;
    }

}
