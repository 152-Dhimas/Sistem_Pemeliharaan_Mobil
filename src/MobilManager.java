import java.io.*;
import java.util.*;

// Interface untuk operasi CRUD
interface DataOperations<T> {
    void save(T item);
    ArrayList<T> loadAll();
    void delete(int index);
    void update(int index, T item);
}

// Class untuk manage data mobil dengan OOP pattern
public class MobilManager implements DataOperations<Mobil> {
    private String username;
    private ArrayList<Mobil> mobilList;

    public MobilManager(String username) {
        this.username = username;
        this.mobilList = new ArrayList<>();
        loadAll();
    }

    @Override
    public void save(Mobil mobil) {
        mobilList.add(mobil);
        saveToFile();
    }

    @Override
    public ArrayList<Mobil> loadAll() {
        mobilList.clear();
        try (BufferedReader reader = new BufferedReader(
                new FileReader("mobil_" + username + ".txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6) {
                    String merek = data[0];
                    String model = data[1];
                    int tahun = Integer.parseInt(data[2]);
                    String nopol = data[3];
                    String tipe = data[4];
                    String detail = data[5];

                    Mobil mobil;
                    if (tipe.equalsIgnoreCase("SUV")) {
                        try {
                            double cc = Double.parseDouble(detail);
                            mobil = new MobilSUV(merek, model, tahun, nopol, cc);
                        } catch (NumberFormatException e) {
                            mobil = new Mobil(merek, model, tahun, nopol);
                        }
                    } else if (tipe.equalsIgnoreCase("Sedan")) {
                        mobil = new MobilSedan(merek, model, tahun, nopol, detail);
                    } else {
                        mobil = new Mobil(merek, model, tahun, nopol);
                    }
                    mobilList.add(mobil);
                }
            }
        } catch (IOException e) {
            // File belum ada
        }
        return mobilList;
    }

    @Override
    public void delete(int index) {
        if (index >= 0 && index < mobilList.size()) {
            mobilList.remove(index);
            saveToFile();
        }
    }

    @Override
    public void update(int index, Mobil mobil) {
        if (index >= 0 && index < mobilList.size()) {
            mobilList.set(index, mobil);
            saveToFile();
        }
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("mobil_" + username + ".txt"))) {
            for (Mobil m : mobilList) {
                writer.write(m.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Mobil> getMobilList() {
        return mobilList;
    }

    public Mobil getMobilByNopol(String nopol) {
        for (Mobil m : mobilList) {
            if (m.getNomorPolisi().equalsIgnoreCase(nopol)) {
                return m;
            }
        }
        return null;
    }

    public int getJumlahMobil() {
        return mobilList.size();
    }
}