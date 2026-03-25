package com.crm.service;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.springframework.stereotype.Service;

import com.crm.entity.Invoice;
import com.crm.entity.InvoiceItem;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.*;
import com.itextpdf.layout.borders.Border;

@Service
public class InvoicePdfService {

    public byte[] generateInvoicePdf(Invoice invoice) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // =============================
            // 🔹 HEADER WITH LOGO
            // =============================
            float[] headerCols = {100, 300};
            Table headerTable = new Table(headerCols).useAllAvailableWidth();

            try {
                String logoPath = "C:/AshokaIt/Inspire-customer-relationship-management/src/main/resources/static/images/logo.png";

                // Debug check
                System.out.println("Logo exists: " + new File(logoPath).exists());

                ImageData imageData = ImageDataFactory.create(logoPath);
                Image logo = new Image(imageData).scaleToFit(80, 80);

                headerTable.addCell(new Cell().add(logo).setBorder(Border.NO_BORDER));

            } catch (Exception e) {
                // fallback if logo fails
                headerTable.addCell(new Cell()
                        .add(new Paragraph("LOGO"))
                        .setBorder(Border.NO_BORDER));
            }

            headerTable.addCell(new Cell()
                    .add(new Paragraph("Inspire Fitness & Spa").setBold().setFontSize(14))
                    .add(new Paragraph("Pune, Maharashtra"))
                    .add(new Paragraph("GSTIN: 27ABCDE1234F1Z5"))
                    .setBorder(Border.NO_BORDER));

            document.add(headerTable);
            document.add(new Paragraph("\n"));

            // =============================
            // 🔹 TITLE
            // =============================
            document.add(new Paragraph("TAX INVOICE")
                    .setBold()
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("\n"));

            // =============================
            // 🔹 CUSTOMER + INVOICE INFO
            // =============================
            Table infoTable = new Table(new float[]{300, 300}).useAllAvailableWidth();

            infoTable.addCell(new Cell()
                    .add(new Paragraph("Bill To:").setBold())
                    .add(new Paragraph("Customer ID: " + invoice.getCustomerId()))
                    .setBorder(Border.NO_BORDER));

            infoTable.addCell(new Cell()
                    .add(new Paragraph("Invoice ID: " + invoice.getId()))
                    .add(new Paragraph("Date: " + invoice.getCreatedAt()))
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBorder(Border.NO_BORDER));

            document.add(infoTable);
            document.add(new Paragraph("\n"));

            // =============================
            // 🔹 ITEMS TABLE
            // =============================
            float[] columnWidths = {200, 80, 80, 100};
            Table table = new Table(columnWidths).useAllAvailableWidth();

            table.addHeaderCell(headerCell("Service"));
            table.addHeaderCell(headerCell("Price"));
            table.addHeaderCell(headerCell("Qty"));
            table.addHeaderCell(headerCell("Total"));

            for (InvoiceItem item : invoice.getItems()) {
                table.addCell(item.getServiceName());
                table.addCell("₹ " + item.getPrice());
                table.addCell(String.valueOf(item.getQuantity()));
                table.addCell("₹ " + item.getTotal());
            }

            document.add(table);
            document.add(new Paragraph("\n"));

            // =============================
            // 🔹 GST SUMMARY
            // =============================
            double gstAmount = invoice.getAmount() * invoice.getGst() / 100;
            double cgst = gstAmount / 2;
            double sgst = gstAmount / 2;

            Table summary = new Table(2)
                    .setWidth(250)
                    .setHorizontalAlignment(HorizontalAlignment.RIGHT);

            summary.addCell(cell("Subtotal"));
            summary.addCell(cell("₹ " + invoice.getAmount()));

            summary.addCell(cell("Discount"));
            summary.addCell(cell(invoice.getDiscount() + " %"));

            summary.addCell(cell("CGST"));
            summary.addCell(cell("₹ " + cgst));

            summary.addCell(cell("SGST"));
            summary.addCell(cell("₹ " + sgst));

            summary.addCell(cell("Final Amount").setBold());
            summary.addCell(cell("₹ " + invoice.getFinalAmount()).setBold());

            document.add(summary);
            document.add(new Paragraph("\n"));

            // =============================
            // 🔹 FOOTER
            // =============================
            document.add(new Paragraph("Thank you for your business!")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setItalic());

            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }

        return out.toByteArray();
    }

    // =============================
    // 🔹 Helper Methods
    // =============================
    private Cell headerCell(String text) {
        return new Cell()
                .add(new Paragraph(text).setBold())
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.CENTER);
    }

    private Cell cell(String text) {
        return new Cell()
                .add(new Paragraph(text))
                .setBorder(Border.NO_BORDER);
    }
}