package com.bank.services;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.dtos.EmailDetails;
import com.bank.entities.AccountOperation;
import com.bank.entities.BankAccount;
import com.bank.entities.Customer;
import com.bank.repositories.AccountOperationRepository;
import com.bank.repositories.BankAccountRepository;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BankStatement {

    @Autowired
    private AccountOperationRepository accountOperationRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private EmailService emailService;

    private static final String FILE = "D:\\Account_Statement.pdf";
    
    public List<AccountOperation> generateStatement(String accountNumber) throws ParseException, FileNotFoundException, DocumentException {
        
        List<AccountOperation> operationList = accountOperationRepository.findByBankAccountId(accountNumber);

        Customer customer = bankAccountRepository.findById(accountNumber).orElseThrow().getCustomer();
        BankAccount bankAccount = bankAccountRepository.findById(accountNumber).orElseThrow();

        Document document = createDocument(FILE);

        addBankInfo(document);
        addCustomerInfo(document, customer.getName(), accountNumber, bankAccount.getBalance());
        addStatementHeading(document);
        addTransactionTable(document, operationList);

        document.close();

        sendEmailWithStatement(customer.getEmail());

        return operationList;
    }

    private Document createDocument(String filePath) throws FileNotFoundException, DocumentException {
        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);
        OutputStream outPutStream = new FileOutputStream(filePath);
        PdfWriter.getInstance(document, outPutStream);
        document.open();
        return document;
    }

    private void addBankInfo(Document document) throws DocumentException {
        PdfPTable bankInfoTable = new PdfPTable(1);
        PdfPCell bankName = createCell("SUNBANK", new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.WHITE));
        bankName.setBackgroundColor(BaseColor.DARK_GRAY);
        bankName.setPadding(20f);

        PdfPCell bankAddress = createCell("Sunbeam, Karad-413511\n\n", new Font(Font.FontFamily.HELVETICA, 12));
        bankAddress.setBorder(0);

        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);
        document.add(bankInfoTable);
    }

    private void addCustomerInfo(Document document, String customerName, String accountNumber, double balance) throws DocumentException {
        PdfPTable customerInfoTable = new PdfPTable(2);
        customerInfoTable.setSpacingBefore(10f);

        customerInfoTable.addCell(createCell("Customer Name: " + customerName));
        customerInfoTable.addCell(createCell("Account Number: " + accountNumber+"\n\n Current Balance: " + balance));
        customerInfoTable.addCell(createCell("Current Balance: " + balance));
        document.add(customerInfoTable);
    }

    private void addStatementHeading(Document document) throws DocumentException {
        PdfPTable headingTable = new PdfPTable(1);
        headingTable.setSpacingBefore(20f);

        PdfPCell statement = createCell("STATEMENT OF ACCOUNT", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.BLUE));
        statement.setHorizontalAlignment(Element.ALIGN_CENTER);
        statement.setPadding(10f);

        headingTable.addCell(statement);
        document.add(headingTable);
    }

    private void addTransactionTable(Document document, List<AccountOperation> operationList) throws DocumentException {
        PdfPTable transactionsTable = new PdfPTable(4);
        transactionsTable.setSpacingBefore(20f);

        transactionsTable.addCell(createHeaderCell("Transaction ID"));
        transactionsTable.addCell(createHeaderCell("Date"));
        transactionsTable.addCell(createHeaderCell("Transaction Type"));
        transactionsTable.addCell(createHeaderCell("Amount"));

        for (AccountOperation operation : operationList) {
            transactionsTable.addCell(createCell(operation.getId().toString()));
            transactionsTable.addCell(createCell(operation.getOperationDate().toString()));
            transactionsTable.addCell(createCell(operation.getType().toString()));
            transactionsTable.addCell(createCell(String.valueOf(operation.getAmount())));
        }

        document.add(transactionsTable);
    }

    private PdfPCell createCell(String content) {
        return createCell(content, new Font(Font.FontFamily.HELVETICA, 12));
    }

    private PdfPCell createCell(String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setBorder(0);
        return cell;
    }

    private PdfPCell createHeaderCell(String content) {
        PdfPCell headerCell = createCell(content, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE));
        headerCell.setBackgroundColor(BaseColor.ORANGE);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setPadding(8f);
        return headerCell;
    }

    private void sendEmailWithStatement(String recipientEmail) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(recipientEmail)
                    .subject("Statement of Account")
                    .messageBody("Please find your account statement attached.")
                    .attachment(FILE)
                    .build();

            emailService.sendEmailwithAttachment(emailDetails);
        });
        executorService.shutdown();
    }
}


