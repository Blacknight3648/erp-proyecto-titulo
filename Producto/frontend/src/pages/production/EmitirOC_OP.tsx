import { useState, useMemo, useEffect, useCallback } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Search, ChevronRight, ShoppingCart, X, CheckCircle2, Package, Activity, Wrench, FileText, Info } from 'lucide-react';
import { toast } from 'sonner';
import { OrdenProduccionService } from '../../remote/service/OrdenProduccionService';
import { HojaCompraService } from '../../remote/service/HojaCompraService';
import { OrdenCompraService } from '../../remote/service/OrdenCompraService';
import { OrdenServicioService } from '../../remote/service/OrdenServicioService';
import { ProveedorService } from '../../remote/service/ProveedorService';
import { pdfService } from '../../remote/service/pdfService';
import { clampNonNegative } from '../../utils/validations';

const ESTADOS_ACTIVOS = new Set(['PENDIENTE', 'EN_PROCESO', 'DETENIDA']);
const TIPOS_SERVICIO = ['BORDADO', 'ESTAMPADO', 'LAVADO', 'SUBLIMADO', 'OTRO'];

const formatCLP = (value) => `$${Math.round(value || 0).toLocaleString('es-CL')}`;

export default function EmitirOC_OP() {
    const [searchQuery, setSearchQuery] = useState('');
    const [ops, setOps] = useState([]);
    const [loadingOps, setLoadingOps] = useState(true);
    const [selectedOP, setSelectedOP] = useState(null);

    const [hc, setHc] = useState(null);
    const [loadingHc, setLoadingHc] = useState(false);
    const [ordenesVinculadas, setOrdenesVinculadas] = useState([]);

    const [proveedores, setProveedores] = useState([]);
    const [showNewOSModal, setShowNewOSModal] = useState(false);
    const [osForm, setOsForm] = useState({ proveedorId: '', tipoServicio: 'BORDADO', cantidadPactada: '', precioUnitario: '', fechaEntregaEstimada: '', descripcionTrabajo: '' });
    const [submittingOS, setSubmittingOS] = useState(false);

    const navigate = useNavigate();
    const location = useLocation();

    useEffect(() => {
        OrdenProduccionService.getAll()
            .then(setOps)
            .catch((err) => console.error('Error cargando Órdenes de Producción:', err))
            .finally(() => setLoadingOps(false));
        ProveedorService.getAll()
            .then(setProveedores)
            .catch((err) => console.error('Error cargando proveedores:', err));
    }, []);

    const cargarDetalleOP = useCallback(async (op) => {
        setSelectedOP(op);
        setLoadingHc(true);
        setOrdenesVinculadas([]);
        try {
            const hcData = await HojaCompraService.getByOpId(op.idOP);
            setHc(hcData);

            const ocIds = [...new Set((hcData?.items || []).map(i => i.ocId).filter(Boolean))];
            const ocs = await Promise.all(ocIds.map(id => OrdenCompraService.getById(id).catch(() => null)));
            setOrdenesVinculadas(ocs.filter(Boolean));
        } catch (err) {
            console.error(`Error cargando Hoja de Compra de la OP ${op.idOP}:`, err);
            setHc(null);
        } finally {
            setLoadingHc(false);
        }
    }, []);

    // Auto-abrir el detalle si la OP llega vía deep-link (ej. desde el detalle de una OP)
    useEffect(() => {
        if (location.state?.op) {
            cargarDetalleOP(location.state.op);
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [location.state]);

    const activeOPs = useMemo(() => {
        const q = searchQuery.toLowerCase();
        return ops.filter(op =>
            ESTADOS_ACTIVOS.has(op.estado) &&
            ((op.numeroOP || '').toLowerCase().includes(q) || String(op.notaVentaId || '').includes(q))
        );
    }, [ops, searchQuery]);

    const nombreProveedor = (proveedorId) =>
        proveedores.find(p => p.proveedorId === proveedorId)?.nombreProveedor || `Proveedor #${proveedorId}`;

    const handleBack = () => {
        setSelectedOP(null);
        setHc(null);
        setOrdenesVinculadas([]);
    };

    const irAGenerarOFlujoDeCompra = () => {
        navigate('/produccion/hoja-compra', { state: { opId: selectedOP.idOP } });
    };

    const abrirNuevaOS = () => {
        setOsForm({ proveedorId: '', tipoServicio: 'BORDADO', cantidadPactada: '', precioUnitario: '', fechaEntregaEstimada: '', descripcionTrabajo: '' });
        setShowNewOSModal(true);
    };

    const handleCrearOS = async () => {
        if (!osForm.proveedorId || !osForm.cantidadPactada) {
            toast.error('Seleccione un proveedor y una cantidad pactada.');
            return;
        }
        setSubmittingOS(true);
        try {
            await OrdenServicioService.crear({
                opId: selectedOP.idOP,
                proveedorId: Number(osForm.proveedorId),
                tipoServicio: osForm.tipoServicio,
                cantidadPactada: Number(osForm.cantidadPactada),
                precioUnitario: osForm.precioUnitario ? Number(osForm.precioUnitario) : 0,
                fechaEntregaEstimada: osForm.fechaEntregaEstimada || null,
                descripcionTrabajo: osForm.descripcionTrabajo || null,
            });
            toast.success('Orden de Servicio creada correctamente.');
            setShowNewOSModal(false);
        } catch (err) {
            console.error('Error creando OS:', err);
            toast.error(err?.response?.data?.message || 'No se pudo crear la Orden de Servicio.');
        } finally {
            setSubmittingOS(false);
        }
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

            {!selectedOP ? (
                <>
                    <div className="bg-white p-4 rounded-2xl shadow-sm border border-gray-100 flex flex-wrap gap-4 items-center">
                        <div className="relative flex-1 min-w-[200px]">
                            <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 w-4 h-4" />
                            <input
                                type="text"
                                placeholder="Buscar por N° OP o Nota de Venta..."
                                className="w-full pl-10 pr-4 py-2 bg-gray-50 border-none rounded-xl text-sm focus:ring-2 focus:ring-blue-500 outline-none"
                                value={searchQuery}
                                onChange={(e) => setSearchQuery(e.target.value)}
                            />
                        </div>
                    </div>

                    <div className="bg-white rounded-[2.5rem] shadow-sm border border-gray-100 overflow-hidden animate-in fade-in duration-500">
                        <table className="w-full text-left">
                            <thead className="bg-gray-50 border-b border-gray-100">
                                <tr>
                                    <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest">N° OP</th>
                                    <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">NV Origen</th>
                                    <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">Estado</th>
                                    <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-right">Acciones</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-gray-50">
                                {loadingOps && (
                                    <tr><td colSpan="4" className="py-16 text-center text-[10px] font-black text-gray-300 uppercase tracking-widest italic">Cargando Órdenes de Producción...</td></tr>
                                )}
                                {!loadingOps && activeOPs.map((op) => (
                                    <tr
                                        key={op.idOP}
                                        className="hover:bg-amber-50/40 transition-all group cursor-pointer border-l-4 border-transparent hover:border-amber-500"
                                        onClick={() => cargarDetalleOP(op)}
                                    >
                                        <td className="px-8 py-6">
                                            <div className="flex items-center">
                                                <div className="bg-amber-100 p-2.5 rounded-xl mr-4 group-hover:bg-amber-600 group-hover:text-white transition-all">
                                                    <Package className="w-5 h-5 text-amber-600 group-hover:text-white" />
                                                </div>
                                                <div className="font-black text-gray-900 text-sm tracking-tight italic">{op.numeroOP}</div>
                                            </div>
                                        </td>
                                        <td className="px-8 py-6 text-center">
                                            <span className="text-[10px] font-black text-blue-600 bg-blue-50 px-3 py-1.5 rounded-lg border border-blue-100 uppercase tracking-widest italic">NV {op.notaVentaId}</span>
                                        </td>
                                        <td className="px-8 py-6 text-center">
                                            <span className="text-[10px] font-black text-gray-600 uppercase">{op.estado}</span>
                                            {op.alertas?.length > 0 && (
                                                <div className="text-[9px] font-black text-red-500 uppercase mt-1">Atrasada: {op.alertas[0].motivo}</div>
                                            )}
                                        </td>
                                        <td className="px-8 py-6 text-right">
                                            <button className="p-2 bg-gray-50 text-gray-300 rounded-xl group-hover:bg-amber-600 group-hover:text-white transition-all scale-90 group-hover:scale-110">
                                                <ChevronRight className="w-5 h-5" />
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                                {!loadingOps && activeOPs.length === 0 && (
                                    <tr><td colSpan="4" className="py-16 text-center text-[10px] font-black text-gray-300 uppercase tracking-widest italic">Sin OPs activas</td></tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                </>
            ) : (
                <div className="space-y-8 animate-in slide-in-from-right-8 duration-500 pb-20">
                    <div className="flex items-center space-x-6">
                        <button
                            onClick={handleBack}
                            className="p-3 bg-white border border-gray-100 shadow-sm rounded-2xl hover:bg-gray-50 transition-all text-gray-400 hover:text-amber-600 active:scale-95"
                        >
                            <ChevronRight className="w-5 h-5 rotate-180" />
                        </button>
                        <div className="flex-1">
                            <h2 className="text-2xl font-black text-gray-800 tracking-tight italic">Gestión de Compra OP {selectedOP.numeroOP}</h2>
                            <p className="text-[10px] text-gray-500 font-black uppercase tracking-widest mt-1">NV {selectedOP.notaVentaId}</p>
                        </div>
                        <div className="flex space-x-3">
                            <button
                                onClick={irAGenerarOFlujoDeCompra}
                                className="flex items-center px-6 py-3 bg-blue-600 text-white rounded-2xl font-black text-[10px] uppercase tracking-widest shadow-xl shadow-blue-100 hover:bg-blue-700 hover:-translate-y-0.5 transition-all"
                            >
                                <ShoppingCart className="w-4 h-4 mr-2" />
                                Ir a Hoja de Compra / OC
                            </button>
                            <button
                                onClick={abrirNuevaOS}
                                className="flex items-center px-6 py-3 bg-indigo-600 text-white rounded-2xl font-black text-[10px] uppercase tracking-widest shadow-xl shadow-indigo-100 hover:bg-indigo-700 hover:-translate-y-0.5 transition-all"
                            >
                                <Wrench className="w-4 h-4 mr-2" />
                                Nueva OS (Servicio)
                            </button>
                        </div>
                    </div>

                    <div className="bg-white rounded-[2.5rem] shadow-sm border border-gray-100 overflow-hidden">
                        <div className="p-8 border-b border-gray-100 bg-amber-50/20 flex justify-between items-center">
                            <div>
                                <h3 className="text-sm font-black text-gray-800 uppercase tracking-widest italic">Requerimientos de Producción (Hoja de Compra)</h3>
                                <p className="text-[10px] text-gray-400 font-bold uppercase mt-1">Insumos requeridos y su vínculo con Órdenes de Compra</p>
                            </div>
                        </div>

                        {loadingHc ? (
                            <div className="py-20 text-center text-[10px] font-black text-gray-300 uppercase tracking-widest italic">Cargando Hoja de Compra...</div>
                        ) : !hc ? (
                            <div className="py-16 flex flex-col items-center gap-4">
                                <Info className="w-8 h-8 text-gray-300" />
                                <p className="text-[10px] font-black text-gray-400 uppercase tracking-widest italic">Aún no se ha generado una Hoja de Compra para esta OP</p>
                                <button
                                    onClick={irAGenerarOFlujoDeCompra}
                                    className="px-6 py-2.5 bg-amber-600 text-white rounded-xl text-[10px] font-black uppercase tracking-widest hover:bg-amber-700 transition-all"
                                >
                                    Generar Hoja de Compra
                                </button>
                            </div>
                        ) : (
                            <table className="w-full text-left">
                                <thead className="bg-white border-b border-gray-100">
                                    <tr>
                                        <th className="px-10 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest">Insumo</th>
                                        <th className="px-10 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">Cant. a Comprar</th>
                                        <th className="px-10 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">Proveedor</th>
                                        <th className="px-10 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">OC Vinculada</th>
                                        <th className="px-10 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-right">Estado</th>
                                    </tr>
                                </thead>
                                <tbody className="divide-y divide-gray-50">
                                    {hc.items.map((item) => {
                                        const oc = ordenesVinculadas.find(o => o.idOC === item.ocId);
                                        return (
                                            <tr key={item.idHCItem} className="hover:bg-gray-50 transition-colors group">
                                                <td className="px-10 py-5">
                                                    <div className="flex items-center gap-3">
                                                        <div className="p-2 bg-gray-100 rounded-lg group-hover:bg-amber-100 transition-colors">
                                                            <Package className="w-4 h-4 text-gray-400 group-hover:text-amber-600" />
                                                        </div>
                                                        <div className="text-sm font-black text-gray-800 tracking-tight uppercase">{item.nombreInsumo}</div>
                                                    </div>
                                                </td>
                                                <td className="px-10 py-5 text-center text-xs font-bold text-gray-600">{item.cantidadAComprar ?? item.cantidadRequerida}</td>
                                                <td className="px-10 py-5 text-center text-xs font-bold text-blue-600">{item.proveedorNombre || '-'}</td>
                                                <td className="px-10 py-5 text-center text-xs font-black text-gray-700">{item.numeroOC || '-'}</td>
                                                <td className="px-10 py-5 text-right">
                                                    <span className={`px-3 py-1 rounded-lg text-[8px] font-black uppercase tracking-widest border ${oc ? 'bg-blue-50 text-blue-600 border-blue-100' : 'bg-gray-50 text-gray-400 border-gray-100'}`}>
                                                        {oc ? oc.estado : 'Sin OC'}
                                                    </span>
                                                </td>
                                            </tr>
                                        );
                                    })}
                                    {hc.items.length === 0 && (
                                        <tr><td colSpan="5" className="py-16 text-center text-[10px] font-black text-gray-300 uppercase tracking-widest italic">Sin ítems en la Hoja de Compra</td></tr>
                                    )}
                                </tbody>
                            </table>
                        )}
                    </div>

                    <div className="bg-white rounded-[2.5rem] shadow-sm border border-gray-100 overflow-hidden">
                        <div className="p-8 border-b border-gray-100 bg-gray-50/50 flex justify-between items-center">
                            <div>
                                <h3 className="text-sm font-black text-gray-800 uppercase tracking-widest italic">Órdenes de Compra Vinculadas a la OP</h3>
                                <p className="text-[10px] text-gray-400 font-bold uppercase mt-1">Generadas al consolidar ítems de la Hoja de Compra</p>
                            </div>
                        </div>
                        <table className="w-full text-left">
                            <thead className="bg-white border-b border-gray-100">
                                <tr>
                                    <th className="px-10 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest">N° OC</th>
                                    <th className="px-10 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest">Proveedor</th>
                                    <th className="px-10 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">Fecha Emisión</th>
                                    <th className="px-10 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-right">Total Neto</th>
                                    <th className="px-10 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">Estado</th>
                                    <th className="px-10 py-5 text-[10px] font-black text-gray-400 uppercase tracking-widest text-right">Acciones</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-gray-50">
                                {ordenesVinculadas.map((oc) => (
                                    <tr key={oc.idOC} className="hover:bg-gray-50 transition-colors">
                                        <td className="px-10 py-5 font-black text-blue-600 text-sm italic">{oc.numeroOC}</td>
                                        <td className="px-10 py-5 text-sm font-black text-gray-800 uppercase">{nombreProveedor(oc.proveedorId)}</td>
                                        <td className="px-10 py-5 text-center text-xs text-gray-400 font-black">{oc.fechaEmision}</td>
                                        <td className="px-10 py-5 text-right font-black text-gray-900 text-sm">{formatCLP(oc.totalNeto)}</td>
                                        <td className="px-10 py-5 text-center">
                                            <span className="px-3 py-1 rounded-lg text-[8px] font-black uppercase tracking-widest border bg-amber-50 text-amber-600 border-amber-100">
                                                {oc.estado}
                                            </span>
                                        </td>
                                        <td className="px-10 py-5 text-right">
                                            <button
                                                onClick={() => pdfService.generateOC({ ...oc, proveedorNombre: nombreProveedor(oc.proveedorId), items: oc.items.map(i => ({ ...i, descripcion: i.nombreInsumo, cantidad: i.cantidadComprada })) })}
                                                className="px-3 py-1.5 bg-gray-100 text-gray-600 rounded-lg text-[9px] font-black uppercase tracking-widest hover:bg-gray-200 transition-all shadow-sm flex items-center gap-1 ml-auto"
                                            >
                                                <FileText className="w-3 h-3" /> PDF
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                                {ordenesVinculadas.length === 0 && (
                                    <tr>
                                        <td colSpan="6" className="py-20 text-center text-[10px] font-black text-gray-300 uppercase tracking-widest italic">Sin órdenes de compra registradas para esta OP</td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                </div>
            )}

            {showNewOSModal && selectedOP && (
                <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/60 backdrop-blur-md animate-in fade-in duration-300">
                    <div className="bg-white w-full max-w-2xl rounded-[3rem] p-10 shadow-2xl animate-in zoom-in-95 duration-300 relative max-h-[90vh] overflow-y-auto">
                        <button onClick={() => setShowNewOSModal(false)} className="absolute top-8 right-8 p-3 bg-gray-50 rounded-2xl hover:bg-red-50 text-red-500 transition-all">
                            <X className="w-5 h-5 text-gray-400" />
                        </button>

                        <div className="mb-8">
                            <h2 className="text-2xl font-black text-gray-800 tracking-tight italic uppercase">Nueva Orden de Servicio</h2>
                            <span className="text-[10px] font-black text-amber-600 bg-amber-50 px-3 py-1.5 rounded-full border border-amber-100 uppercase tracking-widest">OP: {selectedOP.numeroOP}</span>
                        </div>

                        <div className="grid grid-cols-2 gap-6 mb-8">
                            <div className="space-y-2 col-span-2">
                                <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest">Proveedor / Taller</label>
                                <select
                                    value={osForm.proveedorId}
                                    onChange={(e) => setOsForm(f => ({ ...f, proveedorId: e.target.value }))}
                                    className="w-full p-4 bg-gray-50 border border-gray-200 rounded-2xl font-black text-xs uppercase outline-none"
                                >
                                    <option value="">Seleccione un proveedor...</option>
                                    {proveedores.map(p => (
                                        <option key={p.proveedorId} value={p.proveedorId}>{p.nombreProveedor}</option>
                                    ))}
                                </select>
                            </div>
                            <div className="space-y-2">
                                <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest">Tipo de Servicio</label>
                                <select
                                    value={osForm.tipoServicio}
                                    onChange={(e) => setOsForm(f => ({ ...f, tipoServicio: e.target.value }))}
                                    className="w-full p-4 bg-gray-50 border border-gray-200 rounded-2xl font-black text-xs uppercase outline-none"
                                >
                                    {TIPOS_SERVICIO.map(t => <option key={t} value={t}>{t}</option>)}
                                </select>
                            </div>
                            <div className="space-y-2">
                                <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest">Fecha Entrega Estimada</label>
                                <input
                                    type="date"
                                    value={osForm.fechaEntregaEstimada}
                                    onChange={(e) => setOsForm(f => ({ ...f, fechaEntregaEstimada: e.target.value }))}
                                    className="w-full p-4 bg-gray-50 border border-gray-200 rounded-2xl font-black text-xs outline-none"
                                />
                            </div>
                            <div className="space-y-2">
                                <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest">Cantidad Pactada</label>
                                <input
                                    type="number" min={1}
                                    value={osForm.cantidadPactada}
                                    onChange={(e) => setOsForm(f => ({ ...f, cantidadPactada: String(clampNonNegative(e.target.value)) }))}
                                    className="w-full p-4 bg-gray-50 border border-gray-200 rounded-2xl font-black text-xs outline-none"
                                />
                            </div>
                            <div className="space-y-2">
                                <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest">Precio Unitario</label>
                                <input
                                    type="number" min={0} step="0.01"
                                    value={osForm.precioUnitario}
                                    onChange={(e) => setOsForm(f => ({ ...f, precioUnitario: String(clampNonNegative(e.target.value)) }))}
                                    className="w-full p-4 bg-gray-50 border border-gray-200 rounded-2xl font-black text-xs outline-none"
                                />
                            </div>
                            <div className="space-y-2 col-span-2">
                                <label className="text-[10px] font-black text-gray-400 uppercase tracking-widest">Descripción del Trabajo</label>
                                <textarea
                                    rows={3}
                                    value={osForm.descripcionTrabajo}
                                    onChange={(e) => setOsForm(f => ({ ...f, descripcionTrabajo: e.target.value }))}
                                    className="w-full p-4 bg-gray-50 border border-gray-200 rounded-2xl font-medium text-xs outline-none resize-none"
                                />
                            </div>
                        </div>

                        <button
                            onClick={handleCrearOS}
                            disabled={submittingOS}
                            className="w-full px-12 py-5 bg-indigo-600 hover:bg-indigo-700 disabled:opacity-50 text-white font-black rounded-2xl shadow-xl transition-all uppercase tracking-widest text-[10px] flex items-center justify-center active:scale-95"
                        >
                            <CheckCircle2 className="w-5 h-5 mr-3" />
                            {submittingOS ? 'Creando...' : 'Crear Orden de Servicio'}
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
}
