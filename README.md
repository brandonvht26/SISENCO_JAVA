SISENCO - Sistema de Gestión de Entregas y Encomiendas
SISENCO es una aplicación de escritorio robusta, desarrollada en Java, diseñada para la gestión integral del ciclo de vida de paquetes en una empresa de logística. El sistema conecta una interfaz de usuario intuitiva construida con Swing a una base de datos profesional Microsoft SQL Server desplegada en la nube (Azure), aplicando principios de Programación Orientada a Objetos y patrones de diseño de software.

✨ Características Principales
El sistema está diseñado con funcionalidades específicas basadas en roles para garantizar la seguridad y la eficiencia operativa.

Rol: Cajero-Despachador
Gestión de Clientes: CRUD completo (Crear, Leer, Actualizar, Eliminar) de los clientes remitentes.

Registro de Encomiendas: Creación de nuevos registros de paquetes, asociando remitente, destinatario y detalles del envío.

Actualización de Estado: Modificación del estado de una encomienda (PENDIENTE, ENVIADO, EN TRÁNSITO, etc.) a través de una interfaz intuitiva.

Trazabilidad: Visualización del historial completo de cambios de estado para cualquier paquete seleccionado.

Rol: Administrador
Todas las funcionalidades del Cajero-Despachador.

Gestión de Usuarios: Creación de nuevas cuentas de usuario y perfiles de empleado para el personal del sistema.

Generación de Reportes: Creación de reportes de encomiendas filtrados por rango de fechas y estado.

Exportación de Datos: Capacidad para exportar los reportes generados a un archivo de Microsoft Excel (.xlsx).

🛠️ Tecnologías y Herramientas
Categoría

Tecnología / Herramienta

Lenguaje

Java (JDK 21 LTS)

Base de Datos

Microsoft SQL Server

Plataforma en la Nube

Azure SQL Database

Interfaz Gráfica

Java Swing

Diseño UI

FlatLaf (Look and Feel moderno)

Gestión de Proyecto

Apache Maven

Librerías Clave

mssql-jdbc, poi-ooxml

IDE

IntelliJ IDEA

Control de Versiones

Git & GitHub

🚀 Cómo Empezar
Para ejecutar este proyecto en tu entorno local, sigue estos pasos:

Prerrequisitos
Tener instalado un JDK 21 (LTS), preferiblemente Temurin.

Tener instalado Apache Maven.

Tener acceso a una instancia de SQL Server (local o en Azure).

1. Configuración de la Base de Datos
Ejecuta el script SQL completo que se encuentra en /src/main/resources/database/sisenco_script.sql en tu instancia de SQL Server. Esto creará todas las tablas, procedimientos almacenados, triggers y datos iniciales necesarios.

2. Clonar el Repositorio
git clone https://github.com/brandonvht26/SISENCO_JAVA.git
cd SISENCO_JAVA

3. Configurar la Conexión
Abre el proyecto en IntelliJ IDEA.

Navega a la clase com.sisenco.db.DatabaseConnection.java.

Actualiza las constantes SERVER_NAME, DATABASE_NAME, USER y PASSWORD con las credenciales de tu propia base de datos en Azure.

4. Ejecutar la Aplicación
Espera a que Maven descargue todas las dependencias.

Localiza la clase com.sisenco.app.Main.java y ejecuta el método main().

🏗️ Arquitectura del Software
El proyecto sigue una arquitectura limpia y organizada para facilitar la mantenibilidad:

com.sisenco.app: Punto de entrada de la aplicación.

com.sisenco.db: Encapsula la lógica de conexión a la base de datos.

com.sisenco.model: Contiene las clases que representan las entidades del negocio (Ej: Cliente, Encomienda).

com.sisenco.dao: Implementa el patrón Data Access Object (DAO) para separar la lógica de negocio del acceso a datos.

com.sisenco.ui: Contiene todas las clases de la interfaz gráfica.

com.sisenco.util: Clases de utilidad, como el ExcelExporter.

📝 Estado del Proyecto
El proyecto está funcionalmente completo. Todas las características descritas han sido implementadas y probadas.

Nota sobre el Ejecutable: Durante la fase final de empaquetado, se encontraron problemas de compatibilidad persistentes entre el diseñador visual de Swing en IntelliJ (.form) y la creación de un ejecutable final (.exe). Se determinó que la causa raíz es un "enlace" frágil que se rompe fuera del IDE. Como solución robusta, las interfaces más críticas fueron refactorizadas a código puro. El proyecto se entrega como un archivo JAR funcional, y el proceso de depuración ha sido documentado como una lección clave del desarrollo.

👨‍💻 Autor
Brandon Huera - brandonvht26
