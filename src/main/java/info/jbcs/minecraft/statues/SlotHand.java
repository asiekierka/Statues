/**
 * Slot class for the slot hand
 */

package info.jbcs.minecraft.statues;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.IIcon;

public class SlotHand extends Slot {
	public SlotHand(IInventory iinventory, int i, int j, int k) {
		super(iinventory, i, j, k);
	}

	public SlotHand(TileEntityStatue te, int i, int j, int k) {
		super(te, i, j, k);
	}

	/**
	 * Returns the icon index on items.png that is used as background image of
	 * the slot.
	 */
	@Override
	public IIcon getBackgroundIconIndex() {
		return Statues.slotHand;
	}
}