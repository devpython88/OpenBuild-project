/*
 * Decompiled with CFR 0.152.
 */
package org.seriouz.openbuild;

import javax.swing.JOptionPane;

import org.seriouz.openbuild.builders.BlockParameterBuilder;
import org.seriouz.openbuild.managers.BlockManager;
import org.seriouz.openbuild.managers.EntityManager;
import org.seriouz.openbuild.user.Player;
import org.seriouz.openbuild.utilities.MsgBox;

public class CommandExecutioner {
    public static void execute(String command, String[] args, Player player, EntityManager entityManager, BlockManager blockManager, BlockParameterBuilder blockParameterBuilder) {
        if (command.equals("go")) {
            if (args.length < 2) {
                JOptionPane.showMessageDialog(null, "Not enough arguments", "Error", 0);
                return;
            }
            String sX = args[0];
            String sY = args[1];
            int x = Integer.parseInt(sX);
            int y = Integer.parseInt(sY);
            player.x = x * 16;
            player.y = y * 16;
        } else if (command.equals("summon")) {
            if (args.length < 3) {
                JOptionPane.showMessageDialog(null, "Not enough arguments", "Error", 0);
                return;
            }
            String name = args[0];
            int x = Integer.parseInt(args[1]) * 16;
            int y = Integer.parseInt(args[2]) * 16;
            entityManager.createEntity(name, x, y, blockManager);
        }
    }
}
