import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import FirmaAprobacionModal from './FirmaAprobacionModal';

const base = {
    open: true,
    onConfirm: vi.fn(),
    onClose: vi.fn(),
};

describe('FirmaAprobacionModal', () => {
    it('no renderiza nada cuando open=false', () => {
        render(<FirmaAprobacionModal {...base} open={false} title="Firmar" />);
        expect(screen.queryByText('Firmar')).toBeNull();
    });

    it('deshabilita confirmar sin aprobador y lo habilita al escribirlo', async () => {
        render(<FirmaAprobacionModal {...base} defaultAprobador="" />);

        const confirmar = screen.getByRole('button', { name: /Confirmar/i });
        expect(confirmar).toBeDisabled();

        await userEvent.type(
            screen.getByPlaceholderText(/Nombre \/ código del aprobador/i),
            'tester'
        );
        expect(confirmar).toBeEnabled();
    });

    it('onConfirm recibe aprobador y observacion (trim)', async () => {
        const onConfirm = vi.fn();
        render(<FirmaAprobacionModal {...base} onConfirm={onConfirm} defaultAprobador="" />);

        await userEvent.type(screen.getByPlaceholderText(/Nombre \/ código del aprobador/i), '  ana  ');
        await userEvent.type(screen.getByPlaceholderText(/Opcional: notas para el historial/i), '  ok  ');
        await userEvent.click(screen.getByRole('button', { name: /Confirmar/i }));

        expect(onConfirm).toHaveBeenCalledWith({ aprobador: 'ana', observacion: 'ok' });
    });

    it('con requireObservacion exige el motivo', async () => {
        render(<FirmaAprobacionModal {...base} defaultAprobador="firmante" requireObservacion />);

        const confirmar = screen.getByRole('button', { name: /Confirmar/i });
        expect(confirmar).toBeDisabled();

        await userEvent.type(screen.getByPlaceholderText(/Indique el motivo/i), 'precio alto');
        expect(confirmar).toBeEnabled();
    });

    it('Cancelar dispara onClose', async () => {
        const onClose = vi.fn();
        render(<FirmaAprobacionModal {...base} onClose={onClose} defaultAprobador="x" />);
        await userEvent.click(screen.getByRole('button', { name: /Cancelar/i }));
        expect(onClose).toHaveBeenCalled();
    });
});
