-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: May 15, 2019 at 03:01 PM
-- Server version: 10.1.19-MariaDB
-- PHP Version: 7.0.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `input nilai`
--

-- --------------------------------------------------------

--
-- Table structure for table `build`
--

CREATE TABLE `build` (
  `id_build` int(11) NOT NULL,
  `id_fokus` int(11) NOT NULL,
  `nip` char(32) NOT NULL,
  `nama` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
('197604292008122001', 'Masayu Leylia Khodra', 1, 'FhmhJD');

-- --------------------------------------------------------

--
-- Table structure for table `fokus`
--

CREATE TABLE `fokus` (
  `id_fokus` int(11) NOT NULL,
  `nama_fokus` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `fokus`
--

INSERT INTO `fokus` (`id_fokus`, `nama_fokus`) VALUES
(1, 'Artificial Intelligence');

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
('14117151', 'Mohamad Yusuf Rizaldi', 3, 'hmT623', 0, 0, 0),
('aisdjasidja', 'Jamban', 0, 'xKhThmTm', 0, 0, 0),
('Gusti WEH', '14117170', 0, 'hmTQpGwAi', 0, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `mata kuliah`
--

CREATE TABLE `mata kuliah` (
  `id_matkul` varchar(8) NOT NULL,
  `matkul` varchar(20) DEFAULT NULL,
  `sks` int(11) DEFAULT NULL,
  `semester` char(5) DEFAULT NULL,
  `nilai` varchar(5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `matkul`
--

CREATE TABLE `matkul` (
  `id_matkul` char(8) NOT NULL,
  `mk` varchar(64) NOT NULL,
  `sks` int(5) DEFAULT NULL,
  `semester` int(2) NOT NULL,
  `wajib` tinyint(1) NOT NULL,
  `prioritas` int(11) NOT NULL,
  `RateLogicPoint` double DEFAULT NULL,
  `MathPoint` double NOT NULL,
  `MemoPoint` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `matkul`
--

INSERT INTO `matkul` (`id_matkul`, `mk`, `sks`, `semester`, `wajib`, `prioritas`, `RateLogicPoint`, `MathPoint`, `MemoPoint`) VALUES
('MA1101A', 'Matematika IB', 4, 1, 1, 1, 0.15, 0, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `build`
--
ALTER TABLE `build`
  ADD PRIMARY KEY (`id_build`),
  ADD KEY `id_fokus_2` (`id_fokus`),
  ADD KEY `dosen_pembuat_build` (`nip`);

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
-- Indexes for table `mata kuliah`
--
ALTER TABLE `mata kuliah`
  ADD PRIMARY KEY (`id_matkul`);

--
-- Indexes for table `matkul`
--
ALTER TABLE `matkul`
  ADD PRIMARY KEY (`id_matkul`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `build`
--
ALTER TABLE `build`
  MODIFY `id_build` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `fokus`
--
ALTER TABLE `fokus`
  MODIFY `id_fokus` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `build`
--
ALTER TABLE `build`
  ADD CONSTRAINT `dosen_pembuat_build` FOREIGN KEY (`nip`) REFERENCES `dosen` (`nip`),
  ADD CONSTRAINT `id_fokus_2` FOREIGN KEY (`id_fokus`) REFERENCES `fokus` (`id_fokus`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
