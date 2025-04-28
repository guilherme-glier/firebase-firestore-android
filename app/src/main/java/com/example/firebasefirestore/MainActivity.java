package com.example.firebasefirestore;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDeveloper, editTextGenre, editTextReleaseYear, editTextRating;
    private Button buttonAdd, buttonGet;
    private RecyclerView recyclerViewGames;

    private FirebaseFirestore db;
    private static final String COLLECTION_GAMES = "games";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        initializeViews();
        setClickListeners();
    }

    private void initializeViews() {
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDeveloper = findViewById(R.id.editTextDeveloper);
        editTextGenre = findViewById(R.id.editTextGenre);
        editTextReleaseYear = findViewById(R.id.editTextReleaseYear);
        editTextRating = findViewById(R.id.editTextRating);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonGet = findViewById(R.id.buttonGet);
        recyclerViewGames = findViewById(R.id.recyclerViewGames);
        recyclerViewGames.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setClickListeners() {
        buttonAdd.setOnClickListener(v -> addGame());
        buttonGet.setOnClickListener(v -> getAllGames());
    }

    private void addGame() {
        String title = editTextTitle.getText().toString().trim();
        String developer = editTextDeveloper.getText().toString().trim();
        String genre = editTextGenre.getText().toString().trim();
        String releaseYearStr = editTextReleaseYear.getText().toString().trim();
        String ratingStr = editTextRating.getText().toString().trim();

        if (title.isEmpty() || developer.isEmpty() || genre.isEmpty() || releaseYearStr.isEmpty() || ratingStr.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        int releaseYear = Integer.parseInt(releaseYearStr);
        double rating = Double.parseDouble(ratingStr);

        if (rating < 0 || rating > 5) {
            Toast.makeText(this, "Avaliação deve ser entre 0 e 5!", Toast.LENGTH_SHORT).show();
            return;
        }

        Game game = new Game(title, developer, genre, releaseYear, rating);

        db.collection(COLLECTION_GAMES)
                .add(game)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Jogo adicionado com sucesso!", Toast.LENGTH_SHORT).show();
                    clearForm();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Erro ao adicionar: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void clearForm() {
        editTextTitle.setText("");
        editTextDeveloper.setText("");
        editTextGenre.setText("");
        editTextReleaseYear.setText("");
        editTextRating.setText("");
    }

    private void getAllGames() {
        db.collection(COLLECTION_GAMES)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Game> gameList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Game game = document.toObject(Game.class);
                            game.setId(document.getId());
                            gameList.add(game);
                        }
                        displayGames(gameList);
                    } else {
                        Toast.makeText(this, "Erro ao buscar jogos!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayGames(List<Game> games) {
        GameAdapter adapter = new GameAdapter(this, games);
        recyclerViewGames.setAdapter(adapter);
    }
}
