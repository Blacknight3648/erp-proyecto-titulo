import { useNavigate } from 'react-router-dom';
import { X, ExternalLink } from 'lucide-react';

const ESTADOS_ACTIVOS = new Set(['PENDIENTE', 'EN_PROCESO', 'DETENIDA']);

const diasEntre = (desde, hasta) => Math.round((hasta - desde) / (1000 * 60 * 60 * 24));

// Busca, dentro del arreglo `alertas` de una OP (puede traer varias a la vez),
// la que corresponde al código que usa el backend para esta categoría.
const buscarAlerta = (op, codigo) => (op.alertas || []).find(a => a.etapaAtrasada === codigo);

// Mismas reglas que evalúa el backend (DashboardOPService.evaluarAlertas), aplicadas
// sobre el arreglo `alertas` que ya trae cada OP (o sobre fechaEntregaProgramada).
export const CATEGORIAS_ALERTA = {
    opAtrasada: {
        titulo: 'OP Atrasada',
        descripcion: 'Sin confirmación de OC de Materia Prima hace más de 3 días',
        filtro: (op) => !!buscarAlerta(op, 'OP'),
        dias: (op) => buscarAlerta(op, 'OP')?.diasAtraso,
    },
    corteAtrasado: {
        titulo: 'Corte Atrasado',
        descripcion: 'Corte iniciado hace más de 10 días sin finalizar',
        filtro: (op) => !!buscarAlerta(op, 'CORTE'),
        dias: (op) => buscarAlerta(op, 'CORTE')?.diasAtraso,
    },
    recepcionLogoAtrasado: {
        titulo: 'Recepción de Logo Atrasada',
        descripcion: 'Logo enviado hace más de 3 días sin retorno',
        filtro: (op) => !!buscarAlerta(op, 'LOGO'),
        dias: (op) => buscarAlerta(op, 'LOGO')?.diasAtraso,
    },
    envioAtrasado: {
        titulo: 'Envío Atrasado',
        descripcion: 'Logo devuelto hace más de 2 días sin iniciar taller',
        filtro: (op) => !!buscarAlerta(op, 'ENVIO'),
        dias: (op) => buscarAlerta(op, 'ENVIO')?.diasAtraso,
    },
    devolucionTallerAtrasada: {
        titulo: 'Devolución Taller Atrasada',
        descripcion: 'En taller externo hace más de 7 días sin devolución',
        filtro: (op) => !!buscarAlerta(op, 'TALLER'),
        dias: (op) => buscarAlerta(op, 'TALLER')?.diasAtraso,
    },
    entregas7d: {
        titulo: 'Entregas (Próximos 7 días)',
        descripcion: 'Entrega programada dentro de los próximos 7 días',
        columnaDias: 'Días para Entrega',
        filtro: (op) => {
            if (!op.fechaEntregaProgramada || !ESTADOS_ACTIVOS.has(op.estado)) return false;
            const dias = diasEntre(new Date(new Date().toDateString()), new Date(op.fechaEntregaProgramada));
            return dias >= 0 && dias <= 7;
        },
        dias: (op) => diasEntre(new Date(new Date().toDateString()), new Date(op.fechaEntregaProgramada)),
    },
};

export default function AlertaOPModal({ categoria, ops, onClose }) {
    if (!categoria) return null;
    const config = CATEGORIAS_ALERTA[categoria];
    const navigate = useNavigate();
    const opsFiltradas = ops.filter(config.filtro);

    const irADetalle = (op) => {
        onClose();
        navigate('/produccion/ordenes', { state: { opId: op.idOP } });
    };

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/60 backdrop-blur-sm animate-in fade-in duration-200">
            <div className="bg-white w-full max-w-2xl rounded-[2.5rem] shadow-2xl overflow-hidden max-h-[85vh] flex flex-col animate-in zoom-in-95 duration-200">
                <div className="p-8 border-b border-gray-100 flex items-start justify-between">
                    <div>
                        <h3 className="text-xl font-black text-gray-800 uppercase tracking-tight">{config.titulo}</h3>
                        <p className="text-xs text-gray-400 font-medium mt-1">{config.descripcion}</p>
                    </div>
                    <button onClick={onClose} className="p-2 bg-gray-50 rounded-xl hover:bg-red-50 hover:text-red-500 transition-all text-gray-400">
                        <X className="w-5 h-5" />
                    </button>
                </div>

                <div className="flex-1 overflow-y-auto">
                    {opsFiltradas.length === 0 ? (
                        <div className="py-16 text-center text-gray-300 font-bold uppercase tracking-widest text-xs">
                            Sin OPs en esta categoría
                        </div>
                    ) : (
                        <table className="w-full text-left">
                            <thead className="bg-gray-50 border-b border-gray-100 sticky top-0">
                                <tr>
                                    <th className="px-8 py-4 text-[10px] font-black text-gray-400 uppercase tracking-widest">N° OP</th>
                                    <th className="px-8 py-4 text-[10px] font-black text-gray-400 uppercase tracking-widest">NV Origen</th>
                                    <th className="px-8 py-4 text-[10px] font-black text-gray-400 uppercase tracking-widest text-center">{config.columnaDias || 'Días Atraso'}</th>
                                    <th className="px-8 py-4 text-[10px] font-black text-gray-400 uppercase tracking-widest text-right">Acción</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-gray-50">
                                {opsFiltradas.map((op) => {
                                    const dias = config.dias(op);
                                    return (
                                        <tr key={op.idOP} className="hover:bg-gray-50 transition-colors">
                                            <td className="px-8 py-5 font-black text-gray-800 text-sm">{op.numeroOP}</td>
                                            <td className="px-8 py-5 text-xs font-bold text-blue-600">NV {op.notaVentaId}</td>
                                            <td className="px-8 py-5 text-center text-xs font-black text-red-500">
                                                {dias !== undefined && dias !== null ? `${dias}d` : '—'}
                                            </td>
                                            <td className="px-8 py-5 text-right">
                                                <button
                                                    onClick={() => irADetalle(op)}
                                                    className="inline-flex items-center gap-1.5 px-4 py-2 bg-gray-900 text-white rounded-xl text-[10px] font-black uppercase tracking-widest hover:bg-black transition-all"
                                                >
                                                    Ver OP <ExternalLink className="w-3 h-3" />
                                                </button>
                                            </td>
                                        </tr>
                                    );
                                })}
                            </tbody>
                        </table>
                    )}
                </div>
            </div>
        </div>
    );
}
