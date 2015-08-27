package ru.youcraft.tank.blocks;

import java.util.List;
import java.util.Random;

import ru.youcraft.tank.Tank;
import ru.youcraft.tank.Utils;
import ru.youcraft.tank.tiles.TileTank;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class BlockTank extends BlockContainer implements ITileEntityProvider {

	public BlockTank(int id) {
		super(id, Material.glass);
		setHardness(0.5F);
		this.setCreativeTab(Tank.tabExtendedTanks);
		this.setUnlocalizedName("blockExtendedTank");
	}

	@Override
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z) {
		if (!player.capabilities.isCreativeMode) {
			TileTank tank = (TileTank) world.getBlockTileEntity(x, y, z);
			Utils.dropStackInWorld(world, x, y, z, Utils.getTankStackFromTile(tank, true));
		}
		return super.removeBlockByPlayer(world, player, x, y, z);
	}

	@Override
	public int quantityDropped(Random par1Random) {
		return 0;
	}

	@Override
	public int damageDropped(int par1) {
		return par1;
	}

	@Override
	public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
		TileTank tank = (TileTank) world.getBlockTileEntity(x, y, z);
		Utils.dropStackInWorld(world, x, y, z, Utils.getTankStackFromTile(tank, true));
		super.onBlockExploded(world, x, y, z, explosion);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
		if (stack.hasTagCompound()) {
			TileTank tank = (TileTank) world.getBlockTileEntity(x, y, z);
			if (tank != null) {
				NBTTagCompound tagFluid = stack.getTagCompound().getCompoundTag("Fluid");
				if (tagFluid != null) {
					FluidStack liquid = FluidStack.loadFluidStackFromNBT(tagFluid);
					tank.tank.setFluid(liquid);
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	private Icon[] icons;

	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister reg) {
		icons = new Icon[3 * 26];
		int i = 0;
		for (int j = 0; j < 3; j++)
			for (int k = 0; k < 26; k++)
				if (k == 0)
					icons[i++] = reg.registerIcon(Tank.modid + ":blockTank" + j + "Top");
				else
					icons[i++] = reg.registerIcon(Tank.modid + ":blockTank" + j + "Side" + (k - 1));
	}

	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int meta) {
		if (meta > 2) {
			System.out.println("Invalid metadata for " + this.getUnlocalizedName());
			return icons[0];
		}
		if (par1 < 2)
			return icons[meta * 26];
		return icons[meta * 26 + 1];
	}

	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess iBlockAccess, int x, int y, int z, int side) {
		TileEntity te = iBlockAccess.getBlockTileEntity(x, y, z);
		if (te instanceof TileTank) {
			if (side < 2)
				return icons[((TileTank) te).getBlockTopTextureIndex()];
			else
				return icons[((TileTank) te).getBlockSideTextureIndex()];
		}
		return icons[0];
	}

	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		for (int i = 0; i < 3; i++) {
			par3List.add(new ItemStack(par1, 1, i));
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return null;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileTank(metadata);
	}

	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7,
			float par8, float par9) {
		ItemStack current = entityplayer.inventory.getCurrentItem();
		if (current != null) {
			FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(current);
			TileTank tank = (TileTank) world.getBlockTileEntity(i, j, k);
			if (liquid != null) {
				int available1 = tank.fill(ForgeDirection.UNKNOWN, liquid, true);
				if (available1 != 0 && !entityplayer.capabilities.isCreativeMode) {
					entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem,
							Utils.consumeItem(current));
				}
				return true;
			}

			FluidStack available = tank.getTankInfo(ForgeDirection.UNKNOWN)[0].fluid;
			if (available != null) {
				ItemStack filled = FluidContainerRegistry.fillFluidContainer(available, current);
				liquid = FluidContainerRegistry.getFluidForFilledItem(filled);
				if (liquid != null) {
					if (!entityplayer.capabilities.isCreativeMode) {
						if (current.stackSize > 1) {
							if (!entityplayer.inventory.addItemStackToInventory(filled)) {
								return false;
							}

							entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem,
									Utils.consumeItem(current));
						} else {
							entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem,
									Utils.consumeItem(current));
							entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, filled);
						}
					}

					tank.drain(ForgeDirection.UNKNOWN, liquid.amount, true);
					return true;
				}
			}
		}

		return false;
	}

}