import { Search, Filter, History } from 'lucide-react';

export default function SearchFilterBar({ onSearch }) {
    return (
        <div className="bg-white p-4 rounded-2xl shadow-sm border border-gray-100 flex flex-wrap gap-4 items-center">
            <div className="relative flex-1 min-w-[200px]">
                <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 w-4 h-4" />
                <input
                    type="text"
                    onChange={onSearch}
                    placeholder="Buscar por NV, Cliente..."
                    className="w-full pl-10 pr-4 py-2 bg-gray-50 border-none rounded-xl text-sm focus:ring-2 focus:ring-indigo-500 outline-none"
                />
            </div>
            <div className="flex space-x-2">
                <button className="flex items-center px-3 py-2 bg-gray-50 text-gray-500 rounded-lg text-xs font-bold border border-transparent hover:border-gray-200">
                    <Filter className="w-3 h-3 mr-2" /> Estado
                </button>
                <button className="flex items-center px-3 py-2 bg-gray-50 text-gray-500 rounded-lg text-xs font-bold border border-transparent hover:border-gray-200">
                    <History className="w-3 h-3 mr-2" /> Fecha
                </button>
            </div>
        </div>
    );
}
