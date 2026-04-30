export const mockDashboard = {
    atrasosTotales: 12,
    throughputDiario: 85,
    oeeFabrica: 78,
    ventasMensuales: [
        { mes: 'Ene', monto: 4500 },
        { mes: 'Feb', monto: 5200 },
        { mes: 'Mar', monto: 4800 },
        { mes: 'Abr', monto: 6100 },
    ],
    produccionTrend: [
        { dia: 'Lun', corte: 20, taller: 15 },
        { dia: 'Mar', corte: 25, taller: 18 },
        { dia: 'Mie', corte: 18, taller: 22 },
        { dia: 'Jue', corte: 30, taller: 25 },
        { dia: 'Vie', corte: 22, taller: 20 },
    ]
};

export const mockOperaciones = [
    {
        idOP: 'OP-2024-001',
        notaVentaId: 'NV-20584',
        cliente: 'PROCESOS SANITARIOS SPA',
        producto: 'Polera Pique',
        estado: 'Taller Externo',
        progreso: 65,
        prioridad: 'Alta',
        fechaInicio: '2026-02-05',
        fechaTizado: '2026-02-06',
        fechaCorte: '2026-02-08',
        fechaLogo: '2026-02-10',
        sla: { corte: 2, logo: 2, taller: 5, term: 2, entrega: 1 },
        tienePersonalizado: false
    },
    {
        idOP: 'OP-2024-002',
        notaVentaId: 'NV-20586',
        cliente: 'I MUNICIPALIDAD LO BARNECHEA',
        producto: 'Geologo Gabardina',
        estado: 'Entrega',
        progreso: 100,
        prioridad: 'Media',
        fechaInicio: '2026-02-01',
        fechaTizado: '2026-02-02',
        fechaCorte: '2026-02-04',
        fechaLogo: '2026-02-05',
        fechaTallerExt: '2026-02-10',
        fechaTerminaciones: '2026-02-12',
        fechaEntrega: '2026-02-13',
        sla: { corte: 2, logo: 2, taller: 5, term: 2, entrega: 1 },
        tienePersonalizado: false
    },
    {
        idOP: 'OP-2024-003',
        notaVentaId: 'NV-20585',
        cliente: 'CLINICA ALEMANA',
        producto: 'Delantal Medico',
        estado: 'Logo',
        progreso: 40,
        prioridad: 'Baja',
        fechaInicio: '2026-02-01',
        fechaTizado: '2026-02-02',
        fechaCorte: '2026-02-04',
        sla: { corte: 2, logo: 2, taller: 5, term: 2, entrega: 1 },
        tienePersonalizado: false
    },
    {
        idOP: 'OP-104',
        notaVentaId: 'NV-20584',
        cliente: 'PROCESOS SANITARIOS SPA',
        producto: 'Pantalón Cargo',
        estado: 'Personalizado',
        progreso: 85,
        prioridad: 'Alta',
        fechaInicio: '2026-01-20',
        fechaTizado: '2026-01-21',
        fechaCorte: '2026-01-25',
        fechaLogo: '2026-01-27',
        fechaTallerExt: '2026-02-05',
        fechaTerminaciones: '2026-02-08',
        sla: { corte: 2, logo: 2, taller: 5, term: 2, personalizado: 3, entrega: 1 },
        tienePersonalizado: true,
        opPersonalizadoId: 'PERS-104'
    },
];

