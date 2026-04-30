/**
 * Calcula resumen de tallas y total de prendas
 * Compatible con estructuras:
 * - cot.prendas
 * - cot.items
 */

export const tallaResumen = (cot) => {
    const lista = cot?.prendas || cot?.items || [];
    let totalPrendas = 0;
    let totalCosto = 0;
    const resumenTallas = {};

    lista.forEach((item) => {
        const cantidad =
            Number(item?.cantidad) ||
            Number(item?.qty) ||
            Number(item?.total) ||
            0;

        const precio =
            Number(item?.precio) ||
            Number(item?.valorUnitario) ||
            Number(item?.precioUnitario) ||
            0;

        const talla = item?.talla || "N/A";

        totalPrendas += cantidad;
        totalCosto += cantidad * precio;

        if (!resumenTallas[talla]) {
            resumenTallas[talla] = 0;
        }

        resumenTallas[talla] += cantidad;
    });

    return {
        totalPrendas,
        totalCosto,
        resumenTallas
    };
};

/**
 * Calcula el monto total de una cotización
 * para mostrar en tablas
 */
export const calcularTotalCotizacion = (cot) => {
    const { totalCosto } = tallaResumen(cot);
    if (totalCosto > 0) return totalCosto;
    return (
        cot?.costoTotalCalculado?.amount ||
        cot?.costoTotalCalculado ||
        cot?.monto ||
        0
    );
};
