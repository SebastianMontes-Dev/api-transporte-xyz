# API Rest de transporte XYZ

Gestion de camiones y conductores para la empresa de transporte de alimentos XYZ.

## Requisitos

- Java 21
- Maven 3.9

## Ejecucion

    mvn spring-boot:run

La aplicacion queda en http://localhost:8080. La base de datos es H2 en memoria,
los datos se pierden al detener la aplicacion.

## Usuarios

Definidos en src/main/resources/application.properties.

| Usuario    | Clave           | Rol           |
|------------|-----------------|---------------|
| admin      | admin123        | ADMINISTRADOR |
| supervisor | supervisor123   | SUPERVISOR    |

## Autenticacion (JWT)

Toda peticion (salvo `/api/auth/login`) requiere un token JWT en la cabecera
`Authorization: Bearer <token>`. El token se obtiene autenticandose con
usuario y clave:

    curl -X POST http://localhost:8080/api/auth/login ^
      -H "Content-Type: application/json" ^
      -d "{\"usuario\":\"admin\",\"clave\":\"admin123\"}"

La respuesta incluye el token:

    {"token":"eyJhbGciOiJIUzUxMiJ9...", "tipo":"Bearer"}

El token expira segun `seguridad.jwt.expiracion-ms` (1 hora por defecto).
La clave de firma (`seguridad.jwt.clave-secreta`) debe sobreescribirse en
produccion.

## Endpoints

| Metodo | Ruta                          | Rol permitido        | Descripcion                     |
|--------|-------------------------------|----------------------|---------------------------------|
| POST   | /api/conductores              | ADMINISTRADOR        | Registra un conductor           |
| GET    | /api/conductores              | ADMINISTRADOR, SUPERVISOR | Lista los conductores      |
| POST   | /api/camiones                 | ADMINISTRADOR        | Registra un camion              |
| GET    | /api/camiones                 | ADMINISTRADOR, SUPERVISOR | Lista los camiones         |
| GET    | /api/camiones/{id}            | ADMINISTRADOR, SUPERVISOR | Consulta un camion         |
| PUT    | /api/camiones/{id}/conductor  | SUPERVISOR           | Asocia un conductor al camion   |

Un conductor solo puede estar asignado a un camion a la vez.

## Ejemplos

Registrar un camion:

    curl -H "Authorization: Bearer <token-admin>" -H "Content-Type: application/json" ^
      -d "{\"placa\":\"ABC123\",\"tipoVehiculo\":\"Furgon refrigerado\"}" ^
      http://localhost:8080/api/camiones

Registrar un conductor:

    curl -H "Authorization: Bearer <token-admin>" -H "Content-Type: application/json" ^
      -d "{\"nombre\":\"Juan Perez\",\"documento\":\"1020304050\",\"licencia\":\"C2-889977\"}" ^
      http://localhost:8080/api/conductores

Asociar el conductor 1 al camion 1:

    curl -H "Authorization: Bearer <token-supervisor>" -X PUT -H "Content-Type: application/json" ^
      -d "{\"conductorId\":1}" ^
      http://localhost:8080/api/camiones/1/conductor

Listar camiones:

    curl -H "Authorization: Bearer <token-supervisor>" http://localhost:8080/api/camiones

## Codigos de respuesta

- 200 consulta o asignacion correcta
- 201 registro creado
- 400 datos invalidos
- 401 sin token, token invalido/expirado, o credenciales incorrectas en el login
- 403 el rol no tiene permiso para esa operacion
- 404 camion o conductor inexistente
- 409 placa o documento repetido, o conductor ya asignado a otro camion
