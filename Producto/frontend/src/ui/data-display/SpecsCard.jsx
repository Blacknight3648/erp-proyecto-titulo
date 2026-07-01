import React from 'react';
import { Shirt, Ruler, Palette } from 'lucide-react';

export default function SpecsCard({ specs }) {
    if (!specs) return null;

    return (
        <div className="flex flex-wrap gap-2 mt-3">
            <div className="flex items-center space-x-1.5 bg-muted px-2 py-1 rounded-lg border border-border">
                <Shirt className="w-3 h-3 text-muted-foreground" />
                <span className="text-[9px] font-bold text-foreground">{specs.tela}</span>
            </div>
            <div className="flex items-center space-x-1.5 bg-muted px-2 py-1 rounded-lg border border-border">
                <Palette className="w-3 h-3 text-muted-foreground" />
                <span className="text-[9px] font-bold text-foreground">{specs.color}</span>
            </div>
            <div className="flex items-center space-x-1.5 bg-muted px-2 py-1 rounded-lg border border-border">
                <Ruler className="w-3 h-3 text-muted-foreground" />
                <span className="text-[9px] font-bold text-foreground">{specs.gramos}g</span>
            </div>
        </div>
    );
}
