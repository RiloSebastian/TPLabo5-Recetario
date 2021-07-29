package com.example.tplab5_appderecetas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tplab5_appderecetas.infoReceta.PagerAdapter;
import com.example.tplab5_appderecetas.modelos.Receta;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class infoRecetaActivity extends AppCompatActivity {
    Receta recetaInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_receta);
        setTitle("Recipe Information");
        ActionBar actBar = getSupportActionBar();
        if (actBar != null) {
            actBar.setDisplayHomeAsUpEnabled(true);
        }
        Bundle extras = super.getIntent().getExtras();
        JSONObject x;
        try {
            x = new JSONObject((String) extras.get("receta"));
            String ingredientesPref = extras.getString("ingredientesPref");
            recetaInfo = Receta.parsearJson(x);
            TabLayout tabLayout = findViewById(R.id.infoRecetaTabLayout);
            ViewPager2 viewPager = findViewById(R.id.infoRecetaViewPager);
            FragmentManager fm = getSupportFragmentManager();
            PagerAdapter pa = new PagerAdapter(fm, getLifecycle(), recetaInfo, ingredientesPref);
            viewPager.setAdapter(pa);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }
                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }
                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
            viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    tabLayout.selectTab(tabLayout.getTabAt(position));
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info_receta, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.finish();
        } else if (item.getItemId() == R.id.mirShare) {
            Log.d("menu info receta", "comparte receta");
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/html");
            StringBuilder receta = new StringBuilder();
            receta.append("<h1>Would you like to try this recipe?</h1><br>");
            receta.append("<p>Name: ".concat(recetaInfo.getNombre()).concat("</p>"));
            receta.append("<p>Description: ".concat(recetaInfo.getDescripcion()).concat("</p>"));
            receta.append("<p>Preparation time: ".concat(String.valueOf(recetaInfo.getTiempoPreparacion())).concat("mins </p>"));
            receta.append("<p>Download the app if you want to know more!</p>");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(receta.toString()));
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
        }
        return super.onOptionsItemSelected(item);
    }
}