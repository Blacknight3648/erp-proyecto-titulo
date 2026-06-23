# Guía de Despliegue Distribuido - AWS EC2

Esta guía detalla la configuración y los comandos necesarios para desplegar el ERP de forma segura utilizando dos servidores EC2 separados (uno público para el Frontend y uno privado para el Backend y la Base de Datos).

---

## 1. Topología de Red y Grupos de Seguridad (Security Groups)

### A. EC2 Pública (Frontend)
* **Propósito:** Aloja la interfaz web en React y actúa como proxy inverso hacia el backend privado.
* **Grupo de Seguridad (SG):**
  * **Entrada (Inbound):**
    * Puerto `80` (HTTP): Permitido desde cualquier origen (`0.0.0.0/0`).
    * Puerto `22` (SSH): Permitido desde tu IP local (o desde el laboratorio).
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

## 2. Despliegue en la EC2 Pública (Frontend)

1. Crea la carpeta de la aplicación en el servidor:
   ```bash
   mkdir -p ~/app
   cd ~/app
   ```
2. Asegúrate de tener los archivos `docker-compose.frontend.yml` y un archivo local `.env`.
3. El archivo `.env` en la **EC2 Pública** debe contener:
   ```env
   DOCKER_USER=blacknight3648
   BACKEND_API_URL=http://<IP_PRIVADA_DE_TU_EC2_DE_BACKEND>:8050
   ```
4. Levanta el contenedor del frontend:
   ```bash
   docker compose -f docker-compose.frontend.yml pull
   docker compose -f docker-compose.frontend.yml up -d
   ```

---

## 3. Despliegue en la EC2 Privada (Backend + MySQL)

1. Crea la carpeta de la aplicación en el servidor:
   ```bash
   mkdir -p ~/app
   cd ~/app
   ```
2. Asegúrate de tener los archivos `docker-compose.backend.yml` y un archivo local `.env`.
3. El archivo `.env` en la **EC2 Privada** debe contener:
   ```env
   DOCKER_USER=blacknight3648
   DB_PASSWORD=tu_contraseña_segura
   MYSQL_ROOT_PASSWORD=tu_contraseña_segura
   ```
   *(Nota: `DB_PASSWORD` y `MYSQL_ROOT_PASSWORD` deben ser idénticos).*
4. Levanta el backend y la base de datos:
   ```bash
   docker compose -f docker-compose.backend.yml pull
   docker compose -f docker-compose.backend.yml up -d
   ```

---

## 4. Comprobaciones de Salud (Healthchecks)
* En la máquina de backend, puedes ver si todo arrancó correctamente:
  ```bash
  docker ps
  ```
  *(El contenedor de mysql debería decir `healthy` tras unos segundos, y luego el de backend también quedará `healthy`).*
* En la máquina de frontend, Nginx procesará dinámicamente tu variable `${BACKEND_API_URL}` al arrancar y la mapeará a la configuración interna del servidor web.