export const mockComercial = [
    {
        idSC: 1,
        numeroSC: 'SC-101',
        notaVentaId: 'NV-20584',
        cliente: 'PROCESOS SANITARIOS SPA',
        clienteId: 1,
        fechaEmision: '2026-02-10',
        estado: 'Parcial',
        prioridad: 'Alta',
        fechaCompromiso: '2026-02-25',
        idOP: 'OP-2024-001',
        items: [
            { id: 'SCITEM-001', productoId: 1, descripcion: 'Tela Jersey 100% Algodón', cantidad: 300, unidad: 'mts', sku: 'TEL-JER-001' },
            { id: 'SCITEM-002', productoId: 2, descripcion: 'Hilo Poliéster 40/2 Azul', cantidad: 50, unidad: 'conos', sku: 'HIL-AZU-001' },
            { id: 'SCITEM-003', productoId: 3, descripcion: 'Botones 4 Ojales Marinos', cantidad: 1000, unidad: 'un', sku: 'ACC-BOT-001' }
        ],
        ocs: [
            { idOC: 1, numeroOC: 'OC-61034', proveedor: 'TEXTIL PACÍFICO', proveedorId: 1, estado: 'Pendiente', items: 'Hilo Poliéster...', montoTotal: 150000, fechaEmision: '2024-02-11' },
            { idOC: 2, numeroOC: 'OC-61035', proveedor: 'AVITEX LTDA', proveedorId: 2, estado: 'Recepcionada', items: 'Tela Jersey...', montoTotal: 850000, fechaEmision: '2024-02-12' }
        ]
    },
    {
        idSC: 2,
        numeroSC: 'SC-102',
        notaVentaId: 'NV-20585',
        cliente: 'I MUNICIPALIDAD LO BARNECHEA',
        clienteId: 2,
        fechaEmision: '2026-02-11',
        estado: 'Pendiente OC',
        prioridad: 'Media',
        fechaCompromiso: '2026-03-01',
        items: [
            { id: 'SCITEM-004', productoId: 4, descripcion: 'Tela Gabardina Verde', cantidad: 150, unidad: 'mts', sku: 'TEL-GAB-002' }
        ],
        ocs: []
    }
];

export const mockSolicitudesCostos = [
    {
        idSCOS: 1,
        numeroSCOS: "SCOS-2026-001",
        idSolicitud: "SCOS-2026-001", // Keep for legacy
        fecha: "2026-02-12",
        clienteId: 1,
        clienteNombre: "PROCESOS SANITARIOS SPA", // Added for convenience
        vendedorId: 101,
        articuloDescripcion: "Polera Piqué Institucional",
        esMuestra: false,
        cantidad: 500,
        costoFijo: {
            hilos: 150,
            manoObra: 1200,
            etiquetas: 80,
            embalaje: 120,
            flete: 200,
            porcentajeCostoFijo: 15
        },
        telas: [
            { id: "T1", productoId: 1, descripcion: "Piqué Algodón/Poly 65/35", proveedorId: 1, nombreProveedor: "TEXTIL PACÍFICO", precioUnitario: 5800, consumo: 0.8, unidadMedida: "mts", costoTotal: 4640 }
        ],
        accesorios: [
            { id: "A1", productoId: 2, descripcion: "Cuello Tejido Piqué", proveedorId: 2, nombreProveedor: "AVITEX LTDA", precioUnitario: 1200, consumo: 1, unidadMedida: "un", costoTotal: 1200 },
            { id: "A2", productoId: 3, descripcion: "Puños Tejidos (Par)", proveedorId: 2, nombreProveedor: "AVITEX LTDA", precioUnitario: 800, consumo: 1, unidadMedida: "un", costoTotal: 800 }
        ],
        logotipo: [
            { id: "L1", descripcion: "Bordado Pecho 8000 puntadas", proveedorId: null, nombreProveedor: "Taller Interno", precioUnitario: 450, consumo: 1, unidadMedida: "un", costoTotal: 450 }
        ]
    },
    {
        idSCOT: 1,
        numeroSCOT: "COT-7741",
        idSCOS: 2, // Keep for legacy link if needed
        idSolicitud: "COT-7741",
        fecha: "2026-02-18",
        fechaEmision: "2026-02-18",
        clienteId: 2,
        clienteNombre: "I MUNICIPALIDAD LO BARNECHEA",
        vendedorId: 102,
        tipo: "cotizacion",
        articuloDescripcion: "Cotización de Bordados y Prendas Listas",
        estado: "PENDIENTE ADQUISICIÓN",
        proveedorSugerido: "TEXTIL PACÍFICO",
        prendas: [
            { productoId: 5, nombre: "Polera Pique Blanca", cantidad: 100, talla: "XL", color: "Blanco", linkReferencia: "http://proveedor.com/pique-blanco", observaciones: "Prenda de alta calidad" }
        ],
        // Mantener telas para retrocompatibilidad inmediata en algunos componentes
        telas: [
            { id: "T1", productoId: 5, descripcion: "Polera Pique Blanca XL", proveedorId: null, nombreProveedor: "-", precioUnitario: 0, consumo: 100, unidadMedida: "un", costoTotal: 0 }
        ],
        accesorios: [
            { id: "A1", productoId: 6, descripcion: "Bordado Logo Institucional", proveedorId: null, nombreProveedor: "-", precioUnitario: 0, consumo: 100, unidadMedida: "un", costoTotal: 0 }
        ]
    }
];

