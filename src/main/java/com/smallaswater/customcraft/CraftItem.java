package com.smallaswater.customcraft;

import cn.nukkit.item.Item;

class CraftItem {

    private static final String SPLIT_1 = "@";
    private static final String SPLIT_2 = ":";
    private static final String NBT = "nbt";
    private static final String NOT = "not";

    static Item toItem(String str) {
        if (str.split(SPLIT_1).length > 1) {
            String itemString = str.split(SPLIT_1)[0];
            if (NBT.equals(str.split(SPLIT_1)[1])) {
                String configString = CustomCraft.getNbtItem(itemString);
                if (!"".equals(configString)) {
                    String[] items = configString.split(SPLIT_2);
                    Item item = Item.get(Integer.parseInt(items[0]));
                    item.setDamage(Integer.parseInt(items[1]));
                    item.setCount(Integer.parseInt(items[2]));
                    if (!NOT.equals(items[3])) {
                        item.setCompoundTag(Tools.hexStringToBytes(items[3]));
                    }
                    return item;
                }
            }
            return toStringItem(itemString);
        } else {
            return toStringItem(str);
        }
    }

    private static Item toStringItem(String i) {
        String[] items = i.split(SPLIT_2);
        if (items.length > 1) {
            if (items.length > 2) {
                return Item.get(Integer.parseInt(items[0]), Integer.parseInt(items[1]), Integer.parseInt(items[2]));
            }
            return Item.get(Integer.parseInt(items[0]), Integer.parseInt(items[1]));
        }
        return Item.get(0);
    }


    static boolean isCraftItem(String string) {
        if (!"".equals(string)) {
            Item item = toItem(string);
            return (item.getId() != 0);
        }
        return false;
    }

    static boolean isCraftItem(Item item) {
        if (item != null) {
            return (item.getId() != 0);
        }
        return false;
    }

    static String toStringItem(Item item) {
        String tag = "not";
        if (item.hasCompoundTag()) {
            tag = Tools.bytesToHexString(item.getCompoundTag());
        }
        return item.getId() + ":" + item.getDamage() + ":" + item.getCount() + ":" + tag;
    }

}
