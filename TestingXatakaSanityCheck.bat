@echo off
setlocal

if "%BROKEN_LINKS_USERNAME%"=="" set "BROKEN_LINKS_USERNAME=guest"
if "%BROKEN_LINKS_PASSWORD%"=="" set "BROKEN_LINKS_PASSWORD=guest2026"
if "%BROKEN_LINKS_MAX_PAGES%"=="" set "BROKEN_LINKS_MAX_PAGES=25"
if "%BROKEN_LINKS_MAX_LINKS%"=="" set "BROKEN_LINKS_MAX_LINKS=500"
if "%BROKEN_LINKS_FAIL_ON_BROKEN%"=="" set "BROKEN_LINKS_FAIL_ON_BROKEN=false"
if "%BROKEN_LINKS_CHECK_EXTERNAL%"=="" set "BROKEN_LINKS_CHECK_EXTERNAL=false"
if "%BROKEN_LINKS_INCLUDE_ASSETS%"=="" set "BROKEN_LINKS_INCLUDE_ASSETS=false"
if "%SANITY_MAX_SITEMAPS%"=="" set "SANITY_MAX_SITEMAPS=1000"
if "%SANITY_MAX_SITEMAP_URLS%"=="" set "SANITY_MAX_SITEMAP_URLS=0"
if "%SANITY_FAIL_ON_ERROR%"=="" set "SANITY_FAIL_ON_ERROR=false"

mvn test -Ptesting-xataka-sanity-check ^
  -DbrokenLinks.username=%BROKEN_LINKS_USERNAME% ^
  -DbrokenLinks.password=%BROKEN_LINKS_PASSWORD% ^
  -DbrokenLinks.maxPages=%BROKEN_LINKS_MAX_PAGES% ^
  -DbrokenLinks.maxLinks=%BROKEN_LINKS_MAX_LINKS% ^
  -DbrokenLinks.failOnBroken=%BROKEN_LINKS_FAIL_ON_BROKEN% ^
  -DbrokenLinks.checkExternalLinks=%BROKEN_LINKS_CHECK_EXTERNAL% ^
  -DbrokenLinks.includeAssets=%BROKEN_LINKS_INCLUDE_ASSETS% ^
  -Dsanity.maxSitemaps=%SANITY_MAX_SITEMAPS% ^
  -Dsanity.maxSitemapUrls=%SANITY_MAX_SITEMAP_URLS% ^
  -Dsanity.failOnError=%SANITY_FAIL_ON_ERROR%

endlocal
