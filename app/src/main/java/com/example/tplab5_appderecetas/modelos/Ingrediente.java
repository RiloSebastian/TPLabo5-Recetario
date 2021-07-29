package com.example.tplab5_appderecetas.modelos;


import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Ingrediente implements Serializable {
    private Integer id;
    private String nombre;

    public Ingrediente(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public static Ingrediente parsearJson(String x) {
        try {
            JSONObject objetoJson = new JSONObject(x);
            Log.d("ingredientes compar 1",objetoJson.toString());
            if(!objetoJson.isNull("id")) {
                return new Ingrediente(objetoJson.getInt("id"), objetoJson.getString("nombre"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Ingrediente> parsearArrayJson(JSONArray arrayJson) {
        List<Ingrediente> x = new ArrayList<>();
        if(arrayJson != null && arrayJson.length() > 0) {
            for (int i = 0; i < arrayJson.length();i++){
                try {
                    x.add(Ingrediente.parsearJson(arrayJson.get(i).toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return x;
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass().equals(this.getClass()) && ((Ingrediente) obj).getNombre().equals(this.getNombre());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,nombre);
    }

    @NonNull
    @Override
    public String toString() {
        return "{'id': ".concat(this.id.toString()).concat(", 'nombre': ").concat(this.nombre).concat("}");
    }
}
