# ProyectoFinalDAM

## Conectar Android con Spring Boot

La app Android usa `http://10.0.2.2:8080/` por defecto, que es la dirección del
equipo anfitrión desde un emulador Android. Inicia MySQL con la base `helpdesk_pro`
configurada en `helpdesk-pro_SpringBoot/src/main/resources/application.properties`
y arranca el backend desde su carpeta:

```powershell
.\mvnw.cmd spring-boot:run
```

Para usar un dispositivo físico, compila con la dirección IP del equipo que
ejecuta Spring Boot, accesible desde el mismo Wi-Fi y con el puerto `8080`
habilitado:

```powershell
.\gradlew.bat :app:installDebug -PhelpdeskApiBaseUrl=http://192.168.1.10:8080/
```

Sustituye `192.168.1.10` por la IP correspondiente. La barra final es necesaria.
El tráfico HTTP sin cifrar solo está habilitado para la variante de depuración;
release requiere HTTPS. El token JWT se guarda localmente y se elimina al cerrar
sesión. El alta pública siempre crea cuentas con rol Empleado.

La recuperación de contraseña todavía requiere configurar un proveedor de correo
en Spring Boot; la app informa que la función no está disponible en vez de mostrar
un éxito falso.