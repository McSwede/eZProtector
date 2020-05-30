/*
 * eZProtector - Copyright (C) 2018-2020 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.sponge.listeners;

import com.github.donotspampls.ezprotector.sponge.Main;
import com.moandjiezana.toml.Toml;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.TabCompleteEvent;

import java.util.List;

public class TabCompletionListener {

    /**
     * Checks if a player is tab completing a forbidden command.
     *
     * @param event The tab complete event from which other information is gathered.
     */
    @Listener
    public void onTabComplete(TabCompleteEvent.Command event) {
        Toml config = Main.getConfig();
        final List<String> blocked = config.getList("tab-completion.commands");

        if (config.getBoolean("tab-completion.blocked") && event.getSource() instanceof Player) {
            Player player = (Player) event.getSource();
            String cmd = event.getRawMessage().replace(" ", "");
            List<String> completions = event.getTabCompletions();

            System.out.println(blocked);

            System.out.println(event.getTabCompletions());
            System.out.println(event.getRawMessage());

            if (completions.isEmpty()) return;

            if (!player.hasPermission("ezprotector.bypass.command.tabcomplete")) {
                if (!config.getBoolean("tab-completion.whitelist")) {
                    completions.removeIf(blocked::contains);
                    if (blocked.contains(cmd)) event.setCancelled(true);
                } else {
                    // TODO: ???????
                    completions.removeIf(lcmd -> !blocked.contains(lcmd));
                    for (String lcmd : blocked) {
                        if (lcmd.equalsIgnoreCase(cmd)) event.setCancelled(true);
                    }
                }
            }
        }
    }

}
