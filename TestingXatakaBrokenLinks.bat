@echo off
setlocal

if "%BROKEN_LINKS_USERNAME%"=="" set "BROKEN_LINKS_USERNAME=guest"
if "%BROKEN_LINKS_PASSWORD%"=="" set "BROKEN_LINKS_PASSWORD=guest2026"
if "%BROKEN_LINKS_MAX_PAGES%"=="" set "BROKEN_LINKS_MAX_PAGES=25"
if "%BROKEN_LINKS_MAX_LINKS%"=="" set "BROKEN_LINKS_MAX_LINKS=500"
if "%BROKEN_LINKS_FAIL_ON_BROKEN%"=="" set "BROKEN_LINKS_FAIL_ON_BROKEN=true"

mvn test -Ptesting-xataka-broken-links ^
  -DbrokenLinks.username=%BROKEN_LINKS_USERNAME% ^
  -DbrokenLinks.password=%BROKEN_LINKS_PASSWORD% ^
  -DbrokenLinks.maxPages=%BROKEN_LINKS_MAX_PAGES% ^
  -DbrokenLinks.maxLinks=%BROKEN_LINKS_MAX_LINKS% ^
  -DbrokenLinks.failOnBroken=%BROKEN_LINKS_FAIL_ON_BROKEN%

endlocal
