import { useState, useMemo, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Search, Filter, History, ChevronRight, ShoppingCart, Plus, X, CheckCircle2, Package, Calendar, Activity, Info, AlertCircle, Wrench, Clock, Truck, Factory, FileText } from 'lucide-react';
import { mockOperaciones, mockProveedores, mockAllOCs, mockOpDetails } from '../../../data/mockData';
import { pdfService } from '../../../remote/service/pdfService';
import { toast } from 'sonner';
import { validateNumericInput } from '../../../utils/validations';

export default function EmitirOC_OP() {
    const [searchQuery, setSearchQuery] = useState('');
    const [selectedOP, setSelectedOP] = useState(null);
    const [showNewOCModal, setShowNewOCModal] = useState(false);
    const [tipoOC, setTipoOC] = useState('PROVEEDOR'); // PROVEEDOR, OS (Orden de Servicio)
    const [selectedProvider, setSelectedProvider] = useState('');
    const [ocItems, setOcItems] = useState([]);
    const [emissionDate, setEmissionDate] = useState(new Date().toISOString().split('T')[0]);
    const [localOCs, setLocalOCs] = useState(mockAllOCs);
    const [editingOCId, setEditingOCId] = useState(null);
    const navigate = useNavigate();
    const location = useLocation();

    // Auto-open modal if OP is passed via state (deep-linking)
    useEffect(() => {
        if (location.state?.op) {
            handleOpenModal(location.state.op);
        }
    }, [location.state]);

    // Filter OPs that aren't finished yet
    const activeOPs = useMemo(() => {
        return mockOperaciones.filter(op =>
            ((op.idOP || op.id || '').toString().toLowerCase().includes(searchQuery.toLowerCase()) ||
                (op.cliente || '').toString().toLowerCase().includes(searchQuery.toLowerCase())) &&
            op.estado !== 'Cerrada'
        );
    }, [searchQuery]);

    const handleBack = () => {
        setSelectedOP(null);
        setEditingOCId(null);
    };

    const handleOpenModal = (op, tipo = 'PROVEEDOR') => {
        setTipoOC(tipo);
        setSelectedOP(op);
        setEditingOCId(null);
        // Pre-populate with a default item based on the OP product if ocItems is empty
        if (ocItems.length === 0) {
            setOcItems([{
                id: Date.now(),
                nombreProducto: op.producto,
                color: 'Por Definir',
                size: 'N/A',
                cantidad: 1,
                precioUnitario: 0
            }]);
        }
        setShowNewOCModal(true);
    };

    const handleRejectOC = (id) => {
        const reason = prompt('Ingrese el motivo del rechazo (ej: Sin stock, No disponible):');
        if (reason === null) return; // Cancelled

        setLocalOCs(localOCs.map(oc => {
            if (oc.idOC === id) {
                return { ...oc, estado: 'Rechazado', motivoRechazo: reason };
            }
            return oc;
        }));
        alert(`La orden ${id} ha sido rechazada.`);
    };

    const handleReemitOC = (oc) => {
        setEditingOCId(oc.idOC);
        setTipoOC(oc.tipo);
        setSelectedProvider(oc.proveedor);
        setOcItems(oc.detalles || [{
            id: Date.now(),
            nombreProducto: oc.items,
            cantidad: 1,
            precioUnitario: oc.total
        }]);
        setShowNewOCModal(true);
    };

    const handleProviderChange = (providerName) => {
        setSelectedProvider(providerName);
        const provider = mockProveedores.find(p => (p.nombreProveedor || p.nombre) === providerName);
        if (provider) {
            setOcItems(ocItems.map(item => {
                const itemInfo = provider.precios.find(pr => pr.garment === item.garment);
                return { ...item, price: itemInfo ? itemInfo.price : 0 };
            }));
        }
    };

    const addItem = () => {
        setOcItems([...ocItems, {
            id: Date.now(),
            garment: selectedOP?.producto || 'Nuevo Ítem',
            color: 'Por Definir',
            size: 'N/A',
            qty: 1,
            price: 0
        }]);
    };

    const updateItem = (id, field, value) => {
        if (field === 'precioUnitario' || field === 'cantidad') {
            const label = field === 'precioUnitario' ? 'Precio Unitario' : 'Cantidad';
            const error = validateNumericInput(value, label);
            if (error) {
                toast.error(error);
                return;
            }
        }
        setOcItems(ocItems.map(item => item.id === id ? { ...item, [field]: value } : item));
    };

    const removeItem = (id) => {
        setOcItems(ocItems.filter(item => item.id !== id));
    };

    const handleGenerateOC = () => {
        if (!selectedProvider) return alert('Seleccione un proveedor');

        if (editingOCId) {
            // Update existing OC
            setLocalOCs(localOCs.map(oc => {
                if (oc.idOC === editingOCId) {
                    return {
                        ...oc,
                        fechaEmision: emissionDate,
                        proveedor: selectedProvider,
                        items: ocItems.map(i => i.nombreProducto).join(', '),
                        montoTotal: ocItems.reduce((sum, item) => sum + (item.cantidad * (item.precioUnitario || 0)), 0),
                        estado: 'Pendiente', // Reset status
                        detalles: ocItems,
                        motivoRechazo: null // Clear rejection reason
                    };
                }
                return oc;
            }));
            alert(`¡Documento ${editingOCId} Re-emitido con Éxito!`);
            setEditingOCId(null);
        } else {
            // Create New
            const newOCIdInt = Date.now();
            const newOCNumero = `OC-${Math.floor(Math.random() * 90000) + 10000}`;
            const finalizedOC = {
                idOC: newOCIdInt,
                numeroOC: newOCNumero,
                fechaEmision: emissionDate,
                proveedor: selectedProvider,
                items: ocItems.map(i => i.nombreProducto).join(', '),
                montoTotal: ocItems.reduce((sum, item) => sum + (item.cantidad * (item.precioUnitario || 0)), 0),
                estado: 'Pendiente',
                solicitudCompraId: selectedOP.solicitudCompraId || 'N/A',
                idOP: selectedOP.idOP,
                tipo: tipoOC,
                detalles: ocItems
            };

            setLocalOCs([finalizedOC, ...localOCs]);
            alert(`¡${tipoOC === 'PROVEEDOR' ? 'Orden de Compra' : 'Orden de Servicio'} ${newOCNumero} Registrada con Éxito!`);
        }
        setShowNewOCModal(false);
    };

    return (
        <div className="space-y-6">
            <div className="flex items-center justify-between">
                <div>
                    <h1 className="text-3xl font-bold text-gray-800 tracking-tight italic">emitir OC/OS (OP)</h1>
                    <p className="text-sm text-gray-500 font-medium">Gestión de suministros y servicios externos específicos para Planta</p>
                </div>
                <div className="flex space-x-3">
                    <div className="bg-amber-100 px-4 py-2 rounded-xl border border-amber-200 flex items-center space-x-2">
                        <Activity className="w-4 h-4 text-amber-600" />
                        <span className="text-xs font-black text-amber-700 uppercase">Flujo: Producción (OP)</span>
                    </div>
                </div>
            </div>

            <div className="grid grid-cols-4 gap-4 mb-8">
                {['Planificación', 'Abastecimiento MP', 'Asignación Taller', 'Terminado'].map((step, i) => (
                    <div key={step} className="relative">
                        <div className={`p-4 rounded-xl text-center border-b-4 ${i === 1 ? 'bg-amber-50 border-amber-500 text-amber-700' : 'bg-gray-50 border-gray-200 text-gray-400'}`}>
                            <span className="text-[10px] font-black uppercase tracking-widest block mb-1">Paso {i + 1}</span>
                            <span className="text-xs font-black uppercase tracking-tight">{step}</span>
                        </div>
                        {i < 3 && <ChevronRight className="absolute -right-3 top-1/2 -translate-y-1/2 text-gray-200 w-5 h-5 z-0" />}
                    </div>
                ))}
            </div>

            <div className="bg-white p-4 rounded-2xl shadow-sm border border-gray-100 flex flex-wrap gap-4 items-center">
                <div className="relative flex-1 min-w-[200px]">
                    <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 w-4 h-4" />
                    <input
                        type="text"
                        placeholder="Buscar por OP o Cliente..."
                        className="w-full pl-10 pr-4 py-2 bg-gray-50 border-none rounded-xl text-sm focus:ring-2 focus:ring-blue-500 outline-none"
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                    />
                </div>
                <div className="flex space-x-2">
                    <button className="flex items-center px-3 py-2 bg-gray-50 text-gray-500 rounded-lg text-[10px] font-black uppercase border border-transparent hover:border-gray-200">
                        <Filter className="w-3 h-3 mr-2" /> Estado Planta
                    </button>
                    <button className="flex items-center px-3 py-2 bg-gray-50 text-gray-500 rounded-lg text-[10px] font-black uppercase border border-transparent hover:border-gray-200">
                        <History className="w-3 h-3 mr-2" /> Recientes
                    </button>
                </div>
            </div>

            {!selectedOP ? (
                /* VISTA MASTER: LISTA DE OPs */
                <div className="bg-white rounded-[2.5rem] shadow-sm border border-gray-100 overflow-hidden animate-in fade-in duration-500">
                    <table className="w-full text-left">
                        <thead className="bg-gray-50 border-b border-gray-100">
                            <tr>
                                <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest">ID OP / Cliente</th>
                                <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">Producto Principal</th>
                                <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">NV Origen</th>
                                <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">Avance Planta</th>
                                <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-right">Acciones</th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-50">
                            {activeOPs.map((op) => (
                                <tr
                                    key={op.id}
                                    className="hover:bg-amber-50/40 transition-all group cursor-pointer border-l-4 border-transparent hover:border-amber-500"
                                    onClick={() => setSelectedOP(op)}
                                >
                                    <td className="px-8 py-6">
                                        <div className="flex items-center">
                                            <div className="bg-amber-100 p-2.5 rounded-xl mr-4 group-hover:bg-amber-600 group-hover:text-white transition-all">
                                                <Factory className="w-5 h-5 text-amber-600 group-hover:text-white" />
                                            </div>
                                            <div>
                                                <div className="font-black text-gray-900 text-sm tracking-tight italic">{op.idOP === 'OP-2024-001' ? 'OP 20549' : op.idOP}</div>
                                                <div className="text-[10px] text-gray-400 font-bold uppercase tracking-wider">{op.cliente}</div>
                                            </div>
                                        </div>
                                    </td>
                                    <td className="px-8 py-6 text-center">
                                        <span className="text-xs font-black text-gray-700 uppercase">{op.producto}</span>
                                    </td>
                                    <td className="px-8 py-6 text-center">
                                        <span className="text-[10px] font-black text-blue-600 bg-blue-50 px-3 py-1.5 rounded-lg border border-blue-100 uppercase tracking-widest italic">{op.notaVentaId}</span>
                                    </td>
                                    <td className="px-8 py-6 text-center">
                                        <div className="flex items-center justify-center space-x-2">
                                            <div className="w-24 h-1.5 bg-gray-100 rounded-full overflow-hidden">
                                                <div className="h-full bg-amber-500 rounded-full" style={{ width: `${op.progreso}%` }}></div>
                                            </div>
                                            <span className="text-[10px] font-black text-amber-600">{op.progreso}%</span>
                                        </div>
                                    </td>
                                    <td className="px-8 py-6 text-right">
                                        <button className="p-2 bg-gray-50 text-gray-300 rounded-xl group-hover:bg-amber-600 group-hover:text-white transition-all scale-90 group-hover:scale-110">
                                            <ChevronRight className="w-5 h-5" />
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            ) : (
                /* VISTA DETALLE: SEGUIMIENTO Y EMISION */
                <div className="space-y-8 animate-in slide-in-from-right-8 duration-500 pb-20">
                    <div className="flex items-center space-x-6">
                        <button
                            onClick={handleBack}
                            className="p-3 bg-white border border-gray-100 shadow-sm rounded-2xl hover:bg-gray-50 transition-all text-gray-400 hover:text-amber-600 active:scale-95"
                        >
                            <ChevronRight className="w-5 h-5 rotate-180" />
                        </button>
                        <div className="flex-1">
                            <h2 className="text-2xl font-black text-gray-800 tracking-tight italic">Gestión de Compra OP {selectedOP.idOP}</h2>
                            <p className="text-[10px] text-gray-500 font-black uppercase tracking-widest mt-1">{selectedOP.cliente} • {selectedOP.producto}</p>
                        </div>
                        <div className="flex space-x-3">
                            <button
                                onClick={() => handleOpenModal(selectedOP, 'PROVEEDOR')}
                                className="flex items-center px-6 py-3 bg-blue-600 text-white rounded-2xl font-black text-[10px] uppercase tracking-widest shadow-xl shadow-blue-100 hover:bg-blue-700 hover:-translate-y-0.5 transition-all"
                            >
                                <ShoppingCart className="w-4 h-4 mr-2" />
                                Nueva OC
                            </button>
                            <button
                                onClick={() => handleOpenModal(selectedOP, 'OS')}
                                className="flex items-center px-6 py-3 bg-indigo-600 text-white rounded-2xl font-black text-[10px] uppercase tracking-widest shadow-xl shadow-indigo-100 hover:bg-indigo-700 hover:-translate-y-0.5 transition-all"
                            >
                                <Wrench className="w-4 h-4 mr-2" />
                                Nueva OS (Servicio)
                            </button>
                        </div>
                    </div>

                    {/* SECCIÓN: Seguimiento de Requerimientos Técnicos */}
                    <div className="bg-white rounded-[2.5rem] shadow-sm border border-gray-100 overflow-hidden">
                        <div className="p-8 border-b border-gray-100 bg-amber-50/20 flex justify-between items-center">
                            <div>
                                <h3 className="text-sm font-black text-gray-800 uppercase tracking-widest italic">Requerimientos de Producción (Insumos y Servicios)</h3>
                                <p className="text-[10px] text-gray-400 font-bold uppercase mt-1">Control de insumos por pedir y servicios externos asignados</p>
                            </div>
                        </div>
                        <table className="w-full text-left">
                            <thead className="bg-white border-b border-gray-100">
                                <tr>
                                    <th className="px-10 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest">Requerimiento</th>
                                    <th className="px-10 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">Total Req.</th>
                                    <th className="px-10 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">En OC/OS</th>
                                    <th className="px-10 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">Recibido</th>
                                    <th className="px-10 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">Progreso</th>
                                    <th className="px-10 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-right">Estado</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-gray-50">
                                {mockOpDetails[selectedOP.idOP]?.requerimientos?.map((req) => {
                                    // Calcular cantidadPedida dinámicamente basado en OCs que NO estén rechazadas
                                    const dynamicOrdered = localOCs
                                        .filter(oc => oc.idOP === selectedOP.idOP && oc.estado !== 'Rechazado')
                                        .reduce((acc, oc) => {
                                            const item = oc.detalles?.find(d => d.nombreProducto.includes(req.item));
                                            return acc + (item ? item.cantidad : 0);
                                        }, 0);

                                    return (
                                        <tr key={req.id} className="hover:bg-gray-50 transition-colors group">
                                            <td className="px-10 py-5">
                                                <div className="flex items-center gap-3">
                                                    <div className="p-2 bg-gray-100 rounded-lg group-hover:bg-amber-100 transition-colors">
                                                        {req.item.includes('Tela') ? <Package className="w-4 h-4 text-gray-400 group-hover:text-amber-600" /> : <Wrench className="w-4 h-4 text-gray-400 group-hover:text-amber-600" />}
                                                    </div>
                                                    <div>
                                                        <div className="text-sm font-black text-gray-800 tracking-tight uppercase">{req.item}</div>
                                                        <div className="text-[9px] font-black text-gray-400 uppercase tracking-widest">{req.detalle}</div>
                                                    </div>
                                                </div>
                                            </td>
                                            <td className="px-10 py-5 text-center text-xs font-bold text-gray-600">{req.cantidad} {req.unidad}</td>
                                            <td className="px-10 py-5 text-center text-xs font-bold text-blue-600">{dynamicOrdered}</td>
                                            <td className="px-10 py-5 text-center text-xs font-bold text-green-600">{req.cantidadRecibida}</td>
                                            <td className="px-10 py-5">
                                                <div className="w-full max-w-[100px] mx-auto space-y-1.5">
                                                    <div className="flex justify-between text-[8px] font-black uppercase tracking-widest text-gray-400">
                                                        <span>{Math.round((req.cantidadRecibida / req.cantidad) * 100)}%</span>
                                                    </div>
                                                    <div className="h-1.5 w-full bg-gray-100 rounded-full overflow-hidden">
                                                        <div
                                                            className={`h-full transition-all duration-700 ${req.cantidadRecibida >= req.cantidad ? 'bg-green-500' :
                                                                dynamicOrdered > 0 ? 'bg-blue-500' : 'bg-gray-300'
                                                                }`}
                                                            style={{ width: `${(req.cantidadRecibida / req.cantidad) * 100}%` }}
                                                        ></div>
                                                    </div>
                                                </div>
                                            </td>
                                            <td className="px-10 py-5 text-right">
                                                <span className={`px-3 py-1 rounded-lg text-[8px] font-black uppercase tracking-widest border ${req.estado === 'Completado' ? 'bg-green-50 text-green-600 border-green-100' :
                                                    req.estado === 'Parcial' ? 'bg-blue-50 text-blue-600 border-blue-100' :
                                                        req.estado === 'OC Emitida' ? 'bg-amber-50 text-amber-600 border-amber-100' :
                                                            'bg-red-50 text-red-600 border-red-100'
                                                    }`}>
                                                    {req.estado}
                                                </span>
                                            </td>
                                        </tr>
                                    );
                                })}
                            </tbody>
                        </table>
                    </div>

                    {/* SECCIÓN: Órdenes Generadas Localmente */}
                    <div className="bg-white rounded-[2.5rem] shadow-sm border border-gray-100 overflow-hidden">
                        <div className="p-8 border-b border-gray-100 bg-gray-50/50 flex justify-between items-center">
                            <div>
                                <h3 className="text-sm font-black text-gray-800 uppercase tracking-widest italic">Historial de Órdenes Vinculadas a OP</h3>
                                <p className="text-[10px] text-gray-400 font-bold uppercase mt-1">Órdenes de Compra y de Servicio registradas</p>
                            </div>
                        </div>
                        <table className="w-full text-left">
                            <thead className="bg-white border-b border-gray-100">
                                <tr>
                                    <th className="px-10 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest">Código Doc.</th>
                                    <th className="px-10 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest">Proveedor / Taller</th>
                                    <th className="px-10 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">Tipo</th>
                                    <th className="px-10 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">Fecha</th>
                                    <th className="px-10 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-right">Total Neto</th>
                                    <th className="px-10 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">Estado</th>
                                    <th className="px-10 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-right">Acciones</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-gray-50">
                                {localOCs.filter(oc => oc.idOP === selectedOP.idOP).map((oc) => (
                                    <tr key={oc.idOC || oc.id_oc || index} className="hover:bg-gray-50 transition-colors">
                                        <td className="px-10 py-5 font-black text-blue-600 text-sm italic">{oc.numeroOC || oc.idOC}</td>
                                        <td className="px-10 py-5">
                                            <div className="text-sm font-black text-gray-800 uppercase">{oc.proveedor}</div>
                                            {oc.motivoRechazo && <div className="text-[8px] text-red-500 font-bold uppercase mt-1 italic">Motivo: {oc.motivoRechazo}</div>}
                                        </td>
                                        <td className="px-10 py-5 text-center">
                                            <span className={`text-[8px] font-black px-2 py-0.5 rounded-full border ${oc.tipo === 'OS' ? 'bg-indigo-50 text-indigo-600 border-indigo-100' : 'bg-blue-50 text-blue-600 border-blue-100'}`}>
                                                {oc.tipo}
                                            </span>
                                        </td>
                                        <td className="px-10 py-5 text-center text-xs text-gray-400 font-black">{oc.fechaEmision || oc.fecha}</td>
                                        <td className="px-10 py-5 text-right font-black text-gray-900 text-sm">${(oc.montoTotal || oc.total || 0).toLocaleString()}</td>
                                        <td className="px-10 py-5 text-center">
                                            <span className={`px-3 py-1 rounded-lg text-[8px] font-black uppercase tracking-widest border ${oc.estado === 'Rechazado' ? 'bg-red-50 text-red-600 border-red-100' :
                                                oc.estado === 'Completado' ? 'bg-green-50 text-green-600 border-green-100' :
                                                    'bg-amber-50 text-amber-600 border-amber-100'
                                                }`}>
                                                {oc.estado}
                                            </span>
                                        </td>
                                        <td className="px-10 py-5 text-right">
                                            <div className="flex justify-end gap-2">
                                                <button
                                                    onClick={() => pdfService.generateOC(oc)}
                                                    className="px-3 py-1.5 bg-gray-100 text-gray-600 rounded-lg text-[9px] font-black uppercase tracking-widest hover:bg-gray-200 transition-all shadow-sm flex items-center gap-1"
                                                >
                                                    <FileText className="w-3 h-3" /> PDF
                                                </button>
                                                {oc.estado === 'Rechazado' ? (
                                                    <button
                                                        onClick={() => handleReemitOC(oc)}
                                                        className="px-3 py-1.5 bg-blue-600 text-white rounded-lg text-[9px] font-black uppercase tracking-widest hover:bg-black transition-all"
                                                    >
                                                        Re-emitir
                                                    </button>
                                                ) : (
                                                    <button
                                                        onClick={() => handleRejectOC(oc.idOC)}
                                                        className="px-3 py-1.5 bg-white border border-red-200 text-red-500 rounded-lg text-[9px] font-black uppercase tracking-widest hover:bg-red-50 transition-all"
                                                    >
                                                        Rechazar
                                                    </button>
                                                )}
                                            </div>
                                        </td>
                                    </tr>
                                ))}
                                {localOCs.filter(oc => oc.idOP === selectedOP.idOP).length === 0 && (
                                    <tr>
                                        <td colSpan="7" className="py-20 text-center text-[10px] font-black text-gray-300 uppercase tracking-widest italic">Sin órdenes registradas para esta OP</td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                </div>
            )}

            {/* Modal de Nueva OC */}
            {showNewOCModal && selectedOP && (
                <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/60 backdrop-blur-md animate-in fade-in duration-300">
                    <div className="bg-white w-full max-w-4xl rounded-[3rem] p-10 shadow-2xl animate-in zoom-in-95 duration-300 relative max-h-[90vh] overflow-y-auto">
                        <button onClick={() => setShowNewOCModal(false)} className="absolute top-8 right-8 p-3 bg-gray-50 rounded-2xl hover:bg-red-50 hover text-red-500 transition-all">
                            <X className="w-5 h-5 text-gray-400" />
                        </button>

                        <div className="mb-10">
                            <h2 className="text-3xl font-black text-gray-800 tracking-tight italic uppercase">Nueva Orden desde Producción</h2>
                            <div className="flex items-center space-x-3 mt-2">
                                <span className="text-[10px] font-black text-amber-600 bg-amber-50 px-3 py-1.5 rounded-full border border-amber-100 uppercase tracking-widest">OP: {selectedOP.idOP}</span>
                                <span className="text-[10px] font-black text-blue-600 bg-blue-50 px-3 py-1.5 rounded-full border border-blue-100 uppercase tracking-widest">NV: {selectedOP.notaVentaId}</span>
                            </div>
                        </div>

                        <div className="grid grid-cols-2 gap-8 mb-8">
                            <div className="space-y-4">
                                <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest block ml-1">Tipo de Documento</label>
                                <div className="flex bg-gray-50 p-1 rounded-2xl border border-gray-100">
                                    <button
                                        onClick={() => setTipoOC('PROVEEDOR')}
                                        className={`flex-1 py-4 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all ${tipoOC === 'PROVEEDOR' ? 'bg-white text-blue-600 shadow-sm border border-blue-50' : 'text-gray-400 opacity-60'}`}
                                    >
                                        Proveedor MP
                                    </button>
                                    <button
                                        onClick={() => setTipoOC('OS')}
                                        className={`flex-1 py-4 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all ${tipoOC === 'OS' ? 'bg-white text-indigo-600 shadow-sm border border-indigo-50' : 'text-gray-400 opacity-60'}`}
                                    >
                                        Servicio Ext. (OS)
                                    </button>
                                </div>
                            </div>

                        </div>

                        {/* OP Requirements Section */}
                        {mockOpDetails[selectedOP.idOP]?.requerimientos && (
                            <div className="mb-8 bg-amber-50/50 p-6 rounded-3xl border border-amber-100">
                                <div className="flex items-center justify-between mb-4">
                                    <h3 className="text-[10px] font-black text-amber-600 uppercase tracking-widest flex items-center">
                                        <AlertCircle className="w-3 h-3 mr-2" /> Requerimientos Pendientes de la OP
                                    </h3>
                                    <span className="text-[9px] font-bold text-amber-500 uppercase tracking-tighter italic">Selecciona para añadir a la OC</span>
                                </div>
                                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-3">
                                    {mockOpDetails[selectedOP.idOP].requerimientos.map(req => (
                                        <button
                                            key={req.id}
                                            onClick={() => {
                                                setOcItems([...ocItems, {
                                                    id: Date.now() + Math.random(),
                                                    nombreProducto: `${req.item} (${req.detalle})`,
                                                    color: req.detalle,
                                                    talla: 'N/A',
                                                    cantidad: parseInt(req.cantidad) || 1,
                                                    precioUnitario: 0
                                                }]);
                                            }}
                                            className="flex items-center justify-between p-3 bg-white rounded-xl border border-amber-200 hover:border-amber-500 hover:shadow-md transition-all text-left"
                                        >
                                            <div>
                                                <p className="text-[11px] font-black text-gray-800 uppercase">{req.item}</p>
                                                <p className="text-[9px] font-bold text-gray-400 uppercase tracking-tight">{req.cantidad} • {req.detalle}</p>
                                            </div>
                                            <Plus className="w-4 h-4 text-amber-400" />
                                        </button>
                                    ))}
                                </div>
                            </div>
                        )}

                        <div className="bg-white border-2 border-gray-50 rounded-[2.5rem] overflow-hidden shadow-sm mb-10">
                            <div className="p-5 bg-gray-50/50 border-b border-gray-50 flex justify-between items-center px-8">
                                <span className="text-[10px] font-black text-gray-400 uppercase tracking-widest italic">Detalle de Insumos / Servicios a Contratar</span>
                                <button onClick={addItem} className="p-2 bg-blue-600 text-white rounded-xl hover:bg-black transition-all">
                                    <Plus className="w-4 h-4" />
                                </button>
                            </div>
                            <table className="w-full text-left">
                                <thead className="bg-white border-b border-gray-50">
                                    <tr>
                                        <th className="px-8 py-4 text-[9px] font-black text-gray-400 uppercase tracking-widest">Descripción</th>
                                        <th className="px-8 py-4 text-[9px] font-black text-gray-400 uppercase tracking-widest text-center">Cant.</th>
                                        <th className="px-8 py-4 text-[9px] font-black text-gray-400 uppercase tracking-widest text-right">P. Unitario</th>
                                        <th className="px-8 py-4 text-[9px] font-black text-gray-400 uppercase tracking-widest text-right">Subtotal</th>
                                        <th className="px-8 py-4"></th>
                                    </tr>
                                </thead>
                                <tbody className="divide-y divide-gray-50">
                                    {ocItems.map((item) => (
                                        <tr key={item.id} className="group">
                                            <td className="px-8 py-4">
                                                <input
                                                    type="text"
                                                    value={item.nombreProducto}
                                                    onChange={(e) => updateItem(item.id, 'nombreProducto', e.target.value)}
                                                    className="w-full bg-transparent border-none outline-none font-black text-xs text-gray-800 uppercase"
                                                />
                                                <div className="flex gap-2 mt-1">
                                                    <input
                                                        type="text"
                                                        placeholder="COLOR"
                                                        className="text-[9px] font-bold text-gray-400 border-none bg-transparent outline-none w-20 uppercase"
                                                        value={item.color}
                                                        onChange={(e) => updateItem(item.id, 'color', e.target.value)}
                                                    />
                                                    <span className="text-gray-200">|</span>
                                                    <input
                                                        type="text"
                                                        placeholder="TALLA"
                                                        className="text-[9px] font-bold text-gray-400 border-none bg-transparent outline-none w-10 uppercase"
                                                        value={item.talla}
                                                        onChange={(e) => updateItem(item.id, 'talla', e.target.value)}
                                                    />
                                                </div>
                                            </td>
                                            <td className="px-8 py-4 text-center">
                                                <input
                                                    type="number"
                                                    value={item.cantidad}
                                                    onChange={(e) => updateItem(item.id, 'cantidad', parseInt(e.target.value) || 0)}
                                                    className="w-16 bg-gray-50 border-none outline-none rounded-lg p-2 text-xs font-black text-center"
                                                />
                                            </td>
                                            <td className="px-8 py-4 text-right">
                                                <div className="flex items-center justify-end space-x-1">
                                                    <span className="text-gray-300 text-[10px] font-black">$</span>
                                                    <input
                                                        type="number"
                                                        value={item.precioUnitario}
                                                        onChange={(e) => updateItem(item.id, 'precioUnitario', parseInt(e.target.value) || 0)}
                                                        className="w-24 bg-gray-50 border-none outline-none rounded-lg p-2 text-xs font-black text-right text-blue-600"
                                                    />
                                                </div>
                                            </td>
                                            <td className="px-8 py-4 text-xs font-black text-gray-800 text-right">
                                                ${((item.cantidad || 0) * (item.precioUnitario || 0)).toLocaleString()}
                                            </td>
                                            <td className="px-8 py-4 text-right">
                                                <button onClick={() => removeItem(item.id)} className="p-2 text-gray-300 hover:text-red-500 transition-colors">
                                                    <X className="w-4 h-4" />
                                                </button>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>

                        <div className="flex flex-col md:flex-row gap-6 items-center justify-between bg-slate-900 p-8 rounded-[2.5rem] text-white">
                            <div className="flex space-x-10">
                                <div>
                                    <p className="text-[10px] font-black text-blue-400 uppercase tracking-[0.2em] mb-1">Total Neto {tipoOC === 'OS' ? 'Servicio' : 'Compra'}</p>
                                    <p className="text-4xl font-black italic tracking-tighter shadow-blue-500">
                                        ${ocItems.reduce((sum, item) => sum + (item.cantidad * item.precioUnitario), 0).toLocaleString()}
                                    </p>
                                </div>
                                <div className="hidden border-l border-white/10 pl-10 md:block">
                                    <p className="text-[10px] font-black text-gray-400 uppercase tracking-[0.2em] mb-1">Fecha Emisión</p>
                                    <p className="text-xl font-black italic">{emissionDate.split('-').reverse().join('/')}</p>
                                </div>
                            </div>
                            <button
                                onClick={handleGenerateOC}
                                disabled={!selectedProvider || ocItems.length === 0}
                                className="w-full md:w-auto px-12 py-5 bg-blue-600 hover:bg-orange-500 disabled:opacity-50 disabled:bg-gray-800 text-white font-black rounded-2xl shadow-xl transition-all uppercase tracking-widest text-[10px] flex items-center justify-center active:scale-95"
                            >
                                <CheckCircle2 className="w-5 h-5 mr-3" />
                                Emitir {tipoOC === 'OS' ? 'Orden de Servicio' : 'Orden de Compra'}
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
