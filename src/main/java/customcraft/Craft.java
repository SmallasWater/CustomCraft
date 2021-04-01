package customcraft;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.inventory.CraftingManager;
import cn.nukkit.inventory.FurnaceRecipe;
import cn.nukkit.inventory.ShapedRecipe;
import cn.nukkit.inventory.ShapelessRecipe;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.Utils;
import io.netty.util.collection.CharObjectHashMap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Craft extends PluginBase {

    private static Craft instance;

    private Config craft = null;
    private Config nbt = null;
    private Config frame = null;

    private static final String CRAFT = "/craft.json";
    private static final String FRAME = "/frame.json";
    private static final String NBT_ITEM = "/nbtItems.json";

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        this.loadCraftConfig();
        this.loadFrameConfig();
        this.getCraftItemConfig();
        this.registerCraft();
        this.registerFrame();

        this.getLogger().info("自定义合成加载成功!");
    }

    private void registerFrame() {
        Map<String, Object> strings = frame.getAll();
        if (strings.size() > 0) {
            for (String outputName : strings.keySet()) {
                Object o = strings.get(outputName);
                if (o instanceof Map) {
                    Item outputItem = CraftItem.toItem((String) ((Map) o).get("output"));
                    if (CraftItem.isCraftItem(outputItem)) {
                        Item inputItem = CraftItem.toItem((String) ((Map) o).get("input"));
                        Server.getInstance().getCraftingManager().registerFurnaceRecipe(new FurnaceRecipe(outputItem, inputItem));
                        Server.getInstance().getCraftingManager().rebuildPacket();
                    }
                }
            }
        }
    }

    private void registerCraft() {
        CraftingManager manager = this.getServer().getCraftingManager();
        List<Map> recipes = craft.getMapList("recipes");
        if (recipes.size() > 0) {
            for (Map recipe : recipes) {
                if (Utils.toInt(recipe.get("type")) == 0) {
                    Item outputItem = CraftItem.toItem((String) recipe.get("output"));
                    if (!Item.isCreativeItem(outputItem)) {
                        Item.addCreativeItem(outputItem);
                    }
                    ArrayList<Item> inputItems = new ArrayList<>();
                    List items = (List) recipe.get("input");
                    for (Object s : items) {
                        if (s instanceof String) {
                            if (CraftItem.isCraftItem((String) s)) {
                                Item input = CraftItem.toItem((String) s);
                                inputItems.add(input);
                            }
                        }
                    }
                    ShapelessRecipe result = new ShapelessRecipe(outputItem, inputItems);
                    manager.registerRecipe(result);
                } else {
                    Item outputItem = CraftItem.toItem((String) recipe.get("output"));
                    if (!Item.isCreativeItem(outputItem)) {
                        Item.addCreativeItem(outputItem);
                    }
                    String[] shape = (String[]) ((List) recipe.get("shape")).toArray(new String[0]);
                    Map input = (Map) recipe.get("input");
                    Map<Character, Item> ingredients = new CharObjectHashMap<>();
                    for (Object s : input.keySet()) {
                        if (s instanceof String) {
                            if (CraftItem.isCraftItem((String) input.get(s))) {
                                Item inputs = CraftItem.toItem((String) input.get(s));
                                ingredients.put(((String) s).charAt(0), inputs);
                            }
                        }
                    }

                    manager.registerRecipe(new ShapedRecipe(outputItem, shape, ingredients, new LinkedList<>()));
                }
            }
        }
        manager.rebuildPacket();
    }

    private static Craft getInstance() {
        return instance;
    }

    private void loadCraftConfig() {
        this.saveResource("craft.json");
        this.craft = new Config(this.getDataFolder() + CRAFT, Config.JSON);
    }

    private void loadFrameConfig() {
        this.saveResource("frame.json");
        this.frame = new Config(this.getDataFolder() + FRAME, Config.JSON);
    }

    private Config getCraftItemConfig() {
        this.saveResource("nbtItems.json");
        if (this.nbt == null) {
            this.nbt = new Config(this.getDataFolder() + NBT_ITEM, Config.JSON);
        }
        return this.nbt;
    }

    static String getNbtItem(String string) {
        return Craft.getInstance().getCraftItemConfig().getString(string);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ("craft".equals(command.getName())) {
            if (sender instanceof Player) {
                if (args.length > 0) {
                    if ("add".equals(args[0])) {
                        if (args.length > 1) {
                            String name = args[1];
                            String tag = getNbtItem(name);
                            if (tag != null && !"".equals(tag)) {
                                sender.sendMessage(TextFormat.RED + "此物品名称已经存在..");
                                return true;
                            } else {
                                Item item = ((Player) sender).getInventory().getItemInHand();
                                Config config = getCraftItemConfig();
                                config.set(name, CraftItem.toStringItem(item));
                                config.save();
                                sender.sendMessage(TextFormat.GREEN + "名称" + name + "已保存到/nbtItems.json");
                                return true;
                            }
                        }
                    }
                }
            } else {
                sender.sendMessage(TextFormat.RED + "不要用控制台执行");
                return true;
            }

        }
        return false;
    }

    public static boolean toSaveItem(String name, Item item) {
        String tag = getNbtItem(name);
        if (tag != null && !"".equals(tag)) {
            return false;
        } else {
            Config config = Craft.getInstance().getCraftItemConfig();
            config.set(name, CraftItem.toStringItem(item));
            config.save();
            return true;
        }
    }

}
