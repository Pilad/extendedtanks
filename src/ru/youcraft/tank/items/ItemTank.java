package ru.youcraft.tank.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.resources.Language;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import ru.youcraft.tank.Utils;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTank extends ItemBlock {
	
	public ItemTank(int id) {
		super(id);
		setHasSubtypes(true);
		this.setMaxStackSize(1);
	}

	public String getUnlocalizedName(ItemStack itemstack) {
		return getUnlocalizedName() + "." + itemstack.getItemDamage();
	}

	public int getMetadata(int par1) {
		return par1;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean isAdvanced) {

		int size = 16000;
		for(int i=0; i<stack.getItemDamage(); i++)
			size *= 8;
		if (stack.hasTagCompound()) {
			NBTTagCompound tag = stack.stackTagCompound;
			FluidStack fluid = null;
			if (tag.hasKey("Fluid")) {
				fluid = FluidStack.loadFluidStackFromNBT((NBTTagCompound) tag.getTag("Fluid"));
				tooltip.add(StatCollector.translateToLocal("extendedtanks.fluid") + ": " + Utils.getFluidName(fluid.getFluid()));
			}
			int amount = (fluid != null) ? fluid.amount : 0;
			tooltip.add(StatCollector.translateToLocal("extendedtanks.amount") + ": " + amount + " / " + size + " mB");

		} else
			tooltip.add(StatCollector.translateToLocal("extendedtanks.size")+ ": "+size + " mB");
	}

}
