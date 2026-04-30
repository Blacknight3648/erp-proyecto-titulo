export const calculateSCStatus = (sc) => {
    if (!sc) return { estadoGlobal: 'FLUJO_NORMAL', etapaCritica: null, diasAtraso: 0, motivo: '' };

    const hoy = new Date();
    const fechaCreacion = new Date(sc.fechaEmision || sc.fechaCreacion || sc.fecha_creacion);

    // 1. Regla del 3: Más de 3 días sin emitir OC desde la creación
    const diffCreacion = Math.floor((hoy - fechaCreacion) / (1000 * 60 * 60 * 24));
    const limiteSinOC = sc.diasMaxSinOC || sc.dias_max_sin_oc || 3;

    if (!(sc.fechaOcEmitida || sc.fecha_oc_emitida) && diffCreacion > limiteSinOC) {
        return {
            estadoGlobal: 'ATRASADA',
            etapaCritica: 'REQ.',
            diasAtraso: diffCreacion,
            motivo: 'Falta emisión de OC (> 3 días)'
        };
    }

    // 2. Atraso en Recepción
    if ((sc.fechaOcEmitida || sc.fecha_oc_emitida) && !(sc.fechaRecibido || sc.fecha_recibido)) {
        const fechaOC = new Date(sc.fechaOcEmitida || sc.fecha_oc_emitida);
        const diffOC = Math.floor((hoy - fechaOC) / (1000 * 60 * 60 * 24));
        const limiteRecepcion = sc.diasMaxEnRecepcion || sc.dias_max_en_recepcion || 5;

        if (diffOC > limiteRecepcion) {
            return {
                estadoGlobal: 'ATRASADA',
                etapaCritica: 'EN RECEP.',
                diasAtraso: diffOC,
                motivo: `Retraso en entrega de proveedor (SLA: ${limiteRecepcion}d)`
            };
        }
    }

    return {
        estadoGlobal: 'FLUJO_NORMAL',
        etapaCritica: null,
        diasAtraso: 0,
        motivo: ''
    };
};

export const calculateOPStatus = (op, ots = [], ocs = [], personalizaciones = []) => {
    if (!op) return { estadoGlobal: 'FLUJO_NORMAL', etapaCritica: null, diasAtraso: 0, motivo: '' };

    const hoy = new Date();
    const sla = op.sla || { corte: 2, logo: 2, taller: 5, term: 2, personalizado: 3, entrega: 1 };

    // 1. Verificación de OTs (Orden Técnica / Servicio Externo)
    const otRelacionada = ots.find(ot => (ot.ordenProduccionId || ot.op_id) === op.id);
    if (otRelacionada && otRelacionada.estado === 'Atrasado') {
        const fechaPromesa = new Date(otRelacionada.fechaPromesa || otRelacionada.fecha_promesa);
        const diffOT = Math.floor((hoy - fechaPromesa) / (1000 * 60 * 60 * 24));
        return {
            estadoGlobal: 'ATRASADA',
            etapaCritica: op.estado === 'Logo' ? 'BORDADO/LOGO' : 'TALLER EXT.',
            diasAtraso: diffOT > 0 ? diffOT : 1,
            motivo: `Retraso en Taller Externo (${otRelacionada.proveedor})`
        };
    }

    // 2. Verificación de OCs (Compra de Materia Prima)
    const ocRelacionada = ocs.find(oc => (oc.ordenProduccionId || oc.op_id) === op.id && oc.estado === 'Pendiente');
    if (ocRelacionada) {
        // Regla: Si la OC tiene más de 5 días (SLA estándar de recepción)
        const fechaStr = ocRelacionada.fechaEmision || ocRelacionada.fecha;
        let fechaOC;
        if (fechaStr.includes('/')) {
            const [d, m, y] = fechaStr.split('/');
            fechaOC = new Date(`${y}-${m}-${d}`);
        } else {
            fechaOC = new Date(fechaStr);
        }
        const diffOC = Math.floor((hoy - fechaOC) / (1000 * 60 * 60 * 24));

        if (diffOC > 5) {
            return {
                estadoGlobal: 'ATRASADA',
                etapaCritica: 'EN RECEP.',
                diasAtraso: diffOC,
                motivo: `Retraso en Materia Prima (${ocRelacionada.proveedor})`
            };
        }
    }

    // 3. Verificación de Personalización
    if (op.tienePersonalizado || op.tiene_personalizado) {
        const persRelacionada = personalizaciones.find(p => (p.ordenProduccionId || p.op_id) === op.id);
        if (persRelacionada) {
            const itemsPendientes = persRelacionada.items.filter(i => i.estado !== 'Completado');
            if (itemsPendientes.length > 0 && op.estado === 'Personalizado') {
                const startDate = new Date(op.fechaTerminaciones || op.fechaTerminaciones || op.fechaTallerExt || op.fecha_taller_ext);
                const diff = Math.floor((hoy - startDate) / (1000 * 60 * 60 * 24));
                const slaPers = sla.personalizado || 3;

                if (diff > slaPers) {
                    return {
                        estadoGlobal: 'ATRASADA',
                        etapaCritica: 'PERSONALIZADO',
                        diasAtraso: diff,
                        motivo: `Pendiente: ${itemsPendientes.length} nombres por procesar`
                    };
                }
            }
        }
    }

    // 4. Mapeo de estados a sus fechas de inicio (fin de etapa anterior) y SLAs
    const logic = {
        'Corte': { start: op.fechaTizado || op.fecha_tizado, days: sla.corte },
        'Logo': { start: op.fechaCorte || op.fecha_corte, days: sla.logo },
        'Taller Externo': { start: op.fechaLogo || op.fecha_logo, days: sla.taller },
        'Terminaciones': { start: op.fechaTallerExt || op.fecha_taller_ext, days: sla.term },
        'Personalizado': { start: op.fechaTerminaciones || op.fecha_terminaciones, days: sla.personalizado || 3 },
        'Entrega': { start: op.fechaPersonalizado || op.fecha_personalizado || op.fechaTerminaciones || op.fecha_terminaciones, days: sla.entrega }
    };

    const currentStageLogic = logic[op.estado];

    if (currentStageLogic && currentStageLogic.start) {
        const startDate = new Date(currentStageLogic.start);
        const diff = Math.floor((hoy - startDate) / (1000 * 60 * 60 * 24));

        if (diff > currentStageLogic.days) {
            return {
                estadoGlobal: 'ATRASADA',
                etapaCritica: op.estado.toUpperCase(),
                diasAtraso: diff,
                motivo: `Excede SLA de ${op.estado}`
            };
        }
    }

    // Caso especial: Tizado recién iniciado
    if (op.estado === 'Tizado' && (op.fechaInicio || op.fecha_inicio)) {
        const startDate = new Date(op.fechaInicio || op.fecha_inicio);
        const diff = Math.floor((hoy - startDate) / (1000 * 60 * 60 * 24));
        if (diff > 1) { // 1 día para tizado
            return {
                estadoGlobal: 'ATRASADA',
                etapaCritica: 'TIZADO',
                diasAtraso: diff,
                motivo: 'Tizado pendiente'
            };
        }
    }

    return {
        estadoGlobal: 'FLUJO_NORMAL',
        etapaCritica: null,
        diasAtraso: 0,
        motivo: ''
    };
};