export const mockSC = {
    ocAtrasadas: 0,
    sinRecepcion: 0,
    recepError: 0,
    recepParcial: 0,
    entregasAtrasadas: 0,
    proyeccionCierre: 0
};

export const mockKPIs = {
    tiemposPromedio: [
        { name: 'Emisión OC', dias: 4.3, fill: '#10b981' },
        { name: 'Recepción OC', dias: 5.4, fill: '#10b981' },
        { name: 'Gestión Incidencias', dias: 5.0, fill: '#f59e0b' },
        { name: 'Envío Logo (SC)', dias: 2.0, fill: '#3b82f6' },
        { name: 'Retorno Logo (SC)', dias: 2.2, fill: '#6366f1' },
    ],
    flujoVolumen: [
        { name: '# SC DOC', valor: 60 },
        { name: '# ITEMS SC', valor: 306 },
        { name: '# ITEMS OC', valor: 200 },
        { name: 'U. RECEP', valor: 1150 },
        { name: 'U. LOGO', valor: 520 },
        { name: 'U. CLTE', valor: 111 },
    ]
};

export const mockOP = {
    opAtrasada: 2,
    corteAtrasado: 0,
    recepcionLogoAtrasado: 2,
    envioAtrasado: 0,
    devolucionTallerAtrasada: 0,
    entregas7d: 0
};

export const mockOPKPIs = {
    tiemposPorEtapa: [
        { name: 'Corte', dias: 5.5, fill: '#94a3b8' },
        { name: 'Logotipo', dias: 3.5, fill: '#f59e0b' },
        { name: 'Taller Externo', dias: 5.5, fill: '#3b82f6' },
        { name: 'Terminaciones', dias: 1.0, fill: '#10b981' },
    ],
    promedioPorLote: [
        { rango: '1-49', ops: 47, corte: 5, logo: 3.5, taller: 5.6, term: 1, total: 10.5 },
        { rango: '50-99', ops: 7, corte: 5.6, logo: 3.7, taller: null, term: null, total: null },
        { rango: '100-199', ops: 3, corte: 15, logo: null, taller: null, term: null, total: null },
        { rango: '200-399', ops: 1, corte: null, logo: null, taller: null, term: null, total: null },
        { rango: '+400', ops: 0, corte: null, logo: null, taller: null, term: null, total: null },
    ],
    distribucionLote: [
        { name: '1-49', valor: 34 },
        { name: '50-99', valor: 5 },
        { name: '100-199', valor: 2 },
        { name: '200-399', valor: 1 },
        { name: '+400', valor: 0 },
    ]
};

export const mockNotifications = [
    {
        id: 1,
        type: 'OC',
        message: 'OC #4021 - Sin Recepción > 3 días',
        timestamp: 'Hace 10 min',
        read: false,
        priority: 'high',
        category: 'COMPRAS'
    },
    {
        id: 2,
        type: 'LOGO',
        message: 'Logo recepcionado para OP-2024-001',
        timestamp: 'Hace 30 min',
        read: false,
        priority: 'medium',
        category: 'PRODUCCION'
    },
    {
        id: 3,
        type: 'MP',
        message: 'Materia Prima recibida: Tela Algodón',
        timestamp: 'Hace 2 horas',
        read: true,
        priority: 'normal',
        category: 'BODEGA'
    },
    {
        id: 4,
        type: 'OC',
        message: 'Orden de Compra #4020 aprobada',
        timestamp: 'Hace 3 horas',
        read: true,
        priority: 'normal',
        category: 'COMPRAS'
    }
];

