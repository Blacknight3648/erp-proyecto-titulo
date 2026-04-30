import { useState, useEffect } from "react";
import jsPDF from "jspdf";
import autoTable from "jspdf-autotable";
import SolicitudListView from "../../../../../../components/layout/SolicitudListView.jsx";
import SolicitudForm from "./SolicitudFormSCOS.jsx";
import { useComercial } from "../../../../../../hooks/useComercial.js";
import {toast} from "sonner";

const initialSCOSForm = {
  idSolicitud: "",
  numero: "",
  fecha: "",
  clienteId: "",
  vendedorId: "",
  nroReferencia: "",
  articuloId: "",
  articuloDescripcion: "",
  nombrePrenda: "",
  genero:"",
  esMuestra: false,
  cantidad: 0,
  tipo: "SCOS",
  costoTotalCalculado: 0,

  telas: [],
  accesorios: [],
  logotipo: [],
  plantillas: [],

  hasLogo: false,

  linkReferencia: "",
  tallaje: "",
  vinculos: [],

};

export default function SolicitudCostosContainer() {
  const [view, setView] = useState("list");
  const { solicitudesCostos, load, createSolicitudCostos, updateSolicitudCostos, deleteSolicitudCostos } = useComercial();
  const [formData, setFormData] = useState(initialSCOSForm);
  const [searchTerm, setSearchTerm] = useState("");
  const [isEditing, setIsEditing] = useState(false);

  useEffect(() => {
    load();
  }, []);


  const handleSave = async () => {
    if (!formData.articuloDescripcion || !formData.clienteId || !formData.genero) {
      toast.error("Por favor complete el articulo, seleccione un cliente y defina el género");
      return;
    }

    try {
      const payload = {
        clienteId: parseInt(formData.clienteId) ,
        vendedorId: formData.vendedorId ? parseInt(formData.vendedorId): null ,
        articuloDescripcion: formData.articuloDescripcion,
        nombrePrenda: formData.nombrePrenda,
        genero: formData.genero,
        cantidad: parseInt(formData.cantidad) || 0,
        esMuestra: formData.esMuestra,
        hasLogo: formData.hasLogo,
        tallaje: formData.tallaje,
        tipo: "SCOS",
        telas:(formData.telas || []).map(t => ({
          id: t.id && !isNaN(t.id) ? parseInt(t.id) : null,
          tempId: t.id, // Enviar UUID original como tempId
          aplicacion: t.aplicacion,
          nombre: t.nombre,
          proveedorReferencia: t.proveedorReferencia || t.nombre,
          composicion: t.composicion,
          color: t.color,
          peso: t.peso,
          unidadMedida: t.unidadMedida || "MTRS"
        })),
        accesorios: (formData.accesorios || []).map(a => ({
          id: a.id && !isNaN(a.id) ? parseInt(a.id) : null,
          tempId: a.id, // Enviar UUID original como tempId
          tipo: a.tipo,
          nombreAccesorio: a.nombreAccesorio,
          cantidad: a.cantidad,
        })),
        plantillas: (formData.plantillas || []).map(p => ({
          id: p.id && !isNaN(p.id) ? parseInt(p.id) : null,
          nombre: p.nombre,
          descripcion: p.descripcion,
          nombrePrenda: p.nombrePrenda,
          forro:              p.forro,
          relleno:            p.relleno,
          colorForro:         p.colorForro,
          gorro:              p.gorro,
          cuello:             p.cuello,
          abotonaduraCierre:  p.abotonaduraCierre,
          cortesAplicaciones: p.cortesAplicaciones,
          fuelles:            p.fuelles,
          mangas:             p.mangas,
          pretinasRuedo:      p.pretinasRuedo,
          bolsillos:          p.bolsillos,
          cintaDetalle:       p.cintaDetalle,
          logoDetalle:        p.logoDetalle,
          accesoriosDetalle:  p.accesoriosDetalle,
          obsModelo:          p.obsModelo,
          genero:             p.genero || formData.genero,
          camposActivos:      p.camposActivos || [],
          telas: (p.telas || []).map(t => ({
            aplicacion: t.aplicacion,
            nombre: t.nombre,
            composicion: t.composicion,
            color: t.color,
            peso: t.peso,
            unidadMedida: t.unidadMedida || "MTRS"
          })),
          accesorios: (p.accesorios || []).map(a => ({
            tipo: a.tipo,
            nombreAccesorio: a.nombreAccesorio,
            cantidad: a.cantidad
          })),
          logotipos: (p.logotipos || []).map(l => ({
            tipo: l.tipo,
            nombre: l.nombre,
            ubicacion: l.ubicacion,
            color: l.color,
            tamanio: l.tamanio || l.tamano,
            cantidad: l.cantidad,
            precio: l.precio
          })),
          cintas: (p.cintas || []).map(c => ({
            tipo: c.tipo,
            marca: c.marca,
            medida: c.medida,
            cantidad: c.cantidad
          })),
          moPrenda: p.moPrenda || 0,
          moCinta: p.moCinta || 0,
          moCosturaSellada: p.moCosturaSellada || 0,
          moAcolchado: p.moAcolchado || 0,
          vinculos: (p.vinculos || []).map(v => ({
            id: v.id && !isNaN(v.id) ? parseInt(v.id) : null,
            tempId: v.id,
            fieldName: v.fieldName,
            materialType: v.materialType,
            materialId: v.materialId && !isNaN(v.materialId) ? parseInt(v.materialId) : null,
            tempMaterialId: v.materialId,
            cantidad: v.cantidad
          }))
        })),
        logotipos: (formData.logotipo || []).map(l => ({
          tipo: l.tipo,
          nombre: l.nombre,
          ubicacion: l.ubicacion,
          color: l.color,
          tamanio: l.tamanio || l.tamano,
          cantidad: l.cantidad,
          precio: l.precio,
        }))

      }; // simplificado para el ejemplo
      if (formData.idSolicitud && !isNaN(formData.idSolicitud)) {
        await updateSolicitudCostos(formData.idSolicitud, payload);
      } else {
        await createSolicitudCostos(payload);
      }

      await load();
      toast.success("Solicitud de costos guardada con éxito!");
      setView("list");
    } catch (e) {
      console.error("Error saving to backend", e);
      toast.error("Error al guardar en el servidor");
    }
  };



  // 🚀 Función PDF
  const handleDownloadPDF = (costos) => {
    const doc = new jsPDF();
    doc.setFontSize(18);
    doc.text(`Costos #${costos.numero || costos.id}`, 14, 22);
    doc.setFontSize(12);
    doc.text(`Cliente ID: ${costos.clienteId}`, 14, 32);
    doc.text(`Tipo: ${costos.tipo}`, 14, 40);
    doc.text(`Cantidad total: ${costos.cantidad}`, 14, 48);
    doc.text(`Costo total: $${costos.costoTotalCalculado?.toFixed(2)}`, 14, 56);

    // Tabla de prendas
    const rowsTelas = (costos.telas || []).map(t => ([
        t.aplicacion, t.nombreTela, t.composicion, t.color, t.peso
    ]));
    if (rowsTelas.length>0) {
      autoTable(doc,{
        startY: 65,
        head: [['Aplicacion', 'Nombre Tela', 'Composición', 'Color', 'Peso (kg)']],
        body: rowsTelas
      })
    }

    doc.save(`SCOS_${costos.numero || costos.id}.pdf`);
  };

  // 🚀 Función eliminar
  const handleDelete = async (costos) => {

    try {
      await deleteSolicitudCostos(costos.id);
      await load();
      toast.success("Solicitud de costo eliminada correctamente");
    } catch (e) {
      console.error("Error deleting costo", e);
      toast.error("Error al eliminar la solicitud de costo");
    }
  };


  const handleOpenForm = (record, type = "SCOS") => {
    if (record) {
      setFormData({ ...initialSCOSForm, ...record, idSolicitud: record.id, tipo: type });
      setIsEditing(false); // Ver existente -> Solo lectura inicial
    } else {
      setFormData({ ...initialSCOSForm, tipo: type });
      setIsEditing(true);  // Nueva -> Edición inmediata
    }
    setView("form");
  };

  const filteredRecords = solicitudesCostos.filter(s =>
      s.tipo?.toUpperCase() === "SCOS" &&
      ((s.articuloDescripcion ?? '').toLowerCase().includes(searchTerm.toLowerCase()) ||
          (s.numero ?? '').toLowerCase().includes(searchTerm.toLowerCase()))
  );

  return view === "list" ? (
      <SolicitudListView
          title="Solicitudes de costos"
          subtitle="Solicitud de costos interno a producción"
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
    <SolicitudForm
      formData={formData}
      setFormData={setFormData}
      onSave={handleSave}
      onBack={() => setView("list")}
      isEditing={isEditing}
      setIsEditing={setIsEditing}
    />
  );
}