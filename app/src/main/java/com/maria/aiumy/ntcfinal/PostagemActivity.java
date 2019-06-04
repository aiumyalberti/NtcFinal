package com.maria.aiumy.ntcfinal;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.maria.aiumy.ntcfinal.MainActivity;
import com.maria.aiumy.ntcfinal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bdcontroler.GlobalDBHelper;
import bdcontroler.LocalDBHelper;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class PostagemActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private GlobalDBHelper globalDBHelper = new GlobalDBHelper();
    String codGrupo;
    ArrayList<String> listaPostagens = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postagem);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Bundle bundle = getIntent().getExtras();
        codGrupo = bundle.getString("codGrupo");

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
        getMenuInflater().inflate(R.menu.postagem, menu);
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


    public void botao(View view){
        MediaPlayer som_r2d2 = MediaPlayer.create(this,R.raw.r2d2);
        som_r2d2.start();
    }

    public void emailSimulator() throws IOException, JSONException {

    }

    public void postar(View v) throws IOException, JSONException {
        EditText postEditText = (EditText) findViewById(R.id.usuarioCriaPost);
        String conteudo = postEditText.getText().toString();
        EditText nickEditText = (EditText) findViewById(R.id.usuarioCriaNick);
        String nick = nickEditText.getText().toString();

        JSONArray usuarios = globalDBHelper.selectAllFromUsuarios(getApplicationContext());

        ArrayList<String> listaEmail = new ArrayList<String>();
        SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
        String emailUser = sp.getString("emailLogado",null);
        int deuCerto = globalDBHelper.insertIntoPostagem(getApplicationContext(), conteudo, nick, codGrupo, emailUser);
        if (deuCerto == 1) {
            gerarAlertDialog("Sua postagem realizada!", "Postagem concluída com sucesso!");

        } else {
            gerarAlertDialog("Posatagem não realizada!", "Sua postagem não pode ser concluída, verifique sua conexão com a Internet e tente novamente!");

        }
    }


//

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel("my_channel_id_01", "my_channel",  NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.createNotificationChannel(channel);
        }
    }


    public void gerarAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        DialogInterface.OnClickListener btnOk = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getBaseContext(), GrupoActivity.class);
                startActivity(intent);
            }
        };
        builder.setPositiveButton("Ok", btnOk);
        builder.create().show();
    }

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
}