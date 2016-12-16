rem JBoss, Home of Professional Open Source
rem Copyright 2016, Red Hat, Inc., and others contributors as indicated
rem by the @authors tag. All rights reserved.
rem See the copyright.txt in the distribution for a
rem full listing of individual contributors.
rem This copyrighted material is made available to anyone wishing to use,
rem modify, copy, or redistribute it subject to the terms and conditions
rem of the GNU Lesser General Public License, v. 2.1.
rem This program is distributed in the hope that it will be useful, but WITHOUT A
rem WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
rem PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
rem You should have received a copy of the GNU Lesser General Public License,
rem v.2.1 along with this distribution; if not, write to the Free Software
rem Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
rem MA  02110-1301, USA.

SETLOCAL EnableDelayedExpansion

set M2_HOME=c:\hudson\tools\apache-maven-3.0.3
set ANT_HOME=C:\hudson\tools\apache-ant-1.8.2
set GNUWIN32_PATH=C:\hudson\gnuwin32
set PATH=%M2_HOME%\bin\;%ANT_HOME%\bin;%GNUWIN32_PATH%\bin;%PATH%

call:comment_on_pull "Starting tests %BUILD_URL%"

git remote add upstream https://github.com/jbosstm/quickstart.git
set BRANCHPOINT=master
git branch %BRANCHPOINT% origin/%BRANCHPOINT%
git pull --rebase --ff-only origin %BRANCHPOINT% || (call:comment_on_pull "Rebase failed %BUILD_URL%" && exit -1)

rem INITIALIZE C++ COMPILER
call "C:\Program Files (x86)\Microsoft Visual Studio 9.0\VC\vcvarsall.bat"

rem find the ip address of the host
rem for /f "delims=" %%a in ('hostname') do @set host=%%a 
rem for /f "tokens=2 delims=[]" %%a in ('ping -n 1 %host%') do set JBOSSAS_IP_ADDR=%%a
rem INITIALIZE ENV

set ORACLE_HOME=C:\hudson\workspace\%JOB_NAME%\instantclient_11_2
set TNS_ADMIN=C:\hudson\workspace\%JOB_NAME%\instantclient_11_2\network\admin


echo Running quickstarts
echo %PATH%
mvn --version
call build.bat -f blacktie\pom.xml clean install || (call:comment_on_pull "Pull failed %BUILD_URL%" && exit -1)


call:comment_on_pull "Pull passed %BUILD_URL%"


rem -------------------------------------------------------
rem -                 Functions bellow                    -
rem -------------------------------------------------------

goto:eof

:comment_on_pull
   if not "%COMMENT_ON_PULL%"=="1" goto:eof

   for /f "tokens=1,2,3,4 delims=/" %%a in ("%GIT_BRANCH%") do set IS_PULL=%%b&set PULL_NUM=%%c
   if not "%IS_PULL%"=="pull" goto:eof
   
   curl -k -d "{ \"body\": \"%~1\" }" -ujbosstm-bot:%BOT_PASSWORD% https://api.github.com/repos/%GIT_ACCOUNT%/%GIT_REPO%/issues/%PULL_NUM%/comments

goto:eof