export const mockAllOCs = [
    {
        idOC: 1,
        numeroOC: 'OC-61034',
        fechaEmision: '2024-02-11',
        proveedor: 'TEXTIL PACÍFICO',
        proveedorId: 1,
        items: 'Hilo Poliéster 40/2 Azul',
        montoTotal: 150000,
        estado: 'Pendiente',
        solicitudCompraId: 'SC-101',
        ordenProduccionId: 'OP-2024-001',
        tipo: 'PROVEEDOR',
        lineas: [
            { scItemId: 'SCITEM-002', cantidad: 50, cantidadRecibida: 0 }
        ]
    },
    {
        idOC: 2,
        numeroOC: 'OC-61035',
        fechaEmision: '2024-02-12',
        proveedor: 'AVITEX LTDA',
        proveedorId: 2,
        items: 'Tela Jersey 100% Algodón',
        montoTotal: 850000,
        estado: 'Recibida',
        solicitudCompraId: 'SC-101',
        ordenProduccionId: 'OP-2024-001',
        tipo: 'PROVEEDOR',
        lineas: [
            { scItemId: 'SCITEM-001', cantidad: 300, cantidadRecibida: 300 }
        ],
        recepciones: [
            { fecha: '2024-02-14', cantidad: 300, guia: 'G-9988' }
        ]
    },
];

export const mockNVs = [
    {
        idNV: 1,
        numeroNV: 'NV-20584',
        cliente: 'PROCESOS SANITARIOS SPA',
        clienteId: 1,
        vendedorId: 101,
        vendedor: 'Juan Pérez',
        fechaEmision: '2026-02-10',
        estado: 'Pendiente SC',
        montoTotal: 1250000,
        solicitudCompraId: 'SC-101',
        idOP: 'OP-2024-001',
        esKit: true,
        items: [
            { nroItem: 1, modelo: 'Polera Jersey', nombreProducto: 'Polera Jersey', color: 'Azul', talla: 'M', cantidad: 50, nombreProveedor: 'TEXTIL PACÍFICO', proveedorId: 1, generaOt: true, detalleOt: 'Bordado frontal 5x5cm logo empresa' },
            { nroItem: 2, modelo: 'Polera Jersey', nombreProducto: 'Polera Jersey', color: 'Azul', talla: 'L', cantidad: 20, nombreProveedor: 'TEXTIL PACÍFICO', proveedorId: 1, generaOt: false }
        ]
    },
    {
        idNV: 2,
        numeroNV: 'NV-20585',
        cliente: 'I MUNICIPALIDAD LO BARNECHEA',
        clienteId: 2,
        vendedorId: 102,
        vendedor: 'María González',
        fechaEmision: '2026-02-11',
        estado: 'Evaluación',
        montoTotal: 890000,
        solicitudCompraId: 'SC-102',
        idOP: 'OP-2024-003',
        esKit: false,
        items: [
            { nroItem: 1, modelo: 'Jockey Gabardina', nombreProducto: 'Jockey Gabardina', color: 'Verde', talla: 'Única', cantidad: 30, nombreProveedor: 'AVITEX LTDA', proveedorId: 2 }
        ]
    },
    {
        idNV: 3,
        numeroNV: 'NV-20586',
        cliente: 'TEXTILES EXPORT LTDA',
        clienteId: 4,
        vendedorId: 103,
        vendedor: 'Carlos López',
        fechaEmision: '2026-02-12',
        estado: 'Pendiente SC',
        montoTotal: 2100000,
        solicitudCompraId: null,
        idOP: 'OP-2024-002',
        esKit: false,
        items: [
            { nroItem: 1, modelo: 'Camisa Oxford', nombreProducto: 'Camisa Oxford', color: 'Blanco', talla: 'L', cantidad: 500, nombreProveedor: 'TELAS COLÓN', proveedorId: 3 },
            { nroItem: 2, modelo: 'Botones 24"', nombreProducto: 'Botones 24"', color: 'Blanco', talla: 'N/A', cantidad: 2000, nombreProveedor: 'AVITEX LTDA', proveedorId: 2 }
        ]
    },
    {
        idNV: 'NV-20580',
        cliente: 'CLINICA ALEMANA',
        vendedor: 'Juan Pérez',
        fecha: '01/02/2026',
        estado: 'Entregado',
        total: 750000,
        solicitudCompraId: 'SC-99',
        idOP: 'OP-100',
        items: [
            { id: 6, nombreProducto: 'Delantal Médico', color: 'Blanco', talla: 'L', cantidad: 100, nombreProveedor: 'AVITEX LTDA' }
        ]
    },
    {
        idNV: 'NV-20578',
        cliente: 'I MUNICIPALIDAD LO BARNECHEA',
        vendedor: 'María González',
        fecha: '25/01/2026',
        estado: 'Entregado',
        total: 1500000,
        solicitudCompraId: 'SC-95',
        idOP: 'OP-090',
        items: [
            { id: 7, nombreProducto: 'Geologo Gabardina', color: 'Naranja', talla: 'XL', cantidad: 200, nombreProveedor: 'AVITEX LTDA' }
        ]
    }
];

