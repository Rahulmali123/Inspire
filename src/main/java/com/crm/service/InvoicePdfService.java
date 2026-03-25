package com.crm.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.crm.entity.Invoice;
import com.crm.entity.InvoiceItem;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;

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
            Table headerTable = new Table(new float[]{100, 300}).useAllAvailableWidth();

            try {
                String logoPath = "C:/AshokaIt/Inspire-customer-relationship-management/src/main/resources/static/images/logo.png";

                System.out.println("Logo exists: " + new File(logoPath).exists());

                ImageData imageData = ImageDataFactory.create(logoPath);
                Image logo = new Image(imageData).scaleToFit(80, 80);

                headerTable.addCell(new Cell().add(logo).setBorder(Border.NO_BORDER));

            } catch (Exception e) {
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
            Table table = new Table(new float[]{200, 80, 80, 100}).useAllAvailableWidth();

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
            // 🔹 SUMMARY
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
            // 🔹 QR CODE (FIXED)
            // =============================
            String upiData = "upi://pay?pa=rahulmali@oksbi&pn=Inspire&am=" + invoice.getFinalAmount();

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix matrix = qrCodeWriter.encode(upiData, BarcodeFormat.QR_CODE, 200, 200);

            BufferedImage bufferedImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < 200; x++) {
                for (int y = 0; y < 200; y++) {
                    bufferedImage.setRGB(x, y, matrix.get(x, y) ? 0x000000 : 0xFFFFFF);
                }
            }

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "PNG", pngOutputStream);

            Image qrImage = new Image(ImageDataFactory.create(pngOutputStream.toByteArray()))
                    .setWidth(120)
                    .setHeight(120);

            document.add(new Paragraph("Scan to Pay").setBold());
            document.add(qrImage);

            document.add(new Paragraph("\n"));

            // =============================
            // 🔹 SIGNATURE
            // =============================
            Table signTable = new Table(1)
                    .setWidth(200)
                    .setHorizontalAlignment(HorizontalAlignment.RIGHT);

            signTable.addCell(new Cell()
                    .add(new Paragraph("\n\nAuthorized Signature"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBorderTop(new SolidBorder(1)));

            document.add(signTable);

            // =============================
            // 🔹 FOOTER
            // =============================
            document.add(new Paragraph("\nThank you for your business!")
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