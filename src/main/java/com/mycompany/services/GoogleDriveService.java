package com.mycompany.services;

import java.io.File;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;

public class GoogleDriveService {
    public boolean uploadBackup() {

        try {

            File tokenFile = new File("tokens/StoredCredential");

            if (!tokenFile.exists()) {
                System.out.println("Token tidak ditemukan");
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

            // ==========================
            // HAPUS FILE LAMA DI DRIVE
            // ==========================
            FileList result = drive.files()
                    .list()
                    .setQ("name='db_enjoy_cafe.db' and trashed=false")
                    .setFields("files(id,name)")
                    .execute();

            for (com.google.api.services.drive.model.File file : result.getFiles()) {

                System.out.println("Menghapus file lama: "
                        + file.getName()
                        + " | ID = "
                        + file.getId());

                drive.files()
                        .delete(file.getId())
                        .execute();
            }

            // ==========================
            // FILE YANG AKAN DIUPLOAD
            // ==========================
            java.io.File uploadFile = new java.io.File(
                    "src/main/resources/db/db_enjoy_cafe_backup.db");

            System.out.println("Path = " + uploadFile.getAbsolutePath());
            System.out.println("Ada File = " + uploadFile.exists());
            System.out.println("Ukuran = " + uploadFile.length());

            if (!uploadFile.exists()) {
                System.out.println("File backup tidak ditemukan");
                return false;
            }

            // ==========================
            // UPLOAD FILE BARU
            // ==========================
            com.google.api.services.drive.model.File metadata = new com.google.api.services.drive.model.File();

            metadata.setName("db_enjoy_cafe.db");

            FileContent mediaContent = new FileContent(
                    "application/octet-stream",
                    uploadFile);

            com.google.api.services.drive.model.File uploadedFile = drive.files()
                    .create(metadata, mediaContent)
                    .setFields("id,name")
                    .execute();

            System.out.println("Upload berhasil");
            System.out.println("ID = " + uploadedFile.getId());
            System.out.println("Nama = " + uploadedFile.getName());

            return true;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }

    //restore database
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

            // Cari file backup
            FileList result = drive.files()
                    .list()
                    .setQ("name='db_enjoy_cafe.db' and trashed=false")
                    .setFields("files(id,name)")
                    .execute();

            if (result.getFiles().isEmpty()) {
                System.out.println("Backup tidak ditemukan");
                return false;
            }

            String fileId = result.getFiles().get(0).getId();

            System.out.println("File ditemukan");
            System.out.println("ID = " + fileId);

            java.io.File localDb = new java.io.File("src/main/resources/db/db_enjoy_cafe.db");

            try (java.io.FileOutputStream output = new java.io.FileOutputStream(localDb)) {

                drive.files()
                        .get(fileId)
                        .executeMediaAndDownloadTo(output);
            }

            System.out.println("Restore berhasil");

            return true;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }
}
