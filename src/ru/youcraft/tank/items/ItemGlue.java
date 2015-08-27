package ru.youcraft.tank.items;

import net.minecraft.item.Item;
import ru.youcraft.tank.Tank;

public class ItemGlue extends Item {

	public ItemGlue(int par1) {
		super(par1);
		this.maxStackSize = 64;
		setCreativeTab(Tank.tabExtendedTanks);
		setUnlocalizedName("itemGlue");
		}
}
