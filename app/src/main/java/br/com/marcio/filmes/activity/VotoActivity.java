package br.com.marcio.filmes.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.marcio.filmes.R;
import br.com.marcio.filmes.adapter.FilmeAdapterVoto;
import br.com.marcio.filmes.model.Filme;
import br.com.marcio.filmes.model.FilmesDatabase;

public class VotoActivity extends AppCompatActivity {

    private RecyclerView recyclerViewVoto;
    private FilmeAdapterVoto filmeAdapterVoto;
    private List<Filme> listaFilmes = new ArrayList<>();
    private DatabaseReference filmesFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voto);

        // INSTANCIA DO BANCO DE DADOS FIREBASE
        filmesFirebase = FilmesDatabase.getFirebaseDatabase();

        // CONFIGURANDO O ADAPTER
        filmeAdapterVoto = new FilmeAdapterVoto(listaFilmes);

        // CONFIGURANDO O RECYCLERVIEW
        recyclerViewVoto = findViewById(R.id.recyclerViewVotoId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewVoto.setLayoutManager(layoutManager);
        recyclerViewVoto.setHasFixedSize(true);
        recyclerViewVoto.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                LinearLayout.VERTICAL));
        recyclerViewVoto.setAdapter( filmeAdapterVoto );


        filmesFirebase.orderByChild("nome").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaFilmes.clear();

                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    Filme filme = dados.getValue(Filme.class);
                    listaFilmes.add(filme);
                }

                filmeAdapterVoto.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
