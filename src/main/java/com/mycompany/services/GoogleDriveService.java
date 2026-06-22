package com.mycompany.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;
import com.mycompany.projectuas.Popup;

public class GoogleDriveService {

    // =========================================================================
    // Path Helper
    // =========================================================================

    private static File getDbFile() {
        String appData = System.getenv("APPDATA");
        return (appData != null && !appData.isEmpty())
                ? new File(appData + "\\ProjectUAS\\db\\db_enjoy_cafe.db")
                : new File(System.getProperty("user.home") + "/ProjectUAS/db/db_enjoy_cafe.db");
    }

    private static File getTokenFile() {
        String appData = System.getenv("APPDATA");
        return (appData != null && !appData.isEmpty())
                ? new File(appData + "\\ProjectUAS\\tokens\\StoredCredential")
                : new File(System.getProperty("user.home") + "/ProjectUAS/tokens/StoredCredential");
    }

    private static File getImageFolder() {
        String appData = System.getenv("APPDATA");
        return (appData != null && !appData.isEmpty())
                ? new File(appData + "\\ProjectUAS\\image-barang")
                : new File(System.getProperty("user.home") + "/ProjectUAS/image-barang");
    }

    private static File getImageZipFile() {
        String appData = System.getenv("APPDATA");
        return (appData != null && !appData.isEmpty())
                ? new File(appData + "\\ProjectUAS\\images-backup.zip")
                : new File(System.getProperty("user.home") + "/ProjectUAS/images-backup.zip");
    }

    // =========================================================================
    // Drive Helper
    // =========================================================================

    private Drive buildDrive() throws Exception {
        if (!getTokenFile().exists()) {
            throw new Exception("Belum login Google");
        }

        Credential credential = GoogleAuthService.loadCredential();
        if (credential == null)
            throw new Exception("Credential NULL");

        return new Drive.Builder(
                new ApacheHttpTransport(),
                GsonFactory.getDefaultInstance(),
                credential)
                .setApplicationName("Enjoy Cafe POS")
                .build();
    }

    private void hapusFileLama(Drive drive, String namaFile) throws Exception {
        FileList result = drive.files()
                .list()
                .setQ("name='" + namaFile + "' and trashed=false")
                .setFields("files(id,name)")
                .execute();

        for (com.google.api.services.drive.model.File file : result.getFiles()) {
            System.out.println("Menghapus file lama: " + file.getName() + " | ID = " + file.getId());
            drive.files().delete(file.getId()).execute();
        }
    }

    private void uploadFileToDrive(Drive drive, File file, String namaFile) throws Exception {
        try {
            com.google.api.services.drive.model.File metadata = new com.google.api.services.drive.model.File();
            metadata.setName(namaFile);

            FileContent mediaContent = new FileContent("application/octet-stream", file);

            com.google.api.services.drive.model.File uploaded = drive.files()
                    .create(metadata, mediaContent)
                    .setFields("id,name")
                    .execute();

            System.out.println("Upload berhasil | ID = " + uploaded.getId() + " | Nama = " + uploaded.getName());

        } catch (com.google.api.client.googleapis.json.GoogleJsonResponseException e) {
            int statusCode = e.getStatusCode();
            if (statusCode == 403) {
                throw new Exception(
                        "IZIN_DITOLAK: Akses Google Drive ditolak. Silakan login ulang dan berikan izin akses Drive.");
            } else if (statusCode == 401) {
                throw new Exception("TOKEN_EXPIRED: Sesi Google telah kedaluwarsa. Silakan login ulang.");
            } else {
                throw new Exception("Google API Error " + statusCode + ": " + e.getMessage());
            }
        } catch (java.net.UnknownHostException e) {
            throw new Exception("TIDAK_ADA_INTERNET: Tidak ada koneksi internet.");
        }
    }

    private String getFileIdFromDrive(Drive drive, String namaFile) throws Exception {
        FileList result = drive.files()
                .list()
                .setQ("name='" + namaFile + "' and trashed=false")
                .setFields("files(id,name)")
                .execute();

        if (result.getFiles().isEmpty())
            return null;
        return result.getFiles().get(0).getId();
    }

    // =========================================================================
    // ZIP Helper
    // =========================================================================

