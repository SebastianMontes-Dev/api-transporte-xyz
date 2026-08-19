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

Toda peticion requiere autenticacion basica. No hay endpoints publicos.

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

    curl -u admin:admin123 -H "Content-Type: application/json" ^
      -d "{\"placa\":\"ABC123\",\"tipoVehiculo\":\"Furgon refrigerado\"}" ^
      http://localhost:8080/api/camiones

Registrar un conductor:

    curl -u admin:admin123 -H "Content-Type: application/json" ^
      -d "{\"nombre\":\"Juan Perez\",\"documento\":\"1020304050\",\"licencia\":\"C2-889977\"}" ^
      http://localhost:8080/api/conductores

Asociar el conductor 1 al camion 1:

    curl -u supervisor:supervisor123 -X PUT -H "Content-Type: application/json" ^
      -d "{\"conductorId\":1}" ^
      http://localhost:8080/api/camiones/1/conductor

Listar camiones:

    curl -u supervisor:supervisor123 http://localhost:8080/api/camiones

## Codigos de respuesta

- 200 consulta o asignacion correcta
- 201 registro creado
- 400 datos invalidos
- 401 sin credenciales o credenciales incorrectas
- 403 el rol no tiene permiso para esa operacion
- 404 camion o conductor inexistente
- 409 placa o documento repetido, o conductor ya asignado a otro camion
