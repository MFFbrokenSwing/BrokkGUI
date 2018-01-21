package org.yggard.brokkgui.wrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.opengl.GL11;
import org.yggard.brokkgui.data.EAlignment;
import org.yggard.brokkgui.data.Vector2i;
import org.yggard.brokkgui.internal.EGuiRenderMode;
import org.yggard.brokkgui.internal.IGuiHelper;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.Texture;

import java.util.List;

public class GuiHelper implements IGuiHelper
{
    private RenderItem          itemRender;
    private Minecraft           mc;
    private GuiRenderItemHelper itemHelper;

    public GuiHelper()
    {
        this.mc = Minecraft.getMinecraft();
        this.itemRender = this.mc.getRenderItem();

        this.itemHelper = new GuiRenderItemHelper();
    }

    @Override
    public void bindTexture(Texture texture)
    {
        this.mc.renderEngine.bindTexture(new ResourceLocation(texture.getResource()));
    }

    @Override
    public void scissorBox(float f, float g, float h, float i)
    {
        int width = (int) (h - f);
        int height = (int) (i - g);
        ScaledResolution sr = new ScaledResolution(this.mc);
        int factor = sr.getScaleFactor();
        GuiScreen currentScreen = this.mc.currentScreen;
        if (currentScreen != null)
        {
            int bottomY = (int) (currentScreen.height - i);
            GL11.glScissor((int) (f * factor), bottomY * factor, width * factor, height * factor);
        }
    }

    @Override
    public void drawString(String string, int x, int y, float zLevel, Color textColor, Color shadowColor,
                           EAlignment alignment)
    {
        GlStateManager.enableBlend();
        // This is a very, very, very ugly hack but I've not seen any other way
        // to fix this minecraft bug. @see GlStateManager color(float colorRed,
        // float colorGreen, float colorBlue, float colorAlpha) L 675
        GlStateManager.color(0, 0, 0, 0);
        if (zLevel != 0)
        {
            GL11.glPushMatrix();
            GL11.glTranslated(0, 0, zLevel);
        }

        if (alignment.isHorizontalCentered())
            x -= this.mc.fontRenderer.getStringWidth(string) / 2;
        else if (alignment.isRight())
            x -= this.mc.fontRenderer.getStringWidth(string);

        if (alignment.isVerticalCentered())
            y -= this.mc.fontRenderer.FONT_HEIGHT / 2;
        else if (alignment.isDown())
            y -= this.mc.fontRenderer.FONT_HEIGHT;

        if (shadowColor.getAlpha() != 1)
            this.mc.fontRenderer.drawString(string, x + 1, y + 1, shadowColor.toRGBAInt(), false);
        this.mc.fontRenderer.drawString(string, x, y, textColor.toRGBAInt(), false);
        if (zLevel != 0)
            GL11.glPopMatrix();
        GlStateManager.resetColor();
        GlStateManager.disableBlend();
    }

    @Override
    public void drawString(String string, double x, double y, float zLevel, Color textColor, Color shadowColor)
    {
        this.drawString(string, (int) x, (int) y, zLevel, textColor, shadowColor, EAlignment.LEFT_UP);
    }

    @Override
    public void drawString(String string, double x, double y, float zLevel, Color textColor, EAlignment alignment)
    {
        this.drawString(string, (int) x, (int) y, zLevel, textColor, Color.ALPHA, alignment);
    }

    @Override
    public void drawString(String string, double x, double y, float zLevel, Color textColor)
    {
        this.drawString(string, (int) x, (int) y, zLevel, textColor, Color.ALPHA, EAlignment.LEFT_UP);
    }

    @Override
    public void drawTexturedRect(IGuiRenderer renderer, float xStart, float yStart, float uMin, float vMin,
                                 float uMax, float vMax, float width, float height, float zLevel)
    {
        this.enableAlpha();
        GlStateManager.color(1, 1, 1, 1);
        renderer.beginDrawingQuads(true);
        renderer.addVertexWithUV(Math.floor(xStart), Math.floor(yStart + height), zLevel, uMin, vMax);
        renderer.addVertexWithUV(Math.floor(xStart + width), Math.floor(yStart + height), zLevel, uMax, vMax);
        renderer.addVertexWithUV(Math.floor(xStart + width), Math.floor(yStart), zLevel, uMax, vMin);
        renderer.addVertexWithUV(Math.floor(xStart), Math.floor(yStart), zLevel, uMin, vMin);
        renderer.endDrawing();
        this.disableAlpha();
    }

    @Override
    public void drawTexturedRect(IGuiRenderer renderer, float xStart, float yStart, float uMin, float vMin,
                                 float width, float height, float zLevel)
    {
        this.drawTexturedRect(renderer, xStart, yStart, uMin, vMin, 1, 1, width, height, zLevel);
    }

