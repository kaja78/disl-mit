package org.disl.db.reverseEngineering

import groovy.json.StringEscapeUtils
import org.disl.meta.Column
import org.disl.pattern.FileOutputStep
import org.disl.pattern.TablePattern

/**
 * Pattern for generating source code for DISL data model table.
 * */
class ReverseTablePattern extends TablePattern<ReverseEngineeredTable> {
    /**
     * Output directory for generated source code.
     * */
    File outputDir = new File("src")

    /**
     * Package name of generated table class.
     * */
    String packageName

    /**
     * Class name of table class parent.
     * */
    String parentClassName

    File getFile() {
        File directory = new File(outputDir, packageName.replace('.', '/'))
        new File(directory, "${table.name}.groovy")
    }

    @Override
    public void init() {
        add CreateDislTable
    }

    static class CreateDislTable extends FileOutputStep {

        ReverseTablePattern getPattern() {
            super.pattern
        }

        File getFile() {
            getPattern().getFile()
        }

        String description(String text) {
            if (text) {
                return "@Description(\"\"\"${escape(text)}\"\"\")"
            }
            return ''
        }

        String escape(String text) {
            return StringEscapeUtils.escapeJava(text)
        }

        String getCode() {
            """\
package $pattern.packageName

import org.disl.meta.*

${description(pattern.table.description)}$foreignKeyDefinition
@groovy.transform.CompileStatic
class $pattern.table.name extends ${pattern.parentClassName} {

$columnDefinitions
}"""
        }

        String getColumnDefinitions() {
            pattern.table.getColumns().collect { getColumnDefinitionCode(it) }.join("\n\n")
        }

        String getForeignKeyDefinition() {
            if (pattern.table.foreignKeys.size() == 0) {
                return ''
            }
            return """
@ForeignKeys([${pattern.table.foreignKeys.collect({ "@ForeignKey(name='${it.name}',targetTable=${it.targetTableClassName},sourceColumn='${it.sourceColumn}',targetColumn=('${it.targetColumn}'))" }).join(',\n')}])"""
        }

        String getColumnDefinitionCode(Column column) {
            String notNull = column.notNull ? "\n\t\t@NotNull" : ""
            String primaryKey = column.primaryKey ? "\n\t\t@PrimaryKey" : ""

            """\
		${description(column.description)}
		@DataType(\"\"\"$column.dataType\"\"\")$primaryKey$notNull
		Column $column.name"""
        }


    }


}








 