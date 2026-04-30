import { useState, useMemo, useEffect, useCallback } from 'react';
import { toast } from 'sonner';
import { useComercial } from './useComercial';
import { useProduccion } from './useProduccion';
import { useClientes } from './useClientes';
import { useProveedores } from './useProveedores';
import { mockOperaciones } from '../data/mockData';
import { parseId } from '../utils/formUtils';

export function useCosteosOPState() {
    const [view, setView] = useState('list'); // 'list', 'form'
    const [activeTab, setActiveTab] = useState('materiales');
    const [selectedRecord, setSelectedRecord] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [statusFilter, setStatusFilter] = useState('Todos');
    const [showDashboard, setShowDashboard] = useState(false);
    const [showCompareModal, setShowCompareModal] = useState(false);
    const [showPlantilla, setShowPlantilla] = useState(false);
    const [showTelasSCOS, setShowTelasSCOS] = useState(false);
    const [showAccesoriosSCOS, setShowAccesoriosSCOS] = useState(false);

    const [moPrenda, setMoPrenda] = useState(0);
    const [moCinta, setMoCinta] = useState(0);
    const [moCosturaSellada, setMoCosturaSellada] = useState(0);
    const [moAcolchado, setMoAcolchado] = useState(0);

    // Costos Fijos state
    const [costoHilo, setCostoHilo] = useState(0);
    const [costoMoPropia, setCostoMoPropia] = useState(0);
    const [costoGratificacion, setCostoGratificacion] = useState(0);
    const [costoEtiqueta, setCostoEtiqueta] = useState(0);
    const [costoEmbalaje, setCostoEmbalaje] = useState(0);
    const [costoFlete, setCostoFlete] = useState(0);

    const { solicitudesCostos, loading: loadingComercial, loadSolicitudesCostos, updateSolicitudCostos } = useComercial();
    const { getCosteoBySCOS, saveCosteo, loading: loadingProduccion } = useProduccion();
    const { clientes, loading: loadingClientes } = useClientes();
    const { proveedores, loading: loadingProveedores } = useProveedores();

    const [currentSolicitud, setCurrentSolicitud] = useState(null);
    const [insumos, setInsumos] = useState([]);

    useEffect(() => {
        loadSolicitudesCostos();
    }, []);

    const isLoading = loadingComercial || loadingClientes || loadingProveedores || loadingProduccion;

    // Solo solicitudes de tipo COSTEO para esta vista
    const solicitudesFiltradas = useMemo(() => {
        return solicitudesCostos.filter(s =>
            s.tipo?.toUpperCase() === 'COSTEO' ||
            s.tipo?.toUpperCase() === 'SCOS'
        );
    }, [solicitudesCostos]);

    // Registros que ya tienen costeos realizados (Estado Costeado) o están listos para ser procesados
    const allRecords = useMemo(() => {
        return solicitudesFiltradas.filter(s =>
            s.estado === 'Costeado' ||
            s.estado === 'COSTEO REALIZADO' ||
            s.estado === 'PENDIENTE' ||
            s.estado === 'PENDIENTE PRODUCCIÓN'
        );
    }, [solicitudesFiltradas]);

    const dashboardStats = useMemo(() => {
        const finishedItems = solicitudesFiltradas.filter(r => r.estado?.toUpperCase() === 'COSTEADO' || r.estado === 'COSTEO REALIZADO');
        const inProgressItems = solicitudesFiltradas.filter(r => r.estado === 'En Proceso' || r.estado === 'Borrador');
        const pendingItems = solicitudesFiltradas.filter(s => s.estado === 'PENDIENTE PRODUCCIÓN' || s.estado === 'PENDIENTE');

        return {
            pending: pendingItems.length,
            pendingItems,
            inProgress: inProgressItems.length,
            inProgressItems,
            finished: finishedItems.length,
            finishedItems
        };
    }, [solicitudesFiltradas]);

    const filteredRecords = useMemo(() => {
        return allRecords.filter(r => {
            const cliente = clientes.find(c => (c.clienteId || c.id)?.toString() === r.clienteId?.toString());
            const clienteNombre = r.clienteNombre || cliente?.nombreCliente || cliente?.nombre || 'Cliente Desconocido';

            const matchesSearch = clienteNombre.toUpperCase().includes(searchTerm.toUpperCase()) ||
                (r.id?.toString() || '').toUpperCase().includes(searchTerm.toUpperCase()) ||
                (r.numero?.toString() || '').toUpperCase().includes(searchTerm.toUpperCase());

            const matchesStatus = statusFilter === 'Todos' || r.estado === statusFilter;

            return matchesSearch && matchesStatus;
        });
    }, [allRecords, clientes, searchTerm, statusFilter]);

    const totalMateriales = useMemo(() => {
        return insumos.reduce((acc, current) => acc + (current.costo * current.cantidad), 0);
    }, [insumos]);

    const totalMO = useMemo(() => {
        return Number(moPrenda) + Number(moCinta) + Number(moCosturaSellada) + Number(moAcolchado);
    }, [moPrenda, moCinta, moCosturaSellada, moAcolchado]);

    const totalCostosFijos = useMemo(() => {
        return Number(costoHilo) + Number(costoMoPropia) + Number(costoGratificacion) +
            Number(costoEtiqueta) + Number(costoEmbalaje) + Number(costoFlete);
    }, [costoHilo, costoMoPropia, costoGratificacion, costoEtiqueta, costoEmbalaje, costoFlete]);

    const totalGeneral = useMemo(() => totalMateriales + totalMO + totalCostosFijos, [totalMateriales, totalMO, totalCostosFijos]);

    const handleOpenForm = async (record) => {
        if (!record) return;

        setSelectedRecord(record);
        setCurrentSolicitud(record);

        let savedCosteo = null;
        try {
            savedCosteo = await getCosteoBySCOS(record.id);
            if (savedCosteo) {
                setCostoHilo(savedCosteo.costoHilos || 0);
                setCostoMoPropia(savedCosteo.costoManoObra || 0);
                setCostoGratificacion(0);
                setCostoEtiqueta(savedCosteo.costoEtiquetas || 0);
                setCostoEmbalaje(savedCosteo.costoEmbalaje || 0);
                setCostoFlete(savedCosteo.costoFlete || 0);
            } else {
                setCostoHilo(0);
                setCostoMoPropia(0);
                setCostoGratificacion(0);
                setCostoEtiqueta(0);
                setCostoEmbalaje(0);
                setCostoFlete(0);
            }
        } catch (err) {
            console.error("Error cargando costeo de producción:", err);
        }

        const initialInsumos = [];
        const sourceLogos = record.logotipos?.length > 0 ? record.logotipos : (record.plantillas?.[0]?.logotipos || []);
        sourceLogos.forEach((l, idx) => {
            initialInsumos.push({
                id: l.id || `LOGO-${idx}`,
                producto: l.nombre || l.tipo || 'Logotipo',
                costo: l.precio || 0,
                cantidad: l.cantidad || 0,
                unidad: 'un',
                categoryId: 'logotipo',
                tipo: l.tipo,
                ubicacion: l.ubicacion,
                color: l.color
            });
        });

        if (savedCosteo && savedCosteo.items && savedCosteo.items.length > 0) {
            savedCosteo.items.forEach(savedItem => {
                const matchIndex = initialInsumos.findIndex(i =>
                    i.producto === savedItem.nombreInsumo &&
                    i.categoryId === savedItem.tipoInsumo?.toUpperCase()
                );

                if (matchIndex !== -1) {
                    initialInsumos[matchIndex].costo = savedItem.precioUnitario || 0;
                    if (savedItem.consumo) initialInsumos[matchIndex].cantidad = savedItem.consumo;
                    // Preservar ID de la base de datos
                    initialInsumos[matchIndex].idCosteoItem = savedItem.idCosteoItem;
                    initialInsumos[matchIndex].insumoId = savedItem.insumoId;
                } else {
                    initialInsumos.push({
                        id: `EXTRA-${savedItem.idCosteoItem}`,
                        idCosteoItem: savedItem.idCosteoItem,
                        insumoId: savedItem.insumoId,
                        producto: savedItem.nombreInsumo,
                        costo: savedItem.precioUnitario || 0,
                        cantidad: savedItem.consumo || 0,
                        unidad: 'un',
                        categoryId: savedItem.tipoInsumo?.toLowerCase() || 'insumos'
                    });
                }
            });
        }

        setInsumos(initialInsumos);
        setView('form');
        setActiveTab('materiales');
    };

    const handleUpdateItem = (id, field, value) => {
        setInsumos(insumos.map(item =>
            item.id === id ? { ...item, [field]: value } : item
        ));
    };

    const handleRemoveItem = (id) => {
        setInsumos(insumos.filter(item => item.id !== id));
    };

    const handleAddItem = (cat = 'telas') => {
        const nextId = insumos.length > 0 ? Math.max(...insumos.map(i => {
            if (typeof i.id === 'number') return i.id;
            const num = parseInt(i.id?.toString().replace('REQ-', '').replace('NEW-', ''));
            return isNaN(num) ? 0 : num;
        })) + 1 : 1;

        setInsumos([...insumos, {
            id: `NEW-${nextId}`,
            producto: "Nuevo Insumo",
            costo: 0,
            unidad: "m",
            cantidad: 0,
            categoryId: cat
        }]);
    };

    const handleValidateCostos = async () => {
        try {
            const updatedTelas = insumos.filter(i => i.categoryId === 'telas').map(i => ({
                id: (typeof i.id === 'string') ? null : i.id,
                tempId: (typeof i.id === 'string') ? i.id : null,
                nombre: i.producto,
                unidadMedida: i.unidad,
                consumo: parseFloat(i.cantidad) || 0,
                precioUnitario: 0,
                color: i.color,
                composicion: i.composicion
            }));

            const updatedAccesorios = insumos.filter(i => i.categoryId === 'accesorios').map(i => ({
                id: (typeof i.id === 'string') ? null : i.id,
                tempId: (typeof i.id === 'string') ? i.id : null,
                tipo: i.producto,
                nombreAccesorio: i.producto,
                unidadMedida: i.unidad,
                consumo: parseFloat(i.cantidad) || 0,
                precioUnitario: 0
            }));

            const updatedLogotipos = insumos.filter(i => i.categoryId === 'logotipo').map(i => ({
                id: (typeof i.id === 'string') ? null : i.id,
                nombre: i.producto,
                cantidad: parseInt(i.cantidad) || 0,
                precio: 0,
                tipo: i.tipo || 'Bordado',
                ubicacion: i.ubicacion || 'Pecho',
                color: i.color || 'Unificado',
                tamanio: parseFloat(i.tamanio || i.tamano) || 0
            }));

            const updatedCintas = insumos.filter(i => i.categoryId === 'cintas').map(i => ({
                id: (typeof i.id === 'string') ? null : i.id,
                tipo: i.producto,
                cantidad: parseInt(i.cantidad) || 0,
                medida: parseFloat(i.medida) || 0,
                marca: i.marca || 'N/A'
            }));

            const productionItems = insumos.map(i => ({
                idCosteoItem: i.idCosteoItem || null,
                tipoInsumo: i.categoryId.toUpperCase(),
                insumoId: i.insumoId || (typeof i.id === 'string' ? null : i.id),
                nombreInsumo: i.producto,
                consumo: parseFloat(i.cantidad) || 0,
                precioUnitario: parseFloat(i.costo) || 0,
                costoTotal: (parseFloat(i.cantidad) || 0) * (parseFloat(i.costo) || 0)
            }));

            const commercialPayload = {
                id: currentSolicitud.id,
                clienteId: parseId(currentSolicitud.clienteId),
                vendedorId: parseId(currentSolicitud.vendedorId),
                articuloDescripcion: currentSolicitud.articuloDescripcion,
                nombrePrenda: currentSolicitud.nombrePrenda,
                cantidad: parseInt(currentSolicitud.cantidad),
                genero: currentSolicitud.genero,
                tallaje: currentSolicitud.tallaje,
                tipo: currentSolicitud.tipo || 'COSTEO',
                esMuestra: currentSolicitud.esMuestra || false,
                hasLogo: currentSolicitud.hasLogo || false,
                costoTotal: totalGeneral,
                telas: updatedTelas,
                accesorios: updatedAccesorios,
                logotipos: updatedLogotipos,
                cintas: updatedCintas,
                plantillas: currentSolicitud.plantillas || [],
                prendas: currentSolicitud.prendas || [],
                estado: 'Costeado'
            };

            const productionPayload = {
                solicitudCostosId: parseId(currentSolicitud.id),
                numeroCosteo: `C-${(currentSolicitud.numero || currentSolicitud.id).toString().slice(-10)}-${Date.now().toString().slice(-4)}`,
                costoHilos: costoHilo,
                costoManoObra: totalMO + costoMoPropia + costoGratificacion,
                costoEtiquetas: costoEtiqueta,
                costoEmbalaje: costoEmbalaje,
                costoFlete: costoFlete,
                porcentajeCostoFijo: (totalGeneral > 0 && totalCostosFijos > 0) ? parseFloat((totalCostosFijos / totalGeneral * 100).toFixed(2)) : 0,
                costoTotalMateriaPrima: totalMateriales || 0,
                precioVentaSugerido: (totalGeneral || 0) * 1.35,
                margenBrutoSugerido: 35,
                items: productionItems
            };

            await Promise.all([
                updateSolicitudCostos(currentSolicitud.id, commercialPayload),
                saveCosteo(productionPayload)
            ]);

            const scosNumber = currentSolicitud.numero || currentSolicitud.id;
            const match = String(scosNumber).match(/(\d+)/);
            if (match) {
                const numericId = match[1];
                const newOpId = `OP-${numericId}`;
                const existingOpIndex = mockOperaciones.findIndex(op => op.idOP === newOpId);
                const cliente = currentSolicitud.clienteNombre ||
                    clientes.find(c => (c.clienteId || c.id)?.toString() === currentSolicitud.clienteId?.toString())?.nombreCliente ||
                    'Cliente SCOS';

                if (existingOpIndex >= 0) {
                    mockOperaciones[existingOpIndex].estado = 'En Proceso';
                    mockOperaciones[existingOpIndex].progreso = Math.max(20, mockOperaciones[existingOpIndex].progreso);
                } else {
                    mockOperaciones.unshift({
                        idOP: newOpId,
                        notaVentaId: 'S/N',
                        cliente: cliente,
                        producto: currentSolicitud.articuloDescripcion || 'Sin Descripción',
                        estado: 'En Proceso',
                        progreso: 20,
                        prioridad: 'Media',
                        fechaInicio: new Date().toISOString().split('T')[0],
                        sla: { corte: 2, logo: 2, taller: 5, term: 2, entrega: 1 },
                        tienePersonalizado: false
                    });
                }
            }

            toast.success("Costeo validado y guardado correctamente");
            loadSolicitudesCostos();
            setView('list');
        } catch (error) {
            console.error("Error al validar costos:", error);
            toast.error("Error al persistir los cambios en el backend");
        }
    };

    return {
        view, setView,
        activeTab, setActiveTab,
        selectedRecord, setSelectedRecord,
        searchTerm, setSearchTerm,
        statusFilter, setStatusFilter,
        showDashboard, setShowDashboard,
        showCompareModal, setShowCompareModal,
        showPlantilla, setShowPlantilla,
        showTelasSCOS, setShowTelasSCOS,
        showAccesoriosSCOS, setShowAccesoriosSCOS,
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
        isLoading,
        dashboardStats,
        filteredRecords,
        totalMateriales,
        totalMO,
        totalCostosFijos,
        totalGeneral,
        currentSolicitud,
        insumos,
        handleOpenForm,
        handleUpdateItem,
        handleRemoveItem,
        handleAddItem,
        handleValidateCostos,
        clientes,
        proveedores
    };
}
