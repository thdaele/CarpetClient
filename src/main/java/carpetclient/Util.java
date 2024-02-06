package carpetclient;

import net.minecraft.client.Minecraft;
import net.minecraft.text.LiteralText;

public class Util {

    /**
     * Prints a text to the chat
     * @param text The text to be printed.
     */
    public static void printToChat(String text){
        Minecraft.getInstance().gui.getChat().addMessage(new LiteralText(text));
    }
}
