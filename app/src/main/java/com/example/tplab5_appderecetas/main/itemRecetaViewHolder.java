package com.example.tplab5_appderecetas.main;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tplab5_appderecetas.R;
import com.example.tplab5_appderecetas.interefaces.OnClickItemView;

public class itemRecetaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public View v;
    public OnClickItemView clickItem;
    public Integer index;
    public TextView rvRecetaNombre;
    public TextView rvRecetaTiempoPreparacion;
    public TextView rvRecetaDescripcion;
    public ImageView rvRecetaImagen;
    public TextView rvLabelMatchIngredeientes;
    public Button rvRecetaInfo;

    public itemRecetaViewHolder(View v, OnClickItemView itemV) {
        super(v);
        this.v = v;
        this.clickItem = itemV;
        this.rvRecetaImagen = v.findViewById(R.id.rvRecetaImagen);
        this.rvRecetaNombre = v.findViewById(R.id.rvRecetaNombre);
        this.rvRecetaDescripcion = v.findViewById(R.id.rvRecetaDescripcion);
        this.rvRecetaTiempoPreparacion = v.findViewById(R.id.rvRecetaTiempoPreparacion);
        this.rvLabelMatchIngredeientes = v.findViewById(R.id.rvLabelMatchIngredientes);
        this.rvRecetaInfo = v.findViewById(R.id.rvRecetaInfo);
        rvRecetaInfo.setOnClickListener(this);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void onClick(View v) {
        this.clickItem.onItemClick(index, "receta", 0);
    }
}