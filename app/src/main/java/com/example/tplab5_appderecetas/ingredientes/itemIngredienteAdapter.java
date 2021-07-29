package com.example.tplab5_appderecetas.ingredientes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tplab5_appderecetas.R;
import com.example.tplab5_appderecetas.interefaces.OnClickItemView;
import com.example.tplab5_appderecetas.modelos.Ingrediente;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class itemIngredienteAdapter extends RecyclerView.Adapter<itemIngredienteViewHolder> {
    JSONArray ingredientes;
    JSONArray ingredienteList;
    OnClickItemView clickItem;

    public itemIngredienteAdapter(JSONArray ingredientes, JSONArray ingredienteList, OnClickItemView onClick) {
        this.clickItem = onClick;
        this.ingredientes = ingredientes;
        this.ingredienteList = ingredienteList;
    }

    public void setingredientes(JSONArray ingredientes) {
        this.ingredientes = ingredientes;
    }

    public void setingredientesPref(JSONArray ingredienteList) {
        this.ingredienteList = ingredienteList;
    }

    @NonNull
    @Override
    public itemIngredienteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingrediente, parent, false);
        return new itemIngredienteViewHolder(v, clickItem);
    }

    @Override
    public void onBindViewHolder(@NonNull itemIngredienteViewHolder holder, int position) {
        try {
            Ingrediente ingrediente = Ingrediente.parsearJson(ingredientes.get(position).toString());
            assert ingrediente != null;
            holder.idIngred.setText(ingrediente.getId().toString());
            holder.nombreIngred.setText(ingrediente.getNombre());
            int valido = checkMatchIngreds(ingrediente);
            if (valido != -1) {
                holder.eliminarIngred.setEnabled(true);
                holder.agregarIngred.setEnabled(false);
            } else {
                holder.eliminarIngred.setEnabled(false);
                holder.agregarIngred.setEnabled(true);
            }
            holder.setIndex(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return ingredientes.length();
    }

    public int checkMatchIngreds(Ingrediente ingrediente) {
        int retorno = -1;
        try {
            if (ingredienteList != null && this.ingredienteList.length() > 0) {
                for (int i = 0; i < ingredienteList.length(); i++) {
                    Ingrediente ingAux = Ingrediente.parsearJson(ingredienteList.get(i).toString());
                    if (ingrediente.equals(ingAux)) {
                        retorno = i;
                        break;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retorno;
    }
}
