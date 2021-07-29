package com.example.tplab5_appderecetas.main;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tplab5_appderecetas.R;
import com.example.tplab5_appderecetas.interefaces.OnClickItemView;
import com.example.tplab5_appderecetas.modelos.Ingrediente;
import com.example.tplab5_appderecetas.modelos.Receta;
import com.example.tplab5_appderecetas.querys.HiloImagen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class itemRecetaAdapter extends RecyclerView.Adapter<itemRecetaViewHolder> implements Handler.Callback {
    JSONArray recetas;
    List<Ingrediente> ingredientesPref;
    OnClickItemView clickItem;
    itemRecetaViewHolder holder;

    public itemRecetaAdapter(JSONArray recetas, JSONArray ingredientespref, OnClickItemView clickItem) {
        this.recetas = recetas;
        this.ingredientesPref = Ingrediente.parsearArrayJson(ingredientespref);
        this.clickItem = clickItem;
    }

    public void setRecetas(JSONArray recetas) {
        this.recetas = recetas;
    }

    public void setIngredientes(JSONArray ingredientes) {
        this.ingredientesPref = Ingrediente.parsearArrayJson(ingredientes);
    }


    @NonNull
    @Override
    public itemRecetaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receta, parent, false);
        return new itemRecetaViewHolder(v, clickItem);
    }

    @Override
    public void onBindViewHolder(@NonNull itemRecetaViewHolder holderPos, int position) {
        holder = holderPos;
        try {
            JSONObject recetaJson = this.recetas.getJSONObject(position);
            Log.d("ingredientes compar", recetaJson.toString());
            Receta receta = Receta.parsearJson(recetaJson);
            Handler imagenHandler = new Handler(Looper.myLooper(), this);
            if (receta != null) {
                holder.rvRecetaNombre.setText(receta.getNombre());
                holder.rvRecetaDescripcion.setText(receta.getDescripcion());
                holder.rvRecetaTiempoPreparacion.setText(String.valueOf(receta.getTiempoPreparacion()).concat(" min."));
                String labelIngrdientesMatch = "No Matching Ingredients";
                String labelIngrdientesTextColor = "#A40606";
                String labelIngredientesBackground = "#C56969";
                int ingredientesPoseidos = 0;
                for (Ingrediente ing : receta.getIngredientes()) {
                    if (ingredientesPref.contains(ing)) {
                        ingredientesPoseidos++;
                    }
                }
                if (ingredientesPoseidos > 0 && ingredientesPoseidos < receta.getIngredientes().size()) {
                    labelIngrdientesMatch = "Some Matching Ingredients";
                    labelIngrdientesTextColor = "#685A00";
                    labelIngredientesBackground = "#D0C362";
                } else if (ingredientesPoseidos == receta.getIngredientes().size()) {
                    labelIngrdientesMatch = "All Matching Ingredients";
                    labelIngrdientesTextColor = "#172400";
                    labelIngredientesBackground = "#89D062";
                }
                holder.rvLabelMatchIngredeientes.setText(labelIngrdientesMatch);
                holder.rvLabelMatchIngredeientes.setTextColor(Color.parseColor(labelIngrdientesTextColor));
                holder.rvLabelMatchIngredeientes.setBackgroundColor(Color.parseColor(labelIngredientesBackground));
                holder.setIndex(position);
                if (receta.getUrlImagen() != null && receta.getImagen() != null) {
                    holder.rvRecetaImagen.setImageBitmap(BitmapFactory.decodeByteArray(receta.getImagen(), 0, receta.getImagen().length));
                } else if (receta.getUrlImagen() != null && receta.getImagen() == null) {
                    HiloImagen hiloImg = new HiloImagen(position, receta.getUrlImagen(), imagenHandler);
                    hiloImg.start();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.recetas.length();
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        if (msg.arg1 >= 0) {
            try {
                JSONObject recetaData = this.recetas.getJSONObject(msg.arg1);
                String stringData = Base64.encodeToString((byte[]) msg.obj, Base64.DEFAULT);
                recetaData.put("imagenData", stringData);
                this.notifyItemChanged(msg.arg1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
