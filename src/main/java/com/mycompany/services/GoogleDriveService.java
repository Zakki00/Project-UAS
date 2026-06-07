package com.mycompany.services;

import java.io.File;

import com.google.api.client.auth.oauth2.Credential;
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
                ? new File(appData + "\\ProjectUAS\\db\\db_enjoy_cafe.db")
                : new File(System.getProperty("user.home") + "/ProjectUAS/db/db_enjoy_cafe.db");
    }

    private static File getTokenFile() {
        String appData = System.getenv("APPDATA");
        return (appData != null && !appData.isEmpty())
                ? new File(appData + "\\ProjectUAS\\tokens\\StoredCredential")
                : new File(System.getProperty("user.home") + "/ProjectUAS/tokens/StoredCredential");
    }

    // =========================================================================
    // Upload Backup ke Google Drive
    // =========================================================================

    public boolean uploadBackup() {
        try {
            // Cek token
            if (!getTokenFile().exists()) {
                System.out.println("Token tidak ditemukan: " + getTokenFile().getAbsolutePath());
                return false;
            }

            Credential credential = GoogleAuthService.loadCredential();
            if (credential == null) {
                System.out.println("Credential NULL");
                return false;
            }

            Drive drive = new Drive.Builder(
                    new ApacheHttpTransport(),
                    GsonFactory.getDefaultInstance(),
                    credential)
                    .setApplicationName("Enjoy Cafe POS")
                    .build();

            // Hapus file lama di Drive
            FileList result = drive.files()
                    .list()
                    .setQ("name='db_enjoy_cafe.db' and trashed=false")
                    .setFields("files(id,name)")
                    .execute();

            for (com.google.api.services.drive.model.File file : result.getFiles()) {
                System.out.println("Menghapus file lama: " + file.getName() + " | ID = " + file.getId());
                drive.files().delete(file.getId()).execute();
            }

            // Cek file yang akan diupload
            File uploadFile = getDbFile();
            System.out.println("Path = " + uploadFile.getAbsolutePath());
            System.out.println("Ada File = " + uploadFile.exists());
            System.out.println("Ukuran = " + uploadFile.length());

            if (!uploadFile.exists()) {
                System.out.println("File backup tidak ditemukan");
                return false;
            }

            // Upload file baru
            com.google.api.services.drive.model.File metadata = new com.google.api.services.drive.model.File();
            metadata.setName("db_enjoy_cafe.db");

            FileContent mediaContent = new FileContent("application/octet-stream", uploadFile);

            com.google.api.services.drive.model.File uploadedFile = drive.files()
                    .create(metadata, mediaContent)
                    .setFields("id,name")
                    .execute();

            System.out
                    .println("Upload berhasil | ID = " + uploadedFile.getId() + " | Nama = " + uploadedFile.getName());
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================================================================
    // Restore Backup dari Google Drive
    // =========================================================================

    public boolean restoreBackup() {
        try {
            Credential credential = GoogleAuthService.loadCredential();
            if (credential == null) {
                System.out.println("Credential NULL");
                return false;
            }

            Drive drive = new Drive.Builder(
                    new ApacheHttpTransport(),
                    GsonFactory.getDefaultInstance(),
                    credential)
                    .setApplicationName("Enjoy Cafe POS")
                    .build();

            // Cari file backup di Drive
            FileList result = drive.files()
                    .list()
                    .setQ("name='db_enjoy_cafe.db' and trashed=false")
                    .setFields("files(id,name)")
                    .execute();

            if (result.getFiles().isEmpty()) {
                System.out.println("Backup tidak ditemukan di Google Drive");
                return false;
            }

            String fileId = result.getFiles().get(0).getId();
            System.out.println("File ditemukan | ID = " + fileId);

            // Download ke AppData
            File localDb = getDbFile();
            if (!localDb.getParentFile().exists()) {
                localDb.getParentFile().mkdirs();
            }

            try (java.io.FileOutputStream output = new java.io.FileOutputStream(localDb)) {
                drive.files().get(fileId).executeMediaAndDownloadTo(output);
            }

            System.out.println("Restore berhasil ke: " + localDb.getAbsolutePath());
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}