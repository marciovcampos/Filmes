package br.com.marcio.filmes.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class FilmesDatabase {

    private static DatabaseReference firebase;

    public static DatabaseReference getFirebaseDatabase(){

        if(firebase == null) {
            firebase = FirebaseDatabase.getInstance().getReference("filmes");
        }

        return firebase;
    }
}
