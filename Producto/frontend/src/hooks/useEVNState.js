import { useState, useMemo, useCallback, useEffect } from 'react';
import { toast } from 'sonner';
import { EvaluacionNegocioService } from '../remote/service/EvaluacionNegocioService';
import { useAuth } from '../contexts/AuthContext';
import { useProveedores } from './useProveedores';
import { useComercial } from './useComercial';
import { useProduccion } from './useProduccion';
import { useClientes } from './useClientes';
import { useVendedores } from './useVendedores';
import { validateNumericInput } from '../utils/validations';

export const DEFAULT_ITEM = {
    id: 1,
    numero: 1,
    cant: 0,
    descripcion: "",
    tipo: "SC",
    codigoInterno: "",
    proveedor: "",
    codigoProveedor: "",
    producto: "",
    modelo: "",
    genero: "",
    tela: "",
    composicion: "",
    costeoId: null,
    solicitudCostosId: null,
    costoProducto: 0,
    costoLogo: 0,
    costoOrdenTrabajo: 0,
    costosGenerales: 0,
    costoTotalUnitario: 0,
    costoTotal: 0,
    precioVenta20MG: 0,
    precioVentaNeto: 0,
    precioVentaTotal: 0,
    mgSobreCosto: 0,
    mgSobreVenta: 0,
    mgSobreVentaPesos: 0
};

export const DEFAULT_OTROS_COSTOS = {
    garantiaSeriedad: 0,
    garantiaFielCumplimiento: 0,
    flete: 0,
    certificacion: 0,
    muestras: 0,
    entregaPersonalizada: 0,
    tomaTallaje: {
        diasRecinto: 1,
        persRecinto: 2,
        colacionPorPersona: 20000,
        asignacionPorPersona: 10000,
        peajes: 0,
        bencinaPorLitro: 900,
        kmTotal: 0,
        rendimiento: 10,
        costoPersonal: 0,
        costoMovilizacion: 0,
        total: 0
    },
    pegadoCinta: [
        { id: 1, etiqueta: 'PANTALON', costoCinta: 400, costoMO: 2600, mtsCinta: 2.4, total: 3560 },
        { id: 2, etiqueta: 'TOP', costoCinta: 400, costoMO: 2500, mtsCinta: 2.0, total: 3300 }
    ],
    porcentajeComision: 0.05
};

export const parseId = (id) => {
    if (!id) return null;
    if (typeof id === 'number') return id;
    if (typeof id === 'string') {
        const match = id.match(/\d+/);
        return match ? parseInt(match[0]) : null;
    }
    return null;
};

