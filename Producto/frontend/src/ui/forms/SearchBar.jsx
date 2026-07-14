import React from "react";
import { Search } from "lucide-react";

export default function SearchBar({ value, onChange, placeholder = "Buscar..." }) {
  return (
    <div className="relative w-full xl:w-[500px] group">
      <div className="absolute left-6 top-1/2 -translate-y-1/2 text-muted-foreground group-focus-within:text-primary transition-colors">
        <Search size={20} />
      </div>
      <input
        type="text"
        placeholder={placeholder}
        value={value}
        onChange={(e) => onChange(e.target.value)}
        className="w-full pl-16 pr-8 py-5 bg-card rounded-[2rem] border-2 border-transparent focus:border-primary/20 outline-none text-sm font-bold shadow-sm transition-all focus:shadow-[var(--shadow-xl)]"
      />
    </div>
  );
}
