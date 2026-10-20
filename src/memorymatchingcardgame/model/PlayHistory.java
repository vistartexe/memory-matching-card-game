package memorymatchingcardgame.model;

public class PlayHistory {

    private int id;
    private int userId;
    private String username;
    private int score;
    private int duration;
    private String difficulty;
    private String playedAt;

    public PlayHistory() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getPlayedAt() { return playedAt; }
    public void setPlayedAt(String playedAt) { this.playedAt = playedAt; }
}