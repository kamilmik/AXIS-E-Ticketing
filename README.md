# Axis E-Ticketing

<img width="2752" height="1536" alt="axis" src="https://github.com/user-attachments/assets/1bc19637-76c5-4a4b-9943-7ce7cd72156f" />

---

## Deskripsi Singkat

Axis E-Ticketing adalah sistem aplikasi berbasis Java dengan database terpusat yang mendigitalisasi alur pelayanan transportasi perkeretaapian untuk rute antarkota maupun komuter. Sistem ini memfasilitasi dua peran pengguna: Penumpang yang mendaftar akun, mengisi saldo e-wallet, memesan tiket antarkota hingga penerbitan e-ticket, serta melakukan tap-in/tap-out layanan komuter; dan Admin yang bertugas memantau total penumpang harian, mengelola data master (stasiun, jadwal antarkota, dan tarif komuter), serta memverifikasi transaksi pemesanan dan keberangkatan penumpang. Seluruh alur operasi ini terhubung dalam arsitektur basis data relasional yang mengintegrasikan entitas pengguna (spesialisasi admin dan penumpang), riwayat_topup, pemesanan, tiket, jadwal_antarkota, stasiun, perjalanan_komuter, hingga tarif_komuter guna memastikan alur transaksi digital berjalan dengan aman, transparan, dan terstruktur.

---

## Fitur Utama Program

1. **Login dengan Hak Akses (Role-Based)**: pendaftaran akun dan login dengan dua peran.
   - **Penumpang**: memesan tiket serta melihat jadwal dan harga secara *real-time*.
   - **Admin**: mengelola seluruh sistem.
2. **Dashboard Admin**: admin dapat menambah, mengedit, melihat, dan menghapus (CRUD) data **rute**, **jadwal**, dan **harga tiket**.
3. **Pemesanan Dua Alur**:
   - **Antarkota**: penumpang mencari tiket dan wajib memilih nomor kursi dan gerbong.
   - **Komuter**: penumpang memesan tiket rute tanpa memilih kursi (berbasis sistem kuota).
4. **Konfirmasi Pembayaran**: sistem membuat **kode booking unik**, lalu penumpang mengunggah **bukti transfer** pembayaran.
5. **Verifikasi dan Penerbitan E-Ticket**: admin memeriksa dan memvalidasi bukti pembayaran, kemudian sistem otomatis menerbitkan **e-ticket (barcode/ID unik)** ke akun penumpang.
6. **Rekapitulasi Transaksi**: halaman rekap data transaksi yang berhasil beserta total pendapatan perusahaan.

---

##  Tech Stack

| Kategori | Teknologi |
|----------|-----------|
| **Bahasa Pemrograman** | Java with Maven|
| **Database** | MySQL |
| **Tools** | NetBeans, Laragon, Git, GitHub |

---

##  Perancangan Sistem

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


### 2. Use Case Diagram

<img width="1089" height="1027" alt="UseCase_AXIS-Use Case AXIS Ver1 drawio" src="https://github.com/user-attachments/assets/ee2aad1c-7354-4f0b-9faa-f931e205998d" />

Sistem E-Ticketing AXIS melibatkan dua aktor utama: Penumpang yang berwenang mencari jadwal, memesan tiket antarkota, membayar pesanan, mengisi saldo e-wallet, melakukan tap in/tap out, melihat riwayat tiket, serta mengelola akun; dan Admin yang bertugas mengelola data stasiun, jadwal antarkota, tarif komuter, memantau data pemesanan/tiket, serta melakukan verifikasi keluar komuter. Seluruh akses fungsionalitas tersebut terintegrasi melalui mekanisme keamanan dasar berupa Login, Registrasi untuk pengguna baru, dan Logout.

### 3. Entity Relationship Diagram (ERD)

**A. Logical ERD**

<img width="1593" height="651" alt="image" src="https://github.com/user-attachments/assets/5023cdde-dc29-4def-8b3d-29212c75bd7b" />

**B. Relational ERD**

<img width="1600" height="711" alt="image" src="https://github.com/user-attachments/assets/965f8581-2ac5-4c1c-b33b-29e0aa9d5f0c" />

