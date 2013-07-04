package org.openl.rules.table.properties.def;

import org.openl.message.Severity;
import org.openl.rules.lang.xls.XlsNodeTypes;
import org.openl.rules.table.properties.def.TablePropertyDefinition.SystemValuePolicy;
import org.openl.rules.table.properties.inherit.InheritanceLevel;

/**
 * Definitions of supported properties.
 * @author snshor
 * Created Jul 21, 2009 
 *
 */
public class DefaultPropertyDefinitions 
{
    private static final TablePropertyDefinition[] definitions;  
    
    static {  
        // <<< INSERT TablePropertiesDefinition >>>
		definitions = new TablePropertyDefinition[33];
		definitions[0] = new TablePropertyDefinition();
		definitions[0].setConstraints(new org.openl.rules.table.constraints.Constraints("unique in:module"));
		definitions[0].setDeprecation("removed");
		definitions[0].setDescription("Deprecated. The name of the table displayed in OpenL Tablets");
		definitions[0].setDimensional(false);
		definitions[0].setDisplayName("Name");
		definitions[0].setErrorSeverity(Severity.WARN);
		definitions[0].setGroup("Info");
		definitions[0].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.TABLE});
		definitions[0].setName("name");
		definitions[0].setPrimaryKey(false);
		definitions[0].setSecurityFilter("no");
		definitions[0].setSystem(false);
		definitions[0].setType(org.openl.types.java.JavaOpenClass.getOpenClass(java.lang.String.class));
		definitions[1] = new TablePropertyDefinition();
		definitions[1].setConstraints(new org.openl.rules.table.constraints.Constraints("no"));
		definitions[1].setDescription("The category of the table. For a two-level category use the <category>-<subcateg"
		 + "ory> format.");
		definitions[1].setDimensional(false);
		definitions[1].setDisplayName("Category");
		definitions[1].setGroup("Info");
		definitions[1].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.CATEGORY, InheritanceLevel.TABLE});
		definitions[1].setName("category");
		definitions[1].setPrimaryKey(false);
		definitions[1].setSecurityFilter("yes (coma separated filter specification by user role: category/role pairs)");
		definitions[1].setSystem(false);
		definitions[1].setType(org.openl.types.java.JavaOpenClass.getOpenClass(java.lang.String.class));
		definitions[2] = new TablePropertyDefinition();
		definitions[2].setConstraints(new org.openl.rules.table.constraints.Constraints("no"));
		definitions[2].setDescription("Any additional information to clarify the use of the table");
		definitions[2].setDimensional(false);
		definitions[2].setDisplayName("Description");
		definitions[2].setGroup("Info");
		definitions[2].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.TABLE});
		definitions[2].setName("description");
		definitions[2].setPrimaryKey(false);
		definitions[2].setSecurityFilter("no");
		definitions[2].setSystem(false);
		definitions[2].setType(org.openl.types.java.JavaOpenClass.getOpenClass(java.lang.String.class));
		definitions[3] = new TablePropertyDefinition();
		definitions[3].setConstraints(new org.openl.rules.table.constraints.Constraints("no"));
		definitions[3].setDescription("Comma separated tags to be used for search, navigation, etc");
		definitions[3].setDimensional(false);
		definitions[3].setDisplayName("Tags");
		definitions[3].setGroup("Info");
		definitions[3].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.TABLE});
		definitions[3].setName("tags");
		definitions[3].setPrimaryKey(false);
		definitions[3].setSecurityFilter("no");
		definitions[3].setSystem(false);
		definitions[3].setType(org.openl.types.java.JavaOpenClass.getOpenClass(java.lang.String[].class));
		definitions[4] = new TablePropertyDefinition();
		definitions[4].setConstraints(new org.openl.rules.table.constraints.Constraints("< expirationDate"));
		definitions[4].setDescription("The starting date of the time interval within which the rule table is active");
		definitions[4].setDimensional(true);
		definitions[4].setDisplayName("Effective Date");
		definitions[4].setExpression(new org.openl.rules.table.properties.expressions.match.MatchingExpression("le(currentDate)"));
		definitions[4].setFormat("MM/dd/yyyy");
		definitions[4].setGroup("Business Dimension");
		definitions[4].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.MODULE, InheritanceLevel.CATEGORY, InheritanceLevel.TABLE});
		definitions[4].setName("effectiveDate");
		definitions[4].setPrimaryKey(true);
		definitions[4].setSecurityFilter("no");
		definitions[4].setSystem(false);
		definitions[4].setType(org.openl.types.java.JavaOpenClass.getOpenClass(java.util.Date.class));
		definitions[5] = new TablePropertyDefinition();
		definitions[5].setConstraints(new org.openl.rules.table.constraints.Constraints("> effectiveDate"));
		definitions[5].setDescription("The end date after which the rule table becomes inactive");
		definitions[5].setDimensional(true);
		definitions[5].setDisplayName("Expiration Date");
		definitions[5].setExpression(new org.openl.rules.table.properties.expressions.match.MatchingExpression("ge(currentDate)"));
		definitions[5].setFormat("MM/dd/yyyy");
		definitions[5].setGroup("Business Dimension");
		definitions[5].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.MODULE, InheritanceLevel.CATEGORY, InheritanceLevel.TABLE});
		definitions[5].setName("expirationDate");
		definitions[5].setPrimaryKey(false);
		definitions[5].setSecurityFilter("no");
		definitions[5].setSystem(false);
		definitions[5].setType(org.openl.types.java.JavaOpenClass.getOpenClass(java.util.Date.class));
		definitions[6] = new TablePropertyDefinition();
		definitions[6].setConstraints(new org.openl.rules.table.constraints.Constraints("< endRequestDate"));
		definitions[6].setDescription("The starting date when rules are available for usage in production");
		definitions[6].setDimensional(true);
		definitions[6].setDisplayName("Start Request Date");
		definitions[6].setExpression(new org.openl.rules.table.properties.expressions.match.MatchingExpression("le(requestDate)"));
		definitions[6].setFormat("MM/dd/yyyy");
		definitions[6].setGroup("Business Dimension");
		definitions[6].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.MODULE, InheritanceLevel.CATEGORY, InheritanceLevel.TABLE});
		definitions[6].setName("startRequestDate");
		definitions[6].setPrimaryKey(true);
		definitions[6].setSecurityFilter("no");
		definitions[6].setSystem(false);
		definitions[6].setType(org.openl.types.java.JavaOpenClass.getOpenClass(java.util.Date.class));
		definitions[7] = new TablePropertyDefinition();
		definitions[7].setConstraints(new org.openl.rules.table.constraints.Constraints("> startRequestDate"));
		definitions[7].setDescription("The last date when rules are available for usage in production");
		definitions[7].setDimensional(true);
		definitions[7].setDisplayName("End Request Date");
		definitions[7].setExpression(new org.openl.rules.table.properties.expressions.match.MatchingExpression("ge(requestDate)"));
		definitions[7].setFormat("MM/dd/yyyy");
		definitions[7].setGroup("Business Dimension");
		definitions[7].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.MODULE, InheritanceLevel.CATEGORY, InheritanceLevel.TABLE});
		definitions[7].setName("endRequestDate");
		definitions[7].setPrimaryKey(false);
		definitions[7].setSecurityFilter("no");
		definitions[7].setSystem(false);
		definitions[7].setType(org.openl.types.java.JavaOpenClass.getOpenClass(java.util.Date.class));
		definitions[8] = new TablePropertyDefinition();
		definitions[8].setConstraints(new org.openl.rules.table.constraints.Constraints("no"));
		definitions[8].setDescription("A name of a user created the table in OpenL Tablets WebStudio");
		definitions[8].setDimensional(false);
		definitions[8].setDisplayName("Created By");
		definitions[8].setGroup("Info");
		definitions[8].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.TABLE});
		definitions[8].setName("createdBy");
		definitions[8].setPrimaryKey(false);
		definitions[8].setSecurityFilter("no");
		definitions[8].setSystem(true);
		definitions[8].setSystemValueDescriptor("currentUser");
		definitions[8].setSystemValuePolicy(SystemValuePolicy.IF_BLANK_ONLY);
		definitions[8].setType(org.openl.types.java.JavaOpenClass.getOpenClass(java.lang.String.class));
		definitions[9] = new TablePropertyDefinition();
		definitions[9].setConstraints(new org.openl.rules.table.constraints.Constraints("no"));
		definitions[9].setDescription("Date of the table creation in OpenL Tablets WebStudio");
		definitions[9].setDimensional(false);
		definitions[9].setDisplayName("Created On");
		definitions[9].setFormat("MM/dd/yyyy");
		definitions[9].setGroup("Info");
		definitions[9].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.TABLE});
		definitions[9].setName("createdOn");
		definitions[9].setPrimaryKey(false);
		definitions[9].setSecurityFilter("no");
		definitions[9].setSystem(true);
		definitions[9].setSystemValueDescriptor("currentDate");
		definitions[9].setSystemValuePolicy(SystemValuePolicy.IF_BLANK_ONLY);
		definitions[9].setType(org.openl.types.java.JavaOpenClass.getOpenClass(java.util.Date.class));
		definitions[10] = new TablePropertyDefinition();
		definitions[10].setConstraints(new org.openl.rules.table.constraints.Constraints("no"));
		definitions[10].setDescription("A name of a user last modified the table in OpenL Tablets WebStudio");
		definitions[10].setDimensional(false);
		definitions[10].setDisplayName("Modified By");
		definitions[10].setGroup("Info");
		definitions[10].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.TABLE});
		definitions[10].setName("modifiedBy");
		definitions[10].setPrimaryKey(false);
		definitions[10].setSecurityFilter("no");
		definitions[10].setSystem(true);
		definitions[10].setSystemValueDescriptor("currentUser");
		definitions[10].setSystemValuePolicy(SystemValuePolicy.ON_EACH_EDIT);
		definitions[10].setType(org.openl.types.java.JavaOpenClass.getOpenClass(java.lang.String.class));
		definitions[11] = new TablePropertyDefinition();
		definitions[11].setConstraints(new org.openl.rules.table.constraints.Constraints("no"));
		definitions[11].setDescription("The date of the last table modification in OpenL Tablets WebStudio");
		definitions[11].setDimensional(false);
		definitions[11].setDisplayName("Modified On");
		definitions[11].setFormat("MM/dd/yyyy");
		definitions[11].setGroup("Info");
		definitions[11].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.TABLE});
		definitions[11].setName("modifiedOn");
		definitions[11].setPrimaryKey(false);
		definitions[11].setSecurityFilter("no");
		definitions[11].setSystem(true);
		definitions[11].setSystemValueDescriptor("currentDate");
		definitions[11].setSystemValuePolicy(SystemValuePolicy.ON_EACH_EDIT);
		definitions[11].setType(org.openl.types.java.JavaOpenClass.getOpenClass(java.util.Date.class));
		definitions[12] = new TablePropertyDefinition();
		definitions[12].setConstraints(new org.openl.rules.table.constraints.Constraints("unique in:module&regexp:([a-zA-Z_][a-zA-Z0-9_]*)"));
		definitions[12].setDescription("Unique ID to be used for calling the rule table");
		definitions[12].setDimensional(false);
		definitions[12].setDisplayName("ID");
		definitions[12].setErrorSeverity(Severity.ERROR);
		definitions[12].setGroup("Dev");
		definitions[12].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.TABLE});
		definitions[12].setName("id");
		definitions[12].setPrimaryKey(false);
		definitions[12].setSecurityFilter("no");
		definitions[12].setSystem(false);
		definitions[12].setTableType(new XlsNodeTypes[] {XlsNodeTypes.XLS_DT, XlsNodeTypes.XLS_SPREADSHEET, XlsNodeTypes.XLS_TBASIC, XlsNodeTypes.XLS_COLUMN_MATCH, XlsNodeTypes.XLS_METHOD});
		definitions[12].setType(org.openl.types.java.JavaOpenClass.getOpenClass(java.lang.String.class));
		definitions[13] = new TablePropertyDefinition();
		definitions[13].setConstraints(new org.openl.rules.table.constraints.Constraints("one of: common, vocabulary[N], main[N]"));
		definitions[13].setDescription("The property to be used for managing dependencies between build phases");
		definitions[13].setDimensional(false);
		definitions[13].setDisplayName("Build Phase");
		definitions[13].setGroup("Dev");
		definitions[13].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.MODULE, InheritanceLevel.CATEGORY, InheritanceLevel.TABLE});
		definitions[13].setName("buildPhase");
		definitions[13].setPrimaryKey(false);
		definitions[13].setSecurityFilter("no");
		definitions[13].setSystem(false);
		definitions[13].setType(org.openl.types.java.JavaOpenClass.getOpenClass(java.lang.String.class));
		definitions[14] = new TablePropertyDefinition();
		definitions[14].setConstraints(new org.openl.rules.table.constraints.Constraints("one of: on, off, gaps, overlaps"));
		definitions[14].setDescription("On/Off/Gap/Overlap validation mode for the rule table");
		definitions[14].setDimensional(false);
		definitions[14].setDisplayName("Validate DT");
		definitions[14].setGroup("Dev");
		definitions[14].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.MODULE, InheritanceLevel.CATEGORY, InheritanceLevel.TABLE});
		definitions[14].setName("validateDT");
		definitions[14].setPrimaryKey(false);
		definitions[14].setSecurityFilter("no");
		definitions[14].setSystem(false);
		definitions[14].setTableType(new XlsNodeTypes[] {XlsNodeTypes.XLS_DT, XlsNodeTypes.XLS_PROPERTIES});
		definitions[14].setType(org.openl.types.java.JavaOpenClass.getOpenClass(java.lang.String.class));
		definitions[15] = new TablePropertyDefinition();
		definitions[15].setConstraints(new org.openl.rules.table.constraints.Constraints("list: Defined by method getLob()"));
		definitions[15].setDescription("LOB (line of business) for which this table works and should be used");
		definitions[15].setDimensional(true);
		definitions[15].setDisplayName("LOB");
		definitions[15].setExpression(new org.openl.rules.table.properties.expressions.match.MatchingExpression("eq(lob)"));
		definitions[15].setGroup("Business Dimension");
		definitions[15].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.MODULE, InheritanceLevel.CATEGORY, InheritanceLevel.TABLE});
		definitions[15].setName("lob");
		definitions[15].setPrimaryKey(false);
		definitions[15].setSecurityFilter("yes (coma separated filter specification by user role: category/role pairs)");
		definitions[15].setSystem(false);
		definitions[15].setType(org.openl.types.java.JavaOpenClass.getOpenClass(java.lang.String.class));
		definitions[16] = new TablePropertyDefinition();
		definitions[16].setConstraints(new org.openl.rules.table.constraints.Constraints("data: usRegions"));
		definitions[16].setDescription("US region(s) for which the table works and should be used");
		definitions[16].setDimensional(true);
		definitions[16].setDisplayName("US Region");
		definitions[16].setExpression(new org.openl.rules.table.properties.expressions.match.MatchingExpression("contains(usRegion)"));
		definitions[16].setGroup("Business Dimension");
		definitions[16].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.MODULE, InheritanceLevel.CATEGORY, InheritanceLevel.TABLE});
		definitions[16].setName("usregion");
		definitions[16].setPrimaryKey(false);
		definitions[16].setSecurityFilter("yes (coma separated filter specification by user role: category/role pairs)");
		definitions[16].setSystem(false);
		definitions[16].setType(org.openl.types.java.JavaOpenClass.getOpenClass(org.openl.rules.enumeration.UsRegionsEnum[].class));
		definitions[17] = new TablePropertyDefinition();
		definitions[17].setConstraints(new org.openl.rules.table.constraints.Constraints("data: countries"));
		definitions[17].setDescription("Countrie(s) for which the table works and should be used");
		definitions[17].setDimensional(true);
		definitions[17].setDisplayName("Countries");
		definitions[17].setExpression(new org.openl.rules.table.properties.expressions.match.MatchingExpression("contains(country)"));
		definitions[17].setGroup("Business Dimension");
		definitions[17].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.MODULE, InheritanceLevel.CATEGORY, InheritanceLevel.TABLE});
		definitions[17].setName("country");
		definitions[17].setPrimaryKey(false);
		definitions[17].setSecurityFilter("yes (coma separated filter specification by user role: category/role pairs)");
		definitions[17].setSystem(false);
		definitions[17].setType(org.openl.types.java.JavaOpenClass.getOpenClass(org.openl.rules.enumeration.CountriesEnum[].class));
		definitions[18] = new TablePropertyDefinition();
		definitions[18].setConstraints(new org.openl.rules.table.constraints.Constraints("data: currencies"));
		definitions[18].setDescription("Currencie(s) for which the table works and should be used");
		definitions[18].setDimensional(true);
		definitions[18].setDisplayName("Currency");
		definitions[18].setExpression(new org.openl.rules.table.properties.expressions.match.MatchingExpression("contains(currency)"));
		definitions[18].setGroup("Business Dimension");
		definitions[18].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.MODULE, InheritanceLevel.CATEGORY, InheritanceLevel.TABLE});
		definitions[18].setName("currency");
		definitions[18].setPrimaryKey(false);
		definitions[18].setSecurityFilter("yes (coma separated filter specification by user role: category/role pairs)");
		definitions[18].setSystem(false);
		definitions[18].setType(org.openl.types.java.JavaOpenClass.getOpenClass(org.openl.rules.enumeration.CurrenciesEnum[].class));
		definitions[19] = new TablePropertyDefinition();
		definitions[19].setConstraints(new org.openl.rules.table.constraints.Constraints("data: languages"));
		definitions[19].setDescription("Language(s) for which this table works and should be used");
		definitions[19].setDimensional(true);
		definitions[19].setDisplayName("Language");
		definitions[19].setExpression(new org.openl.rules.table.properties.expressions.match.MatchingExpression("contains(lang)"));
		definitions[19].setGroup("Business Dimension");
		definitions[19].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.MODULE, InheritanceLevel.CATEGORY, InheritanceLevel.TABLE});
		definitions[19].setName("lang");
		definitions[19].setPrimaryKey(false);
		definitions[19].setSecurityFilter("yes (coma separated filter specification by user role: category/role pairs)");
		definitions[19].setSystem(false);
		definitions[19].setType(org.openl.types.java.JavaOpenClass.getOpenClass(org.openl.rules.enumeration.LanguagesEnum[].class));
		definitions[20] = new TablePropertyDefinition();
		definitions[20].setConstraints(new org.openl.rules.table.constraints.Constraints("data: usStates"));
		definitions[20].setDescription("US State(s) for which this table works and should be used");
		definitions[20].setDimensional(true);
		definitions[20].setDisplayName("US States");
		definitions[20].setExpression(new org.openl.rules.table.properties.expressions.match.MatchingExpression("contains(usState)"));
		definitions[20].setGroup("Business Dimension");
		definitions[20].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.MODULE, InheritanceLevel.CATEGORY, InheritanceLevel.TABLE});
		definitions[20].setName("state");
		definitions[20].setPrimaryKey(false);
		definitions[20].setSecurityFilter("yes (coma separated filter specification by user role: category/role pairs)");
		definitions[20].setSystem(false);
		definitions[20].setType(org.openl.types.java.JavaOpenClass.getOpenClass(org.openl.rules.enumeration.UsStatesEnum[].class));
		definitions[21] = new TablePropertyDefinition();
		definitions[21].setConstraints(new org.openl.rules.table.constraints.Constraints("data: regions"));
		definitions[21].setDescription("Economic Region(s) for which the table works and should be used");
		definitions[21].setDimensional(true);
		definitions[21].setDisplayName("Region");
		definitions[21].setExpression(new org.openl.rules.table.properties.expressions.match.MatchingExpression("contains(region)"));
		definitions[21].setGroup("Business Dimension");
		definitions[21].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.MODULE, InheritanceLevel.CATEGORY});
		definitions[21].setName("region");
		definitions[21].setPrimaryKey(false);
		definitions[21].setSecurityFilter("yes (coma separated filter specification by user role: category/role pairs)");
		definitions[21].setSystem(false);
		definitions[21].setType(org.openl.types.java.JavaOpenClass.getOpenClass(org.openl.rules.enumeration.RegionsEnum[].class));
		definitions[22] = new TablePropertyDefinition();
		definitions[22].setConstraints(new org.openl.rules.table.constraints.Constraints("NN.NN[.NN]"));
		definitions[22].setDescription("Defines a version of this table. The \u201cversion\u201d should be different for each tabl"
		 + "e with the same signature and business dimensional properties values");
		definitions[22].setDimensional(false);
		definitions[22].setDisplayName("Version");
		definitions[22].setGroup("Version");
		definitions[22].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.TABLE});
		definitions[22].setName("version");
		definitions[22].setPrimaryKey(false);
		definitions[22].setSystem(false);
		definitions[22].setTableType(new XlsNodeTypes[] {XlsNodeTypes.XLS_DT, XlsNodeTypes.XLS_SPREADSHEET, XlsNodeTypes.XLS_TBASIC, XlsNodeTypes.XLS_COLUMN_MATCH, XlsNodeTypes.XLS_METHOD});
		definitions[22].setType(org.openl.types.java.JavaOpenClass.getOpenClass(java.lang.String.class));
		definitions[23] = new TablePropertyDefinition();
		definitions[23].setConstraints(new org.openl.rules.table.constraints.Constraints("unique in:TableGroup"));
		definitions[23].setDefaultValue("true");
		definitions[23].setDescription("Indicates if the current table version is active or not");
		definitions[23].setDimensional(false);
		definitions[23].setDisplayName("Active");
		definitions[23].setGroup("Version");
		definitions[23].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.TABLE});
		definitions[23].setName("active");
		definitions[23].setPrimaryKey(false);
		definitions[23].setSystem(false);
		definitions[23].setTableType(new XlsNodeTypes[] {XlsNodeTypes.XLS_DT, XlsNodeTypes.XLS_SPREADSHEET, XlsNodeTypes.XLS_TBASIC, XlsNodeTypes.XLS_COLUMN_MATCH, XlsNodeTypes.XLS_METHOD});
		definitions[23].setType(org.openl.types.java.JavaOpenClass.getOpenClass(java.lang.Boolean.class));
		definitions[24] = new TablePropertyDefinition();
		definitions[24].setDefaultValue("false");
		definitions[24].setDescription("Defines whether to raise an error in case no rules are matched");
		definitions[24].setDimensional(false);
		definitions[24].setDisplayName("Fail On Miss");
		definitions[24].setGroup("Dev");
		definitions[24].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.MODULE, InheritanceLevel.CATEGORY, InheritanceLevel.TABLE});
		definitions[24].setName("failOnMiss");
		definitions[24].setPrimaryKey(false);
		definitions[24].setSystem(false);
		definitions[24].setTableType(new XlsNodeTypes[] {XlsNodeTypes.XLS_DT, XlsNodeTypes.XLS_PROPERTIES});
		definitions[24].setType(org.openl.types.java.JavaOpenClass.getOpenClass(java.lang.Boolean.class));
		definitions[25] = new TablePropertyDefinition();
		definitions[25].setConstraints(new org.openl.rules.table.constraints.Constraints("Worksheet, Workbook, Module"));
		definitions[25].setDescription("The scope for a properties table");
		definitions[25].setDimensional(false);
		definitions[25].setDisplayName("Scope");
		definitions[25].setGroup("Dev");
		definitions[25].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.CATEGORY, InheritanceLevel.MODULE});
		definitions[25].setName("scope");
		definitions[25].setPrimaryKey(false);
		definitions[25].setSystem(false);
		definitions[25].setTableType(new XlsNodeTypes[] {XlsNodeTypes.XLS_PROPERTIES});
		definitions[25].setType(org.openl.types.java.JavaOpenClass.getOpenClass(java.lang.String.class));
		definitions[26] = new TablePropertyDefinition();
		definitions[26].setConstraints(new org.openl.rules.table.constraints.Constraints("regexp:([a-zA-Z_]{1}[a-zA-Z0-9_]*(\\.[a-zA-Z_]{1}[a-zA-Z0-9_]*)*)"));
		definitions[26].setDefaultValue("org.openl.generated.beans");
		definitions[26].setDescription("The name of the package for datatype generation");
		definitions[26].setDimensional(false);
		definitions[26].setDisplayName("Datatype Package");
		definitions[26].setGroup("Dev");
		definitions[26].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.MODULE, InheritanceLevel.TABLE});
		definitions[26].setName("datatypePackage");
		definitions[26].setPrimaryKey(false);
		definitions[26].setSystem(false);
		definitions[26].setTableType(new XlsNodeTypes[] {XlsNodeTypes.XLS_DATATYPE, XlsNodeTypes.XLS_PROPERTIES});
		definitions[26].setType(org.openl.types.java.JavaOpenClass.getOpenClass(java.lang.String.class));
		definitions[27] = new TablePropertyDefinition();
		definitions[27].setConstraints(new org.openl.rules.table.constraints.Constraints("no"));
		definitions[27].setDimensional(false);
		definitions[27].setDisplayName("Transaction Type");
		definitions[27].setGroup("Dev");
		definitions[27].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.TABLE});
		definitions[27].setName("transaction");
		definitions[27].setPrimaryKey(false);
		definitions[27].setSystem(false);
		definitions[27].setType(org.openl.types.java.JavaOpenClass.getOpenClass(java.lang.String[].class));
		definitions[28] = new TablePropertyDefinition();
		definitions[28].setConstraints(new org.openl.rules.table.constraints.Constraints("no"));
		definitions[28].setDimensional(false);
		definitions[28].setDisplayName("Custom1");
		definitions[28].setGroup("Dev");
		definitions[28].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.TABLE});
		definitions[28].setName("custom1");
		definitions[28].setPrimaryKey(false);
		definitions[28].setSystem(false);
		definitions[28].setType(org.openl.types.java.JavaOpenClass.getOpenClass(java.lang.String[].class));
		definitions[29] = new TablePropertyDefinition();
		definitions[29].setConstraints(new org.openl.rules.table.constraints.Constraints("no"));
		definitions[29].setDimensional(false);
		definitions[29].setDisplayName("Custom2");
		definitions[29].setGroup("Dev");
		definitions[29].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.TABLE});
		definitions[29].setName("custom2");
		definitions[29].setPrimaryKey(false);
		definitions[29].setSystem(false);
		definitions[29].setType(org.openl.types.java.JavaOpenClass.getOpenClass(java.lang.String[].class));
		definitions[30] = new TablePropertyDefinition();
		definitions[30].setDescription("Defines whether or not to use cache while recalculating the table for a variatio"
		 + "n, depending on the rule input");
		definitions[30].setDimensional(false);
		definitions[30].setDisplayName("Cacheable");
		definitions[30].setGroup("Dev");
		definitions[30].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.MODULE, InheritanceLevel.CATEGORY, InheritanceLevel.TABLE});
		definitions[30].setName("cacheable");
		definitions[30].setPrimaryKey(false);
		definitions[30].setSystem(false);
		definitions[30].setType(org.openl.types.java.JavaOpenClass.getOpenClass(java.lang.Boolean.class));
		definitions[31] = new TablePropertyDefinition();
		definitions[31].setConstraints(new org.openl.rules.table.constraints.Constraints("data: recalculate"));
		definitions[31].setDescription("The way of recalculation of the table for a variation - slightly varied input pa"
		 + "rameter(s)");
		definitions[31].setDimensional(false);
		definitions[31].setDisplayName("Recalculate");
		definitions[31].setGroup("Dev");
		definitions[31].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.MODULE, InheritanceLevel.CATEGORY, InheritanceLevel.TABLE});
		definitions[31].setName("recalculate");
		definitions[31].setPrimaryKey(false);
		definitions[31].setSystem(false);
		definitions[31].setType(org.openl.types.java.JavaOpenClass.getOpenClass(org.openl.rules.enumeration.RecalculateEnum.class));
		definitions[32] = new TablePropertyDefinition();
		definitions[32].setConstraints(new org.openl.rules.table.constraints.Constraints("regexp:(-?[0-9]+)"));
		definitions[32].setDescription("Precision of comparing the returned results with the expected ones");
		definitions[32].setDimensional(false);
		definitions[32].setDisplayName("Precision");
		definitions[32].setGroup("Dev");
		definitions[32].setInheritanceLevel(new InheritanceLevel[] {InheritanceLevel.MODULE, InheritanceLevel.CATEGORY, InheritanceLevel.TABLE});
		definitions[32].setName("precision");
		definitions[32].setPrimaryKey(false);
		definitions[32].setSystem(false);
		definitions[32].setTableType(new XlsNodeTypes[] {XlsNodeTypes.XLS_TEST_METHOD, XlsNodeTypes.XLS_PROPERTIES});
		definitions[32].setType(org.openl.types.java.JavaOpenClass.getOpenClass(java.lang.String.class));
// <<< END INSERT TablePropertiesDefinition >>>
    }

    public static TablePropertyDefinition[] getDefaultDefinitions() {
        return definitions;
    }

}
