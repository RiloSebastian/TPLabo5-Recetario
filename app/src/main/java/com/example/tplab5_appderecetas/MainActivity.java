package com.example.tplab5_appderecetas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tplab5_appderecetas.interefaces.OnClickItemView;
import com.example.tplab5_appderecetas.main.itemRecetaAdapter;
import com.example.tplab5_appderecetas.modelos.QueryParams;
import com.example.tplab5_appderecetas.querys.HiloConexion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, OnClickItemView, Handler.Callback {
    SharedPreferences pref;
    JSONArray recetas = null;
    JSONArray recetasInicial = null;
    JSONArray ingredientes = null;
    itemRecetaAdapter recetaAdapter;
    String ingredientesPref = null;
    QueryParams parametrosHttp;
    Handler handler;
    HiloConexion hiloConn;
    RecyclerView rv;
    ActivityResultLauncher<Intent> ingredientesRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            this.pref = getSharedPreferences("recipePrefs", Context.MODE_PRIVATE);
            String x = this.pref.getString("ingredientesObtenidos", null);
            this.setIngredientes(x);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        parametrosHttp = new QueryParams();
        parametrosHttp.setNumber("10");
        String query = "recetas random";
        if (this.ingredientesPref != null) {
            parametrosHttp.setIncludeIngredients(ingredientesPref);
            parametrosHttp.setAddRecipeInformation("true");
            parametrosHttp.setFillIngredients("true");
            parametrosHttp.setInstructionsRequired("true");
            parametrosHttp.setSort("max-used-ingredients");
            query = "recetas match ingrediente";
        }

        ingredientesRL = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getStringExtra("activity").equals("ingredientes")) {
                            actualizarPreferencias();
                        }
                    }
                });

        rv = super.findViewById(R.id.rvListaRecetas);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        rv.setLayoutManager(gridLayoutManager);
        handler = new Handler(Looper.myLooper(), this);
        hiloConn = new HiloConexion(handler, query, parametrosHttp);
        hiloConn.start();
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        Log.d("recetas",msg.obj.toString());
        this.setAdapter(msg.obj);
        return false;
    }

    public void setIngredientes(String x) throws JSONException {
        if (x != null) {
            this.ingredientes = new JSONArray(x);
            for (int i = 0; i < this.ingredientes.length(); i++) {
                JSONObject ingred = new JSONObject(this.ingredientes.getString(i));
                String nombreIngre = ingred.getString("nombre");
                if (ingredientesPref == null || ingredientesPref.isEmpty()) {
                    ingredientesPref = nombreIngre;
                } else {
                    ingredientesPref = ingredientesPref.concat(",").concat(nombreIngre);
                }
            }
        }else{
            this.ingredientes = new JSONArray();
        }
    }

    public void setAdapter(Object o) {
        try {
            if (recetasInicial == null) {
                recetasInicial = new JSONArray((String) o);
                recetas = new JSONArray((String) o);
                this.recetaAdapter = new itemRecetaAdapter(recetas, ingredientes, this);
                rv.setAdapter(recetaAdapter);
            } else {
                if (o == null) {
                    recetas = recetasInicial;
                } else {
                    recetas = new JSONArray((String) o);
                }
                if (recetas.length() < 1) {
                    lanzarAlertDialog("Search Result", "There are no recipes with the selected name");
                } else {
                    this.recetaAdapter.setRecetas(recetas);
                    this.recetaAdapter.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void lanzarAlertDialog(String titulo, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        MenuItem menuItem = menu.findItem(R.id.mFiltrarPorIngred);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        ImageView closeButton = searchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(v -> {
            EditText et = findViewById(R.id.search_src_text);
            et.setText("");
            searchView.setQuery("", false);
            recetas = recetasInicial;
            setAdapter(null);
            searchView.onActionViewCollapsed();
            menuItem.collapseActionView();
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mIngredientes) {
            Intent irIngredientes = new Intent(this, ingredientesActivity.class);
            ingredientesRL.launch(irIngredientes);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        this.parametrosHttp.setIncludeIngredients(null);
        this.parametrosHttp.setQuery(query);
        this.parametrosHttp.setAddRecipeInformation("true");
        this.parametrosHttp.setFillIngredients("true");
        this.parametrosHttp.setInstructionsRequired("true");
        this.parametrosHttp.setNumber("10");
        hiloConn = new HiloConexion(handler, "recetas match nombre", this.parametrosHttp);
        hiloConn.start();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onItemClick(int index, String itemType, int accion) {
        if (itemType.equals("receta")) {
            try {
                JSONObject receta = (JSONObject) recetas.get(index);
                Intent irInfoReceta = new Intent(this, infoRecetaActivity.class);
                irInfoReceta.putExtra("receta", receta.toString());
                if (this.ingredientes != null) {
                    Log.d("ingredientes compar 0", ingredientes.toString());
                    irInfoReceta.putExtra("ingredientesPref", this.ingredientes.toString());
                }
                irInfoReceta.putExtra("index", index);
                startActivity(irInfoReceta);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void actualizarPreferencias() {
        String x = this.pref.getString("ingredientesObtenidos", null);
        Log.d("ingredientes", x);
        if (x != null && !this.ingredientes.toString().equals(x)) {
            try {
                this.setIngredientes(x);
                this.recetaAdapter.setIngredientes(this.ingredientes);
                rv.setAdapter(recetaAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}