import React from 'react';

const statusStyles = {
    'Pendiente Costeo': 'bg-warning/10 text-warning border-warning/20',
    'Costeado': 'bg-primary/10 text-primary border-primary/20',
    'Costo Aprobado': 'bg-brand-indigo/10 text-brand-indigo border-brand-indigo/20',
    'Evaluado': 'bg-success/10 text-success border-success/20',
};

export default function CostoBadge({ status }) {
    return (
        <span className={`px-2 py-0.5 rounded-full text-[9px] font-black uppercase tracking-tighter border ${statusStyles[status] || 'bg-muted text-muted-foreground border-border'}`}>
            {status}
        </span>
    );
}
