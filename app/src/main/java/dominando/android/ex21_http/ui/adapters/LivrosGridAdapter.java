package dominando.android.ex21_http.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import java.util.ArrayList;
import dominando.android.ex21_http.R;
import dominando.android.ex21_http.VolleySingleton;
import dominando.android.ex21_http.models.Livro;

public class LivrosGridAdapter extends ArrayAdapter<Livro> {
    private ImageLoader mLoader;

    public LivrosGridAdapter(Context context, ArrayList<Livro> livros){
        super(context, 0, livros);
        mLoader = VolleySingleton.getInstance(context).getImageLoader();
    }
    @Override public View getView(int pos, View convertView, ViewGroup parent){
        Context ctx = parent.getContext();
        if(convertView == null){
            convertView = LayoutInflater.from(ctx)
                    .inflate(R.layout.grid_item_livros, parent, false);
        }
        NetworkImageView img = convertView.findViewById(R.id.imgLivroCapa);
        TextView txt = convertView.findViewById(R.id.txtLivroTitulo);

        Livro l = getItem(pos);
        txt.setText(l.titulo);
        img.setImageUrl("https://images-na.ssl-images-amazon.com/images/I/71e39jzOunL.jpg", mLoader);
        return convertView;
    }
}

