package helloworld;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceCharacteristicsDictionary;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class CreateSimpleFormWithEmbeddedFont {
    private CreateSimpleFormWithEmbeddedFont() { }
            public static void main(String[] args) throws IOException {
	                    // Create a new document with an empty page.
                try (PDDocument doc = new PDDocument())
        	        {
                        PDPage page = new PDPage(PDRectangle.A4);
                        doc.addPage(page);
                        PDAcroForm acroForm = new PDAcroForm(doc);
                        doc.getDocumentCatalog().setAcroForm(acroForm);
                                    // Note that the font is fully embedded. If you use a different font, make sure that
                        // its license allows full embedding.
//                        InputStream in = CreateSimpleFormWithEmbeddedFont.class.getResourceAsStream("Roboto-Regular.ttf");
                        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("Roboto-Regular.ttf");
                        System.out.println(in);
                        URL url = Thread.currentThread().getContextClassLoader().getResource("unsigned_agreement_v1.pdf");
                        System.out.println(url);

                        PDFont formFont = PDType0Font.load(doc, in, false);

                        // Add and set the resources and default appearance at the form level
                        final PDResources resources = new PDResources();
                        acroForm.setDefaultResources(resources);
                        final String fontName = resources.add(formFont).getName();
                                    // Acrobat sets the font size on the form level to be
                        // auto sized as default. This is done by setting the font size to '0'
                        acroForm.setDefaultResources(resources);
                        String defaultAppearanceString = "/" + fontName + " 0 Tf 0 g";
                                    PDTextField textBox = new PDTextField(acroForm);
                        textBox.setPartialName("SampleField");
                        textBox.setDefaultAppearance(defaultAppearanceString);
                        acroForm.getFields().add(textBox);
                                    // Specify the widget annotation associated with the field
                        PDAnnotationWidget widget = textBox.getWidgets().get(0);
                        PDRectangle rect = new PDRectangle(50, 700, 200, 50);
                        widget.setRectangle(rect);
                        widget.setPage(page);
                        page.getAnnotations().add(widget);
                                    // set green border and yellow background
                        // if you prefer defaults, delete this code block
                        PDAppearanceCharacteristicsDictionary fieldAppearance
                                = new PDAppearanceCharacteristicsDictionary(new COSDictionary());
                        fieldAppearance.setBorderColour(new PDColor(new float[]{0,1,0}, PDDeviceRGB.INSTANCE));
                        fieldAppearance.setBackground(new PDColor(new float[]{1,1,0}, PDDeviceRGB.INSTANCE));
                        widget.setAppearanceCharacteristics(fieldAppearance);
                                    // set the field value. Note that the last character is a turkish capital I with a dot,
                        // which is not part of WinAnsiEncoding
                        textBox.setValue("Sample field İ");

            	            // put some text near the field
            	            try (PDPageContentStream cs = new PDPageContentStream(doc, page))
            	            {
                	                cs.beginText();
                	                cs.setFont(PDType1Font.HELVETICA, 15);
                	                cs.newLineAtOffset(50, 760);
                	                cs.showText("Field:");
                	                cs.endText();
                	            }

            	            doc.save("SimpleFormWithEmbeddedFont.pdf");
            	        } catch (IOException e) {
                    e.printStackTrace();
                }
            }
}
