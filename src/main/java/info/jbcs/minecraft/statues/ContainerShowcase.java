/**
 * Container class for the statue
 */

package info.jbcs.minecraft.statues;

import pl.asie.lib.block.ContainerBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;

public class ContainerShowcase extends ContainerBase {

	public ContainerShowcase(InventoryPlayer inventory, TileEntityShowcase tile) {
		super(tile, inventory);
		
		addSlotToContainer(new SlotHand(tile, 0, 120, 59));

		bindPlayerInventory(inventory, 48, 144);
		
		tile.openInventory();
	}
}
