package com.mycompany.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class BackupService {

    public boolean backupLocal() {
        try {

            Path source = Path.of("src/main/resources/db/db_enjoy_cafe.db");
            Path target = Path.of("src/main/resources/db/db_enjoy_cafe_backup.db");

            Files.createDirectories(target.getParent());

            Files.copy(
                    source,
                    target,
                    StandardCopyOption.REPLACE_EXISTING);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}