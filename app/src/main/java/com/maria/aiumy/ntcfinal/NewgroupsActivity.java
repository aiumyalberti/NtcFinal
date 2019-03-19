package com.maria.aiumy.ntcfinal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import bdcontroler.GlobalDBHelper;

public class NewgroupsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener  {

    private GlobalDBHelper globalDBHelper = new GlobalDBHelper();
    private ArrayList<String> listaNovos = new ArrayList<String>();
    private ArrayList<String> listaCodigosGrupos = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newgroups);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        try {
            arrayNovos();
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
        getMenuInflater().inflate(R.menu.newgroups, menu);
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
        // Handle navigation view item clicks here.
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

    public void arrayNovos() throws IOException, JSONException //
    {
        JSONArray jsonNovos = globalDBHelper.selectAllFromGrupos(getApplicationContext());


        for (int i = 0; i < jsonNovos.length(); i++) {
            JSONObject grupoObject = jsonNovos.getJSONObject(i);
            String nomeGrupo = grupoObject.getString("nome");
            String codGrupo = grupoObject.getString("cod");
            listaNovos.add(nomeGrupo);
            listaCodigosGrupos.add(codGrupo);
        }

        ListView neoListView = (ListView) findViewById(R.id.list_new);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaNovos);
        neoListView.setOnItemClickListener(this);
        neoListView.setAdapter(adapter);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //String nomeGrupo =  listaNovos.get(position);
        String codGrupo = listaCodigosGrupos.get(position);
        gerarAlertDialog("Entrar no grupo?", "Você deseja participar deste grupo?", codGrupo);

    }

    public void gerarAlertDialog(String title, String message, final String codGrupo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title);
        builder.setMessage(message);
        DialogInterface.OnClickListener btnOk = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
                String emailUser = sp.getString("emailLogado",null);
                try {
                    globalDBHelper.insertIntoGrupoHasUsuario(getApplicationContext(), emailUser, codGrupo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        builder.setPositiveButton("Participar", btnOk);
        builder.create().show();
    }
}
