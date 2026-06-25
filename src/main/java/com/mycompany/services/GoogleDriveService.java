package com.mycompany.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;

public class GoogleDriveService {

    // =========================================================================
    // Path Helper
    // =========================================================================

    private static File getDbFile() {
        String appData = System.getenv("APPDATA");
        return (appData != null && !appData.isEmpty())
                ? new File(appData + "\\EnjoyCafe\\db\\db_enjoy_cafe.db")
                : new File(System.getProperty("user.home") + "/EnjoyCafe/db/db_enjoy_cafe.db");
    }

    private static File getTokenFile() {
        String appData = System.getenv("APPDATA");
        return (appData != null && !appData.isEmpty())
                ? new File(appData + "\\EnjoyCafe\\tokens\\StoredCredential")
                : new File(System.getProperty("user.home") + "/EnjoyCafe/tokens/StoredCredential");
    }

    private static File getImageFolder() {
        String appData = System.getenv("APPDATA");
        return (appData != null && !appData.isEmpty())
                ? new File(appData + "\\EnjoyCafe\\image-barang")
                : new File(System.getProperty("user.home") + "/EnjoyCafe/image-barang");
    }

    private static File getImageZipFile() {
        String appData = System.getenv("APPDATA");
        return (appData != null && !appData.isEmpty())
                ? new File(appData + "\\EnjoyCafe\\images-backup.zip")
                : new File(System.getProperty("user.home") + "/EnjoyCafe/images-backup.zip");
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
        com.google.api.services.drive.model.File metadata = new com.google.api.services.drive.model.File();
        metadata.setName(namaFile);

        FileContent mediaContent = new FileContent("application/octet-stream", file);

        com.google.api.services.drive.model.File uploaded = drive.files()
                .create(metadata, mediaContent)
                .setFields("id,name")
                .execute();

        System.out.println("Upload berhasil | ID = " + uploaded.getId() + " | Nama = " + uploaded.getName());
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
    // Error Helper — parse kode error dari exception
    // =========================================================================

    private String parseErrorCode(Exception e) {
        if (e instanceof GoogleJsonResponseException) {
            int code = ((GoogleJsonResponseException) e).getStatusCode();
            if (code == 403)
                return "IZIN_DITOLAK";
            if (code == 401)
                return "TOKEN_EXPIRED";
            return "GOOGLE_ERROR_" + code;
        }
        if (e instanceof java.net.UnknownHostException)
            return "TIDAK_ADA_INTERNET";
        if (e instanceof IOException)
            return "TIDAK_ADA_INTERNET";
        if (e.getMessage() != null && e.getMessage().equals("Belum login Google"))
            return "BELUM_LOGIN";
        return null;
    }

    private void tanganiError(Exception e, Consumer<String> onError, String konteks) {
        String kode = parseErrorCode(e);
        if (kode != null) {
            System.out.println("[" + konteks + "] Error: " + kode);
            if (onError != null)
                onError.accept(kode);
        } else {
            System.out.println("[" + konteks + "] Gagal: " + e.getMessage());
        }
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

    public boolean uploadBackup(Consumer<String> onError) {
        try {
            if (!getTokenFile().exists()) {
                System.out.println("Token tidak ditemukan.");
                if (onError != null)
                    onError.accept("BELUM_LOGIN");
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
            tanganiError(e, onError, "uploadBackup");
            return false;
        }
    }

    public boolean uploadBackup() {
        return uploadBackup(null);
    }

    // =========================================================================
    // Restore Backup Database
    // =========================================================================

    public boolean restoreBackup(Consumer<String> onError) {
        try {
            Drive drive = buildDrive();

            String fileId = getFileIdFromDrive(drive, "db_enjoy_cafe.db");
            if (fileId == null) {
                System.out.println("Backup database tidak ditemukan di Google Drive.");
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
            tanganiError(e, onError, "restoreBackup");
            return false;
        }
    }

    public boolean restoreBackup() {
        return restoreBackup(null);
    }

    // =========================================================================
    // Upload Backup Gambar
    // =========================================================================

    public boolean uploadImageBackup(Consumer<String> onError) {
        try {
            if (!getTokenFile().exists()) {
                System.out.println("Token tidak ditemukan.");
                if (onError != null)
                    onError.accept("BELUM_LOGIN");
                return false;
            }

            File imageFolder = getImageFolder();
            File[] files = imageFolder.listFiles();
            if (!imageFolder.exists() || files == null || files.length == 0) {
                System.out.println("Folder gambar kosong, skip backup gambar.");
                return true;
            }

            File zipFile = getImageZipFile();
            zipFolder(imageFolder, zipFile);

            if (!zipFile.exists()) {
                System.out.println("Zip gambar gagal dibuat.");
                return false;
            }

            Drive drive = buildDrive();
            hapusFileLama(drive, "images-backup.zip");
            uploadFileToDrive(drive, zipFile, "images-backup.zip");
            zipFile.delete();

            return true;

        } catch (Exception e) {
            tanganiError(e, onError, "uploadImageBackup");
            return false;
        }
    }

    public boolean uploadImageBackup() {
        return uploadImageBackup(null);
    }

    // =========================================================================
    // Restore Backup Gambar
    // =========================================================================

    public boolean restoreImageBackup(Consumer<String> onError) {
        try {
            Drive drive = buildDrive();

            String fileId = getFileIdFromDrive(drive, "images-backup.zip");
            if (fileId == null) {
                System.out.println("Backup gambar tidak ditemukan di Google Drive.");
                return false;
            }

            System.out.println("File gambar ditemukan | ID = " + fileId);

            File zipFile = getImageZipFile();
            try (FileOutputStream output = new FileOutputStream(zipFile)) {
                drive.files().get(fileId).executeMediaAndDownloadTo(output);
            }

            File imageFolder = getImageFolder();
            extractZip(zipFile, imageFolder);
            zipFile.delete();

            System.out.println("Restore gambar berhasil ke: " + imageFolder.getAbsolutePath());
            return true;

        } catch (Exception e) {
            tanganiError(e, onError, "restoreImageBackup");
            return false;
        }
    }

    public boolean restoreImageBackup() {
        return restoreImageBackup(null);
    }

    // =========================================================================
    // Backup Semua (DB + Gambar)
    // =========================================================================

    public boolean uploadBackupAll(Consumer<String> onError) {
        System.out.println("=== Memulai backup semua data ===");

        boolean dbOk = uploadBackup(onError);
        System.out.println("Backup DB: " + (dbOk ? "Berhasil" : "Gagal"));

        // Hanya lanjut backup gambar kalau db berhasil
        if (!dbOk)
            return false;

        boolean imgOk = uploadImageBackup(onError);
        System.out.println("Backup Gambar: " + (imgOk ? "Berhasil" : "Gagal"));

        return imgOk;
    }

    public boolean uploadBackupAll() {
        return uploadBackupAll(null);
    }

    // =========================================================================
    // Restore Semua (DB + Gambar)
    // =========================================================================

    public boolean restoreBackupAll(Consumer<String> onError) {
        System.out.println("=== Memulai restore semua data ===");

        boolean dbOk = restoreBackup(onError);
        System.out.println("Restore DB: " + (dbOk ? "Berhasil" : "Gagal"));

        // Hanya lanjut restore gambar kalau db berhasil
        if (!dbOk)
            return false;

        boolean imgOk = restoreImageBackup(onError);
        System.out.println("Restore Gambar: " + (imgOk ? "Berhasil" : "Gagal"));

        return imgOk;
    }

    public boolean restoreBackupAll() {
        return restoreBackupAll(null);
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

        } catch (java.net.UnknownHostException e) {
            System.out.println("Tidak ada koneksi internet.");
            return false;
        } catch (Exception e) {
            System.out.println("Cek backup kadaluarsa gagal: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // Mendapatkan waktu terakhir backup
    // =========================================================================

    public String getLastBackupTime() {
        try {
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