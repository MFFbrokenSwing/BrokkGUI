package org.yggard.brokkgui.internal;

import org.yggard.brokkgui.data.EAlignment;
import org.yggard.brokkgui.data.Vector2i;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.Texture;

/**
 * @author Ourten 5 oct. 2016
 */
public interface IGuiHelper
{
    void bindTexture(Texture texture);

    void beginScissor();

    void endScissor();

    void scissorBox(float f, float g, float h, float i);

    void drawString(String string, int x, int y, float zLevel, Color textColor, Color shadowColor,
                    EAlignment alignment);

    void drawString(String string, double x, double y, float zLevel, Color textColor, Color shadowColor);

    void drawString(String string, double x, double y, float zLevel, Color textColor, EAlignment alignment);

    void drawString(String string, double x, double y, float zLevel, Color textColor);

    void drawTexturedRect(IGuiRenderer renderer, float xStart, float yStart, float uMin, float vMin, float uMax,
                          float vMax, float width, float height, float zLevel);

    void drawTexturedRect(IGuiRenderer renderer, float xStart, float yStart, float uMin, float vMin, float width,
                          float height, float zLevel);

    void drawColoredEmptyRect(IGuiRenderer renderer, float startX, float startY, float width, float height,
                              float zLevel, Color c, float thin);

    void drawColoredRect(IGuiRenderer renderer, float startX, float startY, float width, float height, float zLevel,
                         Color color);

    void drawTexturedCircle(IGuiRenderer renderer, float xStart, float yStart, float uMin, float vMin, float uMax,
                            float vMax, float radius, float zLevel);

    void drawTexturedCircle(IGuiRenderer renderer, float xStart, float yStart, float uMin, float vMin, float radius,
                            float zLevel);

    void drawColoredEmptyCircle(IGuiRenderer renderer, float startX, float startY, float radius, float zLevel,
                                Color color, float thin);

    void drawColoredCircle(IGuiRenderer renderer, float startX, float startY, float radius, float zLevel, Color c);

    void drawColoredLine(IGuiRenderer renderer, float startX, float startY, float endX, float endY, float lineWeight,
                         float zLevel, Color c);

    void translateVecToScreenSpace(Vector2i vec);

    String trimStringToPixelWidth(String str, int pixelWidth);

    float getStringWidth(String str);

    float getStringHeight();
}