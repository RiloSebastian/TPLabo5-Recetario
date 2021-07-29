package com.example.tplab5_appderecetas.infoReceta;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.tplab5_appderecetas.R;
import com.example.tplab5_appderecetas.modelos.Ingrediente;

import java.util.List;

public class ListaIngredientesAdapter extends ArrayAdapter<Ingrediente> {
    List<Ingrediente> ingredientes;
    List<Ingrediente> ingredientesPref;
    int layout;
    Context contexto;

    public ListaIngredientesAdapter(Context context, int resource, List<Ingrediente> ingredientes, List<Ingrediente> ingredientesPref) {
        super(context,resource,ingredientes);
        this.ingredientes = ingredientes;
        this.ingredientesPref = ingredientesPref;
        this.contexto = context;
        this.layout = resource;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        View view = v;
        if (v == null) {
            view = LayoutInflater.from(contexto).inflate(layout, null);
        }
        Ingrediente ingrediente = ingredientes.get(position);
        if(ingrediente != null) {
            TextView nombreIngred = view.findViewById(R.id.listItemIngredNombre);
            CheckBox matchIngred = view.findViewById(R.id.listItemIngredMatch);
            nombreIngred.setText(ingrediente.getNombre());
            if (ingredientesPref != null && ingredientesPref.contains(ingrediente)) {
                Log.d("ingredientes compar",ingredientesPref.toString());
                matchIngred.setChecked(true);
            } else {
                matchIngred.setChecked(false);
            }
        }
        return view;
    }
}