    private void zipFolder(File folder, File outputZip) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(outputZip);
                ZipOutputStream zos = new ZipOutputStream(fos)) {

            File[] files = folder.listFiles();
            if (files == null || files.length == 0) {
                System.out.println("Folder gambar kosong, tidak ada yang di-zip.");
                return;
            }

            for (File file : files) {
                if (file.isFile()) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        ZipEntry entry = new ZipEntry(file.getName());
                        zos.putNextEntry(entry);

                        byte[] buffer = new byte[4096];
                        int len;
                        while ((len = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }
                        zos.closeEntry();
                    }
                }
            }
        }
        System.out.println("Zip berhasil: " + outputZip.getAbsolutePath());
    }

    private void extractZip(File zipFile, File outputFolder) throws IOException {
        if (!outputFolder.exists())
            outputFolder.mkdirs();

        try (FileInputStream fis = new FileInputStream(zipFile);
                ZipInputStream zis = new ZipInputStream(fis)) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File outFile = new File(outputFolder, entry.getName());

                try (FileOutputStream fos = new FileOutputStream(outFile)) {
                    byte[] buffer = new byte[4096];
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                }
                zis.closeEntry();
                System.out.println("Extract: " + outFile.getName());
            }
        }
        System.out.println("Extract selesai ke: " + outputFolder.getAbsolutePath());
    }

    // =========================================================================
    // Upload Backup Database
    // =========================================================================

    public boolean uploadBackup() {
        try {
            if (!getTokenFile().exists()) {
                System.out.println("Token tidak ditemukan.");
                return false;
            }

            Drive drive = buildDrive();
            hapusFileLama(drive, "db_enjoy_cafe.db");

            File uploadFile = getDbFile();
            if (!uploadFile.exists()) {
                System.out.println("File database tidak ditemukan.");
                return false;
            }

            uploadFileToDrive(drive, uploadFile, "db_enjoy_cafe.db");
            return true;

        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg != null && msg.startsWith("IZIN_DITOLAK")) {
                new Popup().showConfirmPopup("AKSES DI TOLAK", "⚠ Izin Google Drive ditolak. Silakan login ulang dan centang izin akses Drive.", null);
            } else if (msg != null && msg.startsWith("TOKEN_EXPIRED")) {
                System.out.println("⚠ Sesi Google kedaluwarsa. Silakan login ulang.");
            } else if (msg != null && msg.startsWith("TIDAK_ADA_INTERNET")) {
                System.out.println("⚠ Tidak ada koneksi internet.");
            } else {
                System.out.println("Backup gagal: " + msg);
            }
            return false;
        }
    }

    // =========================================================================
    // Restore Backup Database
    // =========================================================================

    public boolean restoreBackup() {
        try {
            Drive drive = buildDrive();

            String fileId = getFileIdFromDrive(drive, "db_enjoy_cafe.db");
            if (fileId == null) {
                System.out.println("Backup database tidak ditemukan di Google Drive");
                return false;
            }

            System.out.println("File database ditemukan | ID = " + fileId);

            File localDb = getDbFile();
            if (!localDb.getParentFile().exists())
                localDb.getParentFile().mkdirs();

            try (FileOutputStream output = new FileOutputStream(localDb)) {
                drive.files().get(fileId).executeMediaAndDownloadTo(output);
            }

            System.out.println("Restore database berhasil ke: " + localDb.getAbsolutePath());
            return true;

        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg != null && msg.startsWith("IZIN_DITOLAK")) {
                new Popup().showConfirmPopup("AKSES DI TOLAK",
                        "⚠ Izin Google Drive ditolak. Silakan login ulang dan centang izin akses Drive.", null);
            } else if (msg != null && msg.startsWith("TOKEN_EXPIRED")) {
                System.out.println("⚠ Sesi Google kedaluwarsa. Silakan login ulang.");
            } else if (msg != null && msg.startsWith("TIDAK_ADA_INTERNET")) {
                System.out.println("⚠ Tidak ada koneksi internet.");
            } else {
                System.out.println("Restore gagal: " + msg);
            }
            return false;
        }
    }

    // =========================================================================
    // Upload Backup Gambar
    // =========================================================================

    public boolean uploadImageBackup() {
        try {
            if (!getTokenFile().exists()) {
                System.out.println("Token tidak ditemukan");
                return false;
            }

            File imageFolder = getImageFolder();
            File[] files = imageFolder.listFiles();
            if (!imageFolder.exists() || files == null || files.length == 0) {
                System.out.println("Folder gambar kosong, skip backup gambar.");
                return true; // bukan error, hanya skip
            }

            // Zip folder gambar
            File zipFile = getImageZipFile();
            zipFolder(imageFolder, zipFile);

            if (!zipFile.exists()) {
                System.out.println("Zip gambar gagal dibuat");
                return false;
            }

            Drive drive = buildDrive();
            hapusFileLama(drive, "images-backup.zip");
            uploadFileToDrive(drive, zipFile, "images-backup.zip");

            // Hapus file zip sementara
            zipFile.delete();

            return true;

        } catch (IOException e) {
            System.out.println("Tidak ada koneksi internet. Backup gambar dibatalkan.");
            return false;

        } catch (Exception e) {
            System.out.println("Backup gambar gagal: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // Restore Backup Gambar
    // =========================================================================

    public boolean restoreImageBackup() {
        try {
            Drive drive = buildDrive();

            String fileId = getFileIdFromDrive(drive, "images-backup.zip");
            if (fileId == null) {
                System.out.println("Backup gambar tidak ditemukan di Google Drive");
                return false;
            }

            System.out.println("File gambar ditemukan | ID = " + fileId);

            // Download zip ke AppData sementara
            File zipFile = getImageZipFile();
            try (FileOutputStream output = new FileOutputStream(zipFile)) {
                drive.files().get(fileId).executeMediaAndDownloadTo(output);
            }

            // Extract ke folder image-barang
            File imageFolder = getImageFolder();
            extractZip(zipFile, imageFolder);

            // Hapus file zip sementara
            zipFile.delete();

            System.out.println("Restore gambar berhasil ke: " + imageFolder.getAbsolutePath());
            return true;

        } catch (IOException e) {
            System.out.println("Tidak ada koneksi internet. Restore gambar dibatalkan.");
            return false;

        } catch (Exception e) {
            System.out.println("Restore gambar gagal: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // Backup Semua (DB + Gambar)
    // =========================================================================

    public boolean uploadBackupAll() {
        System.out.println("=== Memulai backup semua data ===");

        boolean dbOk = uploadBackup();
        System.out.println("Backup DB: " + (dbOk ? "Berhasil" : "Gagal"));

        boolean imgOk = uploadImageBackup();
        System.out.println("Backup Gambar: " + (imgOk ? "Berhasil" : "Gagal"));

        return dbOk && imgOk;
    }

    // =========================================================================
    // Restore Semua (DB + Gambar)
    // =========================================================================

    public boolean restoreBackupAll() {
        System.out.println("=== Memulai restore semua data ===");

        boolean dbOk = restoreBackup();
        System.out.println("Restore DB: " + (dbOk ? "Berhasil" : "Gagal"));

        boolean imgOk = restoreImageBackup();
        System.out.println("Restore Gambar: " + (imgOk ? "Berhasil" : "Gagal"));

        return dbOk && imgOk;
        

    }

    // =========================================================================
    // Cek apakah backup sudah kadaluarsa
    // =========================================================================

    public boolean isBackupExpired(long intervalJam) {
        try {
            Drive drive = buildDrive();

            FileList result = drive.files()
                    .list()
                    .setQ("name='db_enjoy_cafe.db' and trashed=false")
                    .setFields("files(id,name,modifiedTime)")
                    .execute();

            if (result.getFiles().isEmpty()) {
                System.out.println("Backup tidak ditemukan.");
                return true;
            }

            com.google.api.services.drive.model.File file = result.getFiles().get(0);
            long terakhirBackup = file.getModifiedTime().getValue();
            long sekarang = System.currentTimeMillis();
            long selisihJam = (sekarang - terakhirBackup) / (1000 * 60 * 60);

            System.out.println("Backup terakhir = " + selisihJam + " jam lalu");
            return selisihJam >= intervalJam;

        } catch (IOException e) {
            System.out.println("Tidak ada koneksi internet. File Backup Tidak Dapat Di Baca.");
            return false;

        } catch (Exception e) {
            System.out.println("Membaca File Backup Terkahir Gagal: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // Mendapatkan waktu terakhir backup
    // =========================================================================

    public String getLastBackupTime() {
        try {
            // Cek token dulu sebelum request ke internet
            if (!getTokenFile().exists()) {
                return "Belum login Google";
            }

            Drive drive = buildDrive();

            FileList result = drive.files()
                    .list()
                    .setQ("name='db_enjoy_cafe.db' and trashed=false")
                    .setFields("files(modifiedTime)")
                    .execute();

            if (result.getFiles().isEmpty()) {
                return "Belum pernah backup";
            }

            long waktuBackup = result.getFiles().get(0).getModifiedTime().getValue();

            java.time.LocalDateTime tanggal = java.time.Instant.ofEpochMilli(waktuBackup)
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDateTime();

            return tanggal.format(
                    java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        } catch (java.net.UnknownHostException e) {
            System.out.println("Tidak ada koneksi internet.");
            return "Tidak ada internet";
        } catch (Exception e) {
            return "Belum login Google";
        }
    }
}