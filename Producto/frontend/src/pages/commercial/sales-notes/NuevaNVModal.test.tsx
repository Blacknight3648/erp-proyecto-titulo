import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import NuevaNVModal from './NuevaNVModal';

const evns = [
    { evaluacionNegocioId: 1, numero: 'EVN-000001', clienteNombre: 'Cliente Uno', referencia: 'Ref A', items: [{}, {}] },
    { evaluacionNegocioId: 2, numero: 'EVN-000002', clienteNombre: 'Cliente Dos', referencia: 'Ref B', items: [] },
];

const baseProps = {
    open: true,
    onClose: vi.fn(),
    onDesdeCero: vi.fn(),
    onSelectEVN: vi.fn(),
    evaluaciones: evns,
};

describe('NuevaNVModal', () => {
    it('no renderiza nada cuando open=false', () => {
        render(<NuevaNVModal {...baseProps} open={false} />);
        expect(screen.queryByText('Nueva Nota de Venta')).toBeNull();
    });

    it('"Crear desde cero" dispara onDesdeCero', async () => {
        const onDesdeCero = vi.fn();
        render(<NuevaNVModal {...baseProps} onDesdeCero={onDesdeCero} />);
        await userEvent.click(screen.getByText('Crear desde cero'));
        expect(onDesdeCero).toHaveBeenCalledTimes(1);
    });

    it('deshabilita la opción plantilla y muestra leyenda si no hay EVN', () => {
        render(<NuevaNVModal {...baseProps} evaluaciones={[]} />);
        const btn = screen.getByText('Desde plantilla EVN').closest('button');
        expect(btn).toBeDisabled();
        expect(screen.getByText(/No hay evaluaciones adjudicadas disponibles/i)).toBeInTheDocument();
    });

    it('al elegir plantilla lista las EVN y seleccionar una dispara onSelectEVN', async () => {
        const onSelectEVN = vi.fn();
        render(<NuevaNVModal {...baseProps} onSelectEVN={onSelectEVN} />);

        await userEvent.click(screen.getByText('Desde plantilla EVN'));

        // Cambió al paso de selección
        expect(screen.getByText('Elegir Evaluación de Negocio')).toBeInTheDocument();
        expect(screen.getByText('Cliente Uno')).toBeInTheDocument();
        expect(screen.getByText('Cliente Dos')).toBeInTheDocument();

        await userEvent.click(screen.getByText('Cliente Uno'));
        expect(onSelectEVN).toHaveBeenCalledWith(evns[0]);
    });

    it('el buscador filtra la lista de EVN', async () => {
        render(<NuevaNVModal {...baseProps} />);
        await userEvent.click(screen.getByText('Desde plantilla EVN'));

        await userEvent.type(
            screen.getByPlaceholderText(/Buscar por número, cliente o referencia/i),
            'Dos'
        );

        expect(screen.queryByText('Cliente Uno')).toBeNull();
        expect(screen.getByText('Cliente Dos')).toBeInTheDocument();
    });

    it('botón cerrar (X) dispara onClose', async () => {
        const onClose = vi.fn();
        render(<NuevaNVModal {...baseProps} onClose={onClose} />);
        await userEvent.click(screen.getByLabelText('Cerrar'));
        expect(onClose).toHaveBeenCalled();
    });
});
