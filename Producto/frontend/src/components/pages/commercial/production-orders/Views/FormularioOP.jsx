import React from 'react';
import OPSolicitudForm from '../components/OPSolicitudForm';
import OPModificacion from '../components/OPModificacion';

export default function FormularioOP({ 
    formData, setFormData, isReadOnly, setView, isSubmitting, 
    clientes, nextNumbers, onSave 
}) {
    // Si no tiene ID, asumimos que es una creación (Solicitud)
    const isCreation = !formData.id && !formData.idOP;

    if (isCreation) {
        return (
            <OPSolicitudForm 
                formData={formData}
                setFormData={setFormData}
                onCancel={() => setView('list')}
                onSave={onSave}
                isSubmitting={isSubmitting}
                clientes={clientes}
                nextNumbers={nextNumbers}
            />
        );
    }

    // Si tiene ID y no es solo lectura, permitimos la modificación
    return (
        <OPModificacion 
            formData={formData}
            setFormData={setFormData}
            onCancel={() => setView('list')} // O volver al detalle si prefieres
            onSave={onSave}
            isSubmitting={isSubmitting}
        />
    );
}
