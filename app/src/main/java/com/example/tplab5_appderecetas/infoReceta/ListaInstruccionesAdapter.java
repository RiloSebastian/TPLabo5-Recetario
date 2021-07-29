package com.example.tplab5_appderecetas.infoReceta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.tplab5_appderecetas.R;
import com.example.tplab5_appderecetas.modelos.Instruccion;
import java.util.List;

public class ListaInstruccionesAdapter extends ArrayAdapter<Instruccion> {
    List<Instruccion> instrucciones;
    int layout;
    Context contexto;

    public ListaInstruccionesAdapter(Context context, int resource, List<Instruccion> instrucciones) {
        super(context, resource, instrucciones);
        this.instrucciones = instrucciones;
        this.contexto = context;
        this.layout = resource;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        View view = v;
        if (v == null) {
            view = LayoutInflater.from(contexto).inflate(layout, null);
        }
        Instruccion instruccion = instrucciones.get(position);
        if(instruccion != null) {
            TextView indexInstruct = view.findViewById(R.id.listItemInstrucIndex);
            TextView stepInstruct = view.findViewById(R.id.listItemInstrucPaso);
            indexInstruct.setText(instruccion.getNumber().toString());
            stepInstruct.setText(instruccion.getStep());
        }
        return view;
    }
}
