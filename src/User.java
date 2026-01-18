import java.io.*;

/**
 * Enhanced User class dengan keamanan password
 */
public class User {
    private String name;
    private String username;
    private String email;
    private String phone;
    private String address;
    private String passwordHash;
    private String salt;

    // Constructor untuk registrasi baru
    public User(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = "";
        this.address = "";
        this.salt = PasswordUtils.generateSalt();
        this.passwordHash = PasswordUtils.hashPassword(password, salt);
    }

    // Constructor lengkap
    public User(String name, String username, String email, String phone,
                String address, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.salt = PasswordUtils.generateSalt();
        this.passwordHash = PasswordUtils.hashPassword(password, salt);
    }

    // Constructor dari file
    public User(String name, String username, String email, String phone,
                String address, String passwordHash, String salt) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.passwordHash = passwordHash;
        this.salt = salt;
    }

    /**
     * Menyimpan informasi pengguna ke dalam file
     */
    public void register() {
        try {
            FileWriter fileWriter = new FileWriter("users.txt", true);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            writer.write(toFileString());
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Convert ke format file (CSV)
     * Format: name,username,email,phone,address,passwordHash,salt
     */
    public String toFileString() {
        return String.join(",",
                name,
                username,
                email,
                phone != null ? phone : "",
                address != null ? address : "",
                passwordHash,
                salt
        );
    }

    /**
     * Verify password
     */
    public boolean verifyPassword(String password) {
        return PasswordUtils.verifyPassword(password, salt, passwordHash);
    }

    /**
     * Update password
     */
    public void updatePassword(String newPassword) {
        this.salt = PasswordUtils.generateSalt();
        this.passwordHash = PasswordUtils.hashPassword(newPassword, salt);
    }

    /**
     * Update profile
     */
    public void updateProfile(String name, String email, String phone, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    // Getters
    public String getName() { return name; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getPasswordHash() { return passwordHash; }
    public String getSalt() { return salt; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
}