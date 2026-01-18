class Mobil {
    private String merek;
    private String model;
    private int tahun;
    private String nomorPolisi;
    private String statusPerawatan;

    public Mobil(String merek, String model, int tahun, String nomorPolisi) {
        this.merek = merek;
        this.model = model;
        this.tahun = tahun;
        this.nomorPolisi = nomorPolisi;
        this.statusPerawatan = "Belum dirawat";
    }

    public String displayInfo() {
        return merek + " " + model + " (" + tahun + ") - " + nomorPolisi +
                " - Status: " + statusPerawatan;
    }

    public void lakukanPerawatan() {
        statusPerawatan = "Sudah dirawat";
    }

    public String getMerek() {
        return merek;
    }

    public String getModel() {
        return model;
    }

    public int getTahun() {
        return tahun;
    }

    public String getNomorPolisi() {
        return nomorPolisi;
    }

    public String getStatusPerawatan() {
        return statusPerawatan;
    }

    public void setStatusPerawatan(String status) {
        this.statusPerawatan = status;
    }

    // Method untuk convert ke format file
    public String toFileString() {
        return merek + "," + model + "," + tahun + "," + nomorPolisi + ",Regular," + merek;
    }
}

class MobilSUV extends Mobil {
    private double kapasitasMesin;

    public MobilSUV(String merek, String model, int tahun, String nomorPolisi, double kapasitasMesin) {
        super(merek, model, tahun, nomorPolisi);
        this.kapasitasMesin = kapasitasMesin;
    }

    @Override
    public String displayInfo() {
        return super.displayInfo() + " - Kapasitas Mesin: " + kapasitasMesin + "L (SUV)";
    }

    public double getKapasitasMesin() {
        return kapasitasMesin;
    }

    @Override
    public String toFileString() {
        return getMerek() + "," + getModel() + "," + getTahun() + "," +
                getNomorPolisi() + ",SUV," + kapasitasMesin;
    }
}

class MobilSedan extends Mobil {
    private String tipeMesin;

    public MobilSedan(String merek, String model, int tahun, String nomorPolisi, String tipeMesin) {
        super(merek, model, tahun, nomorPolisi);
        this.tipeMesin = tipeMesin;
    }

    @Override
    public String displayInfo() {
        return super.displayInfo() + " - Tipe Mesin: " + tipeMesin + " (Sedan)";
    }

    public String getTipeMesin() {
        return tipeMesin;
    }

    @Override
    public String toFileString() {
        return getMerek() + "," + getModel() + "," + getTahun() + "," +
                getNomorPolisi() + ",Sedan," + tipeMesin;
    }
}