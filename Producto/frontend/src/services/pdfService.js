import jsPDF from 'jspdf';
import 'jspdf-autotable';

const COMPANY_INFO = {
    name: "ANTUAN S.A.",
    rut: "76.123.456-7",
    address: "Santiago, Chile",
    phone: "+56 2 2345 6789",
    email: "contacto@antuan.cl"
};

const colors = {
    primary: [37, 99, 235], // Blue-600
    secondary: [30, 41, 59], // Slate-900
    accent: [79, 70, 229], // Indigo-600
    text: [31, 41, 55], // Gray-800
    lightText: [107, 114, 128], // Gray-500
    white: [255, 255, 255]
};

const drawHeader = (doc, title, folio) => {
    // Elegant background band
    doc.setFillColor(...colors.secondary);
    doc.rect(0, 0, 215, 45, 'F');

    // Logo / Company Name
    doc.setTextColor(...colors.white);
    doc.setFontSize(28);
    doc.setFont("helvetica", "bold");
    doc.text(COMPANY_INFO.name, 15, 28);

    doc.setFontSize(9);
    doc.setFont("helvetica", "normal");
    doc.text(`RUT: ${COMPANY_INFO.rut}`, 15, 35);
    doc.text(COMPANY_INFO.address, 15, 39);

    // Document type and folio (Right aligned)
    doc.setFontSize(14);
    doc.setFont("helvetica", "bold");
    doc.text(title.toUpperCase(), 140, 20);

    doc.setFontSize(24);
    doc.text(`N° ${folio || 'PENDIENTE'}`, 140, 32);
};

const drawFooter = (doc) => {
    const pageCount = doc.internal.getNumberOfPages();
    for (let i = 1; i <= pageCount; i++) {
        doc.setPage(i);
        doc.setFontSize(8);
        doc.setTextColor(...colors.lightText);
        doc.text(`Página ${i} de ${pageCount}`, 180, 285);
        doc.text(`${COMPANY_INFO.name} - Sistema de Gestión ERP - Generado el ${new Date().toLocaleString()}`, 15, 285);
    }
};

const formatCurrency = (val) => `$${(val || 0).toLocaleString('es-CL')}`;

