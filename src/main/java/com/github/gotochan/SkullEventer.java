package com.github.gotochan;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.milkbowl.vault.economy.Economy;

public class SkullEventer
{
	protected static SkullInventory sInventory;
	public SkullEventer() {
		sInventory = new SkullInventory();
	}

	/* 右クリックでアイテム呼び出し */
	public static void Interact(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();

		if ( item == null )
			return;

		if ( !(player.hasPermission("sg.use")) )
		{
			player.sendMessage("§c[SkullGetter] 権限がありません!");
			return;
		}

		if ( item.getType() == Material.SKULL_ITEM &&
				item.hasItemMeta() &&
				item.getItemMeta().hasDisplayName() &&
				item.getItemMeta().getDisplayName().contains("SkullGetter"))
		{
			if ( event.getAction() == Action.LEFT_CLICK_BLOCK )
				return;
			event.setCancelled(true);
			player.openInventory(sInventory.createInventory());
		}
	}


	public static void InventoryClick(InventoryClickEvent event)
	{
		Player clickplayer = (Player) event.getWhoClicked();
		Inventory inventory = event.getClickedInventory();
		ItemStack item = event.getCurrentItem();
		SkullInventory skullInventory = new SkullInventory();
		ItemStack skull;
		Economy econ = SkullGetter.econ;
		boolean isEnable = SkullConfigrable.isEnableEconomy;
		int value = SkullConfigrable.EconomyValue;

		if ( inventory == null )
			return;

		if ( item == null )
			return;

		if ( inventory.getName().equalsIgnoreCase("SkullInventory") )
		{
			event.setCancelled(true);
			clickplayer.playSound(clickplayer.getLocation(), Sound.CLICK, 10L, (float) 1.5);

			if ( item.getType() == Material.EMERALD )
			{
				clickplayer.openInventory(skullInventory.createInventory());
				clickplayer.sendMessage("§6[SkullGetter] 読み込みに時間がかかる場合があります。");
				return;
			}
			if ( !item.hasItemMeta() )
				return;

			if ( !item.getItemMeta().hasDisplayName() )
				return;

			if ( isEnable )
			{
				if ( econ.has(clickplayer, value) )
				{
					econ.withdrawPlayer(clickplayer, value);
					clickplayer.sendMessage("§a[SkullGetter] " + String.format("%s", econ.format(value)) +
							" が所持金から引かれました。");
				}
				else
				{
					clickplayer.sendMessage(
							"§c[SkullGetter] Skullをゲットするには " +
									String.format("%s", econ.format(value)) + " 必要です!");
					return;
				}
			}
			Player selected = Bukkit.getPlayer(item.getItemMeta().getDisplayName());
			String name = "§r" + selected.getName();
			clickplayer.getInventory().addItem(getSkullItem(selected.getName()));
			clickplayer.sendMessage("§a[SkullGetter] " + name + " の頭をインベントリに追加しました。");

		}
		else if ( inventory.getName().contains("MobSkullInventory") )
		{
			event.setCancelled(true);
			clickplayer.playSound(clickplayer.getLocation(), Sound.CLICK, 10L, (float) 1.5);
			if ( item.getType() == Material.DIAMOND_AXE )
			{
				clickplayer.openInventory(skullInventory.o_Inventory2);
				return;
			}
			else if ( item.getType() == Material.GOLD_AXE )
			{
				clickplayer.openInventory(skullInventory.o_Inventory);
				return;
			}
			if ( !item.hasItemMeta() )
				return;

			if ( !item.getItemMeta().hasDisplayName() )
				return;

			if ( isEnable )
			{
				if ( econ.has(clickplayer, value) )
				{
					econ.withdrawPlayer(clickplayer, value);
				}
				else
				{
					clickplayer.sendMessage(
							"§c[SkullGetter] Skullをゲットするには" +
									String.format("%s", econ.format(value)) + "必要です!");
					return;
				}
			}
			skull = getSkullItem(item.getItemMeta().getDisplayName());
			String name = "§r" + skull.getItemMeta().getDisplayName();
			clickplayer.sendMessage("§a[SkullGetter] " + name  + " の頭をインベントリに追加しました。");
			clickplayer.getInventory().addItem(skull);
			return;
		}
	}

	@SuppressWarnings("deprecation")
	private static ItemStack getSkullItem(String name)
	{
		OfflinePlayer offlinePlayer;
		Player player;
		SkullInventory skullInventory = new SkullInventory();

		if ( Bukkit.getServer().getPlayer(name) == null )
		{
			offlinePlayer = Bukkit.getOfflinePlayer(name);
			return skullInventory.getSkullOffline(offlinePlayer);
		}
		else
		{
			player = Bukkit.getServer().getPlayer(name);
			return skullInventory.getSkull(player);
		}
	}

	public static void InventoryDrag(InventoryDragEvent event)
	{
		Inventory inventory = event.getInventory();
		if ( inventory.getName().contains("SkullInventory"))
		{
			event.setCancelled(true);
			return;
		}
	}


}

