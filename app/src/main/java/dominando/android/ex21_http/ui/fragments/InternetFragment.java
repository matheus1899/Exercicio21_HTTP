package dominando.android.ex21_http.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import androidx.fragment.app.Fragment;
import dominando.android.ex21_http.LivroHTTP;

public abstract class InternetFragment extends Fragment {
    private ConexaoReceiver mReceiver;

    @Override public void onResume(){
        super.onResume();
        mReceiver = new ConexaoReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override public void onPause(){
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
    }

    public abstract void iniciarDownload();

    class ConexaoReceiver extends BroadcastReceiver{
        boolean primeiraVez = true;
        @Override public void onReceive(Context context, Intent intent){
            if(primeiraVez){
                primeiraVez = false;
                return;
            }
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
                if(LivroHTTP.temConexao(context)){
                    iniciarDownload();
                }
            }
        }
    }
}
