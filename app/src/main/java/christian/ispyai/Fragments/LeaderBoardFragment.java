package christian.ispyai.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import christian.ispyai.Firebase.Player;
import christian.ispyai.R;

/**
 * Created by christianmaschka on 27/01/2018.
 */

public class LeaderBoardFragment extends BaseFragment {

    private final FirebaseDatabase db = FirebaseDatabase.getInstance();

    public static LeaderBoardFragment create() {
        return new LeaderBoardFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        String username = getActivity().getIntent().getStringExtra("Username");
        String gameID = getActivity().getIntent().getStringExtra("GameID");

        TextView gameIDTV = view.findViewById(R.id.TVGameID);
        TextView usernameTV = view.findViewById(R.id.TVUsername);
        gameIDTV.setText(gameID);
        usernameTV.setText(username);

        db.getReference("games").child(gameID).child("participants").orderByChild("score").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (isAdded()) {
                    List<Player> players = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Player p = snapshot.getValue(Player.class);
                        players.add(0, p);
                    }

                    TableLayout leaderboard = getView().findViewById(R.id.leaderboard);
                    leaderboard.removeAllViews();

                    for (Player p : players) {
                        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
                        TableRow row = new TableRow(getContext());

                        TextView usernameTV = new TextView(getContext());
                        usernameTV.setText(p.username);
                        usernameTV.setPadding(padding, padding, padding, padding);
                        row.addView(usernameTV);

                        TextView scoreTV = new TextView(getContext());
                        scoreTV.setText(Integer.toString(p.score));
                        usernameTV.setPadding(padding, padding, padding, padding);
                        scoreTV.setGravity(Gravity.RIGHT);
                        row.addView(scoreTV);

                        leaderboard.addView(row);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error while querying database: " + databaseError.getDetails(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_leaderboard;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    }
}
