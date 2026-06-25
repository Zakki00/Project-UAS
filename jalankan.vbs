Set objShell = CreateObject("WScript.Shell")
Set objFSO = CreateObject("Scripting.FileSystemObject")

strDir = objFSO.GetParentFolderName(WScript.ScriptFullName)

strCmd = "cmd /c cd /d """ & strDir & """ && runtime\bin\javaw.exe " & _
    "--module-path javafx-libs " & _
    "--add-modules javafx.controls,javafx.fxml,java.desktop,jdk.crypto.ec,java.net.http " & _
    "--enable-native-access=javafx.graphics " & _
    "-Dfile.encoding=UTF-8 " & _
    "-Djava.library.path=javafx-libs " & _
    "-jar EnjoyCafe-1.0-SNAPSHOT.jar"

objShell.Run strCmd, 0, False
