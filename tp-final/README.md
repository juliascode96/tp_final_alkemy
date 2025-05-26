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