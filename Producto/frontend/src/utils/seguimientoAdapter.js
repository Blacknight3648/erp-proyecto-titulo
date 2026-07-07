// Adaptador compartido entre pantallas de Producción (OpRegistro, TableroOP, ...)
// para traducir SeguimientoOPDTO (backend) hacia/desde la forma que usa la UI.

// Mapeo de field.key (frontend) → nombre del campo en ActualizarSeguimientoCommand (backend)
export const FIELD_KEY_TO_BACKEND = {
    recepcionOP:    'fechaRecepcionOp',
    finTizado:      'finTizado',
    recepcionCompra: 'recepcionCompras',
    inicioCorte:    'inicioCorte',
    finCorte:       'finCorte',
    inicioLogo:     'inicioLogo',
    regresoLogo:    'regresoLogo',
    estadoRecLogo:  'estadoRecLogo',
    finTaller:      'finTallerExterno',
    calidadTaller:  'calidadTaller',
    obsTaller:      'obsTaller',
    finPersonalizado: 'finPersonalizado',
    finOP:          'finTerminacion',
    estadoOcMP:     'estadoOcMp',
    inicioTaller:   'inicioTallerExterno',
    // cantidadCortes, entregaBodega — sin equivalente en backend, se omiten
};

// Campos de ActualizarSeguimientoCommand que el backend espera como LocalDate (ISO yyyy-MM-dd).
export const DATE_FIELDS = new Set([
    'fechaRecepcionOp', 'finTizado', 'recepcionCompras', 'inicioCorte', 'finCorte',
    'inicioLogo', 'regresoLogo', 'inicioTallerExterno', 'finTallerExterno',
    'finTerminacion', 'finPersonalizado'
]);

// Normaliza cualquier formato de fecha común (ISO yyyy-MM-dd, DD-MM-YYYY, DD/MM/YYYY)
// al formato ISO que espera el backend (java.time.LocalDate). El <input type="date">
// nativo siempre entrega ISO, pero se normaliza igual por robustez ante datos que
// hayan quedado guardados o pre-cargados en otro formato.
export const normalizeDateToISO = (value) => {
    if (!value) return null;
    const iso = /^\d{4}-\d{2}-\d{2}$/;
    if (iso.test(value)) return value;
    const dmy = /^(\d{1,2})[/-](\d{1,2})[/-](\d{4})$/;
    const match = dmy.exec(value);
    if (match) {
        const [, day, month, year] = match;
        return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;
    }
    return value;
};

export const mapEnumToBackend = (key, value) => {
    if (!value) return null;
    if (key === 'estadoRecLogo') {
        if (value === 'Recep. completa') return 'RECEPCION_COMPLETA';
        if (value === 'Recep. parcial') return 'RECEPCION_PARCIAL';
        if (value === 'N/A') return 'NA';
    }
    if (key === 'calidadTaller') {
        if (value === 'Aprobado') return 'APROBADO';
        if (value === 'Rechazado') return 'RECHAZADO';
        if (value === 'con Obs.') return 'CON_OBSERVACIONES';
    }
    if (key === 'estadoOcMP') {
        if (value === 'sin tela en mercado') return 'SIN_TELA_EN_MERCADO';
        if (value === 'OC emitida') return 'OC_EMITIDA';
        if (value === 'tela en stock') return 'TELA_EN_STOCK';
        if (value === 'en stock y OC emitida') return 'EN_STOCK_Y_OC_EMITIDA';
        if (value === 'N/A') return 'NA';
    }
    return value;
};

export const mapBackendToEnum = (key, value) => {
    if (!value) return null;
    if (key === 'estadoRecLogo') {
        if (value === 'RECEPCION_COMPLETA') return 'Recep. completa';
        if (value === 'RECEPCION_PARCIAL') return 'Recep. parcial';
        if (value === 'NA') return 'N/A';
    }
    if (key === 'calidadTaller') {
        if (value === 'APROBADO') return 'Aprobado';
        if (value === 'RECHAZADO') return 'Rechazado';
        if (value === 'CON_OBSERVACIONES') return 'con Obs.';
    }
    if (key === 'estadoOcMP') {
        if (value === 'SIN_TELA_EN_MERCADO') return 'sin tela en mercado';
        if (value === 'OC_EMITIDA') return 'OC emitida';
        if (value === 'TELA_EN_STOCK') return 'tela en stock';
        if (value === 'EN_STOCK_Y_OC_EMITIDA') return 'en stock y OC emitida';
        if (value === 'NA') return 'N/A';
    }
    return value;
};

// Construye el payload completo para ActualizarSeguimientoCommand a partir del DTO actual
// aplicando el nuevo valor para el campo editado.
export const buildSeguimientoPayload = (seguimientoDTO, fieldKey, value) => {
    const backendKey = FIELD_KEY_TO_BACKEND[fieldKey];
    const mappedValue = DATE_FIELDS.has(backendKey) ? normalizeDateToISO(value) : mapEnumToBackend(fieldKey, value);
    const base = {
        fechaRecepcionOp:  normalizeDateToISO(seguimientoDTO?.fechaRecepcionOp),
        finTizado:         normalizeDateToISO(seguimientoDTO?.finTizado),
        estadoOcMp:        seguimientoDTO?.estadoOcMp         ?? null,
        recepcionCompras:  normalizeDateToISO(seguimientoDTO?.recepcionCompras),
        inicioCorte:       normalizeDateToISO(seguimientoDTO?.inicioCorte),
        finCorte:          normalizeDateToISO(seguimientoDTO?.finCorte),
        inicioLogo:        normalizeDateToISO(seguimientoDTO?.inicioLogo),
        estadoIdaLogo:     seguimientoDTO?.estadoIdaLogo      ?? null,
        regresoLogo:       normalizeDateToISO(seguimientoDTO?.regresoLogo),
        estadoRecLogo:     seguimientoDTO?.estadoRecLogo      ?? null,
        inicioTallerExterno: normalizeDateToISO(seguimientoDTO?.inicioTallerExterno),
        finTallerExterno:  normalizeDateToISO(seguimientoDTO?.finTallerExterno),
        calidadTaller:     seguimientoDTO?.calidadTaller      ?? null,
        obsTaller:         seguimientoDTO?.obsTaller          ?? null,
        finTerminacion:    normalizeDateToISO(seguimientoDTO?.finTerminacion),
        finPersonalizado:  normalizeDateToISO(seguimientoDTO?.finPersonalizado),
    };
    if (backendKey) {
        base[backendKey] = mappedValue;
    }
    return base;
};

// Traduce un mapa { opId: SeguimientoOPDTO } a { opId: { <frontKey>: valorParaMostrar } },
// formateando fechas ISO a DD/MM/YYYY y enums backend a las etiquetas que usa la UI.
export const mapSeguimientoToDetails = (seguimientosPorOpId) => {
    const result = {};
    for (const [opId, dto] of Object.entries(seguimientosPorOpId || {})) {
        result[opId] = {};
        for (const [frontKey, backKey] of Object.entries(FIELD_KEY_TO_BACKEND)) {
            if (dto[backKey] !== undefined && dto[backKey] !== null) {
                let val = dto[backKey];
                if (typeof val === 'string' && val.match(/^\d{4}-\d{2}-\d{2}$/)) {
                    const [y, m, d] = val.split('-');
                    val = `${d}/${m}/${y}`;
                }
                result[opId][frontKey] = mapBackendToEnum(frontKey, val);
            }
        }
    }
    return result;
};
