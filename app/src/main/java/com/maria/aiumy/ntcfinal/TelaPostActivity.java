package com.maria.aiumy.ntcfinal;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import bdcontroler.GlobalDBHelper;

public class TelaPostActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private GlobalDBHelper globalDBHelper = new GlobalDBHelper();
    ArrayList<String> listacoments = new ArrayList<String>();
    String listaposts;
    String postName;
    String codPost;
    String emailUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
        emailUser = sp.getString("emailLogado",null);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        Bundle bundle = getIntent().getExtras();
        postName = bundle.getString("postagem");
        codPost = bundle.getString("postCod");

        displayPostName();

        try {
            arrayComments();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setUserView();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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
        getMenuInflater().inflate(R.menu.tela_post, menu);
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
        } else if (id == R.id.nav_groups) {
            Intent intent = new Intent(this, MygroupsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_create) {
            Intent intent = new Intent(this, CriargrupoActivity.class);
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

    public void displayPostName() {
        TextView postConteudo = (TextView) findViewById(R.id.conteudoPost);
        postConteudo.setText(postName);
    }

    public void arrayComments() throws IOException, JSONException
    {
        codPost = getIntent().getExtras().getString("postCod");
        JSONArray jsonComments = globalDBHelper.selectComments(getApplicationContext(), codPost);


        for (int i = 0; i < jsonComments.length(); i++) {
            JSONObject grupoObject = jsonComments.getJSONObject(i);
            String comment = grupoObject.getString("conteudo");
            listacoments.add(comment);
        }
        System.out.println(listacoments);
        ListView neoListView = (ListView) findViewById(R.id.listacoments);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listacoments);
        neoListView.setAdapter(adapter);

    }


    public void clicarparaNovoComment(View view) {
        Bundle bundle = getIntent().getExtras();
        String postCod = bundle.getString("postCod");
        Bundle b = new Bundle();
        Intent intent = new Intent(this, ComentarioActivity.class);
        b.putString("postCod", postCod);
        intent.putExtras(b);
        startActivity(intent);

    }


    public void setUserView(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);



        ImageView img = (ImageView) header.findViewById(R.id.userImg);
        img.setImageResource(R.drawable.ntc_icon);
        img.setMaxWidth(150);
        img.setMaxHeight(150);

        TextView email = (TextView) header.findViewById(R.id.userEmailNav);
        email.setText(emailUser);
    }
}
