# Script pour installer gradle-wrapper.jar au bon endroit
# Executez ce script dans PowerShell : clic droit -> "Executer avec PowerShell"
# ou dans PowerShell : .\install_gradle_wrapper.ps1

$wrapperDir = "$PSScriptRoot\gradle\wrapper"
$jarPath = "$wrapperDir\gradle-wrapper.jar"

# Creer le dossier si besoin
if (-not (Test-Path $wrapperDir)) {
    New-Item -ItemType Directory -Path $wrapperDir -Force
}

# Telecharger gradle-wrapper.jar (source officielle Gradle)
$url = "https://github.com/gradle/gradle/raw/v7.5.0/gradle/wrapper/gradle-wrapper.jar"
Write-Host "Telechargement de gradle-wrapper.jar depuis Gradle..."
try {
    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
    Invoke-WebRequest -Uri $url -OutFile $jarPath -UseBasicParsing
    Write-Host "OK : Fichier installe dans $jarPath" -ForegroundColor Green
    Write-Host "Vous pouvez maintenant relancer la synchronisation Gradle (Try Again)."
} catch {
    Write-Host "Erreur de telechargement : $_" -ForegroundColor Red
    Write-Host ""
    Write-Host "Solution manuelle :"
    Write-Host "1. Ouvrez Android Studio"
    Write-Host "2. File -> Open -> selectionnez le dossier FoodSaver_Android"
    Write-Host "3. Android Studio va creer le wrapper automatiquement"
    exit 1
}