export const pdfService = {
    /**
     * Genera PDF para Nota de Venta (NV)
     */
    generateNV: (data) => {
        const doc = new jsPDF();
        const folio = data.folio || data.numeroNV || 'DRAFT';
        drawHeader(doc, "Nota de Venta", folio);

        // Info Block
        doc.setTextColor(...colors.primary);
        doc.setFontSize(12);
        doc.setFont("helvetica", "bold");
        doc.text("DATOS DE LA OPERACIÓN", 15, 60);

        doc.setTextColor(...colors.text);
        doc.setFontSize(9);
        doc.setFont("helvetica", "bold");
        doc.text("CLIENTE:", 15, 68);
        doc.text("VENDEDOR:", 15, 73);
        doc.text("FECHA EMISIÓN:", 110, 68);
        doc.text("FECHA ENTREGA:", 110, 73);

        doc.setFont("helvetica", "normal");
        doc.text(String(data.clienteNombre || 'N/A'), 45, 68);
        doc.text(String(data.vendedorNombre || data.vendedorId || 'Principal'), 45, 73);
        doc.text(String(data.fechaEmision || new Date().toLocaleDateString()), 145, 68);
        doc.text(String(data.fechaEntregaEstimada || 'Por definir'), 145, 73);

        if (data.esKit) {
            doc.setFillColor(243, 244, 246);
            doc.roundedRect(15, 80, 180, 12, 2, 2, 'F');
            doc.setFont("helvetica", "bold");
            doc.text("FORMATO:", 20, 88);
            doc.setFont("helvetica", "normal");
            doc.text(`KIT - ${data.detalleKit || 'Sin especificaciones'}`, 45, 88);
        }

        const items = data.items || [];
        const tableColumn = ["Ítem", "Producto / Detalles", "Tallas (XS-M-L-XL)", "Cant.", "P. Unit", "Total"];
        const tableRows = items.map((item, index) => [
            index + 1,
            {
                content: `${item.nombreProducto}\n${item.modelo || ''} | ${item.tela || ''} | ${item.color || ''}\nLogo: ${item.logo || 'N/A'}`,
                styles: { cellPadding: 3 }
            },
            `${item.sizes?.XS || 0} - ${item.sizes?.S || 0} - ${item.sizes?.M || 0} - ${item.sizes?.L || 0} - ${item.sizes?.XL || 0}`,
            item.quantity,
            formatCurrency(item.unitPrice),
            formatCurrency((item.unitPrice || 0) * item.quantity)
        ]);

        doc.autoTable({
            head: [tableColumn],
            body: tableRows,
            startY: data.esKit ? 95 : 85,
            theme: 'grid',
            headStyles: { fillColor: colors.secondary, textColor: colors.white, fontSize: 8, fontStyle: 'bold', halign: 'center' },
            columnStyles: {
                0: { halign: 'center', cellWidth: 10 },
                2: { halign: 'center', cellWidth: 40 },
                3: { halign: 'center', cellWidth: 15 },
                4: { halign: 'right', cellWidth: 25 },
                5: { halign: 'right', cellWidth: 25 },
            },
            bodyStyles: { fontSize: 8, textColor: colors.text, valign: 'middle' },
            alternateRowStyles: { fillColor: [250, 252, 255] },
        });

        const finalY = doc.lastAutoTable.finalY + 10;
        const totalAmount = items.reduce((sum, i) => sum + (i.unitPrice * i.quantity), 0);
        const totalItems = items.reduce((sum, i) => sum + i.quantity, 0);

        doc.setFont("helvetica", "bold");
        doc.setFontSize(10);
        doc.text(`TOTAL UNIDADES: ${totalItems}`, 130, finalY);
        doc.setTextColor(...colors.primary);
        doc.text(`TOTAL NETO: ${formatCurrency(totalAmount)}`, 130, finalY + 6);

        drawFooter(doc);
        doc.save(`NV_${folio}.pdf`);
    },

    /**
     * Genera PDF para Solicitud de Compra (SC)
     */
    generateSC: (data) => {
        const doc = new jsPDF();
        const folio = data.folio || data.id || 'PENDIENTE';
        drawHeader(doc, "Solicitud de Compra", folio);

        doc.setTextColor(...colors.accent);
        doc.setFontSize(12);
        doc.setFont("helvetica", "bold");
        doc.text("DETALLES DE SOLICITUD", 15, 60);

        doc.setTextColor(...colors.text);
        doc.setFontSize(9);
        doc.text(`SOLICITANTE: ${data.requester || 'DEPARTAMENTO TÉCNICO'}`, 15, 68);
        doc.text(`VINCULADO A: ${data.linkedNV || 'MANUAL'}`, 15, 73);
        doc.text(`FECHA SOLICITUD: ${new Date().toLocaleDateString()}`, 110, 68);
        doc.text(`CLIENTE REF: ${data.customer || 'N/A'}`, 110, 73);

        const tableColumn = ["#", "Prenda / Código", "Proveedor Sugerido", "Tallas", "Cant.", "Obs. OT"];
        const tableRows = (data.items || []).map((item, index) => [
            index + 1,
            `${item.garment}\nCod: ${item.code || 'S/C'}\n${item.color || ''} | ${item.gender || 'U'}`,
            item.supplier || 'SIN ASIGNAR',
            `${item.sizes?.XS || 0}/${item.sizes?.S || 0}/${item.sizes?.M || 0}/${item.sizes?.L || 0}/${item.sizes?.XL || 0}`,
            item.quantity,
            item.generaOt ? `[OT] ${item.detalleOt || 'Requerida'}` : 'N/A'
        ]);

        doc.autoTable({
            head: [tableColumn],
            body: tableRows,
            startY: 85,
            theme: 'striped',
            headStyles: { fillColor: colors.accent, textColor: colors.white, fontSize: 8 },
            bodyStyles: { fontSize: 8 },
        });

        drawFooter(doc);
        doc.save(`SC_${folio}.pdf`);
    },

    /**
     * Genera PDF para Orden de Producción (OP)
     */
    generateOP: (data) => {
        const doc = new jsPDF();
        const folio = data.numeroOP || data.id || 'N/A';
        drawHeader(doc, "Orden de Producción", folio);

        doc.setTextColor(...colors.primary);
        doc.setFontSize(12);
        doc.text("PLANIFICACIÓN DE PRODUCCIÓN", 15, 60);

        doc.setTextColor(...colors.text);
        doc.setFontSize(9);
        doc.text(`FECHA INICIO: ${data.fechaInicio || 'Pendiente'}`, 15, 68);
        doc.text(`ENTREGA ESTIMADA: ${data.fechaEntregaProgramada || 'S/P'}`, 15, 73);
        doc.text(`REF NOTA VENTA: ${data.notaVentaId || 'N/A'}`, 110, 68);

        const tableColumn = ["#", "Producto / Modelo", "Talla", "Cant.", "Telas / Comp.", "Logotipos"];
        const tableRows = (data.items || []).map((item, index) => [
            index + 1,
            `${item.productoNombre || 'Prenda'}\n${item.modelo || ''}`,
            item.talla || 'S/T',
            item.cantidad,
            `${item.tela || ''}\n${item.composicion || ''}`,
            item.llevaLogo || 'N/A'
        ]);

        doc.autoTable({
            head: [tableColumn],
            body: tableRows,
            startY: 85,
            headStyles: { fillColor: [51, 65, 85] }
        });

        drawFooter(doc);
        doc.save(`OP_${folio}.pdf`);
    },

    /**
     * Genera PDF para Orden de Compra (OC)
     */
    generateOC: (data) => {
        const doc = new jsPDF();
        const folio = data.idOC || data.id || data.numeroOC || 'N/A';
        drawHeader(doc, "Orden de Compra", folio);

        doc.setTextColor(...colors.accent);
        doc.setFontSize(12);
        doc.text("ORDEN DE SUMINISTRO", 15, 60);

        doc.setTextColor(...colors.text);
        doc.setFontSize(9);
        doc.text(`PROVEEDOR: ${data.proveedorNombre || data.proveedor || 'N/A'}`, 15, 68);
        doc.text(`FECHA EMISIÓN: ${data.fechaEmision || data.fecha || new Date().toLocaleDateString()}`, 110, 68);

        const tableColumn = ["#", "Descripción", "Cantidad", "Unitario", "Total"];
        const tableRows = (data.items || []).map((item, index) => [
            index + 1,
            item.descripcion || item.garment,
            item.cantidad || item.qty,
            formatCurrency(item.precioUnitario || item.unitPrice || item.price),
            formatCurrency((item.precioUnitario || item.unitPrice || item.price || 0) * (item.cantidad || item.qty))
        ]);

        doc.autoTable({
            head: [tableColumn],
            body: tableRows,
            startY: 80,
            headStyles: { fillColor: colors.accent }
        });

        drawFooter(doc);
        doc.save(`OC_${folio}.pdf`);
    }
};
