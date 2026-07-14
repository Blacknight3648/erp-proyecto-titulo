import React from 'react';
import HCModificacion from './HCModificacion';

export default function ModificacionHC({
    selectedHC,
    setSelectedHC,
    registros,
    setView,
    formatCLP,
    consolidarOCLote,
    agregarItemManual,
    rechazarOC,
    reingresarOC,
    ocsById,
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
            onAgregarItemManual={agregarItemManual}
            onRechazarOC={rechazarOC}
            onReingresarOC={reingresarOC}
            ocsById={ocsById}
            formatCLP={formatCLP}
        />
    );
}
