package dominando.android.ex21_http.ui.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;
import dominando.android.ex21_http.LivroHTTP;
import dominando.android.ex21_http.R;
import dominando.android.ex21_http.models.Livro;
import dominando.android.ex21_http.ui.adapters.LivrosListAdapter;

public class LivrosListFragment extends InternetFragment {
    LivrosTask mTask;
    List<Livro> mLivros;
    ListView mListView;
    TextView mTextMesagem;
    ProgressBar mProgressBar;
    LivrosListAdapter mAdapter;

    @Override public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View layout = inflater.inflate(R.layout.fragment_livros_list, null);
        mTextMesagem = layout.findViewById(android.R.id.empty);
        mProgressBar = layout.findViewById(R.id.progressBar);
        mListView = layout.findViewById(R.id.listView);
        mListView.setEmptyView(mTextMesagem);
        return layout;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if(mLivros == null){
            mLivros = new ArrayList<>();
        }
        mAdapter =new LivrosListAdapter(getActivity(), mLivros);
        mListView.setAdapter(mAdapter);

        if(mTask == null){
            //App executado pela primeira vez.
            if(LivroHTTP.temConexao(getActivity())){
                iniciarDownload();
            }else{
                mTextMesagem.setText("Sem conexão");
            }
        }else if(mTask.getStatus() == AsyncTask.Status.RUNNING){
            exibirProgress(true);
        }
    }

    @Override public void iniciarDownload() {
        if(mTask ==null || mTask.getStatus() != AsyncTask.Status.RUNNING){
            mTask = new LivrosTask();
            mTask.execute();
        }
    }

    private void exibirProgress(boolean b) {
        if(b){
            mTextMesagem.setText("Baixando informações dos livros...");
        }
        mTextMesagem.setVisibility(b? View.VISIBLE: View.GONE);
        mProgressBar.setVisibility(b? View.VISIBLE: View.GONE);
    }
    class LivrosTask extends AsyncTask<Void, Void, List<Livro>>{
        @Override protected void onPreExecute(){
            super.onPreExecute();
            exibirProgress(true);
        }
        @Override protected List<Livro> doInBackground(Void... strings){
            return LivroHTTP.carregarLivrosJson();
        }

        @Override protected void onPostExecute(List<Livro> livros){
            super.onPostExecute(livros);
            exibirProgress(false);

            if(livros != null){
                mLivros.clear();
                mLivros.addAll(livros);
                mAdapter.notifyDataSetChanged();
            }else{
                mTextMesagem.setText("Falha ao obter os livros");
            }
        }
    }
}
