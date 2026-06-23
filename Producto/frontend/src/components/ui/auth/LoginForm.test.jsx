import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import LoginForm from './LoginForm';

const renderForm = (props = {}) => {
    const onSubmit = vi.fn((e) => e?.preventDefault?.());
    render(
        <LoginForm
            email={props.email ?? ''}
            setEmail={vi.fn()}
            password={props.password ?? ''}
            setPassword={vi.fn()}
            onSubmit={onSubmit}
        />
    );
    return { onSubmit };
};

describe('LoginForm', () => {
    it('muestra errores de validación y no envía con campos vacíos', async () => {
        const { onSubmit } = renderForm({ email: '', password: '' });

        await userEvent.click(screen.getByRole('button', { name: /Iniciar Sesión/i }));

        expect(screen.getByText('Introduce un usuario o correo electrónico')).toBeInTheDocument();
        expect(screen.getByText('Introduce una contraseña')).toBeInTheDocument();
        expect(onSubmit).not.toHaveBeenCalled();
    });

    it('exige la contraseña cuando solo falta ella', async () => {
        const { onSubmit } = renderForm({ email: 'admin', password: '' });

        await userEvent.click(screen.getByRole('button', { name: /Iniciar Sesión/i }));

        expect(screen.getByText('Introduce una contraseña')).toBeInTheDocument();
        expect(onSubmit).not.toHaveBeenCalled();
    });

    it('invoca onSubmit cuando los campos son válidos', async () => {
        const { onSubmit } = renderForm({ email: 'admin', password: 'admin' });

        await userEvent.click(screen.getByRole('button', { name: /Iniciar Sesión/i }));

        expect(onSubmit).toHaveBeenCalledTimes(1);
    });
});
