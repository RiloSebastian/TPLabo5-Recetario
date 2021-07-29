package com.example.tplab5_appderecetas.infoReceta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.tplab5_appderecetas.R;
import com.example.tplab5_appderecetas.modelos.Ingrediente;
import com.example.tplab5_appderecetas.modelos.Receta;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class InfoRecetaIngredientesFragment extends Fragment {
    List<Ingrediente> ingredientes;
    List<Ingrediente> ingredientesPref1;

    public InfoRecetaIngredientesFragment() {
    }

    public static InfoRecetaIngredientesFragment newInstance(Receta receta, String ingredientesPref) {
        InfoRecetaIngredientesFragment fragment = new InfoRecetaIngredientesFragment();
        try {
            Bundle args = new Bundle();
            ObjectMapper mapper = new ObjectMapper();
            String jsonIngreds = mapper.writeValueAsString(receta.getIngredientes());
            args.putString("ingredientes", jsonIngreds);
            args.putString("ingredientesPref", ingredientesPref);
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
                String ingredientesPref = getArguments().getString("ingredientesPref");
                ingredientes = Ingrediente.parsearArrayJson(new JSONArray(getArguments().getString("ingredientes")));
                ingredientesPref1 = null;
                if (ingredientesPref != null) {
                    ingredientesPref1 = Ingrediente.parsearArrayJson(new JSONArray(ingredientesPref));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info_receta_ingredientes, container, false);
        ListView infoRecetaIngredientes = v.findViewById(R.id.infoRecetaIngredientes);
        ListaIngredientesAdapter adapterIngred = new ListaIngredientesAdapter(getContext(), R.layout.list_item_ingredientes_receta, ingredientes, ingredientesPref1);
        infoRecetaIngredientes.setAdapter(adapterIngred);
        return v;
    }

}