import React from 'react';
import HCSolicitudForm from '../components/HCSolicitudForm';

export default function FormularioHC({ 
    formData, setFormData, setView, isSubmitting, 
    onSave, addItem, removeItem, updateItem, 
    totalItems, totalBudget, formatCLP
}) {
    // Si no tiene ID, asumimos que es una creación
    const isCreation = !formData.id;

    return (
        <HCSolicitudForm 
            formData={formData}
            setFormData={setFormData}
            onCancel={() => setView('list')}
            onSave={onSave}
            isSubmitting={isSubmitting}
            addItem={addItem}
            removeItem={removeItem}
            updateItem={updateItem}
            totalItems={totalItems}
            totalBudget={totalBudget}
            formatCLP={formatCLP}
        />
    );
}
