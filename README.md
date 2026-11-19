# Lab 10 – Configuración de Ambientes

Spring Profiles + variables de entorno para ejecutar la misma aplicación en distintos ambientes sin tocar el código.

| Carpeta | Contenido |
|---|---|
| `productos-ambientes/` | Procedimiento de la guía: proyecto de la semana 05 con perfiles `dev`, `qa`, `prod` y `GET /config` |
| `empleados-ambientes/` | Actividad (TechSolutions SAC): CRUD de empleados con perfiles `dev`, `qa`, `test`, `prod` y `GET /configuracion` |
| `.run/` | Configuraciones de IntelliJ con las variables de entorno de cada ambiente |
| `postman/` | Colección para probar el CRUD y `/configuracion` |

## 1. Bases de datos

Ejecutar en MySQL:

- `productos-ambientes/database/bases_de_datos.sql` → `productos_dev`, `productos_qa`, `productos_prod`
- `empleados-ambientes/database/bases_de_datos.sql` → `empleados_dev`, `empleados_qa`, `empleados_test`, `empleados_prod` (con la tabla `empleado`)

> En `prod` se usa `ddl-auto=validate`: Hibernate no crea tablas, solo verifica que existan. Por eso los scripts crean las tablas de producción.

## 2. Variables de entorno

| Variable | Uso |
|---|---|
| `PROFILE_ACTIVE` | Perfil a cargar (`dev` por defecto) |
| `DB_URL` | URL JDBC de la base del ambiente |
| `DB_USER` / `DB_PASSWORD` | Credenciales de MySQL |
| `SERVER_PORT` | Puerto del ambiente |

`application.properties` solo contiene `spring.profiles.active=${PROFILE_ACTIVE:dev}`; cada `application-<perfil>.properties` lee el resto con `${VARIABLE:valor_por_defecto}`.

En IntelliJ: **Run → Edit Configurations** o usar directamente las configuraciones de la carpeta `.run/` (Empleados DEV, QA, TEST, PROD).

## 3. Ambientes de la actividad

| Perfil | Puerto | Base de datos | `empresa.ambiente` |
|---|---|---|---|
| dev | 8080 | empleados_dev | DESARROLLO |
| qa | 8082 | empleados_qa | CALIDAD |
| test | 8084 | empleados_test | PRUEBAS |
| prod | 8086 | empleados_prod | PRODUCCION |

## 4. Endpoints

| Método | Endpoint |
|---|---|
| POST | `/empleados` |
| GET | `/empleados` |
| GET | `/empleados/{id}` |
| PUT | `/empleados/{id}` |
| DELETE | `/empleados/{id}` |
| GET | `/configuracion` |
| GET | `/configuracion/conexion` |

Respuesta de `/configuracion` en dev:

```json
{"ambiente":"DESARROLLO","perfil":"dev","puerto":"8080","baseDatos":"empleados_dev"}
```

`/configuracion/conexion` ejecuta `SELECT DATABASE()` en MySQL y compara la base realmente conectada con la del perfil activo:

```json
{"perfil":"dev","baseDatosEsperada":"empleados_dev","baseDatosConectada":"empleados_dev","conectado":true,"mensaje":"Conectado a la base de datos del perfil dev"}
```

## Lectura de propiedades

| Forma | Dónde | Qué lee |
|---|---|---|
| `@Value` | `ConfiguracionService.ambiente` | `empresa.ambiente` |
| `Environment` | `ConfiguracionService` | perfil activo (`getActiveProfiles`), puerto (`local.server.port`), URL de la base (`spring.datasource.url`) |

## Errores de configuración

| Escenario | Resultado |
|---|---|
| `PROFILE_ACTIVE=produccion` (perfil inexistente) | No arranca: `Perfil no válido: 'produccion'. Use uno de: dev, qa, test, prod` (`ValidadorPerfil`) |
| `DB_URL` apunta a una base que no existe | No arranca: `Unknown database 'empleados_noexiste'` |
| Perfil `prod` sin la tabla `empleado` | No arranca: `ddl-auto=validate` informa que falta la tabla |
| Puerto ocupado por otra aplicación | No arranca: `Port 8080 was already in use`; se cambia con `SERVER_PORT` |

## Observaciones

1. La guía nombra los archivos `application.dev.properties`, pero Spring Boot solo reconoce el formato `application-dev.properties` (con guion); con punto el perfil no se carga.
2. Con `ddl-auto=validate` (producción) la aplicación no arranca si la tabla no existe, así que el script SQL crea la tabla en las bases de producción.
3. Si el puerto del ambiente ya está ocupado por otra aplicación, basta con cambiar `SERVER_PORT` sin tocar el código.
4. Las variables tienen valores por defecto (`${DB_URL:...}`) para que el proyecto también arranque si no se configuran en el IDE.
5. El proyecto de la semana 05 exige las cabeceras `Usuario` y `Rol` (aspecto de auditoría) en `/api/productos`; las pruebas de integración se actualizaron para enviarlas.

## Conclusiones

1. Los perfiles de Spring separan la configuración de cada ambiente sin duplicar ni modificar el código fuente.
2. Las variables de entorno permiten cambiar de base de datos, puerto o credenciales en el despliegue, y evitan guardar contraseñas en el repositorio.
3. Usar `validate` en producción protege el esquema real frente a cambios automáticos de Hibernate.
4. Un endpoint como `/configuracion` facilita comprobar rápidamente con qué ambiente se está trabajando.
5. Tener una base de datos por ambiente evita que las pruebas de QA o TEST alteren los datos de producción.
