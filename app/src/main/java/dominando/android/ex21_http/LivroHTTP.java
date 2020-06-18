package dominando.android.ex21_http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import dominando.android.ex21_http.models.Livro;

public class LivroHTTP {
    public static final String LIVROS_URL_JSON =
            "https://raw.githubusercontent.com/nglauber/" +"dominando_android/master/livros_novatec.json";

    private static HttpURLConnection conectar(String urlArquivo) throws IOException {
        try{
            final int SEGUNDOS = 1000;
            URL url = new URL(urlArquivo);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setReadTimeout(10 * SEGUNDOS);
            con.setConnectTimeout(15 * SEGUNDOS);
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(false);
            con.connect();
            return con;
        }catch(Exception ex){
            Log.e("TAG", "conectar: "+ex.getMessage());
            return null;
        }
    }
    public static boolean temConexao(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm == null){ return false; }
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }
    public static List<Livro> carregarLivrosJson(){
        try {
            HttpURLConnection con = conectar(LIVROS_URL_JSON);
            int res = con.getResponseCode();

            if(res == HttpURLConnection.HTTP_OK){
                InputStream is = con.getInputStream();
                JSONObject json = new JSONObject(bytesParaString(is));
                return lerJsonLivros(json);
            }
        }
        catch(Exception ex){
            Log.e("TAG", "carregarLivrosJson: "+ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }
    private static String bytesParaString(InputStream is) throws IOException{
        byte[] buffer =new byte[1024];
        ByteArrayOutputStream bufferzao = new ByteArrayOutputStream();
        int bytesLidos;
        while((bytesLidos = is.read(buffer)) != -1){
            bufferzao.write(buffer, 0, bytesLidos);
        }
        return new String(bufferzao.toByteArray(), StandardCharsets.UTF_8);
    }
    public static List<Livro> lerJsonLivros(JSONObject json) throws JSONException {
        List<Livro> lista = new ArrayList<>();
        String categoriaAtual;

        JSONArray jsonNovatec = json.getJSONArray("novatec");
        for(int i = 0; i< jsonNovatec.length(); i++){
            JSONObject categoria = jsonNovatec.getJSONObject(i);
            categoriaAtual = categoria.getString("categoria");

            JSONArray livros = categoria.getJSONArray("livros");
            for(int j =0; j<livros.length(); j++){
                JSONObject l = livros.getJSONObject(j);
                lista.add(new Livro(
                        l.getString("titulo"),
                        categoriaAtual,
                        l.getString("autor"),
                        l.getInt("ano"),
                        l.getInt("paginas"),
                        l.getString("capa")
                ));
            }
        }
        return lista;
    }
}
