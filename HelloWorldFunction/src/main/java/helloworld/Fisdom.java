package helloworld;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.apache.pdfbox.pdmodel.interactive.form.*;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;

public class Fisdom {
    public static void main(String[] args) throws Exception {
        String FileName = "";
        FileName = "fisdom-kyc.pdf";
        String fileName = FileName;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        System.out.println("class loader : " + classLoader);
        System.out.println("-------------------------------------");
        System.out.println(classLoader.getResource(fileName));
        System.out.println(classLoader.getResource(fileName).getFile());
        System.out.println("-------------------------------------");
        File file = new File(classLoader.getResource(fileName).toURI());

        System.out.println("File path : " + file);

        try {
            PDDocument pdDocument = PDDocument.load(file);
//            PDDocument pdDocument = PDDocument.load(new FileInputStream("/home/manojprabhakar/dev/Hello World/HelloWorldFunction/target/classes/Form.pdf"));
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

            getMandateForm(pdDocument, pDAcroForm, mapValues, "");

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

//        PDDocument document = new PDDocument();
//        PDPage page = new PDPage();
//        document.addPage(page);
//
//        PDPageContentStream contentStream = new PDPageContentStream(document, page);
//
//        contentStream.setFont(PDType1Font.COURIER, 12);
//        contentStream.beginText();
//        contentStream.showText("Hello World");
//        contentStream.endText();
//
//        Path path = Paths.get(ClassLoader.getSystemResource("digitap-logo.png").toURI());
//        PDPageContentStream contentStream = new PDPageContentStream(document, page);
//        PDImageXObject image
//                = PDImageXObject.createFromFile(path.toAbsolutePath().toString(), document);
//        contentStream.drawImage(image, 0, 0);
//
//        contentStream.close();
//
//        document.save("pdfBoxHelloWorld.pdf");
//        document.close();
    }

    private static void getMandateForm(PDDocument pdDocument, PDAcroForm pdAcroForm, Map<String, String> valueMap, String nachID) throws IOException, URISyntaxException {
        System.out.println("In getMandateForm ");

        Set<String> keys = valueMap.keySet();

//        System.out.println(pdAcroForm.getFields().get(0));
        System.out.println("fields inside pdf");
        for(PDField pdField : pdAcroForm.getFields()) {
            System.out.println(pdField + " " + pdField.getFullyQualifiedName() + " " +
                    pdField.getFieldType());
        }
        for (String key : keys) {
            System.out.println("Key = " + key);
            PDField pDField = pdAcroForm.getField(key);

            if (pDField != null) {
//                System.out.println("(type: " + pDField.getClass().getSimpleName() + ")" + " , field name : "
//                        + pDField.getFullyQualifiedName());
                if (pDField instanceof PDNonTerminalField) {
                    System.out.println("(type: " + pDField.getClass().getSimpleName() + ")" + " , field name : "
                            + pDField.getFullyQualifiedName());
                    pDField.setValue(valueMap.get(key));
                } else if (pDField instanceof PDCheckBox) {
                    System.out.println("(type: " + pDField.getClass().getSimpleName() + ")" + " , field name : "
                            + pDField.getFullyQualifiedName());
                    pDField.setValue(valueMap.get(key));
                } else if (pDField instanceof PDTextField) {
//                    ((PDTextField) pDField).setDefaultAppearance("/Helv 10 Tf 0 g");
                    System.out.print("(type: " + pDField.getClass().getSimpleName() + ")" + " , field name : "
                            + pDField.getFullyQualifiedName());
                    long curTime = System.currentTimeMillis();
                    pDField.setValue(valueMap.get(key));
                    System.out.println("time taken " + (System.currentTimeMillis() - curTime));
                } else if (pDField instanceof PDPushButton) {
                    System.out.println("PDPushButton");
                    String MANDATE_LOGO = "Logo_af_image";
                    String MANDATE_QR_CODE = "qr code";
                    String USER_IMG = "user_photo";
                    String COMPANY_LOGO = "company_logo";
                    File imageFile = null;
                    float uppeRightX = pDField.getWidgets().get(0).getRectangle().getUpperRightX();
                    float width = pDField.getWidgets().get(0).getRectangle().getWidth();
                    float height = pDField.getWidgets().get(0).getRectangle().getHeight();
                    imageFile = new File(ClassLoader.getSystemResource("Manoj(original).jpg").toURI());
                    insertImage(pdDocument, pDField, imageFile, (int)height*2, (int)width*2);

                    if (key.equalsIgnoreCase(COMPANY_LOGO)) {
                        imageFile = new File(ClassLoader.getSystemResource("digitap-logo.png").toURI());
                        insertImage(pdDocument, pDField, imageFile, 30, 150);
                    } else if (key.equalsIgnoreCase(USER_IMG)) {
                        imageFile = new File(ClassLoader.getSystemResource("Manoj(original).jpg").toURI());
                        insertImage(pdDocument, pDField, imageFile, 156, 130);
//                        imageFile = new File(ClassLoader.getSystemResource("img_avatar.png").toURI());
//                        insertImage(pdDocument, pDField, imageFile, 156, 130);
                    }
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
        mapValue.put("corres_address_line3", "");
        mapValue.put("corres_address_city", "");
        mapValue.put("corres_address_district", "55503");
        mapValue.put("corres_address_state", "ref id");
        mapValue.put("corres_address_country", "ManojPrabhakar");
        mapValue.put("dcorres_address_countryob", "05-08-1997");
        mapValue.put("corres_address_country_code", "Manoharan");
        mapValue.put("nri_address", "Male");
        mapValue.put("nri_city", "opp to marimuthu rice mill");
        mapValue.put("nri_state", "631-B");
        mapValue.put("nri_pincode", "55503");
        mapValue.put("nri_countrycode", "ref id");
        mapValue.put("name", "ManojPrabhakar");
        mapValue.put("date", "05-08-1997");
        mapValue.put("ipv_employee_name", "Manoharan");
        mapValue.put("ipv_employee_code", "Male");
        mapValue.put("ipv_employee_designation", "opp to marimuthu rice mill");
        mapValue.put("ipv_employee_branch", "631-B");
        mapValue.put("mobile_number", "55503");
        mapValue.put("email_address", "ref id");
        mapValue.put("city", "cheyyar");
        mapValue.put("aadhaar_number", "05-08-1997");
        mapValue.put("#c.gender", "male");
        mapValue.put("#c.gender.male", "Yes");
        mapValue.put("#i.selfie_af_image", "");
//        mapValue.put("ipv_employee_name", "Manoharan");
//        mapValue.put("ipv_employee_code", "Male");
//        mapValue.put("ipv_employee_designation", "opp to marimuthu rice mill");
//        mapValue.put("ipv_employee_branch", "631-B");
//        mapValue.put("qr code", "");
//        mapValue.put("Logo_af_image", "");

        System.out.println("Return from getPdfFormData");
        return mapValue;
    }

    private static void insertImage(PDDocument pdDocument, PDField pDField, File imageFile, int a, int b) throws IOException, URISyntaxException {

        System.out.println("In insertImage");

        PDAnnotationWidget annotationWidget;

        System.out.println("(type: " + pDField.getClass().getSimpleName() + ")" + ", field name : "
                + pDField.getFullyQualifiedName());

        PDPushButton pdPushButton = (PDPushButton) pDField;
        List<PDAnnotationWidget> widgets = pdPushButton.getWidgets();

//        String qrCodeText = CommonConstants.NACH_ID + nachId + "\n" +
//                CommonConstants.CRN + CommonConstants.CRN_NO + CRN_NO;

//        File imageFile = null;
//        imageFile = new File(ClassLoader.getSystemResource("digitap-logo.png").toURI());
        if (widgets != null && widgets.size() > 0) {
            annotationWidget = widgets.get(0);
//            try {
//                File tempFile = File.createTempFile("temp", ".png");
//                imageFile = QRCodeGenerator.createQRImage(tempFile, qrCodeText, 125, "png");
//            } catch (WriterException e) {
//                e.printStackTrace();
//            }

            if (imageFile != null && imageFile.exists()) {
                PDImageXObject pdImageXObject = PDImageXObject.createFromFile(imageFile.getAbsolutePath(), pdDocument);
                float imageScaleRatio = (float) pdImageXObject.getHeight() / (float) pdImageXObject.getWidth();

                PDRectangle buttonPosition = getFieldArea(pdPushButton);
                float height = buttonPosition.getHeight() + 50;
                float width = height / imageScaleRatio;
                float x = buttonPosition.getHeight() - a;
                float y = buttonPosition.getWidth() - b;

                PDAppearanceStream pdAppearanceStream = new PDAppearanceStream(pdDocument);
                pdAppearanceStream.setResources(new PDResources());
                try (PDPageContentStream pdPageContentStream = new PDPageContentStream(pdDocument,
                        pdAppearanceStream)) {
                    pdPageContentStream.drawImage(pdImageXObject, x, y, width, height);
                }
                pdAppearanceStream.setBBox(new PDRectangle(x, y, width, height));

                PDAppearanceDictionary pdAppearanceDictionary = annotationWidget.getAppearance();
                if (pdAppearanceDictionary == null) {
                    pdAppearanceDictionary = new PDAppearanceDictionary();
                    annotationWidget.setAppearance(pdAppearanceDictionary);
                }
                pdAppearanceDictionary.setNormalAppearance(pdAppearanceStream);
            }
        }
        System.out.println("Return from insertImage");
    }

    public static PDRectangle getFieldArea(PDField field) {

        System.out.println("enter into getFieldArea");
        COSDictionary fieldDict = field.getCOSObject();

        COSArray fieldAreaArray = (COSArray) fieldDict.getDictionaryObject(COSName.RECT);

        System.out.println("Return from getFieldArea");
        return new PDRectangle(fieldAreaArray);

    }
}
