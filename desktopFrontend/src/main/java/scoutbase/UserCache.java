package scoutbase;

import java.util.ArrayList;
import java.util.List;

public class UserCache {

    private static final List<UserDto> users = new ArrayList<>();

    public static void addUser(UserDto user) {
        if (user == null) {
            return;
        }

        boolean exists = users.stream().anyMatch(existing ->
                existing.getId() != null
                        && user.getId() != null
                        && existing.getId().equals(user.getId())
        );

        if (!exists) {
            users.add(user);
        }
    }

    public static List<UserDto> getUsers() {
        return new ArrayList<>(users);
    }

    public static void clear() {
        users.clear();
    }
}