package com.maria.aiumy.ntcfinal;

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
import android.widget.EditText;

import com.maria.aiumy.ntcfinal.MainActivity;
import com.maria.aiumy.ntcfinal.R;

import java.io.IOException;

import bdcontroler.GlobalDBHelper;
import bdcontroler.LocalDBHelper;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class CadastroActivity extends Activity {

    private GlobalDBHelper globalDbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        globalDbHelper = new GlobalDBHelper();

    }

    public void botao(View view){
        MediaPlayer som_r2d2 = MediaPlayer.create(this,R.raw.r2d2);
        som_r2d2.start();
    }

    public void cadastrar(View view) throws IOException {
        EditText emailEditText = (EditText) findViewById(R.id.cadEmail);
        String email = emailEditText.getText().toString();
        if (email.trim().equals("")) {
            email = null;
        }
        EditText senhaEditText = (EditText) findViewById(R.id.cadSenha);
        String senha = senhaEditText.getText().toString();
        if (senha.trim().equals("")) {
            senha = null;
        }
        MediaPlayer som_r2d2 = MediaPlayer.create(this,R.raw.r2d2);
        som_r2d2.start();
        int deuCerto = globalDbHelper.insertIntoUsuarios(getApplicationContext(), email, senha);
        if (deuCerto == 1) {
            gerarAlertDialog("Cadastro gerado", "Cadastro efetivado com sucesso!");
//            gerarNotificacao("Cadastro concluído!", "Seu cadastro foi efetuado com sucesso!");
        } else {
            gerarAlertDialog("Cadastro não deu certo!", "Cadastro não efetivado com sucesso!");
//            gerarNotificacao("Cadastro não concluído!", "Seu cadastro não foi efetuado com sucesso!");
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel("my_channel_id_01", "my_channel",  NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.createNotificationChannel(channel);
        }
    }

//    public void gerarNotificacao(String titulo, String corpo) {
//        createNotificationChannel();
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "my_channel_id_01");
//        //mBuilder.setSmallIcon(R.drawable.ic_notification);
//        mBuilder.setAutoCancel(true);
//        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
//        mBuilder.setVibrate(new long[]{150, 300, 150, 600});
//        mBuilder.setContentTitle(titulo);
//        mBuilder.setContentText(corpo);
//
//        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        nm.notify(123456, mBuilder.build());
//    }

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
}
