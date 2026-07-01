import React, { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useNVState } from '../../../hooks/useNVState';
import ListaNV from './ListaNV';
import FormularioNV from './FormularioNV';
import DetalleNV from './DetalleNV';

export default function NotaDeVenta() {
    const location = useLocation();
    const navigate = useNavigate();
    
    // Determinamos la vista inicial basada en la ruta o el estado de navegación
    const getInitialView = () => {
        if (location.pathname === '/detalle-nv') return 'detail';
        if (location.state?.initialView) return location.state.initialView;
        return 'list';
    };

    const {
        view, setView,
        activeTab, setActiveTab,
        selectedNV, setSelectedNV,
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
        addItem,
        removeItem,
        updateItem,
        updateSize,
        handleConfirmNV,
        totalItems,
        totalAmount
    } = useNVState(getInitialView());

    // Sincronizar selectedNV si viene desde la navegación (ej. Detalle 360)
    useEffect(() => {
        if (location.state?.selectedNV) {
            setSelectedNV(location.state.selectedNV);
            setView('detail');
            // Limpiamos el estado para evitar re-ejecuciones en re-renders
            navigate(location.pathname, { replace: true, state: { ...location.state, selectedNV: undefined } });
        } else if (location.state?.initialData) {
            handleOpenForm(location.state.initialData, 'template');
            // Limpiamos el estado de inicialización
            navigate(location.pathname, { replace: true, state: { ...location.state, initialData: undefined } });
        }
    }, [location.state, location.pathname, navigate, setSelectedNV, setView, handleOpenForm]);

    return (
        <div className="min-h-screen bg-transparent">
            {view === 'list' && (
                <ListaNV 
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
                            setSelectedNV(data.idNV || data.id);
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
                <FormularioNV 
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
                />
            )}

            {view === 'detail' && (
                <DetalleNV 
                    selectedNV={selectedNV}
                    setSelectedNV={setSelectedNV}
                    registros={registros}
                    setView={setView}
                />
            )}
        </div>
    );
}
