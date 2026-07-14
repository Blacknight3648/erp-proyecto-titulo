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
    onSelectEVN: vi.fn(),
    evaluaciones: evns,
};

describe('NuevaNVModal', () => {
    it('no renderiza nada cuando open=false', () => {
        render(<NuevaNVModal {...baseProps} open={false} />);
        expect(screen.queryByText('Elegir Evaluación de Negocio')).toBeNull();
    });

    it('abre directo en la lista de EVN adjudicadas y seleccionar una dispara onSelectEVN', async () => {
        const onSelectEVN = vi.fn();
        render(<NuevaNVModal {...baseProps} onSelectEVN={onSelectEVN} />);

        expect(screen.getByText('Elegir Evaluación de Negocio')).toBeInTheDocument();
        expect(screen.getByText('Cliente Uno')).toBeInTheDocument();
        expect(screen.getByText('Cliente Dos')).toBeInTheDocument();

        await userEvent.click(screen.getByText('Cliente Uno'));
        expect(onSelectEVN).toHaveBeenCalledWith(evns[0]);
    });

    it('muestra leyenda bloqueante si no hay EVN adjudicadas', () => {
        render(<NuevaNVModal {...baseProps} evaluaciones={[]} />);
        expect(screen.getByText(/No hay evaluaciones adjudicadas disponibles/i)).toBeInTheDocument();
        expect(screen.getByText(/Toda Nota de Venta debe originarse desde una Evaluación de Negocio adjudicada/i)).toBeInTheDocument();
    });

    it('el buscador filtra la lista de EVN', async () => {
        render(<NuevaNVModal {...baseProps} />);

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
