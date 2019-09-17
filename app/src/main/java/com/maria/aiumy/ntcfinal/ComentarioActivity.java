package com.maria.aiumy.ntcfinal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import bdcontroler.GlobalDBHelper;

public class ComentarioActivity extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener {

    String data;
    String comentarios_cod = null;
    String codGrupo;
    String postCod;

    private GlobalDBHelper globalDBHelper = new GlobalDBHelper();
    String emailUser;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override

    // oncreate
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
        emailUser = sp.getString("emailLogado",null);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setUserView();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



    // formatar a data
        Date date = new Date();

        DateFormat dateFormat = new SimpleDateFormat("HH");
        String hour = dateFormat.format(date);
        dateFormat = new SimpleDateFormat("mm");
        String minute = dateFormat.format(date);
        dateFormat = new SimpleDateFormat("ss");
        String second = dateFormat.format(date);

        dateFormat = new SimpleDateFormat("dd");
        String day = dateFormat.format(date);
        dateFormat = new SimpleDateFormat("MM");
        String mouth = dateFormat.format(date);
        dateFormat = new SimpleDateFormat("yyyy");
        String year = dateFormat.format(date);

        System.out.println(year+"-"+mouth+"-"+day+ " " +hour+ ":"+ minute+ ":"+ second);
        data = year+"-"+mouth+"-"+day+ "%20" +hour+ ":"+ minute+ ":"+ second;

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
        getMenuInflater().inflate(R.menu.comentario, menu);
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

    // INSERT COMENTARIOS
    public void comentar(View v) throws IOException, JSONException {
        EditText comentEditText = (EditText) findViewById(R.id.usuarioCriaComent);
        String conteudo = comentEditText.getText().toString();
        EditText nickEditText = (EditText) findViewById(R.id.usuarioCriaNick);
        String nick = nickEditText.getText().toString();

        SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
        String emailUser = sp.getString("emailLogado",null);

        Bundle bundle = getIntent().getExtras();
        postCod = bundle.getString("postCod");

        int deuCerto;

            deuCerto = globalDBHelper.insertIntoComentario(getApplicationContext(), nick, data, conteudo, emailUser, null, postCod);

        if (deuCerto == 1) {
            gerarAlertDialog("Seu comentário foi realizado!", "Comentário concluído com sucesso!");
        } else {
            gerarAlertDialog("Comentário não realizado!", "Seu comentário não pode ser concluído, verifique sua conexão com a Internet e tente novamente!");

        }
    }



    public void gerarAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        DialogInterface.OnClickListener btnOk = new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which)
            {

        }
        };
        builder.setPositiveButton("Ok", btnOk);
        builder.create().show();
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
