import React from 'react';
import { Shirt, Ruler, Palette } from 'lucide-react';

export default function SpecsCard({ specs }) {
    if (!specs) return null;

    return (
        <div className="flex flex-wrap gap-2 mt-3">
            <div className="flex items-center space-x-1.5 bg-gray-50 px-2 py-1 rounded-lg border border-gray-100">
                <Shirt className="w-3 h-3 text-gray-400" />
                <span className="text-[9px] font-bold text-gray-600">{specs.tela}</span>
            </div>
            <div className="flex items-center space-x-1.5 bg-gray-50 px-2 py-1 rounded-lg border border-gray-100">
                <Palette className="w-3 h-3 text-gray-400" />
                <span className="text-[9px] font-bold text-gray-600">{specs.color}</span>
            </div>
            <div className="flex items-center space-x-1.5 bg-gray-50 px-2 py-1 rounded-lg border border-gray-100">
                <Ruler className="w-3 h-3 text-gray-400" />
                <span className="text-[9px] font-bold text-gray-600">{specs.gramos}g</span>
            </div>
        </div>
    );
}
