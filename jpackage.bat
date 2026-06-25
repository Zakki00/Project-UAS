@echo off
echo Building installer...

jpackage ^
  --type exe ^
  --name "Enjoy Cafe" ^
  --app-version 1.0 ^
  --input "target\jpackage-input" ^
  --main-jar ProjectUAS-1.0-SNAPSHOT.jar ^
  --main-class com.mycompany.projectuas.App ^
  --runtime-image "%JAVA_HOME%" ^
  --dest "target\installer" ^
  --java-options "--module-path $APPDIR/javafx-libs" ^
  --java-options "--add-modules javafx.controls,javafx.fxml,java.desktop,jdk.crypto.ec,java.net.http" ^
  --java-options "--enable-native-access=javafx.graphics" ^
  --java-options "-Dfile.encoding=UTF-8" ^
  --win-shortcut ^
  --win-menu ^
  --win-dir-chooser

if %ERRORLEVEL% EQU 0 (
  echo.
  echo SUCCESS! Installer ada di: target\installer\
) else (
  echo.
  echo GAGAL! Cek pesan error di atas.
)
pause