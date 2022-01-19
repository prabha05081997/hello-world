package helloworld.itext;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import org.apache.commons.io.FileUtils;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LoanAgreementMinksPay1 {
    public static final String DEST = "/tmp/minkspay1.pdf";
    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        manipulatePdf(DEST);
    }

    protected static void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("fisdom-kyc.pdf")), new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(PageSize.Default), true);
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        // Being set as true, this parameter is responsible to generate an appearance Stream
        // while flattening for all form fields that don't have one. Generating appearances will
        // slow down form flattening, but otherwise Acrobat might render the pdf on its own rules.
        form.setGenerateAppearance(true);

//        System.out.println(form.getFormFields());
        Map<String, PdfFormField> formFields = form.getFormFields();
        Set<String> formKeys = formFields.keySet();
        for(String key : formKeys) {
            System.out.println(key);
        }
        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);

        var mapValue = new HashMap<String, String>();

        mapValue.put("gromor_ref_no", "12345");
        mapValue.put("borrower_contact _name", "Yuvraj Maruti Dhumal");
        mapValue.put("date_of_sanction", "22-12-2021");
        mapValue.put("borrower_details", "test_business, a proprietorship firm having its office at Koramangala Bengaluru Bangalore North - 560010, Karnataka\n" +
                "represented by its Proprietor, Yuvraj Maruti Dhumal, having Permanent Account Number TEST1234NO and residing at\n" +
                "Koramangala Bengaluru Old Goa - 560010, Karnataka.7");
        mapValue.put("borrower_state", "Karnataka");
        mapValue.put("distributor_name", "Some business");
        mapValue.put("credit_limit", "10000/-");
        mapValue.put("credit_limit_words", "three lakh Rupees");
        mapValue.put("last_disbursement_request_date", "22-11-2021");
        mapValue.put("interest_rate", "2.33% per month");
        mapValue.put("borrower_name", "test_business");
        mapValue.put("borrower_address", "Koramangala Bengaluru Bangalore North - 560010, Karnataka");
        mapValue.put("borrower_city", "Bangalore North");
        mapValue.put("borrower_pincode", "560010");
        mapValue.put("borrower_role", "Proprietor");
        mapValue.put("credit_term", "Maximum 30 days");
        mapValue.put("borrower_firm_type", "Proprietor");
        mapValue.put("borrower_gst_no", "adASDASD");
        mapValue.put("borrower_email", "testmail9900957087@gmail.co");
        mapValue.put("borrower_mobile", "1231231231");
        mapValue.put("borrower_poi", "Voter Id: TEST123VIDNUM");
        mapValue.put("borrower_poa", "Adhar No.:");
        mapValue.put("borrower_bank", "HDFC BANK");
        mapValue.put("borrower_ac_no","50100125307260");
        mapValue.put("borrower_ifsc","HDFC0000446");

        Set<String> resultSet = mapValue.keySet();
        for(String key : resultSet) {
            PdfFormField pdfFormField = form.getField(key);
            if(pdfFormField != null) {
                pdfFormField.setValue(mapValue.get(key));
//                pdfFormField.setFont(font);
//                pdfFormField.setFontSize(6);
            }
        }
        form.flattenFields();

        doc.close();
        pdfDoc.close();
    }
}
