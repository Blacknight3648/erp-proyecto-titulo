import React from 'react';

const statusStyles = {
    'Pendiente Costeo': 'bg-orange-50 text-orange-600 border-orange-100',
    'Costeado': 'bg-blue-50 text-blue-600 border-blue-100',
    'Costo Aprobado': 'bg-indigo-50 text-indigo-600 border-indigo-100',
    'Evaluado': 'bg-green-50 text-green-600 border-green-100',
};

export default function CostoBadge({ status }) {
    return (
        <span className={`px-2 py-0.5 rounded-full text-[9px] font-black uppercase tracking-tighter border ${statusStyles[status] || 'bg-gray-50 text-gray-500 border-gray-100'}`}>
            {status}
        </span>
    );
}
