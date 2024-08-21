# TaskSystem
## Запуск на Docker
1. В консоли надо запустить 2 команды из корневой папки проекта:
   1. **./gradlew clean build**
   2. **cp .\\build\\libs\\TaskSystem.jar .\\Docker\\**
2. Запустить docker-compose.yml в папке Docker. Для этого
   1. Перейти в папку Docker (например, командой **cd .\\Docker\\**)
   2. Запустить docker-compose командой **docker-compose up**

После этого моё приложение будет доступно по адресу http://localhost:8080  
Документация endpoint`ов доступна по адресу http://localhost:8080/swagger-ui/index.html