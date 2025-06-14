package io.github.nhtuan10.mykafkatool.ui.schemaregistry;

import io.github.nhtuan10.mykafkatool.annotation.FilterableTableItemField;
import io.github.nhtuan10.mykafkatool.annotation.TableViewColumn;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;

public class SchemaTableItem {
    @TableViewColumn
    private final SimpleStringProperty subject;
    @TableViewColumn
    private final SimpleStringProperty schemaId;
    @TableViewColumn
    private final SimpleStringProperty latestVersion;
    @TableViewColumn
    private final SimpleStringProperty type;
    @TableViewColumn
    private final SimpleStringProperty compatibility;
    @FilterableTableItemField
    private final SimpleStringProperty schema;

    @Getter
    private final String clusterName;

    public SchemaTableItem(String subject, String schemaId, String latestVersion, String type, String compatibility, String schema, String clusterName) {
        this.schemaId = new SimpleStringProperty(schemaId);
        this.latestVersion = new SimpleStringProperty(latestVersion);
        this.type = new SimpleStringProperty(type);
        this.subject = new SimpleStringProperty(subject);
        this.compatibility = new SimpleStringProperty(compatibility);
        this.schema = new SimpleStringProperty(schema);
        this.clusterName = clusterName;
    }

    public String getSchemaId() {
        return schemaId.get();
    }

    public void setSchemaId(String schemaId) {
        this.schemaId.set(schemaId);
    }

    public String getLatestVersion() {
        return latestVersion.get();
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion.set(latestVersion);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getSubject() {
        return subject.get();
    }

    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    public String getCompatibility() {
        return compatibility.get();
    }

    public void setCompatibility(String compatibility) {
        this.compatibility.set(compatibility);
    }

    public String getSchema() {
        return schema.get();
    }

    public SimpleStringProperty schemaProperty() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema.set(schema);
    }


}
