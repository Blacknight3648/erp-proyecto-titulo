-- ─────────────────────────────────────────────────────────────────────────
-- V7: Crear sistema nuevo de Plantillas, ModeloPlantilla, DescripcionPlantilla
-- y SCOSPlantillaMaterialVinculo (modelo normalizado).
--
-- Usa IF NOT EXISTS para ser idempotente cuando Hibernate (ddl-auto=update)
-- ya haya creado las tablas antes de que Flyway ejecute esta migración.
-- ─────────────────────────────────────────────────────────────────────────

-- Catálogo de campos posibles para describir una prenda
-- Ej: "Forro", "Mangas", "Capucha", "Cuello", "Bolsillos"
CREATE TABLE IF NOT EXISTS plantilla (
    id_plantilla BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_campo VARCHAR(100) NOT NULL UNIQUE
);

-- Configuración por defecto: qué campos sugiere cada Articulo
-- Es schema declarativo (no validador estricto).
CREATE TABLE IF NOT EXISTS modelo_plantilla (
    id_modelo_plantilla BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_articulo INT NOT NULL,
    id_plantilla BIGINT NOT NULL,
    CONSTRAINT fk_modelo_articulo FOREIGN KEY (id_articulo)
        REFERENCES articulo(id_articulo) ON DELETE CASCADE,
    CONSTRAINT fk_modelo_plantilla FOREIGN KEY (id_plantilla)
        REFERENCES plantilla(id_plantilla) ON DELETE CASCADE,
    CONSTRAINT uk_modelo_articulo_plantilla UNIQUE (id_articulo, id_plantilla)
);

-- Valor concreto que llena un campo de plantilla en una SolicitudCostos
-- Ej: SCOS=1, plantilla=Forro, valorDescripcion="polar"
CREATE TABLE IF NOT EXISTS descripcion_plantilla (
    id_descripcion_plantilla BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_scos BIGINT NOT NULL,
    id_plantilla BIGINT NOT NULL,
    valor_descripcion VARCHAR(500),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_descripcion_scos FOREIGN KEY (id_scos)
        REFERENCES solicitudes_costos(id_scos) ON DELETE CASCADE,
    CONSTRAINT fk_descripcion_plantilla FOREIGN KEY (id_plantilla)
        REFERENCES plantilla(id_plantilla)
);

CREATE INDEX idx_descripcion_scos ON descripcion_plantilla(id_scos);

-- Vínculos 1:N con DescripcionPlantilla
-- Permite asociar varios materiales (telas o accesorios) a un campo
CREATE TABLE IF NOT EXISTS scos_plantilla_material_vinculo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_descripcion_plantilla BIGINT NOT NULL,
    material_type VARCHAR(20) NOT NULL,   -- 'TELA' | 'ACCESORIO'
    material_id BIGINT NOT NULL,
    cantidad INT,
    CONSTRAINT fk_vinculo_descripcion FOREIGN KEY (id_descripcion_plantilla)
        REFERENCES descripcion_plantilla(id_descripcion_plantilla) ON DELETE CASCADE
);

CREATE INDEX idx_vinculo_descripcion ON scos_plantilla_material_vinculo(id_descripcion_plantilla);
