package helloworld.itext;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
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
import java.util.*;

public class FisdomItext {
    public static final String DEST = "/tmp/fisdom.pdf";
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
        System.out.println("---------------------------------------------");
        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);

        var mapValue = new HashMap<String, String>();

        mapValue.put("maiden_name", "55503");
        mapValue.put("mother_name", "ref id");
        mapValue.put("father_name", "ManojPrabhakar");
        mapValue.put("dob", "05-08-1997");
        mapValue.put("passport_number", "Manoharan");
        mapValue.put("passport_expiry_date", "Male");
        mapValue.put("voter_id_number", "opp to marimuthu rice mill");
        mapValue.put("pan", "631-B");
        mapValue.put("dl_number", "Victory First Street");
        mapValue.put("nrega_number", "6044407");
        mapValue.put("nri_country_code", "Tiruvettipuram");
        mapValue.put("nri_tax_identification", "Tiruvannamalai");
        mapValue.put("permanent_address_city", "Tamil Nadu");
        mapValue.put("permanent_address_district", "India");
        mapValue.put("permanent_address_state", "xxxx xxxx 6198");
        mapValue.put("permanent_address_country", "2532432");
        mapValue.put("permanent_address_countrycode", "8122949665");
        mapValue.put("corres_address_line1", "prabha05081997@gmail.com");
        mapValue.put("corres_address_line2", "22-10-2020");
        mapValue.put("corres_address_line3", "address line 3");
        mapValue.put("corres_address_city", "address city");
        mapValue.put("corres_address_district", "55503");
        mapValue.put("corres_address_state", "ref id");
        mapValue.put("corres_address_country", "ManojPrabhakar");
        mapValue.put("corres_address_country_code", "Manoharan");
        mapValue.put("nri_address", "Male");
        mapValue.put("nri_city", "opp to marimuthu rice mill");
        mapValue.put("nri_state", "631-B");
        mapValue.put("nri_pincode", "55503");
        mapValue.put("nri_countrycode", "ref id");
        mapValue.put("nri_country_birth_code", "code_unique_value");
        mapValue.put("nri_place_birth", checkForNull(null));
        mapValue.put("name", "First Mp");
        mapValue.put("name.1", "Second Mp");
        mapValue.put("name.2", "third MP");
        mapValue.put("name.3", "fourth MP");
        mapValue.put("date", "05-08-1997");
        mapValue.put("date.1", "05-08-1997");
        mapValue.put("date.2", "05-08-1997");
        mapValue.put("ipv_employee_name", "Manoharan");
        mapValue.put("ipv_employee_code", "Male");
        mapValue.put("ipv_employee_designation", "opp to marimuthu rice mill");
        mapValue.put("ipv_employee_branch", "631-B");
        mapValue.put("mobile_number", "55503");
        mapValue.put("email_address", "ref id");
        mapValue.put("city", "cheyyar");
        mapValue.put("aadhaar_number", "05-08-1997");
        mapValue.put("#c.gender.male", "");
        mapValue.put("#c.gender.female", "");
        mapValue.put("#c.gender.others", "");

        mapValue.put("#c.marital_status.single", "");
        mapValue.put("#c.marital_status.married", "");
        mapValue.put("#c.marital_status.others", "");

        mapValue.put("#c.resident_status.ri", "");
        mapValue.put("#c.resident_status.nri", "");

        mapValue.put("#c.occupation.service", "");
        mapValue.put("#c.occupation.private_sector", "");
        mapValue.put("#c.occupation.public_sector", "");
        mapValue.put("#c.occupation.government_sector", "");
        mapValue.put("#c.occupation.business", "");
        mapValue.put("#c.occupation.uncategorised", "");
        mapValue.put("#c.occupation.others", "");
        mapValue.put("#c.occupation.professional", "");
        mapValue.put("#c.occupation.self_employed", "");
        mapValue.put("#c.occupation.retired", "");
        mapValue.put("#c.occupation.housewife", "");
        mapValue.put("#c.occupation.student", "");

        mapValue.put("#c.tax_outside_india.yes", "");

        mapValue.put("#c.poi.passport", "");
        mapValue.put("#c.poi.voter_id", "");
        mapValue.put("#c.poi.pan", "");
        mapValue.put("#c.poi.dl", "");
        mapValue.put("#c.poi.aadhaar", "");
        mapValue.put("#c.poi.nrega", "");

        mapValue.put("#c.permanent_address_type.both", "");
        mapValue.put("#c.permanent_address_type.residential", "");
        mapValue.put("#c.permanent_address_type.business", "");
        mapValue.put("#c.permanent_address_type.registered_office", "");
        mapValue.put("#c.permanent_address_type.unspecified", "");

        mapValue.put("#c.poa.passport", "");
        mapValue.put("#c.poa.voterid", "");
        mapValue.put("#c.poa.dl", "");
        mapValue.put("#c.poa.aadhaar", "");
        mapValue.put("#c.poa.nrega", "");

        mapValue.put("#c.corres_address.passport", "");
        mapValue.put("#c.corres_address.voter_id", "");
        mapValue.put("#c.corres_address.dl", "");
        mapValue.put("#c.corres_address.aadhaar", "");
        mapValue.put("#c.corres_address.nrega", "");

        mapValue.put("#c.nri_tax_address.same_as_permanent", "");
        mapValue.put("#c.nri_tax_address.same_as_correspondence", "");

        mapValue.put("#c.related_person.add", "");
        mapValue.put("#c.related_person.delete", "");
        mapValue.put("#c.related_person.guardian", "");
        mapValue.put("#c.related_person.assignee", "");
        mapValue.put("#c.related_person.representative", "");

        mapValue.put("#c.doc_received.certified_copies", "");
        mapValue.put("#c.doc_received.offline", "");
        mapValue.put("#c.doc_received.edocument", "");
        mapValue.put("#c.doc_received.ekyc", "");
        mapValue.put("#c.doc_received.digital_kyc", "");
        mapValue.put("#c.doc_received.video_kyc", "");

        mapValue.put("#c.correspondence_address_sameas_permanent.yes", "");

        mapValue.put("#b.permanent_address#1|length:20", "abc");
        mapValue.put("#b.permanent_address#2|length:20", "dfd");
        mapValue.put("#b.permanent_address#3|length:20", "sdfds");

        mapValue.put("#i.selfie_af_image", "");
        mapValue.put("#i.sign_af_image", "");
        mapValue.put("#i.sign_af_image.1", "");
        mapValue.put("#i.sign_af_image.2", "");
