package christian.ispyai;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by tgkokk on 28/01/2018.
 */

public class HostActivity extends AppCompatActivity {
    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private String gameID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        gameID = getIntent().getStringExtra("GameID");

        TextView gameIDTV = findViewById(R.id.TVGameIDHost);
        gameIDTV.setText(gameID);

        Button btn = findViewById(R.id.update_target);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topic = ((EditText) findViewById(R.id.target)).getText().toString();
                db.getReference("games").child(gameID).child("target").setValue(topic).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Target updated successfully", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Target failed to update", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
