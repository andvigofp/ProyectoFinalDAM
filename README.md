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

Sustituye `172.20.10.6` por la IP correspondiente. La barra final es necesaria.
El tráfico HTTP sin cifrar solo está habilitado para la variante de depuración;
release requiere HTTPS. El token JWT se guarda localmente y se elimina al cerrar
sesión. El alta pública siempre crea cuentas con rol Empleado.

La recuperación de contraseña envía un código al correo de una cuenta existente
y activa. La API responde con un mensaje genérico incluso si no encuentra la
cuenta; si no llega el correo, comprueba también Spam y los logs de Spring Boot
por errores de envío.

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
  Las credenciales de correo se pueden poner en un fichero `.env` junto al
  `docker-compose.yml` y Compose las inyecta al backend. Con Resend configura
  `RESEND_API_KEY` y `RESEND_FROM`; el remitente debe pertenecer a un dominio
  verificado en Resend. Si no defines `RESEND_FROM`, Docker usa
  `onboarding@resend.dev`, que Resend limita a destinatarios autorizados en tu
  cuenta. Con Gmail SMTP configura `GMAIL_USERNAME`,
  `GMAIL_APP_PASSWORD` y, opcionalmente, `GMAIL_FROM`. Si hay una clave de
  Resend, el backend prioriza Resend y no necesita credenciales SMTP; si no,
  usa SMTP. Tras cambiar variables en `.env`, recrea el backend con
  `docker compose up -d --force-recreate backend`.

La app Android no necesita cambios: el emulador sigue usando
`http://10.0.2.2:8080/`, que el contenedor publica en el puerto 8080 del equipo.

Comandos útiles:

```powershell
docker logs helpdesk-backend     # Ver los logs de la API
docker logs helpdesk-mysql       # Ver los logs de la base de datos
docker compose ps                # Estado de los contenedores
```

### Imagen publicada en Docker Hub

La imagen del backend está publicada en Docker Hub como
`andriu987fp/helpdesk-pro-backend:latest`. Para descargarla y usarla:

```powershell
docker pull andriu987fp/helpdesk-pro-backend:latest
```

Para volver a publicarla tras hacer cambios (con la sesión iniciada en
Docker Desktop o con `docker login`):

```powershell
docker compose build backend
docker tag helpdesk-pro-backend:latest andriu987fp/helpdesk-pro-backend:latest
docker push andriu987fp/helpdesk-pro-backend:latest
```

## Despliegue en la nube con Railway

El proyecto está desplegado en **Railway** (https://railway.app) con dos
servicios: la base de datos MySQL y el backend Spring Boot construido con el
mismo `Dockerfile` del proyecto. La app Android se conecta por HTTPS.

### Arquitectura del despliegue

| Servicio | Qué es | Dirección |
|---|---|---|
| `MySQL` | Imagen oficial `mysql:8.4` con volumen persistente | Privada: `mysql.railway.internal:3306` (solo accesible dentro de Railway). Pública: proxy TCP que genera Railway (`*.proxy.rlwy.net:puerto`) |
| `ProyectoFinalDAM` | Backend Spring Boot, construido desde el repo de GitHub con el `Dockerfile` | `https://proyectofinaldam-production-0311c21f.up.railway.app` |

### Pasos realizados para desplegar

1. **Crear el proyecto** en Railway → *New Project*.
2. **Añadir MySQL**: *Add Service → Database → MySQL*. Railway crea el
   contenedor con un volumen persistente y genera las credenciales.
3. **Cargar los datos**: en el servicio MySQL, apartado *Networking*, se activa
   el **proxy TCP público** y desde el PC se ejecuta el script de siembra:

   ```powershell
   mysql -h <host-proxy> -P <puerto-proxy> -uroot -p<contraseña> < helpdesk_pro_mvp.sql
   ```

4. **Añadir el backend**: *Add Service → GitHub Repo → ProyectoFinalDAM*.
   En *Settings → Source* hay que indicar el **Root Directory**:
   `helpdesk-pro_SpringBoot` (ahí están el `Dockerfile` y el `pom.xml`).
5. **Variables de entorno del backend** (*Variables → Raw Editor*):

   ```
   DB_URL=jdbc:mysql://mysql.railway.internal:3306/helpdesk_pro?useUnicode=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true&useSSL=false
   DB_USER=root
   DB_PASSWORD=<contraseña que genera Railway para MySQL>
   JWT_SECRETO=<clave secreta larga para firmar los JWT>
   JWT_DURACION_MS=86400000
   RESEND_API_KEY=<clave API de Resend>
   RESEND_FROM=Helpdesk Pro <no-reply@tu-dominio-verificado>
   ```

   `mysql.railway.internal` es la red privada de Railway: el backend habla con
   MySQL sin salir a internet y sin gastar ancho de banda público. Verifica el
   dominio del remitente en Resend antes de usarlo. `GMAIL_FROM` solo se utiliza
   con SMTP; no es el remitente de Resend. No configures `GMAIL_RECOVERY_TO` en
   producción, ya que redirige todos los códigos de recuperación a una sola
   dirección.
6. **Dominio HTTPS**: *Settings → Networking → Generate Domain* (puerto `8080`).
   Railway asigna un dominio `*.up.railway.app` con certificado HTTPS.

Railway reconstruye y redespliega el backend automáticamente con cada
`git push` a la rama conectada.

### Compilar la app Android contra la nube

```powershell
.\gradlew.bat :app:installDebug "-PhelpdeskApiBaseUrl=https://proyectofinaldam-production-0311c21f.up.railway.app/"
```

Al ser HTTPS, esta misma URL sirve también para la variante release y para
dispositivos físicos sin configuración extra.

### Respaldo automático local ↔ nube

La app lleva integrada una **URL de respaldo**: primero intenta conectar con la
URL principal y, si no responde (error de red), reintenta automáticamente contra
Railway. Por defecto la principal es `http://10.0.2.2:8080/` (backend local) y
el respaldo es el dominio de Railway, configurable con la propiedad de Gradle
`helpdeskApiFallbackUrl`. Así la misma APK funciona con el backend en local
cuando Docker está encendido y contra la nube cuando está apagado.

### APK de demostración (demo sin internet)

Para una demo sin Wi-Fi (por ejemplo en el instituto), se usa el hotspot del
móvil como red local y el PC como servidor:

1. Activa el hotspot del móvil y conecta el PC a esa red.
2. En el PC, ejecuta `ipconfig` y anota la **Dirección IPv4** (192.168.x.x).
3. Arranca el backend: `docker compose up -d`.
4. Genera la APK con `.\generar-apk-demo.ps1` (pide la IP del PC y compila
   `helpdeskpro-demo.apk` en la raíz del proyecto).
5. Instala la APK en el móvil de la demo.

La app intentará primero contra el PC (`http://IP-DEL-PC:8080`) y, si el móvil
tiene datos, saltará a Railway como respaldo automático.

### Notas

- El plan de prueba de Railway tiene crédito limitado (unos 23 días / 5 $);
  pasado ese tiempo hay que pasar a plan de pago o migrar a otro servidor
  (por ejemplo una VM de Oracle Cloud, siempre gratuita, con el script
  `deploy-oracle.sh`).
- Las contraseñas reales (MySQL, JWT) **no** se guardan en este repositorio:
  viven únicamente en las variables de entorno de Railway.
