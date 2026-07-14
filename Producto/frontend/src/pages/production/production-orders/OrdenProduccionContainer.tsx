import { useState } from 'react';
import { useLocation } from 'react-router-dom';
import OrdenProduccionList from './OrdenProduccionList';
import OrdenProduccionDetail from './OrdenProduccionDetail';

export default function OrdenProduccionContainer() {
    const location = useLocation();
    // Permite deep-linking desde otras pantallas (ej. Dashboard OP) pasando { state: { opId } }
    const [view, setView] = useState(location.state?.opId ? 'detail' : 'list');
    const [selectedOpId, setSelectedOpId] = useState(location.state?.opId ?? null);

    const handleSelectOp = (id) => {
        setSelectedOpId(id);
        setView('detail');
    };

    const handleBack = () => {
        setView('list');
        setSelectedOpId(null);
    };

    return (
        <div className="space-y-6">
            <div className="flex justify-between items-center">
                <div>
                    <h1 className="text-3xl font-black text-foreground tracking-tight italic uppercase">Ordenes de Producción</h1>
                    <p className="text-sm text-muted-foreground font-medium">Visualización 360° de la Orden de Producción (NV + EVN + Costeo)</p>
                </div>
            </div>

            {view === 'list' ? (
                <OrdenProduccionList onSelect={handleSelectOp} />
            ) : (
                <OrdenProduccionDetail opId={selectedOpId} onBack={handleBack} />
            )}
        </div>
    );
}
