import React from 'react';
import HCVisualizacion from '../components/HCVisualizacion';

export default function DetalleHC({
    selectedHC,
    setSelectedHC,
    registros,
    setView,
    formatCLP,
    aprobar,
    cerrar,
    onModificar,
}) {
    const hcActual = registros?.find(r => r.id === selectedHC);

    const handleBack = () => {
        setSelectedHC(null);
        setView('list');
    };

    const handleAprobar = async (idHC) => {
        await aprobar?.(idHC);
        handleBack();
    };

    const handleCerrar = async (idHC) => {
        await cerrar?.(idHC);
        handleBack();
    };

    return (
        <HCVisualizacion
            hc={hcActual}
            onBack={handleBack}
            onAprobar={aprobar ? handleAprobar : undefined}
            onCerrar={cerrar ? handleCerrar : undefined}
            onModificar={onModificar}
            formatCLP={formatCLP}
        />
    );
}
