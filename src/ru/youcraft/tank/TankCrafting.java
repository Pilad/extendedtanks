package ru.youcraft.tank;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import cpw.mods.fml.common.registry.GameRegistry;

public class TankCrafting {
	public static void addRecipes() {
		GameRegistry.addSmelting(Item.fishCooked.itemID, new ItemStack(Tank.fishOilItem), 0.1f);
		GameRegistry.addShapelessRecipe(new ItemStack(Tank.glueItem, 2), new ItemStack(Item.dyePowder,1,15), new ItemStack(Tank.fishOilItem), new ItemStack(Item.bucketMilk.setContainerItem(Item.bucketEmpty)));
		GameRegistry.addRecipe(new ItemStack(Tank.tankBlock), new Object[] {
				"XZX", "ZCZ", "XZX", 'X', Item.ingotIron, 'C', Tank.glueItem, 'Z', Block.glass });
		GameRegistry.addShapelessRecipe(new ItemStack(Tank.tankBlock, 8, 0), new ItemStack(Tank.tankBlock, 1, 1));
		GameRegistry.addRecipe(new ItemStack(Tank.tankBlock, 1, 1), new Object[] {
				"XXX", "XCX", "XXX", 'X', new ItemStack(Tank.tankBlock, 1, 0), 'C', Block.blockGold });
		GameRegistry.addShapelessRecipe(new ItemStack(Tank.tankBlock, 8, 1), new ItemStack(Tank.tankBlock, 1, 2));
		GameRegistry.addRecipe(new ItemStack(Tank.tankBlock, 1, 2), new Object[] {
				"XXX", "XCX", "XXX", 'X', new ItemStack(Tank.tankBlock, 1, 1), 'C', Block.blockDiamond });

	}
}