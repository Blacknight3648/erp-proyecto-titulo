import React from 'react';
import { useOSState } from '../../../../hooks/useOSState';
import ListaOS from './views/ListaOS';
import CrearOS from './views/CrearOS';
import DetalleOS from './views/DetalleOS';

export default function OrdenServicio() {
    const state = useOSState();
    const { view } = state;

    return (
        <div className="min-h-screen bg-transparent">
            {view === 'list'   && <ListaOS {...state} />}
            {view === 'create' && <CrearOS {...state} />}
            {view === 'detail' && <DetalleOS {...state} />}
        </div>
    );
}
