package ru.youcraft.tank.tiles;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileTank extends TileEntity implements IFluidHandler {
	public FluidTank tank = null;
	public int meta = 0;
	private boolean needsUpdate = true;
	private int updateTimer = 0;

	public TileTank() {
	}

	public TileTank(int metadata) {
		if (metadata > 2 || metadata < 0)
			metadata = 0;
		this.meta = metadata;
		int size = 16000;
		while (metadata-- > 0)
			size *= 8;
		this.tank = new FluidTank(size);
	}

	public int getBlockTopTextureIndex()
	{
		return this.meta * 26;
	}

	public int getBlockSideTextureIndex()
	{
		int i = getBlockTopTextureIndex();
		if(this.tank != null) {
			if(this.tank.getFluidAmount() == 0) i++;
			else
				i += 2 + Math.round((double)(23 * this.tank.getFluidAmount()) / (double)(this.tank.getCapacity()));
		}
		return i;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		int i = this.tank.getFluidAmount();
		int ret = this.tank.fill(resource, doFill);
		needsUpdate = needsUpdate || (this.tank.getFluidAmount() != i);
		return ret;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		int i = this.tank.getFluidAmount();
		FluidStack ret = this.tank.drain(resource.amount, doDrain);
		needsUpdate = needsUpdate || (this.tank.getFluidAmount() != i);
		return ret;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		int i = this.tank.getFluidAmount();
		FluidStack ret = this.tank.drain(maxDrain, doDrain);
		needsUpdate = needsUpdate || (this.tank.getFluidAmount() != i);
		return ret;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] { this.tank.getInfo() };
	}

	@Override
	public void updateEntity() {
		if (worldObj.isRemote) 
			return;
		if (needsUpdate) {
			if (updateTimer == 0) {
				updateTimer = 16;
			} else {
				--updateTimer;
				if (updateTimer == 0) {
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
					needsUpdate = false;
				}
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		int metadata = tag.getInteger("meta");
		this.meta = metadata;
		int size = 16000;
		while (metadata-- > 0)
			size *= 8;
		this.tank = new FluidTank(size);
		this.tank.readFromNBT(tag);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("meta", this.meta);
		this.tank.writeToNBT(tag);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound syncData = new NBTTagCompound();
		writeToNBT(syncData);
		return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 0, syncData);
	}

	@Override
	public void onDataPacket(INetworkManager netManager, Packet132TileEntityData packet) {
		readFromNBT(packet.data);
		Minecraft.getMinecraft().renderGlobal.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
	}

}
