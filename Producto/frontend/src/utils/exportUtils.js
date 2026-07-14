import { jsPDF } from 'jspdf';
import 'jspdf-autotable';
import ExcelJS from 'exceljs';

/**
 * Generates an Excel file for the Business Evaluation using ExcelJS (Vulnerability-free).
 * @param {Object} data - The evaluation data, including items, otrosCostos, and totals.
 */
export const exportToExcel = async (data) => {
    const { items, otrosCostos, totals, cliente, id } = data;
    const workbook = new ExcelJS.Workbook();

    // 1. Executive Summary Sheet
    const wsSummary = workbook.addWorksheet('Resumen Ejecutivo');
    wsSummary.columns = [
        { header: 'Indicador', key: 'indicador', width: 30 },
        { header: 'Valor', key: 'valor', width: 20 }
    ];
    wsSummary.addRows([
        { indicador: 'Subtotal Venta Artículos', valor: totals.subtotalVenta },
        { indicador: 'Total Otros Costos', valor: totals.totalOtros },
        { indicador: 'Monto Total Neto Negocio', valor: totals.totalNeto },
        { indicador: 'Costo Operacional Total', valor: totals.totalCostoGeneral },
        { indicador: 'Margen de Contribución ($)', valor: totals.margenPesos },
        { indicador: 'Margen de Contribución (%)', valor: totals.margenPorc + '%' }
    ]);
    wsSummary.getRow(1).font = { bold: true };

    // 2. Items Detail Sheet
    const wsItems = workbook.addWorksheet('Detalle Artículos');
    wsItems.columns = [
        { header: '#', key: 'id', width: 5 },
        { header: 'Cantidad', key: 'cant', width: 10 },
        { header: 'Descripción', key: 'desc', width: 40 },
        { header: 'Familia', key: 'fam', width: 15 },
        { header: 'P. Venta Mín', key: 'pmin', width: 15 },
        { header: 'P. Neto Unit Acordado', key: 'pneto', width: 22 },
        { header: 'Total Venta Línea', key: 'total', width: 20 }
    ];

    items.forEach((item, idx) => {
        wsItems.addRow({
            id: idx + 1,
            cant: item.cant,
            desc: item.descripcion,
            fam: item.familia,
            pmin: item.precioVentaMin,
            pneto: item.precioNetoUnit,
            total: item.cant * item.precioNetoUnit
        });
    });
    wsItems.getRow(1).font = { bold: true };

    // 3. Otros Costos Sheet
    const wsOtros = workbook.addWorksheet('Otros Costos');
    wsOtros.columns = [
        { header: 'Concepto', key: 'concepto', width: 30 },
        { header: 'Monto', key: 'monto', width: 15 }
    ];
    wsOtros.addRows([
        { concepto: 'Garantía Seriedad Oferta', monto: otrosCostos.garantiaSeriedad },
        { concepto: 'Garantía Fiel Cumplimiento', monto: otrosCostos.garantiaFiel },
        { concepto: 'Costo Flete Especial', monto: otrosCostos.flete },
        { concepto: 'Modificación de Prenda', monto: otrosCostos.modificacion },
        { concepto: 'Toma de Tallaje Especial', monto: otrosCostos.tomaTallaje },
        { concepto: 'Certificación / Ensayos', monto: otrosCostos.certificacion },
        { concepto: 'Costo Muestras Físicas', monto: otrosCostos.muestras }
    ]);
    wsOtros.getRow(1).font = { bold: true };

    // Generate and save file
    const buffer = await workbook.xlsx.writeBuffer();
    const blob = new Blob([buffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
    const url = window.URL.createObjectURL(blob);
    const anchor = document.createElement('a');
    anchor.href = url;
    anchor.download = `Evaluacion_${id}_${cliente.replace(/ /g, '_')}.xlsx`;
    anchor.click();
    window.URL.revokeObjectURL(url);
};

/**
 * Generates a formal PDF for the Business Evaluation.
 * @param {Object} data - The evaluation data.
 */
export const exportToPDF = (data) => {
    const { items, otrosCostos, totals, cliente, id, fecha, condiciones } = data;
    const doc = new jsPDF();

    // -- Header --
    doc.setFontSize(22);
    doc.setTextColor(63, 81, 181); // Indigo color
    doc.text('EVALUACIÓN DE NEGOCIO', 105, 20, { align: 'center' });

    doc.setDrawColor(63, 81, 181);
    doc.setLineWidth(1);
    doc.line(20, 25, 190, 25);

    // -- Client Info --
    doc.setFontSize(10);
    doc.setTextColor(100);
    doc.text(`ID Evaluación: ${id}`, 20, 35);
    doc.text(`Fecha: ${fecha || new Date().toISOString().split('T')[0]}`, 190, 35, { align: 'right' });

    doc.setFontSize(14);
    doc.setTextColor(0);
    doc.setFont(undefined, 'bold');
    doc.text(`CLIENTE: ${cliente.toUpperCase()}`, 20, 45);
    doc.setFont(undefined, 'normal');

    // -- Totals Summary Box --
    doc.setFillColor(248, 250, 252);
    doc.roundedRect(20, 55, 170, 30, 3, 3, 'F');

    doc.setFontSize(9);
    doc.setTextColor(150);
    doc.text('MONTO TOTAL NETO', 35, 65, { align: 'center' });
    doc.text('MARGEN FINAL', 105, 65, { align: 'center' });
    doc.text('TOTAL COSTOS', 175, 65, { align: 'center' });

    doc.setFontSize(16);
    doc.setTextColor(30);
    doc.setFont(undefined, 'bold');
    doc.text(`$${Number(totals.totalNeto).toLocaleString('es-CL')}`, 35, 75, { align: 'center' });
    doc.setTextColor(totals.margenPorc < 20 ? 230 : 25, totals.margenPorc < 20 ? 80 : 130, totals.margenPorc < 20 ? 30 : 60); // Green or Orange
    doc.text(`${totals.margenPorc}%`, 105, 75, { align: 'center' });
    doc.setTextColor(30);
    doc.text(`$${Number(totals.totalCostoGeneral).toLocaleString('es-CL')}`, 175, 75, { align: 'center' });
    doc.setFont(undefined, 'normal');

    // -- Items Table --
    doc.setFontSize(11);
    doc.setTextColor(63, 81, 181);
    doc.text('DETALLE DE ARTÍCULOS Y PRECIOS', 20, 95);

    const tableRows = items.map((item, idx) => [
        idx + 1,
        item.cant,
        item.descripcion,
        item.familia,
        `$${item.precioNetoUnit.toLocaleString('es-CL')}`,
        `$${(item.cant * item.precioNetoUnit).toLocaleString('es-CL')}`,
        `${(((item.precioNetoUnit - (item.tipo === 'Compra' ? Number(item.costoCompra) + Number(item.costoBordado) : Number(item.costoPrenda) + Number(item.costoBordado))) / item.precioNetoUnit) * 100).toFixed(1)}%`
    ]);

    doc.autoTable({
        startY: 100,
        head: [['#', 'Cant', 'Descripción', 'Fam', 'P.Unit', 'Total', 'Margen']],
        body: tableRows,
        theme: 'grid',
        headStyles: { fillColor: [63, 81, 181], textColor: [255, 255, 255], fontSize: 8, fontStyle: 'bold' },
        styles: { fontSize: 8, cellPadding: 3 },
        columnStyles: {
            0: { cellWidth: 10 },
            1: { cellWidth: 15 },
            3: { cellWidth: 15 },
            4: { cellWidth: 25 },
            5: { cellWidth: 30 },
            6: { cellWidth: 20 }
        }
    });

    // -- Otros Costos Table --
    let currentY = doc.lastAutoTable.finalY + 15;
    doc.setFontSize(11);
    doc.setTextColor(63, 81, 181);
    doc.text('OTROS COSTOS Y ADICIONALES', 20, currentY);

    const otrosRows = Object.entries(otrosCostos)
        .filter(([_, val]) => val > 0)
        .map(([key, val]) => [
            key.replace(/([A-Z])/g, ' $1').toUpperCase(),
            `$${Number(val).toLocaleString('es-CL')}`
        ]);

    if (otrosRows.length > 0) {
        doc.autoTable({
            startY: currentY + 5,
            head: [['Concepto', 'Monto']],
            body: otrosRows,
            theme: 'striped',
            headStyles: { fillColor: [100, 116, 139], fontSize: 8 },
            styles: { fontSize: 8 },
            margin: { left: 20, right: 100 }
        });
        currentY = doc.lastAutoTable.finalY + 15;
    } else {
        doc.setFontSize(9);
        doc.setTextColor(150);
        doc.text('No se registraron costos adicionales.', 20, currentY + 7);
        currentY += 15;
    }

    // -- Commercial Conditions --
    doc.setFontSize(11);
    doc.setTextColor(63, 81, 181);
    doc.text('CONDICIONES COMERCIALES', 20, currentY);

    doc.setFontSize(8);
    doc.setTextColor(50);
    const condBoxY = currentY + 5;
    doc.setDrawColor(230);
    doc.rect(20, condBoxY, 170, 25);

    doc.text(`Anticipo: ${condiciones?.anticipo ?? 50}%`, 25, condBoxY + 7);
    doc.text(`Saldo contra entrega: ${100 - (condiciones?.anticipo ?? 50)}%`, 25, condBoxY + 12);
    doc.text(`Flete: ${condiciones?.flete ?? 'Cliente'}`, 25, condBoxY + 17);
    doc.text(`Plazo de Entrega: ${condiciones?.plazoEntrega ?? 'Lugar a convenir'}`, 100, condBoxY + 7);
    doc.text(`Garantía: ${condiciones?.garantia ?? '30 días de corrido'}`, 100, condBoxY + 12);

    // -- Footer --
    const pageCount = doc.internal.getNumberOfPages();
    for (let i = 1; i <= pageCount; i++) {
        doc.setPage(i);
        doc.setFontSize(8);
        doc.setTextColor(150);
        doc.text(`Generado por ERP Commercial Suite - Página ${i} de ${pageCount}`, 105, 285, { align: 'center' });
    }

    doc.save(`Evaluacion_${id}_${cliente.replace(/ /g, '_')}.pdf`);
};
