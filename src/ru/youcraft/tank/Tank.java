package ru.youcraft.tank;

import ru.youcraft.tank.blocks.BlockTank;
import ru.youcraft.tank.items.ItemFishOil;
import ru.youcraft.tank.items.ItemGlue;
import ru.youcraft.tank.items.ItemTank;
import ru.youcraft.tank.tiles.TileTank;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = Tank.modid, name = "Extended Tanks", version = "1.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = true)
public class Tank {
	public static final String modid = "extendedtanks";
	public static Block tankBlock;
	public static Item fishOilItem;
	public static Item glueItem;
	public static int TANK_ID = 597;
	public static int FISHOIL_ID = 15597;
	public static int GLUE_ID = 15598;

	public static CreativeTabs tabExtendedTanks =new CreativeTabs("extendedTanks") {
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return fishOilItem;
		}
	};

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		TANK_ID = config.getBlock("Tank", TANK_ID).getInt();
		FISHOIL_ID = config.getItem("FishOil", FISHOIL_ID).getInt();
		GLUE_ID = config.getItem("Glue", GLUE_ID).getInt();
		config.save();
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {
		tankBlock = new BlockTank(TANK_ID);
		fishOilItem = new ItemFishOil(FISHOIL_ID).setTextureName(modid + ":itemFishOil");
		glueItem = new ItemGlue(GLUE_ID).setTextureName(modid + ":itemGlue");
		GameRegistry.registerBlock(tankBlock, ItemTank.class, tankBlock.getUnlocalizedName().substring(5));
		GameRegistry.registerTileEntity(TileTank.class, "ExtendedTank");
		GameRegistry.registerItem(fishOilItem, fishOilItem.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(glueItem, glueItem.getUnlocalizedName().substring(5));
		TankCrafting.addRecipes();
		boolean isWaila = FMLInterModComms.sendMessage("Waila", "register", "ru.youcraft.tank.WailaIntegration.callbackRegister");
		
    }

}
