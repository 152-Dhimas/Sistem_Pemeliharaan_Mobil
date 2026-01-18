import java.io.*;
import java.util.ArrayList;

/**
 * Enhanced UserManager dengan fitur lengkap
 */
public class UserManager {
    private static final String USER_FILE = "users.txt";

    /**
     * Register user baru dengan validasi lengkap
     */
    public static ValidationResult register(User user) {
        // Validasi username
        if (!InputValidator.isValidUsername(user.getUsername())) {
            return new ValidationResult(false, InputValidator.getUsernameValidationMessage());
        }

        // Validasi email
        if (!InputValidator.isValidEmail(user.getEmail())) {
            return new ValidationResult(false, InputValidator.getEmailValidationMessage());
        }

        // Cek username sudah ada
        if (isUsernameExists(user.getUsername())) {
            return new ValidationResult(false, "Username sudah terdaftar!");
        }

        // Cek email sudah ada
        if (isEmailExists(user.getEmail())) {
            return new ValidationResult(false, "Email sudah terdaftar!");
        }

        // Register user
        user.register();
        return new ValidationResult(true, "Registrasi berhasil!");
    }

    /**
     * Login user
     */
    public static User login(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 7 && data[1].equals(username)) {
                    // Format baru dengan password hash
                    User user = new User(
                            data[0],  // name
                            data[1],  // username
                            data[2],  // email
                            data[3],  // phone
                            data[4],  // address
                            data[5],  // passwordHash
                            data[6]   // salt
                    );

                    if (user.verifyPassword(password)) {
                        return user;
                    }
                } else if (data.length == 3 && data[0].equals(username)) {
                    // Format lama (backward compatibility)
                    if (data[2].equals(password)) {
                        // Convert ke format baru
                        User user = new User(data[0], data[0], data[1], password);
                        updateUserInFile(username, user);
                        return user;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cek apakah username sudah ada
     */
    public static boolean isUsernameExists(String username) {
        try {
            File file = new File(USER_FILE);
            if (!file.exists()) return false;

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2 && data[1].equals(username)) {
                    reader.close();
                    return true;
                }
                // Format lama
                if (data.length == 3 && data[0].equals(username)) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cek apakah email sudah ada
     */
    public static boolean isEmailExists(String email) {
        try {
            File file = new File(USER_FILE);
            if (!file.exists()) return false;

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3 && data[2].equals(email)) {
                    reader.close();
                    return true;
                }
                // Format lama
                if (data.length == 3 && data[1].equals(email)) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get user info by username
     */
    public static User getUserInfo(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 7 && data[1].equals(username)) {
                    return new User(
                            data[0], data[1], data[2], data[3], data[4],
                            data[5], data[6]
                    );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Update user in file
     */
    public static boolean updateUserInFile(String username, User updatedUser) {
        ArrayList<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2 && data[1].equals(username)) {
                    lines.add(updatedUser.toFileString());
                    found = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (!found) return false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all users (untuk statistik)
     */
    public static ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 7) {
                    users.add(new User(
                            data[0], data[1], data[2], data[3], data[4],
                            data[5], data[6]
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Delete user
     */
    public static boolean deleteUser(String username) {
        ArrayList<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2 && !data[1].equals(username)) {
                    lines.add(line);
                } else if (data.length >= 2 && data[1].equals(username)) {
                    found = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (!found) return false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

/**
 * Class untuk hasil validasi
 */
class ValidationResult {
    private boolean success;
    private String message;

    public ValidationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}