export const mockCotizaciones = [
    {
        id: 'COT-2024-089',
        cliente: 'PROCESOS SANITARIOS SPA',
        fecha: '05/02/2026',
        validez: '20/02/2026',
        total: 1250000,
        estado: 'Aprobada',
        items: [
            { id: 1, garment: 'Polera Jersey', color: 'Azul Marino', size: 'M', quantity: 50, price: 15000 },
            { id: 2, garment: 'Polera Jersey', color: 'Azul Marino', size: 'L', quantity: 20, price: 15000 }
        ]
    },
    {
        id: 'COT-2024-092',
        cliente: 'I MUNICIPALIDAD LO BARNECHEA',
        fecha: '08/02/2026',
        validez: '23/02/2026',
        total: 890000,
        estado: 'Pendiente NV',
        items: [
            { id: 3, garment: 'Jockey Gabardina', color: 'Beige', size: 'Única', quantity: 100, price: 8900 }
        ]
    },
    {
        id: 'COT-2024-095',
        cliente: 'TEXTILES EXPORT LTDA',
        fecha: '09/02/2026',
        validez: '24/02/2026',
        total: 2100000,
        estado: 'Aprobada',
        items: [
            { id: 4, garment: 'Camisa Oxford', color: 'Celeste', size: 'M', quantity: 80, price: 18500 },
            { id: 5, garment: 'Camisa Oxford', color: 'Celeste', size: 'L', quantity: 20, price: 18500 },
            { id: 6, garment: 'Pantalón Cargo', color: 'Azul', size: '44', quantity: 25, price: 21000 }
        ]

    }
];

export const mockOpDetails = {
    'OP-2024-001': {
        tizado: '09/02/2026',
        finCorte: '10/02/2026',
        regresoLogo: '11/02/2026',
        finTaller: '12/02/2026',
        finOP: null,
        personalizado: null,
        entregaBodega: null,
        obsTaller: 'Taller Maquila Centro: 50 poleras con costura reforzada listas. Faltan terminaciones.',
        requerimientos: [
            { id: 'R1', item: 'Tela Jersey 24/1', detalle: 'Azul Marino', cantidad: 120, unidad: 'Kg', cantidadPedida: 100, cantidadRecibida: 0, estado: 'Parcial' },
            { id: 'R2', item: 'Cuellos Tejidos', detalle: 'Azul/Blanco', cantidad: 500, unidad: 'Un', cantidadPedida: 500, cantidadRecibida: 500, estado: 'Completado' },
            { id: 'R3', item: 'Logo Bordado', detalle: 'Pecho Izq', cantidad: 500, unidad: 'Un', cantidadPedida: 0, cantidadRecibida: 0, estado: 'Pendiente' }
        ]
    },
    'OP-2024-002': {
        tizado: '08/02/2026',
        finCorte: '09/02/2026',
        regresoLogo: '09/02/2026',
        finTaller: '10/02/2026',
        finOP: '11/02/2026',
        personalizado: '12/02/2026',
        entregaBodega: '12/02/2026',
        obsTaller: 'Orden completada satisfactoriamente. Calidad aprobada.',
    },
    'OP-2024-003': {
        tizado: '12/02/2026',
        finCorte: null,
        regresoLogo: null,
        finTaller: null,
        finOP: null,
        personalizado: null,
        entregaBodega: null,
        obsTaller: 'Esperando recepción de tela Oxford celeste.',
        requerimientos: [
            { id: 'R4', item: 'Tela Oxford', detalle: 'Celeste', cantidad: 200, unidad: 'Mt', cantidadPedida: 0, cantidadRecibida: 0, estado: 'Pendiente' },
            { id: 'R5', item: 'Botones 24"', detalle: 'Blanco Perla', cantidad: 2000, unidad: 'Un', cantidadPedida: 1000, cantidadRecibida: 0, estado: 'OC Emitida' }
        ]
    },
    'OP-104': {
        tizado: '10/02/2026',
        finCorte: '12/02/2026',
        regresoLogo: null,
        finTaller: null,
        finOP: null,
        personalizado: null,
        entregaBodega: null,
        obsTaller: 'Corte terminado. Enviado a taller de bordado.',
    },
    'OP-105': {
        tizado: '11/02/2026',
        finCorte: '11/02/2026',
        regresoLogo: '12/02/2026',
        finTaller: null,
        finOP: null,
        personalizado: null,
        entregaBodega: null,
        obsTaller: 'Bordado recibido. Pendiente envío a taller confección.',
    },
    'OP-106': {
        tizado: '09/02/2026',
        finCorte: '10/02/2026',
        regresoLogo: '10/02/2026',
        finTaller: '11/02/2026',
        finOP: '12/02/2026',
        personalizado: null,
        entregaBodega: null,
        obsTaller: 'Terminaciones finales. Limpieza de hilos y planchado.',
    }
};

