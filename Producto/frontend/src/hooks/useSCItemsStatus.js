import { useMemo } from 'react';
import { mockComercial, mockAllOCs } from '../data/mockData';

/**
 * Hook to calculate the status of SC items by crossing requirements, OCs, and receptions.
 * @param {string} solicitudCompraId - The ID of the Supply Chain request
 */
export function useSCItemsStatus(solicitudCompraId) {
    const sc = useMemo(() => mockComercial.find(item => (item.idSC || item.id) === solicitudCompraId), [solicitudCompraId]);

    const stats = useMemo(() => {

        if (!sc) return null;

        const scIdValue = sc.idSC || sc.id;
        const linkedOCs = mockAllOCs.filter(oc => (oc.solicitudCompraId || oc.sc_id) === scIdValue);

        return sc.items.map(item => {
            // Find total quantity ordered for this item SKU/ID across all linked OCs
            const qtyOrdered = linkedOCs.reduce((acc, oc) => {
                const line = oc.lineas?.find(l => l.sku === item.sku || l.descripcion === item.descripcion);
                return acc + (line ? line.cantidad : 0);
            }, 0);

            // Find total quantity received for this item across all linked OCs
            const qtyReceived = linkedOCs.reduce((acc, oc) => {
                if (!oc.recepciones) return acc;
                const recForThisItem = oc.recepciones.reduce((rAcc, rec) => {
                    const recLine = rec.items?.find(ri => ri.sku === item.sku);
                    return rAcc + (recLine ? recLine.cantidad : 0);
                }, 0);
                return acc + recForThisItem;
            }, 0);

            const qtyReq = item.cantidad || item.cantidadRequerida || 0;
            const percentReceived = qtyReq > 0
                ? Math.min(100, Math.round((qtyReceived / qtyReq) * 100))
                : 0;

            let status = 'Sin OC';
            if (qtyOrdered > 0) status = 'OC Emitida';
            if (qtyReceived > 0) status = 'Parcial';
            if (qtyReceived >= qtyReq && qtyReq > 0) status = 'Completado';

            return {
                ...item,
                qtyOrdered,
                qtyReceived,
                percentReceived,
                status,
                ocs: linkedOCs
                    .filter(oc => oc.lineas?.some(l => l.sku === item.sku))
                    .map(oc => ({ id: oc.id, nro: oc.nro_orden, estado: oc.estado }))
            };
        });
    }, [sc, solicitudCompraId]);

    return stats;
}
