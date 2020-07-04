start cmd /k "title server & cd brain-server & call "Step 2) Run JAR.bat""
timeout /t 15
start cmd /k "title zuul & cd brain-zuul & call "Step 2) Run JAR.bat""
start cmd /k "title core-api & cd brain-core-api & call "Step 2) Run JAR.bat""
start cmd /k "title auth & cd brain-auth & call "Step 2) Run JAR.bat""