// -- FASE 2: TRAZABILIDAD Y ALERTAS --
export const mockOS = [
    { id: 'OS-801', idOP: 'OP-2024-001', servicio: 'Taller Confección', proveedor: 'Maquila Centro', fechaEnvio: '05/02/2026', fechaPromesa: '10/02/2026', estado: 'En Proceso' },
    { id: 'OS-802', idOP: 'OP-2024-003', servicio: 'Bordado Logo', proveedor: 'Taller Logo Premium', fechaEnvio: '01/02/2026', fechaPromesa: '05/02/2026', estado: 'Atrasado' }
];

export const mockOE = [
    { id: 'OE-901', op: 'OP-2024-002', fecha: '20/02/2026', cliente: 'Cliente Y', estado: 'Pendiente' }
];

export const mockClientesVIP = [
    { id: 'CL-001', nombre: 'Orden de Malta', status: 'VIP', opCritica: 'OP-001' },
    { id: 'CL-002', nombre: 'Clinica Las Condes', status: 'VIP', opCritica: null }
];

export const mockClientes = [
    {
        clienteId: 1,
        runCliente: '76.120.450-8',
        nombreCliente: 'PROCESOS SANITARIOS SPA',
        contacto: 'Juan Pérez',
        correoCliente: 'jperez@prosan.cl',
        telefonoCliente: '+56 9 8877 6655',
        direccion: 'Av. Las Industrias 4050, San Joaquín',
        activo: true,
        segmento: 'Industrial'
    },
    {
        clienteId: 2,
        runCliente: '69.050.300-K',
        nombreCliente: 'I MUNICIPALIDAD LO BARNECHEA',
        contacto: 'María González',
        correoCliente: 'mgonzalez@lobarnechea.cl',
        telefonoCliente: '+56 2 2750 1200',
        direccion: 'Av. El Rodeo 12750, Lo Barnechea',
        activo: true,
        segmento: 'Público'
    },
    {
        clienteId: 3,
        runCliente: '77.890.120-2',
        nombreCliente: 'CLINICA ALEMANA',
        contacto: 'Carlos López',
        correoCliente: 'clopez@alemana.cl',
        telefonoCliente: '+56 2 2210 1111',
        direccion: 'Av. Vitacura 5951, Vitacura',
        activo: false,
        segmento: 'Salud'
    },
    {
        clienteId: 4,
        runCliente: '88.330.220-4',
        nombreCliente: 'TEXTILES EXPORT LTDA',
        contacto: 'Ana Rojas',
        correoCliente: 'arojas@texexport.cl',
        telefonoCliente: '+56 9 9988 7766',
        direccion: 'Panamericana Norte 15000, Lampa',
        activo: true,
        segmento: 'Textil'
    }
];



export const mockCosteos = [
    {
        id: "COS-002",
        solicitudCostosId: "SCOS-002",
        insumos: [
            { id: 1, producto: "Tela Poliéster", costo: 4500, unidad: "m", cantidad: 250, subtotal: 1125000 },
            { id: 2, producto: "Hilo Verde", costo: 1200, unidad: "kg", cantidad: 5, subtotal: 6000 },
            { id: 3, producto: "Etiqueta", costo: 150, unidad: "und", cantidad: 1000, subtotal: 150000 }
        ],
        costoTotalMP: 1281000,
        margenBruto: 35,
        costoVentaSugerido: 1970769
    }
];

