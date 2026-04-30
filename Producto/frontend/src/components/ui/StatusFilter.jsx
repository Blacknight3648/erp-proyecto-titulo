import React from "react";
import { Filter } from "lucide-react";

export default function StatusFilter({ value, onChange }) {
  return (
    <div className="flex items-center gap-4 w-full xl:w-auto overflow-x-auto pb-2 xl:pb-0 custom-scrollbar">
      <div className="p-3 bg-white rounded-2xl shadow-sm text-gray-400">
        <Filter size={18} />
      </div>
      <div className="flex bg-gray-100/50 p-1.5 rounded-2xl border border-gray-100">
        {["Todos", "Activo", "Suspendido"].map((status) => (
          <button
            key={status}
            onClick={() => onChange(status)}
            className={`px-8 py-3 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all duration-300 ${
              value === status
                ? "bg-white text-blue-600 shadow-xl shadow-gray-200"
                : "text-gray-400 hover:text-gray-600"
            }`}
          >
            {status}
          </button>
        ))}
      </div>
    </div>
  );
}
