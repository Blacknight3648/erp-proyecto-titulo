import { useState, useCallback } from 'react';
import ListaEVN from './ListaEVN';
import DetalleEVN from './DetalleEVN';

export default function AdministracionNegocios() {
    const [view, setView] = useState('list');
    const [selectedEval, setSelectedEval] = useState(null);
    const [mode, setMode] = useState('create'); // 'create' | 'edit' | 'view'
    const [listKey, setListKey] = useState(0); // forces ListaEVN to reload

    const handleAbrir = useCallback((ev = null, modeParam = 'create') => {
        setSelectedEval(ev);
        setMode(modeParam);
        setView('detail');
    }, []);

    const handleVolver = useCallback((shouldRefresh = false) => {
        setSelectedEval(null);
        setView('list');
        if (shouldRefresh) {
            setListKey(prev => prev + 1); // remount ListaEVN → triggers loadEvaluations()
        }
    }, []);

    return (
        <div>
            {view === 'list' ? (
                <ListaEVN
                    key={listKey}
                    onNueva={() => handleAbrir(null, 'create')}
                    onEditar={(ev) => handleAbrir(ev, 'edit')}
                    onVer={(ev) => handleAbrir(ev, 'view')}
                />
            ) : (
                <DetalleEVN
                    initialEval={selectedEval}
                    onBack={handleVolver}
                    mode={mode}
                />
            )}
        </div>
    );
}
