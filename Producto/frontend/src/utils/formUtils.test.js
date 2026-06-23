import { describe, it, expect } from 'vitest';
import { parseId } from './formUtils';

describe('parseId', () => {
    it('devuelve el mismo número si ya es numérico', () => {
        expect(parseId(26001)).toBe(26001);
    });

    it('extrae el número de un string con prefijo', () => {
        expect(parseId('SCOS-26001')).toBe(26001);
        expect(parseId('EVN-000002')).toBe(2);
    });

    it('devuelve null para vacío, null o sin dígitos', () => {
        expect(parseId(null)).toBeNull();
        expect(parseId('')).toBeNull();
        expect(parseId(undefined)).toBeNull();
        expect(parseId('sin-numeros')).toBeNull();
    });
});
