#define MyAppName "Enjoy Cafe"
#define MyAppVersion "1.0"
#define MyAppPublisher "Kelompok 1 ITBT"

[Setup]
AppId={{EnjoyСafe-2026-ITBT-K1}}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
AppPublisher={#MyAppPublisher}
DefaultDirName={autopf64}\Enjoy Cafe
DefaultGroupName={#MyAppName}
OutputDir=output-inno
OutputBaseFilename=EnjoyСafe-Setup-1.0
Compression=lzma2/ultra64
SolidCompression=yes
WizardStyle=modern
UninstallDisplayName=Enjoy Cafe
UninstallDisplayIcon={app}\icon.ico
PrivilegesRequired=admin
ArchitecturesInstallIn64BitMode=x64compatible

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "Buat shortcut di Desktop"; GroupDescription: "Shortcut:"; Flags: unchecked

[Files]
; JAR utama
Source: "D:\Project Java\Project-UAS\target\jpackage-input\EnjoyCafe-1.0-SNAPSHOT.jar"; DestDir: "{app}"; Flags: ignoreversion

; JavaFX JAR (folder lib)
Source: "C:\javafx-sdk-26.0.1\lib\*"; DestDir: "{app}\javafx-libs"; Flags: ignoreversion

; JavaFX DLL (folder bin)
Source: "C:\javafx-sdk-26.0.1\bin\*"; DestDir: "{app}\javafx-libs"; Flags: ignoreversion

; Java Runtime
Source: "C:\oracleJdk-26\*"; DestDir: "{app}\runtime"; Flags: ignoreversion recursesubdirs

; Icon
Source: "D:\Project Java\Project-UAS\src\main\resources\image\Logo.ico"; DestDir: "{app}"; DestName: "icon.ico"; Flags: ignoreversion

; Launcher VBS
Source: "jalankan.vbs"; DestDir: "{app}"; Flags: ignoreversion

[Icons]
; Start Menu - pakai wscript.exe untuk jalankan .vbs
Name: "{group}\Enjoy Cafe"; Filename: "{sys}\wscript.exe"; Parameters: """{app}\jalankan.vbs"""; IconFilename: "{app}\icon.ico"; WorkingDir: "{app}"
Name: "{group}\Uninstall Enjoy Cafe"; Filename: "{uninstallexe}"

; Desktop shortcut
Name: "{autodesktop}\Enjoy Cafe"; Filename: "{sys}\wscript.exe"; Parameters: """{app}\jalankan.vbs"""; IconFilename: "{app}\icon.ico"; WorkingDir: "{app}"; Tasks: desktopicon

[Run]
; Jalankan setelah install selesai
Filename: "{sys}\wscript.exe"; Parameters: """{app}\jalankan.vbs"""; Description: "Jalankan Enjoy Cafe sekarang"; Flags: nowait postinstall skipifsilent
