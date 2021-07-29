package com.example.tplab5_appderecetas.modelos;

import android.util.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Receta implements Serializable {
    private Integer id;
    private String nombre;
    private String descripcion;
    private List<Ingrediente> ingredientes;
    private List<Instruccion> instrucciones;
    private Integer tiempoPreparacion;
    private String urlImagen;
    private byte[] imagen;

    public Receta(Integer id, String nombre, String descripcion, List<Ingrediente> ingredientes, List<Instruccion> instrucciones, Integer tiempoPreparacion, String urlImagen) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ingredientes = ingredientes;
        this.instrucciones = instrucciones;
        this.tiempoPreparacion = tiempoPreparacion;
        this.urlImagen = urlImagen;
        this.imagen = null;
    }

    public byte[] getImagen() { return imagen; }

    public void setImagen(byte[] imagen) { this.imagen = imagen; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public List<Instruccion> getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(List<Instruccion> instrucciones) {
        this.instrucciones = instrucciones;
    }

    public int getTiempoPreparacion() {
        return tiempoPreparacion;
    }

    public void setTiempoPreparacion(int tiempoPreparacion) {
        this.tiempoPreparacion = tiempoPreparacion;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public static Receta parsearJson(JSONObject objetoJson) {
        Receta receta;
        try {
            Integer id = (Integer) objetoJson.get("id");
            String nombre = objetoJson.getString("nombre");
            String descripcion = objetoJson.getString("descripcion");
            String urlImagen = null;
            byte[] imagenData = null;
            if(objetoJson.has("urlImagen")) {
                urlImagen = objetoJson.getString("urlImagen");
                if(objetoJson.has("imagenData")){
                    imagenData = Base64.decode(objetoJson.get("imagenData").toString(),Base64.DEFAULT);
                }
            }
            List<Ingrediente> ingredientes = new ArrayList<>();
            JSONArray ingredientesJson = objetoJson.getJSONArray("ingredientes");
            for (int i = 0; i < ingredientesJson.length(); i++) {
                Ingrediente x = Ingrediente.parsearJson(ingredientesJson.get(i).toString());
                ingredientes.add(x);
            }
            List<Instruccion> instrucciones = null;
            if (objetoJson.has("instrucciones")) {
                instrucciones = Instruccion.parsearArrayJson(objetoJson.getJSONArray("instrucciones"));
            }
            Integer tiempoPreparacion = (Integer) objetoJson.get("tiempoPreparacion");
            receta = new Receta(id, nombre, descripcion, ingredientes, instrucciones, tiempoPreparacion, urlImagen);
            receta.setImagen(imagenData);
            return receta;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
