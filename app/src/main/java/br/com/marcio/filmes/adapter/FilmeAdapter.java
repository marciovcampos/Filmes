package br.com.marcio.filmes.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.marcio.filmes.R;
import br.com.marcio.filmes.model.Filme;

public class FilmeAdapter extends RecyclerView.Adapter<FilmeAdapter.MyViewHolder>{

    private List<Filme> lista;

    public FilmeAdapter(List<Filme> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_filme_item, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final Filme filme = lista.get(position);
        holder.txtFilme.setText(filme.getNome());
        holder.txtVoto.setText(String.valueOf(filme.getVotos()));
    }


    @Override
    public int getItemCount() {
        return this.lista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtFilme, txtVoto;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtFilme = itemView.findViewById(R.id.txtFilmeId);
            txtVoto = itemView.findViewById(R.id.txtVotosId);
        }
    }
}
