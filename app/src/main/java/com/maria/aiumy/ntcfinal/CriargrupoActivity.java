package com.maria.aiumy.ntcfinal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
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
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import bdcontroler.GlobalDBHelper;

public class CriargrupoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private GlobalDBHelper globalDBHelper = new GlobalDBHelper();
    String emailUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criargrupo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
        emailUser = sp.getString("emailLogado",null);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//
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

    public void switchBtn(View v) {
        TextView senha = (TextView) findViewById(R.id.senhona);
        if (senha.getVisibility() == View.INVISIBLE){
            senha.setVisibility(TextView.VISIBLE);
        }

        else if (senha.getVisibility() == View.VISIBLE){
            senha.setVisibility(TextView.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.criargrupo, menu);
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
    public void criarGrupo(View v) throws IOException, JSONException {
        EditText nomeEditText = (EditText) findViewById(R.id.grupoNomeC);
        String nome = nomeEditText.getText().toString();

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String tema_tema = spinner.getSelectedItem().toString();
        EditText senhaEditText = (EditText) findViewById(R.id.senhona);
        String senha = senhaEditText.getText().toString();

        System.out.println(tema_tema);

        SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
        String emailUser = sp.getString("emailLogado",null);

        System.out.println(emailUser);

        int deuCerto = globalDBHelper.insertGrupo(getApplicationContext(), nome, senha, tema_tema, emailUser);
        if (deuCerto == 1) {
            gerarAlertDialog("Grupo criado!", "Grupo criado com sucesso!");
        } else {
            gerarAlertDialog("Erro ao criar grupo", "Ocorreu um erro ao criar o grupo, por favor tente novamente :)");

        }
    }


    public void gerarAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        DialogInterface.OnClickListener btnOk = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        };
        builder.setPositiveButton("Ok", btnOk);
        builder.create().show();
    }

    public void setUserView(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView email = (TextView) header.findViewById(R.id.userEmailNav);
        email.setText(emailUser);
    }
}
