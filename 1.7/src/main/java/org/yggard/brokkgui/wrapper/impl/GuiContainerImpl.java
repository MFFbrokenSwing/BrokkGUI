package org.yggard.brokkgui.wrapper.impl;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.yggard.brokkgui.internal.IBrokkGuiImpl;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.EGuiRenderPass;
import org.yggard.brokkgui.wrapper.GuiRenderer;
import org.yggard.brokkgui.wrapper.container.BrokkGuiContainer;

public class GuiContainerImpl extends GuiContainer implements IBrokkGuiImpl
{
    private final BrokkGuiContainer<? extends Container> brokkgui;
    private       String                                 modID;

    private final GuiRenderer renderer;

    public GuiContainerImpl(String modID, BrokkGuiContainer<? extends Container> brokkGui)
    {
        super(brokkGui.getContainer());
        this.brokkgui = brokkGui;
        this.modID = modID;
        this.renderer = new GuiRenderer(Tessellator.instance);
        this.brokkgui.setWrapper(this);

        brokkGui.getWidthProperty().addListener((obs, oldValue, newValue) ->
        {
            this.width = newValue.intValue();
            this.guiLeft = (this.width - this.xSize) / 2;
        });
        brokkGui.getHeightProperty().addListener((obs, oldValue, newValue) ->
        {
            this.height = newValue.intValue();
            this.guiTop = (this.height - this.ySize) / 2;
        });
    }

    @Override
    public void initGui()
    {
        super.initGui();

        this.brokkgui.getScreenWidthProperty().setValue(this.width);
        this.brokkgui.getScreenHeightProperty().setValue(this.height);

        Keyboard.enableRepeatEvents(true);
        this.brokkgui.initGui();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);

        this.brokkgui.onClose();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY)
    {
        this.brokkgui.render(mouseX, mouseY, EGuiRenderPass.MAIN);
        this.brokkgui.render(mouseX, mouseY, EGuiRenderPass.HOVER);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        GL11.glTranslatef(-this.guiLeft, -this.guiTop, 0);
        this.brokkgui.render(mouseX, mouseY, EGuiRenderPass.SPECIAL);
        GL11.glTranslatef(this.guiLeft, this.guiTop, 0);
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int key)
    {
        super.mouseClicked(mouseX, mouseY, key);

        this.brokkgui.onClick(mouseX, mouseY, key);
    }

    @Override
    public void handleMouseInput()
    {
        super.handleMouseInput();
        this.brokkgui.handleMouseInput();
    }

    @Override
    public void keyTyped(final char c, final int key)
    {
        super.keyTyped(c, key);
        this.brokkgui.onKeyTyped(c, key);
    }

    @Override
    public void askClose()
    {
        if (this.mc.thePlayer != null)
            this.inventorySlots.onContainerClosed(this.mc.thePlayer);
        this.mc.displayGuiScreen(null);
        this.mc.setIngameFocus();

        this.brokkgui.onClose();
    }

    @Override
    public void askOpen()
    {
        // TODO : Container opening sync

        this.brokkgui.onOpen();
    }

    @Override
    public int getScreenWidth()
    {
        return this.width;
    }

    @Override
    public int getScreenHeight()
    {
        return this.height;
    }

    @Override
    public IGuiRenderer getRenderer()
    {
        return this.renderer;
    }

    @Override
    public String getThemeID()
    {
        return this.modID;
    }

    @Override
    protected void handleMouseClick(final Slot slot, final int slotID, final int button, final int flag)
    {
        super.handleMouseClick(slot, slotID, button, flag);

        if (slot != null)
            this.brokkgui.slotClick(slot, button);
    }
}