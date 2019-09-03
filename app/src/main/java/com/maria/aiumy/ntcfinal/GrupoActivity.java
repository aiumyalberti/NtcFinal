package com.maria.aiumy.ntcfinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import bdcontroler.GlobalDBHelper;

public class GrupoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {
    private GlobalDBHelper globalDBHelper = new GlobalDBHelper();
    ArrayList<String> listaPosts = new ArrayList<String>();
    ArrayList<String> listaCodPosts = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Bundle bundle = getIntent().getExtras();
        String nomeGrupo = bundle.getString("nomeGrupo");
        TextView textViewGrupo = (TextView) findViewById(R.id.textGrupo);
        textViewGrupo.setText(nomeGrupo);
        String codGrupo = bundle.getString("codGrupo");

        try {
            arrayPosts(codGrupo);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.grupo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, PerfilActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_notification) {
            Intent intent = new Intent(this, PostagemActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_groups) {
            Intent intent = new Intent(this, MygroupsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_create) {
            Intent intent = new Intent(this, CriargrupoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_new) {
            Intent intent = new Intent(this, NewgroupsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_exit) {
            SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.remove("emailLogado");
            editor.apply();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        String nomePost =  listaPosts.get(position);
        String codPost = null;
        JSONArray jsonComments = null;
        try {
             jsonComments = globalDBHelper.selectAllFromPostagem(getApplicationContext());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < jsonComments.length(); i++) {
            JSONObject grupoObject = null;
            try {
                grupoObject = jsonComments.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                String namePost = grupoObject.getString("conteudo");
                if (namePost.equals(nomePost)) {
                    codPost = grupoObject.getString("cod");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        Bundle b = new Bundle();
        Intent intent = new Intent(this, TelaPostActivity.class);
        b.putString("postagem", nomePost);
        b.putString("codPost", codPost);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void clicarparaNovoPost (View v){
        onItemClickNovo();
    }

    public void arrayPosts(String codGrupo) throws IOException, JSONException //
    {
        JSONArray jsonPosts = globalDBHelper.selectPostGrupo(getApplicationContext(), codGrupo);


        for (int i = 0; i < jsonPosts.length(); i++) {
            JSONObject grupoObject = jsonPosts.getJSONObject(i);
            String conteudo = grupoObject.getString("conteudo");
            listaPosts.add(conteudo);
            String codPost = grupoObject.getString("cod");
            listaCodPosts.add(codPost);
        }

        ListView neoListView = (ListView) findViewById(R.id.listacoments);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaPosts);
        neoListView.setOnItemClickListener(this);
        neoListView.setAdapter(adapter);

    }


    public void onItemClickNovo() {
        Bundle bundle = getIntent().getExtras();
        String codGrupo = bundle.getString("codGrupo");
        Bundle b = new Bundle();
        Intent intent = new Intent(this, PostagemActivity.class);
        b.putString("codGrupo", codGrupo);
        intent.putExtras(b);
        startActivity(intent);

    }


}
