package com.example.tplab5_appderecetas.querys;

import android.net.Uri;

import com.example.tplab5_appderecetas.modelos.QueryParams;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConexion {
    public Uri.Builder builder;

    public byte[] ObtenerRespuesta(QueryParams parametros, String query, boolean image) {
        String urlS = query;
        if (!image) {
            urlS = this.formarURl(this.elegirUrl(query), parametros);
        }
        try {
            URL url = new URL(urlS);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            int resp = httpConn.getResponseCode();
            if (resp == 200) {
                InputStream is = httpConn.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int cant;
                while ((cant = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, cant);
                }
                is.close();
                return baos.toByteArray();

            } else {
                throw new RuntimeException("no se pudo establecer una connexion con el servidor");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String elegirUrl(String query) {
        String s = null;
        switch (query) {
            case "recetas random":
                s = "/recipes/random";
                break;
            case "recetas match ingrediente":
            case "recetas match nombre":
                s = "/recipes/complexSearch";
                break;
            case "buscar ingredientes":
                s = "/food/ingredients/search";
                break;
        }
        return s;
    }

    public String formarURl(String urlBase, QueryParams parametros) {
        String retorno = null;
        builder = null;
        builder = new Uri.Builder();
        builder.scheme("https");
        builder.authority("api.spoonacular.com");
        builder.path(urlBase);
        ObjectMapper mapper = new ObjectMapper();
        parametros.setApiKey("ce9aae8921244dc09a4e449105de6fde");
        try {
            JSONObject parametrosJson = new JSONObject(mapper.writeValueAsString(parametros));
            for (int i = 0; i < parametrosJson.names().length(); i++) {
                String key = (String) parametrosJson.names().get(i);
                builder.appendQueryParameter(key, parametrosJson.getString(key));
            }
            retorno = builder.toString();
        } catch (JSONException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return retorno;
    }
}
