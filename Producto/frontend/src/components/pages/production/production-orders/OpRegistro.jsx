import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Loader2 } from 'lucide-react';
import { toast } from 'sonner';
import { useOpRegistroState } from '../../../../hooks/useOpRegistroState';
import { validateNumericInput } from '../../../../utils/validations';
import ListaOPs from './Views/ListaOPs';
import DetalleOP from './Views/DetalleOP';
import BulkEditOP from './Views/BulkEditOP';
import SelectionModal from './Views/Modals/SelectionModal';

export default function OpRegistro() {
    const navigate = useNavigate();
    const {
        selectedOP, setSelectedOP,
        view, setView,
        isSelectionMode, setIsSelectionMode,
        selectedOPIds, setSelectedOPIds,
        showSelectionModal, setShowSelectionModal,
        editingFieldIdx, setEditingFieldIdx,
        tempValue, setTempValue,
        showConfirmModal, setShowConfirmModal,
        isReadOnly, setIsReadOnly,
        isManualCutting, setIsManualCutting,
        isSubmitting,
        opFields,
        handleModificarRegistro,
        handleVerDetalles,
        toggleSelection,
        handleBulkEdit,
        handleSelectFieldInline,
        handleSaveInline,
        finalizeSave,
        calculateTotalQty,
        ordenes,
        isLoadingOrdenes,
        mockOpDetails
    } = useOpRegistroState();

    const getClientName = (op) => {
        if (!op) return '';
        return op.notaVentaId ? `NV ${op.notaVentaId}` : '-';
    };

    if (view === 'list' && isLoadingOrdenes) {
        return (
            <div className="flex flex-col items-center justify-center min-h-[calc(100vh-120px)] p-4">
                <div className="bg-white p-12 rounded-[3rem] shadow-xl border border-gray-100 flex flex-col items-center animate-in fade-in zoom-in-95 duration-500">
                    <Loader2 className="w-16 h-16 text-blue-600 animate-spin mb-6" />
                    <h2 className="text-xl font-black text-gray-800 mb-2">Consultando Servidor</h2>
                    <p className="text-gray-400 font-medium text-sm animate-pulse tracking-wide italic">Cargando Ordenes de Producción...</p>
                </div>
            </div>
        );
    }

    if (view === 'loading') {
        return (
            <div className="flex flex-col items-center justify-center min-h-[calc(100vh-120px)] p-4">
                <div className="bg-white p-12 rounded-[3rem] shadow-xl border border-gray-100 flex flex-col items-center animate-in fade-in zoom-in-95 duration-500">
                    <Loader2 className="w-16 h-16 text-blue-600 animate-spin mb-6" />
                    <h2 className="text-xl font-black text-gray-800 mb-2">Consultando Servidor</h2>
                    <p className="text-gray-400 font-medium text-sm animate-pulse tracking-wide italic">Sincronizando base de datos...</p>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50/10">
            {view === 'list' && (
                <ListaOPs
                    ordenes={ordenes}
                    isSelectionMode={isSelectionMode}
                    setIsSelectionMode={setIsSelectionMode}
                    selectedOPIds={selectedOPIds}
                    toggleSelection={toggleSelection}
                    setSelectedOP={setSelectedOP}
                    selectedOP={selectedOP}
                    handleModificarRegistro={handleModificarRegistro}
                    handleVerDetalles={handleVerDetalles}
                    handleBulkEdit={handleBulkEdit}
                    getClientName={getClientName}
                />
            )}

            {view === 'detail' && (
                <DetalleOP 
                    selectedOP={selectedOP}
                    view={view}
                    setView={setView}
                    isReadOnly={isReadOnly}
                    setIsReadOnly={setIsReadOnly}
                    editingFieldIdx={editingFieldIdx}
                    handleSelectFieldInline={handleSelectFieldInline}
                    opFields={opFields}
                    mockOpDetails={mockOpDetails}
                    tempValue={tempValue}
                    setTempValue={setTempValue}
                    isManualCutting={isManualCutting}
                    setIsManualCutting={setIsManualCutting}
                    calculateTotalQty={calculateTotalQty}
                    handleSaveInline={handleSaveInline}
                    isSubmitting={isSubmitting}
                    setEditingFieldIdx={setEditingFieldIdx}
                    showConfirmModal={showConfirmModal}
                    finalizeSave={finalizeSave}
                    setShowConfirmModal={setShowConfirmModal}
                    navigate={navigate}
                    getClientName={getClientName}
                    onBack={() => setView('list')}
                />
            )}

            {view === 'bulk_edit' && (
                <BulkEditOP 
                    editingFieldIdx={editingFieldIdx}
                    opFields={opFields}
                    selectedOPIds={selectedOPIds}
                    setTempValue={setTempValue}
                    finalizeSave={finalizeSave}
                    setView={setView}
                    setIsSelectionMode={setIsSelectionMode}
                    setSelectedOPIds={setSelectedOPIds}
                    setShowSelectionModal={setShowSelectionModal}
                    validateNumericInput={validateNumericInput}
                    toast={toast}
                    tempValue={tempValue}
                />
            )}

            <SelectionModal 
                show={showSelectionModal}
                onClose={() => setShowSelectionModal(false)}
                opFields={opFields}
                selectedOPIds={selectedOPIds}
                mockOpDetails={mockOpDetails}
                onSelectField={(idx) => {
                    setEditingFieldIdx(idx);
                    setTempValue('');
                    setShowSelectionModal(false);
                    setView('bulk_edit');
                }}
            />
        </div>
    );
}
