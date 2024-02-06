package carpetclient.rules;

import carpetclient.Config;
import carpetclient.gui.ConfigGUI;
import carpetclient.pluginchannel.CarpetPluginChannel;
import carpetclient.random.RandomtickDisplay;
import carpetclient.util.BiObservable;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;

/*
Carpet rules that is recieved from the server, stored here for use to update GUI and other client related options.
 */
public class CarpetRules {
    private static PacketByteBuf data;
    private static final Map<String, CarpetSettingEntry> rules;

    private static final int CHANGE_RULE = 0;
    private static final int CHANGE_TEXT_RULE = 1;
    private static final int RESET_RULE = 2;
    private static final int REQUEST_RULE_TIP = 3;

    static {
        rules = new HashMap<>();
    }

    /**
     * Setter to set the packet recieved from the server.
     *
     * @param data The data that is recieved from the server.
     */
    public static void setAllRules(PacketByteBuf data) {
        CarpetRules.data = data;
        decodeData();
        editClientRules();
    }

    /**
     * Edits the client rules based on server rules.
     */
    private static void editClientRules() {
        if (hasRule("relaxedBlockPlacement"))
            Config.relaxedBlockPlacement = getRule("relaxedBlockPlacement").getBoolean();
        if (hasRule("accurateBlockPlacement"))
            Config.accurateBlockPlacement = getRule("accurateBlockPlacement").getBoolean();
        if (hasRule("ctrlQCrafting"))
            Config.controlQCrafting = getRule("ctrlQCrafting").getBoolean();
        if (hasRule("missingTools"))
            Config.missingTools = getRule("missingTools").getBoolean();
        //if (hasRule("structureBlockLimit"))
        //    Config.structureBlockLimit = getRule("structureBlockLimit").integer;
        if (hasRule("pushLimit"))
            Config.pushLimit = getRule("pushLimit").integer;
        if (hasRule("disablePlayerCollision"))
            Config.playerCollisions = !getRule("disablePlayerCollision").getBoolean();
        if (hasRule("ignoreEntityWhenPlacing"))
            Config.ignoreEntityWhenPlacing = getRule("ignoreEntityWhenPlacing").getBoolean();
        if (hasRule("movableTileEntities"))
            Config.movableTileEntities = getRule("movableTileEntities").getBoolean();
        if (hasRule("pistonGhostBlocksFix"))
            Config.pistonGhostBlocksFix = getRule("pistonGhostBlocksFix").getCurrentOption();
        if (hasRule("stackableShulkersPlayerInventory"))
            Config.stackableShulkersPlayerInventory = getRule("stackableShulkersPlayerInventory").getBoolean();
        TickRate.setTickRate(Config.tickRate);
    }

    /**
     * Data recieved from server updating a single rule.
     *
     * @param data the data related to a single rule.
     */
    public static void ruleData(PacketByteBuf data) {
        String rule = data.readString(100);
        int infoType = data.readInt();
        String text = data.readString(10000);

        if (CHANGE_RULE == infoType) {
            getRule(rule).changeRule(text);
        } else if (REQUEST_RULE_TIP == infoType) {
            getRule(rule).setRuleTip(text);
        }
    }

    /**
     * Returns the list of rules synced with the server.
     *
     * @return returns the list of all rules.
     */
    public static ArrayList<CarpetSettingEntry> getAllRules() {
        ArrayList<CarpetSettingEntry> res = new ArrayList<>();
        for (String rule : rules.keySet().stream().sorted().collect(Collectors.toList())) {
            res.add(rules.get(rule));
        }
        return res;
    }

    public static void requestUpdate() {
        PacketByteBuf sender = new PacketByteBuf(Unpooled.buffer());
        sender.writeInt(CarpetPluginChannel.GUI_ALL_DATA);

        CarpetPluginChannel.packatSender(sender);
    }

    /**
     * Requests server to change a rule.
     *
     * @param rule the rule name.
     */
    public static void ruleChange(String rule) {
        PacketByteBuf sender = new PacketByteBuf(Unpooled.buffer());
        sender.writeInt(CarpetPluginChannel.RULE_REQUEST);
        sender.writeInt(CHANGE_RULE);
        sender.writeString(rule);

        CarpetPluginChannel.packatSender(sender);
    }

    /**
     * Requests server to change a rule with given text.
     *
     * @param rule the rule name
     * @param text the text to change the rule with
     */
    public static void textRuleChange(String rule, String text) {
        PacketByteBuf sender = new PacketByteBuf(Unpooled.buffer());
        sender.writeInt(CarpetPluginChannel.RULE_REQUEST);
        sender.writeInt(CHANGE_TEXT_RULE);
        sender.writeString(rule);
        sender.writeString(text);

        CarpetPluginChannel.packatSender(sender);
    }

    /**
     * Requests server to reset a rule.
     *
     * @param rule the rule name.
     */
    public static void resetRule(String rule) {
        PacketByteBuf sender = new PacketByteBuf(Unpooled.buffer());
        sender.writeInt(CarpetPluginChannel.RULE_REQUEST);
        sender.writeInt(RESET_RULE);
        sender.writeString(rule);

        CarpetPluginChannel.packatSender(sender);
    }

