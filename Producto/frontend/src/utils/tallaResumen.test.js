import { describe, it, expect } from 'vitest';
import { tallaResumen, calcularTotalCotizacion } from './tallaResumen';

describe('tallaResumen', () => {
    it('agrega cantidades y costos desde prendas, agrupando por talla', () => {
        const cot = {
            prendas: [
                { cantidad: 2, precio: 1000, talla: 'M' },
                { cantidad: 3, precio: 500, talla: 'M' },
                { qty: 1, precioUnitario: 200, talla: 'L' },
            ],
        };
        const r = tallaResumen(cot);
        expect(r.totalPrendas).toBe(6);
        expect(r.totalCosto).toBe(2 * 1000 + 3 * 500 + 1 * 200);
        expect(r.resumenTallas).toEqual({ M: 5, L: 1 });
    });

    it('usa items como fallback y talla N/A cuando falta', () => {
        const r = tallaResumen({ items: [{ total: 4, valorUnitario: 100 }] });
        expect(r.totalPrendas).toBe(4);
        expect(r.totalCosto).toBe(400);
        expect(r.resumenTallas).toEqual({ 'N/A': 4 });
    });

    it('devuelve ceros para entrada vacía o nula', () => {
        expect(tallaResumen(null)).toEqual({ totalPrendas: 0, totalCosto: 0, resumenTallas: {} });
        expect(tallaResumen({})).toEqual({ totalPrendas: 0, totalCosto: 0, resumenTallas: {} });
    });
});

describe('calcularTotalCotizacion', () => {
    it('prioriza el costo calculado desde las prendas', () => {
        const cot = { prendas: [{ cantidad: 2, precio: 1000, talla: 'M' }] };
        expect(calcularTotalCotizacion(cot)).toBe(2000);
    });

    it('cae a costoTotalCalculado.amount, costoTotalCalculado o monto', () => {
        expect(calcularTotalCotizacion({ costoTotalCalculado: { amount: 500 } })).toBe(500);
        expect(calcularTotalCotizacion({ costoTotalCalculado: 750 })).toBe(750);
        expect(calcularTotalCotizacion({ monto: 9999 })).toBe(9999);
        expect(calcularTotalCotizacion({})).toBe(0);
    });
});
