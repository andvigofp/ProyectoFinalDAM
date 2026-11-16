# ProyectoFinalDAM

## Conectar Android con Spring Boot

La app Android usa `http://10.0.2.2:8080/` por defecto, que es la dirección del
equipo anfitrión desde un emulador Android. Inicia MySQL con la base `helpdesk_pro`
configurada en `helpdesk-pro_SpringBoot/src/main/resources/application.properties`.
Para enviar correos de recuperación, configura `GMAIL_USERNAME` y
`GMAIL_APP_PASSWORD` como variables de entorno en la configuración de ejecución
de Spring Boot en IntelliJ. Usa una contraseña de aplicación de Google, no la
contraseña normal de la cuenta. Reinicia Spring Boot después de cambiar estas
variables. Arranca el backend desde su carpeta:

```powershell
.\mvnw.cmd spring-boot:run
```

Para pruebas, puedes añadir `GMAIL_RECOVERY_TO=animacion96@gmail.com` a esa
misma configuración. Esto redirige a ese Gmail los códigos solicitados para
cualquier cuenta, aunque en la app se introduzca el correo corporativo. No lo
actives en producción: quien controle ese Gmail podría restablecer las
contraseñas de todas las cuentas.

Para usar un dispositivo físico, compila con la dirección IP del equipo que
ejecuta Spring Boot, accesible desde el mismo Wi-Fi y con el puerto `8080`
habilitado:

```powershell
.\gradlew.bat :app:installDebug -PhelpdeskApiBaseUrl=http://172.20.10.6:8080/
```

Sustituye `192.168.1.10` por la IP correspondiente. La barra final es necesaria.
El tráfico HTTP sin cifrar solo está habilitado para la variante de depuración;
release requiere HTTPS. El token JWT se guarda localmente y se elimina al cerrar
sesión. El alta pública siempre crea cuentas con rol Empleado.

La recuperación de contraseña envía un código al correo de una cuenta existente
y activa. La API responde con un mensaje genérico incluso si no encuentra la
cuenta; si no llega el correo, comprueba también Spam y los logs de Spring Boot
por errores SMTP.

Las búsquedas de incidencias y usuarios se filtran y ordenan alfabéticamente en
la API con el parámetro opcional `busqueda`; por ejemplo,
`GET /api/incidencias/usuario/3?busqueda=correo` o
`GET /api/usuarios?busqueda=ana`.
## Despliegue con Docker

El backend y la base de datos se pueden levantar juntos en contenedores con un
solo comando. Requisito: tener **Docker Desktop** instalado y en ejecución
(en Windows requiere WSL2, que el propio instalador o `wsl --install` configura).

Desde la raíz del proyecto:

```powershell
docker compose up --build -d   # Primera vez: construye la imagen y arranca todo
docker compose up -d           # Siguientes veces: arranque normal
docker compose down            # Parar los contenedores (los datos se conservan)
docker compose down -v         # Parar y BORRAR la base de datos de Docker
```

Esto levanta dos contenedores:

| Contenedor | Imagen | Puerto en el PC | Función |
|---|---|---|---|
| `helpdesk-mysql` | `mysql:8.4` | `3307` | Base de datos `helpdesk_pro` |
| `helpdesk-backend` | construida con el `Dockerfile` | `8080` | API Spring Boot |

- **Inicialización automática**: la primera vez, MySQL ejecuta
  `helpdesk-pro_SpringBoot/helpdesk_pro_mvp.sql` y crea la base de datos con
  todos los datos de prueba. El script solo se ejecuta con el volumen vacío;
  si cambias el SQL, haz `docker compose down -v` antes de volver a arrancar.
- **Persistencia**: los datos se guardan en el volumen `mysql_data` y sobreviven
  a paradas y reinicios.
- **Puerto 3307**: el MySQL de Docker usa el 3307 en el PC porque el 3306 suele
  estar ocupado por el MySQL local de desarrollo. Dentro de la red de Docker el
  backend conecta por el 3306 interno; no hay que cambiar nada en el código.
- **Bases de datos independientes**: la de Docker y la de tu MySQL local no
  comparten datos.
- **Variables de entorno**: `application.properties` toma la conexión de
  `DB_URL`, `DB_USER` y `DB_PASSWORD`, con valores por defecto para desarrollo
  local, así que el arranque con `mvnw spring-boot:run` sigue funcionando igual.
  Las credenciales de correo (`GMAIL_USERNAME`, `GMAIL_APP_PASSWORD`,
  `GMAIL_FROM`, `GMAIL_RECOVERY_TO`) se pueden poner en un fichero `.env` junto
  al `docker-compose.yml` y Compose las inyecta al backend.

La app Android no necesita cambios: el emulador sigue usando
`http://10.0.2.2:8080/`, que el contenedor publica en el puerto 8080 del equipo.

Comandos útiles:

```powershell
docker logs helpdesk-backend     # Ver los logs de la API
docker logs helpdesk-mysql       # Ver los logs de la base de datos
docker compose ps                # Estado de los contenedores
```

### Subir la imagen a Docker Hub

Con la sesión iniciada (`docker login` o desde Docker Desktop):

```powershell
docker tag helpdesk-pro-backend:latest TU_USUARIO/helpdesk-pro-backend:latest
docker push TU_USUARIO/helpdesk-pro-backend:latest
```

Sustituye `TU_USUARIO` por tu nombre de usuario de Docker Hub.
