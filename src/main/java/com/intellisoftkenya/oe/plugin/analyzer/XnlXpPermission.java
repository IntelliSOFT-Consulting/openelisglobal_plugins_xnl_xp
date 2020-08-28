package com.intellisoftkenya.oe.plugin.analyzer;

import org.openelisglobal.common.services.PluginPermissionService;
import org.openelisglobal.plugin.PermissionPlugin;
import org.openelisglobal.role.valueholder.Role;
import org.openelisglobal.systemmodule.valueholder.SystemModule;

public class XnlXpPermission extends PermissionPlugin {

    @Override
    protected boolean insertPermission() {
        PluginPermissionService service = new PluginPermissionService();
        SystemModule module = service.getOrCreateSystemModule( "AnalyzerResults", "SysmexXnlAnalyzer", "Results->Analyzer->SysmexXnlAnalyzer" );
        Role role = service.getSystemRole( "Results entry" );
        return service.bindRoleToModule( role, module );
    }
    
}