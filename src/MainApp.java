import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.io.File;

/**
 * MainApp dengan logout, refresh, sorting, dan tampilan modern
 */
public class MainApp extends JFrame {
    private User currentUser;
    private JTabbedPane tabbedPane;
    private MobilManager mobilManager;
    private PerawatanManager perawatanManager;

    private DefaultTableModel mobilTableModel;
    private JTable mobilTable;
    private TableRowSorter<DefaultTableModel> mobilSorter;
    private JTextField mobilSearchField;

    private DefaultTableModel perawatanTableModel;
    private JTable perawatanTable;
    private TableRowSorter<DefaultTableModel> perawatanSorter;
    private JTextField perawatanSearchField;

    private JPanel statsPanel;
    private JTextArea riwayatTextArea;

    private static final Color PRIMARY = new Color(52, 152, 219);
    private static final Color SUCCESS = new Color(46, 204, 113);
    private static final Color DANGER = new Color(231, 76, 60);
    private static final Color WARNING = new Color(241, 196, 15);
    private static final Color BG = new Color(236, 240, 241);

    public MainApp(User user) {
        this.currentUser = user;
        mobilManager = new MobilManager(user.getUsername());
        perawatanManager = new PerawatanManager(user.getUsername());

        setTitle("Sistem Pemeliharaan Mobil - " + user.getName());
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadData();
    }

    private void initComponents() {
        setJMenuBar(createMenuBar());

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        tabbedPane.addTab("Dashboard", createDashboardPanel());
        tabbedPane.addTab("Data Mobil", createMobilPanel());
        tabbedPane.addTab("Perawatan", createPerawatanPanel());
        tabbedPane.addTab("Riwayat", createRiwayatPanel());
        tabbedPane.addTab("Laporan", createLaporanPanel());
        tabbedPane.addTab("Profil", createProfilPanel());

        add(tabbedPane);
    }

    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        // Membuat item-menu dengan teks yang dipusatkan
        JMenuItem refresh = new JMenuItem("Refresh");
        refresh.setHorizontalAlignment(SwingConstants.CENTER); // Memusatkan teks secara horizontal
        refresh.setPreferredSize(new Dimension(150, 40)); // Menyesuaikan ukuran tombol
        refresh.addActionListener(e -> refreshAll());

        JMenuItem logout = new JMenuItem("Logout");
        logout.setHorizontalAlignment(SwingConstants.CENTER); // Memusatkan teks secara horizontal
        logout.setPreferredSize(new Dimension(150, 40)); // Menyesuaikan ukuran tombol
        logout.addActionListener(e -> logout());

        JMenuItem exit = new JMenuItem("Exit");
        exit.setHorizontalAlignment(SwingConstants.CENTER); // Memusatkan teks secara horizontal
        exit.setPreferredSize(new Dimension(150, 40)); // Menyesuaikan ukuran tombol
        exit.addActionListener(e -> System.exit(0));

        // Menambahkan item ke menu 'File'
        fileMenu.add(refresh);
        fileMenu.add(logout);
        fileMenu.add(exit);

