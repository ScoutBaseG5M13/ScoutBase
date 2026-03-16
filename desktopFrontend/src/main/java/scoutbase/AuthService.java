package scoutbase;

public class AuthService {

    public boolean login(String username, String password) {
        // Validación temporal local
        // Cambia esto más adelante por llamada HTTP a la API
        return username.equals("admin") && password.equals("1234");
    }
}