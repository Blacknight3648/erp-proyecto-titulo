import { useState, useEffect, useMemo } from 'react';
import { X, TrendingUp, Download, Activity } from 'lucide-react';
import { api } from '../../../remote/service/api';
import { ProveedorService } from '../../../remote/service/ProveedorService';
import { ReportesService } from '../../../remote/service/ReportesService';

export default function PriceComparisonModal({ show, onClose }) {
    const [articulos, setArticulos] = useState([]);
    const [proveedores, setProveedores] = useState([]);
    const [articuloId, setArticuloId] = useState('');
    const [proveedorId, setProveedorId] = useState('');
    const [rows, setRows] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (!show) return;
        api.get('/maestros/articulos')
            .then(({ data }) => setArticulos(Array.isArray(data) ? data : []))
            .catch((err) => console.error('Error cargando artículos:', err));
        ProveedorService.getAll()
            .then(setProveedores)
            .catch((err) => console.error('Error cargando proveedores:', err));
    }, [show]);

    useEffect(() => {
        if (!show) return;
        setLoading(true);
        ReportesService.historialPrecios({ articuloId: articuloId || undefined, proveedorId: proveedorId || undefined })
            .then(setRows)
            .catch(() => setRows([]))
            .finally(() => setLoading(false));
    }, [show, articuloId, proveedorId]);

    // El backend entrega las filas ordenadas desc. por fecha; la variación de cada
    // fila se calcula contra el precio inmediatamente anterior del mismo artículo.
    const rowsConVariacion = useMemo(() => {
        return rows.map((row, idx) => {
            const anterior = rows.slice(idx + 1).find(r => r.articuloId === row.articuloId);
            const variacion = anterior && Number(anterior.precioUnitario) > 0
                ? Math.round(((row.precioUnitario - anterior.precioUnitario) / anterior.precioUnitario) * 10000) / 100
                : 0;
            return { ...row, variacion, esBase: !anterior };
        });
    }, [rows]);

    const descargarCSV = () => {
        const encabezado = ['N° OC', 'Fecha Emisión', 'Producto', 'Proveedor', 'Precio Unitario', 'Variación (%)'];
        const filas = rowsConVariacion.map(r => [r.numeroOC, r.fechaEmision, r.nombreInsumo, r.proveedorNombre, r.precioUnitario, r.esBase ? '' : r.variacion]);
        const csv = [encabezado, ...filas].map(fila => fila.map(v => `"${v ?? ''}"`).join(';')).join('\n');
        const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
        const url = URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = 'historial-precios.csv';
        link.click();
        URL.revokeObjectURL(url);
    };

    if (!show) return null;

    return (
        <div className="fixed inset-0 z-[110] flex items-center justify-center p-6 bg-foreground/60 backdrop-blur-md animate-in fade-in duration-300">
            <div className="bg-card w-full max-w-5xl rounded-[3.5rem] shadow-2xl relative overflow-hidden animate-in zoom-in-95 duration-300 flex flex-col max-h-[90vh]">
                {/* Header del Modal */}
                <div className="bg-brand-indigo p-8 text-white">
                    <button
                        onClick={onClose}
                        className="absolute top-8 right-8 p-2 bg-white/10 rounded-full hover:bg-white/20 transition-all active:scale-95"
                    >
                        <X className="w-5 h-5" />
                    </button>
                    <div className="flex items-center space-x-4">
                        <div className="p-3 bg-white/10 rounded-2xl">
                            <TrendingUp className="w-6 h-6" />
                        </div>
                        <div>
                            <h3 className="text-2xl font-black tracking-tight uppercase italic underline decoration-white/40 decoration-4 underline-offset-4">Análisis Comparativo de Precios</h3>
                            <p className="text-[10px] font-black text-white/70 uppercase tracking-[0.3em] mt-2 ml-1">Evaluación de costos históricos por producto y proveedor</p>
                        </div>
                    </div>
                </div>

                {/* Filtros de Comparación */}
                <div className="p-8 bg-muted border-b border-border grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div className="space-y-2">
                        <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest ml-1">Producto / Insumo</label>
                        <select
                            value={articuloId}
                            onChange={(e) => setArticuloId(e.target.value)}
                            className="w-full p-4 bg-card border border-border rounded-2xl font-bold text-xs uppercase tracking-widest outline-none focus:ring-2 focus:ring-brand-indigo/50 transition-all appearance-none cursor-pointer"
                        >
                            <option value="">Todos los productos</option>
                            {articulos.map(a => (
                                <option key={a.idArticulo} value={a.idArticulo}>{a.nombreArticulo}</option>
                            ))}
                        </select>
                    </div>
                    <div className="space-y-2">
                        <label className="text-[10px] font-black text-muted-foreground uppercase tracking-widest ml-1">Proveedor Base</label>
                        <select
                            value={proveedorId}
                            onChange={(e) => setProveedorId(e.target.value)}
                            className="w-full p-4 bg-card border border-border rounded-2xl font-bold text-xs uppercase tracking-widest outline-none focus:ring-2 focus:ring-brand-indigo/50 transition-all appearance-none cursor-pointer"
                        >
                            <option value="">Todos los proveedores</option>
                            {proveedores.map(p => (
                                <option key={p.proveedorId} value={p.proveedorId}>{p.nombreProveedor}</option>
                            ))}
                        </select>
                    </div>
                </div>

                {/* Tabla de Resultados */}
                <div className="flex-1 overflow-y-auto p-8 custom-scrollbar bg-card">
                    {loading ? (
                        <p className="text-center text-[10px] font-black text-muted-foreground uppercase tracking-widest italic py-10">Cargando historial de precios...</p>
                    ) : rowsConVariacion.length === 0 ? (
                        <p className="text-center text-[10px] font-black text-muted-foreground uppercase tracking-widest italic py-10">Sin órdenes de compra registradas para este filtro</p>
                    ) : (
                        <table className="w-full text-left border-separate border-spacing-y-3">
                            <thead>
                                <tr className="text-[10px] font-black text-muted-foreground uppercase tracking-[0.2em]">
                                    <th className="px-6 py-2">Fecha / N° OC</th>
                                    <th className="px-6 py-2">Producto Analizado</th>
                                    <th className="px-6 py-2">Proveedor</th>
                                    <th className="px-6 py-2 text-right">Precio / Costo</th>
                                    <th className="px-6 py-2 text-center">Variación</th>
                                </tr>
                            </thead>
                            <tbody>
                                {rowsConVariacion.map((row, i) => (
                                    <tr key={`${row.numeroOC}-${i}`} className="group hover:bg-brand-indigo/5 transition-all">
                                        <td className="px-6 py-5 bg-muted/50 rounded-l-2xl border-y border-l border-border group-hover:bg-card group-hover:border-brand-indigo/20">
                                            <div className="text-xs font-black text-foreground">{row.numeroOC}</div>
                                            <div className="text-[10px] font-bold text-muted-foreground uppercase tracking-widest mt-0.5">{row.fechaEmision}</div>
                                        </td>
                                        <td className="px-6 py-5 bg-muted/50 border-y border-border group-hover:bg-card group-hover:border-brand-indigo/20">
                                            <span className="text-[11px] font-black text-foreground uppercase tracking-tight">{row.nombreInsumo}</span>
                                        </td>
                                        <td className="px-6 py-5 bg-muted/50 border-y border-border group-hover:bg-card group-hover:border-brand-indigo/20">
                                            <span className="text-[10px] font-black text-brand-indigo bg-brand-indigo/10 px-3 py-1 rounded-full uppercase tracking-widest border border-brand-indigo/20">{row.proveedorNombre}</span>
                                        </td>
                                        <td className="px-6 py-5 bg-muted/50 border-y border-border group-hover:bg-card group-hover:border-brand-indigo/20 text-right">
                                            <span className="text-lg font-black text-foreground tracking-tighter italic">
                                                <span className="text-xs text-muted-foreground not-italic mr-1">$</span>
                                                {Number(row.precioUnitario || 0).toLocaleString('es-CL')}
                                            </span>
                                        </td>
                                        <td className="px-6 py-5 bg-muted/50 rounded-r-2xl border-y border-r border-border group-hover:bg-card group-hover:border-brand-indigo/20 text-center">
                                            <div className={`flex items-center justify-center gap-1 font-black text-[10px] uppercase tracking-widest ${row.variacion > 0 ? 'text-destructive' : row.variacion < 0 ? 'text-success' : 'text-muted-foreground'
                                                }`}>
                                                {row.variacion > 0 ? <TrendingUp className="w-3 h-3 rotate-45" /> : row.variacion < 0 ? <TrendingUp className="w-3 h-3 -rotate-45" /> : <Activity className="w-3 h-3" />}
                                                {row.esBase ? 'Base' : `${row.variacion > 0 ? '+' : ''}${row.variacion}%`}
                                            </div>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    )}
                </div>

                {/* Footer del Modal */}
                <div className="p-8 bg-muted border-t border-border flex items-center justify-between">
                    <div className="flex items-center space-x-2">
                        <span className="w-3 h-3 bg-brand-indigo rounded-full animate-pulse" />
                        <span className="text-[10px] font-black text-muted-foreground uppercase tracking-widest italic">
                            {rowsConVariacion.length} registro{rowsConVariacion.length === 1 ? '' : 's'} histórico{rowsConVariacion.length === 1 ? '' : 's'}
                        </span>
                    </div>
                    <div className="flex space-x-3">
                        <button
                            onClick={onClose}
                            className="px-6 py-3 text-[10px] font-black text-muted-foreground uppercase tracking-widest hover:text-brand-indigo transition-colors"
                        >
                            Cerrar Ventana
                        </button>
                        <button
                            onClick={descargarCSV}
                            disabled={rowsConVariacion.length === 0}
                            className="px-8 py-3 bg-foreground text-background rounded-xl text-[10px] font-black uppercase tracking-widest shadow-xl shadow-foreground/10 hover:bg-foreground/90 transition-all disabled:opacity-50"
                        >
                            <Download className="w-4 h-4 mr-2 inline" /> Descargar Informe
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}
