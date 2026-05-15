import {
    ArrowRight,
    Building2,
    Trash2,
    Plus,
    Calculator,
    Edit3,
    Users
} from 'lucide-react';
import EvaluacionForm from "./EvaluacionForm";
import EVNActionBar from "./components/EVNActionBar";
import EVNVinculacionesPanel from "./components/EVNVinculacionesPanel";
import EVNResumenSidebar from "./components/EVNResumenSidebar";
import QuotationSelectionModal from "./components/QuotationSelectionModal";
import { useEVNState, parseId, DEFAULT_ITEM } from '../../../../../hooks/useEVNState';

export default function DetalleEVN({ initialEval, onBack, isReadOnly }) {
    const {
        items, setItems,
        otrosCostos, setOtrosCostos,
        solicitud, setSolicitud,
        evalData, setEvalData,
        vinculados, setVinculados,
        selectedSCOSIds,
        selectedSCOTIds,
        isSaving,
        showQuotationModal, setShowQuotationModal,
        availableQuotations,
        pendingSCOS,
        totals,

        handleUpdateItem,
        handleBulkLink,
        applySCOSQuotation,
        toggleDocSelection,
        handleGenerarPropuesta,

        proveedores,
        clientes,
        vendedores,
        solicitudesCostos
    } = useEVNState(initialEval);

    const onGenerarPropuestaInterno = async () => {
        const success = await handleGenerarPropuesta();
        if (success) onBack();
    };

    return (
        <div className="min-h-screen bg-[#F8FAFC] pb-20 animate-in fade-in duration-500">
            <EVNActionBar
                initialEval={initialEval}
                totals={totals}
                items={items}
                otrosCostos={otrosCostos}
                solicitud={solicitud}
                evalData={evalData}
                isReadOnly={isReadOnly}
                isSaving={isSaving}
                onBack={onBack}
                onGenerarPropuesta={onGenerarPropuestaInterno}
            />

            <div className="max-w-[1700px] mx-auto px-8 space-y-6">
                <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-4 flex items-center justify-between gap-6 animate-in slide-in-from-top-4 duration-500">
                    <div className="flex items-center gap-6 flex-1">
                        <div className="flex items-center bg-gray-50 px-4 py-2 rounded-xl border border-gray-100 shadow-sm focus-within:ring-2 focus-within:ring-indigo-100 transition-all flex-1 max-w-[300px]">
                            <Users className="w-4 h-4 text-indigo-400 mr-3" />
                            <div className="flex-1">
                                <p className="text-[8px] font-black text-gray-400 uppercase tracking-widest leading-none mb-1">Ejecutivo Comercial</p>
                                <select
                                    className="w-full bg-transparent border-none text-xs font-black text-indigo-600 uppercase italic p-0 focus:ring-0 outline-none cursor-pointer"
                                    value={parseId(solicitud.vendedorId || initialEval?.vendedorId) || ''}
                                    onChange={(e) => setSolicitud(prev => ({ ...prev, vendedorId: parseInt(e.target.value) }))}
                                >
                                    <option value="">Seleccionar...</option>
                                    {vendedores.map(v => (
                                        <option key={v.id} value={v.id}>
                                            {v.nombreUsuario ? `${v.nombreUsuario} ${v.apellidosUsuario || ''}`.trim() : v.codigoVendedor}
                                        </option>
                                    ))}
                                </select>
                            </div>
                        </div>

                        <div className="flex items-center bg-gray-50 px-4 py-2 rounded-xl border border-gray-100 shadow-sm focus-within:ring-2 focus-within:ring-indigo-100 transition-all flex-1 max-w-[400px]">
                            <Building2 className="w-4 h-4 text-gray-400 mr-3" />
                            <div className="flex-1">
                                <p className="text-[8px] font-black text-gray-400 uppercase tracking-widest leading-none mb-1">Cliente Solicitante</p>
                                <select
                                    className="w-full bg-transparent border-none text-xs font-black text-gray-700 uppercase p-0 focus:ring-0 outline-none cursor-pointer"
                                    value={parseId(solicitud.clienteId || initialEval?.clienteId) || ''}
                                    onChange={(e) => {
                                        const id = parseInt(e.target.value);
                                        const c = clientes.find(cli => (cli.clienteId || cli.id) === id);
                                        setSolicitud(prev => ({
                                            ...prev,
                                            clienteId: id,
                                            clienteNombre: c ? `${c.nombreCliente} ${c.apellidoCliente || ''}`.trim() : ''
                                        }));
                                    }}
                                >
                                    <option value="">Seleccionar Cliente...</option>
                                    {clientes.map(c => (
                                        <option key={c.clienteId || c.id} value={c.clienteId || c.id}>{c.nombreCliente} {c.apellidoCliente || ''}</option>
                                    ))}
                                </select>
                            </div>
                        </div>
                    </div>

                    <div className="flex items-center gap-8 pl-6 border-l border-gray-100">
                        <div className="min-w-[250px]">
                            <p className="text-[8px] font-black text-gray-400 uppercase tracking-widest leading-none mb-1">Referencia de Negocio</p>
                            <div className="flex items-center">
                                <Edit3 className="w-3.5 h-3.5 text-gray-300 mr-2" />
                                <input
                                    type="text"
                                    placeholder="Ingrese referencia..."
                                    className="bg-transparent border-none text-xs font-black text-gray-600 uppercase p-0 focus:ring-0 w-full placeholder:text-gray-200"
                                    value={evalData.referencia}
                                    onChange={(e) => setEvalData({ ...evalData, referencia: e.target.value })}
                                />
                            </div>
                        </div>
                    </div>
                </div>

                <div className="bg-white rounded-[2rem] shadow-sm border border-gray-100 overflow-hidden">
                    <div className="p-6 border-b border-gray-50 flex items-center bg-gray-50/30">
                        <div className="flex items-center space-x-3">
                            <div className="w-2 h-6 bg-indigo-600 rounded-full" />
                            <h3 className="text-sm font-black text-gray-800 uppercase tracking-widest">
                                Matriz de Precios y Costos Unitarios
                            </h3>
                        </div>
                    </div>

                    <EVNVinculacionesPanel
                        isReadOnly={isReadOnly}
                        solicitudesCostos={solicitudesCostos}
                        vinculados={vinculados}
                        setVinculados={setVinculados}
                        selectedSCOSIds={selectedSCOSIds}
                        selectedSCOTIds={selectedSCOTIds}
                        toggleDocSelection={toggleDocSelection}
                        onBulkLink={handleBulkLink}
                    />


                    <div className="overflow-x-auto min-h-[400px]">
                        <table className="w-full text-left border-collapse min-w-[2000px]">
                            <thead>
                                <tr className="bg-indigo-600 text-white">
                                    <th className="px-3 py-3 text-[10px] font-black uppercase border-b border-indigo-500">#</th>
                                    <th className="px-3 py-3 text-[10px] font-black uppercase border-b border-indigo-500">Cant</th>
                                    <th className="px-3 py-3 text-[10px] font-black uppercase border-b border-indigo-500">Precio Venta 20% MG</th>
                                    <th className="px-3 py-3 text-[10px] font-black uppercase border-b border-indigo-500 bg-green-700">P. Venta Neto</th>
                                    <th className="px-3 py-3 text-[10px] font-black uppercase border-b border-indigo-500">P. Venta Total</th>
                                    <th className="px-3 py-3 text-[10px] font-black uppercase border-b border-indigo-500">Tipo</th>
                                    <th className="px-3 py-3 text-[10px] font-black uppercase border-b border-indigo-500">Código Interno Antuan</th>
                                    <th className="px-3 py-3 text-[10px] font-black uppercase border-b border-indigo-500">Proveedor</th>
                                    <th className="px-3 py-3 text-[10px] font-black uppercase border-b border-indigo-500">Producto</th>
                                    <th className="px-3 py-3 text-[10px] font-black uppercase border-b border-indigo-500">Código Prov.</th>
                                    <th className="px-3 py-3 text-[10px] font-black uppercase border-b border-indigo-500">Modelo</th>
                                    <th className="px-3 py-3 text-[10px] font-black uppercase border-b border-indigo-500">Género</th>
                                    <th className="px-3 py-3 text-[10px] font-black uppercase border-b border-indigo-500">Tela</th>
                                    <th className="px-3 py-3 text-[10px] font-black uppercase border-b border-indigo-500">Composición</th>
                                    <th className="px-3 py-3 text-[10px] font-black uppercase border-b border-gray-700 bg-gray-700">Costo Producto</th>
                                    <th className="px-3 py-3 text-[10px] font-black uppercase border-b border-gray-700 bg-gray-700">Costo Logo</th>
                                    <th className="px-3 py-3 text-[10px] font-black uppercase border-b border-gray-700 bg-gray-700">Costo OT</th>
                                    <th className="px-3 py-3 text-[10px] font-black uppercase border-b border-blue-500 bg-blue-700">Costos Generales %</th>
                                    <th className="px-3 py-3 text-[10px] font-black uppercase border-b border-gray-600 bg-gray-800">Costo Total Unit.</th>
                                    <th className="px-3 py-3 text-[10px] font-black uppercase border-b border-gray-700 bg-gray-900">Costo Total</th>
                                    <th className="px-3 py-3 text-[10px] font-black uppercase border-b border-orange-500 bg-orange-600">MG s/ Costo %</th>
                                    <th className="px-3 py-3 text-[10px] font-black uppercase border-b border-orange-500 bg-orange-600">MG s/ Venta %</th>
                                    <th className="px-3 py-3 text-[10px] font-black uppercase border-b border-orange-500 bg-orange-600">MG s/ Venta $</th>
                                    <th className="px-3 py-3 text-[10px] font-black uppercase border-b border-indigo-500 text-center">Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                {totals.itemsConCostos.map((item, idx) => (
                                    <tr key={item.id} className="hover:bg-gray-50 border-b group transition-colors">
                                        <td className="px-3 py-3 text-xs font-bold text-gray-400 italic">{idx + 1}</td>
                                        <td className="px-3 py-3">
                                            <input
                                                type="number"
                                                className="w-16 px-2 py-1.5 bg-white border border-gray-100 rounded-lg text-xs font-black focus:ring-2 focus:ring-indigo-100 outline-none transition-all"
                                                value={item.cant}
                                                onChange={(e) => handleUpdateItem(item.id, 'cant', e.target.value)}
                                            />
                                        </td>
                                        <td className="px-3 py-3 text-[11px] font-black text-gray-400 italic">
                                            ${(item.precioVenta20MG || 0).toLocaleString('es-CL', { maximumFractionDigits: 0 })}
                                        </td>
                                        <td className="px-3 py-3 bg-green-50/30">
                                            <div className="flex items-center">
                                                <span className="text-green-600 font-black mr-1 text-[10px]">$</span>
                                                <input
                                                    type="number"
                                                    className="w-24 px-2 py-1.5 bg-white border border-green-100 rounded-lg text-[11px] font-black text-green-700 focus:ring-2 focus:ring-green-400 outline-none transition-all"
                                                    value={item.precioVentaNeto}
                                                    onChange={(e) => handleUpdateItem(item.id, 'precioVentaNeto', e.target.value)}
                                                />
                                            </div>
                                        </td>
                                        <td className="px-3 py-3 text-[11px] font-black text-gray-800 tracking-tight">
                                            ${(item.precioVentaTotal || 0).toLocaleString('es-CL')}
                                        </td>
                                        <td className="px-3 py-3">
                                            <select
                                                className="px-2 py-1.5 bg-white border border-gray-100 rounded-lg text-[10px] font-black uppercase outline-none focus:ring-2 focus:ring-indigo-100"
                                                value={item.tipo}
                                                onChange={(e) => handleUpdateItem(item.id, 'tipo', e.target.value)}
                                            >
                                                <option value="SC">SC</option>
                                                <option value="SCI">SCI</option>
                                                <option value="OP">OP</option>
                                            </select>
                                        </td>
                                        <td className="px-3 py-3">
                                            <input
                                                type="text"
                                                className="w-28 px-2 py-1.5 bg-white border border-gray-100 rounded-lg text-[10px] font-bold uppercase transition-all"
                                                value={item.codigoInterno}
                                                onChange={(e) => handleUpdateItem(item.id, 'codigoInterno', e.target.value)}
                                                placeholder="Código"
                                            />
                                        </td>
                                        <td className="px-3 py-3">
                                            <select
                                                className="px-2 py-1.5 bg-white border border-gray-100 rounded-lg text-[10px] font-bold uppercase transition-all"
                                                value={item.proveedorId || ''}
                                                onChange={(e) => {
                                                    const pId = e.target.value;
                                                    const p = proveedores.find(prov => String(prov.proveedorId || prov.id) === String(pId));
                                                    handleUpdateItem(item.id, 'proveedorId', pId);
                                                    handleUpdateItem(item.id, 'proveedor', p ? (p.nombreProveedor || p.nombre) : '');
                                                }}
                                            >
                                                <option value="">Prov.</option>
                                                {proveedores.map(p => (
                                                    <option key={p.proveedorId || p.id} value={p.proveedorId || p.id}>
                                                        {p.nombreProveedor || p.nombre}
                                                    </option>
                                                ))}
                                            </select>
                                        </td>
                                        <td className="px-3 py-3">
                                            <input
                                                type="text"
                                                className="w-28 px-2 py-1.5 bg-white border border-gray-100 rounded-lg text-[10px] font-bold uppercase transition-all"
                                                value={item.producto}
                                                onChange={(e) => handleUpdateItem(item.id, 'producto', e.target.value)}
                                                placeholder="Producto"
                                            />
                                        </td>
                                        <td className="px-3 py-3">
                                            <input
                                                type="text"
                                                className="w-28 px-2 py-1.5 bg-white border border-gray-100 rounded-lg text-[10px] font-bold uppercase transition-all"
                                                value={item.codigoProveedor}
                                                onChange={(e) => handleUpdateItem(item.id, 'codigoProveedor', e.target.value)}
                                                placeholder="Cod. Prov"
                                            />
                                        </td>
                                        <td className="px-3 py-3">
                                            <input
                                                type="text"
                                                className="w-32 px-2 py-1.5 bg-white border border-gray-100 rounded-lg text-[10px] font-bold uppercase transition-all"
                                                value={item.modelo}
                                                onChange={(e) => handleUpdateItem(item.id, 'modelo', e.target.value)}
                                                placeholder="Modelo"
                                            />
                                        </td>
                                        <td className="px-3 py-3">
                                            <select
                                                className="px-2 py-1.5 bg-white border border-gray-100 rounded-lg text-[10px] font-bold uppercase transition-all"
                                                value={item.genero}
                                                onChange={(e) => handleUpdateItem(item.id, 'genero', e.target.value)}
                                            >
                                                <option value="">Gén.</option>
                                                <option value="Masculino">Masc</option>
                                                <option value="Femenino">Fem</option>
                                                <option value="Unisex">Unis</option>
                                            </select>
                                        </td>
                                        <td className="px-3 py-3">
                                            <input
                                                type="text"
                                                className="w-28 px-2 py-1.5 bg-white border border-gray-100 rounded-lg text-[10px] font-bold uppercase transition-all"
                                                value={item.tela}
                                                onChange={(e) => handleUpdateItem(item.id, 'tela', e.target.value)}
                                                placeholder="Tela"
                                            />
                                        </td>
                                        <td className="px-3 py-3">
                                            <input
                                                type="text"
                                                className="w-32 px-2 py-1.5 bg-white border border-gray-100 rounded-lg text-[10px] font-bold uppercase transition-all"
                                                value={item.composicion}
                                                onChange={(e) => handleUpdateItem(item.id, 'composicion', e.target.value)}
                                                placeholder="Comp."
                                            />
                                        </td>
                                        <td className="px-3 py-3 bg-gray-50/50">
                                            <div className="flex items-center">
                                                <span className="text-gray-400 text-[10px] font-black mr-1">$</span>
                                                <input
                                                    type="number"
                                                    className="w-20 px-2 py-1.5 bg-white border border-gray-100 rounded-lg text-[11px] font-black text-gray-700 outline-none transition-all"
                                                    value={item.costoProducto}
                                                    onChange={(e) => handleUpdateItem(item.id, 'costoProducto', e.target.value)}
                                                />
                                            </div>
                                        </td>
                                        <td className="px-3 py-3 bg-gray-50/50">
                                            <div className="flex items-center">
                                                <span className="text-gray-400 text-[10px] font-black mr-1">$</span>
                                                <input
                                                    type="number"
                                                    className="w-20 px-2 py-1.5 bg-white border border-gray-100 rounded-lg text-[11px] font-black text-gray-700 outline-none transition-all"
                                                    value={item.costoLogo}
                                                    onChange={(e) => handleUpdateItem(item.id, 'costoLogo', e.target.value)}
                                                />
                                            </div>
                                        </td>
                                        <td className="px-3 py-3 bg-gray-50/50">
                                            <div className="flex items-center">
                                                <span className="text-gray-400 text-[10px] font-black mr-1">$</span>
                                                <input
                                                    type="number"
                                                    className="w-20 px-2 py-1.5 bg-white border border-gray-100 rounded-lg text-[11px] font-black text-gray-700 outline-none transition-all"
                                                    value={item.costoOrdenTrabajo}
                                                    onChange={(e) => handleUpdateItem(item.id, 'costoOrdenTrabajo', e.target.value)}
                                                />
                                            </div>
                                        </td>
                                        <td className="px-3 py-3 bg-blue-50/30 text-[11px] font-bold text-blue-700 italic">
                                            ${(item.costosGenerales || 0).toLocaleString('es-CL', { maximumFractionDigits: 0 })}
                                        </td>
                                        <td className="px-3 py-3 bg-gray-100/50 text-[11px] font-black text-gray-900 tracking-tight">
                                            ${(item.costoTotalUnitario || 0).toLocaleString('es-CL', { maximumFractionDigits: 0 })}
                                        </td>
                                        <td className="px-3 py-3 bg-gray-900 text-[11px] font-black text-white tracking-widest italic">
                                            ${(item.costoTotal || 0).toLocaleString('es-CL', { maximumFractionDigits: 0 })}
                                        </td>
                                        <td className="px-3 py-3 bg-orange-50/30 text-[11px] font-black text-orange-600">
                                            {((item.mgSobreCosto || 0) * 100).toFixed(2)}%
                                        </td>
                                        <td className="px-3 py-3 bg-orange-50/30 text-[11px] font-black text-orange-600">
                                            {((item.mgSobreVenta || 0) * 100).toFixed(2)}%
                                        </td>
                                        <td className="px-3 py-3 bg-orange-100/30 text-[11px] font-black text-orange-700 tracking-tight italic">
                                            ${(item.mgSobreVentaPesos || 0).toLocaleString('es-CL', { maximumFractionDigits: 0 })}
                                        </td>
                                        <td className="px-3 py-3 text-center">
                                            <button
                                                onClick={() => setItems(items.filter(i => i.id !== item.id))}
                                                className="p-2 text-gray-300 hover:text-red-500 hover:bg-red-50 rounded-lg transition-all"
                                            >
                                                <Trash2 className="w-3.5 h-3.5" />
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                            <tfoot className="sticky bottom-0">
                                <tr className="bg-gray-900 text-white font-black text-[11px] uppercase tracking-[0.1em]">
                                    <td className="px-3 py-5 italic border-r border-white/5" colSpan="1">TOTAL</td>
                                    <td className="px-3 py-5 border-r border-white/5">
                                        {totals.itemsConCostos.reduce((sum, item) => sum + (item.cant || 0), 0)}
                                    </td>
                                    <td colSpan="2" className="border-r border-white/5"></td>
                                    <td className="px-3 py-5 border-r border-white/5 text-sm tracking-tight text-green-400">
                                        ${(totals.subtotalVenta || 0).toLocaleString('es-CL')}
                                    </td>
                                    <td colSpan="9" className="border-r border-white/5"></td>
                                    <td className="px-3 py-5 border-r border-white/5 bg-gray-700 text-center font-bold">
                                        ${(totals.itemsConCostos.reduce((sum, item) => sum + ((item.costoProducto || 0) * (item.cant || 0)), 0)).toLocaleString('es-CL')}
                                    </td>
                                    <td className="px-3 py-5 border-r border-white/5 bg-gray-700 text-center font-bold">
                                        ${(totals.itemsConCostos.reduce((sum, item) => sum + ((item.costoLogo || 0) * (item.cant || 0)), 0)).toLocaleString('es-CL')}
                                    </td>
                                    <td className="px-3 py-5 border-r border-white/5 bg-gray-700 text-center font-bold">
                                        <div className="flex flex-col items-center">
                                            <span>${(totals.itemsConCostos.reduce((sum, item) => sum + ((item.costoOrdenTrabajo || 0) * (item.cant || 0)), 0)).toLocaleString('es-CL')}</span>
                                            <span className="text-[7px] text-purple-300 font-black tracking-tighter opacity-80">+ CINTA (${(totals.totalPC || 0).toLocaleString('es-CL')})</span>
                                        </div>
                                    </td>
                                    <td className="px-3 py-5 border-r border-white/5 bg-blue-700 text-center font-bold">
                                        ${(totals.prorrateoLineal || 0).toLocaleString('es-CL', { maximumFractionDigits: 0 })}/u
                                    </td>
                                    <td colSpan="1" className="border-r border-white/5"></td>
                                    <td className="px-3 py-5 border-r border-white/5 bg-gray-800 text-lg tracking-tighter">
                                        ${(totals.totalCostoGeneral || 0).toLocaleString('es-CL')}
                                    </td>
                                    <td className="px-3 py-5 bg-orange-600 text-center" colSpan="3">
                                        MARGEN: {totals.margenPorc}% — P_NETO: ${(totals.margenPesos || 0).toLocaleString('es-CL')}
                                    </td>
                                    <td className="bg-indigo-600 flex items-center justify-center h-full">
                                        <button
                                            onClick={() => setItems([...items, { ...DEFAULT_ITEM, id: Date.now(), numero: items.length + 1 }])}
                                            className="w-10 h-10 bg-white/20 hover:bg-white/40 rounded-xl transition-all flex items-center justify-center group"
                                        >
                                            <Plus className="w-5 h-5 text-white group-hover:scale-125 transition-all" />
                                        </button>
                                    </td>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8 pb-10">
                    <div className="lg:col-span-2 space-y-8">
                        <div className="bg-white rounded-[2rem] shadow-sm border border-gray-100 p-8">
                            <div className="flex items-center space-x-3 mb-8">
                                <div className="w-2 h-6 bg-indigo-600 rounded-full" />
                                <h3 className="text-sm font-black text-gray-800 uppercase tracking-widest italic">Otros Costos y Gastos Operacionales</h3>
                            </div>

                            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-10">
                                <div>
                                    <p className="text-[9px] font-black text-gray-400 uppercase tracking-widest mb-1.5 ml-1">Garantía Seriedad</p>
                                    <input
                                        type="number"
                                        className="w-full px-4 py-2.5 bg-gray-50 border-none rounded-xl text-xs font-black text-gray-700 focus:ring-2 focus:ring-indigo-100 outline-none transition-all"
                                        value={otrosCostos.garantiaSeriedad}
                                        onChange={(e) => setOtrosCostos({ ...otrosCostos, garantiaSeriedad: parseFloat(e.target.value) || 0 })}
                                    />
                                </div>
                                <div>
                                    <p className="text-[9px] font-black text-gray-400 uppercase tracking-widest mb-1.5 ml-1">Garantía Cumplimiento</p>
                                    <input
                                        type="number"
                                        className="w-full px-4 py-2.5 bg-gray-50 border-none rounded-xl text-xs font-black text-gray-700 focus:ring-2 focus:ring-indigo-100 outline-none transition-all"
                                        value={otrosCostos.garantiaFielCumplimiento}
                                        onChange={(e) => setOtrosCostos({ ...otrosCostos, garantiaFielCumplimiento: parseFloat(e.target.value) || 0 })}
                                    />
                                </div>
                                <div>
                                    <p className="text-[9px] font-black text-gray-400 uppercase tracking-widest mb-1.5 ml-1">Flete</p>
                                    <input
                                        type="number"
                                        className="w-full px-4 py-2.5 bg-gray-50 border-none rounded-xl text-xs font-black text-gray-700 focus:ring-2 focus:ring-indigo-100 outline-none transition-all"
                                        value={otrosCostos.flete}
                                        onChange={(e) => setOtrosCostos({ ...otrosCostos, flete: parseFloat(e.target.value) || 0 })}
                                    />
                                </div>
                                <div>
                                    <p className="text-[9px] font-black text-gray-400 uppercase tracking-widest mb-1.5 ml-1">Certificación</p>
                                    <input
                                        type="number"
                                        className="w-full px-4 py-2.5 bg-gray-50 border-none rounded-xl text-xs font-black text-gray-700 focus:ring-2 focus:ring-indigo-100 outline-none transition-all"
                                        value={otrosCostos.certificacion}
                                        onChange={(e) => setOtrosCostos({ ...otrosCostos, certificacion: parseFloat(e.target.value) || 0 })}
                                    />
                                </div>
                            </div>

                            <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                                {/* Toma de Tallaje */}
                                <div className="p-6 bg-indigo-50/50 rounded-[2rem] border border-indigo-100 relative group overflow-hidden">
                                     <div className="absolute top-0 right-0 w-32 h-32 bg-indigo-100/50 rounded-full -mr-16 -mt-16 group-hover:scale-110 transition-transform duration-700" />
                                     <div className="relative">
                                         <p className="text-[10px] font-black text-indigo-600 uppercase tracking-widest mb-6 flex items-center">
                                             <Users className="w-4 h-4 mr-2" />
                                             Desglose Toma de Tallaje
                                         </p>
                                         <div className="grid grid-cols-2 gap-4">
                                             <div>
                                                 <p className="text-[8px] font-black text-indigo-400 uppercase mb-1">Días Recinto</p>
                                                 <input
                                                     type="number"
                                                     className="w-full px-3 py-2 bg-white border border-indigo-100 rounded-xl text-[11px] font-black"
                                                     value={otrosCostos.tomaTallaje.diasRecinto}
                                                     onChange={(e) => setOtrosCostos({ ...otrosCostos, tomaTallaje: { ...otrosCostos.tomaTallaje, diasRecinto: parseFloat(e.target.value) || 0 } })}
                                                 />
                                             </div>
                                             <div>
                                                 <p className="text-[8px] font-black text-indigo-400 uppercase mb-1">Personal</p>
                                                 <input
                                                     type="number"
                                                     className="w-full px-3 py-2 bg-white border border-indigo-100 rounded-xl text-[11px] font-black"
                                                     value={otrosCostos.tomaTallaje.persRecinto}
                                                     onChange={(e) => setOtrosCostos({ ...otrosCostos, tomaTallaje: { ...otrosCostos.tomaTallaje, persRecinto: parseFloat(e.target.value) || 0 } })}
                                                 />
                                             </div>
                                             <div>
                                                 <p className="text-[8px] font-black text-indigo-400 uppercase mb-1">Bencina $/Lt</p>
                                                 <input
                                                     type="number"
                                                     className="w-full px-3 py-2 bg-white border border-indigo-100 rounded-xl text-[11px] font-black"
                                                     value={otrosCostos.tomaTallaje.bencinaPorLitro}
                                                     onChange={(e) => setOtrosCostos({ ...otrosCostos, tomaTallaje: { ...otrosCostos.tomaTallaje, bencinaPorLitro: parseFloat(e.target.value) || 0 } })}
                                                 />
                                             </div>
                                             <div>
                                                 <p className="text-[8px] font-black text-indigo-400 uppercase mb-1">Km Totales</p>
                                                 <input
                                                     type="number"
                                                     className="w-full px-3 py-2 bg-white border border-indigo-100 rounded-xl text-[11px] font-black"
                                                     value={otrosCostos.tomaTallaje.kmTotal}
                                                     onChange={(e) => setOtrosCostos({ ...otrosCostos, tomaTallaje: { ...otrosCostos.tomaTallaje, kmTotal: parseFloat(e.target.value) || 0 } })}
                                                 />
                                             </div>
                                         </div>
                                         <div className="mt-6 pt-4 border-t border-indigo-100 flex justify-between items-center">
                                             <div className="text-[7px] space-y-0.5">
                                                 <p className="font-black text-indigo-300 uppercase">Personal: ${totals.costoPersonalTT.toLocaleString('es-CL')}</p>
                                                 <p className="font-black text-indigo-300 uppercase">Movilidad: ${totals.costoMovilizacionTT.toLocaleString('es-CL')}</p>
                                             </div>
                                             <div className="bg-indigo-600 text-white px-4 py-2 rounded-xl text-center shadow-lg shadow-indigo-100">
                                                 <p className="text-[7px] font-black uppercase opacity-80">Total TT</p>
                                                 <p className="text-sm font-black">${totals.totalTT.toLocaleString('es-CL')}</p>
                                             </div>
                                         </div>
                                     </div>
                                </div>

                                {/* Pegado de Cinta */}
                                <div className="p-6 bg-purple-50/50 rounded-[2rem] border border-purple-100 relative group overflow-hidden">
                                     <div className="absolute top-0 right-0 w-32 h-32 bg-purple-100/50 rounded-full -mr-16 -mt-16 group-hover:scale-110 transition-transform duration-700" />
                                     <div className="relative">
                                         <p className="text-[10px] font-black text-purple-600 uppercase tracking-widest mb-4 flex items-center">
                                             <Calculator className="w-4 h-4 mr-2" />
                                             Costos Pegado de Cinta
                                         </p>
                                         <div className="space-y-4">
                                             {(otrosCostos.pegadoCinta || []).map((itemCinta, idx) => (
                                                 <div key={itemCinta.id} className="bg-white p-3 rounded-2xl border border-purple-100 shadow-sm flex flex-col gap-2">
                                                     <p className="text-[9px] font-black text-purple-700 uppercase italic">{itemCinta.etiqueta}</p>
                                                     <div className="grid grid-cols-3 gap-2">
                                                         <div>
                                                             <p className="text-[7px] font-black text-gray-400 uppercase mb-0.5">Cinta $</p>
                                                             <input
                                                                 type="number"
                                                                 className="w-full px-1.5 py-1 bg-white border border-purple-100 rounded text-[9px] font-bold"
                                                                 value={itemCinta.costoCinta}
                                                                 onChange={(e) => {
                                                                     const newLista = [...otrosCostos.pegadoCinta];
                                                                     newLista[idx].costoCinta = parseFloat(e.target.value) || 0;
                                                                     setOtrosCostos({ ...otrosCostos, pegadoCinta: newLista });
                                                                 }}
                                                             />
                                                         </div>
                                                         <div>
                                                             <p className="text-[7px] font-black text-gray-400 uppercase mb-0.5">MO $</p>
                                                             <input
                                                                 type="number"
                                                                 className="w-full px-1.5 py-1 bg-white border border-purple-100 rounded text-[9px] font-bold"
                                                                 value={itemCinta.costoMO}
                                                                 onChange={(e) => {
                                                                     const newLista = [...otrosCostos.pegadoCinta];
                                                                     newLista[idx].costoMO = parseFloat(e.target.value) || 0;
                                                                     setOtrosCostos({ ...otrosCostos, pegadoCinta: newLista });
                                                                 }}
                                                             />
                                                         </div>
                                                         <div>
                                                             <p className="text-[7px] font-black text-gray-400 uppercase mb-0.5">MTS</p>
                                                             <input
                                                                 type="number"
                                                                 step="0.1"
                                                                 className="w-full px-1.5 py-1 bg-white border border-purple-100 rounded text-[9px] font-bold"
                                                                 value={itemCinta.mtsCinta}
                                                                 onChange={(e) => {
                                                                     const newLista = [...otrosCostos.pegadoCinta];
                                                                     newLista[idx].mtsCinta = parseFloat(e.target.value) || 0;
                                                                     setOtrosCostos({ ...otrosCostos, pegadoCinta: newLista });
                                                                 }}
                                                             />
                                                         </div>
                                                     </div>
                                                 </div>
                                             ))}
                                         </div>
                                         <div className="mt-4 pt-4 border-t border-purple-100 flex justify-between items-center">
                                             <div className="text-left">
                                                 <p className="text-[8px] font-black text-purple-400 uppercase leading-none">Total Cinta</p>
                                                 <p className="text-[8px] text-purple-300 italic font-bold">Sumado a costos OT</p>
                                             </div>
                                             <div className="bg-purple-600 text-white px-4 py-2 rounded-xl text-center shadow-lg shadow-purple-100">
                                                 <p className="text-[7px] font-black uppercase opacity-80 leading-none mb-1">Subtotal</p>
                                                 <p className="text-sm font-black">${totals.totalPC.toLocaleString('es-CL')}</p>
                                             </div>
                                         </div>
                                     </div>
                                </div>
                            </div>

                            <div className="mt-8 pt-8 border-t border-indigo-100 flex justify-between items-center bg-indigo-50/30 p-4 rounded-2xl">
                                <div>
                                    <p className="text-[10px] font-black text-indigo-400 uppercase tracking-[0.2em]">Total Otros Costos (Auxiliares)</p>
                                    <p className="text-[10px] text-indigo-300 font-bold italic">Suma de todas las secciones auxiliares</p>
                                </div>
                                <p className="text-3xl font-black text-indigo-700 tracking-tighter">${totals.totalOtrosCostos.toLocaleString('es-CL')}</p>
                            </div>
                        </div>

                        {/* Commercial Conditions */}
                        <div className="bg-white rounded-[2rem] shadow-sm border border-gray-100 p-8">
                            <div className="flex items-center space-x-3 mb-8">
                                <div className="w-2 h-6 bg-indigo-600 rounded-full" />
                                <h3 className="text-sm font-black text-gray-800 uppercase tracking-widest italic">Condiciones Comerciales y Formales</h3>
                            </div>

                            <EvaluacionForm
                                data={{ ...evalData, margenFinal: totals.margenPorc }}
                                onChange={setEvalData}
                            />

                            {!isReadOnly && (
                                <div className="mt-8 flex justify-end items-center pt-8 border-t border-gray-50">
                                    <button
                                        onClick={onGenerarPropuestaInterno}
                                        disabled={isSaving}
                                        className="flex items-center gap-3 px-6 py-3 bg-gray-900 hover:bg-indigo-600 text-white rounded-2xl text-[11px] font-black uppercase tracking-widest shadow-xl shadow-gray-200 transition-colors disabled:opacity-60"
                                    >
                                        {isSaving ? 'Guardando...' : 'Guardar Condiciones'}
                                        <ArrowRight className="w-4 h-4" />
                                    </button>
                                </div>
                            )}
                        </div>
                    </div>

                    <div className="space-y-6">
                        <EVNResumenSidebar totals={totals} otrosCostos={otrosCostos} />
                    </div>
                </div>

                <QuotationSelectionModal
                    open={showQuotationModal}
                    pendingSCOS={pendingSCOS}
                    availableQuotations={availableQuotations}
                    onApply={applySCOSQuotation}
                    onClose={() => setShowQuotationModal(false)}
                />
            </div>
        </div>
    );
}
