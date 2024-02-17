package fr.edminecoreteam.sheepwars.content.game.teams.displayname;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class TabListManager 
{
	//private static char COLOUR_CHAR = '§';
	private int position;
	private List<String> list;
	private ChatColor colour;
	    
	    public TabListManager(String message, int width, int spaceBetween, char colourChar) {
	        this.colour = ChatColor.RESET;
	        this.list = new ArrayList<String>();
	        if (message.length() < width) {
	            StringBuilder sb = new StringBuilder(message);
	            while (sb.length() < width) {
	                sb.append(" ");
	            }
	            message = sb.toString();
	        }
	        width -= 2;
	        if (width < 1) {
	            width = 1;
	        }
	        if (spaceBetween < 0) {
	            spaceBetween = 0;
	        }
	        if (colourChar != '§') {
	            message = ChatColor.translateAlternateColorCodes(colourChar, message);
	        }
	        for (int i = 0; i < message.length() - width; ++i) {
	            list.add(message.substring(i, i + width));
	        }
	        StringBuilder space = new StringBuilder();
	        for (int j = 0; j < spaceBetween; ++j) {
	            list.add(String.valueOf(message.substring(message.length() - width + ((j > width) ? width : j), message.length())) + (Object)space);
	            if (space.length() < width) {
	                space.append(" ");
	            }
	        }
	        for (int j = 0; j < width - spaceBetween; ++j) {
	            list.add(String.valueOf(message.substring(message.length() - width + spaceBetween + j, message.length())) + (Object)space + message.substring(0, j));
	        }
	        for (int j = 0; j < spaceBetween && j <= space.length(); ++j) {
	            list.add(String.valueOf(space.substring(0, space.length() - j)) + message.substring(0, width - ((spaceBetween > width) ? width : spaceBetween) + j));
	        }
	    }
	    
	    public String next() {
	        StringBuilder sb = getNext();
	        if (sb.charAt(sb.length() - 1) == '§') {
	            sb.setCharAt(sb.length() - 1, ' ');
	        }
	        if (sb.charAt(0) == '§') {
	            ChatColor c = ChatColor.getByChar(sb.charAt(1));
	            if (c != null) {
	                colour = c;
	                sb = getNext();
	                if (sb.charAt(0) != ' ') {
	                    sb.setCharAt(0, ' ');
	                }
	            }
	        }
	        return colour + sb.toString();
	    }
	    
	    private StringBuilder getNext() {
	        return new StringBuilder(list.get(position++ % list.size()).substring(0));
	    }
}
