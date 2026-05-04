package es.dimecresalessis.scoutbase.infrastructure.routes;

/**
 * Centralized repository for API routing constants.
 */
public class Routes {
    public static final String API_ROOT = "/api/v1";
    public static final String USERS = "/users";
    public static final String PLAYERS = "/players";
    public static final String USER_TEAMS = "/user-teams";
    public static final String TEAMS = "/teams";
    public static final String STATS = "/stats";
    public static final String USER_CLUBS = "/user-clubs";
    public static final String CLUBS = "/clubs";
    public static final String HEALTH = "/health";

    public static final String AUTH_LOGIN = "/auth/login";
    public static final String ID_PATHVAR = "/{id}";
    public static final String ID_TWO_PATHVAR = "/{id2}";
    public static final String TEAM_ID_PATHVAR = "/userteam-id";
    public static final String USER_ID_PATHVAR = "/user-id";
    public static final String USERNAME_PATH = "/username";
    public static final String USERNAME_PATHVAR = "/{username}";
    public static final String ROLE_PATH = "/role";
    public static final String ADMIN_PATH = "/admin";
    public static final String TRAINER_PATH = "/trainer";
    public static final String SECOND_TRAINER_PATH = "/second-trainer";
    public static final String SCOUTER_PATH = "/scouter";

    public static final String ADD_PATH = "/add";
    public static final String REMOVE_PATH = "/remove";
    public static final String ME_PATH = "/me";
}