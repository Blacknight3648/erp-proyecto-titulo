import React from 'react';
import { useHCState } from '../../../../hooks/useHCState';
import ListaHC from './views/ListaHC';
import FormularioHC from './views/FormularioHC';
import DetalleHC from './views/DetalleHC';

export default function HojaCompra() {
    const {
        view, setView,
        activeTab, setActiveTab,
        selectedHC, setSelectedHC,
        searchTerm, setSearchTerm,
        registros,
        formData, setFormData,
        isReadOnly,
        isSubmitting,
        handleOpenForm,
        addItem,
        removeItem,
        updateItem,
        handleSave,
        formatCLP,
        totalItems,
        totalBudget,
        totalUnits
    } = useHCState();

    return (
        <div className="min-h-screen bg-transparent">
            {view === 'list' && (
                <ListaHC
                    registros={registros}
                    activeTab={activeTab}
                    setActiveTab={setActiveTab}
                    searchTerm={searchTerm}
                    setSearchTerm={setSearchTerm}
                    handleOpenForm={handleOpenForm}
                />
            )}

            {view === 'form' && (
                <FormularioHC
                    formData={formData}
                    setFormData={setFormData}
                    setView={setView}
                    isSubmitting={isSubmitting}
                    onSave={handleSave}
                    addItem={addItem}
                    removeItem={removeItem}
                    updateItem={updateItem}
                    totalItems={totalItems}
                    totalBudget={totalBudget}
                    formatCLP={formatCLP}
                />
            )}

            {view === 'detail' && (
                <DetalleHC
                    selectedHC={selectedHC}
                    setSelectedHC={setSelectedHC}
                    registros={registros}
                    setView={setView}
                    formatCLP={formatCLP}
                />
            )}
        </div>
    );
}
