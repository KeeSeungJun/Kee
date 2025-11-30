<#
PowerShell wrapper to safely run the Maven Wrapper (mvnw.cmd) on Windows.
Fixes common issue where JAVA_HOME contains surrounding quotes which break batch parsing
(e.g. `"C:\Program Files\..."`).

Usage:
  .\run-windows.ps1 spring-boot:run
  .\run-windows.ps1 -Args 'spring-boot:run','-DskipTests'

If execution policy prevents running the script, run:
  powershell -ExecutionPolicy Bypass -File .\run-windows.ps1 spring-boot:run
#>

param(
    [Parameter(ValueFromRemainingArguments = $true)]
    [string[]]
    $ArgsList
)

function Remove-SurroundingQuotes([string]$s) {
    if ($null -eq $s) { return $s }
    # Remove surrounding single or double quotes safely using regex
    return ($s -replace '^[\'\"]+|[\'\"]+$','')
}

Write-Host "[run-windows.ps1] Starting wrapper..." -ForegroundColor Cyan

# Resolve project root (script location)
$scriptDir = Split-Path -Parent $PSCommandPath
Set-Location $scriptDir

# Determine JAVA_HOME: prefer env, else infer from 'where java'
$rawJavaHome = $env:JAVA_HOME
if ([string]::IsNullOrWhiteSpace($rawJavaHome)) {
    Write-Host "[run-windows.ps1] WARNING: JAVA_HOME not set. Attempting to infer from 'where java'..." -ForegroundColor Yellow
    try {
        $javaPath = (where.exe java) -split "`n" | Select-Object -First 1
        if ($javaPath) {
            # remove trailing \bin\java.exe
            $inferred = Split-Path -Parent $javaPath
            $inferred = Split-Path -Parent $inferred
            $rawJavaHome = $inferred
            Write-Host "[run-windows.ps1] Inferred JAVA_HOME = $rawJavaHome" -ForegroundColor Green
        }
    } catch {
        Write-Host "[run-windows.ps1] Could not infer java location." -ForegroundColor Yellow
    }
}

$cleanJavaHome = Remove-SurroundingQuotes $rawJavaHome
if (-not [string]::IsNullOrWhiteSpace($cleanJavaHome)) {
    Write-Host "[run-windows.ps1] Using JAVA_HOME: $cleanJavaHome" -ForegroundColor Green
    $env:JAVA_HOME = $cleanJavaHome
    # Prepend JAVA_HOME\bin to PATH if not already present
    $javaBin = Join-Path $cleanJavaHome 'bin'
    if ($env:PATH -notlike "*$javaBin*") {
        $env:PATH = "$javaBin;$env:PATH"
        Write-Host "[run-windows.ps1] Added $javaBin to PATH" -ForegroundColor Green
    }
} else {
    Write-Host "[run-windows.ps1] No JAVA_HOME available; continuing with existing PATH." -ForegroundColor Yellow
}

# Locate mvnw.cmd
$mvnw = Join-Path $scriptDir 'mvnw.cmd'
if (-not (Test-Path $mvnw)) {
    Write-Host "[run-windows.ps1] ERROR: mvnw.cmd not found in project root ($scriptDir)." -ForegroundColor Red
    Write-Host "Please run this script from the project root that contains mvnw.cmd." -ForegroundColor Red
    exit 1
}

# Build the argument array for mvnw
$forwardArgs = @()
if ($ArgsList -and $ArgsList.Count -gt 0) {
    $forwardArgs = $ArgsList
}

Write-Host "[run-windows.ps1] Executing: mvnw.cmd $($forwardArgs -join ' ')" -ForegroundColor Cyan

# Execute mvnw.cmd and stream output
try {
    & $mvnw @forwardArgs
    $exitCode = $LASTEXITCODE
    if ($exitCode -ne 0) {
        Write-Host "[run-windows.ps1] mvnw exited with code $exitCode" -ForegroundColor Red
        exit $exitCode
    }
} catch {
    Write-Host "[run-windows.ps1] Exception while running mvnw: $_" -ForegroundColor Red
    exit 1
}

Write-Host "[run-windows.ps1] Done." -ForegroundColor Cyan
