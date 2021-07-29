package com.example.tplab5_appderecetas.ingredientes;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tplab5_appderecetas.R;
import com.example.tplab5_appderecetas.interefaces.OnClickItemView;

public class itemIngredienteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public View v;
    public Integer index;
    public OnClickItemView clickItem;
    public TextView idIngred;
    public TextView nombreIngred;
    public Button agregarIngred;
    public Button eliminarIngred;

    public itemIngredienteViewHolder(@NonNull View itemView, OnClickItemView clickItem) {
        super(itemView);
        this.v = itemView;

        this.clickItem = clickItem;
        this.idIngred = v.findViewById(R.id.itemIngredienteId);
        this.nombreIngred = v.findViewById(R.id.itemIngredienteNombre);
        this.agregarIngred = v.findViewById(R.id.itemIngredienteAgregar);
        this.eliminarIngred = v.findViewById(R.id.itemIngredienteEliminar);
        agregarIngred.setOnClickListener(this);
        eliminarIngred.setOnClickListener(this);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.itemIngredienteAgregar) {
            this.clickItem.onItemClick(index, "ingrediente", 0);
        } else if (v.getId() == R.id.itemIngredienteEliminar) {
            this.clickItem.onItemClick(index, "ingrediente", 1);
        }
    }
}
