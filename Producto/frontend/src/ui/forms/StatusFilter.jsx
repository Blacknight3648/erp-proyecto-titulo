import React from "react";
import { Filter } from "lucide-react";

export default function StatusFilter({ value, onChange }) {
  return (
    <div className="flex items-center gap-4 w-full xl:w-auto overflow-x-auto pb-2 xl:pb-0 custom-scrollbar">
      <div className="p-3 bg-card rounded-2xl shadow-sm text-muted-foreground">
        <Filter size={18} />
      </div>
      <div className="flex bg-secondary/50 p-1.5 rounded-2xl border border-border">
        {["Todos", "Activo", "Suspendido"].map((status) => (
          <button
            key={status}
            onClick={() => onChange(status)}
            className={`px-8 py-3 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all duration-300 ${
              value === status
                ? "bg-card text-primary shadow-[var(--shadow-xl)]"
                : "text-muted-foreground hover:text-foreground"
            }`}
          >
            {status}
          </button>
        ))}
      </div>
    </div>
  );
}
