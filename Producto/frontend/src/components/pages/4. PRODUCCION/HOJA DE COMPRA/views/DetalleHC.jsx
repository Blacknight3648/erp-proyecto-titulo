import React from 'react';
import HCVisualizacion from '../components/HCVisualizacion';

export default function DetalleHC({ selectedHC, setSelectedHC, registros, setView, formatCLP }) {
    // Buscar la HC seleccionada
    const hcActual = registros?.find(r => r.id === selectedHC);

    return (
        <HCVisualizacion
            hcId={selectedHC}
            hc={hcActual}
            onBack={() => {
                setSelectedHC(null);
                setView('list');
            }}
            onEdit={() => {
                setView('form');
            }}
            formatCLP={formatCLP}
        />
    );
}
