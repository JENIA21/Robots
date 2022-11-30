package gui.menu;

import javax.swing.*;
import java.awt.event.ActionListener;

public class MenuGenerator {
    public static JMenu createMenu(String text, String description, int key){
        var menu = new JMenu(text);
        menu.setMnemonic(key);
        menu.getAccessibleContext().setAccessibleDescription(description);
        return menu;
    }
    public static JMenuItem createMenuItem(String text, int key, ActionListener event){
        var menuItem = new JMenuItem(text);
        menuItem.setMnemonic(key);
        menuItem.addActionListener(event);
        return menuItem;
    }
}
