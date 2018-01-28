package christian.ispyai.Firebase;

import android.support.annotation.NonNull;

public class Player implements Comparable {

    public String username;
    public int score;

    public Player() {}

    public Player(String username, int score) {
        this.username = username;
        this.score = score;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Player op = (Player) o;
        if (score == op.score) {
            return 0;
        }
        if (score < op.score) {
            return -1;
        }
        return 1;
    }
}
