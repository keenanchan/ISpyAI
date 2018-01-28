package christian.ispyai;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import christian.ispyai.Firebase.Player;

public class Firstscreen extends AppCompatActivity {
    private final FirebaseDatabase db = FirebaseDatabase.getInstance();

    private Button btnJoin;
    private Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstscreen);
        btnJoin = findViewById(R.id.btnJoin);
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText a = findViewById(R.id.Username);
                String username = a.getText().toString();

                EditText b = findViewById(R.id.game_ID);
                String gameID = b.getText().toString();

                createUserInGame(username, gameID, false);

            }
        });
        btnCreate = findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText a = findViewById(R.id.Username);
                String username = a.getText().toString();

                createGame(username);
            }
        });
    }

    private void createGame(final String username) {
        createUserInGame(username, Integer.toString(new Random().nextInt(1000)), true);
    }

    private void createUserInGame(final String username, final String gameID, boolean isHost) {
        if (!isHost) {
            final DatabaseReference ref = db.getReference("games").child(gameID).child("participants");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild(username)) {
                        ref.child(username).setValue(new Player(username, 0)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    launchMainActivity(username, gameID);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Failed to write to the database", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        launchMainActivity(username, gameID);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Failed to read from the database: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            final DatabaseReference ref = db.getReference("games").child(gameID).child("organiser");
            ref.setValue(username).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        launchHostActivity(username, gameID);
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to write to the database", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void launchHostActivity(String username, String gameID) {
        Intent i = new Intent(Firstscreen.this, HostActivity.class);
        i.putExtra("Username", username);
        i.putExtra("GameID", gameID);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(i);
        Firstscreen.this.finish();
    }

    private void launchMainActivity(String username, String gameID) {
        Intent i = new Intent(Firstscreen.this, MainActivity.class);
        i.putExtra("Username", username);
        i.putExtra("GameID", gameID);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(i);
        Firstscreen.this.finish();
    }
}
