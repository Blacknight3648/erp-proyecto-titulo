import React from 'react';
import { useCosteosOPState } from '../../../../hooks/useCosteosOPState';
import ListaCosteos from './Views/ListaCosteos';
import FormularioCosteo from './Views/FormularioCosteo';
import DashboardCosteos from './Views/Modals/DashboardCosteos';
import PriceComparisonModal from './Views/Modals/PriceComparisonModal';

export default function CosteosOP() {
    const {
        view, setView,
        activeTab, setActiveTab,
        searchTerm, setSearchTerm,
        statusFilter, setStatusFilter,
        showDashboard, setShowDashboard,
        showCompareModal, setShowCompareModal,
        isLoading,
        dashboardStats,
        filteredRecords,
        totalMateriales,
        totalMO,
        totalCostosFijos,
        totalGeneral,
        currentSolicitud,
        costeoVersion,
        costeoEstado,
        motivoRechazo,
        insumos,
        handleOpenForm,
        handleUpdateItem,
        handleRemoveItem,
        handleAddItem,
        handleValidateCostos,
        handleAprobarCosteo,
        handleRechazarCosteo,
        handleReabrirCosteo,
        clientes,
        moPrenda, setMoPrenda,
        moCinta, setMoCinta,
        moCosturaSellada, setMoCosturaSellada,
        moAcolchado, setMoAcolchado,
        costoHilo, setCostoHilo,
        costoMoPropia, setCostoMoPropia,
        costoGratificacion, setCostoGratificacion,
        costoEtiqueta, setCostoEtiqueta,
        costoEmbalaje, setCostoEmbalaje,
        costoFlete, setCostoFlete,
        getHistorialVersionesCosteo
    } = useCosteosOPState();

    if (isLoading) {
        return (
            <div className="min-h-screen flex items-center justify-center bg-gray-50/30 backdrop-blur-sm">
                <div className="flex flex-col items-center">
                    <div className="w-16 h-16 border-4 border-green-500 border-t-transparent rounded-full animate-spin mb-4" />
                    <p className="text-[10px] font-black text-gray-400 uppercase tracking-[0.3em] animate-pulse">Sincronizando Motor de Costos...</p>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50/30">
            {view === 'list' ? (
                <ListaCosteos
                    onOpenDashboard={() => setShowDashboard(true)}
                    onOpenCompare={() => setShowCompareModal(true)}
                    searchTerm={searchTerm}
                    setSearchTerm={setSearchTerm}
                    statusFilter={statusFilter}
                    setStatusFilter={setStatusFilter}
                    recordsToDisplay={filteredRecords}
                    clientes={clientes}
                    handleOpenForm={handleOpenForm}
                    onAprobar={handleAprobarCosteo}
                    onRechazar={handleRechazarCosteo}
                    onReabrir={handleReabrirCosteo}
                />
            ) : (
                <FormularioCosteo
                    onBack={() => setView('list')}
                    currentSolicitud={currentSolicitud}
                    costeoVersion={costeoVersion}
                    costeoEstado={costeoEstado}
                    motivoRechazo={motivoRechazo}
                    handleValidateCostos={handleValidateCostos}
                    totalMateriales={totalMateriales}
                    totalMO={totalMO}
                    totalCostosFijos={totalCostosFijos}
                    totalGeneral={totalGeneral}
                    clientes={clientes}
                    activeTab={activeTab}
                    setActiveTab={setActiveTab}
                    insumos={insumos}
                    handleUpdateItem={handleUpdateItem}
                    handleRemoveItem={handleRemoveItem}
                    handleAddItem={handleAddItem}
                    moPrenda={moPrenda} setMoPrenda={setMoPrenda}
                    moCinta={moCinta} setMoCinta={setMoCinta}
                    moCosturaSellada={moCosturaSellada} setMoCosturaSellada={setMoCosturaSellada}
                    moAcolchado={moAcolchado} setMoAcolchado={setMoAcolchado}
                    costoHilo={costoHilo} setCostoHilo={setCostoHilo}
                    costoMoPropia={costoMoPropia} setCostoMoPropia={setCostoMoPropia}
                    costoGratificacion={costoGratificacion} setCostoGratificacion={setCostoGratificacion}
                    costoEtiqueta={costoEtiqueta} setCostoEtiqueta={setCostoEtiqueta}
                    costoEmbalaje={costoEmbalaje} setCostoEmbalaje={setCostoEmbalaje}
                    costoFlete={costoFlete} setCostoFlete={setCostoFlete}
                    getHistorialVersionesCosteo={getHistorialVersionesCosteo}
                />
            )}

            <DashboardCosteos
                show={showDashboard}
                onClose={() => setShowDashboard(false)}
                dashboardStats={dashboardStats}
            />

            <PriceComparisonModal
                show={showCompareModal}
                onClose={() => setShowCompareModal(false)}
            />
        </div>
    );
}
