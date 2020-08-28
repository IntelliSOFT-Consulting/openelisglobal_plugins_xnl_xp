package com.intellisoftkenya.oe.plugin.analyzer;

import java.util.Locale;

import org.openelisglobal.common.services.PluginMenuService;
import org.openelisglobal.common.services.PluginMenuService.KnownMenu;
import org.openelisglobal.menu.valueholder.Menu;
import org.openelisglobal.plugin.MenuPlugin;

public class XnlXpMenu extends MenuPlugin {

    @Override
    protected void insertMenu() {
        PluginMenuService service = PluginMenuService.getInstance();
        Menu menu = new Menu();

        menu.setParent(PluginMenuService.getInstance().getKnownMenu(KnownMenu.ANALYZER, "menu_results"));
        menu.setPresentationOrder(5);
        menu.setElementId("sysmex_xnl_analyzer_plugin");
        menu.setActionURL("/AnalyzerResults.do?type=SysmexXnlAnalyzer");
        menu.setDisplayKey("banner.menu.results.sysmex.xnl");
        menu.setOpenInNewWindow(false);

        service.addMenu(menu);
        service.insertLanguageKeyValue("banner.menu.results.sysmex.xnl", "Sysmex XN-L", Locale.ENGLISH.toLanguageTag());
        service.insertLanguageKeyValue("banner.menu.results.sysmex.xnl", "Sysmex XN-L", Locale.FRENCH.toLanguageTag());
    }
    
}