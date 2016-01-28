package com.dyonovan.neotech.pipes.gui

import com.dyonovan.neotech.pipes.types.{AdvancedPipe, InterfacePipe}
import com.teambr.bookshelf.client.gui.component.display.GuiComponentText
import com.teambr.bookshelf.client.gui.{GuiColor, GuiBase}
import com.teambr.bookshelf.client.gui.component.control.{GuiComponentTexturedButton, GuiComponentCheckBox}
import com.teambr.bookshelf.common.container.ContainerGeneric
import com.teambr.bookshelf.common.tiles.traits.Syncable
import net.minecraft.util.EnumFacing
import net.minecraftforge.fml.client.FMLClientHandler

import scala.collection.mutable.ArrayBuffer
import scala.reflect.internal.util.StringOps

/**
  * This file was created for NeoTech
  *
  * NeoTech is licensed under the
  * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
  * http://creativecommons.org/licenses/by-nc-sa/4.0/
  *
  * @author Paul Davis <pauljoda>
  * @since 1/21/2016
  */
class GuiAdvancedPipeConnections(tileEntity : AdvancedPipe, tile : Syncable) extends GuiBase[ContainerGeneric](new ContainerGeneric, 75, 150, "Connections") {
    override def addComponents(): Unit = {
        components += new GuiComponentText(" FIX FOR MINECRAFT ", -100, -10000000)

        for(dir <- EnumFacing.values()) {
            components += new GuiComponentText(dir.getName.toUpperCase, 7, 20 * dir.ordinal() + 20)
            components += new GuiComponentTexturedButton(50, 20 * dir.ordinal() + 15,
                tileEntity.getUVForMode(tileEntity.getModeForSide(dir))._1,
                tileEntity.getUVForMode(tileEntity.getModeForSide(dir))._2,
                16, 16, 20, 20) {
                override def doAction(): Unit = {
                    tileEntity.setVariable(AdvancedPipe.IO_FIELD_ID, dir.ordinal())
                    tileEntity.sendValueToServer(AdvancedPipe.IO_FIELD_ID, dir.ordinal())
                }

                override def render(i : Int, j : Int) = {
                    setUV(tileEntity.getUVForMode(tileEntity.getModeForSide(dir)))
                    super.render(i, j)
                }

                override def getDynamicToolTip(mouseX: Int, mouseY: Int): ArrayBuffer[String] = {
                    val tip = new ArrayBuffer[String]()
                    tip += GuiColor.YELLOW + dir.getName.toUpperCase + ": " + GuiColor.WHITE + tileEntity.getDisplayNameForIOMode(tileEntity.getModeForSide(dir))
                    tip
                }
            }
        }
    }
}
