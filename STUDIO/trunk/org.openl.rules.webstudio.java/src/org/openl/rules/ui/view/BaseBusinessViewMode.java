package org.openl.rules.ui.view;

import org.openl.rules.lang.xls.IXlsTableNames;
import org.openl.rules.lang.xls.syntax.TableSyntaxNode;
import org.openl.rules.table.properties.ITableProperties;
import org.openl.rules.ui.OpenLWrapperInfo;
import org.openl.util.StringTool;

public abstract class BaseBusinessViewMode extends WebStudioViewMode {

    private static final String[][] folders = { { "By Type", "Organize Project by component type", "" } };

    @Override
    public String getDisplayName(OpenLWrapperInfo wrapper) {

        String displayName = wrapper.getDisplayName();

        if (displayName.equals(wrapper.getWrapperClassName())) {
            displayName = StringTool.lastToken(displayName, ".");
        }

        return displayName;
    }

    @Override
    public String getTableMode() {
        return IXlsTableNames.VIEW_BUSINESS;
    }

    @Override
    public Object getType() {
        return WebStudioViewMode.BUSINESS_MODE_TYPE;
    }

    @Override
    public boolean select(TableSyntaxNode tsn) {

        String view = null;
        String name = null;

        ITableProperties tableProperties = tsn.getTableProperties();

        if (tableProperties != null) {
            view = tableProperties.getPropertyValueAsString("view");
            name = tableProperties.getPropertyValueAsString("name");
        }

        return name != null && (view == null || view.indexOf(IXlsTableNames.VIEW_BUSINESS) >= 0);
    }

    @Override
    public String[][] getFolders() {
        return folders;
    }
}