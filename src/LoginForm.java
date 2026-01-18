import javax.swing.*;
import java.awt.*;

/**
 * Enhanced LoginForm dengan tampilan modern dan UX yang lebih baik
 */
public class LoginForm extends JFrame {
    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JTextField registerNameField;
    private JTextField registerUsernameField;
    private JTextField registerEmailField;
    private JTextField registerPhoneField;
    private JTextArea registerAddressArea;
    private JPasswordField registerPasswordField;
    private JPasswordField registerConfirmPasswordField;
    private JPanel cardPanel;

    private static final Color PRIMARY = new Color(52, 152, 219);
    private static final Color SUCCESS = new Color(46, 204, 113);

    public LoginForm() {
        setTitle("Sistem Pemeliharaan Mobil - Login");
        setSize(480, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardPanel = new JPanel(new CardLayout());
        cardPanel.add(createLoginPanel(), "login");
        cardPanel.add(createRegisterPanel(), "register");
        add(cardPanel);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(236, 240, 241));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("SISTEM PEMELIHARAAN MOBIL", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Kelola mobil Anda dengan mudah", JLabel.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(236, 240, 241));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(subtitle);
        header.add(titlePanel);

        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        panel.add(header, g);

        g.gridwidth = 1;
        g.gridy = 1;

        // Username
        g.gridx = 0;
        panel.add(new JLabel("Username:"), g);
        g.gridx = 1;
        loginUsernameField = new JTextField(20);
        panel.add(loginUsernameField, g);

        // Password
        g.gridy = 2;
        g.gridx = 0;
        panel.add(new JLabel("Password:"), g);
        g.gridx = 1;
        loginPasswordField = new JPasswordField(20);
        panel.add(loginPasswordField, g);

        // Login Button
        g.gridy = 3;
        g.gridx = 0;
        g.gridwidth = 2;
        JButton loginBtn = btn("Login", PRIMARY);
        loginBtn.addActionListener(e -> handleLogin());
        panel.add(loginBtn, g);

        // Register Button
        g.gridy = 4;
        JButton registerBtn = btn("Belum punya akun? Daftar di sini", SUCCESS);
        registerBtn.addActionListener(e -> {
            clearRegisterFields();
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "register");
        });
        panel.add(registerBtn, g);

        // Enter key support
        loginPasswordField.addActionListener(e -> handleLogin());

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(236, 240, 241));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 8, 5, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(SUCCESS);
        header.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("PENDAFTARAN AKUN BARU", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.DARK_GRAY);
        header.add(title);

        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        panel.add(header, g);

        g.gridwidth = 1;

        // Nama
        g.gridy = 1; g.gridx = 0;
        panel.add(new JLabel("Nama Lengkap:"), g);
        g.gridx = 1;
        registerNameField = new JTextField(20);
        panel.add(registerNameField, g);

        // Username
        g.gridy = 2; g.gridx = 0;
        panel.add(new JLabel("Username:"), g);
        g.gridx = 1;
        registerUsernameField = new JTextField(20);
        panel.add(registerUsernameField, g);

        // Email
        g.gridy = 3; g.gridx = 0;
        panel.add(new JLabel("Email:"), g);
        g.gridx = 1;
        registerEmailField = new JTextField(20);
        panel.add(registerEmailField, g);

        // Phone
        g.gridy = 4; g.gridx = 0;
        panel.add(new JLabel("Telepon (opsional):"), g);
        g.gridx = 1;
        registerPhoneField = new JTextField(20);
        panel.add(registerPhoneField, g);

        // Address
        g.gridy = 5; g.gridx = 0;
        panel.add(new JLabel("Alamat (opsional):"), g);
        g.gridx = 1;
        registerAddressArea = new JTextArea(2, 20);
        registerAddressArea.setLineWrap(true);
        panel.add(new JScrollPane(registerAddressArea), g);

