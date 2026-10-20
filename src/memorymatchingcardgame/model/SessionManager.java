package memorymatchingcardgame.model;

/**
 * Singleton for storing a logged-in user session.
 */
public final class SessionManager {

    private static SessionManager instance;
    private User currentUser;

    private SessionManager() {
    }

    // Get the instance singleton SessionManager
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // Store the successfully logged-in user
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    // Get user that is logged-in
    public User getCurrentUser() {
        return currentUser;
    }

    // Check whether the user already logged-in
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    // Delete session on logout
    public void clear() {
        currentUser = null;
    }
}
