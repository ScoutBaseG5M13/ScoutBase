package es.dimecresalessis.scoutbase.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumeration for Application Error Codes and Messages.
 * <p>
 * Defines application-specific error types, each with a unique code and message.
 * Messages support placeholder variables for dynamic error messaging.
 * </p>
 */
@AllArgsConstructor
@Getter
public enum ErrorEnum {
    PLAYER_NOT_FOUND("PLAYER_NOT_FOUND", "The player '{}' could not be found"),
    PLAYER_IS_NULL("PLAYER_IS_NULL", "The player can't be null"),
    PLAYER_ID_IS_NULL("PLAYER_ID_IS_NULL", "The player id can't be null"),
    PLAYER_ALREADY_EXISTS("PLAYER_ALREADY_EXISTS", "A player '{}' already exists"),
    PLAYER_BAD_FORMAT("PLAYER_BAD_FORMAT", "The player has bad format in fields: {}"),
    PLAYER_STAT_NOT_FOUND("PLAYER_STAT_NOT_FOUND", "The player stat with code '{}' could not be found"),

    ROLE_NOT_FOUND("ROLE_NOT_FOUND", "The role '{}' does not exist"),
    ROLE_NOT_FOUND_INFO("ROLE_NOT_FOUND", "The role '{}' does not exist. Please use one of the following roles: {}"),
    
    NO_USER_CLUB_HAS_BEEN_FOUND("NO_USER_CLUB_HAS_BEEN_FOUND", "No userclub has been found"),
    USER_CLUB_NOT_FOUND("USER_CLUB_NOT_FOUND", "The userclub '{}' could not be found"),
    USER_CLUB_IS_NULL("USER_CLUB_IS_NULL", "The userclub can't be null"),
    USER_CLUB_ID_IS_NULL("USER_CLUB_ID_IS_NULL", "The userclub id can't be null"),
    USER_CLUB_ALREADY_EXISTS("USER_CLUB_ALREADY_EXISTS", "A userclub '{}' already exists"),
    USER_CLUB_BAD_FORMAT("USER_CLUB_BAD_FORMAT", "The userclub has bad format in fields: {}"),

    USER_TEAM_NOT_FOUND("USER_TEAM_NOT_FOUND", "The userteam '{}' could not be found"),
    USER_TEAM_IS_NULL("USER_TEAM_IS_NULL", "The userteam can't be null"),
    USER_TEAM_ID_IS_NULL("USER_TEAM_ID_IS_NULL", "The userteam id can't be null"),
    USER_TEAM_ALREADY_EXISTS("USER_TEAM_ALREADY_EXISTS", "A userteam '{}' already exists"),
    USER_TEAM_BY_PLAYER_NOT_FOUND("USER_TEAM_BY_PLAYER_NOT_FOUND", "The userteam of the player '{}' can't be found"),

    NO_CLUB_HAS_BEEN_FOUND("NO_CLUB_HAS_BEEN_FOUND", "No club has been found"),
    CLUB_NOT_FOUND("CLUB_NOT_FOUND", "The club '{}' could not be found"),
    CLUB_IS_NULL("CLUB_IS_NULL", "The club can't be null"),
    CLUB_ID_IS_NULL("CLUB_ID_IS_NULL", "The club id can't be null"),
    CLUB_ALREADY_EXISTS("CLUB_ALREADY_EXISTS", "A club '{}' already exists"),
    CLUB_BAD_FORMAT("CLUB_BAD_FORMAT", "The club has bad format in fields: {}"),

    TEAM_NOT_FOUND("TEAM_NOT_FOUND", "The team '{}' could not be found"),
    TEAM_IS_NULL("TEAM_IS_NULL", "The team can't be null"),
    TEAM_ID_IS_NULL("TEAM_ID_IS_NULL", "The team id can't be null"),
    TEAM_ALREADY_EXISTS("TEAM_ALREADY_EXISTS", "A team '{}' already exists"),
    TEAM_BY_PLAYER_NOT_FOUND("TEAM_BY_PLAYER_NOT_FOUND", "The team of the player '{}' can't be found"),

    STAT_IS_NULL("STAT_IS_NULL", "The stat can't be null"),
    STAT_ID_IS_NULL("STAT_ID_IS_NULL", "The stat id can't be null"),
    STAT_ID_ALREADY_EXISTS("STAT_ALREADY_EXISTS", "The stat '{}' already exists"),
    STAT_CODE_ALREADY_EXISTS("STAT_ALREADY_EXISTS", "The stat with code '{}' already exists on player '{}'"),
    STAT_MUST_HAVE_PLAYER_ID("STAT_MUST_HAVE_PLAYER_ID", "The stat must have a 'playerId'"),
    STAT_NOT_FOUND("STAT_NOT_FOUND", "The stat '{}' could not be found"),
    STAT_INTEGRITY_ERROR("STAT_INTEGRITY_ERROR", "The stat has name '{}' but code '{}'. They have no correlation"),

    USER_NOT_FOUND("USER_NOT_FOUND", "The user '{}' could not be found"),
    USER_IS_NULL("USER_IS_NULL", "The user can't be null"),
    USER_ID_IS_NULL("USER_ID_IS_NULL", "The user id can't be null"),
    USER_ID_ALREADY_EXISTS("USER_ID_ALREADY_EXISTS", "A user '{}' already exists"),
    USERNAME_ALREADY_EXISTS("USER_NAME_ALREADY_EXISTS", "Username '{}' is already taken"),
    USER_ID_DOES_NOT_MATCH("USER_ID_DOES_NOT_MATCH", "The user id '{}' does not match the one provided '{}'."),
    USER_NOT_VALID("USER_NOT_VALID", "The user is not valid: {}"),
    USER_HAS_NOT_AUTHORIZATION("USER_HAS_NOT_AUTHORIZATION", "The user has not enough authorization. Required: {}"),

    ID_IS_NOT_A_UUID("ID_IS_NOT_A_UUID", "The id '{}' is not a valid UUID"),
    INVALID_UUID("INVALID_UUID", "'{}' must have a valid UUID. Thrown error because of '{}'");

    private final String code;
    private final String message;
}
