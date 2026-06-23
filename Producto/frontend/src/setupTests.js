// Configuración global de los tests (Vitest + jsdom).
// Habilita los matchers de jest-dom (toBeInTheDocument, toBeDisabled, ...) y
// limpia el DOM renderizado entre tests para mantenerlos aislados.
import '@testing-library/jest-dom';
import { cleanup } from '@testing-library/react';
import { afterEach } from 'vitest';

afterEach(() => {
    cleanup();
});
