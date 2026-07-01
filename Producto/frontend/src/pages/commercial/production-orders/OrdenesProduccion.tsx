import React, { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useOPState } from '../../../hooks/useOPState';
import ListaOP from './ListaOP';
import FormularioOP from './FormularioOP';
import DetalleOP from './DetalleOP';
import { toast } from 'sonner';

export default function OrdenDeProduccion() {
    const location = useLocation();
    const navigate = useNavigate();

    const getInitialView = () => {
        if (location.pathname === '/detalle-op') return 'detail';
        if (location.state?.initialView) return location.state.initialView;
        return 'list';
    };

    const {
        view, setView,
        activeTab, setActiveTab,
        selectedOP, setSelectedOP,
        searchTerm, setSearchTerm,
        loadingClientes,
        clientes,
        vendedores,
        proveedores,
        registros,
        evaluaciones,
        loadingComercial,
        formData, setFormData,
        isReadOnly,
        isSubmitting,
        submitStatus,
        sourceEVN,
        nextNumbers,
        evnModal, setEvnModal,
        handleOpenForm,
        addItem: addItem,
        removeItem: removeItem,
        updateItem: updateItem,
        handleSave: handleSave,
        updateSize,
        handleConfirmNV,
        totalItems,
        totalAmount
    } = useOPState(getInitialView());

    useEffect(() => {
        if (location.state?.selectedOP) {
            setSelectedOP(location.state.selectedOP);
            setView('detail');
            // Limpiamos el estado para evitar re-ejecuciones en re-renders
            navigate(location.pathname, { replace: true, state: { ...location.state, selectedOP: undefined } });
        } else if (location.state?.initialData) {
            handleOpenForm(location.state.initialData, 'template');
            // Limpiamos el estado de inicialización
            navigate(location.pathname, { replace: true, state: { ...location.state, initialData: undefined } });
        }
    }, [location.state, location.pathname, navigate, setSelectedOP, setView, handleOpenForm]);

    return (
        <div className="min-h-screen bg-transparent">
            {view === 'list' && (
                <ListaOP
                    registros={registros}
                    evaluaciones={evaluaciones}
                    loadingComercial={loadingComercial}
                    activeTab={activeTab}
                    setActiveTab={setActiveTab}
                    searchTerm={searchTerm}
                    setSearchTerm={setSearchTerm}
                    handleOpenForm={(data, mode) => {
                        handleOpenForm(data, mode);
                        if (mode === 'view') {
                            setSelectedOP(data.idOP || data.id);
                            setView('detail');
                        } else {
                            setView('form');
                        }
                    }}
                    evnModal={evnModal}
                    setEvnModal={setEvnModal}
                />
            )}

            {view === 'form' && (
                <FormularioOP
                    formData={formData}
                    setFormData={setFormData}
                    isReadOnly={isReadOnly}
                    sourceEVN={sourceEVN}
                    nextNumbers={nextNumbers}
                    clientes={clientes}
                    loadingClientes={loadingClientes}
                    vendedores={vendedores}
                    proveedores={proveedores}
                    addItem={addItem}
                    removeItem={removeItem}
                    updateItem={updateItem}
                    updateSize={updateSize}
                    handleConfirmNV={handleConfirmNV}
                    totalItems={totalItems}
                    totalAmount={totalAmount}
                    isSubmitting={isSubmitting}
                    submitStatus={submitStatus}
                    setView={setView}
                    onSave={handleSave}
                />
            )}

            {view === 'detail' && (
                <DetalleOP
                    selectedOP={selectedOP}
                    setSelectedOP={setSelectedOP}
                    registros={registros}
                    setView={setView}
                />
            )}
        </div>
    );



}
