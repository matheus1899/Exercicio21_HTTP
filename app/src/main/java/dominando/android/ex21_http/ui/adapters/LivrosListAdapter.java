package dominando.android.ex21_http.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import dominando.android.ex21_http.R;
import dominando.android.ex21_http.models.Livro;

public class LivrosListAdapter extends ArrayAdapter<Livro> {

    public LivrosListAdapter(Context context, List<Livro> livros){
        super(context, 0, livros);
    }
    @Override public View getView(int pos, View convertView, ViewGroup parent){
        Livro livro = getItem(pos);
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item_livros, parent,false);
            holder = new ViewHolder();
            holder.imgCapa = convertView.findViewById(R.id.imgCapa);
            holder.txtTitulo = convertView.findViewById(R.id.txtTitulo);
            holder.txtAutor = convertView.findViewById(R.id.txtAutor);
            holder.txtAno = convertView.findViewById(R.id.txtAno);
            holder.txtNPaginas = convertView.findViewById(R.id.txtNPaginas);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        Picasso.with(getContext()).load("https://images-na.ssl-images-amazon.com/images/I/71e39jzOunL.jpg")
                .into(holder.imgCapa);
        holder.txtTitulo.setText(livro.titulo);
        holder.txtAutor.setText(livro.autor);
        holder.txtAno.setText(String.valueOf(livro.ano));
        holder.txtNPaginas.setText(getContext().getString(R.string.n_paginas, livro.paginas));

        return convertView;
    }
    static class ViewHolder{
        ImageView imgCapa;
        TextView txtTitulo;
        TextView txtAutor;
        TextView txtAno;
        TextView txtNPaginas;
    }
}
