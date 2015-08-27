package ru.youcraft.tank;
import java.util.List;

import cpw.mods.fml.common.Optional;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import ru.youcraft.tank.blocks.BlockTank;
import ru.youcraft.tank.tiles.TileTank;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

@Optional.Interface(iface = "mcp.mobius.waila.api.IWailaDataProvider", modid = "Waila")
public class WailaIntegration implements IWailaDataProvider {

	@Optional.Method(modid = "Waila")
    public static void callbackRegister(IWailaRegistrar register) {
		WailaIntegration instance = new WailaIntegration();
		register.registerBodyProvider(instance, BlockTank.class);
	}

	@Override
	@Optional.Method(modid = "Waila")
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return accessor.getStack();
	}

	@Override
	@Optional.Method(modid = "Waila")
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		return currenttip;
	}

	@Override
	@Optional.Method(modid = "Waila")
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		if(accessor.getTileEntity() != null && accessor.getTileEntity() instanceof TileTank) {
			TileTank tile = (TileTank)accessor.getTileEntity();
			int size = 16000;
			for(int i = 0; i < tile.meta; i++)
				size *= 8;
			if(tile.tank.getFluidAmount() > 0) {
				currenttip.add(StatCollector.translateToLocal("extendedtanks.fluid") + ": " + Utils.getFluidName(tile.tank.getFluid().getFluid()));
				currenttip.add(StatCollector.translateToLocal("extendedtanks.amount") + ": " + tile.tank.getFluidAmount() + " / " + size + " mB");
			}
			else 
				currenttip.add(StatCollector.translateToLocal("extendedtanks.size")+ ": "+size + " mB");
		}
		return currenttip;
	}

	@Override
	@Optional.Method(modid = "Waila")
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		return currenttip;
	}

}
