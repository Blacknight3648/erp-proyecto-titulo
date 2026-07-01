import React, { useState } from 'react';
import {
    ChevronDown,
    ChevronUp,
    CheckCircle2,
    Clock,
    AlertCircle,
    Info
} from 'lucide-react';
import { useEstadoSC } from '../../hooks/useEstadoSC';
import { useEstadoOP } from '../../hooks/useEstadoOP';
import { pdfService } from '../../remote/service/pdfService';
import { FileText } from 'lucide-react';

const ProgressCard = ({ item, details, stages, type = 'op' }) => {
    const [isExpanded, setIsExpanded] = useState(false);
    const [selectedStage, setSelectedStage] = useState(null);

    const scStatus = useEstadoSC(type === 'sc' ? item : null);
    const opStatus = useEstadoOP(type === 'op' ? item : null);

    const { estadoGlobal, etapaCritica, diasAtraso, motivo } = type === 'sc' ? scStatus : opStatus;
    const isDelayed = estadoGlobal === 'ATRASADA';

    const currentStageIndex = stages.findIndex(s => s.id === item.estado);

    // Logic to determine stage status
    const getStageStatus = (index) => {
        if (index < currentStageIndex) return 'completed';
        if (index === currentStageIndex) return 'current';
        return 'pending';
    };

    const progressValue = item.progreso !== undefined
        ? item.progreso
        : Math.round(((currentStageIndex + 1) / stages.length) * 100);

    return (
        <div className="bg-card rounded-3xl border border-border shadow-sm hover:shadow-md transition-all overflow-hidden mb-4">
            {/* Main Row */}
            <div className="p-5 flex flex-col md:flex-row md:items-center justify-between gap-6">
                {/* Info */}
                <div className="flex-1 min-w-[200px]">
                    <div className="flex items-center space-x-3 mb-1">
                        <span className="text-[10px] font-black text-primary bg-accent px-2 py-0.5 rounded-full uppercase tracking-tighter">
                            {item.numeroSC || item.idSC || item.numeroOP || item.idOP || item.id}
                        </span>
                        {/* Status Badge */}
                        <div className={`flex items-center space-x-1 px-2 py-0.5 rounded-full ${isDelayed ? 'bg-destructive/10 text-destructive' : 'bg-success-bg text-success'
                            }`}>
                            <div className={`w-1.5 h-1.5 rounded-full animate-pulse ${isDelayed ? 'bg-destructive' : 'bg-success'
                                }`} />
                            <span className="text-[8px] font-black uppercase tracking-tighter">
                                {isDelayed ? 'Atrasada' : 'Flujo Normal'}
                            </span>
                        </div>
                    </div>
                    {isDelayed && etapaCritica && (
                        <div className="flex flex-col mt-0.5 ml-1">
                            <div className="flex items-center text-[9px] font-black text-destructive uppercase tracking-widest">
                                <AlertCircle className="w-2.5 h-2.5 mr-1" />
                                Etapa crítica: {etapaCritica} ({diasAtraso} días atraso)
                            </div>
                            {motivo && (
                                <div className="text-[8px] font-bold text-destructive uppercase tracking-tighter ml-3.5">
                                    {motivo}
                                </div>
                            )}
                        </div>
                    )}
                    <div className="flex items-center space-x-2">
                        <h4 className="font-black text-foreground text-sm leading-tight">
                            {type === 'op' ? item.producto : item.cliente}
                        </h4>
                    </div>
                    <p className="text-xs text-muted-foreground font-bold uppercase tracking-widest mt-0.5">
                        {type === 'op' ? item.cliente : `NV: ${item.notaVentaId || item.nv_id}`}
                    </p>
                </div>

                {/* Interactive Progress Bar */}
                <div className="flex-[2] w-full max-w-2xl px-2">
                    <div className="relative flex items-center justify-between h-8 bg-secondary rounded-full p-1 border border-border-strong/50 shadow-inner">
                        {stages.map((stage, idx) => {
                            const status = getStageStatus(idx);
                            const isSelected = selectedStage === stage.id;

                            return (
                                <button
                                    key={stage.id}
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        setSelectedStage(isSelected ? null : stage.id);
                                        if (!isExpanded) setIsExpanded(true);
                                    }}
                                    className={`
                                        relative z-10 flex-1 h-full rounded-full flex items-center justify-center transition-all duration-300
                                        ${status === 'completed' ? 'bg-success text-white shadow-sm' : ''}
                                        ${status === 'current' ? (isDelayed ? 'bg-destructive text-white shadow-xl scale-105' : 'bg-primary text-white shadow-xl scale-105') : ''}
                                        ${status === 'pending' ? 'text-muted-foreground hover:text-foreground' : ''}
                                        ${isSelected ? 'ring-2 ring-offset-2 ring-primary/40' : ''}
                                    `}
                                >
                                    <span className="text-[9px] font-black pointer-events-none uppercase tracking-tighter hidden sm:block">
                                        {stage.label}
                                    </span>
                                    {status === 'completed' && <CheckCircle2 className="w-3 h-3" />}
                                </button>
                            );
                        })}
                        {/* Background line extension logic can be added here if needed */}
                    </div>
                </div>

                {/* Actions */}
                <div className="flex items-center space-x-3">
                    <div className="text-right mr-4">
                        <div className="text-lg font-black text-foreground">{progressValue}%</div>
                        <p className="text-[9px] text-muted-foreground font-black uppercase">Avance Total</p>
                    </div>
                    <button
                        onClick={() => setIsExpanded(!isExpanded)}
                        className={`p-2 rounded-xl transition-colors ${isExpanded ? 'bg-accent text-primary' : 'bg-muted text-muted-foreground hover:bg-secondary'}`}
                    >
                        {isExpanded ? <ChevronUp className="w-5 h-5" /> : <ChevronDown className="w-5 h-5" />}
                    </button>
                </div>
            </div>

            {/* Expanded Content */}
            {isExpanded && (
                <div className="px-5 pb-5 pt-2 bg-muted/50 border-t border-border animate-in slide-in-from-top-4 duration-300">
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                        {/* Stage Details */}
                        <div className="col-span-2">
                            <h5 className="text-[10px] font-black text-muted-foreground uppercase tracking-[0.2em] mb-4">Detalle por Etapa</h5>
                            <div className="grid grid-cols-2 sm:grid-cols-3 gap-3">
                                {stages.map((stage, idx) => {
                                     const status = getStageStatus(idx);
                                     const itemId = item.idSC || item.numeroSC || item.idOP || item.numeroOP || item.id;
                                     const date = details?.[itemId]?.[stage.key];
                                    const isThisStageDelayed = status === 'current' && isDelayed && stage.id === etapaCritica;

                                    return (
                                        <div
                                            key={stage.id}
                                            onClick={() => setSelectedStage(stage.id)}
                                            className={`p-3 rounded-2xl border cursor-pointer transition-all ${selectedStage === stage.id
                                                ? 'bg-card border-primary/20 shadow-md ring-1 ring-primary/10'
                                                : isThisStageDelayed ? 'bg-destructive/10 border-destructive/15 opacity-100' : 'bg-card border-border opacity-60 hover:opacity-100'
                                                }`}
                                        >
                                            <div className="flex items-center justify-between mb-2">
                                                <span className={`text-[8px] font-black px-1.5 py-0.5 rounded-md uppercase tracking-widest ${status === 'completed' ? 'bg-success-bg text-success' :
                                                    status === 'current' ? (isThisStageDelayed ? 'bg-destructive text-white' : 'bg-accent text-accent-foreground') :
                                                        'bg-secondary text-muted-foreground'
                                                    }`}>
                                                    {stage.id}
                                                </span>
                                                {status === 'completed' && <CheckCircle2 className="w-3 h-3 text-success" />}
                                                {status === 'current' && (isThisStageDelayed ?
                                                    <AlertCircle className="w-3 h-3 text-destructive animate-pulse" /> :
                                                    <Clock className="w-3 h-3 text-primary animate-spin-slow" />
                                                )}
                                            </div>
                                            <p className="text-[10px] text-muted-foreground font-bold italic">
                                                {date || (status === 'pending' ? 'Pendiente' : 'Iniciado...')}
                                            </p>
                                            {isThisStageDelayed && (
                                                <div className="mt-1 text-[8px] font-black text-destructive uppercase">
                                                    {diasAtraso} días atraso
                                                </div>
                                            )}
                                        </div>
                                    );
                                })}
                            </div>
                        </div>

                        {/* Extra Info */}
                        <div className="bg-card p-4 rounded-2xl border border-border shadow-sm self-start">
                            <h5 className="text-[10px] font-black text-muted-foreground uppercase tracking-widest mb-3">
                                {type === 'op' ? 'Observaciones Planta' : 'Notas de Adquisición'}
                            </h5>
                             <p className="text-xs text-muted-foreground leading-relaxed font-medium mb-4">
                                 {details?.[item.idSC || item.numeroSC || item.idOP || item.numeroOP || item.id]?.obs || 'Sin observaciones registradas para este documento hasta la fecha.'}
                             </p>
                            <div className="mt-4 pt-4 border-t border-border flex items-center justify-between">
                                <div className="flex items-center space-x-2">
                                    <div className="w-6 h-6 bg-primary rounded-full flex items-center justify-center text-[10px] text-white font-bold uppercase">
                                        {type === 'op' ? 'JS' : 'MA'}
                                    </div>
                                    <span className="text-[9px] text-muted-foreground font-black uppercase tracking-widest">
                                        Responsable: {type === 'op' ? 'Juan S.' : 'Mario A.'}
                                    </span>
                                </div>
                                <button
                                    onClick={() => type === 'op' ? pdfService.generateOP(item) : pdfService.generateSC(item)}
                                    className="text-primary hover:opacity-80 flex items-center gap-1.5 p-2 bg-accent rounded-lg text-[9px] font-black uppercase tracking-widest transition-all hover:shadow-sm"
                                >
                                    <FileText className="w-3 h-3" /> Ficha PDF
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default ProgressCard;
