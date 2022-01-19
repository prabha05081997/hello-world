package helloworld;

import org.apache.commons.codec.binary.Base64;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.form.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LoanAgreementMinkpay {
    public static void main(String[] args) throws Exception {
        String fileName = "unsigned_agreement_v6.pdf";
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = new File(classLoader.getResource(fileName).toURI());


        try {
            PDDocument pdDocument = PDDocument.load(file);
            final PDPage page = new PDPage(PDRectangle.A4);
            System.out.println("no of pages : " + pdDocument.getNumberOfPages());
            long curTime = System.currentTimeMillis();
            System.out.println("before loading font " + curTime);
//            PDFont font = PDType1Font.HELVETICA_BOLD;
            PDPageContentStream contentStream = new PDPageContentStream(pdDocument, page);
//            contentStream.setFont(font, 1);
            System.out.println("time taken to load font " + (System.currentTimeMillis() - curTime));
            PDAcroForm pDAcroForm = pdDocument.getDocumentCatalog().getAcroForm();

            var mapValues = getPdfFormData();

            System.out.println("pdDocument " + pdDocument);
            System.out.println("pdAcroForm " + pDAcroForm);
            System.out.println("mapValues " + mapValues);

            if(pDAcroForm == null) {
                System.out.println("pd acro form is NULL");
                pdDocument.getDocumentCatalog().setAcroForm(pDAcroForm = new PDAcroForm(pdDocument));
            }

            getMandateForm(pdDocument, pDAcroForm, mapValues);

            pDAcroForm.flatten();

            var outputStream = new ByteArrayOutputStream();
            PDDocumentInformation pdDocumentInformation = new PDDocumentInformation();
            pdDocument.setDocumentInformation(pdDocumentInformation);

            contentStream.close();
            pdDocument.save(outputStream);
            pdDocument.save("updatedPDF.pdf");
            pdDocument.close();

            var inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            byte[] buf = new byte[1024];
            int n = 0;

            while ((n = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, n);
            }

            byte[] response = outputStream.toByteArray();

            outputStream.close();
            inputStream.close();

            String base64 = Base64.encodeBase64String(response);
            System.out.println("File Saved Successfully");

        } catch (IOException e) {
            System.out.println("Error while generating template - " + e);
            throw new Exception(e);
        }
    }


    private static void getMandateForm(PDDocument pdDocument, PDAcroForm pdAcroForm, Map<String, String> valueMap) throws IOException, URISyntaxException {
        System.out.println("In getMandateForm ");
        Set<String> keys = valueMap.keySet();
        System.out.println(pdAcroForm.getFields().get(0));

        for(PDField pdField : pdAcroForm.getFields()) {
            System.out.println(pdField + " " + pdField.getFullyQualifiedName() + " " +
                    pdField.getFieldType());
        }
        for (String key : keys) {
            System.out.println("Key = " + key);
            PDField pDField = pdAcroForm.getField(key);

            if (pDField != null) {
                if (pDField instanceof PDTextField) {
                    ((PDTextField) pDField).setDefaultAppearance("/Helv 10 Tf 0 g");
                    System.out.print("(type: " + pDField.getClass().getSimpleName() + ")" + " , field name : "
                            + pDField.getFullyQualifiedName());
                    long curTime = System.currentTimeMillis();
                    pDField.setValue(valueMap.get(key));
                    System.out.println(" time taken " + (System.currentTimeMillis() - curTime));
                }
            } else {
                System.out.println("Key not found = " + key);
                System.out.println("Key not found = " + key);
            }

        }
        System.out.println("Return from getMandateForm");
    }


    private static Map<String, String> getPdfFormData() {
        System.out.println("In getPdfFormData");

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
        mapValue.put("credit_limit_words", "One Lakh Rupees");
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

        System.out.println("Return from getPdfFormData");
        return mapValue;
    }

}
