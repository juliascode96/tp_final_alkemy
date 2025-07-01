🛠 Proyecto: Sistema de Gestión de Stock de Productos

📌 Descripción
Este proyecto consiste en el desarrollo de una aplicación funcional en Java utilizando Spring Boot, que implementa un microservicio RESTful encargado de gestionar el stock de productos. La solución fue diseñada cumpliendo con los requisitos establecidos en la consigna de la cátedra, priorizando la escalabilidad, la seguridad y el rendimiento.

📋 Objetivos 

✅ Implementación de un microservicio en Java con Spring Boot.

✅ Persistencia de datos en una base de datos NoSQL (MongoDB).

✅ Seguridad mediante autenticación con JWT (JSON Web Tokens).

✅ Optimización del rendimiento utilizando JProfiler.

✅ Contenerización de la aplicación con Docker.

✅ Despliegue del sistema en WebSphere Application Server (WAS).

✅ Estructura de base de datos flexible y escalable, adaptada al manejo de productos con características variables.


🔧 Elección de tecnologías

🗄️ Base de datos: MongoDB
Para el desarrollo de esta aplicación, se eligió MongoDB como sistema de base de datos NoSQL. La decisión se basó en los siguientes puntos:

Integración nativa con Spring Boot mediante Spring Data MongoDB, lo cual facilita la implementación de repositorios, consultas y operaciones CRUD.

Modelo de datos flexible, ideal para representar productos con atributos variables (como talles, colores u opciones personalizadas).

Consultas más potentes en comparación con Firebase, incluyendo agregaciones y filtrados complejos, lo cual permite un mayor control sobre la lógica del negocio.

Posibilidad de ejecutar la base localmente o en un servicio gestionado (como MongoDB Atlas), brindando más opciones de despliegue y control de costos.

Aunque Firebase ofrece una solución completa y sencilla, especialmente para aplicaciones móviles o en tiempo real, se optó por MongoDB por su mejor compatibilidad con un backend desarrollado en Java y por su mayor capacidad de personalización.

🔐 Autenticación: JWT (JSON Web Tokens)
Para la autenticación de usuarios se optó por JWT en lugar de OAuth2. Esta elección se justifica por:

Simplicidad de implementación en una aplicación de backend autónoma.

JWT permite una autenticación sin estado (stateless), donde toda la información del usuario se incluye en el token, eliminando la necesidad de mantener sesiones del lado del servidor.

Control total sobre el flujo de autenticación, ideal en un proyecto donde se desarrolla tanto el backend como el frontend.

Integración sencilla con Spring Security, lo cual permite proteger rutas y aplicar control de acceso basado en roles.

OAuth2 es una excelente opción para aplicaciones que requieren autenticación delegada (como iniciar sesión con Google o GitHub), pero se consideró innecesariamente compleja para este caso de uso puntual.

### ⚙️ Análisis y optimización del rendimiento (VisualVM)

Como parte del proceso de mejora del rendimiento, se utilizó **VisualVM** para analizar el comportamiento de la aplicación en tiempo de ejecución. Esta herramienta permitió identificar posibles cuellos de botella relacionados con el uso de CPU, memoria y carga de objetos.

#### 🔍 Problemas detectados

- Uso excesivo de **`findAll()` y filtrado con Streams** en memoria, lo cual implica traer todos los productos desde la base de datos y aplicar filtros desde la lógica de negocio. Esto puede escalar mal en presencia de grandes volúmenes de datos.
- Instanciación manual de componentes (`Mapper`) dentro de las clases de servicio, en lugar de aprovechar la inyección de dependencias de Spring.

#### ✅ Acciones realizadas

- Se comenzaron a reemplazar los filtros manuales por métodos de repositorio específicos, delegando las operaciones a MongoDB para mejorar la eficiencia.

  Ejemplo:  
  En lugar de:
```java
  productoRepository.findAll().stream().filter(...);
```
Se propone:

```java
  productoRepository.findByStockGreaterThan(...);
```

- Se modificó el servicio para que reciba el mapper mediante **inyección de dependencias**, evitando instancias innecesarias y mejorando la testabilidad.
- Se implementó **paginación** (`Pageable`) en el endpoint de búsqueda de productos, permitiendo manejar grandes volúmenes de datos sin cargar todo en memoria.


#### 💡 Recomendaciones para futuras mejoras

- Implementar **paginación** (`Pageable`) en los endpoints que devuelven listas completas de productos **filtrados** para evitar el procesamiento masivo de datos en memoria.
- Reemplazar **ModelMapper** por **MapStruct**, una biblioteca que genera código de mapeo en tiempo de compilación, eliminando el uso de reflexión y mejorando significativamente el rendimiento.
- Analizar los endpoints más utilizados mediante **trazas de CPU en VisualVM** y detectar posibles mejoras adicionales en consultas, serialización de datos y uso de colecciones.

### 🐳 Contenerización con Docker
Para facilitar el despliegue y la portabilidad de la aplicación, se creó un **Dockerfile** que define cómo construir la imagen del microservicio. Este enfoque permite ejecutar la aplicación en cualquier entorno que soporte Docker, garantizando consistencia en las versiones y configuraciones.
### 📁 Archivos incluidos

- `Dockerfile`: define cómo se construye la imagen de la aplicación Spring Boot.
- `docker-compose.yml`: orquesta el despliegue de la aplicación junto con una base de datos MongoDB.

### ▶️ Pasos para ejecutar el proyecto

1. **Cloná el repositorio** (si aún no lo hiciste):

   ```bash
   git clone https://github.com/tu-usuario/tp-final.git
   cd tp-final
    ```
   
Asegurate de reemplazar los valores [user] y [password] en el archivo docker-compose.yml por tus credenciales reales de MongoDB Atlas.
2. **Construí la imagen de Docker**:
    ```bash
    docker build -t stock-management-app .
    ```
   
3. **Ejecutá el contenedor**:
    ```bash
    docker run -p 8080:8080 stock-management-app
    ```
4. **Accedé a la aplicación**: Abrí tu navegador y dirigite a `http://localhost:8080/api/productos` para interactuar con el microservicio.
5. **Configuración de la base de datos**: Asegurate de tener una instancia de MongoDB corriendo en `localhost:27017` o configurá la conexión en el archivo `application.properties` según sea necesario.
6. **Pruebas**: Podés utilizar herramientas como Postman o cURL para probar los endpoints del microservicio. Asegurate de incluir el token JWT en las cabeceras para acceder a los recursos protegidos.
7. **Documentación de la API**: La documentación de los endpoints está disponible en Swagger UI, accesible en `http://localhost:8080/swagger-ui.html`.
