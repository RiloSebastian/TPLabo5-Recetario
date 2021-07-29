package com.example.tplab5_appderecetas.querys;

import android.os.Handler;
import android.os.Message;

import com.example.tplab5_appderecetas.modelos.QueryParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HiloConexion extends Thread {
    Handler handler;
    Message msg;
    String query;
    QueryParams params;

    public HiloConexion(Handler handler, String query, QueryParams params) {
        this.handler = handler;
        this.query = query;
        this.params = params;
    }

    public Message getMsg() {
        return msg;
    }

    public void setMsg(Message msg) {
        this.msg = msg;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public QueryParams getParams() {
        return params;
    }

    public void setParams(QueryParams params) {
        this.params = params;
    }

    @Override
    public void run() {
        HttpConexion connHttp = new HttpConexion();
        msg = new Message();
        byte[] res = connHttp.ObtenerRespuesta(params, query,false);
        msg.obj = formatearRespuesta(new String(res), query);
        handler.sendMessage(msg);
    }

    public String formatearRespuesta(String respuesta, String query) {
        JSONArray retorno = new JSONArray();
        try {
            JSONObject object = new JSONObject(respuesta);
            JSONArray objectArray;
            if (query.equals("recetas random")) {
                objectArray = object.getJSONArray("recipes");
            } else {
                objectArray = object.getJSONArray("results");
            }
            for (int i = 0; i < objectArray.length(); i++) {
                JSONObject aux = objectArray.getJSONObject(i);
                JSONObject formatted = new JSONObject();
                formatted.put("id", aux.get("id"));

                if (query.equals("buscar ingredientes")) {
                    formatted.put("nombre", aux.get("name"));
                } else {
                    if (aux.has("image")) {
                        formatted.put("urlImagen", aux.get("image"));
                    }
                    formatted.put("nombre", aux.getString("title"));
                    String descripcion = aux.getString("title").concat(" is a dish that gives away (")
                            .concat(aux.getString("servings"))
                            .concat(") servings and can be prepared in an average time of ")
                            .concat(aux.getString("readyInMinutes"))
                            .concat(" minutes. This recipe has a quality score of %")
                            .concat(aux.getString("spoonacularScore"))
                            .concat(" and a health score of %")
                            .concat(aux.getString("healthScore"));
                    formatted.put("descripcion", descripcion);
                    formatted.put("tiempoPreparacion", aux.get("readyInMinutes"));
                    if (aux.getJSONArray("analyzedInstructions").length() > 0) {
                        JSONObject auxX = (JSONObject) aux.getJSONArray("analyzedInstructions").get(0);
                        JSONArray auxInstructions = auxX.getJSONArray("steps");
                        for (int j = 0; j < auxInstructions.length(); j++) {
                            JSONObject o = (JSONObject) auxInstructions.get(j);
                            JSONObject nuevoIngred = new JSONObject();
                            nuevoIngred.put("number",o.getInt("number"));
                            nuevoIngred.put("step",o.getString("step"));
                            auxInstructions.put(j, nuevoIngred);
                        }
                        formatted.put("instrucciones", auxInstructions);
                    }
                    if (query.equals("recetas match ingrediente")) {
                        formatted.put("ingredientesPoseidos", aux.get("usedIngredientCount"));
                        formatted.put("ingredientesNoPoseidos", aux.get("missedIngredientCount"));
                    }
                    JSONArray ingredients = aux.getJSONArray("extendedIngredients");
                    for (int j = 0; j < ingredients.length(); j++) {
                        JSONObject auxIngred = (JSONObject) ingredients.get(j);
                        if(!(auxIngred.isNull("id"))) {
                            JSONObject nuevoIngred = new JSONObject();
                            nuevoIngred.put("id", auxIngred.getInt("id"));
                            nuevoIngred.put("nombre", auxIngred.getString("name"));
                            ingredients.put(j, nuevoIngred);
                        }
                    }
                    formatted.put("ingredientes", ingredients);
                }
                retorno.put(formatted);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retorno.toString();
    }
}
