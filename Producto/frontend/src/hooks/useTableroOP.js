import { useState, useEffect } from 'react';
import { OrdenProduccionService } from '../remote/service/OrdenProduccionService';
import { mapSeguimientoToDetails } from '../utils/seguimientoAdapter';

/**
 * Datos reales para el Tablero de Seguimiento de OP: lista de Órdenes de
 * Producción con su alerta de atraso (calculada en el backend) y el detalle
 * de fechas por etapa (SeguimientoOPDTO) de cada una.
 */
export const useTableroOP = () => {
    const [ordenes, setOrdenes] = useState([]);
    const [seguimientoDetails, setSeguimientoDetails] = useState({});
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        let active = true;
        setLoading(true);
        OrdenProduccionService.getAll()
            .then(async (ops) => {
                const conSeguimiento = await Promise.all(
                    ops.map(async (op) => {
                        try {
                            const dto = await OrdenProduccionService.getSeguimiento(op.idOP);
                            return { op: { ...op, id: op.idOP, progreso: dto?.porcentajeAvance ?? 0 }, dto };
                        } catch {
                            return { op: { ...op, id: op.idOP, progreso: 0 }, dto: null };
                        }
                    })
                );
                if (!active) return;
                const nuevasOrdenes = conSeguimiento.map(r => r.op);
                const seguimientos = {};
                conSeguimiento.forEach(r => { if (r.dto) seguimientos[r.op.id] = r.dto; });
                setOrdenes(nuevasOrdenes);
                setSeguimientoDetails(mapSeguimientoToDetails(seguimientos));
            })
            .catch((err) => {
                console.error('Error cargando Órdenes de Producción para el tablero:', err);
            })
            .finally(() => {
                if (active) setLoading(false);
            });
        return () => { active = false; };
    }, []);

    return { ordenes, seguimientoDetails, loading };
};
