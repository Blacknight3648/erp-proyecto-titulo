import { useState, useEffect } from "react";
import jsPDF from "jspdf";
import autoTable from "jspdf-autotable";
import SolicitudListView from "../../../../../../components/layout/SolicitudListView.jsx";
import SolicitudForm from "./SolicitudFormSCOS.jsx";
import { useComercial } from "../../../../../../hooks/useComercial.js";
import { FIELD_LABELS } from "../../../../../../hooks/usePlantillas.js";
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
    // Cuando se seleccionó 'OTRO', el nombre real del artículo viene de nombrePrenda
    const articuloDescripcionFinal = formData.articuloDescripcion === 'OTRO'
      ? (formData.nombrePrenda || '').toUpperCase().trim()
      : formData.articuloDescripcion;

    if (!articuloDescripcionFinal || !formData.clienteId || !formData.genero) {
      toast.error("Por favor complete el artículo, seleccione un cliente y defina el género");
      return;
    }

    try {
      const esPrendaNueva = !!(formData.esPrendaNueva);
      console.log('[SCOS] primeraPlantilla.accesorios:', (formData.plantillas || [])[0]?.accesorios);

      // Convertir detallesPrenda del panel de especificaciones técnicas
      // al formato que espera el backend: [{nombreCampo, valorDescripcion}]
      const primeraPlantilla = (formData.plantillas || [])[0] || {};
      const detallesPrenda = primeraPlantilla.detallesPrenda || {};
      const vinculosPl = primeraPlantilla.vinculos || [];

      const descripciones = Object.entries(detallesPrenda)
        .filter(([, val]) => val !== null && val !== undefined && String(val).trim() !== '')
        .map(([nombreCampo, valorDescripcion]) => {
          const vinculosFiltrados = vinculosPl
            .filter(v => v.fieldName === nombreCampo)
            .map(v => {
              const isNumericId = v.id && !isNaN(v.id);
              const isNumericMatId = v.materialId && !isNaN(v.materialId);
              return {
                id: isNumericId ? parseInt(v.id) : null,
                tempId: isNumericId ? null : v.id,
                materialType: v.materialType,
                materialId: isNumericMatId ? parseInt(v.materialId) : null,
                tempMaterialId: isNumericMatId ? null : v.materialId,
                cantidad: parseInt(v.cantidad) || 1
              };
            })
            .filter(v => v.materialId !== null);

          // La tabla `plantilla` almacena los nombres en mayúsculas (ej: 'ABOTONADURA / CIERRE').
          // FIELD_LABELS tiene el label display (ej: 'Abotonadura / Cierre') → .toUpperCase() = exacto.
          const nombreCampoBackend = (FIELD_LABELS[nombreCampo] || nombreCampo).toUpperCase();
          return {
            nombreCampo: nombreCampoBackend,
            valorDescripcion: String(valorDescripcion),
            vinculos: vinculosFiltrados
          };
        });

      // Campos que el usuario llenó en Especificaciones Técnicas (camelCase keys)
      const camposPlantillaActivos = Object.keys(detallesPrenda).filter(
        k => detallesPrenda[k] !== null && detallesPrenda[k] !== undefined && String(detallesPrenda[k]).trim() !== ''
      );

      const payload = {
        clienteId: parseInt(formData.clienteId),
        vendedorId: formData.vendedorId ? parseInt(formData.vendedorId) : null,
        articuloDescripcion: articuloDescripcionFinal,
        nombrePrenda: formData.nombrePrenda,
        genero: formData.genero,
        cantidad: parseInt(formData.cantidad) || 0,
        esMuestra: formData.esMuestra,
        hasLogo: formData.hasLogo,
        esPrendaNueva,
        camposPlantilla: esPrendaNueva ? camposPlantillaActivos : undefined,
        tallaje: formData.tallaje,
        tipo: "SCOS",
        telas: (primeraPlantilla.telas || []).map(t => ({
          id: t.id && !isNaN(t.id) ? parseInt(t.id) : null,
          tempId: t.id,
          aplicacion: t.aplicacion,
          nombre: t.nombre,
          proveedorReferencia: t.proveedorReferencia || t.nombre,
          composicion: t.composicion,
          color: t.color,
          peso: t.peso,
          unidadMedida: t.unidadMedida || "MTRS"
        })),
        accesorios: (primeraPlantilla.accesorios || []).map(a => ({
          id: a.id && !isNaN(a.id) ? parseInt(a.id) : null,
          tempId: a.id,
          tipo: a.tipo,
          nombreAccesorio: a.nombreAccesorio,
          cantidad: a.cantidad,
        })),
        descripciones,
        logotipos: (primeraPlantilla.logotipos || []).map(l => {
          // El backend guarda tamano como VARCHAR(50): combinamos valor+unidad => "6 cm"
          const val    = l.tamanio || l.tamano || '';
          const unidad = l.unidadMedidaLogo || 'cm';
          const tamanoStr = val !== '' && val !== null && val !== undefined
            ? `${val} ${unidad}`.trim()
            : null;
          return {
            tipo:     l.tipo,
            nombre:   l.nombre,
            ubicacion: l.ubicacion,
            color:    l.color,
            tamanio:  tamanoStr,
            cantidad: l.cantidad,
            precio:   l.precio,
          };
        })
      };
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
      toast.error(e.response?.data?.mensaje || "Error al guardar en el servidor");
    }
  };


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


  // Mapeo inverso: label display en mayúsculas → clave camelCase de FIELD_LABELS
  // Ej: 'ABOTONADURA / CIERRE' → 'abotonaduraCierre'
  const LABEL_TO_CAMPO = Object.fromEntries(
    Object.entries(FIELD_LABELS).map(([key, label]) => [label.toUpperCase(), key])
  );

  const handleOpenForm = (record, type = "SCOS") => {
    if (record) {
      // Reconstruir detallesPrenda y vínculos a partir de descripciones del backend
      const detallesPrenda = {};
      const vinculos = [];

      (record.descripciones || []).forEach(d => {
        // El backend guarda el nombre en mayúsculas (ej: 'ABOTONADURA / CIERRE').
        // PlantillasPanel usa claves camelCase (ej: 'abotonaduraCierre') → revertir aquí.
        const camelKey = LABEL_TO_CAMPO[d.nombreCampo?.toUpperCase()] || d.nombreCampo;
        detallesPrenda[camelKey] = d.valorDescripcion;

        if (d.vinculos && d.vinculos.length > 0) {
          d.vinculos.forEach(v => {
            vinculos.push({
              id: v.id || v.tempId || Math.random().toString(36).substring(2, 9),
              fieldName: camelKey,
              materialType: v.materialType,
              materialId: v.materialId || v.tempMaterialId,
              cantidad: v.cantidad || 1
            });
          });
        }
      });

      // Mapear telas, accesorios y logotipos para la primera plantilla
      const telas = (record.telas || []).map(t => ({
        id: t.id || t.tempId || Math.random().toString(36).substring(2, 9),
        aplicacion: t.aplicacion,
        nombre: t.nombre,
        proveedorReferencia: t.proveedorReferencia,
        composicion: t.composicion,
        color: t.color,
        peso: t.peso,
        unidadMedida: t.unidadMedida || "MTRS"
      }));

      const accesorios = (record.accesorios || []).map(a => ({
        id: a.id || a.tempId || Math.random().toString(36).substring(2, 9),
        tipo: a.tipo,
        nombreAccesorio: a.nombreAccesorio,
        cantidad: a.cantidad
      }));

      const logotipos = (record.logotipos || []).map(l => {
        // El backend devuelve tamano como string "6 cm" o "10 in" — separamos
        const tamanoRaw  = l.tamanio || l.tamano || '';
        const partes     = String(tamanoRaw).trim().split(/\s+/);
        const valorNum   = partes[0] ? parseFloat(partes[0]) : '';
        const unidadStr  = partes[1] || 'cm';
        return {
          id:              Math.random().toString(36).substring(2, 9),
          tipo:            l.tipo,
          nombre:          l.nombre,
          ubicacion:       l.ubicacion,
          color:           l.color,
          tamanio:         isNaN(valorNum) ? '' : valorNum,
          unidadMedidaLogo: unidadStr,
          cantidad:        l.cantidad,
          precio:          l.precio,
        };
      });

      const plantillas = [{
        id: record.id,
        nombre: record.nombrePrenda,
        detallesPrenda,
        camposActivos: Object.keys(detallesPrenda),
        telas,
        accesorios,
        logotipos,
        vinculos
      }];

      setFormData({
        ...initialSCOSForm,
        ...record,
        plantillas,
        idSolicitud: record.id,
        tipo: type
      });
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