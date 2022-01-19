package helloworld;

import org.apache.commons.codec.binary.Base64;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.apache.pdfbox.pdmodel.interactive.form.*;

import javax.xml.XMLConstants;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;

public class EnachSuccessXml {
    private static final PDFont FONT = PDType1Font.HELVETICA;
    private static final float FONT_SIZE = 12;
    private static final float LEADING = -1.5f * FONT_SIZE;

    public static void main(String[] args) throws Exception {
        String fileName = "Form2fillable(1).pdf";
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = new File(classLoader.getResource(fileName).toURI());


        try {
            PDDocument pdDocument = PDDocument.load(file);
            final PDPage page = new PDPage(PDRectangle.A4);
            System.out.println("no of pages : " + pdDocument.getNumberOfPages());
            long curTime = System.currentTimeMillis();
            System.out.println("before loading font " + curTime);
//            PDFont font = PDType1Font.HELVETICA_BOLD;
            PDPageContentStream contentStream = new PDPageContentStream(pdDocument, page, PDPageContentStream.AppendMode.APPEND, false);
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

            pdDocument.addPage(new PDPage());

            //Creating a PDF Document
            PDPage secondpage = pdDocument.getPage(1);
            PDPageContentStream contentStream1 = new PDPageContentStream(pdDocument, secondpage, PDPageContentStream.AppendMode.APPEND, false);


            // add additional text
            //Begin the Content stream
            contentStream1.beginText();

            PDFont font = PDTrueTypeFont.loadTTF(pdDocument,
                    Thread.currentThread().getContextClassLoader().getResourceAsStream("Lato-Regular.ttf"));

//            PDType0Font font = PDType0Font.load(pdDocument,
//                    Thread.currentThread().getContextClassLoader().getResourceAsStream("Lato-Regular.ttf"));

            //Setting the font to the Content stream
//            contentStream1.setFont( PDType1Font.TIMES_ROMAN, 12 );
            contentStream1.setFont( font, 12 );

            //Setting the leading
//            contentStream1.setLeading(14.5f);

            //Setting the position for the line
//            contentStream1.newLineAtOffset(25, 725);

            String text1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><OfflinePaperlessKyc referenceId=\"619820201106213450340\"><UidData><Poi dob=\"05-08-1997\" e=\"a893b09e80a8de4666985976b54f471816e54c0a10efe8738ab275a8200e212c\" gender=\"M\" m=\"b0d59b7af7e8656dfef6e2ced915805cef20719582140d173bf65ff0de6076f6\" name=\"Manojprabhakar M\"/><Poa careof=\"S/O: Manoharan\" country=\"India\" dist=\"Tiruvannamalai\" house=\"631 B\" landmark=\"keezhputhupakkam Extension\" loc=\"cheyyar\" pc=\"604407\" po=\"Cheyyar\" state=\"Tamil Nadu\" street=\"Victory First Street\" subdist=\"Cheyyar\" vtc=\"Cheyyar\"/><Pht>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</Pht></UidData><Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/><SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/><Reference URI=\"\"><Transforms><Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\"/><DigestValue>LakumJZgooN0tpUp1eT+FHx1XOp4i110vbFhWRPUkec=</DigestValue></Reference></SignedInfo><SignatureValue>f6o59bebdfQM2bqGe3qemu4fAZOpbJcPXvoJd+oMID0mdYpLKZJd6n/YuA8sTpWqAcNyMsaY+Dy9" +
                    "nJIn1QRi4fvrACEi2TfCKSltf+drpLJBlEVadL4RgLnBFZ8pGchetAPmgKHYbB55QMPxmqCbmAwt" +
                    "njMn/Oxl185h8nTSdR+8+LteJbNWD5J5Lb8xmmrAYA3TILzskALOt8HYSDKZKrvQhsyvfQuUCvbs" +
                    "eE/2bOzgQz/fyLlSnMkC5ReFm1EvIFSHh9I+U3ph5REJU8MoF1RX1+mmb2X+QxirZboMnbZ+dQNr" +
                    "ApZ82ZfjjYtU4ijMbqKjmKwDvoPpbJrPkLdGTA==</SignatureValue></Signature></OfflinePaperlessKyc>";

            text1 = prettyFormat(text1, 2);
//            text1 = text1.replaceAll("\n", "<br/>");
            System.out.println("text1 " + text1);

//            PDRectangle mediaBox = secondpage.getMediaBox();
//            float marginY = 80;
//            float marginX = 60;
//            float width = mediaBox.getWidth() - 2 * marginX;
//            float startX = mediaBox.getLowerLeftX() + marginX;
//            float startY = mediaBox.getUpperRightY() - marginY;
//
//            addParagraph(contentStream1, width, startX, startY, text1, true);
//            addParagraph(contentStream1, width, 0, -FONT_SIZE, text1);
//            addParagraph(contentStream1, width, 0, -FONT_SIZE, text1, false);

            contentStream1.showText(text1);


            contentStream1.endText();
            contentStream1.close();
            System.out.println("Content added");


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

    private static void addParagraph(PDPageContentStream contentStream, float width, float sx,
                                     float sy, String text) throws IOException {
        addParagraph(contentStream, width, sx, sy, text, false);
    }

    private static void addParagraph(PDPageContentStream contentStream, float width, float sx,
                                     float sy, String text, boolean justify) throws IOException {
        List<String> lines = parseLines(text, width);
        contentStream.setFont(FONT, FONT_SIZE);
        contentStream.newLineAtOffset(sx, sy);
        for (String line: lines) {
            float charSpacing = 0;
            if (justify){
                if (line.length() > 1) {
                    float size = FONT_SIZE * FONT.getStringWidth(line) / 1000;
                    float free = width - size;
                    if (free > 0 && !lines.get(lines.size() - 1).equals(line)) {
                        charSpacing = free / (line.length() - 1);
                    }
                }
            }
            contentStream.setCharacterSpacing(charSpacing);
            contentStream.showText(line);
            contentStream.newLineAtOffset(0, LEADING);
        }
    }

    private static List<String> parseLines(String text, float width) throws IOException {
        List<String> lines = new ArrayList<String>();
        int lastSpace = -1;
        while (text.length() > 0) {
            int spaceIndex = text.indexOf(' ', lastSpace + 1);
            if (spaceIndex < 0)
                spaceIndex = text.length();
            String subString = text.substring(0, spaceIndex);
            float size = FONT_SIZE * FONT.getStringWidth(subString) / 1000;
            if (size > width) {
                if (lastSpace < 0){
                    lastSpace = spaceIndex;
                }
                subString = text.substring(0, lastSpace);
                lines.add(subString);
                text = text.substring(lastSpace).trim();
                lastSpace = -1;
            } else if (spaceIndex == text.length()) {
                lines.add(text);
                text = "";
            } else {
                lastSpace = spaceIndex;
            }
        }
        return lines;
    }

    public static String prettyFormat(String input, int indent) {
        try {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", indent);
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Exception e) {
            throw new RuntimeException(e); // simple exception handling, please review it
        }
    }

//    private static String beautifyXml(String xml) throws TransformerException {
//        Transformer transformer = TransformerFactory.newInstance().newTransformer();
//        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
//        // initialize StreamResult with File object to save to file
//        StreamResult result = new StreamResult(new StringWriter());
//        DOMSource source = new DOMSource(doc);
//        transformer.transform(source, result);
//        String xmlString = result.getWriter().toString();
//        System.out.println(xmlString);
//        return xmlString;
//    }

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
                if (pDField instanceof PDCheckBox) {
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

                    if (key.equalsIgnoreCase(COMPANY_LOGO)) {
                        imageFile = new File(ClassLoader.getSystemResource("digitap-logo.png").toURI());
                        insertImage(pdDocument, pDField, imageFile, 30, 150);
                    } else if (key.equalsIgnoreCase(USER_IMG)) {
//                        imageFile = new File(ClassLoader.getSystemResource("Manoj(original).jpg").toURI());
                        imageFile = new File(ClassLoader.getSystemResource("img_avatar.png").toURI());
                        insertImage(pdDocument, pDField, imageFile, 156, 130);
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

        mapValue.put("gromor_ref_no", "55503");
        mapValue.put("referenceid", "ref id\n123 what is the natural nmae of the world the anturla nmae of the world is the natural name of " +
                "the word is the natura name of theworl is the naturlname of the world");
        mapValue.put("name", "ManojPrabhakar");
        mapValue.put("dob", "05-08-1997");
        mapValue.put("careof", "Manoharan");
        mapValue.put("gender", "Male");
        mapValue.put("landmark", "opp to marimuthu rice mill");
        mapValue.put("house", "631-B");
        mapValue.put("street", "Victory First Street");
        mapValue.put("pc", "6044407");
        mapValue.put("po", "Tiruvettipuram");
        mapValue.put("district", "Tiruvannamalai");
        mapValue.put("state", "Tamil Nadu");
        mapValue.put("country", "India");
        mapValue.put("maskedaadhaarnumber", "xxxx xxxx 6198");
        mapValue.put("uniqueid", "2532432");
        mapValue.put("mobile", "8122949665");
        mapValue.put("email", "prabha05081997@gmail.com");
        mapValue.put("date", "22-10-2020");
        mapValue.put("company_logo", "");
        mapValue.put("user_photo", "");

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

//        String qrCodeText = CommonConstants.NACH_ID + nachId + "" +
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
