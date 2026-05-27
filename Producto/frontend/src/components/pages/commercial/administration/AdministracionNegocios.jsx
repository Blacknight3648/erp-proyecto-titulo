import { useState } from 'react';
import ListaEVN from './Views/ListaEVN';
import DetalleEVN from './Views/DetalleEVN';

export default function AdministracionNegocios() {
    const [view, setView] = useState('list');
    const [selectedEval, setSelectedEval] = useState(null);
    const [isReadOnly, setIsReadOnly] = useState(false);

    const handleAbrir = (ev = null, readOnly = false) => {
        setSelectedEval(ev);
        setIsReadOnly(readOnly);
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
                    onNueva={() => handleAbrir(null, false)}
                    onEditar={(ev) => handleAbrir(ev, false)}
                    onVer={(ev) => handleAbrir(ev, true)}
                />
            ) : (
                <DetalleEVN
                    initialEval={selectedEval}
                    onBack={handleVolver}
                    isReadOnly={isReadOnly}
                />
            )}
        </div>
    );
}
