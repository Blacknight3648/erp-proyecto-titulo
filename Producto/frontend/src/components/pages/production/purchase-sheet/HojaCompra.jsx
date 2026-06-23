import React from 'react';
import { useHCState } from '../../../../hooks/useHCState';
import ListaHC from './views/ListaHC';
import FormularioHC from './views/FormularioHC';
import DetalleHC from './views/DetalleHC';
import ModificacionHC from './views/ModificacionHC';

export default function HojaCompra() {
    const {
        view, setView,
        activeTab, setActiveTab,
        selectedHC, setSelectedHC,
        searchTerm, setSearchTerm,
        registros,
        loading,
        error,
        formData, setFormData,
        isReadOnly,
        isSubmitting,
        handleOpenForm,
        addItem,
        removeItem,
        updateItem,
        handleSave,
        aprobar,
        cerrar,
        handleOpenModificacion,
        consolidarOC,
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
                    aprobar={aprobar}
                    cerrar={cerrar}
                    onModificar={handleOpenModificacion}
                    loading={loading}
                    error={error}
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
                    aprobar={aprobar}
                    cerrar={cerrar}
                    onModificar={handleOpenModificacion}
                />
            )}

            {view === 'modificacion' && (
                <ModificacionHC
                    selectedHC={selectedHC}
                    setSelectedHC={setSelectedHC}
                    registros={registros}
                    setView={setView}
                    formatCLP={formatCLP}
                    consolidarOC={consolidarOC}
                />
            )}
        </div>
    );
}
