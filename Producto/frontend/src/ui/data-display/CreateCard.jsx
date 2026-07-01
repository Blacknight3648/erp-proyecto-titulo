import React from "react";
import { Plus } from "lucide-react";

export default function CreateCard({ onClick, label }) {
  return (
    <div
      onClick={onClick}
      className="group border-4 border-dashed border-border rounded-[3rem] p-10 flex flex-col items-center justify-center text-muted-foreground hover:border-primary/30 hover:text-primary hover:bg-primary/5 transition-all cursor-pointer min-h-[350px] relative overflow-hidden"
    >
      <div className="absolute inset-0 bg-gradient-to-br from-transparent to-primary/5 opacity-0 group-hover:opacity-100 transition-opacity"></div>
      <div className="w-20 h-20 rounded-[2rem] border-4 border-dashed border-current flex items-center justify-center mb-6 group-hover:scale-110 transition-transform bg-card/50">
        <Plus className="w-10 h-10" />
      </div>
      <span className="text-[10px] font-black uppercase tracking-[0.3em] relative z-10">
        {label}
      </span>
    </div>
  );
}
