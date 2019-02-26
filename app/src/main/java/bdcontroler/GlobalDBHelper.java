package bdcontroler;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class GlobalDBHelper {

    private static String URL_GLOBAL_DB = "http://10.21.80.175/webService/";

    public int insertIntoUsuarios(Context context, String email, String senha) throws IOException {
        if (!checkNetworkConnection(context)) {
            return 0;
        }
        checkThreadPolicy();
        String values = "email="+email+"&"+"senha="+senha;
        URL url = new URL(URL_GLOBAL_DB + "ws_insert/ws_insert_usuario.php?"+values);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String response = bufferedReader.readLine();
        if (response.equals("false")) {
            Toast.makeText(context, "Erro no BD Global!", Toast.LENGTH_LONG).show();
            return 0;
        } else {
            return 1;
        }
    }


    public int insertIntoPostagem(Context context, String conteudo, String nick, String grupo_cod, String emailUser) throws IOException {
        if (!checkNetworkConnection(context)) {
            return 0;
        }
        checkThreadPolicy();
        String values = "conteudo="+conteudo+"&nick="+nick+"&grupo_has_usuario_grupo_cod="+
                grupo_cod+"&grupo_has_usuario_usuario_email="+emailUser+"";
        Log.d("HUEHUE", URL_GLOBAL_DB + "ws_insert/ws_insert_postagem.php?"+values);
        URL url = new URL(URL_GLOBAL_DB + "ws_insert/ws_insert_postagem.php?"+values);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String response = bufferedReader.readLine();
        if (response.equals("false")) {
            Toast.makeText(context, "Erro no BD Global!", Toast.LENGTH_LONG).show();
            return 0;
        } else {
            return 1;
        }
    }

    public int insertGrupo(Context context, String nome, String senha, String tema_tema, String usuario_email) throws IOException {
        if (!checkNetworkConnection(context)) {
            return 0;
        }
        checkThreadPolicy();
        String values = "nome="+nome+"&tema_tema="+tema_tema+"&senha"+senha+"&usuario_email="+ usuario_email;
        Log.d("HUEHUE", URL_GLOBAL_DB + "ws_insert/ws_insert_grupo.php?"+values);
        URL url = new URL(URL_GLOBAL_DB + "ws_insert/ws_insert_grupo.php?"+values);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String response = bufferedReader.readLine();
        if (response.equals("false")) {
            Toast.makeText(context, "Erro no BD Global!", Toast.LENGTH_LONG).show();
            return 0;
        } else {
            return 1;
        }
    }

//    public int insertIntoComentario(Context context, String cod, String conteudo, String data_hora, String nick, String grupo_cod, String emailUser) throws IOException {
//        if (!checkNetworkConnection(context)) {
//            return 0;
//        }
//        checkThreadPolicy();
//        String values = "cod="+cod+"&"+"conteudo="+conteudo+"&"+"data_hora="+data_hora+"&"+"nick=+"+nick+"&"
//                +"grupo_has_usuario_grupo_cod="+grupo_cod+"grupo_has_usuario_usuario_email="+emailUser;
//        URL url = new URL(URL_GLOBAL_DB + "ws_insert/ws_insert_usuario.php?"+values);
//        HttpURLConnection con = (HttpURLConnection) url.openConnection();
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//        String response = bufferedReader.readLine();
//        if (response.equals("false")) {
//            Toast.makeText(context, "Erro no BD Global!", Toast.LENGTH_LONG).show();
//            return 0;
//        } else {
//            return 1;
//        }
//    }
     public void updateUsuarios(Context context, String email) throws IOException {
        if (!checkNetworkConnection(context)) {
            return;
        }
        checkThreadPolicy();
        URL url = new URL(URL_GLOBAL_DB + "ws_update/ws_update.php?email="+email);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String response = bufferedReader.readLine();
        if (response.equals("false")) {
            Toast.makeText(context, "Erro no BD Global!", Toast.LENGTH_LONG).show();
        }
    }

    public JSONArray selectAllFromUsuarios(Context context) throws JSONException, IOException {
        if (!checkNetworkConnection(context)) {
            return null;
        }
        checkThreadPolicy();
        URL url = new URL(URL_GLOBAL_DB + "ws_read/ws_read_usuario.php");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String json;
        while ((json = bufferedReader.readLine()) != null) {
            sb.append(json + "\n");
        }
        JSONArray jsonArray = new JSONArray(sb.toString().trim());
        return jsonArray;
    }
    public JSONArray selectAllFromGrupos(Context context) throws JSONException, IOException {
        if (!checkNetworkConnection(context)) {
            return null;
        }
        checkThreadPolicy();
        URL url = new URL(URL_GLOBAL_DB + "ws_read/ws_read_grupo.php");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String json;
        while ((json = bufferedReader.readLine()) != null) {
            sb.append(json + "\n");
        }
        JSONArray jsonArray = new JSONArray(sb.toString().trim());
        return jsonArray;
    }

    public JSONArray selectUserInfo(Context context, String email) throws JSONException, IOException {
        if (!checkNetworkConnection(context)) {
            return null;
        }
        checkThreadPolicy();
        URL url = new URL(URL_GLOBAL_DB + "ws_read/ws_read_usuario_email_digitado.php?email='"+email+"'");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String json;
        while ((json = bufferedReader.readLine()) != null) {
            sb.append(json + "\n");
        }
        JSONArray jsonArray = new JSONArray(sb.toString().trim());
        return jsonArray;
    }

    public JSONArray selectAllFromPostagem(Context context) throws JSONException, IOException {
        if (!checkNetworkConnection(context)) {
            return null;
        }
        checkThreadPolicy();
        URL url = new URL(URL_GLOBAL_DB + "ws_read/ws_read_postagem.php");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String json;
        while ((json = bufferedReader.readLine()) != null) {
            sb.append(json + "\n");
        }
        JSONArray jsonArray = new JSONArray(sb.toString().trim());
        return jsonArray;
    }

    public JSONArray usuarioParticipa(Context context, String email) throws JSONException, IOException {
        if (!checkNetworkConnection(context)) {
            return null;
        }
        checkThreadPolicy();
        URL url = new URL(URL_GLOBAL_DB + "ws_read/ws_read_usuario_participa.php?email='"+email+"'");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String json;
        while ((json = bufferedReader.readLine()) != null) {
            sb.append(json + "\n");
        }
        JSONArray jsonArray = new JSONArray(sb.toString().trim());
        return jsonArray;
    }
    public JSONArray postGrupo(Context context, String cod) throws JSONException, IOException {
        if (!checkNetworkConnection(context)) {
            return null;
        }
        checkThreadPolicy();
        URL url = new URL(URL_GLOBAL_DB + "ws_read/ws_read_posts_grupo.php?cod='"+cod+"'");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String json;
        while ((json = bufferedReader.readLine()) != null) {
            sb.append(json + "\n");
        }
        JSONArray jsonArray = new JSONArray(sb.toString().trim());
        return jsonArray;
    }


    private boolean checkNetworkConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    private void checkThreadPolicy(){
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
}
