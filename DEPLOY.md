# Guía de Despliegue Distribuido - AWS EC2

Esta guía detalla la configuración y los comandos necesarios para desplegar el ERP de forma segura utilizando dos servidores EC2 separados (uno público para el Frontend y uno privado para el Backend y la Base de Datos).

---

## 1. Topología de Red y Grupos de Seguridad (Security Groups)

### A. EC2 Pública (Frontend)
* **Propósito:** Aloja la interfaz web en React y actúa como proxy inverso hacia el backend privado.
* **Grupo de Seguridad (SG):**
  * **Entrada (Inbound):**
    * Puerto `80` (HTTP): Permitido desde cualquier origen (`0.0.0.0/0`).
    * Puerto `22` (SSH): Permitido desde tu IP local (o desde el corredor de GitHub Actions).
  * **Salida (Outbound):** Permitido todo (`0.0.0.0/0`).

### B. EC2 Privada (Backend + MySQL)
* **Propósito:** Aloja el servicio Spring Boot de la API y la base de datos MySQL de manera segura.
* **Grupo de Seguridad (SG):**
  * **Entrada (Inbound):**
    * Puerto `8050` (Spring Boot API): Permitido **únicamente** desde la IP privada o el Grupo de Seguridad de la **EC2 Pública**.
    * Puerto `22` (SSH): Permitido para saltar (SSH Jump) a través de la máquina pública.
    * Puerto `3306/3307` (MySQL): **Cerrado al exterior**. MySQL corre seguro en una red Docker interna y no debe exponerse a internet.
  * **Salida (Outbound):** Permitido todo.

---

## 2. Automatización del Despliegue con GitHub Actions

Gracias a la integración con el **AWS CLI** en el pipeline de GitHub Actions, **no necesitas actualizar las direcciones IP en GitHub cada vez que inicies el laboratorio**. El pipeline consultará la IP pública y privada actual en tiempo real utilizando los **Instance IDs** de tus servidores EC2.

### Configuración requerida en GitHub

En la sección **Settings > Secrets and variables > Actions** de tu repositorio de GitHub, debes configurar las siguientes variables:

#### Variables (Pestaña "Variables")
* **`EC2_PUBLIC_INSTANCE_ID`:** El ID de tu instancia de Frontend (ej. `i-0abcdef123456789a`).
* **`EC2_PRIVATE_INSTANCE_ID`:** El ID de tu instancia de Backend (ej. `i-0fedcba9876543210b`).
* **`EC2_PUBLIC_USERNAME`:** El usuario SSH del Frontend (ej. `ubuntu`).
* **`EC2_PRIVATE_USERNAME`:** El usuario SSH del Backend (ej. `ubuntu`).

#### Secretos (Pestaña "Secrets")
* **`EC2_SSH_KEY`:** Tu clave privada SSH completa (el contenido del archivo `.pem`).
* **`DOCKERHUB_USERNAME`:** Tu usuario de Docker Hub.
* **`DOCKERHUB_TOKEN`:** Tu Personal Access Token (PAT) de Docker Hub con permisos de escritura.
* **`DB_PASSWORD`:** Contraseña de base de datos para producción.
* **`MYSQL_ROOT_PASSWORD`:** Contraseña root de la base de datos (debe ser idéntica a `DB_PASSWORD`).
* **`AWS_ACCESS_KEY_ID`:** Clave de acceso temporal del laboratorio.
* **`AWS_SECRET_ACCESS_KEY`:** Clave secreta temporal del laboratorio.
* **`AWS_SESSION_TOKEN`:** Token de sesión temporal del laboratorio.

---

## 3. Comandos de Despliegue Manual (En caso de emergencia)

Si alguna vez necesitas levantar los servicios manualmente dentro de cada máquina:

### En la EC2 Pública (Frontend)
1. Conéctate por SSH y ve a la carpeta `~/app`.
2. El archivo `.env` en este host debe contener:
   ```env
   DOCKER_USER=blacknight3648
   BACKEND_API_URL=http://<IP_PRIVADA_BACKEND>:8050
   ```
3. Ejecuta:
   ```bash
   docker compose -f docker-compose.frontend.yml pull
   docker compose -f docker-compose.frontend.yml up -d --remove-orphans
   ```

### En la EC2 Privada (Backend + MySQL)
1. Conéctate por SSH y ve a la carpeta `~/app`.
2. El archivo `.env` en este host debe contener:
   ```env
   DOCKER_USER=blacknight3648
   DB_PASSWORD=tu_contraseña_segura
   MYSQL_ROOT_PASSWORD=tu_contraseña_segura
   ```
3. Ejecuta:
   ```bash
   docker compose -f docker-compose.backend.yml pull
   docker compose -f docker-compose.backend.yml up -d --remove-orphans
   ```

---

## 4. Comprobaciones de Salud (Healthchecks)
* En la máquina de backend, puedes ver si todo arrancó correctamente:
  ```bash
  docker ps
  ```
  *(El contenedor de mysql debería decir `healthy` tras unos segundos, y luego el de backend también quedará `healthy`).*
* En la máquina de frontend, Nginx procesará dinámicamente tu variable `${BACKEND_API_URL}` al arrancar y la mapeará a la configuración interna del servidor web.
