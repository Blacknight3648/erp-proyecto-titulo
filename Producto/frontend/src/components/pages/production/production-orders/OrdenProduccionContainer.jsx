import { useState } from 'react';
import OrdenProduccionList from './OrdenProduccionList';
import OrdenProduccionDetail from './OrdenProduccionDetail';
import { Loader2 } from 'lucide-react';

export default function OrdenProduccionContainer() {
    const [view, setView] = useState('list'); // 'list', 'detail'
    const [selectedOpId, setSelectedOpId] = useState(null);
    const [loading, setLoading] = useState(false);

    const handleSelectOp = (id) => {
        setLoading(true);
        setSelectedOpId(id);
        // Simular carga de datos integrados
        setTimeout(() => {
            setView('detail');
            setLoading(false);
        }, 800);
    };

    const handleBack = () => {
        setView('list');
        setSelectedOpId(null);
    };

    if (loading) {
        return (
            <div className="flex flex-col items-center justify-center min-h-[400px]">
                <Loader2 className="w-12 h-12 text-blue-600 animate-spin mb-4" />
                <p className="text-gray-500 font-bold animate-pulse">Cargando información integrada...</p>
            </div>
        );
    }

    return (
        <div className="space-y-6">
            <div className="flex justify-between items-center">
                <div>
                    <h1 className="text-3xl font-black text-gray-800 tracking-tight italic uppercase">Ordenes de Producción</h1>
                    <p className="text-sm text-gray-500 font-medium">Visualización 360° de la Orden de Producción (NV + EVN + Costeo)</p>
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
