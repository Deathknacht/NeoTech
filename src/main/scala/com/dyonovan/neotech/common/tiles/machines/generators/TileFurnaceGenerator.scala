package com.dyonovan.neotech.common.tiles.machines.generators

import cofh.api.energy.EnergyStorage
import com.dyonovan.neotech.common.tiles.MachineGenerator
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntityFurnace
import net.minecraft.util.{EnumFacing, EnumParticleTypes}
import net.minecraftforge.fluids.FluidContainerRegistry

/**
  * This file was created for NeoTech
  *
  * NeoTech is licensed under the
  * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
  * http://creativecommons.org/licenses/by-nc-sa/4.0/
  *
  * @author Dyonovan
  * @since August 13, 2015
  */
class TileFurnaceGenerator extends MachineGenerator {

    final val BASE_ENERGY_TICK = 20
    final val INPUT_SLOT       = 0

    energy = new EnergyStorage(BASE_ENERGY)

    /**
      * The initial size of the inventory
      *
      * @return
      */
    override def initialSize: Int = 1

    /**
      * This method handles how much energy to produce per tick
      *
      * @return How much energy to produce per tick
      */
    override def getEnergyProduced: Int = {
        if(getUpgradeBoard != null && getUpgradeBoard.getProcessorCount > 0)
            BASE_ENERGY_TICK + (getUpgradeBoard.getProcessorCount * 10)
        else
            BASE_ENERGY_TICK
    }

    /**
      * Called to tick generation. This is where you add power to the generator
      */
    override def generate(): Unit =
        energy.receiveEnergy(getEnergyProduced, false)

    /**
      * Called per tick to manage burn time. You can do nothing here if there is nothing to generate. You should decrease burn time here
      * You should be handling checks if burnTime is 0 in this method, otherwise the tile won't know what to do
      *
      * @return True if able to continue generating
      */
    override def manageBurnTime(): Boolean = {
        if(burnTime <= 0) {
            if(getStackInSlot(INPUT_SLOT) != null) {
                burnTime = TileEntityFurnace.getItemBurnTime(getStackInSlot(INPUT_SLOT))

                if (burnTime > 0) {
                    currentObjectBurnTime = burnTime
                    return true
                }
            }
        } else {
            burnTime -= 1
            return burnTime > 0
        }
        false
    }

    /*******************************************************************************************************************
      ************************************************ Inventory methods ***********************************************
      ******************************************************************************************************************/

    /**
      * Used to get what slots are allowed to be input
      *
      * @return The slots to input from
      */
    override def getInputSlots: Array[Int] = Array(INPUT_SLOT)

    /**
      * Used to get what slots are allowed to be output
      *
      * @return The slots to output from
      */
    override def getOutputSlots: Array[Int] = Array()

    /**
      * Returns true if automation can insert the given item in the given slot from the given side. Args: slot, item,
      * side
      */
    override def canInsertItem(index: Int, itemStackIn: ItemStack, direction: EnumFacing): Boolean =
        isItemValidForSlot(index, itemStackIn)

    /**
      * Returns true if automation can extract the given item in the given slot from the given side. Args: slot, item,
      * side
      */
    override def canExtractItem(index: Int, stack: ItemStack, direction: EnumFacing): Boolean = false

    /**
      * Used to define if an item is valid for a slot
      *
      * @param index The slot id
      * @param stack The stack to check
      * @return True if you can put this there
      */
    override def isItemValidForSlot(index: Int, stack: ItemStack): Boolean =
        TileEntityFurnace.getItemBurnTime(stack) > 0 && !FluidContainerRegistry.isContainer(stack)

    /*******************************************************************************************************************
      *************************************************** Misc methods *************************************************
      ******************************************************************************************************************/

    /**
      * Used to output the redstone single from this structure
      *
      * Use a range from 0 - 16.
      *
      * 0 Usually means that there is nothing in the tile, so take that for lowest level. Like the generator has no energy while
      * 16 is usually the flip side of that. Output 16 when it is totally full and not less
      *
      * @return int range 0 - 16
      */
    override def getRedstoneOutput: Int = (energy.getEnergyStored * 16) / energy.getMaxEnergyStored

    /**
      * Used to get what particles to spawn. This will be called when the tile is active
      */
    override def spawnActiveParticles(x: Double, y: Double, z: Double): Unit = {
        worldObj.spawnParticle(EnumParticleTypes.FLAME, x, y, z, 0, 0, 0)
        worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0, 0, 0)
    }
}