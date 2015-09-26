package vexatos.manualtab;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import li.cil.oc.api.Manual;
import li.cil.oc.api.manual.TabIconRenderer;
import li.cil.oc.api.prefab.ItemStackTabIconRenderer;
import li.cil.oc.api.prefab.TextureTabIconRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import vexatos.manualtab.manual.ConfigContentProvider;
import vexatos.manualtab.proxy.CommonProxy;
import vexatos.manualtab.reference.Mods;

import java.util.logging.Logger;

/**
 * @author Vexatos
 */
@Mod(modid = Mods.ManualTab, name = Mods.ManualTab_NAME, version = "@VERSION@",
	dependencies = "required-after:" + Mods.OpenComputers + "@[1.5.17,)")
public class ManualTab {

	@Mod.Instance(Mods.ManualTab)
	public static ManualTab instance;

	@SidedProxy(
		clientSide = "vexatos.manualtab.proxy.ClientProxy",
		serverSide = "vexatos.manualtab.proxy.CommonProxy"
	)
	public static CommonProxy proxy;

	public static Configuration config;
	public static Logger log;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		config = new Configuration(e.getSuggestedConfigurationFile());
		config.load();
		log = Logger.getLogger(Mods.ManualTab_NAME);
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		boolean enable = config.getBoolean("enable", "general", true, "If this is false, the mod will not do anything at all.");
		if(enable) {
			final String prefix = config.getString("pathPrefix", "manual", "manualtab", "The prefix with which to reference manual pages.\nSetting this to 'potato' would allow referencing a manual file at this config folder's 'manualtab/en_US/fish.md'\n"
				+ "from inside the actual manual by writing 'potato/%LANGUAGE%/fish.md' into the referencing markdown page (relative paths work too, of course).");
			Manual.addProvider(new ConfigContentProvider(prefix));
			int iconMode = config.getInt("tabIconMode", "manual", 1, 0, 1, "If this value is 0, tabIcon must be the path to a texture in some mod file's assets folder. It can be changed using Resource Packs.\nIf this value is 1, tabIcon must be an ItemStack to display");
			String icon = config.getString("tabIcon", "manual", "minecraft:potato@0", "Texture path: 'modid:path/to/texture', example: 'minecraft:textures/items/brick.png'. Note that it will have a black background.\nItemStack: 'modid:itemname@metadata', example: 'minecraft:potato'. metadata is optional, default 0.");
			TabIconRenderer r;

			if(iconMode == 1) {
				String[] strings = icon.split("@");
				int meta;
				if(strings.length > 1) {
					try {
						meta = Integer.parseInt(strings[1]);
					} catch(NumberFormatException ex) {
						config.save();
						proxy.throwBadConfigException(icon, ex);
						return;
					}
				} else if(strings.length == 1) {
					meta = 0;
				} else {
					config.save();
					proxy.throwBadConfigException(icon);
					return;
				}
				Object object = Item.itemRegistry.getObject(strings[0]);
				if(object instanceof Item) {
					r = new ItemStackTabIconRenderer(new ItemStack((Item) object, 1, meta));
				} else {
					r = new TextureTabIconRenderer(new ResourceLocation("opencomputers", "textures/gui/manual_missing_item.png"));
				}
			} else {
				r = new TextureTabIconRenderer(new ResourceLocation(icon));
			}
			String mainPath = config.getString("tabPath", "manual", "%LANGUAGE%/index.md", "The path to the manual page that will open when the tab is clicked.\n'%LANGUAGE%' will be replaced with the currently selected language, for instance 'en_US'");
			String tabName = config.getString("tabName", "manual", "tooltip.manualtab.manual.tab", "The name of the tab. May be a string to display or a localization key which can be changed using resource packs.");
			Manual.addTab(r, tabName, prefix + "/" + mainPath);
		} else {
			log.info("Custom OpenComputers Manual Tab disabled.");
		}
		config.save();
	}
}
