package org.disl.meta

import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import org.disl.pattern.Executable
import org.disl.pattern.ExecutionInfo
import org.disl.pattern.TablePattern

import java.lang.reflect.Field
import java.lang.reflect.Modifier

/**
 * Representation of table or view in DISL data model.
 * */
@CompileStatic
abstract class Table extends MappingSource implements Executable, IndexOwner, Initializable {

    public abstract TablePattern getPattern()

    List<Column> columns = []
    String description = ''
    List<IndexMeta> indexes = []
    List<UniqueKeyMeta> uniqueKeys = []
    List<ForeignKeyMeta> foreignKeys = null
    List<CheckMeta> checkConstraints = []

    List<ForeignKeyMeta> getForeignKeys() {
        if (foreignKeys == null) {
            foreignKeys = []
            ForeignKeyMeta.initForeignKey(this)
        }
        return foreignKeys
    }

    public String getSchema() {
        'default'
    }

    @Override
    public String getRefference() {
        if (sourceWithClause) {
            return sourceAlias
        }
        String alias = ""
        if (sourceAlias != null) {
            alias = " ${sourceAlias}"
        }
        return "${fullName}${alias}"
    }

    String getRefferenceColumnList() {
        getColumns().collect { "${it.name}" }.join(",")
    }

    public String getFullName() {
        String ownerPrefix = ""
        String owner = getPhysicalSchema()
        if (owner != null) {
            ownerPrefix = "${owner}."
        }
        "${ownerPrefix}${name}"
    }

    public String getPhysicalSchema() {
        Context.getPhysicalSchemaName(getSchema())
    }

    protected Column c(String name) {
        createColumn(name)
    }

    private Column createColumn(String name) {
        Column c = new Column(name, this)
        columns.add(c)
        c
    }

    public void execute() {
        getPattern().execute()
    }

    public void simulate() {
        getPattern().simulate()
    }

    public ExecutionInfo getExecutionInfo() {
        return getPattern().getExecutionInfo()
    }

    public void init() {
        super.init()

        initColumns()

        IndexMeta.initIndexes(this)
        UniqueKeyMeta.initUniqueKeys(this)
        CheckMeta.initCheckConstraints(this)

        initPattern()
    }


    protected void initPattern() {
        initPatternField()
    }

    private initPatternField() {
        if (!getPattern()) {
            Field patternField = getFieldByName('pattern')
            if (patternField && !Modifier.isAbstract(patternField.getType().getModifiers())) {
                patternField.setAccessible(true)
                patternField.set(this, MetaFactory.create(patternField.getType(), { ((TablePattern<Table>) it).setTable(this) }))
            }
        }
    }

    protected void initColumns() {
        getFieldsByType(Column).each { initColumn(it) }
    }

    protected void initColumn(Field f) {
        Column column = (Column) this[f.getName()]

        if (column == null) {
            column = (Column) f.getType().newInstance()
            column.parent = this
            this[f.getName()] = column
            columns.add(column)
        }


        Name name = f.getAnnotation(Name)
        if (name) {
            column.setName(name.value())
        } else {
            column.setName(f.getName());
        }

        Description desc = f.getAnnotation(Description)
        if (desc) {
            column.setDescription(desc.value())
        }

        DataType dataType = f.getAnnotation(DataType)
        if (dataType) {
            column.setDataType(dataType.value())
        }

        PrimaryKey primaryKey = f.getAnnotation(PrimaryKey)
        if (primaryKey) {
            column.setPrimaryKey(true)
        }

        DefaultMapping defaultMapping = f.getAnnotation(DefaultMapping)
        if (defaultMapping) {
            initDefaultMapping(column, defaultMapping.value())
        }

        NotNull notNull = f.getAnnotation(NotNull)
        if (notNull) {
            column.setNotNull(true)
        }

        DefaultValue defaultValue = f.getAnnotation(DefaultValue)
        if (defaultValue) {
            column.setDefaultValue(defaultValue.value())
        }

        Check check = f.getAnnotation(Check)
        if (check) {
            column.setCheck(check.value())
        }
    }

    @CompileStatic(TypeCheckingMode.SKIP)
    void initDefaultMapping(Column column, String defaultMapping) {
        column.setDefaultMapping(defaultMapping)
    }

    public Iterable<String> getColumnDefinitions() {
        columns.collect { it.columnDefinition }
    }

    public List<Column> getPrimaryKeyColumns() {
        (List<Column>) columns.findAll { it.isPrimaryKey() }
    }

    @Override
    String getWithReference() {
        throw new UnsupportedOperationException()
    }
}
