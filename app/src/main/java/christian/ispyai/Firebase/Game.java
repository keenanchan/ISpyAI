package christian.ispyai.Firebase;

import java.util.List;

public class Game {
    public String gameID;
    public String organizer;
    public List<String> participants;

    public Game(String gameID, String organizer, List<String> participants) {
        this.gameID = gameID;
        this.organizer = organizer;
        this.participants = participants;
    }
}