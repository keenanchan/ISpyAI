package christian.ispyai;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.microsoft.projectoxford.vision.contract.Tag;

import java.util.List;

import christian.ispyai.Adapter.MainPagerAdapter;
import christian.ispyai.Firebase.Player;
import christian.ispyai.Fragments.Camera.MainFragment;
import christian.ispyai.Fragments.MissionFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.OnGuessListener {

    private final FirebaseDatabase db = FirebaseDatabase.getInstance();

    private ViewPager viewPager;
    private MainPagerAdapter adapter;

    private String target;
    private Boolean found;

    private String gameID, username;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.am_view_pager);
        adapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        gameID = getIntent().getStringExtra("GameID");
        username = getIntent().getStringExtra("Username");

        db.getReference("games").child(gameID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals("target")) {
                    target = dataSnapshot.getValue(String.class);
                    Toast.makeText(getApplicationContext(), "Target updated to: " + target, Toast.LENGTH_SHORT).show();
                    if (viewPager.getCurrentItem() == 2) {
                        ((MissionFragment) adapter.getCurrentFragment()).updateMission(target);
                    }
                    found = false;
                    Button captureBtn = findViewById(R.id.picture);
                    if (captureBtn != null) {
                        captureBtn.setEnabled(true);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals("target")) {
                    target = dataSnapshot.getValue(String.class);
                    Toast.makeText(getApplicationContext(), "Target updated to: " + target, Toast.LENGTH_SHORT).show();
                    if (viewPager.getCurrentItem() == 2) {
                        ((MissionFragment) adapter.getCurrentFragment()).updateMission(target);
                    }
                    found = false;
                    Button captureBtn = findViewById(R.id.picture);
                    if (captureBtn != null) {
                        captureBtn.setEnabled(true);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        db.getReference("games").child(gameID).child("participants").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Player p = dataSnapshot.getValue(Player.class);
                if (p != null) {
                    score = p.score;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed to read from the database: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onGuess(List<Tag> tags) {
        if (found) {
            return;
        }

        for (Tag t : tags) {
            Log.d("MainActivity", t.name + " " + t.confidence);
            if (t.name.equals(target)) {
                Toast.makeText(getApplicationContext(), "Found! Wait for the next target...", Toast.LENGTH_SHORT).show();
                found = true;

                score++;
                db.getReference("games").child(gameID).child("participants").child(username).child("score").setValue(score).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Score updated successfully!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Score couldn't be updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                return;
            }
        }

        Toast.makeText(getApplicationContext(), "Not found!", Toast.LENGTH_SHORT).show();
        Button captureBtn = findViewById(R.id.picture);
        captureBtn.setEnabled(true);
    }

    public String getMission() {
        return target;
    }
}