Sistem E-Ticketing AXIS dibangun di atas entitas utama pengguna yang terspesialisasi menjadi dua peran, yaitu admin dan penumpang. Entitas penumpang berelasi langsung dengan riwayat_topup untuk mencatat transaksi pengisian saldo e-wallet, serta dapat melakukan transaksi pemesanan yang diawasi oleh admin. Dari pemesanan tersebut, sistem menerbitkan entitas tiket yang terhubung ke jadwal_antarkota. Keberangkatan dan tujuan pada jadwal_antarkota ini mengacu pada entitas stasiun. Selain itu, penumpang juga berinteraksi dengan tabel perjalanan_komuter yang mencatat aktivitas keberangkatan dan kedatangan antar stasiun, dengan penentuan harga rute yang diatur melalui entitas tarif_komuter.

---

##  Struktur Folder/Package

```
NamaProyek/
├── src/
│   ├── model/          # Class entitas/data (POJO)
│   │   ├── User.java
│   │   └── ...
│   ├── view/           # Tampilan GUI
│   │   ├── LoginForm.java
│   │   └── ...
│   ├── controller/     # Logika penghubung view & model
│   │   └── ...
│   ├── dao/            # Akses database
│   │   └── ...
│   ├── util/           # Class bantu (koneksi DB, validasi, dll.)
│   │   └── DatabaseConnection.java
│   └── Main.java       # Entry point program
├── database/
│   └── nama_database.sql
├── docs/
│   └── images/         # Screenshot & diagram
├── lib/                # Library eksternal (.jar)
└── README.md
```

> Sesuaikan struktur di atas dengan struktur proyek kalian yang sebenarnya.

---

##  Penerapan 4 Pilar OOP

### 1. Encapsulation (Enkapsulasi)

Penjelasan: (jelaskan bagaimana atribut dibuat `private` dan diakses lewat getter/setter)

**Lokasi:** `src/model/NamaClass.java`

```java
public class Anggota {
    private String nama;          // atribut private

    public String getNama() {     // getter
        return nama;
    }

    public void setNama(String nama) {  // setter
        this.nama = nama;
    }
}
```

### 2. Inheritance (Pewarisan)

Penjelasan: (jelaskan class mana yang menjadi parent dan child)

**Lokasi:** `src/model/NamaClass.java`

```java
public class Mahasiswa extends Person {
    // mewarisi atribut & method dari Person
}
```

### 3. Polymorphism (Polimorfisme)

Penjelasan: (jelaskan penerapan overriding/overloading)

**Lokasi:** `src/model/NamaClass.java`

```java
@Override
public void tampilkanInfo() {
    // implementasi berbeda dari parent
}
```

### 4. Abstraction (Abstraksi)

Penjelasan: (jelaskan penggunaan abstract class / interface)

**Lokasi:** `src/model/NamaClass.java`

```java
public abstract class Person {
    public abstract void tampilkanInfo();
}
```

---

## Cara Penggunaan Program

### Instalasi

1. Clone repository
   ```bash
   git clone https://github.com/username/nama-repository.git
   ```
2. Import database
   - Buka MySQL/phpMyAdmin
   - Import file `database/nama_database.sql`
3. Sesuaikan konfigurasi koneksi database di `src/util/DatabaseConnection.java`
4. Buka proyek di IDE, lalu jalankan `Main.java`

### Panduan Penggunaan (dengan Screenshot GUI)

**1. Halaman Login**

![Login](docs/images/login.png)

Penjelasan: (isi cara penggunaan halaman ini)

**2. Halaman Dashboard**

![Dashboard](docs/images/dashboard.png)

Penjelasan: (isi cara penggunaan halaman ini)

**3. Halaman Kelola Data**

![Kelola Data](docs/images/kelola-data.png)

Penjelasan: (isi cara penggunaan halaman ini)

**4. Halaman Transaksi / Laporan**

![Transaksi](docs/images/transaksi.png)

Penjelasan: (isi cara penggunaan halaman ini)

> Tambahkan atau kurangi bagian sesuai jumlah halaman GUI program kalian.


---

### Kontributor

| No | Nama Mahasiswa | NIM | Kelas |
| :---: | :--- | :---: | :---: |
| 1 | Ahmad Afif Al Ghifary | `2509116002` | A 2025 |
| 2 | Muhammad Ibrahim Kamil | `2509116012` | A 2025 |
| 3 | Muhammad Arham Anugrah | `2509116044` | B 2025 |
| 4 | Muhammad Rizky Alfa Reza Basyah | `2509116086` | C 2025 |

---
