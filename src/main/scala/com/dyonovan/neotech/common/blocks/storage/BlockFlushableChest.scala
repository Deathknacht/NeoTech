package com.dyonovan.neotech.common.blocks.storage

import com.dyonovan.neotech.client.gui.storage.GuiFlushableChest
import com.dyonovan.neotech.common.blocks.BaseBlock
import com.dyonovan.neotech.common.container.storage.ContainerFlushableChest
import com.dyonovan.neotech.common.tiles.storage.TileFlushableChest
import com.dyonovan.neotech.managers.ItemManager
import com.teambr.bookshelf.common.blocks.properties.PropertyRotation
import com.teambr.bookshelf.common.blocks.traits.{FourWayRotation, DropsItems}
import com.teambr.bookshelf.common.tiles.traits.OpensGui
import com.teambr.bookshelf.util.WorldUtils
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.{BlockState, IBlockState}
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.{Container, IInventory}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.{EnumParticleTypes, MathHelper, EnumFacing, BlockPos}
import net.minecraft.world.World
import net.minecraftforge.common.property.{ExtendedBlockState, IUnlistedProperty}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

/**
  * This file was created for NeoTech
  *
  * NeoTech is licensed under the
  * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
  * http://creativecommons.org/licenses/by-nc-sa/4.0/
  *
  * @author Dyonovan
  * @since 1/23/2016
  */
class BlockFlushableChest extends BaseBlock(Material.iron, "flushableChest", classOf[TileFlushableChest])
  with DropsItems with OpensGui {

    this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F)

    override def onNeighborBlockChange(world: World, pos: BlockPos, state: IBlockState, block: Block): Unit = {
        if (!world.isRemote) {
            if (world.isBlockPowered(pos)) {
                world.getTileEntity(pos).asInstanceOf[TileFlushableChest].clear()
                world.markBlockForUpdate(pos)
            }
        }
    }

    override def isOpaqueCube: Boolean = false

    override def isFullCube: Boolean = false

    override def getRenderType: Int = 2

    override def rotateBlock(world : World, pos : BlockPos, side : EnumFacing) : Boolean = {
        val tag = new NBTTagCompound
        world.getTileEntity(pos).writeToNBT(tag)
        if(side != EnumFacing.UP && side != EnumFacing.DOWN)
            world.setBlockState(pos, world.getBlockState(pos).withProperty(PropertyRotation.FOUR_WAY, side))
        else
            world.setBlockState(pos, world.getBlockState(pos).withProperty(PropertyRotation.FOUR_WAY, WorldUtils.rotateRight(world.getBlockState(pos).getValue(PropertyRotation.FOUR_WAY))))
        if(tag != null) {
            world.getTileEntity(pos).readFromNBT(tag)
        }
        true
    }

    override def hasComparatorInputOverride = true

    override def getComparatorInputOverride(world : World, pos : BlockPos) : Int = {
        val tile = world.getTileEntity(pos)
        tile match {
            case inventory: IInventory => Container.calcRedstoneFromInventory(inventory)
            case _ => 0
        }
    }

    /**
      * Called when the block is placed, we check which way the player is facing and put our value as the opposite of that
      */
    override def onBlockPlaced(world : World, blockPos : BlockPos, facing : EnumFacing, hitX : Float, hitY : Float, hitZ : Float, meta : Int, placer : EntityLivingBase) : IBlockState = {
        val playerFacingDirection = if (placer == null) 0 else MathHelper.floor_double((placer.rotationYaw / 90.0F) + 0.5D) & 3
        val enumFacing = EnumFacing.getHorizontal(playerFacingDirection).getOpposite
        this.getDefaultState.withProperty(PropertyRotation.FOUR_WAY, enumFacing)
    }

    /**
      * Used to say what our block state is
      */
    override def createBlockState() : BlockState = {
        val listed = new Array[IProperty[_]](1)
        listed(0) = PropertyRotation.FOUR_WAY
        val unlisted = new Array[IUnlistedProperty[_]](0)
        new ExtendedBlockState(this, listed, unlisted)
    }

    /**
      * Used to convert the meta to state
      *
      * @param meta The meta
      * @return
      */
    override def getStateFromMeta(meta : Int) : IBlockState = getDefaultState.withProperty(PropertyRotation.FOUR_WAY, EnumFacing.getFront(meta))

    /**
      * Called to convert state from meta
      *
      * @param state The state
      * @return
      */
    override def getMetaFromState(state : IBlockState) = state.getValue(PropertyRotation.FOUR_WAY).getIndex

    override def getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef = {
        if(player.inventory.getCurrentItem == null || (player.inventory.getCurrentItem != null && player.inventory.getCurrentItem.getItem != ItemManager.wrench))
            new ContainerFlushableChest(player.inventory, world.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[TileFlushableChest])
        else null
    }

    @SideOnly(Side.CLIENT)
    override def getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef = {
        if(player.inventory.getCurrentItem == null || (player.inventory.getCurrentItem != null && player.inventory.getCurrentItem.getItem != ItemManager.wrench))
            new GuiFlushableChest(player, world.getTileEntity(new BlockPos(x, y, z)).asInstanceOf[TileFlushableChest])
        else null
    }
}
