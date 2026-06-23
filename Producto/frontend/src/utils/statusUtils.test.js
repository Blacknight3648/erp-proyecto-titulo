import { describe, it, expect } from 'vitest';
import { calculateSCStatus, calculateOPStatus } from './statusUtils';

// Fecha ISO a N días de hoy (negativo = pasado).
const isoDias = (delta) => {
    const d = new Date();
    d.setDate(d.getDate() + delta);
    return d.toISOString().slice(0, 10);
};

describe('calculateSCStatus', () => {
    it('sin SC → FLUJO_NORMAL', () => {
        expect(calculateSCStatus(null).estadoGlobal).toBe('FLUJO_NORMAL');
    });

    it('marca ATRASADA cuando pasaron > límite días sin emitir OC', () => {
        const r = calculateSCStatus({ fechaCreacion: isoDias(-10) });
        expect(r.estadoGlobal).toBe('ATRASADA');
        expect(r.etapaCritica).toBe('REQ.');
        expect(r.diasAtraso).toBeGreaterThan(3);
    });

    it('flujo normal si recién creada y sin OC', () => {
        const r = calculateSCStatus({ fechaCreacion: isoDias(0) });
        expect(r.estadoGlobal).toBe('FLUJO_NORMAL');
    });

    it('marca ATRASADA por retraso en recepción tras emitir OC', () => {
        const r = calculateSCStatus({
            fechaCreacion: isoDias(-20),
            fechaOcEmitida: isoDias(-10),
        });
        expect(r.estadoGlobal).toBe('ATRASADA');
        expect(r.etapaCritica).toBe('EN RECEP.');
    });
});

describe('calculateOPStatus', () => {
    it('sin OP → FLUJO_NORMAL', () => {
        expect(calculateOPStatus(null).estadoGlobal).toBe('FLUJO_NORMAL');
    });

    it('marca ATRASADA cuando la etapa actual excede su SLA', () => {
        const op = { id: 1, estado: 'Corte', fechaTizado: isoDias(-10) };
        const r = calculateOPStatus(op, [], [], []);
        expect(r.estadoGlobal).toBe('ATRASADA');
        expect(r.etapaCritica).toBe('CORTE');
    });

    it('flujo normal cuando la etapa está dentro del SLA', () => {
        const op = { id: 1, estado: 'Corte', fechaTizado: isoDias(0) };
        const r = calculateOPStatus(op, [], [], []);
        expect(r.estadoGlobal).toBe('FLUJO_NORMAL');
    });
});
