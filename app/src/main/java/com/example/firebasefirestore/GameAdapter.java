package com.example.firebasefirestore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private List<Game> gameList;
    private Context context;

    public GameAdapter(Context context, List<Game> gameList) {
        this.context = context;
        this.gameList = gameList;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.game_item, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game game = gameList.get(position);

        String info = "Título: " + game.getTitle() + "\n" +
                "Desenvolvedor: " + game.getDeveloper() + "\n" +
                "Gênero: " + game.getGenre() + "\n" +
                "Ano: " + game.getReleaseYear() + "\n" +
                "Nota: " + game.getRating() + "/5.0";

        holder.textViewGameInfo.setText(info);

        holder.buttonDelete.setOnClickListener(v -> {
            FirebaseFirestore.getInstance().collection("games")
                    .document(game.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Jogo deletado!", Toast.LENGTH_SHORT).show();
                        gameList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, gameList.size()); // 🔥 Add this line
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, "Erro ao deletar: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });

    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder {
        TextView textViewGameInfo;
        Button buttonDelete;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewGameInfo = itemView.findViewById(R.id.textViewGameInfo);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
