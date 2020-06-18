package dominando.android.ex21_http.ui.fragments;

import android.app.ActivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import dominando.android.ex21_http.LivroHTTP;
import dominando.android.ex21_http.R;
import dominando.android.ex21_http.VolleySingleton;
import dominando.android.ex21_http.models.Livro;
import dominando.android.ex21_http.ui.adapters.LivrosGridAdapter;

public class LivrosGridFragment extends InternetFragment  implements
        Response.Listener<JSONObject>,
        Response.ErrorListener {

    ArrayList<Livro> livros;
    GridView gridView;
    ProgressBar progressBar;
    TextView textView;
    ArrayAdapter<Livro> adapter;
    boolean IsRunning;

    @Override public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View layout = inflater.inflate(R.layout.fragment_livros_grid, parent, false);
        progressBar = layout.findViewById(R.id.progressBarGrid);
        textView = layout.findViewById(android.R.id.empty);
        gridView = layout.findViewById(R.id.gridview);
        gridView.setEmptyView(textView);
        return layout;
    }
    @Override public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if(livros == null){
            livros = new ArrayList<Livro>();
        }
        adapter = new LivrosGridAdapter(getActivity(), livros);
        gridView.setAdapter(adapter);
        if(!IsRunning){
            if (LivroHTTP.temConexao(getActivity())) {
                iniciarDownload();
            }else{
                textView.setText("Sem conexão");
            }
        }else{
            exibirProgress(true);
        }
    }

    private void exibirProgress(boolean exibir) {
        if(exibir){
            textView.setText("Baixando informações dos livros...");
            textView.setVisibility(exibir ? View.VISIBLE : View.GONE);
            progressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
        }else{
            textView.setVisibility(exibir ? View.VISIBLE : View.GONE);
            progressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
        }
    }

    @Override public void iniciarDownload() {
        IsRunning=true;
        exibirProgress(true);
        RequestQueue q = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                LivroHTTP.LIVROS_URL_JSON,//Link de requesição
                null, // obj enviado, normal usado em POST
                this, //Interface Response.Listener
                this);//Interface Response.ErrorListener
        q.add(request);
    }

    @Override public void onErrorResponse(VolleyError volleyError){
        IsRunning = false;
        exibirProgress(false);
        textView.setText("Falha ao obter os livros...");
    }
    @Override public void onResponse(JSONObject jsonObject){
        IsRunning = false;
        exibirProgress(false);
        try{
            List<Livro> l =LivroHTTP.lerJsonLivros(jsonObject);
            livros.clear();
            livros.addAll(l);
            adapter.notifyDataSetChanged();
        }
        catch(JSONException ex){
            Log.e("TAG", "onResponse: "+ex.getMessage());
            ex.printStackTrace();
        }
    }
}
