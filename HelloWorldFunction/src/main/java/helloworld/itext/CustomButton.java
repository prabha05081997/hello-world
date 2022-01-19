package helloworld.itext;

import com.itextpdf.layout.element.AbstractElement;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;

public class CustomButton extends AbstractElement {
    @Override
    protected IRenderer makeNewRenderer() {
        return null;
    }

    protected DrawContext draw(DrawContext drawContext) {
        return drawContext;
    }
}
