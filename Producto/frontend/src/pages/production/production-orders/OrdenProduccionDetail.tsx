import { useState, useEffect } from 'react';
import {
    ChevronLeft,
    Calendar,
    Tag,
    ClipboardList,
    Layers,
    Scissors,
    Info,
    Loader2,
    Activity,
    Edit3,
    CheckCircle2,
    Circle,
    AlertCircle
} from 'lucide-react';
import { OrdenProduccionService } from '../../../remote/service/OrdenProduccionService';
import RegistrarAvanceModal from './RegistrarAvanceModal';

export default function OrdenProduccionDetail({ opId, onBack }) {
    const [op, setOp] = useState(null);
    const [avance, setAvance] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [showAvanceModal, setShowAvanceModal] = useState(false);

    const loadData = () => {
        if (!opId) return;
        let active = true;
        setLoading(true);
        Promise.all([
            OrdenProduccionService.getById(opId),
            OrdenProduccionService.getAvance(opId).catch(() => null)
        ])
            .then(([opData, avanceData]) => {
                if (!active) return;
                setOp(opData);
                setAvance(avanceData);
            })
            .catch((err) => {
                if (active) setError(err);
            })
            .finally(() => {
                if (active) setLoading(false);
            });
        return () => { active = false; };
    };

    useEffect(() => {
        loadData();
    }, [opId]);

    const handleSuccessAvance = () => {
        OrdenProduccionService.getAvance(opId)
            .then(data => setAvance(data))
            .catch(console.error);
    };

    if (loading) {
        return (
            <div className="flex flex-col items-center justify-center min-h-[400px]">
                <Loader2 className="w-10 h-10 text-primary animate-spin mb-4" />
                <p className="text-muted-foreground font-bold animate-pulse">Cargando detalle de la OP...</p>
            </div>
        );
    }

    if (error || !op) {
        return (
            <div className="space-y-6">
                <button
                    onClick={onBack}
                    className="p-3 bg-card border border-border shadow-sm rounded-2xl hover:bg-muted transition-all text-muted-foreground hover:text-primary active:scale-95"
                >
                    <ChevronLeft className="w-5 h-5" />
                </button>
                <div className="p-10 text-center text-destructive font-black">OP NO ENCONTRADA</div>
            </div>
        );
    }

    const hitos = [
        { key: 'fechaRecepcionOp', label: 'Recepción OP', date: avance?.fechaRecepcionOp },
        { key: 'finTizado', label: 'Fin Tizado', date: avance?.finTizado },
        { key: 'fechaEstadoOcMp', label: 'Estado OC (MP)', date: avance?.fechaEstadoOcMp, auto: true },
        { key: 'recepcionCompras', label: 'Recepción Compras', date: avance?.recepcionCompras },
        { key: 'inicioCorte', label: 'Inicio Corte', date: avance?.inicioCorte },
        { key: 'finCorte', label: 'Fin Corte', date: avance?.finCorte },
        { key: 'inicioLogo', label: 'Inicio Logo', date: avance?.inicioLogo },
        { key: 'estadoIdaLogo', label: 'Estado Ida Logo', value: avance?.estadoIdaLogo, isEnum: true },
        { key: 'regresoLogo', label: 'Regreso Logo', date: avance?.regresoLogo },
        { key: 'estadoRecLogo', label: 'Estado Rec. Logo', value: avance?.estadoRecLogo, isEnum: true },
        { key: 'inicioTallerExterno', label: 'Inicio Taller Externo', date: avance?.inicioTallerExterno, auto: true },
        { key: 'finTallerExterno', label: 'Fin Taller Externo', date: avance?.finTallerExterno },
        { key: 'calidadTaller', label: 'Calidad Taller', value: avance?.calidadTaller, isEnum: true },
        { key: 'finTerminacion', label: 'Fin Terminación', date: avance?.finTerminacion },
        { key: 'finPersonalizado', label: 'Fin Personalizado', date: avance?.finPersonalizado }
    ];

    return (
        <div className="space-y-8 animate-in slide-in-from-right-8 duration-500 pb-20">
            {/* Cabecera de Detalle */}
            <div className="flex items-center gap-6">
                <button
                    onClick={onBack}
                    className="p-3 bg-card border border-border shadow-sm rounded-2xl hover:bg-muted transition-all text-muted-foreground hover:text-primary active:scale-95"
                >
                    <ChevronLeft className="w-5 h-5" />
                </button>
                <div className="flex-1">
                    <div className="flex items-center gap-3">
                        <h2 className="text-2xl font-black text-foreground tracking-tight italic uppercase">Detalle OP: {op.numeroOP || op.idOP}</h2>
                        <span className="px-3 py-1 bg-primary/10 text-primary text-[10px] font-black rounded-full border border-primary/20 uppercase tracking-widest">
                            {op.estado}
                        </span>
                    </div>
                    <p className="text-[10px] text-muted-foreground font-black uppercase tracking-widest mt-1">
                        Vinculada a NV: {op.notaVentaId ?? '-'}
                    </p>
                </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                {/* Columna Izquierda: Información General y Tallaje */}
                <div className="lg:col-span-2 space-y-8">
                    {/* Tarjeta de Información General */}
                    <div className="bg-card rounded-[2.5rem] shadow-sm border border-border overflow-hidden">
                        <div className="p-8 border-b border-border bg-muted/50">
                            <h3 className="text-sm font-black text-foreground uppercase tracking-widest flex items-center gap-2">
                                <Info className="w-4 h-4 text-primary" />
                                Información General de la OP
                            </h3>
                        </div>
                        <div className="p-8 grid grid-cols-1 md:grid-cols-2 gap-8">
                            <div className="space-y-6">
                                <InfoItem icon={Tag} label="Número OP" value={op.numeroOP || op.idOP} />
                                <InfoItem icon={Calendar} label="Fecha Inicio" value={op.fechaInicio || "Sin fecha"} />
                                <InfoItem icon={Calendar} label="Fecha Entrega Programada" value={op.fechaEntregaProgramada || "Sin fecha"} />
                            </div>
                            <div className="space-y-6">
                                <InfoItem icon={Layers} label="Nota de Venta" value={op.notaVentaId ?? '-'} />
                                <InfoItem icon={Layers} label="Versión Costeo" value={op.costeoVersionId ?? '-'} />
                                <InfoItem icon={ClipboardList} label="Cantidad de Ítems" value={op.items?.length ?? 0} />
                            </div>
                        </div>
                    </div>

                    {/* Tallaje Detallado (Items OP) */}
                    <div className="bg-card rounded-[2.5rem] shadow-sm border border-border overflow-hidden">
                        <div className="p-8 border-b border-border">
                            <h3 className="text-sm font-black text-foreground uppercase tracking-widest flex items-center gap-2">
                                <Scissors className="w-4 h-4 text-brand-indigo" />
                                Detalle de Ítems
                            </h3>
                        </div>
                        <div className="overflow-x-auto">
                            <table className="w-full text-left">
                                <thead className="bg-muted border-b border-border">
                                    <tr>
                                        <th className="px-8 py-4 text-[9px] font-black text-muted-foreground uppercase tracking-widest">Modelo</th>
                                        <th className="px-8 py-4 text-[9px] font-black text-muted-foreground uppercase tracking-widest text-center">Tela</th>
                                        <th className="px-8 py-4 text-[9px] font-black text-muted-foreground uppercase tracking-widest text-center">Talla</th>
                                        <th className="px-8 py-4 text-[9px] font-black text-muted-foreground uppercase tracking-widest text-center">Color</th>
                                        <th className="px-8 py-4 text-[9px] font-black text-muted-foreground uppercase tracking-widest text-center">Logo</th>
                                        <th className="px-8 py-4 text-[9px] font-black text-muted-foreground uppercase tracking-widest text-center">Cantidad</th>
                                    </tr>
                                </thead>
                                <tbody className="divide-y divide-border">
                                    {op.items?.map((item) => (
                                        <tr key={item.idOPItem} className="hover:bg-muted transition-colors">
                                            <td className="px-8 py-4 text-xs font-black text-foreground uppercase">{item.modelo}</td>
                                            <td className="px-8 py-4 text-center text-xs font-bold text-muted-foreground uppercase">{item.tela}</td>
                                            <td className="px-8 py-4 text-center text-xs font-black text-foreground italic">{item.talla}</td>
                                            <td className="px-8 py-4 text-center text-xs font-bold text-muted-foreground uppercase">{item.color}</td>
                                            <td className="px-8 py-4 text-center text-xs font-bold text-muted-foreground uppercase">{item.llevaLogo}</td>
                                            <td className="px-8 py-4 text-center text-xs font-black text-primary">{item.cantidad}</td>
                                        </tr>
                                    ))}
                                    {(!op.items || op.items.length === 0) && (
                                        <tr>
                                            <td colSpan="6" className="py-8 text-center text-muted-foreground font-bold italic uppercase text-[10px]">No hay ítems registrados en la OP</td>
                                        </tr>
                                    )}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                {/* Columna Derecha: Seguimiento OP */}
                <div className="space-y-8">
                    {/* Hitos de Avance */}
                    <div className="bg-card rounded-[2.5rem] shadow-sm border border-border overflow-hidden">
                        <div className="p-8 border-b border-border bg-warning/5 flex justify-between items-center">
                            <h3 className="text-sm font-black text-foreground uppercase tracking-widest flex items-center gap-2">
                                <Activity className="w-4 h-4 text-warning" />
                                Seguimiento OP
                            </h3>
                            <button
                                onClick={() => setShowAvanceModal(true)}
                                className="flex items-center gap-2 bg-primary text-primary-foreground px-4 py-2 rounded-xl text-[10px] font-black uppercase hover:bg-primary-hover transition-colors"
                            >
                                <Edit3 className="w-3 h-3" /> Actualizar
                            </button>
                        </div>
                        <div className="p-6 space-y-5">
                            <div>
                                <div className="flex justify-between items-center mb-2">
                                    <span className="text-[10px] font-black text-muted-foreground uppercase tracking-widest">Progreso de Hitos</span>
                                    <span className="text-sm font-black text-foreground italic">{Number(avance?.porcentajeGlobal) || 0}%</span>
                                </div>
                                <div className="w-full bg-muted h-3 rounded-full p-0.5 shadow-inner border border-border">
                                    <div className="bg-primary h-full rounded-full transition-all duration-1000" style={{ width: `${Number(avance?.porcentajeGlobal) || 0}%` }}></div>
                                </div>
                            </div>

                            <div className="space-y-1 relative before:absolute before:inset-0 before:ml-4 before:-translate-x-px md:before:mx-auto md:before:translate-x-0 before:h-full before:w-0.5 before:bg-gradient-to-b before:from-transparent before:via-border before:to-transparent pt-4">
                                {hitos.map((hito, idx) => {
                                    const isCompleted = hito.date || hito.value;
                                    return (
                                        <div key={idx} className="relative flex items-center justify-between md:justify-normal md:odd:flex-row-reverse group is-active py-2">
                                            <div className="flex items-center justify-center w-8 h-8 rounded-full border-2 border-card bg-muted shadow shrink-0 md:order-1 md:group-odd:-translate-x-1/2 md:group-even:translate-x-1/2 z-10 transition-colors">
                                                {isCompleted ? (
                                                    <CheckCircle2 className="w-4 h-4 text-success bg-success/10 rounded-full" />
                                                ) : (
                                                    <Circle className="w-4 h-4 text-muted-foreground/40" />
                                                )}
                                            </div>
                                            <div className="w-[calc(100%-3rem)] md:w-[calc(50%-2.5rem)] bg-card p-3 rounded-2xl border border-border shadow-sm flex justify-between items-center gap-3">
                                                <span className="text-[10px] font-black text-muted-foreground uppercase tracking-widest">{hito.label}</span>
                                                {isCompleted ? (
                                                    <span className="text-[10px] font-black text-primary bg-primary/10 px-2 py-1 rounded-lg italic">
                                                        {hito.isEnum ? hito.value.replace(/_/g, ' ') : hito.date}
                                                    </span>
                                                ) : (
                                                    <span className="text-[9px] font-bold text-muted-foreground uppercase">
                                                        {hito.auto ? "Auto" : "Pendiente"}
                                                    </span>
                                                )}
                                            </div>
                                        </div>
                                    );
                                })}
                            </div>

                            {avance?.obsTaller && (
                                <div className="mt-4 p-4 bg-destructive/5 border border-destructive/20 rounded-2xl flex items-start gap-3">
                                    <AlertCircle className="w-4 h-4 text-destructive mt-0.5 shrink-0" />
                                    <div>
                                        <span className="text-[10px] font-black text-destructive uppercase tracking-widest block mb-1">Observación Taller</span>
                                        <p className="text-xs font-bold text-destructive/80 italic">{avance.obsTaller}</p>
                                    </div>
                                </div>
                            )}

                        </div>
                    </div>

                    {/* Observaciones Generales de la OP */}
                    <div className="bg-sidebar text-white rounded-[2.5rem] p-8 shadow-xl shadow-foreground/10">
                        <h3 className="text-xs font-black text-sidebar-primary uppercase tracking-widest mb-6 flex items-center gap-2">
                            <ClipboardList className="w-4 h-4" />
                            Observaciones de la OP
                        </h3>
                        <div className="border-l-2 border-sidebar-border pl-4 py-1">
                            <p className="text-xs text-sidebar-foreground leading-relaxed italic">
                                {op.observaciones || "Sin observaciones registradas."}
                            </p>
                        </div>
                    </div>
                </div>
            </div>

            <RegistrarAvanceModal
                isOpen={showAvanceModal}
                onClose={() => setShowAvanceModal(false)}
                avance={avance}
                opId={opId}
                onSuccess={handleSuccessAvance}
            />
        </div>
    );
}

function InfoItem({ icon: Icon, label, value, color = "text-foreground" }) {
    return (
        <div className="flex items-center gap-3 group transition-all">
            <div className="bg-muted p-3 rounded-2xl group-hover:bg-primary/10 transition-colors">
                <Icon className="w-4 h-4 text-muted-foreground group-hover:text-primary" />
            </div>
            <div>
                <p className="text-[9px] font-black text-muted-foreground uppercase tracking-widest">{label}</p>
                <p className={`text-sm font-black tracking-tight uppercase ${color}`}>{value}</p>
            </div>
        </div>
    );
}
