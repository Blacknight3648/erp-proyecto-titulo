import { describe, it, expect } from 'vitest';
import { construirFichaPlantillas } from './useCosteosOPState';

describe('construirFichaPlantillas', () => {
    it('reconstruye la ficha desde descripciones con campos como props directas', () => {
        const record = {
            id: 7,
            nombrePrenda: 'CHAQUETA',
            descripciones: [
                {
                    nombreCampo: 'forro',
                    valorDescripcion: 'polar',
                    vinculos: [{ id: 1, materialType: 'TELA', materialId: 10, cantidad: 2 }],
                },
                { nombreCampo: 'cuello', valorDescripcion: 'redondo' },
            ],
            telas: [{ id: 10, aplicacion: 'Cuerpo', nombre: 'Polar', peso: 200, unidadMedida: 'GR' }],
            accesorios: [{ id: 20, tipo: 'Cierre', nombreAccesorio: 'YKK', cantidad: 1 }],
            logotipos: [],
        };

        const [p] = construirFichaPlantillas(record);

        expect(p.nombrePrenda).toBe('CHAQUETA');
        expect(p.forro).toBe('polar');
        expect(p.cuello).toBe('redondo');
        expect(p.telas[0].nombre).toBe('Polar');
        expect(p.accesorios[0].nombreAccesorio).toBe('YKK');
        expect(p.vinculos).toHaveLength(1);
        expect(p.vinculos[0]).toMatchObject({
            fieldName: 'forro',
            materialType: 'TELA',
            materialId: 10,
            cantidad: 2,
        });
    });

    it('devuelve [] cuando no hay descripciones', () => {
        expect(construirFichaPlantillas({ id: 1, descripciones: [] })).toEqual([]);
        expect(construirFichaPlantillas({ id: 1 })).toEqual([]);
    });

    it('devuelve [] para record nulo', () => {
        expect(construirFichaPlantillas(null)).toEqual([]);
    });

    it('respeta plantillas ya provistas (compatibilidad)', () => {
        const plantillas = [{ id: 99, nombrePrenda: 'YA-EXISTE' }];
        expect(construirFichaPlantillas({ plantillas })).toBe(plantillas);
    });
});
