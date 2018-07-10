package br.com.marcio.filmes.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

import br.com.marcio.filmes.R;
import br.com.marcio.filmes.model.Filme;
import br.com.marcio.filmes.model.FilmesDatabase;

public class FilmeAdapterVoto extends RecyclerView.Adapter<FilmeAdapterVoto.MyViewHolder>{

    private List<Filme> lista;

    public FilmeAdapterVoto(List<Filme> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_filme_item_voto, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Filme filme = lista.get(position);
        holder.txtFilme.setText(filme.getNome());

        holder.cbVoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Filme filme = lista.get(position);
                filme.setVotos(filme.getVotos() + 1);

                DatabaseReference filmesFirebase = FilmesDatabase.getFirebaseDatabase();
                filmesFirebase.child(filme.getId()).setValue(filme);

            }
        });
    }


    @Override
    public int getItemCount() {
        return this.lista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtFilme;
        CheckBox cbVoto;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtFilme = itemView.findViewById(R.id.txtFilmeVotoId);
            cbVoto = itemView.findViewById(R.id.cbVotoId);

        }
    }
}