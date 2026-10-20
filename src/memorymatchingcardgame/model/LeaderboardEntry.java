package memorymatchingcardgame.model;

/**
 * One row model of leaderboard data.
 */
public class LeaderboardEntry {

    private final int rank;
    private final String username;
    private final int score;
    private final int durationSeconds;
    private final String difficulty;
    private final String playedAt;

    public LeaderboardEntry(int rank, String username, int score,
            int durationSeconds, String difficulty, String playedAt) {
        this.rank = rank;
        this.username = username;
        this.score = score;
        this.durationSeconds = durationSeconds;
        this.difficulty = difficulty;
        this.playedAt = playedAt;
    }

    // Get rank
    public int getRank() {
        return rank;
    }

    // Get username
    public String getUsername() {
        return username;
    }

    // MGet score
    public int getScore() {
        return score;
    }

    // Get duration on seconds
    public int getDurationSeconds() {
        return durationSeconds;
    }

    // Get difficulty
    public String getDifficulty() {
        return difficulty;
    }

    // Get played date
    public String getPlayedAt() {
        return playedAt;
    }
}
