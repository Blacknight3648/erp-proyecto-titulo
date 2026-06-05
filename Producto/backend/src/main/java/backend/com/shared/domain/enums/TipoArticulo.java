package backend.com.shared.domain.enums;

/**
 * Tipo de artículo – equivalente al campo 'type' en product.template de Odoo.
 * Determina qué tabla satélite (articulo_tela, articulo_prenda,
 * articulo_accesorio)
 * contiene los datos específicos del artículo.
 */
public enum TipoArticulo {
    TELA,
    PRENDA_LISTA,
    PRENDA_CONFECCIONAR,
    ACCESORIO
}