export const mockEvaluacionesNegocio = [
    {
        id: "EVN-001",
        evaluacionNegocioId: "EVN-001",
        costoId: "COS-002",
        cliente: "I MUNICIPALIDAD LO BARNECHEA",
        clienteNombre: "I MUNICIPALIDAD LO BARNECHEA",
        clienteId: 2,
        articulo: "CAMISA OXFORD BLANCA",
        articuloDescripcion: "CAMISA OXFORD BLANCA",
        fecha: "2026-02-16",
        solicitud: mockSolicitudesCostos[0],
        items: [
            { id: 1, cant: 56, descripcion: "CAMISA OXFORD BLANCA", familia: "20", precioVentaMin: 13019, precioNetoUnit: 15006, costoCompra: 5497, costoBordado: 2330, cajas: 180, tipo: "Compra" },
            { id: 2, cant: 56, descripcion: "CAMISA OXFORD CELESTE", familia: "20", precioVentaMin: 13019, precioNetoUnit: 15006, costoCompra: 5497, costoBordado: 2330, cajas: 180, tipo: "Compra" },
            { id: 3, cant: 2, descripcion: "CAMISA BLANCA CON LOGO", familia: "20", precioVentaMin: 26178, precioNetoUnit: 30750, costoCompra: 14990, costoBordado: 2330, cajas: 180, tipo: "Compra" },
            { id: 4, cant: 55, descripcion: "BLUSA OXFORD CELESTE", familia: "20", precioVentaMin: 12300, precioNetoUnit: 14760, costoCompra: 4954, costoBordado: 2330, cajas: 180, tipo: "Produccion", costoPrenda: 8560 },
            { id: 5, cant: 56, descripcion: "BLUSA OXFORD BLANCA", familia: "20", precioVentaMin: 12300, precioNetoUnit: 14760, costoCompra: 4954, costoBordado: 2330, cajas: 180, tipo: "Produccion", costoPrenda: 8560 }
        ],
        datosEconomicos: {
            costoDirecto: 1281000,
            precioVenta: 2000000,
            margenFinal: 36
        },
        otrosCostos: {
            garantiaSeriedad: 0,
            garantiaFiel: 0,
            flete: 145658,
            modificacion: 0,
            tomaTallaje: 0,
            certificacion: 0,
            muestras: 0
        },
        costoFinalAcordado: 2000000,
        margenFinal: 36,
        articuloDescripcion: "CAMISA OXFORD BLANCA",
        margenGanancia: 36,
        cantidad: 225,
        condiciones: {
            anticipo: 100,
            saldo: 0,
            flete: "Cliente",
            garantia: "90 días",
            plazoEntrega: "2026-03-10"
        },
        estado: "Activo"
    },
    {
        id: "EVN-002",
        costoId: "COS-003",
        cliente: "PROCESOS SANITARIOS SPA",
        articulo: "Polera Piqué Institucional",
        fecha: "2026-02-10",
        solicitud: mockSolicitudesCostos[0],
        datosEconomicos: {
            costoDirecto: 8612500,
            precioVenta: 12000000,
            margenFinal: 28
        },
        costoFinalAcordado: 12000000,
        margenFinal: 28,
        cantidad: 500,
        condiciones: {
            anticipo: 30,
            saldo: 70,
            flete: "Antuan",
            garantia: "60 días",
            plazoEntrega: "2026-04-01"
        },
        estado: "Cerrado"
    },
    {
        id: "EVN-003",
        costoId: "COS-004",
        cliente: "CLINICA ALEMANA",
        articulo: "Delantal Medico Premium",
        fecha: "2026-02-18",
        solicitud: mockSolicitudesCostos[0],
        datosEconomicos: {
            costoDirecto: 5000000,
            precioVenta: 8500000,
            margenFinal: 41
        },
        costoFinalAcordado: 8500000,
        margenFinal: 41,
        cantidad: 200,
        condiciones: {
            anticipo: 100,
            saldo: 0,
            flete: "Cliente",
            garantia: "90 días",
            plazoEntrega: "2026-03-10"
        },
        estado: "Activo"
    }
];


export const mockTelas = [
    "Algodón 100%", "Poliéster/Algodón 65/35", "Poliéster Reciclado", "Jersey 24/1", "Piqué", "Gabardina"
];

