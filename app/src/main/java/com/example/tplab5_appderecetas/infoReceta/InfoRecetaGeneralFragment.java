package com.example.tplab5_appderecetas.infoReceta;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tplab5_appderecetas.R;
import com.example.tplab5_appderecetas.modelos.Receta;


public class InfoRecetaGeneralFragment extends Fragment {
    private String nombreReceta;
    private String descripcionReceta;
    private byte[] imagenReceta;

    public InfoRecetaGeneralFragment() {}


    public static InfoRecetaGeneralFragment newInstance(Receta receta) {
        InfoRecetaGeneralFragment fragment = new InfoRecetaGeneralFragment();
        Bundle args = new Bundle();
        args.putString("nombre", receta.getNombre());
        args.putString("descripcion", receta.getDescripcion());
        args.putByteArray("imagen", receta.getImagen());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            this.nombreReceta = getArguments().getString("nombre");
            this.descripcionReceta = getArguments().getString("descripcion");
            this.imagenReceta = getArguments().getByteArray("imagen");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info_receta_general, container, false);
        TextView infoRecetaNombre = v.findViewById(R.id.infoRecetaNombre);
        TextView infoRecetaDescripcion = v.findViewById(R.id.infoRecetaDescripcion);
        ImageView rvRecetaImagen = v.findViewById(R.id.infoRecetaImagen);
        infoRecetaNombre.setText(nombreReceta);
        infoRecetaDescripcion.setText(descripcionReceta);
        rvRecetaImagen.setImageBitmap(BitmapFactory.decodeByteArray(imagenReceta,0,imagenReceta.length));
        return v;
    }
}