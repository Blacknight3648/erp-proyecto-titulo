import React from "react";
import { UserPlus } from "lucide-react";
import CreateCard from "../ui/CreateCard";
import SearchBar from "../ui/SearchBar";
import StatusFilter from "../ui/StatusFilter";

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
          <div className="w-16 h-16 bg-blue-600 rounded-[2rem] flex items-center justify-center text-white shadow-2xl shadow-blue-200">
            <Icon size={32} strokeWidth={2.5} />
          </div>
          <div>
            <h1 className="text-4xl font-black text-gray-800 tracking-tight leading-none mb-2">
              {title}
            </h1>
            <p className="text-xs font-bold text-gray-400 uppercase tracking-[0.3em] flex items-center gap-2">
              <span className="w-8 h-px bg-gray-200"></span>
              {subtitle}
            </p>
          </div>
        </div>

        <button
          onClick={onCreateClick}
          className="flex items-center px-8 py-5 bg-blue-600 text-white rounded-[2rem] text-[10px] font-black shadow-2xl shadow-blue-100 hover:bg-black hover:-translate-y-1 transition-all uppercase tracking-[0.2em] group"
        >
          <UserPlus className="w-5 h-5 mr-3 group-hover:scale-125 transition-transform" />
          {createLabel}
        </button>
      </div>

      {/* FILTROS Y BUSCADOR */}
      <div className="flex flex-col xl:flex-row gap-6 items-center justify-between bg-white/50 backdrop-blur-sm p-6 rounded-[3rem] shadow-sm border border-gray-100">
        <StatusFilter value={filterStatus} onChange={onFilterChange} />
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
              className="animate-pulse bg-white border border-gray-50 rounded-[2.5rem] h-80 shadow-sm"
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
