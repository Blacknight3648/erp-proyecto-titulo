-- =============================================================================
-- V36: Versionado (snapshot) de Orden de Compra al rechazar
-- =============================================================================
-- Congela la OC (cabecera + ítems) justo antes de rechazarse, análogo a
-- produccion_costeo_versiones/produccion_costeo_item_versiones (ver V18).
-- Independiente de la columna `version` (contador simple, ya agregada en V33)
-- y del log textual de historial de estado.
--
-- En dev/test (H2) y en PROD, Hibernate (ddl-auto=update) puede haber creado ya
-- estas tablas desde las entidades OrdenCompraVersionJpaEntity/
-- OrdenCompraItemVersionJpaEntity según el historial de despliegues — se usa
-- CREATE TABLE IF NOT EXISTS (MySQL lo soporta nativamente) para cubrir ambos
-- casos sin fallar con "tabla ya existe".
-- =============================================================================

CREATE TABLE IF NOT EXISTS produccion_oc_versiones (
    id_oc_version    BIGINT AUTO_INCREMENT PRIMARY KEY,
    oc_id            BIGINT NOT NULL,
    numero_version   INT NOT NULL,
    fecha_creacion   DATETIME NOT NULL,
    motivo_cambio    VARCHAR(500),
    usuario_creador  VARCHAR(150) NOT NULL,
    proveedor_id     BIGINT,
    fecha_entrega_estimada DATE,
    observaciones    VARCHAR(1000),
    total_neto       DECIMAL(14,2),
    CONSTRAINT uk_oc_version UNIQUE (oc_id, numero_version),
    CONSTRAINT fk_oc_version_oc FOREIGN KEY (oc_id)
        REFERENCES produccion_ordenes_compra (id_oc)
);

CREATE TABLE IF NOT EXISTS produccion_oc_item_versiones (
    id_oc_item_version BIGINT AUTO_INCREMENT PRIMARY KEY,
    oc_version_id      BIGINT NOT NULL,
    oc_item_id         BIGINT NOT NULL,
    tipo_insumo        VARCHAR(30) NOT NULL,
    articulo_id        INT,
    nombre_insumo      VARCHAR(255),
    cantidad_comprada  DECIMAL(12,2),
    precio_unitario    DECIMAL(12,2),
    subtotal           DECIMAL(14,2),
    CONSTRAINT fk_oc_item_version_oc_version FOREIGN KEY (oc_version_id)
        REFERENCES produccion_oc_versiones (id_oc_version)
);
