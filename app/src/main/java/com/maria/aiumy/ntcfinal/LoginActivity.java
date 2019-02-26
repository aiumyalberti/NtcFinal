package com.maria.aiumy.ntcfinal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import bdcontroler.GlobalDBHelper;

public class LoginActivity extends AppCompatActivity {
    private GlobalDBHelper globalDBHelper = new GlobalDBHelper();
    boolean encontrado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
        String emailLogado = sp.getString("emailLogado", null);
        String email = null;
//        try {
//            verificarUsuario(email);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
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


    public void clicarparaCadastrar (View v){
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }

    public void clicarparaLogin (View v){
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }
    private void verificarUsuario(String emailDigitado, String senhaDigitada) throws IOException, JSONException {
        JSONArray jsons = globalDBHelper.selectAllFromUsuarios(getApplicationContext());

        for (int i = 0; i < jsons.length(); i++) {
            JSONObject userObject = (JSONObject) jsons.get(i);
            String emailUser = userObject.getString("email");
            String senhaUser = userObject.getString("senha");

            if (emailUser.toUpperCase().equals(emailDigitado) && senhaUser.equals(senhaDigitada)) {
                encontrado = true;
//                isPasswordValid(true);
                break;
            }
        }
        if (encontrado) {
            gerarAlertDialog("LOGIN", "Login efetuado com sucesso");

            SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("emailLogado", emailDigitado);
            editor.apply();
            clicarparaLogin();

        }else{
            gerarAlertDialog("LOGIN", "ta errado issae");
        }
    }

    public String getUserEmail() throws IOException, JSONException {
        SharedPreferences sp = getSharedPreferences("dadosCompartilhados", Context.MODE_PRIVATE);
        String emailName = sp.getString("emailLogado",null);
        return emailName;
    }

    private void clicarparaLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void onClick (View v)throws JSONException, IOException {
        EditText emailText = (EditText) findViewById(R.id.loginEmail);
        String email = emailText.getText().toString();
        EditText senhaText = (EditText) findViewById(R.id.loginSenha);
        String senha = senhaText.getText().toString();
        verificarUsuario(email.toUpperCase(), senha);
    }

}
