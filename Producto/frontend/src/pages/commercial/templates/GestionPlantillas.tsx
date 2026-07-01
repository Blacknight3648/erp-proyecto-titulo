import React from 'react';
import { Settings, Plus, X, Check } from 'lucide-react';
import { usePlantillasState } from '../../../hooks/usePlantillasState';
import ListaPlantillas from './ListaPlantillas';
import ConfiguracionTecnica from './ConfiguracionTecnica';
import FieldModal from './FieldModal';

export default function GestionPlantillas() {
    const {
        configuraciones, loading, remove,
        camposDisponibles,
        expanded, toggleExpand,
        adding, setAdding,
        newName, setNewName,
        editCampos, editTelas, editAccesorios, editCustomFields,
        newCampos, newTelas, newAccesorios, newCustomFields,
        fieldModal, setFieldModal,
        toggleFieldInEdit, toggleFieldInNew,
        handleSaveEdit, handleCreate,
        updateTableItem, addTableItem, removeTableItem,
        handleAddField, removeCustomField
    } = usePlantillasState();

    if (loading) {
        return (
            <div className="flex items-center justify-center h-64">
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
            </div>
        );
    }

    return (
        <div className="max-w-5xl mx-auto p-6 space-y-8 animate-in fade-in duration-500">
            {/* Header */}
            <div className="flex items-center justify-between">
                <div>
                    <h1 className="text-3xl font-black text-gray-800 tracking-tight italic flex items-center gap-3">
                        <Settings className="w-8 h-8 text-blue-600" />
                        Biblioteca de Plantillas
                    </h1>
                    <p className="text-gray-400 font-bold text-xs uppercase tracking-widest mt-1 ml-11">
                        Gestión de Fichas Técnicas Integrales
                    </p>
                </div>
                {!adding && (
                    <button
                        onClick={() => setAdding(true)}
                        className="flex items-center gap-2 px-5 py-3 bg-blue-600 text-white rounded-2xl font-black text-[10px] uppercase tracking-widest shadow-lg shadow-blue-100 hover:bg-blue-700 hover:-translate-y-0.5 transition-all"
                    >
                        <Plus className="w-4 h-4" /> Gestionar Campos de Plantillas
                    </button>
                )}                
                {!adding && (
                    <button
                        onClick={() => setAdding(true)}
                        className="flex items-center gap-2 px-5 py-3 bg-blue-600 text-white rounded-2xl font-black text-[10px] uppercase tracking-widest shadow-lg shadow-blue-100 hover:bg-blue-700 hover:-translate-y-0.5 transition-all"
                    >
                        <Plus className="w-4 h-4" /> Nueva Prenda
                    </button>
                )}
            </div>

            {/* Formulario nueva prenda */}
            {adding && (
                <div className="bg-white p-8 rounded-[2.5rem] border-2 border-blue-100 shadow-xl space-y-8 animate-in slide-in-from-top-4 duration-300">
                    <div className="flex items-center gap-4">
                        <input
                            autoFocus
                            type="text"
                            value={newName}
                            onChange={e => setNewName(e.target.value.toUpperCase())}
                            placeholder="Nombre de la prenda (ej: MOCHILA)"
                            className="flex-1 p-4 rounded-2xl bg-gray-50 border border-gray-100 text-lg font-black text-blue-600 outline-none focus:ring-2 focus:ring-blue-500 uppercase tracking-widest"
                        />
                        <div className="flex gap-2">
                            <button onClick={handleCreate} disabled={!newName.trim()}
                                className="px-6 py-4 bg-green-500 text-white rounded-2xl font-black text-[10px] uppercase tracking-widest hover:bg-green-600 transition-all disabled:opacity-40 shadow-lg shadow-green-50">
                                <Check className="w-5 h-5 inline mr-2" /> Crear Plantilla
                            </button>
                            <button onClick={() => setAdding(false)}
                                className="p-4 bg-gray-100 text-gray-500 rounded-2xl hover:bg-gray-200 transition-all">
                                <X className="w-5 h-5" />
                            </button>
                        </div>
                    </div>

                    <ConfiguracionTecnica
                        isNew={true}
                        configId={null}
                        camposActivos={newCampos}
                        customFields={newCustomFields}
                        telas={newTelas}
                        accesorios={newAccesorios}
                        availableFields={camposDisponibles}
                        onToggleField={(_, field) => toggleFieldInNew(field)}
                        onRemoveCustomField={removeCustomField}
                        onOpenFieldModal={(isNew, id) => setFieldModal({ open: true, isNew, configId: id, fieldName: "" })}
                        onAddTableItem={addTableItem}
                        onUpdateTableItem={updateTableItem}
                        onRemoveTableItem={removeTableItem}
                    />
                </div>
            )}

            {/* Lista de configuraciones */}
            <ListaPlantillas
                configuraciones={configuraciones}
                expanded={expanded}
                toggleExpand={toggleExpand}
                remove={remove}
                availableFields={camposDisponibles}
                editCampos={editCampos}
                editTelas={editTelas}
                editAccesorios={editAccesorios}
                editCustomFields={editCustomFields}
                toggleFieldInEdit={toggleFieldInEdit}
                removeCustomField={removeCustomField}
                setFieldModal={setFieldModal}
                addTableItem={addTableItem}
                updateTableItem={updateTableItem}
                removeTableItem={removeTableItem}
                handleSaveEdit={handleSaveEdit}
            />

            {configuraciones.length === 0 && !adding && (
                <div className="text-center py-24 bg-gray-50/50 rounded-[3rem] border-2 border-dashed border-gray-100">
                    <Settings className="w-16 h-16 mx-auto mb-4 text-gray-200" />
                    <h3 className="font-black uppercase tracking-widest text-gray-400 text-sm">Biblioteca Vacía</h3>
                    <p className="text-xs text-gray-300 mt-2">Empieza creando una plantilla base para tus costeros.</p>
                </div>
            )}

            {/* Modal para nuevos campos */}
            <FieldModal 
                fieldModal={fieldModal}
                setFieldModal={setFieldModal}
                onConfirm={handleAddField}
            />
        </div>
    );
}
