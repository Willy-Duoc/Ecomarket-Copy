# 🛒 EcoMarket — Plataforma de Comercio Electrónico

Sistema de e-commerce desarrollado con arquitectura de **microservicios** en Spring Boot. Cada servicio es independiente, se comunica mediante REST y gestiona su propia base de datos MySQL.

---

## 📦 Microservicios

| Servicio | Carpeta | Puerto | Base de datos |
|---|---|---|---|
| Registro de usuarios | `registro-service` | 8081 | `usuarios_db` |
| Inicio de sesión (JWT) | `iniciosesion-service` | 8086 | `iniciosesion_db` |
| Carrito de compra | `Ecomarket-Carrito-de-compra` | 8082 | `ecomarket_carrito` |
| Catálogo e inventario | `Ecomarket-Catalogo-Inventario` | 8087 | `ecomarket_catalogo` |
| Proceso de pago | `service-pago` | 8083 | `proceso_pago_db` |
| Envíos y logística | `envioservice` | 8083 | `envio_db` |
| Soporte y notificaciones | `soporteservice` | 8081 | `soporte_db` |
| Gestión de tienda | `gestion-tienda-service` | 8090 | `tienda_db` |
| Analítica y reportes | `analiticaservice` | 8084 | `analitica_db` |

---

## 🛠️ Tecnologías

- **Java 21**
- **Spring Boot 4.0.6**
- **Spring Data JPA / Hibernate**
- **MySQL** (via XAMPP)
- **JWT** para autenticación
- **BCrypt** para encriptación de contraseñas
- **RestTemplate** para comunicación entre servicios
- **Maven**

---

## ⚙️ Requisitos previos

- Java 21+
- Maven 3.8+
- XAMPP con MySQL activo (puerto 3306)
- Git

---

## 🚀 Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/hoxtxnDev/ECOMARKET-REPO.git
cd ECOMARKET-REPO
```

### 2. Crear las bases de datos en MySQL

Abre phpMyAdmin o tu cliente MySQL y ejecuta:

```sql
CREATE DATABASE usuarios_db;
CREATE DATABASE iniciosesion_db;
CREATE DATABASE ecomarket_carrito;
CREATE DATABASE ecomarket_catalogo;
CREATE DATABASE proceso_pago_db;
CREATE DATABASE envio_db;
CREATE DATABASE soporte_db;
CREATE DATABASE tienda_db;
CREATE DATABASE analitica_db;
```

### 3. Configurar credenciales (si tu MySQL tiene contraseña)

En cada `src/main/resources/application.properties` ajusta:

```properties
spring.datasource.username=root
spring.datasource.password=tu_contraseña
```

### 4. Levantar cada microservicio

Entra a la carpeta de cada servicio y ejecuta:

```bash
cd registro-service
mvn spring-boot:run
```

Repite para cada uno. El orden recomendado es:

1. `registro-service`
2. `iniciosesion-service`
3. `Ecomarket-Catalogo-Inventario`
4. `Ecomarket-Carrito-de-compra`
5. `service-pago`
6. `envioservice`
7. `soporteservice`
8. `gestion-tienda-service`
9. `analiticaservice`

---

## 🔗 Comunicación entre servicios

Los servicios se llaman entre sí mediante HTTP usando `RestTemplate`. Las URLs están configuradas en cada `application.properties` bajo las claves `microservicio.*.url`.

```
registro-service (8081) ←──── iniciosesion-service (8086)
        │
        ▼
carrito-service (8082) ──→ catalogo-inventario (8087)
        │                         │
        ▼                         ▼
  service-pago (8083)    gestion-tienda (8090)
        │
        ▼
  envioservice (8083)
        │
        ├──→ soporteservice (8081)
        └──→ analiticaservice (8084)
```

---

## 📁 Estructura del proyecto

```
ECOMARKET-REPO/
├── registro-service/
├── iniciosesion-service/
├── Ecomarket-Carrito-de-compra/
├── Ecomarket-Catalogo-Inventario/
├── service-pago/
├── envioservice/
├── soporteservice/
├── gestion-tienda-service/
└── analiticaservice/
```

Cada microservicio sigue la estructura estándar de Spring Boot:

```
servicio/
├── src/main/java/com/ecomarket/*/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── model/
│   ├── dto/
│   └── client/
└── src/main/resources/
    └── application.properties
```

---

## 👥 Autores

Proyecto académico desarrollado para el ramo de Ingeniería de Software / Desarrollo de Sistemas.
