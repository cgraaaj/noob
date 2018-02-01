 @echo off
 chdir /d C:\xampp\htdocs\CHSC\Output\C
gcc %1>C:\xampp\htdocs\CHSC\Output\C\error\err.txt 2>&1
@echo on