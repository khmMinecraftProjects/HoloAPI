/*
 * This file is part of HoloAPI.
 *
 * HoloAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HoloAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with HoloAPI.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.holoapi.config;

import com.dsh105.holoapi.HoloAPI;
import com.dsh105.powermessage.markup.MarkupBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.entity.Player;

public class LangSetting extends Setting<String> {

    public LangSetting(ConfigType configType, String path, String defaultValue, String... comments) {
        super(configType, path, defaultValue, comments);
    }

    public LangSetting(String path, String defaultValue, String... comments) {
        super(path, defaultValue, comments);
    }

    public String getValue(String... pairedReplacements) {
        String message = super.getValue();
        for (int i = 0; i < pairedReplacements.length; i += 2) {
            if ((i + 1) >= pairedReplacements.length) {
                break;
            }
            message = message.replace("%" + pairedReplacements[i] + "%", pairedReplacements[i + 1]);
        }

        if (message == null || message.isEmpty() || message.equalsIgnoreCase("NONE")) {
            return null;
        }
        return ChatColor.translateAlternateColorCodes('&', HoloAPI.getCommandManager().getMessenger().format(message));
    }

    public void send(Player player, String... pairedReplacements) {
        send((CommandSender) player, pairedReplacements);
    }

    // Not ideal, but it's easy to call
    public void send(CommandSender sender, String... pairedReplacements) {
        String message = getValue(pairedReplacements);
        if (message == null) {
            return;
        }
        new MarkupBuilder().withText(HoloAPI.getPrefix() + ChatColor.translateAlternateColorCodes('&', message)).build().send(sender);
    }

    public void send(Conversable conversable, String... pairedReplacements) {
        send(conversable, getValue(pairedReplacements));
    }

    public static void send(CommandSender sender, String message) {
        if (message == null) {
            return;
        }
        new MarkupBuilder().withText(HoloAPI.getPrefix() + ChatColor.translateAlternateColorCodes('&', message)).build().send(sender);
    }

    public static void send(Conversable conversable, String message) {
        if (conversable instanceof CommandSender) {
            send((CommandSender) conversable, message);
            return;
        }

        if (message != null) {
            conversable.sendRawMessage(HoloAPI.getPrefix() + ChatColor.translateAlternateColorCodes('&', message));
        }
    }
}