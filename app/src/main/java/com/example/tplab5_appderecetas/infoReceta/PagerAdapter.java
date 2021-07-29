package com.example.tplab5_appderecetas.infoReceta;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tplab5_appderecetas.modelos.Receta;

public class PagerAdapter extends FragmentStateAdapter {
    private Receta receta;
    private String ingredientes;

    public PagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, Receta receta, String ingredientes) {
        super(fragmentManager, lifecycle);
        this.receta = receta;
        this.ingredientes = ingredientes;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return InfoRecetaIngredientesFragment.newInstance(receta,ingredientes);
            case 2:
                return InfoRecetaInstruccionesFragment.newInstance(receta);
            default:
                return InfoRecetaGeneralFragment.newInstance(receta);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
