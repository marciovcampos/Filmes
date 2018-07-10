package br.com.marcio.filmes.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.marcio.filmes.R;
import br.com.marcio.filmes.adapter.FilmeAdapter;
import br.com.marcio.filmes.model.Filme;
import br.com.marcio.filmes.model.FilmesDatabase;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference filmesFirebase;

    private RecyclerView recyclerView;
    private FilmeAdapter filmeAdapter;
    private List<Filme> listaFilmes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // INSTANCIA DO BANCO DE DADOS FIREBASE
        filmesFirebase = FilmesDatabase.getFirebaseDatabase();

        // INSTANCIA DO FILMEADAPTER
        filmeAdapter = new FilmeAdapter(listaFilmes);

        // CONFIGURACAO DO RECYCLERVIEW
        recyclerView = findViewById(R.id.recyclerViewId);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                LinearLayout.VERTICAL));
        recyclerView.setAdapter( filmeAdapter );

        filmesFirebase.orderByChild("votos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listaFilmes.clear();

                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    Filme filme = dados.getValue(Filme.class);
                    listaFilmes.add(filme);
                }

                filmeAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.mnVotoId) {

            Intent intent = new Intent(MainActivity.this, VotoActivity.class);
            startActivity(intent);

        } else if (id == R.id.mnAddFilmeId) {

            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
            View viewDigalog = layoutInflater.inflate(R.layout.input_filme, null);

            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

            dialog.setView(viewDigalog);
            final EditText edtFilme = viewDigalog.findViewById(R.id.edtFilmeId);

            dialog.setTitle("Adicionar um novo filme");

            dialog.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    AddFilme(edtFilme.getText().toString());

                }
            });

            dialog.setNegativeButton("Cancelar", null);

            AlertDialog alertDialog =  dialog.create();
            alertDialog.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void AddFilme(String nomeFilme){

        if(nomeFilme == null || nomeFilme.equals("")){
            print("Obrigatório informar o nome do Filme! Filme não cadastrado!");
        }else{

            final Filme filme = new Filme(nomeFilme);

            filmesFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    boolean filmeJaCadastrado = dataSnapshot.hasChild(filme.getId());

                    if(filmeJaCadastrado){
                        print("Filme já cadastrado!");
                    }else{
                        filmesFirebase.child(filme.getId()).setValue(filme);
                        print("Filme cadastrado com sucesso!");
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {  }
            });

        }



    }

    public void print(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
