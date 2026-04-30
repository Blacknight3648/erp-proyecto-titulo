import { useState, useMemo } from 'react';
import { 
    ChevronLeft, 
    Save, 
    Plus, 
    Trash2, 
    Search, 
    Factory, 
    Truck, 
    FileText,
    Package,
    AlertCircle
} from 'lucide-react';
import { 
    mockOperaciones, 
    mockProveedores, 
    mockTalleresExternos 
} from '../../../../data/mockData';
import { toast } from 'sonner';

export default function EmisorCompraProduccion({ onBack }) {
    const [selectedOp, setSelectedOp] = useState('');
    const [selectedProveedor, setSelectedProveedor] = useState('');
    const [items, setItems] = useState([]);
    const [tipoDocumento, setTipoDocumento] = useState('OC'); // OC u OS (Orden de Servicio)

    const handleAddItem = () => {
        setItems([...items, { id: crypto.randomUUID(), descripcion: '', cantidad: 0, precio: 0 }]);
    };

    const handleRemoveItem = (id) => {
        setItems(items.filter(item => item.id !== id));
    };

    const handleUpdateItem = (id, field, value) => {
        setItems(items.map(item => item.id === id ? { ...item, [field]: value } : item));
    };

    const totalMonto = useMemo(() => {
        return items.reduce((acc, current) => acc + (Number(current.cantidad) * Number(current.precio)), 0);
    }, [items]);

    const handleEmitir = () => {
        if (!selectedOp || !selectedProveedor || items.length === 0) {
            toast.error('Faltan datos requeridos');
            return;
        }
        toast.success(`${tipoDocumento === 'OC' ? 'Orden de Compra' : 'Orden de Servicio'} emitida con éxito`);
        onBack();
    };

    return (
        <div className="space-y-8 animate-in slide-in-from-right-8 duration-500 pb-20">
            {/* Cabecera */}
            <div className="flex items-center justify-between">
                <div className="flex items-center gap-6">
                    <button
                        onClick={onBack}
                        className="p-3 bg-white border border-gray-100 shadow-sm rounded-2xl hover:bg-gray-50 transition-all text-gray-400 hover:text-indigo-600 active:scale-95"
                    >
                        <ChevronLeft className="w-5 h-5" />
                    </button>
                    <div>
                        <h2 className="text-2xl font-black text-gray-800 tracking-tight italic uppercase">Emitir Documento de Adquisición</h2>
                        <p className="text-[10px] text-gray-500 font-black uppercase tracking-widest mt-1">Vínculo directo con flujo de planta</p>
                    </div>
                </div>
                <div className="flex gap-3 bg-gray-100 p-1 rounded-2xl border border-gray-200">
                    <button 
                        onClick={() => setTipoDocumento('OC')}
                        className={`px-6 py-2 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all ${tipoDocumento === 'OC' ? 'bg-white text-indigo-600 shadow-sm' : 'text-gray-400 hover:text-gray-600'}`}
                    >
                        Orden de Compra
                    </button>
                    <button 
                        onClick={() => setTipoDocumento('OS')}
                        className={`px-6 py-2 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all ${tipoDocumento === 'OS' ? 'bg-white text-indigo-600 shadow-sm' : 'text-gray-400 hover:text-gray-600'}`}
                    >
                        Orden de Servicio
                    </button>
                </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-4 gap-8">
                {/* Panel de Selección */}
                <div className="lg:col-span-1 space-y-8">
                    <div className="bg-white p-8 rounded-[2.5rem] shadow-sm border border-gray-100 space-y-6">
                        <div>
                            <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest mb-3 block">1. Seleccionar OP</label>
                            <div className="relative">
                                <Factory className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-300 w-4 h-4" />
                                <select 
                                    className="w-full pl-10 pr-4 py-3 bg-gray-50 border-none rounded-2xl text-xs font-bold outline-none ring-0 focus:ring-2 focus:ring-indigo-500 transition-all appearance-none"
                                    value={selectedOp}
                                    onChange={(e) => setSelectedOp(e.target.value)}
                                >
                                    <option value="">Seleccione OP...</option>
                                    {mockOperaciones.map(op => (
                                        <option key={op.idOP} value={op.idOP}>{op.idOP} - {op.producto}</option>
                                    ))}
                                </select>
                            </div>
                        </div>

                        <div>
                            <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest mb-3 block">
                                {tipoDocumento === 'OC' ? '2. Proveedor Materiales' : '2. Taller Externo'}
                            </label>
                            <div className="relative">
                                {tipoDocumento === 'OC' ? <Truck className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-300 w-4 h-4" /> : <Package className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-300 w-4 h-4" />}
                                <select 
                                    className="w-full pl-10 pr-4 py-3 bg-gray-50 border-none rounded-2xl text-xs font-bold outline-none ring-0 focus:ring-2 focus:ring-indigo-500 transition-all appearance-none"
                                    value={selectedProveedor}
                                    onChange={(e) => setSelectedProveedor(e.target.value)}
                                >
                                    <option value="">Seleccione Entidad...</option>
                                    {tipoDocumento === 'OC' ? (
                                        mockProveedores.map(p => <option key={p.proveedorId} value={p.nombreProveedor}>{p.nombreProveedor}</option>)
                                    ) : (
                                        mockTalleresExternos.map(t => <option key={t.id} value={t.nombre}>{t.nombre}</option>)
                                    )}
                                </select>
                            </div>
                        </div>

                        {selectedOp && (
                            <div className="bg-indigo-50/50 p-4 rounded-3xl border border-indigo-100">
                                <div className="flex items-center gap-2 mb-2">
                                    <AlertCircle className="w-3.5 h-3.5 text-indigo-600" />
                                    <span className="text-[9px] font-black text-indigo-600 uppercase tracking-tighter">Info de Contexto</span>
                                </div>
                                <p className="text-[11px] text-indigo-900 font-bold italic line-clamp-3">
                                    Esta OP requiere insumos según costeo técnico. Verifique consumos antes de emitir.
                                </p>
                            </div>
                        )}
                    </div>
                </div>

                {/* Tabla de Items */}
                <div className="lg:col-span-3 space-y-6">
                    <div className="bg-white rounded-[2.5rem] shadow-sm border border-gray-100 overflow-hidden min-h-[400px] flex flex-col">
                        <div className="p-8 border-b border-gray-50 flex justify-between items-center">
                            <h3 className="text-sm font-black text-gray-800 uppercase tracking-widest flex items-center gap-2">
                                <FileText className="w-4 h-4 text-indigo-600" />
                                Detalle de Líneas de {tipoDocumento === 'OC' ? 'Compra' : 'Servicio'}
                            </h3>
                            <button 
                                onClick={handleAddItem}
                                className="px-4 py-2 bg-gray-50 text-indigo-600 rounded-xl text-[10px] font-black uppercase tracking-widest border border-indigo-100 hover:bg-indigo-50 transition-all"
                            >
                                <Plus className="w-3 h-3 mr-2 inline" /> Agregar Línea
                            </button>
                        </div>
                        
                        <div className="flex-1 overflow-auto">
                            <table className="w-full text-left">
                                <thead className="bg-gray-50/50 border-b border-gray-50">
                                    <tr>
                                        <th className="px-8 py-4 text-[9px] font-black text-gray-400 uppercase tracking-widest w-1/2">Descripción / Concepto</th>
                                        <th className="px-4 py-4 text-[9px] font-black text-gray-400 uppercase tracking-widest text-center">Cant.</th>
                                        <th className="px-4 py-4 text-[9px] font-black text-gray-400 uppercase tracking-widest text-center">P. Unitario</th>
                                        <th className="px-4 py-4 text-[9px] font-black text-gray-400 uppercase tracking-widest text-right">Subtotal</th>
                                        <th className="px-4 py-4 text-[9px] font-black text-gray-400 uppercase tracking-widest text-right"></th>
                                    </tr>
                                </thead>
                                <tbody className="divide-y divide-gray-50">
                                    {items.map((item) => (
                                        <tr key={item.id} className="hover:bg-gray-50/50 transition-colors group">
                                            <td className="px-8 py-4">
                                                <input 
                                                    type="text"
                                                    placeholder="Ej: Tela Jersey Azul 24/1"
                                                    className="w-full bg-transparent border-none text-xs font-bold text-gray-800 outline-none placeholder:text-gray-300"
                                                    value={item.descripcion}
                                                    onChange={(e) => handleUpdateItem(item.id, 'descripcion', e.target.value)}
                                                />
                                            </td>
                                            <td className="px-4 py-4">
                                                <input 
                                                    type="number"
                                                    className="w-16 mx-auto bg-transparent border-none text-xs font-black text-blue-600 text-center outline-none"
                                                    value={item.cantidad}
                                                    onChange={(e) => handleUpdateItem(item.id, 'cantidad', e.target.value)}
                                                />
                                            </td>
                                            <td className="px-4 py-4">
                                                <input 
                                                    type="number"
                                                    className="w-24 mx-auto bg-transparent border-none text-xs font-black text-gray-700 text-center outline-none"
                                                    value={item.precio}
                                                    onChange={(e) => handleUpdateItem(item.id, 'precio', e.target.value)}
                                                />
                                            </td>
                                            <td className="px-4 py-4 text-right">
                                                <span className="text-xs font-black text-gray-900 italic">
                                                    ${(item.cantidad * item.precio).toLocaleString()}
                                                </span>
                                            </td>
                                            <td className="px-4 py-4 text-right">
                                                <button 
                                                    onClick={() => handleRemoveItem(item.id)}
                                                    className="p-2 text-gray-300 hover:text-red-500 opacity-0 group-hover:opacity-100 transition-all"
                                                >
                                                    <Trash2 className="w-4 h-4" />
                                                </button>
                                            </td>
                                        </tr>
                                    ))}
                                    {items.length === 0 && (
                                        <tr>
                                            <td colSpan="5" className="py-20 text-center">
                                                <div className="flex flex-col items-center gap-3">
                                                    <div className="bg-gray-50 p-4 rounded-3xl">
                                                        <Search className="w-6 h-6 text-gray-300" />
                                                    </div>
                                                    <p className="text-[10px] font-black text-gray-300 uppercase tracking-widest italic">No hay líneas agregadas</p>
                                                </div>
                                            </td>
                                        </tr>
                                    )}
                                </tbody>
                            </table>
                        </div>

                        <div className="p-8 bg-gray-50/50 border-t border-gray-100 flex justify-between items-center">
                            <div>
                                <p className="text-[9px] font-black text-gray-400 uppercase tracking-widest mb-1">Inversión Estimada</p>
                                <p className="text-3xl font-black text-indigo-900 tracking-tighter italic">${totalMonto.toLocaleString()}</p>
                            </div>
                            <button 
                                onClick={handleEmitir}
                                className="flex items-center gap-3 px-10 py-4 bg-slate-900 text-white rounded-[1.5rem] font-black uppercase text-[10px] tracking-widest hover:bg-slate-800 transition-all shadow-xl shadow-slate-200 active:scale-95 border border-slate-700"
                            >
                                <Save className="w-4 h-4" /> Emitir {tipoDocumento}
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
