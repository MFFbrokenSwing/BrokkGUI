package org.yggard.brokkgui.wrapper;

import org.yggard.brokkgui.BrokkGuiPlatform;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * @author Ourten 5 oct. 2016
 */
@Mod(modid = BrokkGuiWrapperMod.MODID, version = BrokkGuiWrapperMod.VERSION, name = BrokkGuiWrapperMod.MODNAME)
public class BrokkGuiWrapperMod
{
    public static final String MODID   = "brokkguiwrapper";
    public static final String MODNAME = "BrokkGui Wrapper";
    public static final String VERSION = "0.1.0";

    @Mod.EventHandler
    public void onPreInit(final FMLPreInitializationEvent event)
    {
        BrokkGuiPlatform.getInstance().setPlatformName("MC1.7.10");
        BrokkGuiPlatform.getInstance().setKeyboardUtil(new KeyboardUtil());
        BrokkGuiPlatform.getInstance().setMouseUtil(new MouseUtil());

        if (event.getSide().isClient())
            BrokkGuiPlatform.getInstance().setGuiHelper(new GuiHelper());
    }
}