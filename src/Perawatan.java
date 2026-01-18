import java.io.*;
import java.util.*;

// Class untuk merepresentasikan data perawatan
public class Perawatan {
    private String tanggal;
    private String nomorPolisi;
    private String jenisPerawatan;
    private double biaya;
    private String bengkel;
    private String status;

    public Perawatan(String tanggal, String nomorPolisi, String jenisPerawatan,
                     double biaya, String bengkel) {
        this.tanggal = tanggal;
        this.nomorPolisi = nomorPolisi;
        this.jenisPerawatan = jenisPerawatan;
        this.biaya = biaya;
        this.bengkel = bengkel;
        this.status = "Selesai";
    }

    // Constructor untuk load dari file
    public Perawatan(String tanggal, String nomorPolisi, String jenisPerawatan,
                     String biaya, String bengkel) {
        this.tanggal = tanggal;
        this.nomorPolisi = nomorPolisi;
        this.jenisPerawatan = jenisPerawatan;
        try {
            this.biaya = Double.parseDouble(biaya);
        } catch (NumberFormatException e) {
            this.biaya = 0;
        }
        this.bengkel = bengkel;
        this.status = "Selesai";
    }

    // Method untuk menyimpan ke file
    public void save(String username) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("perawatan_" + username + ".txt", true))) {
            writer.write(toFileString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method untuk convert ke format file
    public String toFileString() {
        return tanggal + "," + nomorPolisi + "," + jenisPerawatan + "," +
                biaya + "," + bengkel;
    }

    // Method untuk display info
    public String displayInfo() {
        return String.format("Tanggal: %s | No.Pol: %s | %s | Rp %.2f | %s",
                tanggal, nomorPolisi, jenisPerawatan, biaya, bengkel);
    }

    // Static method untuk load semua perawatan dari file
    public static ArrayList<Perawatan> loadAll(String username) {
        ArrayList<Perawatan> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new FileReader("perawatan_" + username + ".txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 5) {
                    list.add(new Perawatan(data[0], data[1], data[2], data[3], data[4]));
                }
            }
        } catch (IOException e) {
            // File belum ada, return empty list
        }
        return list;
    }

    // Static method untuk save semua perawatan
    public static void saveAll(ArrayList<Perawatan> perawatanList, String username) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("perawatan_" + username + ".txt"))) {
            for (Perawatan p : perawatanList) {
                writer.write(p.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getters
    public String getTanggal() { return tanggal; }
    public String getNomorPolisi() { return nomorPolisi; }
    public String getJenisPerawatan() { return jenisPerawatan; }
    public double getBiaya() { return biaya; }
    public String getBengkel() { return bengkel; }
    public String getStatus() { return status; }

    // Setters
    public void setStatus(String status) { this.status = status; }
}

// Class untuk statistik perawatan
class StatistikPerawatan {
    private ArrayList<Perawatan> perawatanList;

    public StatistikPerawatan(ArrayList<Perawatan> perawatanList) {
        this.perawatanList = perawatanList;
    }

    public double getTotalBiaya() {
        double total = 0;
        for (Perawatan p : perawatanList) {
            total += p.getBiaya();
        }
        return total;
    }

    public int getTotalPerawatan() {
        return perawatanList.size();
    }

    public double getRataRataBiaya() {
        if (perawatanList.isEmpty()) return 0;
        return getTotalBiaya() / getTotalPerawatan();
    }

    public HashMap<String, Integer> getStatistikPerJenis() {
        HashMap<String, Integer> stats = new HashMap<>();
        for (Perawatan p : perawatanList) {
            String jenis = p.getJenisPerawatan();
            stats.put(jenis, stats.getOrDefault(jenis, 0) + 1);
        }
        return stats;
    }
}