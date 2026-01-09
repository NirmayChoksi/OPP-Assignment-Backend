package com.oopAssignment.financeTracker.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.oopAssignment.financeTracker.model.Transaction;

@Service
public class PdfService {

    public ByteArrayInputStream generateTransactionsPdf(List<Transaction> transactions) {

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Transaction History", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" ")); // Add space

            // Table
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new int[] { 10, 15, 20, 20, 35 });

            addTableHeader(table);
            addRows(table, transactions);

            document.add(table);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("ID", "Type", "Amount", "Date", "Note")
                .forEach(column -> {
                    PdfPCell header = new PdfPCell();
                    header.setPhrase(new Phrase(column));
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table, List<Transaction> transactions) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Transaction tx : transactions) {
            table.addCell(tx.getId());
            table.addCell(tx.getType().toString());
            table.addCell(tx.getAmount().toString());
            table.addCell(tx.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).format(formatter));
            table.addCell(tx.getNote() != null ? tx.getNote() : "");
        }
    }
}
