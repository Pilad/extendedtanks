package ru.youcraft.tank;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import ru.youcraft.tank.tiles.TileTank;

public class Utils {

	public static void dropStackInWorld(World world, int x, int y, int z, ItemStack stack) {
		if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
			float f = 0.7F;
			double d0 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
			double d1 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
			double d2 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
			EntityItem entityitem = new EntityItem(world, (double) x + d0, (double) y + d1, (double) z + d2, stack);
			entityitem.delayBeforeCanPickup = 10;
			world.spawnEntityInWorld(entityitem);
		}

	}

	public static ItemStack getTankStackFromTile(TileTank tank, boolean keepFluid) {
		ItemStack stack = new ItemStack(Tank.tankBlock, 1, tank.meta);
		stack.setTagCompound(new NBTTagCompound());
		FluidStack fluid = tank.tank.getFluid();
		if (fluid != null && keepFluid) {
			NBTTagCompound tagFluid = new NBTTagCompound();
			fluid.writeToNBT(tagFluid);
			stack.getTagCompound().setTag("Fluid", tagFluid);
		}
		return stack;
	}

	public static ItemStack consumeItem(ItemStack stack) {
		if (stack.stackSize == 1) {
			if (stack.getItem().hasContainerItem()) {
				return stack.getItem().getContainerItemStack(stack);
			}
			return null;
		}
		stack.splitStack(1);

		return stack;
	}
	
	public static String getFluidName(Fluid fluid) {
		String ret = fluid.getLocalizedName();
		if(ret == null || ret.contains("fluid."))
			ret = fluid.getName();
		return ret;
	}

}