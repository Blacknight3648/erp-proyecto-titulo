import React, { useState, useEffect } from 'react';
import { FileText, Plus, X, Trash2, ChevronRight, Search, Filter, History, ShoppingCart, User, Package, Calculator, Save, AlertCircle, CheckCircle2, Wrench } from 'lucide-react';
import { api } from '../../../../remote/service/api';
import { pdfService } from '../../../../services/pdfService';
import PipelineTimeline from '../../../ui/common/PipelineTimeline';
import SearchFilterBar from '../../../ui/common/SearchFilterBar';
import NVTable from '../../../ui/sc/NVTable';
import SCSummaryCard from '../../../ui/sc/SCSummaryCard';
import { toast } from 'sonner';
import { validateNumericInput } from '../../../../utils/validations';

export default function RegistrosSC() {
    const [selectedNV, setSelectedNV] = useState(null);
    const [showNewSCModal, setShowNewSCModal] = useState(false);
    const [customer, setCustomer] = useState('');
    const [seller, setSeller] = useState('');
    const [scItems, setScItems] = useState([]);
    const [requester, setRequester] = useState('');
    const [linkedNV, setLinkedNV] = useState('');
    const [isKitSC, setIsKitSC] = useState(false);

    // API state
    const [notasVenta, setNotasVenta] = useState([]);
    const [proveedores, setProveedores] = useState([]);
    const [loading, setLoading] = useState(true);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [submitStatus, setSubmitStatus] = useState(null); // { type: 'success'|'error', message: '' }

    useEffect(() => {
        const loadData = async () => {
            try {
                setLoading(true);
                const [nvsRes, provRes] = await Promise.all([
                    api.get('/comercial/notas-venta'),
                    api.get('/shared/proveedores')
                ]);
                setNotasVenta(nvsRes.data || []);
                setProveedores(provRes.data || []);
            } catch (error) {
                console.error('Error cargando datos SC:', error);
            } finally {
                setLoading(false);
            }
        };
        loadData();
    }, []);

    const handleOpenModal = () => {
        if (selectedNV) {
            setCustomer(selectedNV.clienteNombre || selectedNV.cliente || '');
            setSeller(selectedNV.vendedorNombre || selectedNV.vendedor || '');
            setIsKitSC(selectedNV.esKit || selectedNV.isKit || false);
            const initialItems = (selectedNV.items || []).map(item => ({
                id: item.idItemNV || item.id || Date.now() + Math.random(),
                nombreProducto: item.nombreProducto || item.descripcion || item.garment || '',
                modelo: item.modelo || '',
                tela: item.tela || '',
                composicion: item.composicion || '',
                color: item.color || '',
                talla: item.talla || '',
                quantity: item.cantidad || item.quantity || 0,
                proveedorId: item.proveedorId || '',
                genero: item.genero || 'Unisex',
                generaOt: item.generaOt || false,
                detalleOt: item.detalleOt || '',
                productoId: item.productoId || '',
                logo: item.llevaLogo || item.logo || '',
                unitPrice: item.precioUnitario || item.unitPrice || 0,
                tipoItem: item.tipoItem || 'SC',
                sizes: item.sizes || { XS: 0, S: 0, M: 0, L: 0, XL: 0 },
                showSizeMenu: false
            }));
            setScItems(initialItems);
            setLinkedNV(selectedNV.idNV || selectedNV.id);
        } else {
            setCustomer('');
            setSeller('');
            setIsKitSC(false);
            setScItems([{
                id: Date.now(),
                nombreProducto: '',
                modelo: '',
                tela: '',
                composicion: '',
                color: '',
                talla: '',
                quantity: 0,
                proveedorId: '',
                genero: 'Unisex',
                generaOt: false,
                detalleOt: '',
                productoId: '',
                logo: '',
                unitPrice: 0,
                tipoItem: 'SC',
                sizes: { XS: 0, S: 0, M: 0, L: 0, XL: 0 },
                showSizeMenu: false
            }]);
            setLinkedNV(''); // Resetear si es manual
        }
        setShowNewSCModal(true);
    };

    const addItem = () => {
        setScItems([...scItems, {
            id: Date.now(),
            nombreProducto: '',
            modelo: '',
            tela: '',
            composicion: '',
            color: '',
            talla: '',
            quantity: 0,
            proveedorId: '',
            genero: 'Unisex',
            generaOt: false,
            detalleOt: '',
            productoId: '',
            logo: '',
            unitPrice: 0,
            tipoItem: 'SC',
            sizes: { XS: 0, S: 0, M: 0, L: 0, XL: 0 },
            showSizeMenu: false
        }]);
    };

    const removeItem = (id) => {
        setScItems(scItems.filter(item => item.id !== id));
    };

    const updateItem = (id, field, value) => {
        handleUpdateItem(id, field, value);
    };

    const updateSize = (itemId, size, val) => {
        const error = validateNumericInput(val, `Talla ${size}`);
        if (error) {
            toast.error(error);
            return;
        }

        setScItems(scItems.map(item => {
            if (item.id === itemId) {
                const newSizes = { ...item.sizes, [size]: parseInt(val) || 0 };
                const newTotal = Object.values(newSizes).reduce((a, b) => a + b, 0);
                return { ...item, sizes: newSizes, quantity: newTotal };
            }
            return item;
        }));
    };

    const totalUnits = scItems.reduce((sum, item) => sum + (parseInt(item.quantity) || 0), 0);

    const handleUpdateItem = (id, field, value) => {
        if (field === 'unitPrice' || field === 'quantity') {
            const label = field === 'unitPrice' ? 'Precio Estimado' : 'Cantidad';
            const error = validateNumericInput(value, label);
            if (error) {
                toast.error(error);
                return;
            }
        }

        setScItems(scItems.map(item => {
            if (item.id === id) {
                const updatedItem = { ...item, [field]: value };
                // Buscar precio sugerido si cambia producto o proveedor
                if (field === 'nombreProducto' || field === 'proveedorId') {
                    const targetProduct = field === 'nombreProducto' ? value : item.nombreProducto;
                    const currentProveedorId = field === 'proveedorId' ? value : item.proveedorId;

                    if (targetProduct && currentProveedorId) {
                        const provider = proveedores.find(p =>
                            (p.nombreProveedor === currentProveedorId || p.nombre === currentProveedorId) ||
                            (String(p.proveedorId) === String(currentProveedorId) || String(p.id) === String(currentProveedorId))
                        );
                        if (provider) {
                            const priceEntry = provider.precios?.find(pr =>
                                pr.garment.toLowerCase().includes(targetProduct.toLowerCase()) ||
                                targetProduct.toLowerCase().includes(pr.garment.toLowerCase())
                            );
                            if (priceEntry) {
                                updatedItem.unitPrice = priceEntry.price;
                            }
                        }
                    }
                }
                return updatedItem;
            }
            return item;
        }));
    };

    const handleSubmitSC = async () => {
        if (scItems.length === 0) {
            setSubmitStatus({ type: 'error', message: 'Debe agregar al menos un ítem' });
            return;
        }
        setIsSubmitting(true);
        setSubmitStatus(null);
        try {
            const payload = {
                tenantId: 1, // tenant por defecto en desarrollo
                notaVentaId: linkedNV ? parseInt(linkedNV) : null,
                items: scItems.map(item => ({
                    productoId: item.productoId ? parseInt(item.productoId) : null,
                    descripcionProducto: item.nombreProducto,
                    cantidad: item.quantity,
                    precioEstimadoUnitario: item.unitPrice || 0,
                    moneda: 'CLP',
                    tipo: item.tipoItem || 'SC'
                }))
            };
            await api.post('/adquisiciones/solicitudes-compra', payload);
            setSubmitStatus({ type: 'success', message: 'Solicitud de Compra creada exitosamente' });
            setTimeout(() => {
                setShowNewSCModal(false);
                setSubmitStatus(null);
            }, 2000);
        } catch (error) {
            const msg = error.response?.data?.message || error.message || 'Error al crear la SC';
            setSubmitStatus({ type: 'error', message: msg });
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="space-y-6">
            <div className="flex items-center justify-between">
                <div>
                    <h1 className="text-3xl font-bold text-gray-800">Solicitud de Compra (SC)</h1>
                    <p className="text-sm text-gray-400 font-bold mt-1">Gestión de requerimientos desde Ventas</p>
                </div>

                <div className="flex space-x-3">
                    <button
                        onClick={() => { setSelectedNV(null); handleOpenModal(); }}
                        className="flex items-center px-4 py-2 bg-indigo-600 text-white rounded-xl text-sm font-bold shadow-sm hover:bg-indigo-700 transition-colors"
                    >
                        <Plus className="w-4 h-4 mr-2" /> Nueva SC Manual
                    </button>
                </div>
            </div>

            {/* Filters */}
            <SearchFilterBar onSearch={() => { }} />

            {/* Pipeline Visual Mockup */}
            <PipelineTimeline
                steps={['Nota de Venta', 'Evaluación', 'Emisión SC', 'Aprobación']}
                currentStep={2}
            />

            {/* Main Content: Split View */}
            {loading ? (
                <div className="flex justify-center py-20">
                    <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
                </div>
            ) : !selectedNV ? (
                /* LIST VIEW */
                <NVTable nvs={notasVenta} onSelectNV={setSelectedNV} />
            ) : (
                /* DETAIL VIEW */
                <div className="space-y-6 animate-in slide-in-from-right-8 duration-500">
                    <div className="flex items-center space-x-4">
                        <button
                            onClick={() => setSelectedNV(null)}
                            className="p-2 hover:bg-gray-100 rounded-full transition-colors"
                        >
                            <ChevronRight className="w-6 h-6 rotate-180 text-gray-500" />
                        </button>
                        <div>
                            <h2 className="text-2xl font-bold text-gray-800">Detalle de Nota de Venta {selectedNV.id}</h2>
                            <p className="text-sm text-gray-500">Generar Solicitud de Compra para este requerimiento</p>
                        </div>
                    </div>

                    <div className="grid grid-cols-3 gap-6">
                        {/* NV Details Card */}
                        <div className="col-span-2 bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
                            <h3 className="text-lg font-bold text-gray-800 mb-4">Items Solicitados</h3>
                            <div className="bg-gray-50 rounded-xl overflow-hidden">
                                <table className="w-full text-left">
                                    <thead className="border-b border-gray-200">
                                        <tr>
                                            <th className="px-6 py-3 text-xs font-bold text-gray-500 uppercase">Descripción</th>
                                            <th className="px-6 py-3 text-xs font-bold text-gray-500 uppercase text-right">Cantidad</th>
                                        </tr>
                                    </thead>
                                    <tbody className="divide-y divide-gray-200">
                                        {selectedNV.items.map((item) => (
                                            <tr key={item.id}>
                                                <td className="px-6 py-3 text-sm text-gray-700">{item.desc}</td>
                                                <td className="px-6 py-3 text-sm font-bold text-gray-900 text-right">{item.qty} {item.unit}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        {/* Action Card */}
                        <div className="bg-indigo-50 border border-indigo-100 rounded-2xl p-6 flex flex-col justify-center items-center text-center">
                            <ShoppingCart className="w-12 h-12 text-indigo-600 mb-4" />
                            <h3 className="text-lg font-bold text-indigo-900 mb-2">Generar Solicitud</h3>
                            <p className="text-sm text-indigo-700 mb-6">
                                Crear una Solicitud de Compra basada en los items de esta Nota de Venta.
                            </p>
                            <button
                                onClick={handleOpenModal}
                                className="w-full py-3 bg-indigo-600 text-white font-bold rounded-xl shadow-lg shadow-indigo-200 hover:bg-indigo-700 transition-all uppercase text-xs tracking-wider"
                            >
                                Generar SC
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {/* MODAL: Formulario de SC (Adaptado) */}
            {showNewSCModal && (
                <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/60 backdrop-blur-sm animate-in fade-in duration-300">
                    <div className="bg-white w-full max-w-7xl rounded-[2.5rem] p-8 shadow-2xl animate-in zoom-in-95 duration-300 scale-100 relative max-h-[90vh] overflow-y-auto">
                        <button
                            onClick={() => setShowNewSCModal(false)}
                            className="absolute top-6 right-6 p-2 bg-gray-50 rounded-full hover:bg-gray-100 transition-colors"
                        >
                            <X className="w-5 h-5 text-gray-500" />
                        </button>

                        <div className="flex items-center space-x-3 mb-8">
                            <div className="w-10 h-10 bg-indigo-100 rounded-xl flex items-center justify-center">
                                <ShoppingCart className="w-5 h-5 text-indigo-600" />
                            </div>
                            <div>
                                <h2 className="text-2xl font-black text-gray-800 tracking-tight">Nueva Solicitud de Compra</h2>
                                {isKitSC && (
                                    <div className="mt-1 flex items-center space-x-2">
                                        <div className="px-3 py-1 bg-blue-100 text-blue-700 rounded-full text-[10px] font-black uppercase tracking-widest border border-blue-200 flex items-center shadow-sm">
                                            <Package className="w-3 h-3 mr-1.5" />
                                            SC con Kit personalizado
                                        </div>
                                    </div>
                                )}
                                {selectedNV && (
                                    <div className="flex flex-col mt-2">
                                        <p className="text-sm font-bold text-indigo-600">Basada en {selectedNV.id}</p>
                                        <p className="text-xs font-black text-gray-400 uppercase tracking-widest mt-1">Cliente: {selectedNV.cliente}</p>
                                    </div>
                                )}
                            </div>
                        </div>

                        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                            {/* Form Area */}
                            <div className="lg:col-span-2 space-y-6">
                                <div className="grid grid-cols-2 gap-4 bg-gray-50 p-6 rounded-[2rem]">
                                    <div>
                                        <div className="flex items-center space-x-2 mb-2">
                                            <User className="w-4 h-4 text-gray-500" />
                                            <label className="text-xs font-black text-gray-400 uppercase tracking-widest">Cliente</label>
                                        </div>
                                        <input
                                            className="w-full p-4 bg-white border border-gray-100 rounded-2xl font-bold text-sm text-gray-700 outline-none"
                                            value={customer}
                                            readOnly={!!selectedNV}
                                            onChange={(e) => setCustomer(e.target.value)}
                                            placeholder="Nombre del cliente..."
                                        />
                                    </div>
                                    <div>
                                        <div className="flex items-center space-x-2 mb-2">
                                            <User className="w-4 h-4 text-gray-500" />
                                            <label className="text-xs font-black text-gray-400 uppercase tracking-widest">Vendedor</label>
                                        </div>
                                        <input
                                            className="w-full p-4 bg-white border border-gray-100 rounded-2xl font-bold text-sm text-gray-700 outline-none"
                                            value={seller}
                                            readOnly={!!selectedNV}
                                            onChange={(e) => setSeller(e.target.value)}
                                            placeholder="Vendedor asignado..."
                                        />
                                    </div>
                                </div>

                                <div className="bg-gray-50 p-6 rounded-[2rem] space-y-4">
                                    {!selectedNV && (
                                        <div>
                                            <div className="flex items-center space-x-2 mb-2">
                                                <FileText className="w-4 h-4 text-gray-500" />
                                                <label className="text-xs font-black text-gray-500 uppercase tracking-widest">Vincular Nota de Venta</label>
                                            </div>
                                            <select
                                                className="w-full p-4 bg-white border border-gray-200 rounded-2xl font-bold text-sm text-gray-700 outline-none focus:ring-2 focus:ring-indigo-200"
                                                value={linkedNV}
                                                onChange={(e) => setLinkedNV(e.target.value)}
                                            >
                                                <option value="">Seleccionar NV (Opcional)...</option>
                                                {notasVenta.map(nv => (
                                                    <option key={nv.idNV || nv.id} value={nv.idNV || nv.id}>
                                                        {nv.numeroNV || nv.idNV || nv.id} - {nv.clienteNombre || nv.cliente}
                                                    </option>
                                                ))}
                                            </select>
                                        </div>
                                    )}

                                    <div>
                                        <div className="flex items-center space-x-2 mb-2">
                                            <History className="w-4 h-4 text-gray-500" />
                                            <label className="text-xs font-black text-gray-500 uppercase tracking-widest">Departamento Solicitante</label>
                                        </div>
                                        <select
                                            className="w-full p-4 bg-white border border-gray-200 rounded-2xl font-bold text-sm text-gray-700 outline-none focus:ring-2 focus:ring-indigo-200"
                                            value={requester}
                                            onChange={(e) => setRequester(e.target.value)}
                                        >
                                            <option value="">Seleccionar Departamento...</option>
                                            <option value="1">DEPARTAMENTO DE PRODUCCIÓN</option>
                                            <option value="2">CENTRO DE CORTE Y ESTAMPADO</option>
                                            <option value="3">ÁREA DE CALIDAD Y EMPAQUE</option>
                                        </select>
                                    </div>
                                </div>

                                <div className="bg-white border border-gray-200 rounded-[2rem] overflow-hidden">
                                    <div className="p-6 bg-gray-50 border-b border-gray-100 flex justify-between items-center">
                                        <span className="font-black text-xs uppercase text-gray-500 tracking-widest">Items del Requerimiento</span>
                                        <button onClick={addItem} className="text-indigo-600 hover:bg-indigo-50 p-1 rounded-lg transition-colors">
                                            <Plus className="w-4 h-4" />
                                        </button>
                                    </div>
                                    <div className="p-4 space-y-3">
                                        {scItems.map((item, index) => (
                                            <div key={item.id} className="p-8 rounded-[2.5rem] border-2 border-gray-50 hover:border-indigo-100 transition-all space-y-6 relative group/item overflow-visible">
                                                {/* Badge de bloqueo si viene de NV y tiene OT */}
                                                {!!selectedNV && item.generaOt && (
                                                    <div className="absolute -top-3 left-8 px-4 py-1 bg-amber-100 text-amber-700 rounded-full text-[9px] font-black uppercase tracking-widest border border-amber-200 shadow-sm z-10 flex items-center gap-2">
                                                        <AlertCircle className="w-3 h-3" />
                                                        Ítem con Personalización (Bloqueado)
                                                    </div>
                                                )}

                                                <div className="grid grid-cols-12 gap-6 items-center text-left">
                                                    {/* 1. Prenda */}
                                                    <div className="col-span-4">
                                                        <label className="text-[9px] font-black text-gray-400 uppercase tracking-widest mb-2 block ml-1">Prenda / Descripción</label>
                                                        <input
                                                            className={`w-full p-4 border-none rounded-2xl text-[11px] font-black uppercase tracking-tight outline-none focus:ring-2 focus:ring-indigo-100 transition-all ${!!selectedNV && item.generaOt ? 'bg-gray-100 text-gray-400 cursor-not-allowed' : 'bg-gray-50'}`}
                                                            value={item.nombreProducto}
                                                            onChange={(e) => handleUpdateItem(item.id, 'nombreProducto', e.target.value)}
                                                            readOnly={!!selectedNV && item.generaOt}
                                                            placeholder="Ej: Polera Jersey 24/1"
                                                        />
                                                    </div>

                                                    {/* 2. Código */}
                                                    <div className="col-span-2">
                                                        <label className="text-[9px] font-black text-gray-400 uppercase tracking-widest mb-2 block ml-1">Código / SKU</label>
                                                        <input
                                                            value={item.productoId}
                                                            placeholder="SKU-001"
                                                            onChange={(e) => handleUpdateItem(item.id, 'productoId', e.target.value)}
                                                            readOnly={!!selectedNV && item.generaOt}
                                                            className={`w-full p-4 border-none rounded-2xl text-[11px] font-black outline-none focus:ring-2 focus:ring-indigo-100 transition-all uppercase ${!!selectedNV && item.generaOt ? 'bg-gray-100 text-gray-400 cursor-not-allowed' : 'bg-gray-50'}`}
                                                        />
                                                    </div>

                                                    {/* 3. Género */}
                                                    <div className="col-span-2">
                                                        <label className="text-[9px] font-black text-gray-400 uppercase tracking-widest mb-2 block text-center">Género</label>
                                                        <select
                                                            className={`w-full p-4 border-none rounded-2xl text-[11px] font-black uppercase outline-none focus:ring-2 focus:ring-indigo-100 transition-all appearance-none text-center cursor-pointer ${!!selectedNV && item.generaOt ? 'bg-gray-100 text-gray-400 cursor-not-allowed' : 'bg-gray-50'}`}
                                                            value={item.genero}
                                                            onChange={(e) => handleUpdateItem(item.id, 'genero', e.target.value)}
                                                            disabled={!!selectedNV && item.generaOt}
                                                        >
                                                            <option value="Unisex">Unis.</option>
                                                            <option value="Masculino">Masc.</option>
                                                            <option value="Femenino">Fem.</option>
                                                        </select>
                                                    </div>

                                                    {/* 4. Cantidad / Tallas */}
                                                    <div className="col-span-2 relative">
                                                        <label className="text-[9px] font-black text-gray-400 uppercase tracking-widest mb-2 block text-center italic underline underline-offset-4 decoration-gray-200">Total Prends.</label>
                                                        <button
                                                            onClick={() => !(!!selectedNV && item.generaOt) && handleUpdateItem(item.id, 'showSizeMenu', !item.showSizeMenu)}
                                                            className={`w-full p-4 border-none text-white rounded-2xl text-[11px] font-black text-center transition-all shadow-xl shadow-gray-200 ${!!selectedNV && item.generaOt ? 'bg-slate-400 cursor-not-allowed' : 'bg-slate-900 hover:bg-black'}`}
                                                            disabled={!!selectedNV && item.generaOt}
                                                        >
                                                            {item.quantity}
                                                        </button>

                                                        {item.showSizeMenu && (
                                                            <div className="absolute top-full left-0 mt-3 w-56 bg-white rounded-[2rem] shadow-2xl border border-gray-100 p-6 z-50 animate-in fade-in zoom-in-95 duration-200 border-t-4 border-indigo-600">
                                                                <div className="flex justify-between items-center mb-4">
                                                                    <span className="text-[9px] font-black text-gray-400 uppercase tracking-[0.2em]">Curva de Tallas</span>
                                                                    <button onClick={() => handleUpdateItem(item.id, 'showSizeMenu', false)} className="hover:rotate-90 transition-transform"><X className="w-3 h-3 text-gray-300" /></button>
                                                                </div>
                                                                <div className="space-y-3">
                                                                    {['XS', 'S', 'M', 'L', 'XL'].map(size => (
                                                                        <div key={size} className="flex items-center justify-between group/size">
                                                                            <span className="text-[10px] font-black text-gray-400 group-hover/size:text-indigo-600 transition-colors w-8">{size}</span>
                                                                            <input
                                                                                type="number"
                                                                                value={item.sizes[size]}
                                                                                onChange={(e) => updateSize(item.id, size, e.target.value)}
                                                                                className="w-20 p-2 bg-gray-50 border-none rounded-xl text-[11px] font-black text-center focus:ring-2 focus:ring-indigo-100 transition-all"
                                                                            />
                                                                        </div>
                                                                    ))}
                                                                </div>
                                                                <div className="mt-4 pt-4 border-t border-dashed border-gray-100 flex justify-between items-center">
                                                                    <span className="text-[9px] font-black text-gray-800 uppercase italic">Summ. Total</span>
                                                                    <span className="text-[11px] font-black text-indigo-600 bg-indigo-50 px-3 py-1 rounded-lg">{item.quantity} units</span>
                                                                </div>
                                                            </div>
                                                        )}
                                                    </div>

                                                    {/* 5. Acciones */}
                                                    <div className="col-span-2 flex justify-end items-end gap-2">
                                                        <button
                                                            onClick={() => !(!!selectedNV && item.generaOt) && handleUpdateItem(item.id, 'generaOt', !item.generaOt)}
                                                            className={`p-4 rounded-2xl transition-all shadow-sm ${item.generaOt ? 'bg-orange-600 text-white shadow-orange-100' : 'bg-gray-50 text-gray-300 hover:text-orange-500 hover:bg-orange-50'} ${!!selectedNV && item.generaOt ? 'opacity-50 cursor-not-allowed' : ''}`}
                                                            title="¿Lleva Logo / Personalizado?"
                                                            disabled={!!selectedNV && item.generaOt}
                                                        >
                                                            <Wrench className="w-4 h-4" />
                                                        </button>
                                                        <button
                                                            onClick={() => !(!!selectedNV && item.generaOt) && removeItem(item.id)}
                                                            className={`p-4 text-gray-300 rounded-2xl transition-all shadow-sm bg-gray-50 ${!!selectedNV && item.generaOt ? 'cursor-not-allowed opacity-20' : 'hover:text-red-500 hover:bg-red-50'}`}
                                                            disabled={!!selectedNV && item.generaOt}
                                                        >
                                                            <Trash2 className="w-4 h-4" />
                                                        </button>
                                                    </div>
                                                </div>

                                                {/* Fila 2: Línea Técnica (Composicion / Proveedor) */}
                                                <div className="grid grid-cols-12 gap-6 pt-4 border-t border-gray-100">
                                                    <div className="col-span-4">
                                                        <label className="text-[9px] font-black text-gray-400 uppercase tracking-widest mb-2 block text-xs">Composición</label>
                                                        <input
                                                            className={`w-full p-3 border-none rounded-xl text-[11px] font-bold outline-none ${!!selectedNV && item.generaOt ? 'bg-gray-100 text-gray-400 cursor-not-allowed' : 'bg-gray-50'}`}
                                                            value={item.composicion || ''}
                                                            onChange={(e) => handleUpdateItem(item.id, 'composicion', e.target.value)}
                                                            readOnly={!!selectedNV && item.generaOt}
                                                        />
                                                    </div>
                                                    <div className="col-span-4">
                                                        <label className="text-[9px] font-black text-gray-400 uppercase tracking-widest mb-2 block text-xs">Proveedor Sugerido</label>
                                                        <select
                                                            className={`w-full p-4 border-none rounded-2xl text-xs font-black uppercase tracking-widest outline-none focus:ring-2 focus:ring-indigo-500 transition-all appearance-none cursor-pointer ${!!selectedNV && item.generaOt ? 'bg-gray-100 text-gray-400 cursor-not-allowed' : 'bg-gray-50'}`}
                                                            value={item.proveedorId || ''}
                                                            onChange={(e) => handleUpdateItem(item.id, 'proveedorId', e.target.value)}
                                                            disabled={!!selectedNV && item.generaOt}
                                                        >
                                                            <option value="">Seleccionar Proveedor Sugerido...</option>
                                                            {proveedores.map(p => (
                                                                <option key={p.proveedorId || p.id} value={p.proveedorId || p.id}>
                                                                    {p.nombreProveedor || p.nombre}
                                                                </option>
                                                            ))}
                                                        </select>
                                                    </div>
                                                    <div className="col-span-2">
                                                        <label className="text-[9px] font-black text-gray-400 uppercase tracking-widest mb-2 block text-center">P. Unit</label>
                                                        <input
                                                            type="number"
                                                            className={`w-full p-4 border-none rounded-2xl text-[11px] font-black text-center outline-none focus:ring-2 focus:ring-indigo-100 transition-all ${!!selectedNV && item.generaOt ? 'bg-gray-100 text-gray-400 cursor-not-allowed' : 'bg-gray-50'}`}
                                                            value={item.unitPrice}
                                                            onChange={(e) => handleUpdateItem(item.id, 'unitPrice', parseFloat(e.target.value) || 0)}
                                                            readOnly={!!selectedNV && item.generaOt}
                                                        />
                                                    </div>
                                                    <div className="col-span-2">
                                                        <label className="text-[9px] font-black text-gray-400 uppercase tracking-widest mb-2 block text-right pr-1 italic">Total Línea</label>
                                                        <div className="p-4 bg-indigo-600 rounded-2xl text-[11px] font-black text-white text-right shadow-xl shadow-indigo-100">
                                                            ${((item.unitPrice || 0) * item.quantity).toLocaleString('es-CL')}
                                                        </div>
                                                    </div>
                                                </div>

                                                {item.generaOt && (
                                                    <div className="mt-4 animate-in slide-in-from-top-4 duration-300">
                                                        <label className="text-[9px] font-black text-orange-600 uppercase tracking-widest mb-2 block ml-1 underline underline-offset-4 decoration-orange-200">Especificaciones de Personalización / OT</label>
                                                        <textarea
                                                            className="w-full p-4 bg-orange-50/50 border border-orange-100 rounded-2xl text-[11px] font-bold text-orange-700 outline-none placeholder:text-orange-200 italic"
                                                            placeholder="Describa el logo, bordado o ajuste especial requerido..."
                                                            value={item.detalleOt || ''}
                                                            onChange={(e) => handleUpdateItem(item.id, 'detalleOt', e.target.value)}
                                                        />
                                                    </div>
                                                )}
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            </div>

                            {/* Summary Sidebar in Modal (Simplified) */}
                            <SCSummaryCard
                                totalUnits={totalUnits}
                                isManual={!selectedNV}
                                generatePDF={() => pdfService.generateSC({
                                    id: linkedNV || 'N/A',
                                    customer,
                                    requester,
                                    items: scItems
                                })}
                                disabledPdf={scItems.length === 0}
                                onCreate={handleSubmitSC}
                                isSubmitting={isSubmitting}
                                submitStatus={submitStatus}
                            />
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