//        mapValue.put("#i.pan_af_image", "");
        mapValue.put("#i.poa_af_image", "");
        mapValue.put("#i.employee_sign_af_image", "");
        mapValue.put("#i.company_seal_af_image", "");
        mapValue.put("#i.company_seal_af_image.1", "");
        mapValue.put("#i.company_seal_af_image.2", "");
        mapValue.put("#i.company_seal_af_image.3", "");
        mapValue.put("#i.company_seal_af_image.4", "");
        mapValue.put("#i.company_seal_af_image.5", "");
        mapValue.put("#i.osv_seal_af_image", "");
        mapValue.put("#i.osv_seal_af_image.1", "");
        mapValue.put("#i.osv_seal_af_image.2", "");
        mapValue.put("#i.osv_seal_af_image.3", "");
//        mapValue.put("#i.osv_seal_af_image.4", "");


        Set<String> resultSet = mapValue.keySet();
        for(String key : resultSet) {
            PdfFormField pdfFormField = form.getField(key);
            if(pdfFormField != null) {
                PdfName type = pdfFormField.getFormType();
                if(PdfName.Btn.compareTo(type) == 0) {
                    System.out.println("button identified " + key);
                    String[] appearanceStates = pdfFormField.getAppearanceStates();
                    System.out.println(key + " " + Arrays.toString(appearanceStates));
                    if(appearanceStates.length > 0) {
                        pdfFormField.setCheckType(1);
                        pdfFormField.setValue("Yes");
                    } else {
                        pdfFormField.setValue(getBase64(new File(ClassLoader.getSystemResource("Manoj(original).jpg").toURI())));
                    }
                } else if (PdfName.Ch.compareTo(type) == 0) {
                    System.out.println("checkbox identified " + key);
                    pdfFormField.setCheckType(1);
                    pdfFormField.setValue("Yes");
                    pdfFormField.setBorderColor(ColorConstants.BLACK);
                } else if (PdfName.Sig.compareTo(type) == 0) {
                    System.out.println("Signature");
                } else if (PdfName.Tx.compareTo(type) == 0) {
                    pdfFormField.setValue(mapValue.get(key));
                }else {
                    System.out.println("?????");
                }
//                pdfFormField.setFont(font);
//                pdfFormField.setFontSize(6);
            }
        }
        form.flattenFields();

        doc.close();
        pdfDoc.close();
    }

    public static String checkForNull(String str) {
        return str != null ? str : "";
    }

    public static String getBase64(File file) throws IOException {
        byte[] fileContent = FileUtils.readFileToByteArray(file);
        String encodedString = Base64.getEncoder().encodeToString(fileContent);

        return encodedString;
    }

    public static String getBase64(String filePath) throws IOException {
        byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
        String encodedString = Base64.getEncoder().encodeToString(fileContent);

        return encodedString;
    }
}