        // Membuat menu 'Help'
        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        about.setHorizontalAlignment(SwingConstants.CENTER); // Memusatkan teks secara horizontal
        about.setPreferredSize(new Dimension(150, 40)); // Menyesuaikan ukuran tombol
        about.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Sistem Pemeliharaan Mobil v2.0\n\nFitur:\n" +
                        "✓ Manajemen data mobil\n✓ Tracking perawatan\n✓ Laporan & Export\n" +
                        "✓ Sorting & Filter otomatis\n✓ Backup & Restore",
                "About", JOptionPane.INFORMATION_MESSAGE));

        help.add(about);

        // Menambahkan menu 'File' dan 'Help' ke menu bar
        bar.add(fileMenu);
        bar.add(help);

        return bar;
    }



    private void logout() {
        int c = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin logout?",
                "Konfirmasi Logout", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
        }
    }

    private void refreshAll() {
        loadData();
        updateStats();
        refreshRiwayat();
        JOptionPane.showMessageDialog(this, "Data berhasil di-refresh!",
                "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createDashboardPanel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(BG);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel welcome = new JPanel(new BorderLayout());
        welcome.setBackground(PRIMARY);
        welcome.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Selamat Datang, " + currentUser.getName() + "!", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Sistem Pemeliharaan Mobil", JLabel.CENTER);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sub.setForeground(new Color(236, 240, 241));

        JPanel txt = new JPanel(new GridLayout(2, 1));
        txt.setOpaque(false);
        txt.add(title);
        txt.add(sub);
        welcome.add(txt);

        statsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        statsPanel.setBackground(BG);
        updateStats();

        p.add(welcome, BorderLayout.NORTH);
        p.add(statsPanel, BorderLayout.CENTER);
        return p;
    }

    private void updateStats() {
        statsPanel.removeAll();
        mobilManager.loadAll();
        perawatanManager.loadAll();

        int tm = mobilManager.getJumlahMobil();
        int tp = perawatanManager.getJumlahPerawatan();
        double tb = perawatanManager.getTotalBiaya();
        double rb = tp > 0 ? tb / tp : 0;

        statsPanel.add(statCard("Total Mobil", tm + "", "🚗", PRIMARY));
        statsPanel.add(statCard("Total Perawatan", tp + "", "🔧", SUCCESS));
        statsPanel.add(statCard("Biaya Total", String.format("Rp %.0f", tb), "💰", DANGER));
        statsPanel.add(statCard("Rata-rata", String.format("Rp %.0f", rb), "📊", WARNING));

        statsPanel.revalidate();
        statsPanel.repaint();
    }

    private JPanel statCard(String title, String val, String icon, Color c) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(c, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        JLabel ic = new JLabel(icon, JLabel.CENTER);
        ic.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));

        JLabel t = new JLabel(title, JLabel.CENTER);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setForeground(new Color(127, 140, 141));

        JLabel v = new JLabel(val, JLabel.CENTER);
        v.setFont(new Font("Segoe UI", Font.BOLD, 24));
        v.setForeground(c);

        JPanel txt = new JPanel(new GridLayout(2, 1, 0, 5));
        txt.setOpaque(false);
        txt.add(t);
        txt.add(v);

        card.add(ic, BorderLayout.WEST);
        card.add(txt, BorderLayout.CENTER);
        return card;
    }

    private JPanel createMobilPanel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(BG);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.setBackground(BG);

        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT));
        search.setBackground(BG);
        search.add(new JLabel("🔍 Cari:"));
        mobilSearchField = new JTextField(20);
        search.add(mobilSearchField);

        JButton sb = btn("Cari", PRIMARY);
        sb.addActionListener(e -> filterMobil());
        search.add(sb);

        JButton rb = btn("Reset", new Color(149, 165, 166));
        rb.addActionListener(e -> resetMobil());
        search.add(rb);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btns.setBackground(BG);

        JButton add = btn("Tambah", SUCCESS);
        add.addActionListener(e -> showMobilDialog(null, -1));
        JButton edit = btn("Edit", WARNING);
        edit.addActionListener(e -> editMobil());
        JButton del = btn("Hapus", DANGER);
        del.addActionListener(e -> deleteMobil());

        btns.add(add);
        btns.add(edit);
        btns.add(del);

        top.add(search, BorderLayout.WEST);
        top.add(btns, BorderLayout.EAST);

        String[] cols = {"Merek", "Model", "Tahun", "No. Polisi", "Tipe"};
        mobilTableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        mobilTable = new JTable(mobilTableModel);
        mobilTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        mobilTable.setRowHeight(25);
        mobilTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        mobilTable.getTableHeader().setBackground(PRIMARY);
        mobilTable.getTableHeader().setForeground(Color.BLACK);

        mobilSorter = new TableRowSorter<>(mobilTableModel);
        mobilTable.setRowSorter(mobilSorter);

        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(mobilTable), BorderLayout.CENTER);
        return p;
    }

    private void filterMobil() {
        String t = mobilSearchField.getText().trim();
        mobilSorter.setRowFilter(t.isEmpty() ? null : RowFilter.regexFilter("(?i)" + t));
    }

    private void resetMobil() {
        mobilSearchField.setText("");
        mobilSorter.setRowFilter(null);
    }

    private void editMobil() {
        int r = mobilTable.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Pilih mobil yang akan diedit!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int mr = mobilTable.convertRowIndexToModel(r);
        Mobil m = mobilManager.getMobilList().get(mr);
        showMobilDialog(m, mr);
    }

    private void deleteMobil() {
        int r = mobilTable.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Pilih mobil yang akan dihapus!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int mr = mobilTable.convertRowIndexToModel(r);
        if (JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus mobil ini?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            mobilManager.delete(mr);
            loadMobilData();
            updateStats();
            JOptionPane.showMessageDialog(this, "Mobil berhasil dihapus!");
        }
    }

    private void showMobilDialog(Mobil mobil, int idx) {
        JDialog d = new JDialog(this, mobil == null ? "Tambah Mobil" : "Edit Mobil", true);
        d.setSize(420, 380);
        d.setLocationRelativeTo(this);

        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        JTextField merek = new JTextField(mobil != null ? mobil.getMerek() : "", 20);
        JTextField model = new JTextField(mobil != null ? mobil.getModel() : "", 20);
        JTextField tahun = new JTextField(mobil != null ? String.valueOf(mobil.getTahun()) : "", 20);
        JTextField nopol = new JTextField(mobil != null ? mobil.getNomorPolisi() : "", 20);
        if (mobil != null) nopol.setEditable(false);

        JComboBox<String> tipe = new JComboBox<>(new String[]{"SUV", "Sedan", "MPV", "Hatchback"});
        if (mobil instanceof MobilSUV) tipe.setSelectedItem("SUV");
        else if (mobil instanceof MobilSedan) tipe.setSelectedItem("Sedan");

        String det = "";
        if (mobil instanceof MobilSUV) det = String.valueOf(((MobilSUV) mobil).getKapasitasMesin());
        else if (mobil instanceof MobilSedan) det = ((MobilSedan) mobil).getTipeMesin();
        JTextField detail = new JTextField(det, 20);

        int r = 0;
        g.gridx = 0;
        g.gridy = r;
        p.add(new JLabel("Merek:"), g);
        g.gridx = 1;
        p.add(merek, g);

        r++;
        g.gridx = 0;
        g.gridy = r;
        p.add(new JLabel("Model:"), g);
        g.gridx = 1;
        p.add(model, g);

        r++;
        g.gridx = 0;
        g.gridy = r;
        p.add(new JLabel("Tahun:"), g);
        g.gridx = 1;
        p.add(tahun, g);

        r++;
        g.gridx = 0;
        g.gridy = r;
        p.add(new JLabel("No. Polisi:"), g);
        g.gridx = 1;
        p.add(nopol, g);

        r++;
        g.gridx = 0;
        g.gridy = r;
        p.add(new JLabel("Tipe:"), g);
        g.gridx = 1;
        p.add(tipe, g);

        r++;
        g.gridx = 0;
        g.gridy = r;
        p.add(new JLabel("Detail (cc/mesin):"), g);
        g.gridx = 1;
        p.add(detail, g);

        JButton save = btn("Simpan", SUCCESS);
        JButton cancel = btn("Batal", new Color(149, 165, 166));

        save.addActionListener(e -> {
            try {
                String mk = merek.getText().trim();
                String md = model.getText().trim();
                int th = Integer.parseInt(tahun.getText().trim());
                String np = nopol.getText().trim().toUpperCase();
                String tp = (String) tipe.getSelectedItem();
                String dt = detail.getText().trim();

                if (mk.isEmpty() || md.isEmpty() || np.isEmpty()) {
                    JOptionPane.showMessageDialog(d, "Field tidak boleh kosong!");
                    return;
                }

                if (!InputValidator.isValidNopol(np)) {
                    JOptionPane.showMessageDialog(d, InputValidator.getNopolValidationMessage());
                    return;
                }

                if (mobil == null && mobilManager.getMobilByNopol(np) != null) {
                    JOptionPane.showMessageDialog(d, "Nomor polisi sudah terdaftar!");
                    return;
                }

                if (!InputValidator.isValidYear(th)) {
                    JOptionPane.showMessageDialog(d, "Tahun tidak valid!");
                    return;
                }

                Mobil m;
                if (tp.equals("SUV")) {
                    try {
                        double cc = Double.parseDouble(dt);
                        m = new MobilSUV(mk, md, th, np, cc);
                    } catch (NumberFormatException ex) {
                        m = new Mobil(mk, md, th, np);
                    }
                } else if (tp.equals("Sedan")) {
                    m = new MobilSedan(mk, md, th, np, dt);
                } else {
                    m = new Mobil(mk, md, th, np);
                }

                if (mobil == null) {
                    mobilManager.save(m);
                    JOptionPane.showMessageDialog(this, "Mobil berhasil ditambahkan!");
                } else {
                    mobilManager.update(idx, m);
                    JOptionPane.showMessageDialog(this, "Mobil berhasil diupdate!");
                }

                loadMobilData();
                updateStats();
                d.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(d, "Tahun harus berupa angka!");
            }
        });

        cancel.addActionListener(e -> d.dispose());

        r++;
        g.gridx = 0;
        g.gridy = r;
        g.gridwidth = 2;
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(save);
        btnPanel.add(cancel);
        p.add(btnPanel, g);

        d.add(p);
        d.setVisible(true);
    }

    private JPanel createPerawatanPanel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(BG);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.setBackground(BG);

        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT));
        search.setBackground(BG);
        search.add(new JLabel("🔍 Cari:"));
        perawatanSearchField = new JTextField(20);
        search.add(perawatanSearchField);

        JButton sb = btn("Cari", PRIMARY);
        sb.addActionListener(e -> filterPerawatan());
        search.add(sb);

        JButton rb = btn("Reset", new Color(149, 165, 166));
        rb.addActionListener(e -> resetPerawatan());
        search.add(rb);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btns.setBackground(BG);

        JButton add = btn("Tambah", SUCCESS);
        add.addActionListener(e -> showPerawatanDialog());
        JButton del = btn("Hapus", DANGER);
        del.addActionListener(e -> deletePerawatan());

        btns.add(add);
        btns.add(del);

        top.add(search, BorderLayout.WEST);
        top.add(btns, BorderLayout.EAST);

        String[] cols = {"Tanggal", "No. Polisi", "Jenis", "Biaya", "Bengkel"};
        perawatanTableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        perawatanTable = new JTable(perawatanTableModel);
        perawatanTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        perawatanTable.setRowHeight(25);
        perawatanTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        perawatanTable.getTableHeader().setBackground(PRIMARY);
        perawatanTable.getTableHeader().setForeground(Color.BLACK);

        perawatanSorter = new TableRowSorter<>(perawatanTableModel);
        perawatanTable.setRowSorter(perawatanSorter);

        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(perawatanTable), BorderLayout.CENTER);
        return p;
    }

    private void filterPerawatan() {
        String t = perawatanSearchField.getText().trim();
        perawatanSorter.setRowFilter(t.isEmpty() ? null : RowFilter.regexFilter("(?i)" + t));
    }

    private void resetPerawatan() {
        perawatanSearchField.setText("");
        perawatanSorter.setRowFilter(null);
    }

    private void showPerawatanDialog() {
        JDialog d = new JDialog(this, "Tambah Perawatan", true);
        d.setSize(400, 320);
        d.setLocationRelativeTo(this);

        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        JTextField tanggal = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(new Date()), 20);
        JTextField nopol = new JTextField(20);
        JTextField jenis = new JTextField(20);
        JTextField biaya = new JTextField(20);
        JTextField bengkel = new JTextField(20);

        int r = 0;
        g.gridx = 0;
        g.gridy = r;
        p.add(new JLabel("Tanggal:"), g);
        g.gridx = 1;
        p.add(tanggal, g);

        r++;
        g.gridx = 0;
        g.gridy = r;
        p.add(new JLabel("No. Polisi:"), g);
        g.gridx = 1;
        p.add(nopol, g);

        r++;
        g.gridx = 0;
        g.gridy = r;
        p.add(new JLabel("Jenis:"), g);
        g.gridx = 1;
        p.add(jenis, g);

        r++;
        g.gridx = 0;
        g.gridy = r;
        p.add(new JLabel("Biaya (Rp):"), g);
        g.gridx = 1;
        p.add(biaya, g);

        r++;
        g.gridx = 0;
        g.gridy = r;
        p.add(new JLabel("Bengkel:"), g);
        g.gridx = 1;
        p.add(bengkel, g);

        JButton save = btn("Simpan", SUCCESS);
        JButton cancel = btn("Batal", new Color(149, 165, 166));

        save.addActionListener(e -> {
            if (nopol.getText().trim().isEmpty() || jenis.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(d, "Field tidak boleh kosong!");
                return;
            }

            try {
                double b = biaya.getText().trim().isEmpty() ? 0 : Double.parseDouble(biaya.getText().trim());
                Perawatan prw = new Perawatan(tanggal.getText(), nopol.getText().toUpperCase(),
                        jenis.getText(), b, bengkel.getText());
                perawatanManager.save(prw);
                loadPerawatanData();
                updateStats();
                refreshRiwayat();
                JOptionPane.showMessageDialog(this, "Perawatan berhasil ditambahkan!");
                d.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(d, "Biaya harus berupa angka!");
            }
        });

        cancel.addActionListener(e -> d.dispose());

        r++;
        g.gridx = 0;
        g.gridy = r;
        g.gridwidth = 2;
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(save);
        btnPanel.add(cancel);
        p.add(btnPanel, g);

        d.add(p);
        d.setVisible(true);
    }

    private void deletePerawatan() {
        int r = perawatanTable.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Pilih perawatan yang akan dihapus!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int mr = perawatanTable.convertRowIndexToModel(r);
        if (JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus perawatan ini?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            perawatanManager.delete(mr);
            loadPerawatanData();
            updateStats();
            refreshRiwayat();
            JOptionPane.showMessageDialog(this, "Perawatan berhasil dihapus!");
        }
    }

    private JPanel createRiwayatPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        riwayatTextArea = new JTextArea();
        riwayatTextArea.setEditable(false);
        riwayatTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        loadRiwayat();

        p.add(new JScrollPane(riwayatTextArea), BorderLayout.CENTER);
        return p;
    }

    private void refreshRiwayat() {
        if (riwayatTextArea != null) loadRiwayat();
    }

    private void loadRiwayat() {
        StringBuilder sb = new StringBuilder("=== RIWAYAT PERAWATAN MOBIL ===\n\n");
        perawatanManager.loadAll();

        if (perawatanManager.getPerawatanList().isEmpty()) {
            sb.append("Belum ada riwayat perawatan.\n");
        } else {
            for (Perawatan p : perawatanManager.getPerawatanList()) {
                sb.append(String.format("Tanggal    : %s\nNo. Polisi : %s\nPerawatan  : %s\nBiaya      : Rp %.0f\nBengkel    : %s\n",
                        p.getTanggal(), p.getNomorPolisi(), p.getJenisPerawatan(),
                        p.getBiaya(), p.getBengkel()));
                sb.append("----------------------------------------\n\n");
            }
        }
        riwayatTextArea.setText(sb.toString());
    }

    private JPanel createLaporanPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 10, 10, 10);
        g.fill = GridBagConstraints.HORIZONTAL;

        JLabel t = new JLabel("Export & Backup Data", JLabel.CENTER);
        t.setFont(new Font("Segoe UI", Font.BOLD, 18));

        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = 2;
        p.add(t, g);

        g.gridwidth = 1;
        g.gridy++;
        p.add(btn("Export Mobil (CSV)", PRIMARY, e -> exportMobil()), g);

        g.gridy++;
        p.add(btn("Export Perawatan (CSV)", PRIMARY, e -> exportPerawatan()), g);

        g.gridy++;
        p.add(btn("Export Laporan (TXT)", PRIMARY, e -> exportLaporan()), g);

        g.gridy++;
        p.add(btn("Backup Data", WARNING, e -> backup()), g);

        g.gridy++;
        p.add(btn("Restore Data", new Color(149, 165, 166), e -> restore()), g);

        return p;
    }

    private void exportMobil() {
        JFileChooser c = new JFileChooser();
        c.setSelectedFile(new File("mobil_" + currentUser.getUsername() + ".csv"));
        if (c.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            if (ExportUtils.exportMobilToCSV(currentUser.getUsername(), c.getSelectedFile().getAbsolutePath())) {
                JOptionPane.showMessageDialog(this, "Export berhasil!");
            }
        }
    }

    private void exportPerawatan() {
        JFileChooser c = new JFileChooser();
        c.setSelectedFile(new File("perawatan_" + currentUser.getUsername() + ".csv"));
        if (c.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            if (ExportUtils.exportPerawatanToCSV(currentUser.getUsername(), c.getSelectedFile().getAbsolutePath())) {
                JOptionPane.showMessageDialog(this, "Export berhasil!");
            }
        }
    }

    private void exportLaporan() {
        JFileChooser c = new JFileChooser();
        c.setSelectedFile(new File("laporan_" + currentUser.getUsername() + ".txt"));
        if (c.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            if (ExportUtils.exportLaporanLengkap(currentUser.getUsername(), c.getSelectedFile().getAbsolutePath())) {
                JOptionPane.showMessageDialog(this, "Export berhasil!");
            }
        }
    }

    private void backup() {
        JFileChooser c = new JFileChooser();
        c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (c.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            if (ExportUtils.backupUserData(currentUser.getUsername(), c.getSelectedFile().getAbsolutePath())) {
                JOptionPane.showMessageDialog(this, "Backup berhasil!");
            }
        }
    }

    private void restore() {
        JFileChooser c = new JFileChooser();
        JOptionPane.showMessageDialog(this, "Pilih file backup mobil (Cancel jika tidak ada)");
        File m = c.showOpenDialog(this) == JFileChooser.APPROVE_OPTION ? c.getSelectedFile() : null;

        JOptionPane.showMessageDialog(this, "Pilih file backup perawatan (Cancel jika tidak ada)");
        File prw = c.showOpenDialog(this) == JFileChooser.APPROVE_OPTION ? c.getSelectedFile() : null;

        if (m != null || prw != null) {
            if (ExportUtils.restoreUserData(currentUser.getUsername(), m, prw)) {
                JOptionPane.showMessageDialog(this, "Restore berhasil! Data akan di-refresh.");
                refreshAll();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Tidak ada file yang dipilih!");
        }
    }

    private JPanel createProfilPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 10, 10, 10);
        g.anchor = GridBagConstraints.WEST;

        JLabel t = new JLabel("Profil Pengguna");
        t.setFont(new Font("Segoe UI", Font.BOLD, 18));

        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = 2;
        p.add(t, g);

        g.gridwidth = 1;
        g.gridy++;
        p.add(new JLabel("Username:"), g);
        g.gridx = 1;
        p.add(new JLabel(currentUser.getUsername()), g);

        g.gridx = 0;
        g.gridy++;
        p.add(new JLabel("Nama:"), g);
        g.gridx = 1;
        p.add(new JLabel(currentUser.getName()), g);

        g.gridx = 0;
        g.gridy++;
        p.add(new JLabel("Email:"), g);
        g.gridx = 1;
        p.add(new JLabel(currentUser.getEmail()), g);

        g.gridx = 0;
        g.gridy++;
        p.add(new JLabel("Telepon:"), g);
        g.gridx = 1;
        p.add(new JLabel(currentUser.getPhone().isEmpty() ? "-" : currentUser.getPhone()), g);

        g.gridx = 0;
        g.gridy++;
        p.add(btn("Edit Profil", PRIMARY, e -> editProfile()), g);
        g.gridx = 1;
        p.add(btn("Ubah Password", WARNING, e -> changePassword()), g);

        return p;
    }

    private void editProfile() {
        JDialog d = new JDialog(this, "Edit Profil", true);
        d.setSize(400, 280);
        d.setLocationRelativeTo(this);

        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        JTextField nama = new JTextField(currentUser.getName(), 20);
        JTextField email = new JTextField(currentUser.getEmail(), 20);
        JTextField phone = new JTextField(currentUser.getPhone(), 20);
        JTextArea address = new JTextArea(currentUser.getAddress(), 3, 20);
        address.setLineWrap(true);

        int r = 0;
        g.gridx = 0;
        g.gridy = r;
        p.add(new JLabel("Nama:"), g);
        g.gridx = 1;
        p.add(nama, g);

        r++;
        g.gridx = 0;
        g.gridy = r;
        p.add(new JLabel("Email:"), g);
        g.gridx = 1;
        p.add(email, g);

        r++;
        g.gridx = 0;
        g.gridy = r;
        p.add(new JLabel("Telepon:"), g);
        g.gridx = 1;
        p.add(phone, g);

        r++;
        g.gridx = 0;
        g.gridy = r;
        p.add(new JLabel("Alamat:"), g);
        g.gridx = 1;
        p.add(new JScrollPane(address), g);

        JButton save =
                btn("Simpan", SUCCESS);
        JButton cancel = btn("Batal", new Color(149, 165, 166));

        save.addActionListener(e -> {
            String n = nama.getText().trim();
            String em = email.getText().trim();
            String ph = phone.getText().trim();
            String ad = address.getText().trim();

            if (!InputValidator.isValidName(n)) {
                JOptionPane.showMessageDialog(d, "Nama minimal 3 karakter!");
                return;
            }

            if (!InputValidator.isValidEmail(em)) {
                JOptionPane.showMessageDialog(d, InputValidator.getEmailValidationMessage());
                return;
            }

            currentUser.updateProfile(n, em, ph, ad);

            if (UserManager.updateUserInFile(currentUser.getUsername(), currentUser)) {
                JOptionPane.showMessageDialog(this, "Profil berhasil diupdate!");
                tabbedPane.removeTabAt(5);
                tabbedPane.insertTab("👤 Profil", null, createProfilPanel(), null, 5);
                tabbedPane.setSelectedIndex(5);
                d.dispose();
            } else {
                JOptionPane.showMessageDialog(d, "Gagal update profil!");
            }
        });

        cancel.addActionListener(e -> d.dispose());

        r++;
        g.gridx = 0;
        g.gridy = r;
        g.gridwidth = 2;
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(save);
        btnPanel.add(cancel);
        p.add(btnPanel, g);

        d.add(p);
        d.setVisible(true);
    }

    private void changePassword() {
        JDialog d = new JDialog(this, "Ubah Password", true);
        d.setSize(380, 250);
        d.setLocationRelativeTo(this);

        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        JPasswordField oldPass = new JPasswordField(20);
        JPasswordField newPass = new JPasswordField(20);
        JPasswordField confirmPass = new JPasswordField(20);

        int r = 0;
        g.gridx = 0;
        g.gridy = r;
        p.add(new JLabel("Password Lama:"), g);
        g.gridx = 1;
        p.add(oldPass, g);

        r++;
        g.gridx = 0;
        g.gridy = r;
        p.add(new JLabel("Password Baru:"), g);
        g.gridx = 1;
        p.add(newPass, g);

        r++;
        g.gridx = 0;
        g.gridy = r;
        p.add(new JLabel("Konfirmasi:"), g);
        g.gridx = 1;
        p.add(confirmPass, g);

        JButton save = btn("Ubah", SUCCESS);
        JButton cancel = btn("Batal", new Color(149, 165, 166));

        save.addActionListener(e -> {
            String old = new String(oldPass.getPassword());
            String newP = new String(newPass.getPassword());
            String conf = new String(confirmPass.getPassword());

            if (!currentUser.verifyPassword(old)) {
                JOptionPane.showMessageDialog(d, "Password lama salah!");
                return;
            }

            if (!PasswordUtils.isValidPassword(newP)) {
                JOptionPane.showMessageDialog(d, PasswordUtils.getPasswordValidationMessage());
                return;
            }

            if (!newP.equals(conf)) {
                JOptionPane.showMessageDialog(d, "Password baru dan konfirmasi tidak sama!");
                return;
            }

            currentUser.updatePassword(newP);

            if (UserManager.updateUserInFile(currentUser.getUsername(), currentUser)) {
                JOptionPane.showMessageDialog(this, "Password berhasil diubah!");
                d.dispose();
            } else {
                JOptionPane.showMessageDialog(d, "Gagal mengubah password!");
            }
        });

        cancel.addActionListener(e -> d.dispose());

        r++;
        g.gridx = 0;
        g.gridy = r;
        g.gridwidth = 2;
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(save);
        btnPanel.add(cancel);
        p.add(btnPanel, g);

        d.add(p);
        d.setVisible(true);
    }

    private void loadData() {
        loadMobilData();
        loadPerawatanData();
    }

    private void loadMobilData() {
        mobilTableModel.setRowCount(0);
        mobilManager.loadAll();
        for (Mobil m : mobilManager.getMobilList()) {
            String t = m instanceof MobilSUV ? "SUV" : m instanceof MobilSedan ? "Sedan" : "Regular";
            mobilTableModel.addRow(new Object[]{m.getMerek(), m.getModel(), m.getTahun(), m.getNomorPolisi(), t});
        }
    }

    private void loadPerawatanData() {
        perawatanTableModel.setRowCount(0);
        perawatanManager.loadAll();
        for (Perawatan p : perawatanManager.getPerawatanList()) {
            perawatanTableModel.addRow(new Object[]{
                    p.getTanggal(), p.getNomorPolisi(), p.getJenisPerawatan(),
                    String.format("%.0f", p.getBiaya()), p.getBengkel()
            });
        }
    }

    private JButton btn(String txt, Color c) {
        return btn(txt, c, null);
    }

    private JButton btn(String txt, Color c, java.awt.event.ActionListener a) {
        JButton b = new JButton(txt);
        b.setBackground(c);
        b.setForeground(Color.BLACK);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (a != null) b.addActionListener(a);
        return b;
    }
}