package com.dyonovan.neotech.events

import com.dyonovan.neotech.lib.Reference
import com.dyonovan.neotech.registries.ConfigRegistry
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
  * This file was created for NeoTech
  *
  * NeoTech is licensed under the
  * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
  * http://creativecommons.org/licenses/by-nc-sa/4.0/
  *
  * @author Dyonovan
  * @since 3/17/2016
  */
object ConfigChanged {

    @SubscribeEvent
    def onConfigChange(event: ConfigChangedEvent.OnConfigChangedEvent): Unit = {
        if (event.modID.equals(Reference.MOD_ID) && ConfigRegistry.config.hasChanged)
            ConfigRegistry.config.save()
    }
}
