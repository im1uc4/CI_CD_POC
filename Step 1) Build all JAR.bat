start cmd /k "cd brain-server & call mvnw.cmd clean package & copy target\brain-server-0.0.1-SNAPSHOT.jar brain-server-0.0.1-SNAPSHOT.jar"
start cmd /k "cd brain-zuul & call mvnw.cmd clean package & copy target\brain-zuul-0.0.1-SNAPSHOT.jar brain-zuul-0.0.1-SNAPSHOT.jar"
start cmd /k "cd brain-core-api & call mvnw.cmd clean package & copy target\brain-core-api-0.0.1-SNAPSHOT.jar brain-core-api-0.0.1-SNAPSHOT.jar"
start cmd /k "cd brain-auth & call mvnw.cmd clean package & copy target\brain-auth-0.0.1-SNAPSHOT.jar brain-auth-0.0.1-SNAPSHOT.jar"