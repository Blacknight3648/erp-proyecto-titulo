import React, { useState } from 'react';
import ListaEVN from './Views/ListaEVN';
import DetalleEVN from './Views/DetalleEVN';

/**
 * Componente Orquestador de Administracion de Negocios (EVN)
 * Encargado de gestionar la navegación entre la lista y el detalle.
 */
export default function AdministracionNegocios() {
    const [view, setView] = useState('list'); // 'list' | 'detail'
    const [selectedEval, setSelectedEval] = useState(null);
    const [mode, setMode] = useState('crear'); // 'crear' | 'modificar'
    const [isReadOnly, setIsReadOnly] = useState(false);

    const handleNueva = () => {
        setSelectedEval(null);
        setMode('crear');
        setIsReadOnly(false);
        setView('detail');
    };

    const handleEditar = (ev) => {
        setSelectedEval(ev);
        setMode('modificar');
        setIsReadOnly(false);
        setView('detail');
    };

    const handleVer = (ev) => {
        setSelectedEval(ev);
        setMode('modificar');
        setIsReadOnly(true);
        setView('detail');
    };

    const handleVolver = () => {
        setSelectedEval(null);
        setView('list');
    };

    return (
        <div className="administracion-negocios-container">
            {view === 'list' ? (
                <ListaEVN 
                    onNueva={handleNueva} 
                    onEditar={handleEditar} 
                    onVer={handleVer} 
                />
            ) : (
                <DetalleEVN 
                    mode={mode} 
                    initialEval={selectedEval} 
                    onBack={handleVolver} 
                    isReadOnly={isReadOnly}
                />
            )}
        </div>
    );
}
