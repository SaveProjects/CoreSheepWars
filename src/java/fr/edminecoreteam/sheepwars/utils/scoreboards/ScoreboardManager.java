package fr.edminecoreteam.sheepwars.utils.scoreboards;

import fr.edminecoreteam.sheepwars.Core;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/*
 * This file is part of SamaGamesAPI.
 *
 * SamaGamesAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SamaGamesAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SamaGamesAPI.  If not, see <http://www.gnu.org/licenses/>.
 */
public class ScoreboardManager {
    private final Map<UUID, PersonalScoreboard> scoreboards;
	@SuppressWarnings("unused")
	private final ScheduledFuture<?> glowingTask;
	@SuppressWarnings("unused")
	private final ScheduledFuture<?> reloadingTask;
    private int ipCharIndex;

    private int waitCharIndex;
    private int waitcooldown;

    private int startCharIndex;
    private int startcooldown;
    private int cooldown;
 
    public ScoreboardManager() {
        scoreboards = new HashMap<>();
        waitCharIndex = 0;
        startCharIndex = 0;
        ipCharIndex = 0;
        cooldown = 10;
        waitcooldown = 20;
        startcooldown = 20;
 
        glowingTask = Core.getInstance().getScheduledExecutorService().scheduleAtFixedRate(() ->
        {
            String ip = colorIpAt();
            String wait = animWaitText();
            String start = animStartText();
            //String animWaitText = animWaitText();
            //String animStartText = animStartText();
            for (PersonalScoreboard scoreboard : scoreboards.values())
            	Core.getInstance().getExecutorMonoThread().execute(() -> scoreboard.setLines(ip));
        }, 80, 80, TimeUnit.MILLISECONDS);
        reloadingTask = Core.getInstance().getScheduledExecutorService().scheduleAtFixedRate(() ->
        {
            for (PersonalScoreboard scoreboard : scoreboards.values())
            	Core.getInstance().getExecutorMonoThread().execute(scoreboard::reloadData);
        }, 1, 1, TimeUnit.SECONDS);
    }
 
    public void onDisable() {
        scoreboards.values().forEach(PersonalScoreboard::onLogout);
    }
 
    public void onLogin(Player player) {
        if (scoreboards.containsKey(player.getUniqueId())) {
            return;
        }
        scoreboards.put(player.getUniqueId(), new PersonalScoreboard(player));
    }
 
    public void onLogout(Player player) {
        if (scoreboards.containsKey(player.getUniqueId())) {
            scoreboards.get(player.getUniqueId()).onLogout();
            scoreboards.remove(player.getUniqueId());
        }
    }

    public void fakeLogout(Player player) {
        if (scoreboards.containsKey(player.getUniqueId())) {
            scoreboards.get(player.getUniqueId()).fakeLogout();
            scoreboards.remove(player.getUniqueId());
        }
    }
 
    public void update(Player player) {
        if (scoreboards.containsKey(player.getUniqueId())) {
            scoreboards.get(player.getUniqueId()).reloadData();
        }
    }
    
    private String animWaitText() {
    	String attente = "Attente...";

        if (waitcooldown > 0) {
            waitcooldown--;
            return ChatColor.WHITE + attente;
        }
 
        StringBuilder formattedAttente = new StringBuilder();
 
        if (waitCharIndex > 0) {
            formattedAttente.append(attente.substring(0, waitCharIndex - 1));
            formattedAttente.append(ChatColor.GRAY).append(attente.substring(waitCharIndex - 1, waitCharIndex));
        } else {
            formattedAttente.append(attente.substring(0, waitCharIndex));
        }

        formattedAttente.append(ChatColor.DARK_GRAY).append(attente.charAt(waitCharIndex));
 
        if (waitCharIndex + 1 < attente.length()) {
            formattedAttente.append(ChatColor.GRAY).append(attente.charAt(waitCharIndex + 1));
 
            if (waitCharIndex + 2 < attente.length())
                formattedAttente.append(ChatColor.WHITE).append(attente.substring(waitCharIndex + 2));

            waitCharIndex++;
        } else {
            waitCharIndex = 0;
            waitcooldown = 70;
        }
 
        return ChatColor.WHITE + formattedAttente.toString();
    }

    private String animStartText() {
        String attente = "Démarrage...";

        if (startcooldown > 0) {
            startcooldown--;
            return ChatColor.GREEN + attente;
        }

        StringBuilder formattedAttente = new StringBuilder();

        if (startCharIndex > 0) {
            formattedAttente.append(attente.substring(0, startCharIndex - 1));
            formattedAttente.append(ChatColor.GREEN).append(attente.substring(startCharIndex - 1, startCharIndex));
        } else {
            formattedAttente.append(attente.substring(0, startCharIndex));
        }

        formattedAttente.append(ChatColor.DARK_GREEN).append(attente.charAt(startCharIndex));

        if (startCharIndex + 1 < attente.length()) {
            formattedAttente.append(ChatColor.GREEN).append(attente.charAt(startCharIndex + 1));

            if (startCharIndex + 2 < attente.length())
                formattedAttente.append(ChatColor.GREEN).append(attente.substring(startCharIndex + 2));

            startCharIndex++;
        } else {
            startCharIndex = 0;
            startcooldown = 70;
        }

        return ChatColor.GREEN + formattedAttente.toString();
    }
 
    private String colorIpAt() {
        String ip = "play.edmine.net";
 
        if (cooldown > 0) {
            cooldown--;
            return ChatColor.YELLOW + ip;
        }
 
        StringBuilder formattedIp = new StringBuilder();
 
        if (ipCharIndex > 0) {
            formattedIp.append(ip.substring(0, ipCharIndex - 1));
            formattedIp.append(ChatColor.GOLD).append(ip.substring(ipCharIndex - 1, ipCharIndex));
        } else {
            formattedIp.append(ip.substring(0, ipCharIndex));
        }
 
        formattedIp.append(ChatColor.RED).append(ip.charAt(ipCharIndex));
 
        if (ipCharIndex + 1 < ip.length()) {
            formattedIp.append(ChatColor.GOLD).append(ip.charAt(ipCharIndex + 1));
 
            if (ipCharIndex + 2 < ip.length())
                formattedIp.append(ChatColor.YELLOW).append(ip.substring(ipCharIndex + 2));
 
            ipCharIndex++;
        } else {
            ipCharIndex = 0;
            cooldown = 50;
        }
 
        return ChatColor.YELLOW + formattedIp.toString();
    }
 
}