export const mockProveedoresLentos = {
    'Maquila Centro': { demoras: 5, rating: 2.5 },
    'Taller Logo Premium': { demoras: 2, rating: 3.8 }
};

export const mockTalleresExternos = [
    { id: 1, nombre: 'Maquila Centro', capacidad: 500, cargaActual: 450 },
    { id: 2, nombre: 'Taller Logo Premium', capacidad: 200, cargaActual: 180 }
];

export const mockVendedores = [
    { id: 101, nombre: 'JUAN PÉREZ' },
    { id: 102, nombre: 'MARIA GONZALEZ' },
    { id: 103, nombre: 'RODRIGO TAPIA' },
    { id: 104, nombre: 'PATRICIO SOTO' }
];

export const mockProveedores = [
    {
        proveedorId: 1,
        nombreProveedor: 'TEXTIL PACÍFICO',
        rutProveedor: '76.458.120-K',
        categoria: 'Telas y Géneros',
        contactoProveedor: 'Carlos Méndez',
        correoProveedor: 'ventas@textilpacifico.cl',
        activo: true,
        precios: [
            { id: 1, garment: 'Polera Jersey', specs: '100% Algodón, 160g/m2, Jersey 24/1', price: 4500, lastUpdate: '10/01/2026' },
            { id: 2, garment: 'Polera Piqué', specs: '65% Algodón / 35% Poliéster, Piqué Lacoste', price: 5800, lastUpdate: '10/01/2026' },
            { id: 3, garment: 'Oberol Canvas', specs: 'Canvas 100% Algodón, Costuras reforzadas, Cinta 3M', price: 12500, lastUpdate: '15/01/2026' }
        ]
    },
    {
        proveedorId: 2,
        nombreProveedor: 'AVITEX LTDA',
        rutProveedor: '88.122.330-4',
        categoria: 'Insumos / Fornituras',
        contactoProveedor: 'Ana María Rojas',
        correoProveedor: 'arojas@avitex.cl',
        activo: true,
        precios: [
            { id: 4, garment: 'Parka Térmica', specs: 'Impermeable, Relleno térmico 200g, Forro polar', price: 25000, lastUpdate: '05/02/2026' },
            { id: 5, garment: 'Jockey Gabardina', specs: 'Gabardina 6 paneles, Ajuste metálico, Visera curva', price: 3500, lastUpdate: '05/02/2026' },
            { id: 6, garment: 'Botones 24"', specs: 'Madreperla sintética, 4 ojales, Diámetro 15mm', price: 45, lastUpdate: '01/02/2026' }
        ]
    },
    {
        proveedorId: 3,
        nombreProveedor: 'TELAS COLÓN',
        rutProveedor: '77.990.110-2',
        categoria: 'Telas y Géneros',
        contactoProveedor: 'Roberto Colón',
        correoProveedor: 'rcolon@telascolon.cl',
        activo: true,
        precios: [
            { id: 7, garment: 'Camisa Oxford', specs: 'Tela Oxford 60/40, Cuello reforzado, Botones pasta', price: 7800, lastUpdate: '20/01/2026' },
            { id: 8, garment: 'Pantalón Cargo', specs: 'Gabardina 8oz, Múltiples bolsillos, Triple costura', price: 15500, lastUpdate: '20/01/2026' }
        ]
    }
];

export const mockOTs = [
    {
        idOT: 'OT-5001',
        idOP: 'OP-2024-001',
        tipoOT: 'BORDADO',
        proveedor: 'Taller Logo Premium',
        fechaEmision: '2026-02-10',
        fechaPromesa: '2026-02-12',
        estado: 'En Proceso'
    },
    {
        idOT: 'OT-5002',
        idOP: 'OP-104',
        tipoOT: 'ESTAMPADO',
        proveedor: 'Estampados Santiago',
        fechaEmision: '2026-02-08',
        fechaPromesa: '2026-02-10',
        estado: 'Atrasado'
    }
];

export const mockOpPersonalizacion = [
    {
        idPersonalizacion: 'PERS-104',
        idOP: 'OP-104',
        items: [
            { nombre: 'JUAN PEREZ', talla: 'L', estado: 'Completado' },
            { nombre: 'MARIA SOTO', talla: 'M', estado: 'Pendiente' },
            { nombre: 'CARLOS RUIZ', talla: 'XL', estado: 'Iniciado' }
        ]
    }
];