        // Password
        g.gridy = 6; g.gridx = 0;
        panel.add(new JLabel("Password:"), g);
        g.gridx = 1;
        registerPasswordField = new JPasswordField(20);
        panel.add(registerPasswordField, g);

        // Confirm Password
        g.gridy = 7; g.gridx = 0;
        panel.add(new JLabel("Konfirmasi Password:"), g);
        g.gridx = 1;
        registerConfirmPasswordField = new JPasswordField(20);
        panel.add(registerConfirmPasswordField, g);

        // Submit Button
        g.gridy = 8; g.gridx = 0; g.gridwidth = 2;
        JButton submitBtn = btn("✅ Daftar", SUCCESS);
        submitBtn.addActionListener(e -> handleRegister());
        panel.add(submitBtn, g);

        // Back Button
        g.gridy = 9;
        JButton backBtn = btn("◀️ Kembali ke Login", new Color(149, 165, 166));
        backBtn.addActionListener(e -> {
            clearLoginFields();
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "login");
        });
        panel.add(backBtn, g);

        return panel;
    }

    private void handleLogin() {
        String username = loginUsernameField.getText().trim();
        String password = new String(loginPasswordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Username dan password tidak boleh kosong!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = UserManager.login(username, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this,
                    "Login berhasil!\nSelamat datang, " + user.getName() + "!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);

            dispose();
            SwingUtilities.invokeLater(() -> new MainApp(user).setVisible(true));
        } else {
            JOptionPane.showMessageDialog(this,
                    "Login gagal!\nUsername atau password salah.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            loginPasswordField.setText("");
        }
    }

    private void handleRegister() {
        String name = registerNameField.getText().trim();
        String username = registerUsernameField.getText().trim();
        String email = registerEmailField.getText().trim();
        String phone = registerPhoneField.getText().trim();
        String address = registerAddressArea.getText().trim();
        String password = new String(registerPasswordField.getPassword());
        String confirmPassword = new String(registerConfirmPasswordField.getPassword());

        // Validasi field kosong
        if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nama, Username, Email, dan Password harus diisi!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validasi nama
        if (!InputValidator.isValidName(name)) {
            JOptionPane.showMessageDialog(this,
                    "Nama harus minimal 3 karakter!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validasi username
        if (!InputValidator.isValidUsername(username)) {
            JOptionPane.showMessageDialog(this,
                    InputValidator.getUsernameValidationMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validasi email
        if (!InputValidator.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this,
                    InputValidator.getEmailValidationMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validasi phone (jika diisi)
        if (!phone.isEmpty() && !InputValidator.isValidPhone(phone)) {
            JOptionPane.showMessageDialog(this,
                    "Nomor telepon tidak valid!\nHarus 10-13 digit angka.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validasi password
        if (!PasswordUtils.isValidPassword(password)) {
            JOptionPane.showMessageDialog(this,
                    PasswordUtils.getPasswordValidationMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validasi konfirmasi password
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Password dan konfirmasi password tidak sama!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Buat user baru
        User newUser = new User(name, username, email, phone, address, password);

        // Register dengan validasi
        ValidationResult result = UserManager.register(newUser);

        if (result.isSuccess()) {
            JOptionPane.showMessageDialog(this,
                    "Pendaftaran berhasil!\nSilakan login dengan akun Anda.",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);

            clearRegisterFields();
            clearLoginFields();
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "login");
        } else {
            JOptionPane.showMessageDialog(this,
                    result.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearLoginFields() {
        loginUsernameField.setText("");
        loginPasswordField.setText("");
    }

    private void clearRegisterFields() {
        registerNameField.setText("");
        registerUsernameField.setText("");
        registerEmailField.setText("");
        registerPhoneField.setText("");
        registerAddressArea.setText("");
        registerPasswordField.setText("");
        registerConfirmPasswordField.setText("");
    }

    private JButton btn(String txt, Color c) {
        JButton b = new JButton(txt);
        b.setBackground(c);
        b.setForeground(Color.DARK_GRAY);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}