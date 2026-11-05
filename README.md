# AXIS E-Ticketing System

<p align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java" />
  <img src="https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white" alt="Maven" />
  <img src="https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL" />
  <img src="https://img.shields.io/badge/GUI-Java%20Swing-006C3B?style=for-the-badge" alt="Java Swing" />
  <img src="https://img.shields.io/badge/Architecture-MVC%20%2B%20DAO-1B5E20?style=for-the-badge" alt="MVC + DAO" />
</p>

<p align="center">
<img width="688" height="384" alt="axis" src="https://github.com/user-attachments/assets/1bc19637-76c5-4a4b-9943-7ce7cd72156f" />
</p>

---

Daftar Isi
1. [Deskripsi Singkat](#-Deskripsi-singkat)
2. [Fitur Utama Program](#-fitur-utama-program)
3. [Keunggulan Arsitektur & Teknis](#-keunggulan-arsitektur--teknis)
4. [Tech Stack](#-tech-stack)
5. [Perancangan Sistem (Flowchart, Use Case, ERD)](#-perancangan-sistem)
6. [Struktur Folder & Package](#-struktur-folderpackage)
7. [Penerapan 4 Pilar OOP](#-penerapan-4-pilar-oop)
8. [Cara Penggunaan Program](#-cara-penggunaan-program)
9. [Kontributor](#-kontributor)

---

## Deskripsi Singkat

**Axis E-Ticketing** adalah sistem aplikasi desktop berbasis Java Swing dengan basis data terpusat MySQL yang mendigitalisasi alur pelayanan transportasi perkeretaapian untuk rute Kereta Antarkota maupun Kereta Komuter. 

Sistem ini memfasilitasi dua peran pengguna:
* **Penumpang**: Mendaftar akun dengan validasi identitas, mengelola dan mengisi saldo AXIS E-Wallet, mencari serta memesan tiket kereta antarkota dengan fitur alokasi kursi otomatis hingga penerbitan e-ticket, serta melakukan *Tap-In* (dengan *barcode generator*) untuk layanan kereta komuter harian.
* **Admin**: Memantau statistik operasional harian secara real-time, mengelola data master (Stasiun, Jadwal Antarkota, dan Tarif Rute Komuter), memverifikasi proses Tap-Out penumpang komuter di stasiun tujuan, serta memonitor seluruh riwayat pemesanan dan detail tiket penumpang.

Seluruh alur operasi ini dibangun di atas arsitektur MVC (Model-View-Controller) dan DAO (Data Access Object) yang mengintegrasikan entitas pengguna (spesialisasi admin dan penumpang), riwayat_topup, pemesanan, tiket, jadwal_antarkota, stasiun, perjalanan_komuter, hingga tarif_komuter guna memastikan transaksi berjalan aman dan terstruktur.

---

## Fitur Utama Program

1. **Autentikasi Multi-Peran & Validasi Keamanan (*Role-Based Auth*)**
   - Login terpisah antara **Penumpang** (menggunakan Email & Password) dan **Admin** (menggunakan Kode Karyawan & Password).
   - Validasi registrasi ketat meliputi pengecekan format email (`@` dan domain valid), validasi numerik NIK & nomor telepon, serta pencegahan duplikasi akun.
2. **Ekosistem Pembayaran Digital (AXIS E-Wallet)**
   - Fitur *Top-Up* saldo instan menggunakan berbagai metode pembayaran (*Virtual Account*, *QRIS*, Minimarket) dengan batas keamanan nominal transaksi (`Rp 10.000` – `Rp 2.000.000` per transaksi dan limit saldo maksimal `Rp 10.000.000`).
   - Pencatatan otomatis ke dalam tabel **Riwayat Top-Up** di halaman profil penumpang.
3. **Pemesanan Tiket Kereta Antarkota Terintegrasi**
   - Pencarian jadwal berdasarkan stasiun asal, stasiun tujuan, tanggal keberangkatan (dilengkapi penguncian tanggal agar tidak bisa memilih masa lalu), dan jumlah penumpang.
   - Pengisian detail penumpang interaktif dilengkapi fitur *auto-fill* **"Penumpang 1 sama dengan pemesan"** dan *pop-up dialog* ringkas.
   - **Auto-Seat Allocation**: Sistem secara otomatis mengalokasikan nomor gerbong dan nomor kursi berurutan bagi setiap penumpang.
   - Pembayaran langsung memotong saldo AXIS E-Wallet menggunakan mekanisme *Database Transaction* (`commit` & `rollback`).
4. **Layanan Kereta Komuter (*Tap-In Gate* & *Deterministic Barcode*)**
   - Penumpang melakukan *Tap-In* di stasiun awal dan sistem men-*generate* visual garis **Barcode / Kode Unik Gate** (contoh: `KMT-8A3F19C2-001`).
   - **State-Locking Mechanism**: Selama perjalanan komuter belum diverifikasi *Tap-Out* oleh Admin, kode *barcode* penumpang dikunci tetap sama dan penumpang tidak dapat melakukan *Tap-In* ganda.
5. **Dashboard & Manajemen Operasional Admin**
   - **Kartu Statistik Real-Time**: Menampilkan jumlah penumpang antarkota hari ini, penumpang komuter hari ini, total gabungan, serta jumlah keberangkatan kereta.
   - **CRUD Data Master + Filter & Sorting**: Kelola Jadwal Antarkota (dilengkapi *sorting* waktu keberangkatan terdekat/terjauh), Kelola Stasiun (dilengkapi filter kategori layanan: *Antarkota*, *Komuter*, *Keduanya*), dan Kelola Tarif Komuter.
   - **Verifikasi Tap-Out Komuter Dinamis**: Menyaring otomatis daftar stasiun keluar berdasarkan ketersediaan rute pada tabel `tarif_komuter`, mencegah *Tap-Out* di stasiun yang sama dengan stasiun *Tap-In*, dan otomatis memotong saldo E-Wallet penumpang.
   - **Monitoring Pemesanan & Tiket**: Pencarian pemesanan berdasarkan Kode Booking atau NIK, pengurutan waktu transaksi, serta penampilan rincian tiket per kode booking.

---

## 🛡️ Keunggulan Arsitektur & Teknis

* **Pola Desain MVC + DAO Murni**: Memisahkan logika tampilan (`view`), pengendali alur bisnis (`controller`), struktur objek data (`model`), dan kueri basis data (`dao`).
* **Transaksi Database Atomik (*ACID Compliance*)**: Proses krusial seperti pemotongan saldo E-Wallet, pembuatan kode booking pemesanan, dan penerbitan tiket dibungkus dalam satu transaksi SQL (`setAutoCommit(false)`), sehingga jika terjadi kegagalan di tengah proses, saldo penumpang tidak akan terpotong sia-sia.
* **Manajemen Sesi Terpusat (`SessionManager`)**: Menyimpan dan menyinkronkan objek `Penumpang` atau `Admin` yang sedang aktif secara *real-time* di memori aplikasi.

---

## 🛠️ Tech Stack

| Kategori | Teknologi / Komponen |
|----------|-----------|
| **Bahasa Pemrograman** | Java (JDK 21 / JDK 26) |
| **Build Automation Tool** | Apache Maven |
| **Framework GUI** | Java Swing (NetBeans GUI Form Builder & Custom Graphics2D) |
| **Database** | MySQL (Relational Database) |
| **Library / Driver** | MySQL Connector/J (`mysql-connector-j`), JDBC API (`java.sql.*`) |
| **Arsitektur Sistem** | MVC (*Model-View-Controller*) + DAO (*Data Access Object*) |
| **Tools Pengembangan** | Apache NetBeans IDE, Laragon / XAMPP (MySQL Server), Draw.io, Git & GitHub |

---

## 📐 Perancangan Sistem

### 1. Flowchart

**A. Login/Register**

<img width="934" height="1350" alt="CapstoneAxis-Login_Register drawio (1)" src="https://github.com/user-attachments/assets/26c84ce7-b3fd-4abe-b146-ea62643a937e" />

Alur program diawali dari Menu Login yang menyediakan pilihan akses bagi pengguna. Pada akses penumpang, pengguna menginput email dan password yang divalidasi ke database untuk masuk ke Menu Utama, dengan proteksi sleep 5 menit jika gagal 3 kali; sedangkan calon pengguna baru dapat memilih menu registrasi untuk mendaftarkan email, password, nomor telepon, dan NIK hingga akun berhasil dibuat. Bagi pihak pengelola, opsi login admin memerlukan verifikasi kode karyawan dan password untuk membuka Dashboard Admin, atau pengguna dapat memilih keluar dari sistem untuk mengakhiri sesi program secara keseluruhan.

**B. Menu Utama**

<img width="1536" height="2320" alt="CapstoneAxis-Menu Utama drawio (1)" src="https://github.com/user-attachments/assets/58aaf937-3aa8-46de-99e1-01f43464f564" />

Alur program pada flowchart Menu Utama menyediakan navigasi bagi penumpang untuk mengakses fitur utama seperti Menu Membeli Tiket, Menu Riwayat Tiket, Menu Akun, atau LogOut. Pada fitur pembelian tiket, penumpang dapat memilih tiga jenis layanan: E-ticket Antar Kota yang melibatkan pemilihan stasiun asal-tujuan, tanggal, jadwal kereta, pengisian serta validasi data penumpang, penentuan kursi otomatis, hingga proses pembayaran menggunakan saldo e-wallet untuk menerbitkan tiket yang masuk ke riwayat; Isi Saldo Axis E-Wallet yang memproses penginputan nominal serta metode pembayaran hingga pembaruan saldo dan riwayat top-up; serta Layanan Komuter yang menangani proses tap-in stasiun awal (pembuatan QR code), verifikasi admin, pembaruan stasiun akhir, penentuan tarif potongan, hingga pemotongan saldo e-wallet penumpang. Jika saldo e-wallet tidak mencukupi saat transaksi tiket maupun komuter, sistem akan mengarahkan pengguna untuk melakukan top-up saldo terlebih dahulu sebelum menyelesaikan prosesnya.

**C. Dashboard Admin**

<img width="2651" height="2159" alt="CapstoneAxis-DashBoard Admin drawio (1)" src="https://github.com/user-attachments/assets/9666b248-fad1-4bdb-a137-5d5f0d3554a7" />

Alur program pada Dashboard Admin diawali dengan menampilkan ringkasan data total penumpang hari ini (antarkota, komuter, maupun keduanya) beserta jumlah keberangkatan, sebelum admin memilih lima menu navigasi utama. Pada menu Kelola Jadwal Antarkota, Kelola Stasiun, dan Kelola Tarif Stasiun, admin dapat melakukan operasi pembaruan data (seperti penambahan atau pengubahan data kereta, stasiun, serta tarif) yang semuanya melalui tahap validasi input sebelum tersimpan ke dalam database masing-masing. Sementara itu, opsi Monitoring Pemesanan memungkinkan admin memantau riwayat data pemesanan tiket, dan menu Verifikasi Tap Out Komuter digunakan untuk memproses verifikasi penumpang yang keluar, termasuk pengecekan kecukupan saldo e-wallet, pemotongan saldo sesuai tarif komuter, serta pembaruan status data perjalanan penumpang di database.

**D. Riwayat Tiket**

<img width="599" height="802" alt="CapstoneAxis-Riwayat Tiket drawio (1)" src="https://github.com/user-attachments/assets/3850206b-1df7-4324-99ee-66190b184d62" />

Alur program pada flowchart Menu Riwayat Tiket diawali dengan masuk ke Menu Riwayat AntarKota, di mana pengguna dapat memilih untuk melihat Riwayat AntarKota yang mengambil data dari database Pemesanan untuk menampilkan riwayat pemesanan antarkota beserta status pembayarannya, atau beralih ke Menu Riwayat Komuter yang mengambil data dari database Perjalanan Komuter untuk menyajikan riwayat perjalanan komuter. Setelah informasi riwayat ditampilkan, pengguna dihadapkan pada opsi navigasi pindah halaman; jika pengguna memilih Ya, sistem akan mengarahkan pengguna kembali ke Menu Utama, sedangkan jika memilih Tidak, alur akan berputar kembali ke tampilan awal Menu Riwayat AntarKota.

**E. Akun** 

<img width="502" height="816" alt="CapstoneAxis-Akun drawio (1)" src="https://github.com/user-attachments/assets/5f896c25-a7d2-4a71-87ef-01717c8568b8" />

Alur program pada flowchart Menu Akun diawali dengan mengakses database Pengguna.Penumpang untuk menampilkan profil pengguna berupa nama penumpang, email, nomor telepon, dan saldo e-wallet. Selanjutnya, sistem mengambil data dari database Riwayat_topup untuk menyajikan tabel riwayat pengisian saldo yang telah dilakukan oleh pengguna. Setelah seluruh informasi akun dan riwayat top-up ditampilkan, pengguna dihadapkan pada opsi navigasi pindah halaman; jika memilih YES, pengguna akan diarahkan kembali ke Menu Utama, sedangkan jika memilih NO, alur program akan tetap bertahan di halaman Menu Akun.

---

### 2. Use Case Diagram

<img width="1089" height="1027" alt="UseCase_AXIS-Use Case AXIS Ver1 drawio" src="https://github.com/user-attachments/assets/ee2aad1c-7354-4f0b-9faa-f931e205998d" />

Sistem E-Ticketing AXIS melibatkan dua aktor utama: Penumpang yang berwenang mencari jadwal, memesan tiket antarkota, membayar pesanan, mengisi saldo e-wallet, melakukan tap in/tap out, melihat riwayat tiket, serta mengelola akun; dan Admin yang bertugas mengelola data stasiun, jadwal antarkota, tarif komuter, memantau data pemesanan/tiket, serta melakukan verifikasi keluar komuter. Seluruh akses fungsionalitas tersebut terintegrasi melalui mekanisme keamanan dasar berupa Login, Registrasi untuk pengguna baru, dan Logout.

---

### 3. Entity Relationship Diagram (ERD)

**A. Logical ERD**

<img width="1593" height="651" alt="image" src="https://github.com/user-attachments/assets/5023cdde-dc29-4def-8b3d-29212c75bd7b" />

**B. Relational ERD**

<img width="1600" height="711" alt="image" src="https://github.com/user-attachments/assets/965f8581-2ac5-4c1c-b33b-29e0aa9d5f0c" />

Sistem E-Ticketing AXIS dibangun di atas entitas utama `pengguna` yang terspesialisasi menjadi dua peran, yaitu `admin` dan `penumpang`. Entitas `penumpang` berelasi langsung dengan `riwayat_topup` untuk mencatat transaksi pengisian saldo e-wallet, serta dapat melakukan transaksi `pemesanan` yang diawasi oleh `admin`. Dari pemesanan tersebut, sistem menerbitkan entitas `tiket` yang terhubung ke `jadwal_antarkota`. Keberangkatan dan tujuan pada `jadwal_antarkota` ini mengacu pada entitas `stasiun`. Selain itu, penumpang juga berinteraksi dengan tabel `perjalanan_komuter` yang mencatat aktivitas keberangkatan dan kedatangan antar stasiun, dengan penentuan harga rute yang diatur melalui entitas `tarif_komuter`.

---

## 📂 Struktur Folder/Package

Proyek ini menggunakan struktur standar **Apache Maven** dengan pemisahan *package* berbasis tanggung jawab (**MVC + DAO**):

```text
AXIS/
├── src/
│   └── main/
│       ├── java/
│       │   ├── com/mycompany/axis/
│       │   │   └── AXIS.java                    # Main Class (Entry Point Aplikasi)
│       │   ├── model/                           # Representasi Entitas & Logika Domain (POJO/OOP)
│       │   │   ├── Pengguna.java                # Abstract Parent Class
│       │   │   ├── Admin.java                   # Subclass Pengguna (Admin)
│       │   │   ├── Penumpang.java               # Subclass Pengguna (Penumpang)
│       │   │   ├── Stasiun.java
│       │   │   ├── JadwalAntarkota.java
│       │   │   ├── Pemesanan.java
│       │   │   ├── Tiket.java
│       │   │   ├── PerjalananKomuter.java
│       │   │   ├── TarifKomuter.java
│       │   │   └── RiwayatTopup.java
│       │   ├── dao/                             # Data Access Object (Kueri SQL & Transaksi JDBC)
│       │   │   ├── AuthDAO.java
│       │   │   ├── AdminDAO.java
│       │   │   ├── PenumpangDAO.java
│       │   │   ├── PemesananDAO.java
│       │   │   └── KomuterDAO.java
│       │   ├── controller/                      # Penghubung Logika Bisnis antara View & DAO/Model
│       │   │   ├── SessionManager.java
│       │   │   ├── AuthController.java
│       │   │   ├── AdminController.java
│       │   │   ├── MenuUtamaController.java
│       │   │   ├── PemesananController.java
│       │   │   └── KomuterController.java
│       │   ├── view/                            # Antarmuka Pengguna Grafis (Java Swing JFrame Forms)
│       │   │   ├── LoginView.java
│       │   │   ├── RegisterView.java
│       │   │   ├── AdminLoginView.java
│       │   │   ├── DashboardAdminView.java
│       │   │   ├── MenuUtamaView.java
│       │   │   ├── PilihJadwalView.java
│       │   │   ├── ReviewPemesananView.java
│       │   │   └── KomuterView.java
│       │   └── util/                            # Utilitas Koneksi Database
│       │       └── DBConnection.java
│       └── resources/
│           └── image/                           # Aset Gambar & Logo Aplikasi
│               ├── axis-upd-wht.png
│               ├── asix-upd-blck.png
│               ├── axisTrain.jpg
│               └── traindeploy.jpg
├── axis.sql                                     # File Dump Skema & Data Awal Database MySQL
├── pom.xml                                      # Konfigurasi Proyek & Dependensi Maven
└── README.md                                    # Dokumentasi Proyek
```

---

## 🧩 Penerapan 4 Pilar OOP

Seluruh komponen di dalam aplikasi **AXIS E-Ticketing** dirancang dengan menerapkan keempat pilar utama Pemrograman Berorientasi Objek (*Object-Oriented Programming*):

### 1. Abstraction (Abstraksi)
**Penjelasan:**  
Abstraksi diterapkan pada class `Pengguna` yang dideklarasikan sebagai `abstract class`. Di dunia nyata, tidak ada entitas "Pengguna" yang bersifat umum tanpa peran yang jelas; setiap pengguna pasti merupakan seorang `Admin` atau `Penumpang`. Oleh karena itu, `Pengguna` tidak dapat diinstansiasi secara langsung dan mendefinisikan struktur dasar serta kontrak perilaku yang relevan bagi seluruh akun di dalam sistem.

**Lokasi:** `src/main/java/model/Pengguna.java`

```java
package model;

public abstract class Pengguna {
    protected String idPengguna;
    protected String email;
    protected String password;

    public Pengguna(String idPengguna, String email, String password) {
        this.idPengguna = idPengguna;
        this.email = email;
        this.password = password;
    }

    // Method konkret & abstraksi identitas yang diwariskan ke subclass
    public String getIdPengguna() { return idPengguna; }
    public String getEmail() { return email; }
}
```

---

### 2. Inheritance (Pewarisan)
**Penjelasan:**  
Pewarisan diterapkan pada class `Penumpang` dan `Admin` yang merupakan *subclass* (*child class*) dari `Pengguna` menggunakan kata kunci `extends`. Kedua class tersebut mewarisi atribut `idPengguna`, `email`, dan `password` beserta method *getter/setter* dari `Pengguna`, lalu menambahkan atribut spesifik sesuai perannya (seperti `nikNomorPassport` dan `saldoEwallet` pada `Penumpang`, atau `kodeKaryawan` dan `levelAkses` pada `Admin`).

**Lokasi:** `src/main/java/model/Penumpang.java` & `src/main/java/model/Admin.java`

```java
package model;

public class Penumpang extends Pengguna {
    private String nikNomorPassport;
    private String namaLengkap;
    private String nomorTelepon;
    private double saldoEwallet;

    public Penumpang(String idPengguna, String email, String password, 
                     String nikNomorPassport, String namaLengkap, 
                     String nomorTelepon, double saldoEwallet) {
        // Memanggil constructor parent class (Pengguna)
        super(idPengguna, email, password);
        this.nikNomorPassport = nikNomorPassport;
        this.namaLengkap = namaLengkap;
        this.nomorTelepon = nomorTelepon;
        this.saldoEwallet = saldoEwallet;
    }
}
```

---

### 3. Encapsulation (Enkapsulasi)
**Penjelasan:**  
Seluruh atribut pada class-class di dalam *package* `model` (seperti `PerjalananKomuter`, `Tiket`, `Pemesanan`, `Stasiun`) maupun state di dalam `controller` dibungkus menggunakan hak akses `private` (*data hiding*). Akses untuk membaca atau memodifikasi nilai atribut hanya dapat dilakukan melalui method `public` (*Getter* dan *Setter*) serta method bisnis terkontrol, sehingga integritas data tetap terjaga.

**Lokasi:** `src/main/java/model/PerjalananKomuter.java`

```java
package model;

import java.util.UUID;

public class PerjalananKomuter {
    // Atribut dienkapsulasi dengan modifier private
    private int idPerjalanan;
    private String waktuTapIn;
    private String statusPerjalanan;
    private String penumpangNik;

    // Akses terkontrol melalui public method
    public boolean isMasihAktif() {
        return statusPerjalanan == null || !statusPerjalanan.equalsIgnoreCase("Selesai");
    }

    public String getKodeUnikGate() {
        String rawSeed = idPerjalanan + "-" + penumpangNik + "-" + waktuTapIn;
        String hash = UUID.nameUUIDFromBytes(rawSeed.getBytes()).toString().substring(0, 8).toUpperCase();
        return String.format("KMT-%s-%03d", hash, idPerjalanan);
    }

    public int getIdPerjalanan() { return idPerjalanan; }
    public void setIdPerjalanan(int idPerjalanan) { this.idPerjalanan = idPerjalanan; }
}
```

---

### 4. Polymorphism (Polimorfisme)
**Penjelasan:**  
Polimorfisme diterapkan baik dalam bentuk **Method Overriding** (*Runtime Polymorphism*) maupun **Constructor/Method Overloading** (*Compile-time Polymorphism*):
1. **Method Overriding (`@Override`)**: Digunakan pada kustomisasi komponen GUI dan model tabel, seperti meng-*override* method `isCellEditable()` pada `DefaultTableModel` agar sel tabel bersifat *read-only*, serta meng-*override* `paintComponent(Graphics g)` pada `KomuterView` untuk menggambar garis-garis visual *barcode* secara dinamis.
2. **Constructor Overloading**: Digunakan pada class tampilan seperti `PilihJadwalView` dan `ReviewPemesananView` yang memiliki lebih dari satu *constructor* dengan parameter berbeda (tanpa parameter untuk inisialisasi standar, serta dengan parameter `List<JadwalAntarkota>` atau `PemesananController` untuk injeksi data antorhalaman).

**Lokasi:** `src/main/java/view/KomuterView.java` & `src/main/java/view/PilihJadwalView.java`

```java
// Contoh 1: Method Overriding (@Override) untuk menggambar Barcode Komuter
JPanel canvasGarisBarcode = new JPanel() {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Implementasi kustom menggambar pola garis barcode dari kode unik
    }
};

// Contoh 2: Method Overriding (@Override) untuk mengunci edit sel pada JTable
modelJadwal = new DefaultTableModel(
    new String[]{"ID", "Nama Kereta", "Berangkat", "Tiba", "Harga / Pax"}, 0
) {
    @Override
    public boolean isCellEditable(int r, int c) {
        return false; // Mengubah perilaku default tabel menjadi read-only
    }
};
```

---

## 🚀 Cara Penggunaan Program

### Prasyarat & Instalasi

1. **Clone Repository**
   ```bash
   git clone https://github.com/username/AXIS.git
   cd AXIS
   ```
2. **Persiapan Database MySQL**
   - Jalankan layanan **MySQL** melalui Laragon atau XAMPP.
   - Buat database baru bernama `axis` di phpMyAdmin / MySQL Client.
   - *Import* file skema database `axis.sql` ke dalam database `axis`.
3. **Konfigurasi Koneksi Database**
   - Pastikan pengaturan URL, `USER`, dan `PASS` di dalam file `src/main/java/util/DBConnection.java` telah sesuai dengan konfigurasi MySQL lokal Anda (default: `jdbc:mysql://localhost:3306/axis`, user: `root`, password: `""`).
4. **Menjalankan Aplikasi**
   - Buka proyek **AXIS** menggunakan **Apache NetBeans IDE**.
   - Klik kanan pada proyek $\rightarrow$ pilih **Clean and Build**.
   - Tekan **F6 (Run Project)** untuk menjalankan class utama `com.mycompany.axis.AXIS`.

---

### Panduan Penggunaan (dengan Screenshot GUI)

#### 1. Halaman Login & Registrasi Penumpang
![Login](docs/images/login.png)
* **Login Penumpang**: Masukkan `Email` dan `Password` yang telah terdaftar, lalu klik tombol **Login** untuk masuk ke Beranda Utama.
* **Registrasi Akun Baru**: Klik tombol **Belum punya akun? Klik Disini** untuk membuka halaman Registrasi. Isi Nama Lengkap, Email (wajib menggunakan karakter `@` dan domain valid), Password, Nomor Telepon, serta NIK/Paspor, lalu klik **Register**.
* **Akses Admin**: Klik tombol **Admin** di bagian bawah untuk beralih ke halaman Login khusus Admin (`AdminLoginView`).

#### 2. Halaman Beranda Utama Penumpang (`MenuUtamaView`)
![Beranda](docs/images/beranda.png)
* **Pencarian Kereta Antarkota**: Pilih *Stasiun Asal*, *Stasiun Tujuan*, *Tanggal Berangkat* (mulai dari hari ini ke depan), dan *Jumlah Penumpang*, lalu klik **Cari jadwal kereta $\rightarrow$**.
* **Top-Up AXIS E-Wallet**: Pada kartu *Saldo AXIS E-Wallet*, klik tombol **+ Top Up Saldo**, masukkan nominal (`Rp 10.000` – `Rp 2.000.000`), pilih metode pembayaran, dan masukkan nomor rekening/HP asal.
* **Layanan Komuter**: Klik tombol **Masuk Komuter $\rightarrow$** pada kartu *Layanan Kereta Komuter* untuk melakukan *Tap-In*.
* **Navigasi Downbar**: Gunakan bilah navigasi bawah (*Downbar*) untuk berpindah secara instan antara halaman **Beranda**, **Tiket** (*Riwayat Antarkota & Komuter*), dan **Akun** (*Profil & Riwayat Top-Up*).

#### 3. Halaman Pemesanan, Detail Penumpang & Pembayaran Antarkota (`PilihJadwalView` & `ReviewPemesananView`)
![Transaksi Antarkota](docs/images/transaksi-antarkota.png)
* **Langkah 1 (Pilih Jadwal & Isi Penumpang)**: Klik salah satu jadwal kereta yang tersedia pada tabel di sisi kiri. Di sisi kanan, centang **"Penumpang 1 sama dengan pemesan"** untuk mengisi otomatis data diri Anda, atau klik kartu **Isi Detail Penumpang** untuk memunculkan *pop-up* pengisian Nama, Nomor Telepon, dan NIK. Klik **Lanjutkan ke Review & Bayar $\rightarrow$**.
* **Langkah 2 (Review Kursi Otomatis & Pembayaran)**: Periksa daftar penumpang beserta Gerbong dan Nomor Kursi yang telah dialokasikan secara otomatis oleh sistem. Periksa total tagihan dan saldo E-Wallet Anda, kemudian klik **Bayar Sekarang (Potong Saldo)**.

#### 4. Halaman Layanan Kereta Komuter (`KomuterView`)
![Komuter](docs/images/komuter.png)
* Pilih **Stasiun Awal Tap-In** pada *dropdown*, lalu klik **Tap-In & Generate Kode Unik (QR/Barcode)**.
* Sistem akan menampilkan garis visual **Barcode** beserta kode akses *gate* (contoh: `KMT-8A3F19C2-001`). Selama Anda belum diverifikasi *Tap-Out* oleh Admin di stasiun tujuan, tombol *Tap-In* akan terkunci otomatis dan menampilkan status perjalanan aktif Anda.

#### 5. Halaman Dashboard & Manajemen Admin (`DashboardAdminView`)
![Dashboard Admin](docs/images/dashboard-admin.png)
* **Dashboard**: Menampilkan 4 kartu metrik utama (Penumpang Antarkota Hari Ini, Penumpang Komuter Hari Ini, Total Gabungan Penumpang, dan Jumlah Keberangkatan).
* **Kelola Antarkota**: Menambah dan memperbarui jadwal kereta antarkota serta mengurutkan jadwal berdasarkan waktu keberangkatan terdekat atau terjauh.
* **Kelola Stasiun**: Mengelola daftar stasiun (dilengkapi filter kategori layanan) dan mengatur harga rute pada tab **Kelola Tarif**.
* **Verif Tap-Out**: Memilih penumpang komuter yang sedang aktif dari tabel, memilih **Stasiun Akhir** (otomatis terfilter sesuai rute yang tersedia), melihat estimasi potongan tarif, dan menekan **Verifikasi Tap-Out** untuk memotong saldo penumpang.
* **Monitor Pemesanan**: Mencari riwayat pemesanan tiket berdasarkan Kode Booking atau NIK, mengurutkan berdasarkan waktu pesan terbaru/terlama, dan melihat rincian tiket beserta *barcode* penumpang di tabel bawah.

---

## 👥 Kontributor

Proyek Capstone ini dikembangkan oleh Kelompok **AXIS**:

| No | Nama Mahasiswa | NIM | Kelas |
| :---: | :--- | :---: | :---: |
| 1 | **Ahmad Afif Al Ghifary** | `2509116002` | A 2025 |
| 2 | **Muhammad Ibrahim Kamil** | `2509116012` | A 2025 |
| 3 | **Muhammad Arham Anugrah** | `2509116044` | B 2025 |
| 4 | **Muhammad Rizky Alfa Reza Basyah** | `2509116086` | C 2025 |

---
<p align="center">
  <b>Powered by AXIS Capstone Team © 2026</b>
</p>
