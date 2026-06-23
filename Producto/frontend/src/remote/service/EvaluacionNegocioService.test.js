import { describe, it, expect, beforeEach, vi } from 'vitest';

// Mock del cliente axios usado por el servicio (misma ruta './api').
vi.mock('./api', () => ({
    api: { get: vi.fn(), post: vi.fn(), put: vi.fn(), patch: vi.fn() },
}));

import { api } from './api';
import { EvaluacionNegocioService } from './EvaluacionNegocioService';

beforeEach(() => {
    vi.clearAllMocks();
});

describe('EvaluacionNegocioService · acciones PATCH', () => {
    it('cerrar() llama al endpoint correcto con la firma y devuelve data', async () => {
        api.patch.mockResolvedValue({ data: { id: 5, estado: 'CERRADA' } });

        const res = await EvaluacionNegocioService.cerrar(5, 'tester', 'cierre');

        expect(api.patch).toHaveBeenCalledWith(
            '/comercial/evaluaciones-negocio/5/cerrar',
            { aprobador: 'tester', observacion: 'cierre' }
        );
        expect(res).toEqual({ id: 5, estado: 'CERRADA' });
    });

    it('adjudicar() envía aprobador y observacion', async () => {
        api.patch.mockResolvedValue({ data: { id: 3, estado: 'ADJUDICADA' } });

        const res = await EvaluacionNegocioService.adjudicar(3, 'juan', 'ok');

        expect(api.patch).toHaveBeenCalledWith(
            '/comercial/evaluaciones-negocio/3/adjudicar',
            { aprobador: 'juan', observacion: 'ok' }
        );
        expect(res.estado).toBe('ADJUDICADA');
    });

    it('aprobar() envía aprobador y observacion', async () => {
        api.patch.mockResolvedValue({ data: { id: 4, estado: 'APROBADA' } });

        await EvaluacionNegocioService.aprobar(4, 'ana', 'visto');

        expect(api.patch).toHaveBeenCalledWith(
            '/comercial/evaluaciones-negocio/4/aprobar',
            { aprobador: 'ana', observacion: 'visto' }
        );
    });

    it('rechazar() envía aprobador y motivo', async () => {
        api.patch.mockResolvedValue({ data: { id: 6, estado: 'RECHAZADA' } });

        await EvaluacionNegocioService.rechazar(6, 'ana', 'precio alto');

        expect(api.patch).toHaveBeenCalledWith(
            '/comercial/evaluaciones-negocio/6/rechazar',
            { aprobador: 'ana', motivo: 'precio alto' }
        );
    });

    it('propaga el error si la API falla', async () => {
        api.patch.mockRejectedValue(new Error('500'));
        await expect(EvaluacionNegocioService.cerrar(1, 'x', 'y')).rejects.toThrow('500');
    });
});
