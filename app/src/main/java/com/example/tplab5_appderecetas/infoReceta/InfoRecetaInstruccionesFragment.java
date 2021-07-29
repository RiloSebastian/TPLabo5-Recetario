package com.example.tplab5_appderecetas.infoReceta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.tplab5_appderecetas.R;
import com.example.tplab5_appderecetas.modelos.Instruccion;
import com.example.tplab5_appderecetas.modelos.Receta;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;


public class InfoRecetaInstruccionesFragment extends Fragment {
    List<Instruccion> instrucciones;

    public InfoRecetaInstruccionesFragment() {
    }

    public static InfoRecetaInstruccionesFragment newInstance(Receta receta) {
        InfoRecetaInstruccionesFragment fragment = new InfoRecetaInstruccionesFragment();
        try {
            Bundle args = new Bundle();
            ObjectMapper mapper = new ObjectMapper();
            String jsonInstruc = mapper.writeValueAsString(receta.getInstrucciones());
            args.putString("instrucciones",jsonInstruc);
            fragment.setArguments(args);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            try {
                instrucciones = Instruccion.parsearArrayJson(new JSONArray(getArguments().getString("instrucciones")));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info_receta_instrucciones, container, false);
        ListView infoRecetaInstrucciones = v.findViewById(R.id.infoRecetaInstrucciones);
        ListaInstruccionesAdapter adapterInstruc = new ListaInstruccionesAdapter(getContext(), R.layout.list_item_instrucciones_receta, instrucciones);
        infoRecetaInstrucciones.setAdapter(adapterInstruc);
        return v;
    }
}