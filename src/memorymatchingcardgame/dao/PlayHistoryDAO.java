package memorymatchingcardgame.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import memorymatchingcardgame.ConnectionHelper;
import memorymatchingcardgame.model.LeaderboardEntry;
import memorymatchingcardgame.model.PlayHistory;

/**
 * DAO for database table operation on play_history.
 */
public class PlayHistoryDAO {

    // Store the game result to play_history table
    public boolean saveResult(int userId, int score, int durationSeconds, String difficulty)
            throws SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID tidak valid.");
        }
        if (difficulty == null || difficulty.trim().isEmpty()) {
            throw new IllegalArgumentException("Difficulty tidak boleh kosong.");
        }

        Connection conn = ConnectionHelper.getConnection();
        if (conn == null) {
            return false;
        }

        String sql = "INSERT INTO play_history (user_id, score, duration_seconds, difficulty) "
                + "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, score);
            ps.setInt(3, durationSeconds);
            ps.setString(4, difficulty);
            ps.executeUpdate();
            return true;
        }
    }

    // Retrieve top 10 leaderboard score with difficulty filter
    public List<LeaderboardEntry> getTopScores(String difficultyFilter) throws SQLException {
        List<LeaderboardEntry> entries = new ArrayList<>();

        Connection conn = ConnectionHelper.getConnection();
        if (conn == null) {
            return entries;
        }

        StringBuilder sql = new StringBuilder(
                "SELECT u.username, ph.score, ph.duration_seconds, ph.difficulty, ph.played_at "
                + "FROM play_history ph "
                + "INNER JOIN users u ON ph.user_id = u.id ");

        boolean filterDifficulty = difficultyFilter != null
                && !difficultyFilter.trim().isEmpty()
                && !"All".equalsIgnoreCase(difficultyFilter.trim());
        if (filterDifficulty) {
            sql.append("WHERE ph.difficulty = ? ");
        }
        sql.append("ORDER BY ph.score DESC, ph.duration_seconds ASC, ph.played_at DESC LIMIT 10");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            if (filterDifficulty) {
                ps.setString(1, difficultyFilter.trim());
            }
            try (ResultSet rs = ps.executeQuery()) {
                int rank = 1;
                while (rs.next()) {
                    entries.add(new LeaderboardEntry(
                            rank++,
                            rs.getString("username"),
                            rs.getInt("score"),
                            rs.getInt("duration_seconds"),
                            rs.getString("difficulty"),
                            rs.getString("played_at")
                    ));
                }
            }
        }
        return entries;
    }
    
    public void deleteHistory(int id) {
        String sql = "DELETE FROM play_history WHERE id=?";

        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<PlayHistory> getAllHistory() {
        List<PlayHistory> list = new ArrayList<>();

        String sql =
            "SELECT ph.id, ph.user_id, u.username, ph.score, ph.duration_seconds, ph.difficulty, ph.played_at " +
            "FROM play_history ph " +
            "JOIN users u ON ph.user_id = u.id";

        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PlayHistory p = new PlayHistory();

                p.setId(rs.getInt("id"));
                p.setUserId(rs.getInt("user_id"));
                p.setUsername(rs.getString("username"));
                p.setScore(rs.getInt("score"));
                p.setDuration(rs.getInt("duration_seconds"));
                p.setDifficulty(rs.getString("difficulty"));
                p.setPlayedAt(rs.getString("played_at"));

                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}