import { useMemo } from 'react';

// La(s) alerta(s) de atraso vienen calculadas por el backend (misma regla SLA que el
// dashboard operacional) en el arreglo `alertas` de OPResponse — una OP puede tener
// varias a la vez (ver DashboardOPService.evaluarAlertas / OrdenProduccionController).
export const useEstadoOP = (op) => {
    return useMemo(() => {
        const alertas = op?.alertas || [];
        if (alertas.length === 0) {
            return { estadoGlobal: 'FLUJO_NORMAL', etapaCritica: null, diasAtraso: 0, motivo: '', alertas: [] };
        }
        // Para el badge principal se muestra la más antigua (mayor días de atraso).
        const peor = alertas.reduce((a, b) => (b.diasAtraso > a.diasAtraso ? b : a));
        return {
            estadoGlobal: 'ATRASADA',
            etapaCritica: peor.etapaAtrasada,
            diasAtraso: peor.diasAtraso,
            motivo: peor.motivo,
            alertas,
        };
    }, [op]);
};
