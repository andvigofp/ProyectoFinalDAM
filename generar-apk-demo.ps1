# ==========================================================
# Genera la APK de DEMOSTRACIÓN de HelpDesk Pro.
#
# Pensado para la demo sin internet: el móvil comparte datos
# (hotspot), el PC se conecta a esa red y ejecuta el backend
# con Docker. La APK apunta a la IP del PC en esa red y, si no
# responde, usa Railway como respaldo (ya integrado en la app).
#
# Uso:
#   1. Activa el hotspot del móvil y conecta el PC a esa red.
#   2. Ejecuta: ipconfig  ->  anota la "Dirección IPv4" del PC
#      (suele ser 192.168.x.x).
#   3. Arranca el backend:  docker compose up -d
#   4. Ejecuta este script:  .\generar-apk-demo.ps1
#      (te pedirá la IP si no la pasas con -IpPc)
#   5. Pasa el fichero helpdeskpro-demo.apk al móvil e instálalo.
# ==========================================================
param(
    [string]$IpPc = ""
)

$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot

if ([string]::IsNullOrWhiteSpace($IpPc)) {
    $IpPc = Read-Host "IP del PC en la red del hotspot (ej. 192.168.43.128)"
}

if ($IpPc -notmatch '^\d{1,3}(\.\d{1,3}){3}$') {
    Write-Host "La IP '$IpPc' no parece válida." -ForegroundColor Red
    exit 1
}

# Java: usa el JDK local si JAVA_HOME no está definido
if (-not $env:JAVA_HOME) {
    $env:JAVA_HOME = "$env:USERPROFILE\.jdks\ms-25.0.2"
}

Write-Host "Generando APK con la URL http://${IpPc}:8080/ ..." -ForegroundColor Cyan
& .\gradlew.bat :app:assembleDebug "-PhelpdeskApiBaseUrl=http://${IpPc}:8080/"
if ($LASTEXITCODE -ne 0) {
    Write-Host "Error al compilar la APK." -ForegroundColor Red
    exit $LASTEXITCODE
}

$origen = "app\build\outputs\apk\debug\app-debug.apk"
$destino = "helpdeskpro-demo.apk"
Copy-Item $origen $destino -Force

Write-Host ""
Write-Host "APK lista: $PSScriptRoot\$destino" -ForegroundColor Green
Write-Host "Pásala al móvil (cable, correo, Drive...) e instálala." -ForegroundColor Green
Write-Host "Recuerda: el PC y el móvil deben estar en la MISMA red del hotspot" -ForegroundColor Yellow
Write-Host "y el backend arrancado con: docker compose up -d" -ForegroundColor Yellow
