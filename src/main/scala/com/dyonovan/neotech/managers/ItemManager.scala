package com.dyonovan.neotech.managers

import com.dyonovan.neotech.NeoTech
import com.dyonovan.neotech.common.items._
import com.dyonovan.neotech.tools.UpgradeItemManager
import com.dyonovan.neotech.tools.armor.ItemElectricArmor
import com.dyonovan.neotech.tools.tools.{ElectricPickaxe, ElectricSword}
import com.dyonovan.neotech.universe.items.SunItem
import com.teambr.bookshelf.Bookshelf
import com.teambr.bookshelf.helper.LogHelper
import gnu.trove.map.hash.THashMap
import net.minecraft.item.{Item, ItemStack}
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.OreDictionary

import scala.collection.JavaConversions._

/**
 * This file was created for NeoTech
 *
 * NeoTech is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Dyonovan
 * @since August 12, 2015
 */
object ItemManager {

    lazy val itemRegistry = new THashMap[Class[_ <: Item], Item]()

    //Upgrade System
    val upgradeMBEmpty = new MotherBoardItem("upgradeMBEmpty", 1, true)
    val upgradeMBFull = new MotherBoardItem("upgradeMBFull", 1, false)
    val upgradeHardDrive = new MotherBoardUpgradeItem("upgradeHardDrive", 8, NeoTech.tabNeoTech)
    val upgradeControl = new MotherBoardUpgradeItem("upgradeControl", 1, NeoTech.tabNeoTech)
    val upgradeProcessor = new MotherBoardUpgradeItem("upgradeProcessor", 8, NeoTech.tabNeoTech)
    val upgradeExpansion = new MotherBoardUpgradeItem("upgradeExpansion", 1, NeoTech.tabNeoTech)

    //Utils
    val wrench = new ItemWrench
    val trashBag = new ItemTrashBag
    val spawnerMover = new ItemSpawnerMover
    val mobGun = new ItemMobGun
    val mobNet = new ItemMobNet
    val electroMagnet = new ItemElectromagnet

    val basicRFBattery = new RFBattery("basicRFBattery", 1)
    val advancedRFBattery = new RFBattery("advancedRFBattery", 2)
    val eliteRFBattery = new RFBattery("eliteRFBattery", 3)

    //Electric Tools
    val electricPickaxe = new ElectricPickaxe
    val electricSword = new ElectricSword

    //Electric Armor
    val electricArmorHelmet = new ItemElectricArmor("electricArmorHelmet", 1, 0)
    val electricArmorChestplate = new ItemElectricArmor("electricArmorChestplate", 1, 1)
    val electricArmorLeggings = new ItemElectricArmor("electricArmorLeggings", 2, 2)
    val electricArmorBoots = new ItemElectricArmor("electricArmorBoots", 1, 3)

    // Universe Items
    val inertSun = new SunItem("inertSun")
    val blueDwarf = new SunItem("blueDwarf")
    val smallSun = new SunItem("smallSun")
    val largeSun = new SunItem("largeSun")
    val redGiant = new SunItem("redGiant")

    def preInit(): Unit = {
        registerItem(upgradeMBEmpty, "upgradeMBEmpty")
        registerItem(upgradeMBFull, "upgradeMBFull")
        registerItem(upgradeHardDrive, "upgradeHardDrive")
        registerItem(upgradeControl, "upgradeControl")
        registerItem(upgradeProcessor, "upgradeProcessor")
        registerItem(upgradeExpansion, "upgradeExpansion")

        registerItem(wrench, "wrench")
        registerItem(trashBag, "trashBag")
        registerItem(spawnerMover, "spawnerMover")
        registerItem(mobGun, "mobGun")
        registerItem(mobNet, "mobNet")
        registerItem(electroMagnet, "electroMagnet")

        registerItem(basicRFBattery, "basicRFBattery", "rfBattery", OreDictionary.WILDCARD_VALUE)
        registerItem(advancedRFBattery, "advancedRFBattery", "rfBattery", OreDictionary.WILDCARD_VALUE)
        registerItem(eliteRFBattery, "eliteRFBattery", "rfBattery", OreDictionary.WILDCARD_VALUE)

        registerItem(electricPickaxe, "electricPickaxe")
        registerItem(electricSword, "electricSword")

        registerItem(electricArmorHelmet, "electricArmorHelmet")
        registerItem(electricArmorChestplate, "electricArmorChestplate")
        registerItem(electricArmorLeggings, "electricArmorLeggings")
        registerItem(electricArmorBoots, "electricArmorBoots")

        registerItem(inertSun, "inertSun")
        registerItem(blueDwarf, "blueDwarf")
        registerItem(smallSun, "smallSun")
        registerItem(largeSun, "largeSun")
        registerItem(redGiant, "redGiant")

        for (data <- Bookshelf.itemsToRegister) {
            if (data.getAnnotationInfo.get("modid") != null &&
                    data.getAnnotationInfo.get("modid").equals("neotech")) {
                try {
                    val asmClass = Class.forName(data.getClassName)
                    val itemClass = asmClass.asSubclass(classOf[Item])

                    val modItem = itemClass.newInstance()

                    GameRegistry.registerItem(modItem, modItem.getUnlocalizedName.split(":")(1))
                    itemRegistry.put(itemClass, modItem)
                } catch {
                    case e: Exception =>
                        LogHelper.severe(String.format("Could not register item class %s", data.getClassName))
                }
            }
        }

        UpgradeItemManager.preInit()
    }
    /**
     * Helper method to register items
     *
     * @param item The item to register
     * @param name The name of the item
     * @param oreDict The ore dict tag
     */
    def registerItem(item: Item, name: String, oreDict: String) : Item = {
        GameRegistry.registerItem(item, name)
        if (oreDict != null) OreDictionary.registerOre(oreDict, item)
        item
    }

    def registerItem(item: Item, name: String) : Item = {
        registerItem(item, name, null)
    }

    def registerItem(item: Item, name: String, oreDict: String, itemDamage: Int) : Item = {
        GameRegistry.registerItem(item, name)
        if (oreDict != null) OreDictionary.registerOre(oreDict, new ItemStack(item, 1, itemDamage))
        item
    }
}
