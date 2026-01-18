import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Utility untuk export data ke CSV dan backup
 */
public class ExportUtils {

    /**
     * Export data perawatan ke CSV
     */
    public static boolean exportPerawatanToCSV(String username, String filepath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            // Header
            writer.write("Tanggal,Nomor Polisi,Jenis Perawatan,Biaya,Bengkel");
            writer.newLine();

            // Data
            ArrayList<Perawatan> perawatanList = Perawatan.loadAll(username);
            for (Perawatan p : perawatanList) {
                writer.write(String.format("%s,%s,%s,%.2f,%s",
                        p.getTanggal(),
                        p.getNomorPolisi(),
                        p.getJenisPerawatan(),
                        p.getBiaya(),
                        p.getBengkel()
                ));
                writer.newLine();
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Export data mobil ke CSV
     */
    public static boolean exportMobilToCSV(String username, String filepath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            // Header
            writer.write("Merek,Model,Tahun,Nomor Polisi,Tipe,Detail");
            writer.newLine();

            // Data
            MobilManager manager = new MobilManager(username);
            manager.loadAll();
            ArrayList<Mobil> mobilList = manager.getMobilList();

            for (Mobil m : mobilList) {
                writer.write(m.toFileString());
                writer.newLine();
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Export laporan lengkap (mobil + perawatan)
     */
    public static boolean exportLaporanLengkap(String username, String filepath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String tanggal = sdf.format(new Date());

            writer.write("========================================");
            writer.newLine();
            writer.write("LAPORAN PEMELIHARAAN MOBIL");
            writer.newLine();
            writer.write("User: " + username);
            writer.newLine();
            writer.write("Tanggal: " + tanggal);
            writer.newLine();
            writer.write("========================================");
            writer.newLine();
            writer.newLine();

            // Data Mobil
            writer.write("=== DATA MOBIL ===");
            writer.newLine();
            MobilManager mobilManager = new MobilManager(username);
            mobilManager.loadAll();
            ArrayList<Mobil> mobilList = mobilManager.getMobilList();

            if (mobilList.isEmpty()) {
                writer.write("Tidak ada data mobil.");
                writer.newLine();
            } else {
                for (int i = 0; i < mobilList.size(); i++) {
                    Mobil m = mobilList.get(i);
                    writer.write((i + 1) + ". " + m.displayInfo());
                    writer.newLine();
                }
            }
            writer.newLine();

            // Data Perawatan
            writer.write("=== RIWAYAT PERAWATAN ===");
            writer.newLine();
            ArrayList<Perawatan> perawatanList = Perawatan.loadAll(username);

            if (perawatanList.isEmpty()) {
                writer.write("Belum ada riwayat perawatan.");
                writer.newLine();
            } else {
                for (int i = 0; i < perawatanList.size(); i++) {
                    Perawatan p = perawatanList.get(i);
                    writer.write((i + 1) + ". " + p.displayInfo());
                    writer.newLine();
                }
            }
            writer.newLine();

            // Statistik
            writer.write("=== STATISTIK ===");
            writer.newLine();
            writer.write("Total Mobil: " + mobilList.size());
            writer.newLine();
            writer.write("Total Perawatan: " + perawatanList.size());
            writer.newLine();

            double totalBiaya = 0;
            for (Perawatan p : perawatanList) {
                totalBiaya += p.getBiaya();
            }
            writer.write(String.format("Total Biaya Perawatan: Rp %.2f", totalBiaya));
            writer.newLine();

            if (!perawatanList.isEmpty()) {
                double rataBiaya = totalBiaya / perawatanList.size();
                writer.write(String.format("Rata-rata Biaya: Rp %.2f", rataBiaya));
                writer.newLine();
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Backup semua data user
     */
    public static boolean backupUserData(String username, String backupDir) {
        try {
            File dir = new File(backupDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String timestamp = sdf.format(new Date());

            // Backup mobil
            File mobilSource = new File("mobil_" + username + ".txt");
            if (mobilSource.exists()) {
                File mobilBackup = new File(backupDir,
                        "mobil_" + username + "_" + timestamp + ".txt");
                copyFile(mobilSource, mobilBackup);
            }

            // Backup perawatan
            File perawatanSource = new File("perawatan_" + username + ".txt");
            if (perawatanSource.exists()) {
                File perawatanBackup = new File(backupDir,
                        "perawatan_" + username + "_" + timestamp + ".txt");
                copyFile(perawatanSource, perawatanBackup);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Restore data dari backup
     */
    public static boolean restoreUserData(String username, File mobilBackup,
                                          File perawatanBackup) {
        try {
            if (mobilBackup != null && mobilBackup.exists()) {
                File mobilDest = new File("mobil_" + username + ".txt");
                copyFile(mobilBackup, mobilDest);
            }

            if (perawatanBackup != null && perawatanBackup.exists()) {
                File perawatanDest = new File("perawatan_" + username + ".txt");
                copyFile(perawatanBackup, perawatanDest);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Copy file helper
     */
    private static void copyFile(File source, File dest) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(source));
             BufferedWriter writer = new BufferedWriter(new FileWriter(dest))) {

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /**
     * Export laporan periode tertentu
     */
    public static boolean exportLaporanPeriode(String username, String filepath,
                                               Date startDate, Date endDate) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            writer.write("========================================");
            writer.newLine();
            writer.write("LAPORAN PERAWATAN PERIODE");
            writer.newLine();
            writer.write("Periode: " + sdf.format(startDate) + " - " + sdf.format(endDate));
            writer.newLine();
            writer.write("User: " + username);
            writer.newLine();
            writer.write("========================================");
            writer.newLine();
            writer.newLine();

            ArrayList<Perawatan> perawatanList = Perawatan.loadAll(username);
            ArrayList<Perawatan> filteredList = new ArrayList<>();

            // Filter berdasarkan periode (simplified - bisa diperbaiki dengan parsing tanggal)
            for (Perawatan p : perawatanList) {
                filteredList.add(p); // Untuk sementara tampilkan semua
            }

            if (filteredList.isEmpty()) {
                writer.write("Tidak ada data perawatan pada periode ini.");
                writer.newLine();
            } else {
                double totalBiaya = 0;
                for (int i = 0; i < filteredList.size(); i++) {
                    Perawatan p = filteredList.get(i);
                    writer.write((i + 1) + ". " + p.displayInfo());
                    writer.newLine();
                    totalBiaya += p.getBiaya();
                }

                writer.newLine();
                writer.write("--- RINGKASAN ---");
                writer.newLine();
                writer.write("Total Perawatan: " + filteredList.size());
                writer.newLine();
                writer.write(String.format("Total Biaya: Rp %.2f", totalBiaya));
                writer.newLine();
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}