    @Override
    public void drawColoredEmptyRect(IGuiRenderer renderer, float startX, float startY, float width, float height,
                                     float zLevel, Color c, float thin)
    {
        this.drawColoredRect(renderer, startX, startY, width - thin, thin, zLevel, c);
        this.drawColoredRect(renderer, startX + width - thin, startY, thin, height - thin, zLevel, c);
        this.drawColoredRect(renderer, startX + thin, startY + height - thin, width - thin, thin, zLevel, c);
        this.drawColoredRect(renderer, startX, startY + thin, thin, height - thin, zLevel, c);
    }

    @Override
    public void drawColoredRect(IGuiRenderer renderer, float startX, float startY,
                                float width, float height, float zLevel, Color color)
    {
        this.enableAlpha();
        GlStateManager.disableTexture2D();
        GlStateManager.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        renderer.beginDrawingQuads(false);
        renderer.addVertex(Math.floor(startX), Math.floor(startY), zLevel);
        renderer.addVertex(Math.floor(startX), Math.floor(startY + height), zLevel);
        renderer.addVertex(Math.floor(startX + width), Math.floor(startY + height), zLevel);
        renderer.addVertex(Math.floor(startX + width), Math.floor(startY), zLevel);
        renderer.endDrawing();
        GlStateManager.resetColor();
        GlStateManager.enableTexture2D();
        this.disableAlpha();
    }

    @Override
    public void drawColoredEmptyCircle(IGuiRenderer renderer, float startX, float startY, float radius, float zLevel,
                                       Color color, float thin)
    {
        if (thin > 0)
        {
            float x = radius;
            float y = 0;
            float err = 0;

            this.enableAlpha();
            GlStateManager.disableTexture2D();
            GlStateManager.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            renderer.beginDrawing(EGuiRenderMode.POINTS, false);
            while (x >= y)
            {
                renderer.addVertex(Math.floor(startX + x), Math.floor(startY + y), zLevel);
                renderer.addVertex(Math.floor(startX + y), Math.floor(startY + x), zLevel);

                renderer.addVertex(Math.floor(startX - y), Math.floor(startY + x), zLevel);
                renderer.addVertex(Math.floor(startX - x), Math.floor(startY + y), zLevel);

                renderer.addVertex(Math.floor(startX - x), Math.floor(startY - y), zLevel);
                renderer.addVertex(Math.floor(startX - y), Math.floor(startY - x), zLevel);

                renderer.addVertex(Math.floor(startX + y), Math.floor(startY - x), zLevel);
                renderer.addVertex(Math.floor(startX + x), Math.floor(startY - y), zLevel);

                y += 1;
                err += 1 + 2 * y;
                if (2 * (err - x) + 1 > 0)
                {
                    x -= 1;
                    err += 1 - 2 * x;
                }
            }
            renderer.endDrawing();
            GlStateManager.resetColor();
            GlStateManager.enableTexture2D();
            this.disableAlpha();
        }
        if (thin > 1)
            this.drawColoredEmptyCircle(renderer, startX + 1, startY + 1, radius - 1, zLevel, color, thin - 1);
    }

    @Override
    public void drawColoredCircle(IGuiRenderer renderer, float startX, float startY,
                                  float radius, float zLevel, Color c)
    {
        GlStateManager.disableTexture2D();
        this.enableAlpha();
        GlStateManager.color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        renderer.beginDrawing(EGuiRenderMode.POINTS, false);
        float r2 = radius * radius;
        float area = r2 * 4;
        float rr = radius * 2;

        for (int i = 0; i < area; i++)
        {
            float tx = i % rr - radius;
            float ty = i / rr - radius;

            if (tx * tx + ty * ty <= r2)
                renderer.addVertex(Math.floor(startX + tx), Math.floor(startY + ty), zLevel);
        }
        renderer.endDrawing();
        GlStateManager.resetColor();
        GlStateManager.enableTexture2D();
        this.disableAlpha();
    }

    @Override
    public void drawTexturedCircle(IGuiRenderer renderer, float xStart, float yStart,
                                   float uMin, float vMin, float uMax, float vMax,
                                   float radius,
                                   float zLevel)
    {
        renderer.beginDrawing(EGuiRenderMode.POINTS, true);
        float r2 = radius * radius;
        float area = r2 * 4;
        float rr = radius * 2;

        for (int i = 0; i < area; i++)
        {
            float tx = i % rr - radius;
            float ty = i / rr - radius;

            if (tx * tx + ty * ty <= r2)
                renderer.addVertexWithUV(Math.floor(xStart + tx), Math.floor(yStart + ty), zLevel,
                        uMin + tx / rr * (uMax - uMin), vMin + ty / rr * (vMax - vMin));
        }
        renderer.endDrawing();
    }

