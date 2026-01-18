import java.util.*;

// Class untuk manage data perawatan
public class PerawatanManager implements DataOperations<Perawatan> {
    private String username;
    private ArrayList<Perawatan> perawatanList;

    public PerawatanManager(String username) {
        this.username = username;
        this.perawatanList = new ArrayList<>();
        loadAll();
    }

    @Override
    public void save(Perawatan perawatan) {
        perawatanList.add(perawatan);
        saveToFile();
    }

    @Override
    public ArrayList<Perawatan> loadAll() {
        perawatanList = Perawatan.loadAll(username);
        return perawatanList;
    }

    @Override
    public void delete(int index) {
        if (index >= 0 && index < perawatanList.size()) {
            perawatanList.remove(index);
            saveToFile();
        }
    }

    @Override
    public void update(int index, Perawatan perawatan) {
        if (index >= 0 && index < perawatanList.size()) {
            perawatanList.set(index, perawatan);
            saveToFile();
        }
    }

    private void saveToFile() {
        Perawatan.saveAll(perawatanList, username);
    }

    public ArrayList<Perawatan> getPerawatanList() {
        return perawatanList;
    }

    public ArrayList<Perawatan> getPerawatanByNopol(String nopol) {
        ArrayList<Perawatan> result = new ArrayList<>();
        for (Perawatan p : perawatanList) {
            if (p.getNomorPolisi().equalsIgnoreCase(nopol)) {
                result.add(p);
            }
        }
        return result;
    }

    public int getJumlahPerawatan() {
        return perawatanList.size();
    }

    public double getTotalBiaya() {
        double total = 0;
        for (Perawatan p : perawatanList) {
            total += p.getBiaya();
        }
        return total;
    }
}