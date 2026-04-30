import { useState, useEffect } from "react";
import jsPDF from "jspdf";
import autoTable from "jspdf-autotable";
import SolicitudListView from "../../../../../../components/layout/SolicitudListView";
import SolicitudFormSCOT from "./SolicitudFormSCOT";
import { useComercial } from "../../../../../../hooks/useComercial";
import {toast} from "sonner";

const generateId = () => {
    try { return crypto.randomUUID(); }
    catch (e) { return Math.random().toString(36).substring(2, 15); }
};

const initialSCOTForm = {
    idSolicitud: "",
    numero: "",
    fecha: "",
    clienteId: "",
    vendedorId: "",
    esMuestra: false,
    cantidad: 0,
    tipo: "SCOT",
    observaciones: "",
    prendas: [],
};

export default function SolicitudCotizacionesContainer() {
    const [view, setView] = useState("list");
    const { solicitudesCotizaciones, load, createSolicitudCotizaciones, updateSolicitudCotizaciones, deleteSolicitudCotizaciones } = useComercial();
    const [formData, setFormData] = useState(initialSCOTForm);
    const [searchTerm, setSearchTerm] = useState("");

    useEffect(() => { load(); }, []);

    const handleSave = async () => {
        if (!formData.clienteId) {
            toast.error("Por favor seleccione un cliente.");
            return;
        }
        if (!formData.prendas || formData.prendas.length === 0) {
            toast.error("Debe agregar al menos una prenda.");
            return;
        }

        try {
            const payload = {
                clienteId: parseInt(formData.clienteId) ,
                vendedorId: formData.vendedorId? parseInt(formData.vendedorId) : null,
                articuloDescripcion: `SCOT - ${formData.prendas[0]?.nombre || "Productos"}`,
                cantidad: formData.prendas.reduce((acc, p) => acc + (parseInt(p.cantidad) || 0), 0),
                esMuestra: formData.esMuestra,
                tipo: "SCOT",
                observaciones: formData.observaciones || "",
                prendas: (formData.prendas || []).map(p => ({
                    nombre: p.nombre,
                    proveedorReferencia: p.proveedorReferencia,
                    cantidad: parseInt(p.cantidad) || 0,
                })),
            };

            if (formData.idSolicitud && !isNaN(formData.idSolicitud)) {
                await updateSolicitudCotizaciones(formData.idSolicitud, payload);
            } else {
                await createSolicitudCotizaciones(payload);
            }

            await load();
            toast.success("Solicitud de cotización guardada con éxito!");
            setView("list");
        } catch (e) {
            console.error("Error saving SCOT", e);
            toast.error("Error al guardar en el servidor");
        }
    };

    const handleDownloadPDF = (scot) => {
        const doc = new jsPDF();
        doc.setFontSize(18);
        doc.text(`Cotización #${scot.numero || scot.id}`, 14, 22);
        doc.setFontSize(12);
        doc.text(`Cliente ID: ${scot.clienteId}`, 14, 32);
        doc.text(`Tipo: ${scot.tipo}`, 14, 40);

        const rows = (scot.prendas || []).map(p => ([
            p.nombre, p.proveedorReferencia || "-", p.cantidad
        ]));
        if (rows.length > 0) {
            autoTable(doc, {
                startY: 50,
                head: [["Nombre Prenda", "Proveedor Referencia", "Cantidad"]],
                body: rows,
            });
        }
        doc.save(`SCOT_${scot.numero || scot.id}.pdf`);
    };

    const handleDelete = async (scot) => {

        try {
            await deleteSolicitudCotizaciones(scot.id);
            await load();
            toast.success("Solicitud de cotización eliminada correctamente");
        } catch (e) {
            console.error("Error deleting SCOT", e);
            toast.error("Error al eliminar la solicitud");
        }
    };

    const handleOpenForm = (record) => {
        if (record) {
            setFormData({
                ...initialSCOTForm,
                ...record,
                idSolicitud: record.id,
                prendas: (record.prendas || []).map(p => ({ ...p, id: p.id || generateId() })),
            });
        } else {
            setFormData({ ...initialSCOTForm });
        }
        setView("form");
    };

    const filteredRecords = solicitudesCotizaciones.filter(s =>
        s.tipo?.toUpperCase() === "SCOT" &&
        ((s.articuloDescripcion ?? "").toLowerCase().includes(searchTerm.toLowerCase()) ||
            (s.numero ?? "").toLowerCase().includes(searchTerm.toLowerCase()))
    );

    return view === "list" ? (
        <SolicitudListView
            title="Solicitudes de Cotizaciones"
            subtitle="Solicitud de cotizacion a adquisiciones evaluar precios"
            searchTerm={searchTerm}
            setSearchTerm={setSearchTerm}
            filteredRecords={filteredRecords}
            onCreateCosto={null}        // ← oculta el botón SCOS
            onCreateCotizacion={() => handleOpenForm(null)}
            onOpen={handleOpenForm}
            onDelete={handleDelete}
            onDownloadPDF={handleDownloadPDF}
        />
    ) : (
        <SolicitudFormSCOT
            formData={formData}
            setFormData={setFormData}
            onSave={handleSave}
            onBack={() => setView("list")}
        />
    );
}
