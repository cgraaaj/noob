@echo off
 chdir /d C:\xampp\htdocs\CHSC\Output
 python PostBeforeAdjust.py %1
 python copi.py %1
 @echo on