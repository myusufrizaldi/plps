-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 24, 2019 at 01:43 AM
-- Server version: 10.1.37-MariaDB
-- PHP Version: 7.3.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `plps`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `nia` char(10) NOT NULL,
  `nama` varchar(64) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`nia`, `nama`, `password`) VALUES
('1234567890', 'leo', '62348957');

-- --------------------------------------------------------

--
-- Table structure for table `dosen`
--

CREATE TABLE `dosen` (
  `nip` char(32) NOT NULL,
  `nama` varchar(64) NOT NULL,
  `id_fokus` int(11) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dosen`
--

INSERT INTO `dosen` (`nip`, `nama`, `id_fokus`, `password`) VALUES
('197604292008122001', 'Masayu Leylia Khodra', 1, 'hmT623'),
('197604292008122002', 'Rahman Indra Kesuma', 2, '62348957');

-- --------------------------------------------------------

--
-- Table structure for table `fokus`
--

CREATE TABLE `fokus` (
  `id_fokus` int(11) NOT NULL,
  `nama_fokus` varchar(32) NOT NULL,
  `deskripsi` varchar(256) NOT NULL,
  `logic_point` double NOT NULL,
  `math_point` double NOT NULL,
  `memory_point` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `fokus`
--

INSERT INTO `fokus` (`id_fokus`, `nama_fokus`, `deskripsi`, `logic_point`, `math_point`, `memory_point`) VALUES
(1, 'Intelligent System Developer', 'Dengan berbagai teknik artificial intelligence yang dipelajarinya, seorang sarjana informatika juga dapat berperan sebagai pengembang perangkat lunak yang intelejen seperti sistem pakar, image recognizer, prediction system, data miner, dll.', 80, 85, 50),
(2, 'Game Developer', 'Dengan berbagai bekal keinformatikaan yang diperolehnya termasuk computer graphic, human computer interaction, dll, seorang sarjana informatika juga dapat berperan sebagai pengembang perangkat lunak untuk multimedia game.', 80, 90, 60),
(3, 'Software Tester', 'Terkait dengan ukuran perangkat lunak, sarjana informatika dapat juga berperan khusus sebagai penguji perangkat lunak yang bertanggung jawab atas kebenar-an fungsi dari sebuah perangkat lunak.', 80, 60, 85),
(4, 'Programmer', 'Baik sebagai system programmer atau application developer, sarjana informa­tika sangat dibutuhkan di berbagai bidang, misalnya bidang perbankan, teleko­munikasi, industri IT, media, instansi pemerintah, dan lain-lain.', 95, 75, 75),
(5, 'Computer Network Engineer', 'Bertugas merancang arsitektur jaringan, serta melakukan perawatan dan pen­gelolaan jaringan dalam suatu instansi atau perusahaan.', 70, 60, 95),
(6, 'Web Developer', 'Bertugas merancang dan membangun website beserta berbagai layanan dan fasilitas berjalan di atasnya. Ia juga bertugas melakukan pemeliharaan untuk website tersebut dan mengembangkannya.', 70, 70, 80),
(7, 'Database Administrator', 'Berperan dalam perancangan dan pemeliharaan basis data (termasuk data warehouse) untuk suatu instansi atau perusahaan', 90, 70, 90),
(8, 'System Analyst', 'Berperan dalam melakukan analisis terhadap sistem dalam suatu instansi atau perusahaan dan membuat solusi yang integratif dengan memanfaatkan perang­kat lunak.', 80, 80, 85),
(9, 'Software Engineer', 'Berperan dalam pengembangan perangkat lunak untuk berbagai keperluan. Misalnya perangkat lunak untuk pendidikan, telekomunikasi, bisnis, hiburan dan lain-lain, termasuk perangkat lunak untuk model dan simulasi.', 85, 75, 85);

-- --------------------------------------------------------

--
-- Table structure for table `mahasiswa`
--

CREATE TABLE `mahasiswa` (
  `nim` char(12) NOT NULL,
  `nama` varchar(64) NOT NULL,
  `semester_terakhir` int(2) NOT NULL,
  `password` varchar(255) NOT NULL,
  `logic_point` double NOT NULL,
  `math_point` double NOT NULL,
  `memory_point` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `mahasiswa`
--

INSERT INTO `mahasiswa` (`nim`, `nama`, `semester_terakhir`, `password`, `logic_point`, `math_point`, `memory_point`) VALUES
('14111111', 'Jambu', 0, 'FhFhummmm', 0, 0, 0),
('14115000', 'Thanos Sibutarbutar', 0, 'KeNLzv623', 0, 0, 0),
('14117053', 'hendri tri putra', 0, 'hChup326', 0, 0, 0),
('14117125', 'leo', 0, '62348957', 0, 0, 0),
('14117151', 'Mohamad Yusuf Rizaldi', 3, 'hmT623', 75.52083333333333, 52.604166666666664, 90.625),
('14117157', 'haidar', 0, '62348957', 0, 0, 0),
('14117170', 'Gusti Nugroho', 0, 'KeNLzv100', 0, 0, 0),
('14444111', 'Anya Geraldinho', 0, 'hmTQpGwAi', 0, 0, 0),
('aisdjasidja', 'Jamban', 0, 'xKhThmTm', 0, 0, 0),
('Gusti WEH', '14117170', 0, 'hmTQpGwAi', 0, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `mahasiswa_ambil_matkul`
--

CREATE TABLE `mahasiswa_ambil_matkul` (
  `id` int(11) NOT NULL,
  `nim` char(12) NOT NULL,
  `id_matkul` char(8) NOT NULL,
  `semester` int(2) NOT NULL,
  `nilai` char(2) NOT NULL,
  `logic_point` double NOT NULL,
  `math_point` double NOT NULL,
  `memory_point` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `matkul`
--

CREATE TABLE `matkul` (
  `id_matkul` char(8) NOT NULL,
  `nama` varchar(64) NOT NULL,
  `sks` int(11) NOT NULL,
  `semester` int(2) NOT NULL,
  `wajib` tinyint(1) NOT NULL,
  `prioritas` int(11) NOT NULL,
  `logic_point_rate` double NOT NULL,
  `math_point_rate` double NOT NULL,
  `memory_point_rate` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `matkul`
--

INSERT INTO `matkul` (`id_matkul`, `nama`, `sks`, `semester`, `wajib`, `prioritas`, `logic_point_rate`, `math_point_rate`, `memory_point_rate`) VALUES
('BI1103', 'Biologi Dasar 1B', 2, 1, 1, 8, 20, 20, 60),
('FI1101A', 'Fisika Dasar IB', 4, 1, 1, 6, 40, 30, 30),
('FI1201', 'Fisika Dasar II', 4, 2, 1, 15, 40, 30, 30),
('IF1101', 'Pengenalan Prodi Teknik Informatika', 2, 1, 1, 1, 30, 30, 40),
('IF1121', 'Algoritma Pemrograman 1', 3, 1, 1, 2, 60, 10, 30),
('IF1221', 'Algoritma Pemrograman 2', 2, 2, 1, 10, 60, 10, 30),
('IF2111', 'Matematika Diskrit 1', 2, 3, 1, 22, 40, 40, 20),
('IF2114', 'Matriks dan Ruang Vektor', 3, 3, 1, 23, 40, 30, 30),
('IF2121', 'Algoritma dan Struktur Data', 4, 3, 1, 19, 50, 5, 45),
('IF2122R', 'Probabilitas & Statistika', 3, 3, 1, 21, 50, 40, 10),
('IF2131', 'Organisasi & Arsitektur Komputer', 3, 3, 1, 20, 20, 30, 50),
('IF2211', 'Strategi Algoritma', 3, 4, 1, 26, 40, 40, 20),
('IF2212', 'Teori Bahasa Formal dan Otomata', 3, 4, 1, 28, 60, 20, 20),
('IF2221', 'Pemrograman Berorientasi Objek', 3, 4, 1, 24, 60, 5, 35),
('IF2231', 'Sistem Operasi', 3, 4, 1, 29, 30, 40, 30),
('IF2241', 'Dasar Rekayasa Perangkat Lunak', 2, 4, 1, 25, 30, 5, 65),
('IF2242', 'Basis Data', 3, 4, 1, 27, 40, 10, 50),
('IF3111', 'Intelegensi Buatan', 3, 5, 1, 37, 40, 30, 30),
('IF3121', 'Pengembangan Aplikasi Berbasis Web', 3, 5, 1, 34, 50, 20, 30),
('IF3122', 'Embedded System', 3, 5, 1, 38, 40, 30, 30),
('IF3131', 'Jaringan Komputer', 3, 5, 1, 36, 35, 35, 30),
('IF3141', 'Manajemen Proyek Perangkat Lunak', 3, 5, 1, 33, 40, 10, 40),
('IF3142', 'Manajemen Basis Data', 2, 5, 1, 35, 40, 10, 50),
('IF3143', 'Interaksi Manusia dan Komputer', 2, 5, 1, 32, 40, 15, 45),
('IF3201', 'Kewirausahaan', 2, 6, 1, 43, 35, 35, 30),
('IF3211', 'Grafika Komputer', 3, 6, 1, 41, 40, 40, 20),
('IF3221', 'Pengembangan Aplikasi Mobile', 3, 6, 1, 42, 50, 20, 30),
('IF3241', 'Sistem Informasi', 2, 6, 1, 40, 40, 20, 30),
('IF3242', 'Proyek Perangkat Lunak', 3, 6, 1, 39, 40, 20, 30),
('KI1102', 'Kimia Dasar IB', 2, 1, 1, 7, 40, 20, 40),
('KI1202', 'Kimia Dasar II', 2, 2, 1, 17, 40, 20, 40),
('KU1001', 'Tata Tulis Karya Ilmiah', 2, 2, 1, 14, 35, 35, 30),
('KU1101', 'Bahasa Indonesia', 2, 1, 1, 5, 30, 0, 70),
('KU1102', 'Pengantar Komputer & Software I', 2, 1, 1, 3, 30, 30, 40),
('KU1201', 'Bahasa Inggris', 2, 2, 1, 12, 30, 10, 60),
('KU1202', 'Pengantar Komputer dan Software II', 2, 2, 1, 9, 30, 30, 40),
('KU1203', 'Pengenalan Lingkungan dan Potensi Daerah', 2, 2, 1, 16, 30, 20, 50),
('KU1204', 'Penyusunan Laporan', 2, 2, 1, 13, 20, 10, 70),
('KU2006', 'Pendidikan Pancasila dan Kewarganegaraan', 2, 4, 1, 30, 40, 5, 55),
('KU2007', 'Stadium General', 2, 4, 1, 31, 35, 30, 35),
('KU2021', 'Agama dan Etika Islam', 2, 3, 1, 18, 40, 10, 50),
('KU3001', 'Kuliah Kerja Nyata', 2, 6, 1, 44, 35, 35, 30),
('MA1101A', 'Matematika IB', 4, 1, 1, 4, 40, 40, 20),
('MA1201', 'Matematika II', 4, 2, 1, 11, 40, 40, 20);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`nia`);

--
-- Indexes for table `dosen`
--
ALTER TABLE `dosen`
  ADD PRIMARY KEY (`nip`);

--
-- Indexes for table `fokus`
--
ALTER TABLE `fokus`
  ADD PRIMARY KEY (`id_fokus`);

--
-- Indexes for table `mahasiswa`
--
ALTER TABLE `mahasiswa`
  ADD PRIMARY KEY (`nim`);

--
-- Indexes for table `mahasiswa_ambil_matkul`
--
ALTER TABLE `mahasiswa_ambil_matkul`
  ADD PRIMARY KEY (`id`),
  ADD KEY `nim` (`nim`),
  ADD KEY `id_matkul` (`id_matkul`);

--
-- Indexes for table `matkul`
--
ALTER TABLE `matkul`
  ADD PRIMARY KEY (`id_matkul`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `fokus`
--
ALTER TABLE `fokus`
  MODIFY `id_fokus` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `mahasiswa_ambil_matkul`
--
ALTER TABLE `mahasiswa_ambil_matkul`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `mahasiswa_ambil_matkul`
--
ALTER TABLE `mahasiswa_ambil_matkul`
  ADD CONSTRAINT `id_matkul` FOREIGN KEY (`id_matkul`) REFERENCES `matkul` (`id_matkul`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `nim` FOREIGN KEY (`nim`) REFERENCES `mahasiswa` (`nim`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