    @Override
    public void drawTexturedCircle(IGuiRenderer renderer, float xStart, float yStart,
                                   float uMin, float vMin, float radius, float zLevel)
    {
        this.drawTexturedCircle(renderer, xStart, yStart, uMin, vMin, 1, 1, radius, zLevel);
    }

    @Override
    public void drawColoredLine(IGuiRenderer renderer, float startX, float startY,
                                float endX, float endY, float lineWeight, float zLevel,
                                Color c)
    {
        GlStateManager.disableTexture2D();
        this.enableAlpha();
        GlStateManager.color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());

        renderer.beginDrawing(EGuiRenderMode.LINE, false);
        GL11.glLineWidth(lineWeight);

        renderer.addVertex(Math.floor(startX), Math.floor(startY), zLevel);
        renderer.addVertex(Math.floor(endX), Math.floor(endY), zLevel);

        renderer.endDrawing();
        GlStateManager.resetColor();
        GlStateManager.enableTexture2D();
        this.disableAlpha();
    }

    public void drawItemStack(IGuiRenderer renderer, float startX, float startY,
                              float width, float height, float zLevel, ItemStack stack,
                              Color color)
    {
        this.drawItemStack(renderer, startX, startY, width, height, zLevel, stack, null, color);
    }

    public void drawItemStack(IGuiRenderer renderer, float startX, float startY,
                              float width, float height, float zLevel, ItemStack stack,
                              String displayString, Color color)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F);

        if (!stack.isEmpty())
        {
            float scaleX = width / 18;
            float scaleY = height / 18;
            GL11.glPushMatrix();
            GL11.glTranslated(-(startX * (scaleX - 1)) - 8 * scaleX, -(startY * (scaleY - 1)) - 8 * scaleY, 0);
            GlStateManager.scale(scaleX, scaleY, 1);
            FontRenderer font = stack.getItem().getFontRenderer(stack);
            if (font == null)
                font = this.mc.fontRenderer;

            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 0.0F, 32.0F);

            GlStateManager.enableRescaleNormal();
            GlStateManager.enableLighting();
            short short1 = 240;
            short short2 = 240;
            net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, short1 / 1.0F, short2 / 1.0F);

            this.itemHelper.renderItemStack(stack, (int) startX, (int) startY, zLevel, color);
            this.getRenderItem().renderItemOverlayIntoGUI(font, stack, (int) startX, (int) startY, displayString);
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableLighting();
            GL11.glPopMatrix();
        }
    }

    public void drawItemStackTooltip(IGuiRenderer renderer, int mouseX, int mouseY,
                                     ItemStack stack)
    {
        List<String> list = stack.getTooltip(this.mc.player, this.mc.gameSettings.advancedItemTooltips ?
                ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);

        for (int i = 0; i < list.size(); ++i)
        {
            if (i == 0)
                list.set(i, stack.getRarity().rarityColor + list.get(i));
            else
                list.set(i, TextFormatting.GRAY + list.get(i));
        }
        GuiUtils.drawHoveringText(stack, list, mouseX, mouseY, this.mc.displayWidth, this.mc.displayHeight,
                this.mc.displayWidth, this.mc.fontRenderer);
    }

    @Override
    public void translateVecToScreenSpace(Vector2i vec)
    {
        GuiScreen currentScreen = this.mc.currentScreen;
        if (currentScreen != null)
        {
            vec.setX((int) (vec.getX() / ((float) currentScreen.width / this.mc.displayWidth)));
            vec.setY((int) ((currentScreen.height - vec.getY()) / ((float) currentScreen.height / this.mc.displayHeight)
                    - 1));
        }
    }

    @Override
    public String trimStringToPixelWidth(String str, int pixelWidth)
    {
        return this.mc.fontRenderer.trimStringToWidth(str, pixelWidth);
    }

    @Override
    public float getStringWidth(String str)
    {
        return this.mc.fontRenderer.getStringWidth(str);
    }

    @Override
    public float getStringHeight()
    {
        return this.mc.fontRenderer.FONT_HEIGHT;
    }

    @Override
    public void beginScissor()
    {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
    }

    @Override
    public void endScissor()
    {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public void enableAlpha()
    {
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
    }

    public void disableAlpha()
    {
        GlStateManager.disableDepth();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
    }

    private RenderItem getRenderItem()
    {
        if (this.itemRender == null)
            this.itemRender = this.mc.getRenderItem();
        return this.itemRender;
    }
}