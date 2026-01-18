import java.util.regex.Pattern;

/**
 * Class untuk validasi input user
 */
public class InputValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern USERNAME_PATTERN = Pattern.compile(
            "^[A-Za-z0-9]{5,}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^[0-9]{10,13}$"
    );

    private static final Pattern NOPOL_PATTERN = Pattern.compile(
            "^[A-Z]{1,2}\\s?[0-9]{1,4}\\s?[A-Z]{1,3}$"
    );

    /**
     * Validasi email
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validasi username
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username.trim()).matches();
    }

    /**
     * Validasi nomor telepon
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return true; // Optional field
        }
        String cleanPhone = phone.replaceAll("[\\s-]", "");
        return PHONE_PATTERN.matcher(cleanPhone).matches();
    }

    /**
     * Validasi nomor polisi
     */
    public static boolean isValidNopol(String nopol) {
        if (nopol == null || nopol.trim().isEmpty()) {
            return false;
        }
        return NOPOL_PATTERN.matcher(nopol.trim().toUpperCase()).matches();
    }

    /**
     * Validasi nama (tidak boleh kosong, min 3 karakter)
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.trim().length() >= 3;
    }

    /**
     * Validasi tahun (1900 - tahun sekarang + 1)
     */
    public static boolean isValidYear(int year) {
        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        return year >= 1900 && year <= currentYear + 1;
    }

    /**
     * Get pesan error untuk username
     */
    public static String getUsernameValidationMessage() {
        return "Username harus:\n" +
                "- Minimal 5 karakter\n" +
                "- Hanya huruf dan angka\n" +
                "- Tanpa spasi atau karakter khusus";
    }

    /**
     * Get pesan error untuk email
     */
    public static String getEmailValidationMessage() {
        return "Format email tidak valid.\n" +
                "Contoh: user@example.com";
    }

    /**
     * Get pesan error untuk nomor polisi
     */
    public static String getNopolValidationMessage() {
        return "Format nomor polisi tidak valid.\n" +
                "Contoh: B 1234 XYZ atau DK 123 AB";
    }
}