    /**
     * Requests server to send info on a rule.
     *
     * @param rule the rule name.
     */
    public static void ruleTipRequest(String rule) {
        PacketByteBuf sender = new PacketByteBuf(Unpooled.buffer());
        sender.writeInt(CarpetPluginChannel.RULE_REQUEST);
        sender.writeInt(REQUEST_RULE_TIP);
        sender.writeString(rule);

        CarpetPluginChannel.packatSender(sender);
    }

    /**
     * Decoder for the packet into rules.
     */
    private static void decodeData() {
        NbtCompound nbt;
        try {
            nbt = data.readNbtCompound();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        String carpetServerVersion = nbt.getString("carpetVersion");
        Config.tickRate = nbt.getFloat("tickrate");
        NbtList nbttaglist = nbt.getList("ruleList", 10);
        for (int i = 0; i < nbttaglist.size(); i++) {
            NbtCompound ruleNBT = (NbtCompound) nbttaglist.get(i);

            String rule = ruleNBT.getString("rule");
            String current = ruleNBT.getString("current");
            String def = ruleNBT.getString("default");
            boolean isFloat = ruleNBT.getBoolean("isfloat");

            if (hasRule(rule)) {
                getRule(rule).update(current, null, def, isFloat);
            } else {
                rules.put(rule, new CarpetSettingEntry(rule, current, null, def, isFloat));
            }
        }
        int netVersion = nbt.getInt("netVersion");
        applyNetVersionRules(netVersion);
        ConfigGUI.setServerVersion(carpetServerVersion);
    }

    /**
     * Gets a specific rule.
     *
     * @param rule String representation of the rule.
     * @return returns the rule that is requested.
     */
    public static CarpetSettingEntry getRule(String rule) {
        return rules.get(rule);
    }

    public static boolean hasRule(String rule) {
        return rules.containsKey(rule);
    }

    public static void resetToDefaults() {
        rules.values().forEach(rule -> rule.changeRule(rule.defaultOption));
        RandomtickDisplay.reset();
    }

    /**
     * Applys changes to carpet servers above specific versions
     *
     * @param netVersion The version the server is at.
     */
    private static void applyNetVersionRules(int netVersion) {
        if(netVersion >= 1){
            Config.betterMiner = true;
        }
    }

    /*
     * Class that stores the detailed rules.
     */
    public static class CarpetSettingEntry extends BiObservable<CarpetSettingEntry, String>
    {
        private String rule;
        private String currentOption;
        private boolean isNumber;
        private String[] options;
        private String defaultOption;
        private boolean isDefault;
        private String ruleTip;
        private boolean isFloat;

        private int integer;
        private float flt;
        private boolean bool;

        public CarpetSettingEntry(String rule, String currentOption, String[] options, String defaultOption, boolean isFloat) {
            this.rule = rule;
            this.ruleTip = "";
            this.update(currentOption, options, defaultOption, isFloat);
        }

        public void update(String currentOption, String[] options, String defaultOption, boolean isFloat) {
            this.currentOption = currentOption;
            this.options = options;
            this.defaultOption = defaultOption;
            this.isFloat = isFloat;
            this.checkValues();
            this.checkDefault();
            editClientRules();

            this.notifySubscribers(this.currentOption);
        }

        private void checkValues() {
            this.bool = Boolean.parseBoolean(this.currentOption);

            try {
                this.integer = Integer.parseInt(this.currentOption);
            } catch (NumberFormatException e) {
                this.integer = 0;
            }

            try {
                this.flt = Float.parseFloat(this.currentOption);
                this.isNumber = true;
            } catch (NumberFormatException e) {
                this.isNumber = false;
                this.flt = 0.0F;
            }
        }

        /**
         * Checks if the default is the same as the set rule
         */
        private void checkDefault() {
            isDefault = !currentOption.equals(defaultOption);
        }

        /**
         * Getter for the rule name
         *
         * @return rule name
         */
        public String getRule() {
            return rule;
        }

        /**
         * Getter for the currently selected rule value.
         *
         * @return rule value
         */
        public String getCurrentOption() {
            return currentOption;
        }

        /**
         * Getter for the default being true or false. The rule being in the reset or not.
         *
         * @return default value
         */
        public boolean isDefault() {
            return isDefault;
        }

        /**
         * Getter for the option to be a number or a toggleable option.
         *
         * @return number or button
         */
        public boolean isNumber() {
            return isNumber;
        }

        /**
         * A setter for the tooltip of each rule.
         *
         * @param ruletip the value that is going to be set as the tooltip.
         */
        public void setRuleTip(String ruletip) {
            ruleTip = ruletip;
        }

        /**
         * Getter for the tooltip.
         *
         * @return returns the tooltip text
         */
        public String getRuleTip() {
            return ruleTip;
        }

        public void changeRule(String change) {
            this.currentOption = change;
            this.checkValues();
            this.checkDefault();
            editClientRules();

            this.notifySubscribers(this.currentOption);
        }

        /**
         * Checks if this rule uses a floating point or integer when doing the text field restrictions.
         *
         * @return returns true for is integer field.
         */
        public boolean useInteger() {
            return !isFloat;
        }

        /**
         * Gets the boolean value of the rule.
         *
         * @return returns the boolean value of the server rule.
         */
        public boolean getBoolean() {
            return bool;
        }
    }
}
