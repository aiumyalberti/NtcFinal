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
import android.widget.EditText;

import java.io.IOException;

import bdcontroler.GlobalDBHelper;

public class PerfilActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private GlobalDBHelper globalDBHelper = new GlobalDBHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

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
        getMenuInflater().inflate(R.menu.perfil, menu);
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



    // alert dialog apara atualizar senha

    public void gerarAlertDialogAtt(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        DialogInterface.OnClickListener btnOk = new DialogInterface.OnClickListener(){

            SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
            String emailUser = sp.getString("emailLogado",null);

            EditText novaSenha = (EditText) findViewById(R.id.attSenha);
            String senha = novaSenha.getText().toString();

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                int deuCerto = 0;
                try {
                    deuCerto = globalDBHelper.updateUsuarios(getApplicationContext(), emailUser, senha);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (deuCerto == 1) {
                    gerarAlertDeCerteza("Sua senha foi alterada!", "Sua sennha foi atualizada com sucesso!");
                } else {
                    gerarAlertDeCerteza("Senha não alterada!", "Ocorreu algum erro e sua senha não foi modificada, verifique sua conexão com a Internet e tente novamente!");

                }

            }
        };
        builder.setPositiveButton("Ok", btnOk);
        builder.create().show();
    }



    public void gerarAlertDialogExcluir(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        DialogInterface.OnClickListener btnOk = new DialogInterface.OnClickListener(){

            SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
            String emailUser = sp.getString("emailLogado",null);

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                int deuCerto = 0;
                try {
                    deuCerto = globalDBHelper.deleteUsuarios(getApplicationContext(), emailUser);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (deuCerto == 1) {
                    gerarAlertDialogSair("Sua conta foi excluia!", "Sua excluida com sucesso!");

                } else {
                    gerarAlertDialogSair("Conta não deletada!", "Ocorreu algum erro e sua conta não foi excluida, verifique sua conexão com a Internet e tente novamente!");

                }

            }
        };
        builder.setPositiveButton("Ok", btnOk);
        builder.create().show();
    }





    public void gerarAlertDialogSair(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        final Context c = this;

        DialogInterface.OnClickListener btnOk = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(c, LoginActivity.class);
                SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove("emailLogado");
                editor.apply();

                startActivity(intent);
            }
        };
        builder.setPositiveButton("Ok", btnOk);
        builder.create().show();
    }


    public void gerarAlertDeCerteza(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        DialogInterface.OnClickListener btnOk = new DialogInterface.OnClickListener(){

            SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
            String emailUser = sp.getString("emailLogado",null);

            EditText novaSenha = (EditText) findViewById(R.id.attSenha);
            String senha = novaSenha.getText().toString();

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        };
        builder.setPositiveButton("Ok", btnOk);
        builder.create().show();
    }



    public void attSenha (View v) throws IOException {

        gerarAlertDialogAtt("Tem certeza?", "Certeza meeeesmo?");

    }



    public void excluirConta (View v){
        gerarAlertDialogExcluir("Tem certeza?", "Certeza meeeesmo?");

    }

}
