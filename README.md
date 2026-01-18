# **Sistem Manajemen Perawatan Mobil**

## Deskripsi Proyek

Proyek ini adalah aplikasi **Sistem Manajemen Perawatan Mobil** yang dibangun menggunakan prinsip **Pemrograman Berorientasi Objek (OOP)** dengan bahasa pemrograman **Java**. Aplikasi ini bertujuan untuk membantu pemilik kendaraan dalam mengelola data kendaraan dan riwayat perawatan kendaraan secara digital, aman, dan efisien. Sistem ini dapat menangani pendaftaran pengguna, manajemen data kendaraan, manajemen riwayat perawatan, serta menyimpan data dalam format teks.

Aplikasi ini dirancang untuk menangani masalah umum yang dihadapi oleh pemilik kendaraan, seperti:

* Hilangnya buku servis fisik.
* Kesulitan dalam menghitung biaya perawatan.
* Kesulitan dalam melacak riwayat perawatan kendaraan.

Dengan aplikasi ini, pengguna dapat menyimpan data kendaraan dan riwayat perawatan dengan mudah, serta memantau biaya yang dikeluarkan untuk perawatan kendaraan mereka.

## Fitur Utama

1. **Pendaftaran dan Login Pengguna**:
   Pengguna dapat mendaftar dan login menggunakan username dan password yang aman. Password pengguna disimpan dengan teknik **SHA-256 hashing** yang diperkuat dengan **salting** untuk menjaga keamanan data.

2. **Manajemen Data Kendaraan**:
   Pengguna dapat menambahkan, mengedit, dan menghapus data kendaraan, termasuk merek, model, tahun, dan nomor polisi.

3. **Manajemen Riwayat Perawatan**:
   Pengguna dapat menambahkan dan mengelola riwayat perawatan kendaraan, termasuk tanggal perawatan, jenis perawatan, biaya yang dikeluarkan, dan bengkel tempat perawatan dilakukan.

4. **Dashboard**:
   Halaman utama aplikasi menampilkan statistik terkait kendaraan dan perawatan, seperti jumlah kendaraan yang terdaftar, total pengeluaran biaya perawatan, dan rata-rata biaya per servis.

5. **Ekspor Data**:
   Pengguna dapat mengekspor riwayat perawatan kendaraan ke dalam format **CSV**, yang dapat dibuka di aplikasi lain seperti Microsoft Excel.

6. **Keamanan Data**:
   Menggunakan **SHA-256 hashing** untuk melindungi kata sandi pengguna, memastikan bahwa data sensitif tidak disimpan dalam format plain text.

## Teknologi yang Digunakan

* **Bahasa Pemrograman**: Java
* **Framework GUI**: Java Swing
* **Algoritma Keamanan**: SHA-256 Hashing dengan Salting
* **Penyimpanan Data**: File Teks (CSV)
* **IDE yang Digunakan**: IntelliJ IDEA

## Struktur Proyek

Proyek ini menggunakan struktur berikut untuk memisahkan tanggung jawab dan memudahkan pengelolaan kode:

### 1. **User.java**

* Kelas ini mengelola data pengguna dan fitur login.
* Menyediakan metode untuk pendaftaran dan verifikasi kata sandi.

### 2. **Mobil.java**

* Kelas ini menyimpan informasi terkait kendaraan, seperti merek, model, tahun, dan nomor polisi.

### 3. **Perawatan.java**

* Kelas ini merepresentasikan riwayat perawatan kendaraan, termasuk informasi tentang jenis perawatan, biaya, tanggal, dan bengkel yang digunakan.

### 4. **MobilManager.java**

* Kelas ini mengelola daftar kendaraan yang terdaftar di sistem. Menyediakan metode untuk menambah, mengedit, dan menghapus data kendaraan.

### 5. **PerawatanManager.java**

* Kelas ini mengelola riwayat perawatan kendaraan, termasuk menambah, menyimpan, dan memuat data perawatan.

### 6. **PasswordUtils.java**

* Kelas ini mengelola proses pengamanan kata sandi pengguna dengan menggunakan **SHA-256 hashing** dan **salting**.

### 7. **InputValidator.java**

* Kelas ini berfungsi untuk memvalidasi input dari pengguna, seperti memastikan format nomor polisi dan email yang dimasukkan valid.

### 8. **ExportUtils.java**

* Kelas ini menangani fitur ekspor data ke dalam format **CSV** untuk keperluan analisis atau backup.

### 9. **MainApp.java**

* Kelas utama yang mengelola antarmuka pengguna (GUI) menggunakan Java Swing.
* Menampilkan informasi tentang kendaraan dan riwayat perawatan dalam bentuk tabel yang mudah dipahami.

## Cara Menjalankan Aplikasi

Untuk menjalankan aplikasi ini di komputer Anda, pastikan Anda telah menginstal **Java JDK** (versi 8 atau lebih baru) dan **IDE** seperti **IntelliJ IDEA** atau **Eclipse**.

1. **Clone repositori**:

   ```
   git clone https://github.com/username/repository-name.git
   ```

2. **Buka proyek di IDE pilihan Anda**.

3. **Jalankan aplikasi** dengan mengeksekusi kelas **MainApp.java**.

4. **Login** dengan memasukkan username dan password yang telah Anda daftarkan.

5. Setelah login, Anda bisa mulai menambahkan data kendaraan, riwayat perawatan, dan melihat statistik kendaraan Anda.

## Fitur yang Akan Datang

* **Integrasi dengan Database Relasional**:
  Rencana pengembangan lebih lanjut mencakup integrasi dengan **SQLite** atau **MySQL** untuk menangani volume data yang lebih besar.

* **Fitur Notifikasi**:
  Pengingat otomatis untuk jadwal servis kendaraan.

## Kontribusi

Jika Anda ingin berkontribusi pada proyek ini, silakan fork repositori ini, buat perubahan di branch Anda sendiri, dan ajukan pull request.

Pastikan untuk mengikuti pedoman kontribusi dan melakukan pengujian sebelum membuat pull request.

## Lisensi

Proyek ini dilisensikan di bawah **MIT License**.
