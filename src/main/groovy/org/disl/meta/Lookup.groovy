package org.disl.meta

import groovy.transform.CompileStatic

import java.lang.reflect.Field

/**
 * Creates logical SQL query containing constant record set, which can be used as MappingSource.
 * This enables constant lookup definition in DISL model without the need to deploy any database objects to database.
 * */
@CompileStatic
abstract class Lookup extends MappingSource {
    List<Column> columns

    abstract List<List> getRecords();

    public String getSchema() {
        'default'
    }

    public PhysicalSchema getPhysicalSchema() {
        Context.getContext().getPhysicalSchema(getSchema())
    }

    public void init() {
        super.init()
        columns = new LinkedList<Column>()
        initColumns()
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
        }
        column.setName(f.getName());
        columns.add(column)
    }


    public String getRefference() {
        if (sourceWithClause) {
            return sourceAlias
        }
        if (sourceAlias != null) {
            return "(\n${getLookupQuery()}) $sourceAlias"
        }
        return "(${getLookupQuery()})"
    }

    public String getLookupQuery() {
        """\
	/*Lookup $name*/
	select * from 
		${physicalSchema.recordsToSubquery(createRecordsFromList())}
	/*End of lookup $name*/"""

    }

    String getRefferenceColumnList() {
        getColumns().collect { "${it.name}" }.join(",")
    }

    protected List<Map> createRecordsFromList() {
        List<List> recordList = getRecords()
        List<Map> result = (List<Map>) recordList.collect { values ->
            Map record = (Map) new LinkedHashMap()
            columns.each { column ->
                record.put(column.name, values[columns.indexOf(column)])
            }
            return record
        }
        return result
    }

    @Override
    String getWithReference() {
        "$sourceAlias as (${getLookupQuery()})"
    }
}
