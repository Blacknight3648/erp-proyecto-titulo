import React from "react";
import { UserPlus } from "lucide-react";
import CreateCard from "../../ui/data-display/CreateCard";
import SearchBar from "../../ui/forms/SearchBar";
import StatusFilter from "../../ui/forms/StatusFilter";

export default function CrudGridLayout({
  title,
  subtitle,
  icon: Icon = UserPlus,
  createLabel,
  onCreateClick,
  searchPlaceholder,
  searchTerm,
  onSearchChange,
  filterStatus,
  onFilterChange,
  loading,
  items,
  renderItem,
}) {
  return (
    <div className="space-y-8 animate-in fade-in slide-in-from-bottom-4 duration-700 pb-20">
      {/* HEADER */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
        <div className="flex items-center gap-6">
          <div className="w-16 h-16 bg-primary rounded-[2rem] flex items-center justify-center text-white shadow-2xl shadow-primary">
            <Icon size={32} strokeWidth={2.5} />
          </div>
          <div>
            <h1 className="text-4xl font-black text-foreground tracking-tight leading-none mb-2">
              {title}
            </h1>
            <p className="text-xs font-bold text-muted-foreground uppercase tracking-[0.3em] flex items-center gap-2">
              <span className="w-8 h-px bg-secondary"></span>
              {subtitle}
            </p>
          </div>
        </div>

        <button
          onClick={onCreateClick}
          className="flex items-center px-8 py-5 bg-primary text-white rounded-[2rem] text-[10px] font-black shadow-2xl shadow-accent hover:bg-foreground hover:-translate-y-1 transition-all uppercase tracking-[0.2em] group"
        >
          <Icon className="w-5 h-5 mr-3 group-hover:scale-125 transition-transform" />
          {createLabel}
        </button>
      </div>

      {/* FILTROS Y BUSCADOR */}
      <div className="flex flex-col xl:flex-row gap-6 items-center justify-between bg-white/50 backdrop-blur-sm p-6 rounded-[3rem] shadow-sm border border-border">
        {onFilterChange && <StatusFilter value={filterStatus} onChange={onFilterChange} />}
        <SearchBar
          value={searchTerm}
          onChange={onSearchChange}
          placeholder={searchPlaceholder}
        />
      </div>

      {/* GRID */}
      <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 2xl:grid-cols-4 gap-8">
        {loading ? (
          Array.from({ length: 8 }).map((_, i) => (
            <div
              key={i}
              className="animate-pulse bg-card border border-border rounded-[2.5rem] h-80 shadow-sm"
            />
          ))
        ) : (
          <>
            {/* CARD CREAR */}
            <CreateCard label={createLabel} onClick={onCreateClick} />

            {/* ITEMS */}
            {items.map(renderItem)}
          </>
        )}
      </div>
    </div>
  );
}
