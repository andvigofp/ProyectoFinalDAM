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