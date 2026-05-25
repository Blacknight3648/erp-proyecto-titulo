import { useCallback, useState } from 'react';
import { TrazabilidadService } from '../remote/service/TrazabilidadService';

export function useTrazabilidad() {
    const [opIdInput, setOpIdInput] = useState('');
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const buscar = useCallback(async (opId) => {
        const id = Number(opId ?? opIdInput);
        if (!Number.isFinite(id) || id <= 0) {
            setError('Ingresa un opId válido');
            return;
        }
        setLoading(true);
        setError(null);
        setData(null);
        try {
            const result = await TrazabilidadService.obtenerPorOP(id);
            if (!result) {
                setError(`No se encontró OP #${id}`);
            } else {
                setData(result);
            }
        } catch (e) {
            setError(e?.response?.data?.message || e?.message || 'Error en trazabilidad');
        } finally {
            setLoading(false);
        }
    }, [opIdInput]);

    const limpiar = useCallback(() => {
        setData(null);
        setError(null);
        setOpIdInput('');
    }, []);

    const formatCLP = useCallback((value) => {
        return new Intl.NumberFormat('es-CL', {
            style: 'currency',
            currency: 'CLP',
            minimumFractionDigits: 0,
        }).format(Number(value) || 0);
    }, []);

    return { opIdInput, setOpIdInput, data, loading, error, buscar, limpiar, formatCLP };
}
