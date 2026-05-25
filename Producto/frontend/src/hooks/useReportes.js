import { useCallback, useEffect, useState } from 'react';
import { ReportesService } from '../remote/service/ReportesService';

export function useReportes() {
    const [hcsPendientes, setHcsPendientes] = useState([]);
    const [ocsPendientes, setOcsPendientes] = useState([]);
    const [ossEnTaller, setOssEnTaller] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const refresh = useCallback(async () => {
        setLoading(true);
        setError(null);
        try {
            const [hcs, ocs, oss] = await Promise.all([
                ReportesService.hcsPendientesAprobacion(),
                ReportesService.ocsPendientesRecepcion(),
                ReportesService.ossEnTaller(),
            ]);
            setHcsPendientes(hcs);
            setOcsPendientes(ocs);
            setOssEnTaller(oss);
        } catch (e) {
            setError(e?.response?.data?.message || e?.message || 'Error cargando reportes');
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => { refresh(); }, [refresh]);

    const formatCLP = useCallback((value) => {
        return new Intl.NumberFormat('es-CL', {
            style: 'currency',
            currency: 'CLP',
            minimumFractionDigits: 0,
        }).format(Number(value) || 0);
    }, []);

    return { hcsPendientes, ocsPendientes, ossEnTaller, loading, error, refresh, formatCLP };
}
