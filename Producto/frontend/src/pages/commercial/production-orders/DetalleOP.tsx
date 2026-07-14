import React from 'react';
import OPVisualizacion from './OPVisualizacion';

export default function DetalleOP({ selectedOP, setSelectedOP, registros, setView }) {
    // Buscar la OP seleccionada en la lista de registros
    const opActual = registros?.find(r => (r.idOP || r.id) === selectedOP);

    return (
        <OPVisualizacion
            opId={selectedOP}
            op={opActual}
            onBack={() => {
                setSelectedOP(null);
                setView('list');
            }}
            onEdit={() => {
                setView('form'); // FormularioOP detectará que tiene ID y mostrará OPModificacion
            }}
        />
    );
}
