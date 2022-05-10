@echo off

rem pousser l'image vers le dockerhub
set DOMAINE=%1
set PASSWORD=%2

set NOMAPPLI=ws_services_template
set NOMJAR=ws_services_template-0.0.1

IF "%PASSWORD%"=="" goto NOPASSWORD
IF "%DOMAINE%"=="" goto NOPASSWORD

cd ..
set JAVA_HOME=C:\Program Files\Java\jdk-11.0.9


rem compiler le prog
if defined JAVA_HOME (
    echo JAVA_HOME="%JAVA_HOME%"
) else (
    exit 9
)

cmd /c mvn -DskipTests=true -Dfile.encoding=UTF-8 clean install

rem retourner dans dockerhub
cd DockerHub

rem copier les fichiers sources
copy /Y ..\target\%NOMJAR%.jar .\%NOMJAR%.jar


echo Construct %NOMAPPLI% and push to %DOMAINE% using password %PASSWORD%
echo

docker login -u %DOMAINE% -p %PASSWORD%
docker build -t %NOMAPPLI% .
docker tag %NOMAPPLI% %DOMAINE%/%NOMAPPLI%
docker push %DOMAINE%/%NOMAPPLI%:latest
goto SUPP

:NOPASSWORD
cls
echo syntaxe buildN.cmd domaineDH passwordDH
echo exemple : buildN.cmd DomaineDH passwordDockerHub
goto FIN

:SUPP
rem supprimer les fichiers sources
del /Q .\%NOMJAR%.jar

:FIN