export const useEVNState = (initialEval = null) => {
    const { user } = useAuth();
    const { proveedores } = useProveedores();
    const { clientes } = useClientes();
    const { vendedores } = useVendedores();
    const { solicitudesCostos, loadSolicitudesCostos } = useComercial();
    const { getAllCosteosBySCOS, getCosteosDisponiblesEVN, getCosteoResumenEVN } = useProduccion();

    // States
    const [items, setItems] = useState([{ ...DEFAULT_ITEM }]);
    const [otrosCostos, setOtrosCostos] = useState(DEFAULT_OTROS_COSTOS);
    const [solicitud, setSolicitud] = useState({ clienteNombre: "" });
    const [evalData, setEvalData] = useState({
        referencia: "",
        condiciones: {
            anticipo: 50,
            saldo: 50,
            flete: "Cliente",
            garantia: "30 días de corrido",
            plazoEntrega: "",
            formaPago: '30 Días',
            lugarEntrega: 'Instalaciones Cliente',
            validezOferta: '15 Días'
        }
    });

    const [vinculados, setVinculados] = useState({ scos: [], scot: [] });
    const [selectedSCOSIds, setSelectedSCOSIds] = useState(new Set());
    const [selectedSCOTIds, setSelectedSCOTIds] = useState(new Set());
    const [isSaving, setIsSaving] = useState(false);
    const [showQuotationModal, setShowQuotationModal] = useState(false);
    const [availableQuotations, setAvailableQuotations] = useState([]);
    const [pendingSCOS, setPendingSCOS] = useState(null);

    // Modal de selección de costeo para ítems OP
    const [costeosDisponibles, setCosteosDisponibles] = useState([]);
    const [showCosteoModal, setShowCosteoModal] = useState(false);
    const [costeoModalItemId, setCosteoModalItemId] = useState(null);
    const [loadingCosteos, setLoadingCosteos] = useState(false);

    useEffect(() => {
        loadSolicitudesCostos();
    }, [loadSolicitudesCostos]);

    // Initial hydration
    useEffect(() => {
        if (initialEval) {
            setSolicitud({
                clienteNombre: initialEval.clienteNombre || "Cliente #" + initialEval.clienteId,
                clienteId: initialEval.clienteId,
                vendedorId: initialEval.vendedorId
            });

            setItems((initialEval.items || []).map(it => ({
                ...DEFAULT_ITEM,
                id: it.idItemEVN || it.id || Math.random(),
                numero: it.nroItem || it.numero || 1,
                cant: it.cantidad || 0,
                tipo: it.tipoItem || it.tipo || "SC",
                producto: it.descripcion || it.producto || "",
                modelo: it.modelo || "",
                tela: it.tela || "",
                composicion: it.composicion || "",
                genero: it.genero || "",
                codigoInterno: it.codigoInterno || "",
                codigoProveedor: it.codigoProveedor || "",
                proveedorId: it.proveedorId || null,
                proveedor: it.proveedorNombre || "",
                costoProducto: it.costoProducto || it.costoUnitario || 0,
                costoLogo: it.costoLogo || 0,
                costoOrdenTrabajo: it.costoOrdenTrabajo || 0,
                precioVentaNeto: it.precioUnitario || 0,
            })));

            const hydratedOtrosCostos = { ...DEFAULT_OTROS_COSTOS };
            if (initialEval.porcentajeComision !== undefined) {
                hydratedOtrosCostos.porcentajeComision = initialEval.porcentajeComision;
            }

            if (initialEval.gastosAdicionales) {
                initialEval.gastosAdicionales.forEach(g => {
                    const monto = g.monto || 0;
                    if (g.tipoGasto === 'FLETE') hydratedOtrosCostos.flete = monto;
                    if (g.tipoGasto === 'GARANTIA_SERIEDAD') hydratedOtrosCostos.garantiaSeriedad = monto;
                    if (g.tipoGasto === 'GARANTIA_CUMPLIMIENTO') hydratedOtrosCostos.garantiaFielCumplimiento = monto;
                    if (g.tipoGasto === 'CERTIFICACION') hydratedOtrosCostos.certificacion = monto;
                    if (g.tipoGasto === 'MUESTRAS_FISICAS') hydratedOtrosCostos.muestras = monto;
                    if (g.tipoGasto === 'ENTREGA_PERSONALIZADA') hydratedOtrosCostos.entregaPersonalizada = monto;
                });
            }

            // Hidratar metadatos JSON para desgloses operativos
            if (initialEval.tomaTallajeMetadata) {
                try {
                    const ttData = JSON.parse(initialEval.tomaTallajeMetadata);
                    hydratedOtrosCostos.tomaTallaje = { ...DEFAULT_OTROS_COSTOS.tomaTallaje, ...ttData };
                } catch (e) { console.error("Error parsing TT metadata:", e); }
            } else if (initialEval.tomaTallaje) {
                hydratedOtrosCostos.tomaTallaje = {
                    ...DEFAULT_OTROS_COSTOS.tomaTallaje,
                    ...(initialEval.tomaTallaje || {})
                };
            }

            if (initialEval.pegadoCintaMetadata) {
                try {
                    const pcData = JSON.parse(initialEval.pegadoCintaMetadata);
                    hydratedOtrosCostos.pegadoCinta = pcData;
                } catch (e) { console.error("Error parsing PC metadata:", e); }
            }

            const rebuiltVinculados = { scos: [], scot: [] };

            // Fuente primaria: IDs directos del backend
            if (initialEval.costeoId) {
                rebuiltVinculados.scos.push({
                    id: initialEval.costeoId,
                    numero: initialEval.numeroCosteo || `SCOS-${initialEval.costeoId}`
                });
            }
            if (initialEval.solicitudCotizacionId) {
                rebuiltVinculados.scot.push({
                    id: initialEval.solicitudCotizacionId,
                    numero: initialEval.numeroSolicitud || `SCOT-${initialEval.solicitudCotizacionId}`
                });
            }

            // Fuente secundaria (respaldo): desde codigoInterno de items
            (initialEval.items || []).forEach(item => {
                if (item.codigoInterno) {
                    const docData = { id: item.idItemEVN || Math.random(), numero: item.codigoInterno };
                    const tipo = (item.tipoItem || '').toUpperCase();
                    if (tipo === 'SC' || item.codigoInterno.startsWith('SCOS')) {
                        if (!rebuiltVinculados.scos.some(v => v.numero === docData.numero))
                            rebuiltVinculados.scos.push(docData);
                    } else if (tipo === 'SCI' || item.codigoInterno.startsWith('SCOT')) {
                        if (!rebuiltVinculados.scot.some(v => v.numero === docData.numero))
                            rebuiltVinculados.scot.push(docData);
                    }
                }
            });
            setVinculados(rebuiltVinculados);
            setOtrosCostos(hydratedOtrosCostos);

            setEvalData(prev => ({
                ...prev,
                referencia: initialEval.referencia || "",
                condiciones: {
                    ...prev.condiciones,
                    plazoEntrega: initialEval.fechaEvaluacion || ""
                }
            }));
        } else {
            resetState();
        }
    }, [initialEval]);

    // Totals Calculation Engine (useMemo)
    const totals = useMemo(() => {
        const currentItems = items || [];
        const currentOtros = otrosCostos || DEFAULT_OTROS_COSTOS;
        const tt = currentOtros.tomaTallaje || DEFAULT_OTROS_COSTOS.tomaTallaje;

        const costoPersonalTT = (Number(tt.diasRecinto || 0) * Number(tt.persRecinto || 0)) * (Number(tt.colacionPorPersona || 0) + Number(tt.asignacionPorPersona || 0));
        const divisorKmLt = Math.max(Number(tt.rendimiento || 1), 1);
        const costoMovilizacionTT = Number(tt.peajes || 0) + (Number(tt.bencinaPorLitro || 0) * (Number(tt.kmTotal || 0) / divisorKmLt));
        const costoRecintosTT = (Number(tt.diasRecinto || 0) * Number(tt.persRecinto || 0)) * Number(tt.peajes || 0);
        const totalTT = (isNaN(costoPersonalTT) ? 0 : costoPersonalTT) + (isNaN(costoMovilizacionTT) ? 0 : costoMovilizacionTT);

        const listaCinta = currentOtros.pegadoCinta || [];
        const itemsCintaCalculados = listaCinta.map(c => {
            const val = (Number(c.costoCinta || 0) * Number(c.mtsCinta || 0)) + Number(c.costoMO || 0);
            return { ...c, total: isNaN(val) ? 0 : val };
        });
        const totalPC = itemsCintaCalculados.reduce((sum, c) => sum + (c.total || 0), 0);

        const totalOtrosCostos = (
            Number(currentOtros.garantiaSeriedad || 0) +
            Number(currentOtros.garantiaFielCumplimiento || 0) +
            Number(currentOtros.flete || 0) +
            Number(currentOtros.certificacion || 0) +
            Number(currentOtros.muestras || 0) +
            Number(currentOtros.entregaPersonalizada || 0) +
            totalTT
        );

        const totalCantidades = currentItems.reduce((sum, item) => sum + (Number(item.cant) || 0), 0);
        const prorrateoLineal = totalCantidades <= 0 ? 0 : totalOtrosCostos / totalCantidades;
        const costoCintaUnitario = totalCantidades > 0 ? totalPC / totalCantidades : 0;

        const itemsConCostos = currentItems.map(item => {
            const cant = Number(item.cant) || 0;
            const costoProd = Number(item.costoProducto) || 0;
            const pVentaNeto = Number(item.precioVentaNeto) || 0;

            const costoTotalUnitario =
                costoProd +
                (Number(item.costoLogo) || 0) +
                (Number(item.costoOrdenTrabajo) || 0) +
                costoCintaUnitario +
                prorrateoLineal;

            const precioVenta20MG = costoTotalUnitario / 0.75;
            const precioVentaTotal = pVentaNeto * cant;
            const costoTotalItem = costoTotalUnitario * cant;

            return {
                ...item,
                costosGenerales: prorrateoLineal,
                costoTotalUnitario: isNaN(costoTotalUnitario) ? 0 : costoTotalUnitario,
                costoTotal: isNaN(costoTotalItem) ? 0 : costoTotalItem,
                precioVenta20MG: isNaN(precioVenta20MG) ? 0 : precioVenta20MG,
                precioVentaTotal: isNaN(precioVentaTotal) ? 0 : precioVentaTotal,
                mgSobreCosto: (costoTotalUnitario > 0) ? (pVentaNeto - costoTotalUnitario) / costoTotalUnitario : 0,
                mgSobreVenta: (pVentaNeto > 0) ? (pVentaNeto - costoTotalUnitario) / pVentaNeto : 0,
                mgSobreVentaPesos: pVentaNeto - costoTotalUnitario
            };
        });

        const totalNeto = itemsConCostos.reduce((acc, item) => acc + (item.precioVentaTotal || 0), 0);
        const totalCostoGeneral = itemsConCostos.reduce((acc, item) => acc + (item.costoTotal || 0), 0);
        const margenPesos = totalNeto - totalCostoGeneral;
        const margenPorc = totalNeto > 0 ? (margenPesos / totalNeto) * 100 : 0;

        return {
            itemsConCostos,
            totalTT,
            totalPC,
            totalNeto,
            subtotalVenta: totalNeto,
            totalCostoGeneral,
            margenPesos,
            margenPorc: isNaN(margenPorc) ? "0.0" : margenPorc.toFixed(1),
            prorrateoLineal,
            costoPersonalTT,
            costoMovilizacionTT,
            costoRecintosTT: isNaN(costoRecintosTT) ? 0 : costoRecintosTT,
            totalOtrosCostos,
            itemsCintaCalculados
        };
    }, [items, otrosCostos]);

    const resetState = useCallback(() => {
        setSolicitud({ clienteNombre: '', clienteId: null, vendedorId: null });
        setItems([{ ...DEFAULT_ITEM, id: Math.random(), producto: "Producto Nuevo" }]);
        setOtrosCostos(JSON.parse(JSON.stringify(DEFAULT_OTROS_COSTOS)));
        setVinculados({ scos: [], scot: [] });
        setSelectedSCOSIds(new Set());
        setSelectedSCOTIds(new Set());
        setEvalData({
            referencia: '',
            condiciones: {
                anticipo: 50,
                saldo: 50,
                flete: "Cliente",
                garantia: "30 días de corrido",
                plazoEntrega: "",
                formaPago: '30 Días',
                lugarEntrega: 'Instalaciones Cliente',
                validezOferta: '15 Días'
            }
        });
        localStorage.removeItem('currentSolicitudCostos');
        localStorage.removeItem('currentCosteo');
    }, []);

    const handleUpdateItem = useCallback((id, field, value) => {
        const fieldLabels = {
            cant: 'Cantidad',
            precioVentaNeto: 'Precio Venta Neto',
            costoProducto: 'Costo Producto',
            costoLogo: 'Costo Logo',
            costoOrdenTrabajo: 'Costo OT',
            costosGenerales: 'Costos Generales'
        };

        if (fieldLabels[field]) {
            const error = validateNumericInput(value, fieldLabels[field]);
            if (error) {
                toast.error(error);
                return;
            }
        }
        setItems(prev => prev.map(item => item.id === id ? { ...item, [field]: (typeof value === 'string' && field !== 'tipo' && field !== 'producto' && field !== 'modelo' && field !== 'tela' && field !== 'composicion' && field !== 'genero' && field !== 'codigoInterno' && field !== 'codigoProveedor' && field !== 'proveedor') ? (parseFloat(value) || 0) : value } : item));
    }, []);

    /**
     * Vincula un costeo a un ítem (tipo OP) y auto-rellena modelo/tela/composición/género
     * desde el resumen del costeo. Si idCosteo es null, desvincula.
     */
    const applyCosteoToItem = useCallback(async (itemId, idCosteo) => {
        if (!idCosteo) {
            setItems(prev => prev.map(it => it.id === itemId
                ? { ...it, costeoId: null, solicitudCostosId: null }
                : it));
            return;
        }
        try {
            const resumen = await getCosteoResumenEVN(idCosteo);
            setItems(prev => prev.map(it => {
                if (it.id !== itemId) return it;
                return {
                    ...it,
                    costeoId: idCosteo,
                    solicitudCostosId: resumen?.solicitudCostosId ?? null,
                    modelo: resumen?.modelo || it.modelo,
                    tela: resumen?.tela || it.tela,
                    composicion: resumen?.composicion || it.composicion,
                    genero: resumen?.genero || it.genero,
                    producto: it.producto || resumen?.modelo || '',
                    codigoInterno: resumen?.numeroCosteo || it.codigoInterno,
                };
            }));
            toast.success(`Costeo ${resumen?.numeroCosteo || idCosteo} vinculado al ítem`);
        } catch (e) {
            console.error("Error vinculando costeo al ítem:", e);
            toast.error("No se pudo vincular el costeo");
        }
    }, [getCosteoResumenEVN]);

    const applySCOSQuotation = useCallback((doc, quote) => {
        setSolicitud(prev => ({
            ...prev,
            clienteId: doc.clienteId,
            vendedorId: doc.vendedorId,
            clienteNombre: doc.clienteNombre
        }));

        const totalCosto = quote ? (
            (quote.costoHilos || 0) +
            (quote.costoManoObra || 0) +
            (quote.costoEtiquetas || 0) +
            (quote.costoEmbalaje || 0) +
            (quote.costoFlete || 0) +
            (quote.costoTotalMateriaPrima || 0)
        ) : (doc.costoTotal || 0);

        const newItem = {
            ...DEFAULT_ITEM,
            id: Date.now() + Math.random(),
            numero: items.length + 1,
            producto: doc.articuloDescripcion || doc.nombrePrenda || "Producto SCOS",
            cant: doc.cantidad || 0,
            tipo: 'SC',
            codigoInterno: doc.numero || "",
            costoProducto: totalCosto,
            costoTotalUnitario: totalCosto,
            costoTotal: totalCosto * (doc.cantidad || 0)
        };

        setItems(prev => [...prev.filter(i => i.producto !== ""), newItem]);
        setVinculados(prev => ({
            ...prev,
            scos: [...prev.scos.filter(v => v.id !== doc.id), { id: doc.id, numero: doc.numero }]
        }));
        setShowQuotationModal(false);
        toast.success(`SCOS ${doc.numero} vinculada con éxito (${quote?.numeroCosteo || 'Datos base'})`);
    }, [items.length]);

    const handleSingleSCOSLink = useCallback(async (doc) => {
        try {
            const quotes = await getAllCosteosBySCOS(doc.id);
            if (quotes && quotes.length > 1) {
                setAvailableQuotations(quotes);
                setPendingSCOS(doc);
                setShowQuotationModal(true);
            } else if (quotes && quotes.length === 1) {
                applySCOSQuotation(doc, quotes[0]);
            } else {
                applySCOSQuotation(doc, null);
            }
        } catch (error) {
            console.error("Error fetching quotes:", error);
            applySCOSQuotation(doc, null);
        }
    }, [getAllCosteosBySCOS, applySCOSQuotation]);

    const handleBulkLink = useCallback((type) => {
        const selectedIds = type === 'SCOS' ? selectedSCOSIds : selectedSCOTIds;
        const selectedDocs = solicitudesCostos.filter(s => selectedIds.has(s.id));
        if (selectedDocs.length === 0) return;

        if (type === 'SCOS' && selectedDocs.length === 1) {
            handleSingleSCOSLink(selectedDocs[0]);
            return;
        }

        if (selectedDocs.length > 0) {
            setSolicitud(prev => ({
                ...prev,
                clienteId: selectedDocs[0].clienteId,
                vendedorId: selectedDocs[0].vendedorId,
                clienteNombre: selectedDocs[0].clienteNombre
            }));
        }

        const newItems = selectedDocs.map((doc, index) => {
            const costoPersistido = doc.costoTotal || 0;
            const totalMateriales = costoPersistido > 0 ? costoPersistido : (
                (doc.telas || []).reduce((sum, t) => sum + (t.precioTotal || t.costoTotal || ((t.precioUnitario || 0) * (t.consumo || 0))), 0) +
                (doc.accesorios || []).reduce((sum, a) => sum + (a.precioTotal || a.costoTotal || ((a.precioUnitario || 0) * (a.consumo || a.cantidad || 0))), 0)
            );
            const totalLogo = (doc.logotipos || []).reduce((sum, l) => sum + ((l.precio || 0) * (l.cantidad || 1)), 0);

            return {
                ...DEFAULT_ITEM,
                id: Date.now() + index + Math.random(),
                numero: items.length + index + 1,
                producto: doc.articuloDescripcion || "",
                cant: doc.cantidad || 0,
                tipo: type === 'SCOS' ? 'SC' : 'SCI',
                codigoInterno: doc.numero || "",
                proveedor: doc.clienteNombre || "",
                tela: doc.telas?.[0]?.descripcion || "",
                composicion: doc.telas?.[0]?.composicion || "",
                costoProducto: totalMateriales,
                costoLogo: totalLogo,
                costoOrdenTrabajo: doc.costoFijo?.total || 0,
            };
        });

        setItems(prev => [...prev.filter(i => i.descripcion !== "Producto Nuevo" && i.producto !== ""), ...newItems]);

        const newVinculados = { ...vinculados };
        selectedDocs.forEach(d => {
            const docData = { id: d.id, numero: d.numero };
            if (type === 'SCOS') {
                if (!newVinculados.scos.some(v => v.id === docData.id)) newVinculados.scos.push(docData);
            } else {
                if (!newVinculados.scot.some(v => v.id === docData.id)) newVinculados.scot.push(docData);
            }
        });
        setVinculados(newVinculados);

        if (type === 'SCOS') {
            setSelectedSCOSIds(new Set());
        } else {
            setSelectedSCOTIds(new Set());
        }
        toast.success(`Vinculados ${selectedDocs.length} documentos correctamente`);
    }, [items.length, solicitudesCostos, selectedSCOSIds, selectedSCOTIds, handleSingleSCOSLink, vinculados]);

    const toggleDocSelection = useCallback((id, type) => {
        const setSetter = type === 'SCOS' ? setSelectedSCOSIds : setSelectedSCOTIds;
        setSetter(prev => {
            const next = new Set(prev);
            if (next.has(id)) next.delete(id);
            else next.add(id);
            return next;
        });
    }, []);

    const handleGenerarPropuesta = async () => {
        const hasCliente = solicitud.clienteId || initialEval?.clienteId || solicitud.clienteNombre || initialEval?.clienteNombre;

        if (!hasCliente) {
            toast.error("Debe seleccionar un cliente para generar la propuesta.");
            return;
        }

        setIsSaving(true);
        try {
            const payload = {
                // numero: Math.floor(10000 + Math.random() * 90000), // Eliminado: El backend genera el correlativo real
                clienteId: parseId(solicitud.clienteId || initialEval?.clienteId),
                vendedorId: parseId(solicitud.vendedorId || initialEval?.vendedorId) || user?.id || 1,
                clienteNombre: solicitud.clienteNombre || initialEval?.clienteNombre || '',
                referencia: evalData.referencia || '',
                estado: 'BORRADOR',
                porcentajeComision: otrosCostos.porcentajeComision || 0,
                garantiaSeriedad: otrosCostos.garantiaSeriedad || 0,
                garantiaFielCumplimiento: otrosCostos.garantiaFielCumplimiento || 0,
                flete: otrosCostos.flete || 0,
                certificacion: otrosCostos.certificacion || 0,
                muestras: otrosCostos.muestras || 0,
                entregaPersonalizada: otrosCostos.entregaPersonalizada || 0,

                // Campos aplanados según CrearEVNCommand.java
                tomaTallajeMonto: totals.totalTT || 0,
                tomaTallajeObservaciones: otrosCostos.tomaTallaje.observaciones || '',
                tomaTallajeMetadata: JSON.stringify(otrosCostos.tomaTallaje),

                pegadoCinta: (otrosCostos.pegadoCinta || []).reduce((acc, c) => acc + (c.total || 0), 0),
                pegadoCintaMetadata: JSON.stringify(otrosCostos.pegadoCinta),

                items: (items || []).map((item, idx) => ({
                    nroItem: idx + 1,
                    productoId: parseId(item.productoId),
                    proveedorId: parseId(item.proveedorId),
                    costeoId: parseId(item.costeoId),
                    solicitudCostosId: parseId(item.solicitudCostosId),
                    descripcion: item.producto || item.descripcion || '',
                    modelo: item.modelo || '',
                    tela: item.tela || '',
                    composicion: item.composicion || '',
                    genero: item.genero || '',
                    codigoInterno: item.codigoInterno || '',
                    codigoProveedor: item.codigoProveedor || '',
                    proveedorNombre: item.proveedor || '',
                    technicalSpecs: {
                        descripcion: item.producto || item.descripcion || '',
                        modelo: item.modelo || '',
                        tela: item.tela || '',
                        composicion: item.composicion || '',
                        genero: item.genero || '',
                        codigoInterno: item.codigoInterno || '',
                        codigoProveedor: item.codigoProveedor || '',
                        proveedor: item.proveedor || ''
                    },
                    cantidad: item.cant || 1,
                    precioUnitario: item.precioVentaNeto || 0,
                    costoUnitario: item.costoProducto || 0,
                    costoProducto: item.costoProducto || 0,
                    costoLogo: item.costoLogo || 0,
                    costoOrdenTrabajo: item.costoOrdenTrabajo || 0,
                    tipoItem: item.tipo || 'SC',
                })),
            };
            await EvaluacionNegocioService.save(payload);
            resetState();
            toast.success("Propuesta generada correctamente");
            return true;
        } catch (error) {
            console.error("Error al guardar la propuesta:", error);
            const msg = error.response?.data?.message || error.message;
            toast.error(`Error al guardar: ${msg}`);
            return false;
        } finally {
            setIsSaving(false);
        }
    };

    const handleAdjudicar = async (ev) => {
        if (!window.confirm(`¿Adjudicar la EVN "${ev.referencia || ev.evaluacionNegocioId}"? Esta acción no se puede deshacer.`)) return;
        try {
            await EvaluacionNegocioService.adjudicar(ev.evaluacionNegocioId || ev.id);
            toast.success("EVN adjudicada con éxito");
            return true;
        } catch (error) {
            toast.error('Error al adjudicar: ' + (error.response?.data?.message || error.message));
            return false;
        }
    };

    return {
        // State
        items, setItems,
        otrosCostos, setOtrosCostos,
        solicitud, setSolicitud,
        evalData, setEvalData,
        vinculados, setVinculados,
        selectedSCOSIds, setSelectedSCOSIds,
        selectedSCOTIds, setSelectedSCOTIds,
        isSaving,
        showQuotationModal, setShowQuotationModal,
        availableQuotations,
        pendingSCOS,
        totals,
        costeos,

        // Handlers
        handleUpdateItem,
        applyCosteoToItem,
        handleBulkLink,
        handleSingleSCOSLink,
        applySCOSQuotation,
        toggleDocSelection,
        resetState,
        handleGenerarPropuesta,
        handleAdjudicar,

        // Context data needed
        proveedores,
        clientes,
        vendedores,
        solicitudesCostos,
        loadSolicitudesCostos
    };
};
