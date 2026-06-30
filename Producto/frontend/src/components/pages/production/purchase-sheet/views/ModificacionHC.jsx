import React from 'react';
import HCModificacion from '../components/HCModificacion';

export default function ModificacionHC({
    selectedHC,
    setSelectedHC,
    registros,
    setView,
    formatCLP,
    consolidarOCLote,
}) {
    const hcActual = registros?.find(r => r.id === selectedHC);

    const handleBack = () => {
        setSelectedHC(null);
        setView('list');
    };

    return (
        <HCModificacion
            hc={hcActual}
            onBack={handleBack}
            onConsolidarLote={consolidarOCLote}
            formatCLP={formatCLP}
        />
    );
}
