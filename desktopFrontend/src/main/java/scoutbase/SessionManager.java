package scoutbase;

public class SessionManager {

    private static String authToken;
    private static String sessionId;
    private static String username;
    private static String role;

    private SessionManager() {
    }

    public static void saveSession(String token, String session, String user, String userRole) {
        authToken = token;
        sessionId = session;
        username = user;
        role = userRole;
    }

    public static String getAuthToken() {
        return authToken;
    }

    public static String getSessionId() {
        return sessionId;
    }

    public static String getUsername() {
        return username;
    }

    public static String getRole() {
        return role;
    }

    public static boolean isLoggedIn() {
        return authToken != null && !authToken.isBlank();
    }

    public static void clear() {
        authToken = null;
        sessionId = null;
        username = null;
        role = null;
    }
}