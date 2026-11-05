-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Oct 28, 2026 at 02:00 PM
-- Server version: 8.0.30
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `axis`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id_pengguna` varchar(15) NOT NULL,
  `kode_karyawan` varchar(50) NOT NULL,
  `level_akses` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id_pengguna`, `kode_karyawan`, `level_akses`) VALUES
('ADM-001', 'AXISEMPL_01', 'Superadmin'),
('ADM-002', 'AXISEMPL_02', 'Validator');

-- --------------------------------------------------------

--
-- Table structure for table `jadwal_antarkota`
--

CREATE TABLE `jadwal_antarkota` (
  `id_jadwal` int NOT NULL,
  `waktu_berangkat` datetime NOT NULL,
  `waktu_tiba` datetime NOT NULL,
  `harga_dasar` decimal(10,2) NOT NULL,
  `nama_kereta` varchar(50) NOT NULL,
  `total_gerbong` int NOT NULL,
  `kapasitas_kursi_per_gerbong` int NOT NULL,
  `stasiun_id_asal` int NOT NULL,
  `stasiun_id_tujuan` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `jadwal_antarkota`
--

INSERT INTO `jadwal_antarkota` (`id_jadwal`, `waktu_berangkat`, `waktu_tiba`, `harga_dasar`, `nama_kereta`, `total_gerbong`, `kapasitas_kursi_per_gerbong`, `stasiun_id_asal`, `stasiun_id_tujuan`) VALUES
(101, '2026-11-01 08:00:00', '2026-11-01 11:00:00', '150000.00', 'Argo Parahyangan', 8, 50, 1, 3),
(102, '2026-11-01 09:30:00', '2026-11-01 17:00:00', '350000.00', 'Argo Bromo Anggrek', 10, 50, 1, 5),
(103, '2026-11-01 10:15:00', '2026-11-01 18:30:00', '250000.00', 'Taksaka', 8, 50, 1, 4),
(104, '2026-11-01 14:00:00', '2026-11-01 17:15:00', '130000.00', 'Argo Cheribon', 6, 60, 1, 9),
(105, '2026-11-01 20:00:00', '2026-11-02 05:30:00', '200000.00', 'Matarmaja', 10, 80, 2, 8),
(106, '2026-11-02 07:00:00', '2026-11-02 10:05:00', '150000.00', 'Argo Parahyangan', 8, 50, 3, 1),
(107, '2026-11-02 08:30:00', '2026-11-02 14:45:00', '220000.00', 'Lodaya', 8, 60, 3, 7),
(108, '2026-11-02 09:00:00', '2026-11-02 13:30:00', '180000.00', 'Malabar', 6, 60, 3, 10),
(109, '2026-11-02 16:00:00', '2026-11-02 23:45:00', '280000.00', 'Argo Sindoro', 8, 50, 1, 6),
(110, '2026-11-03 06:15:00', '2026-11-03 14:20:00', '240000.00', 'Fajar Utama YK', 8, 60, 2, 4),
(111, '2026-11-03 07:30:00', '2026-11-03 11:15:00', '140000.00', 'Kamandaka', 6, 80, 6, 10),
(112, '2026-11-03 10:00:00', '2026-11-03 14:00:00', '170000.00', 'Sancaka', 8, 60, 4, 5),
(113, '2026-11-03 15:45:00', '2026-11-03 17:30:00', '80000.00', 'Joglosemarkerto', 6, 80, 7, 4),
(114, '2026-11-04 08:10:00', '2026-11-04 10:20:00', '90000.00', 'Jayabaya', 8, 80, 5, 8),
(115, '2026-11-04 19:30:00', '2026-11-05 04:15:00', '260000.00', 'Gumarang', 10, 60, 2, 5);

-- --------------------------------------------------------

--
-- Table structure for table `pemesanan`
--

CREATE TABLE `pemesanan` (
  `id_pemesanan` int NOT NULL,
  `kode_booking` varchar(30) NOT NULL,
  `waktu_pesan` datetime NOT NULL,
  `total_bayar` decimal(10,2) NOT NULL,
  `penumpang_nik` varchar(50) NOT NULL,
  `status_pembayaran` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `pemesanan`
--

INSERT INTO `pemesanan` (`id_pemesanan`, `kode_booking`, `waktu_pesan`, `total_bayar`, `penumpang_nik`, `status_pembayaran`) VALUES
(5001, 'AXS-A1B2C', '2026-10-25 10:00:00', '150000.00', '317120485930', 'Lunas'),
(5002, 'AXS-D3E4F', '2026-10-25 11:30:00', '350000.00', '317183920152', 'Lunas'),
(5003, 'AXS-G5H6I', '2026-10-26 09:15:00', '500000.00', '317157483921', 'Lunas'),
(5004, 'AXS-J7K8L', '2026-10-26 14:20:00', '260000.00', '317182930485', 'Lunas'),
(5005, 'AXS-M9N0O', '2026-10-27 08:45:00', '200000.00', '317105948372', 'Lunas'),
(5006, 'AXS-P1Q2R', '2026-10-27 16:10:00', '150000.00', '317149586732', 'Lunas'),
(5007, 'AXS-S3T4U', '2026-10-28 07:30:00', '440000.00', '317158493027', 'Lunas'),
(5008, 'AXS-V5W6X', '2026-10-28 10:05:00', '180000.00', '317102938465', 'Lunas'),
(5009, 'AXS-Y7Z8A', '2026-10-29 13:50:00', '560000.00', '317120485930', 'Lunas'),
(5010, 'AXS-B9C0D', '2026-10-29 19:20:00', '240000.00', '317183920152', 'Lunas'),
(5011, 'AXS-E1F2G', '2026-10-30 06:40:00', '140000.00', '317157483921', 'Lunas'),
(5012, 'AXS-H3I4J', '2026-10-30 11:15:00', '340000.00', '317105948372', 'Lunas'),
(5013, 'AXS-K5L6M', '2026-10-31 08:00:00', '80000.00', '317149586732', 'Lunas'),
(5014, 'AXS-N7O8P', '2026-10-31 15:30:00', '180000.00', '317158493027', 'Lunas'),
(5015, 'AXS-Q9R0S', '2026-11-01 09:45:00', '260000.00', '317102938465', 'Lunas'),
(5017, 'AXS-T5U6V', '2026-11-02 10:05:00', '220000.00', '317159283741', 'Lunas');

-- --------------------------------------------------------

--
-- Table structure for table `pengguna`
--

CREATE TABLE `pengguna` (
  `id_pengguna` varchar(15) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(50) NOT NULL,
  `tipe_pengguna` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `pengguna`
--

INSERT INTO `pengguna` (`id_pengguna`, `email`, `password`, `tipe_pengguna`) VALUES
('ADM-001', 'admin.budi@gmail.com', 'admin123', 'A'),
('ADM-002', 'admin.siti@gmail.com', 'admin123', 'A'),
('CUST-001', 'andi.wijaya@gmail.com', 'pass123', 'P'),
('CUST-002', 'rina.melati@gmail.com', 'pass123', 'P'),
('CUST-003', 'joko.susilo@gmail.com', 'pass123', 'P'),
('CUST-004', 'maya.sari@gmail.com', 'pass123', 'P'),
('CUST-005', 'doni.pratama@gmail.com', 'pass123', 'P'),
('CUST-006', 'nina.safitri@gmail.com', 'pass123', 'P'),
('CUST-007', 'bima.putra@gmail.com', 'pass123', 'P'),
('CUST-008', 'lina.kurnia@gmail.com', 'pass123', 'P'),
('CUST-009', 'agus.santoso@gmail.com', 'pass123', 'P'),
('CUST-010', 'desi.ratna@gmail.com', 'pass123', 'P'),
('CUST-011', 'fajar.nugroho@gmail.com', 'pass123', 'P'),
('CUST-012', 'dian.pertiwi@gmail.com', 'pass123', 'P'),
('CUST-013', 'eka.saputra@gmail.com', 'pass123', 'P'),
('CUST-014', 'fitri.ani@gmail.com', 'pass123', 'P'),
('CUST-015', 'galih.prambudi@gmail.com', 'pass123', 'P');

-- --------------------------------------------------------

--
-- Table structure for table `penumpang`
--

CREATE TABLE `penumpang` (
  `id_pengguna` varchar(15) NOT NULL,
  `nik_nomor_passport` varchar(50) NOT NULL,
  `nama_lengkap` varchar(100) NOT NULL,
  `nomor_telepon` varchar(20) NOT NULL,
  `saldo_ewallet` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `penumpang`
--

INSERT INTO `penumpang` (`id_pengguna`, `nik_nomor_passport`, `nama_lengkap`, `nomor_telepon`, `saldo_ewallet`) VALUES
('CUST-001', '317120485930', 'Andi Wijaya', '081283746501', '200000.00'),
('CUST-002', '317159283741', 'Rina Melati', '085619283742', '50000.00'),
('CUST-003', '317183920152', 'Joko Susilo', '081384759203', '200000.00'),
('CUST-004', '317102938475', 'Maya Sari', '085739281044', '75000.00'),
('CUST-005', '317193847566', 'Doni Pratama', '081294857615', '300000.00'),
('CUST-006', '317148576920', 'Nina Safitri', '081347586926', '120000.00'),
('CUST-007', '317157483921', 'Bima Putra', '085648392017', '45000.00'),
('CUST-008', '317182930485', 'Lina Kurnia', '081273849508', '250000.00'),
('CUST-009', '317139485762', 'Agus Santoso', '085728394059', '80000.00'),
('CUST-010', '317105948372', 'Desi Ratna', '081394857260', '180000.00'),
('CUST-011', '317128394051', 'Fajar Nugroho', '085638475921', '35000.00'),
('CUST-012', '317149586732', 'Dian Pertiwi', '081258493022', '400000.00'),
('CUST-013', '317193840596', 'Eka Saputra', '081369584733', '95000.00'),
('CUST-014', '317158493027', 'Fitri Ani', '085748392044', '110000.00'),
('CUST-015', '317102938465', 'Galih Prambudi', '081293847555', '60000.00');

-- --------------------------------------------------------

--
-- Table structure for table `perjalanan_komuter`
--

CREATE TABLE `perjalanan_komuter` (
  `id_perjalanan` int NOT NULL,
  `waktu_tap_in` datetime NOT NULL,
  `waktu_tap_out` datetime DEFAULT NULL,
  `tarif_potongan` decimal(10,2) DEFAULT NULL,
  `status_perjalanan` varchar(30) NOT NULL,
  `penumpang_nik` varchar(50) NOT NULL,
  `stasiun_id_asal` int NOT NULL,
  `stasiun_id_tujuan` int DEFAULT NULL,
  `admin_kode_karyawan` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `perjalanan_komuter`
--

INSERT INTO `perjalanan_komuter` (`id_perjalanan`, `waktu_tap_in`, `waktu_tap_out`, `tarif_potongan`, `status_perjalanan`, `penumpang_nik`, `stasiun_id_asal`, `stasiun_id_tujuan`, `admin_kode_karyawan`) VALUES
(9001, '2026-10-27 07:00:00', '2026-10-27 07:45:00', '5000.00', 'Perlu Audit', '317159283741', 12, 11, 'AXISEMPL_01'),
(9002, '2026-10-27 07:15:00', '2026-10-27 08:00:00', '3000.00', 'Perlu Audit', '317102938475', 13, 11, 'AXISEMPL_02'),
(9003, '2026-10-27 08:30:00', '2026-10-27 09:10:00', '6000.00', 'Selesai', '317157483921', 12, 15, 'AXISEMPL_01'),
(9004, '2026-10-27 09:00:00', '2026-10-27 09:30:00', '4000.00', 'Selesai', '317139485762', 11, 15, 'AXISEMPL_02'),
(9005, '2026-10-27 10:15:00', '2026-10-27 11:00:00', '5000.00', 'Selesai', '317128394051', 14, 13, 'AXISEMPL_01'),
(9006, '2026-10-27 12:00:00', '2026-10-27 12:25:00', '3000.00', 'Selesai', '317193840596', 11, 14, 'AXISEMPL_02'),
(9007, '2026-10-27 13:30:00', '2026-10-27 14:15:00', '4000.00', 'Selesai', '317102938465', 14, 2, 'AXISEMPL_01'),
(9008, '2026-10-27 15:00:00', '2026-10-27 15:50:00', '6000.00', 'Selesai', '317159283741', 15, 12, 'AXISEMPL_02'),
(9009, '2026-10-27 16:10:00', '2026-10-27 16:45:00', '3000.00', 'Selesai', '317102938475', 15, 11, 'AXISEMPL_01'),
(9010, '2026-10-27 17:05:00', '2026-10-27 17:55:00', '5000.00', 'Selesai', '317157483921', 11, 12, 'AXISEMPL_02'),
(9011, '2026-10-27 18:00:00', '2026-10-27 18:30:00', '3000.00', 'Selesai', '317139485762', 2, 11, 'AXISEMPL_01'),
(9012, '2026-10-27 19:15:00', '2026-10-27 19:40:00', '4000.00', 'Selesai', '317128394051', 11, 13, 'AXISEMPL_02'),
(9013, '2026-10-27 20:00:00', '2026-10-27 20:45:00', NULL, 'Selesai', '317193840596', 12, NULL, NULL),
(9014, '2026-10-27 20:15:00', '2026-10-27 21:00:00', NULL, 'Selesai', '317102938465', 13, NULL, NULL),
(9015, '2026-10-27 20:30:00', NULL, NULL, 'Berjalan', '317159283741', 11, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `riwayat_topup`
--

CREATE TABLE `riwayat_topup` (
  `id_topup` int NOT NULL,
  `nominal_topup` decimal(10,2) NOT NULL,
  `waktu_topup` datetime NOT NULL,
  `metode_pembayaran` varchar(50) NOT NULL,
  `status_topup` varchar(20) NOT NULL,
  `penumpang_nik` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `riwayat_topup`
--

INSERT INTO `riwayat_topup` (`id_topup`, `nominal_topup`, `waktu_topup`, `metode_pembayaran`, `status_topup`, `penumpang_nik`) VALUES
(8001, '50000.00', '2026-10-20 08:15:00', 'Transfer Bank', 'Berhasil', '317120485930'),
(8002, '100000.00', '2026-10-20 10:30:00', 'Loket Stasiun', 'Berhasil', '317159283741'),
(8003, '250000.00', '2026-10-21 09:00:00', 'Mobile Banking', 'Berhasil', '317183920152'),
(8004, '75000.00', '2026-10-21 14:45:00', 'Kartu Kredit', 'Berhasil', '317102938475'),
(8005, '300000.00', '2026-10-22 11:20:00', 'Transfer Bank', 'Berhasil', '317193847566'),
(8006, '150000.00', '2026-10-22 16:10:00', 'Loket Stasiun', 'Berhasil', '317148576920'),
(8007, '50000.00', '2026-10-23 07:30:00', 'Mobile Banking', 'Berhasil', '317157483921'),
(8008, '200000.00', '2026-10-23 13:15:00', 'ATM', 'Berhasil', '317182930485'),
(8009, '100000.00', '2026-10-24 09:40:00', 'Transfer Bank', 'Berhasil', '317139485762'),
(8010, '180000.00', '2026-10-24 18:00:00', 'Mobile Banking', 'Berhasil', '317105948372'),
(8011, '50000.00', '2026-10-25 08:50:00', 'Loket Stasiun', 'Berhasil', '317128394051'),
(8012, '400000.00', '2026-10-25 15:25:00', 'Mobile Banking', 'Berhasil', '317149586732'),
(8013, '150000.00', '2026-10-26 10:10:00', 'ATM', 'Berhasil', '317193840596'),
(8014, '200000.00', '2026-10-26 14:40:00', 'Transfer Bank', 'Berhasil', '317158493027'),
(8015, '80000.00', '2026-10-27 06:30:00', 'Loket Stasiun', 'Berhasil', '317102938465');

-- --------------------------------------------------------

--
-- Table structure for table `stasiun`
--

CREATE TABLE `stasiun` (
  `id_stasiun` int NOT NULL,
  `nama_stasiun` varchar(50) NOT NULL,
  `kode_kota` varchar(10) NOT NULL,
  `kategori_layanan` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `stasiun`
--

INSERT INTO `stasiun` (`id_stasiun`, `nama_stasiun`, `kode_kota`, `kategori_layanan`) VALUES
(1, 'Stasiun Gambir', 'JKT', 'Antarkota'),
(2, 'Stasiun Pasar Senen', 'JKT', 'Keduanya'),
(3, 'Stasiun Bandung', 'BDO', 'Keduanya'),
(4, 'Stasiun Tugu Yogyakarta', 'YK', 'Keduanya'),
(5, 'Stasiun Surabaya Gubeng', 'SGU', 'Keduanya'),
(6, 'Stasiun Semarang Tawang', 'SMT', 'Antarkota'),
(7, 'Stasiun Solo Balapan', 'SLO', 'Keduanya'),
(8, 'Stasiun Malang', 'ML', 'Keduanya'),
(9, 'Stasiun Cirebon', 'CN', 'Keduanya'),
(10, 'Stasiun Purwokerto', 'PWT', 'Antarkota'),
(11, 'Stasiun Manggarai', 'JKT', 'Keduanya'),
(12, 'Stasiun Bogor', 'BGR', 'Komuter'),
(13, 'Stasiun Depok Baru', 'DPK', 'Komuter'),
(14, 'Stasiun Tanah Abang', 'JKT', 'Komuter'),
(15, 'Stasiun Jakarta Kota', 'JKT', 'Komuter'),
(16, 'Stasiun Bekasi', 'BKS', 'Komuter');

-- --------------------------------------------------------

--
-- Table structure for table `tarif_komuter`
--

CREATE TABLE `tarif_komuter` (
  `id_tarif` varchar(50) NOT NULL,
  `harga_rute` decimal(10,2) NOT NULL,
  `stasiun_id_asal` int NOT NULL,
  `stasiun_id_tujuan` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `tarif_komuter`
--

INSERT INTO `tarif_komuter` (`id_tarif`, `harga_rute`, `stasiun_id_asal`, `stasiun_id_tujuan`) VALUES
('TRF-001', '3000.00', 12, 13),
('TRF-002', '5000.00', 12, 11),
('TRF-003', '6000.00', 12, 15),
('TRF-004', '4000.00', 13, 11),
('TRF-005', '5000.00', 13, 14),
('TRF-006', '3000.00', 11, 14),
('TRF-007', '4000.00', 11, 15),
('TRF-008', '3000.00', 14, 11),
('TRF-009', '4000.00', 14, 2),
('TRF-010', '3000.00', 15, 11),
('TRF-011', '3000.00', 2, 11),
('TRF-012', '6000.00', 15, 12),
('TRF-013', '5000.00', 11, 12),
('TRF-014', '4000.00', 11, 13),
('TRF-015', '5000.00', 14, 13),
('TRF-016', '4000.00', 16, 11),
('TRF-017', '4000.00', 11, 16);

-- --------------------------------------------------------

--
-- Table structure for table `tiket`
--

CREATE TABLE `tiket` (
  `id_tiket` int NOT NULL,
  `id_unik_barcode` varchar(50) NOT NULL,
  `nama_penumpang_tiket` varchar(100) NOT NULL,
  `no_identitas` varchar(30) NOT NULL,
  `nomor_kursi` varchar(10) NOT NULL,
  `nomor_gerbong` varchar(10) NOT NULL,
  `pemesanan_kode_booking` varchar(30) NOT NULL,
  `jadwal_antarkota_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `tiket`
--

INSERT INTO `tiket` (`id_tiket`, `id_unik_barcode`, `nama_penumpang_tiket`, `no_identitas`, `nomor_kursi`, `nomor_gerbong`, `pemesanan_kode_booking`, `jadwal_antarkota_id`) VALUES
(7001, 'QR-12345ABC', 'Andi Wijaya', '317120485930', '12A', 'EKS-1', 'AXS-A1B2C', 101),
(7002, 'QR-23456BCD', 'Joko Susilo', '317183920152', '8C', 'EKS-2', 'AXS-D3E4F', 102),
(7003, 'QR-34567CDE', 'Doni Pratama', '317157483921', '4B', 'EKS-1', 'AXS-G5H6I', 103),
(7004, 'QR-45678DEF', 'Istri Doni', '317157483922', '4C', 'EKS-1', 'AXS-G5H6I', 103),
(7005, 'QR-56789EFG', 'Nina Safitri', '317182930485', '10D', 'EKS-3', 'AXS-J7K8L', 104),
(7006, 'QR-67890FGH', 'Suami Nina', '317182930486', '10E', 'EKS-3', 'AXS-J7K8L', 104),
(7007, 'QR-78901GHI', 'Lina Kurnia', '317105948372', '15A', 'EKO-1', 'AXS-M9N0O', 105),
(7008, 'QR-89012HIJ', 'Desi Ratna', '317149586732', '7B', 'EKS-2', 'AXS-P1Q2R', 106),
(7009, 'QR-90123IJK', 'Dian Pertiwi', '317158493027', '2A', 'BIS-1', 'AXS-S3T4U', 107),
(7010, 'QR-01234JKL', 'Anak Dian', '317158493028', '2B', 'BIS-1', 'AXS-S3T4U', 107),
(7011, 'QR-11223KLM', 'Fitri Ani', '317102938465', '9C', 'BIS-2', 'AXS-V5W6X', 108),
(7012, 'QR-22334LMN', 'Galih Prambudi', '317120485930', '5D', 'EKS-1', 'AXS-Y7Z8A', 109),
(7013, 'QR-33445MNO', 'Ibu Joko', '317183920153', '11A', 'BIS-3', 'AXS-B9C0D', 110),
(7014, 'QR-44556NOP', 'Agus Santoso', '317139485762', '14C', 'EKO-2', 'AXS-E1F2G', 111),
(7015, 'QR-55667OPQ', 'Eka Saputra', '317193840596', '6B', 'EKS-4', 'AXS-Q9R0S', 115);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id_pengguna`),
  ADD UNIQUE KEY `kode_karyawan` (`kode_karyawan`);

--
-- Indexes for table `jadwal_antarkota`
--
ALTER TABLE `jadwal_antarkota`
  ADD PRIMARY KEY (`id_jadwal`),
  ADD KEY `fk_jadwal_asal` (`stasiun_id_asal`),
  ADD KEY `fk_jadwal_tujuan` (`stasiun_id_tujuan`);

--
-- Indexes for table `pemesanan`
--
ALTER TABLE `pemesanan`
  ADD PRIMARY KEY (`id_pemesanan`),
  ADD UNIQUE KEY `kode_booking` (`kode_booking`),
  ADD KEY `fk_pemesanan_penumpang_nik` (`penumpang_nik`);

--
-- Indexes for table `pengguna`
--
ALTER TABLE `pengguna`
  ADD PRIMARY KEY (`id_pengguna`);

--
-- Indexes for table `penumpang`
--
ALTER TABLE `penumpang`
  ADD PRIMARY KEY (`id_pengguna`),
  ADD UNIQUE KEY `nik_nomor_passport` (`nik_nomor_passport`);

--
-- Indexes for table `perjalanan_komuter`
--
ALTER TABLE `perjalanan_komuter`
  ADD PRIMARY KEY (`id_perjalanan`),
  ADD KEY `fk_komuter_penumpang` (`penumpang_nik`),
  ADD KEY `fk_komuter_asal` (`stasiun_id_asal`),
  ADD KEY `fk_komuter_tujuan` (`stasiun_id_tujuan`),
  ADD KEY `perjalanan_komuter_admin_fk` (`admin_kode_karyawan`);

--
-- Indexes for table `riwayat_topup`
--
ALTER TABLE `riwayat_topup`
  ADD PRIMARY KEY (`id_topup`),
  ADD KEY `fk_topup_penumpang` (`penumpang_nik`);

--
-- Indexes for table `stasiun`
--
ALTER TABLE `stasiun`
  ADD PRIMARY KEY (`id_stasiun`);

--
-- Indexes for table `tarif_komuter`
--
ALTER TABLE `tarif_komuter`
  ADD PRIMARY KEY (`id_tarif`),
  ADD KEY `fk_tarif_asal` (`stasiun_id_asal`),
  ADD KEY `fk_tarif_tujuan` (`stasiun_id_tujuan`);

--
-- Indexes for table `tiket`
--
ALTER TABLE `tiket`
  ADD PRIMARY KEY (`id_tiket`),
  ADD UNIQUE KEY `id_unik_barcode` (`id_unik_barcode`),
  ADD KEY `fk_tiket_jadwal` (`jadwal_antarkota_id`),
  ADD KEY `fk_tiket_pemesanan` (`pemesanan_kode_booking`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `jadwal_antarkota`
--
ALTER TABLE `jadwal_antarkota`
  ADD CONSTRAINT `fk_jadwal_asal` FOREIGN KEY (`stasiun_id_asal`) REFERENCES `stasiun` (`id_stasiun`),
  ADD CONSTRAINT `fk_jadwal_tujuan` FOREIGN KEY (`stasiun_id_tujuan`) REFERENCES `stasiun` (`id_stasiun`);

--
-- Constraints for table `pemesanan`
--
ALTER TABLE `pemesanan`
  ADD CONSTRAINT `fk_pemesanan_penumpang_nik` FOREIGN KEY (`penumpang_nik`) REFERENCES `penumpang` (`nik_nomor_passport`);

--
-- Constraints for table `perjalanan_komuter`
--
ALTER TABLE `perjalanan_komuter`
  ADD CONSTRAINT `fk_komuter_asal` FOREIGN KEY (`stasiun_id_asal`) REFERENCES `stasiun` (`id_stasiun`),
  ADD CONSTRAINT `fk_komuter_penumpang` FOREIGN KEY (`penumpang_nik`) REFERENCES `penumpang` (`nik_nomor_passport`),
  ADD CONSTRAINT `fk_komuter_tujuan` FOREIGN KEY (`stasiun_id_tujuan`) REFERENCES `stasiun` (`id_stasiun`),
  ADD CONSTRAINT `perjalanan_komuter_admin_fk` FOREIGN KEY (`admin_kode_karyawan`) REFERENCES `admin` (`kode_karyawan`);

--
-- Constraints for table `riwayat_topup`
--
ALTER TABLE `riwayat_topup`
  ADD CONSTRAINT `fk_topup_penumpang` FOREIGN KEY (`penumpang_nik`) REFERENCES `penumpang` (`nik_nomor_passport`);

--
-- Constraints for table `tarif_komuter`
--
ALTER TABLE `tarif_komuter`
  ADD CONSTRAINT `fk_tarif_asal` FOREIGN KEY (`stasiun_id_asal`) REFERENCES `stasiun` (`id_stasiun`),
  ADD CONSTRAINT `fk_tarif_tujuan` FOREIGN KEY (`stasiun_id_tujuan`) REFERENCES `stasiun` (`id_stasiun`);

--
-- Constraints for table `tiket`
--
ALTER TABLE `tiket`
  ADD CONSTRAINT `fk_tiket_jadwal` FOREIGN KEY (`jadwal_antarkota_id`) REFERENCES `jadwal_antarkota` (`id_jadwal`),
  ADD CONSTRAINT `fk_tiket_pemesanan` FOREIGN KEY (`pemesanan_kode_booking`) REFERENCES `pemesanan` (`kode_booking`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
