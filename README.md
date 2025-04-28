## 🚀 Como configurar o Firebase Firestore no seu projeto Android 🛠️

### 1. Criar um projeto no Firebase Console 🔥

1. Acesse o [Firebase Console](https://console.firebase.google.com/).
2. Clique em **Adicionar projeto** e siga as instruções para criar um novo projeto ou adicionar o Firebase a um projeto existente.

### 2. Adicionar seu aplicativo Android ao projeto Firebase 📲

1. No painel do Firebase, clique no ícone do Android para adicionar um novo aplicativo Android.
2. Insira o nome do pacote do seu aplicativo (encontrado em `app/build.gradle`).
3. (Opcional) Adicione um apelido para o aplicativo e o certificado de assinatura SHA-1.
4. Clique em **Registrar aplicativo**.

### 3. Baixar e configurar o arquivo `google-services.json` 📄

1. Após registrar o aplicativo, faça o download do arquivo `google-services.json`.
2. Coloque este arquivo na pasta `app/` do seu projeto Android.

### 4. Modificar os arquivos `build.gradle` 📑

No arquivo `build.gradle` (nível de projeto), adicione o classpath do plugin do Google Services:

```gradle
classpath 'com.google.gms:google-services:4.3.15'
```

No arquivo `build.gradle` (nível de aplicativo), aplique o plugin e adicione as dependências do Firebase:

```gradle
apply plugin: 'com.google.gms.google-services'

dependencies {
    implementation platform('com.google.firebase:firebase-bom:33.12.0')
    implementation 'com.google.firebase:firebase-firestore'
    implementation 'com.google.firebase:firebase-auth'
}
```

### 5. Sincronizar o projeto com o Gradle 🔄

Clique em **Sync Now** no Android Studio para sincronizar o projeto com as dependências.

---

## 🧪 Como utilizar o Firestore no seu aplicativo Android 💡

### 1. Inicializar o Firestore 🧯

No seu código Java, inicialize o Firestore:

```java
FirebaseFirestore db = FirebaseFirestore.getInstance();
```

### 2. Classe `Game` 🎮

A classe `Game` é usada para armazenar as informações do jogo, como título, desenvolvedor, gênero, ano de lançamento e avaliação. Aqui está a estrutura da classe:

```java
public class Game {
    private String id;
    private String title;
    private String developer;
    private String genre;
    private int releaseYear;
    private double rating;

    // Constructor
    public Game(String title, String developer, String genre, int releaseYear, double rating) {
        this.title = title;
        this.developer = developer;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.rating = rating;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Outros getters e setters...
}
```

### 3. Adicionar jogos no Firestore ➕

Para adicionar um jogo ao Firestore, preencha os campos de entrada e envie para o banco de dados:

```java
private void addGame() {
    String title = editTextTitle.getText().toString().trim();
    String developer = editTextDeveloper.getText().toString().trim();
    String genre = editTextGenre.getText().toString().trim();
    String releaseYearStr = editTextReleaseYear.getText().toString().trim();
    String ratingStr = editTextRating.getText().toString().trim();

    // Validação dos campos
    if (title.isEmpty() || developer.isEmpty() || genre.isEmpty() || releaseYearStr.isEmpty() || ratingStr.isEmpty()) {
        Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
        return;
    }

    int releaseYear = Integer.parseInt(releaseYearStr);
    double rating = Double.parseDouble(ratingStr);

    // Criação do objeto Game
    Game game = new Game(title, developer, genre, releaseYear, rating);

    // Adiciona o jogo no Firestore
    db.collection("games")
        .add(game)
        .addOnSuccessListener(documentReference -> {
            Toast.makeText(this, "Jogo adicionado com sucesso!", Toast.LENGTH_SHORT).show();
            clearForm();
        })
        .addOnFailureListener(e -> Toast.makeText(this, "Erro ao adicionar: " + e.getMessage(), Toast.LENGTH_SHORT).show());
}
```

### 4. Recuperar todos os jogos do Firestore 📥

Para recuperar todos os jogos armazenados no Firestore e exibi-los em uma lista:

```java
private void getAllGames() {
    db.collection("games")
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
```

---

## 📸 Imagem do App em Funcionamento 🎥

Veja abaixo como o aplicativo se parece em funcionamento:

![App em funcionamento](![image](https://github.com/user-attachments/assets/214a4a98-0613-4ca2-8d70-872d4274bd2b))

---

## 🔐 Regras de segurança do Firestore 🔒

Para proteger seus dados, configure as regras de segurança no Firebase Console corretamente para produção. Nesse projeto, como é um teste, ele está com a confiuração NÃO segura:

```plaintext
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if false;
    }
    match /games/{gameId} {
      allow read, write: if true;
    }
  }
}
```

---

## 📊 Casos de uso do Firestore 🚀

O Firestore é ideal para:

- **Aplicativos de bate-papo em tempo real** 🗨️: Sincronização instantânea de mensagens entre usuários.
- **Aplicativos de redes sociais** 📸: Armazenamento de postagens, comentários e curtidas.
- **Aplicativos de e-commerce** 🛒: Gerenciamento de inventário, pedidos e usuários.
- **Aplicativos de produtividade** 📅: Armazenamento de tarefas, notas e calendários.
