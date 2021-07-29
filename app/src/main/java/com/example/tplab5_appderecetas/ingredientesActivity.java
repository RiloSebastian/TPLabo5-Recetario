package com.example.tplab5_appderecetas;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tplab5_appderecetas.ingredientes.itemIngredienteAdapter;
import com.example.tplab5_appderecetas.interefaces.OnClickItemView;
import com.example.tplab5_appderecetas.modelos.Ingrediente;
import com.example.tplab5_appderecetas.modelos.QueryParams;
import com.example.tplab5_appderecetas.querys.HiloConexion;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ingredientesActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, Handler.Callback, OnClickItemView {
    JSONArray ingredientes = null;
    JSONArray ingredientesInicial = null;
    JSONArray ingredienteList;
    itemIngredienteAdapter ingredsAdapter;
    QueryParams parametrosHttp;
    Handler handler;
    HiloConexion hiloConn;
    RecyclerView rv;
    String query;
    List<String> random = new ArrayList<>();
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredientes);
        setTitle("Ingredients Management");
        rv = super.findViewById(R.id.rvIngredientes);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        rv.setLayoutManager(gridLayoutManager);
        ActionBar actBar = getSupportActionBar();
        assert actBar != null;
        actBar.setDisplayHomeAsUpEnabled(true);
        pref = getSharedPreferences("recipePrefs",Context.MODE_PRIVATE);
        String x = traerIngredienteList();
        try {
            if(x != null) {
                this.ingredienteList = new JSONArray(x);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        random.addAll(Arrays.asList("tomato", "egg", "vegetable", "pasta", "meat", "soup"));
        Collections.shuffle(random);
        parametrosHttp = new QueryParams();
        parametrosHttp.setQuery(random.get(2));
        parametrosHttp.setNumber("10");
        if (ingredienteList != null && ingredienteList.length() > 0) {
            setAdapter(x);
        } else {
            query = "buscar ingredientes";
            handler = new Handler(Looper.myLooper(), this);
            hiloConn = new HiloConexion(handler, query, parametrosHttp);
            hiloConn.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ingredientes, menu);
        MenuItem menuItem = menu.findItem(R.id.mFiltrarPorIngredNombre);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        ImageView closeButton = searchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(v -> {
            EditText et = findViewById(R.id.search_src_text);
            et.setText("");
            searchView.setQuery("", false);
            ingredientes = ingredientesInicial;
            setAdapter(null);
            searchView.onActionViewCollapsed();
            menuItem.collapseActionView();
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent();
            intent.putExtra("activity", "ingredientes");
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setAdapter(Object o) {
        try {
            if (ingredientesInicial == null) {
                ingredientes = new JSONArray((String) o);
                ingredientesInicial = new JSONArray((String) o);
                ingredsAdapter = new itemIngredienteAdapter(ingredientes, ingredienteList, this);
            } else {
                if (o == null) {
                    ingredientes = ingredientesInicial;
                } else {
                    ingredientes = new JSONArray((String) o);
                }
                this.ingredsAdapter.setingredientes(ingredientes);
            }
            rv.setAdapter(ingredsAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        this.parametrosHttp.setQuery(query);
        this.parametrosHttp.setSort(null);
        handler = new Handler(Looper.myLooper(), this);
        hiloConn = new HiloConexion(handler, "buscar ingredientes", this.parametrosHttp);
        hiloConn.start();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        setAdapter(msg.obj.toString());
        return false;
    }

    public String traerIngredienteList() {
        Log.d("ingredientes compar",String.valueOf(this.pref.contains("ingredientesObtenidos")));
        String x = pref.getString("ingredientesObtenidos", null);
        if (x != null) {
            return x;
        }
        return null;
    }

    public void onItemClick(int index, String itemType, int accion) {
        if (itemType.equals("ingrediente")) {
            try {
                Ingrediente ingrediente = Ingrediente.parsearJson(ingredientes.getString(index));
                this.editor = this.pref.edit();
                if (accion == 0) {
                    if (this.ingredienteList == null) {
                        this.ingredienteList = new JSONArray();
                    }
                    ObjectMapper x = new ObjectMapper();
                    this.ingredienteList.put(x.writeValueAsString(ingrediente));
                } else if (accion == 1) {
                    int pos = ingredsAdapter.checkMatchIngreds(ingrediente);
                    if (pos != -1) {
                        this.ingredienteList.remove(pos);
                    }
                }
                this.editor.putString("ingredientesObtenidos", this.ingredienteList.toString());
                this.editor.commit();
                this.ingredsAdapter.setingredientesPref(this.ingredienteList);
                this.ingredsAdapter.notifyItemChanged(index);

            } catch (JSONException | JsonProcessingException  e) {
                e.printStackTrace();
            }
        }
    }
}