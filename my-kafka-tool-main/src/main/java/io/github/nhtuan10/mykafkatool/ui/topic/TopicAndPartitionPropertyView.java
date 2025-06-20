package io.github.nhtuan10.mykafkatool.ui.topic;

import io.github.nhtuan10.mykafkatool.MyKafkaToolApplication;
import io.github.nhtuan10.mykafkatool.model.kafka.KafkaPartition;
import io.github.nhtuan10.mykafkatool.model.kafka.KafkaTopic;
import io.github.nhtuan10.mykafkatool.ui.event.PartitionEventSubscriber;
import io.github.nhtuan10.mykafkatool.ui.event.PartitionUIEvent;
import io.github.nhtuan10.mykafkatool.ui.event.TopicEventSubscriber;
import io.github.nhtuan10.mykafkatool.ui.event.TopicUIEvent;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

public class TopicAndPartitionPropertyView extends AnchorPane {
//    BooleanProperty isBlockingAppUINeeded;

//    ReadOnlyBooleanProperty isShownOnWindow;

    @Getter
    private final TopicEventSubscriber topicEventSubscriber;

    @Getter
    private final PartitionEventSubscriber partitionEventSubscriber;

    @FXML
    private TopicAndPartitionPropertyTable topicAndPartitionPropertyTable;

    @FXML
    private PartitionsTable kafkaPartitionsTable;

    @FXML
    private TitledPane partitionsTitledPane;

    private final StringProperty totalMessagesInTheTopicStringProperty;

    @FXML
    private Label totalMessagesInTheTopicLabel;

    public TopicAndPartitionPropertyView() {
        topicEventSubscriber = new PropertyViewTopicEventSubscriber(this);
        partitionEventSubscriber = new PropertyViewPartitionEventSubscriber(this);
        totalMessagesInTheTopicStringProperty = new SimpleStringProperty("0 Messages");
        FXMLLoader fxmlLoader = new FXMLLoader(MyKafkaToolApplication.class.getResource(
                "topic-or-partition-property-view.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setStage(Stage stage) {
        topicAndPartitionPropertyTable.setStage(stage);
        kafkaPartitionsTable.setStage(stage);
    }

    @FXML
    private void initialize() {
        //TODO: Don't use Accordion, so it can show both properties & partitions panel
        totalMessagesInTheTopicLabel.textProperty().bind(totalMessagesInTheTopicStringProperty);
//        this.setProperties(this.isBlockingAppUINeeded, this.isShownOnWindow);
    }

    public void loadTopicConfig(KafkaTopic topic) {
        this.topicAndPartitionPropertyTable.loadTopicConfig(topic);
    }

    public void loadTopicPartitions(KafkaTopic topic) {
        this.kafkaPartitionsTable.loadTopicPartitions(topic, null, this.totalMessagesInTheTopicStringProperty);
    }

    public void loadPartitionConfig(KafkaPartition kafkaPartition) {
        this.topicAndPartitionPropertyTable.loadPartitionConfig(kafkaPartition);
        this.kafkaPartitionsTable.loadTopicPartitions(kafkaPartition.topic(), kafkaPartition, this.totalMessagesInTheTopicStringProperty);

    }

    public void setProperties(BooleanProperty isBlockingAppUINeeded, ReadOnlyBooleanProperty isShownOnWindow) {
//        this.isBlockingAppUINeeded = isBlockingAppUINeeded;
//        this.isShownOnWindow = isShownOnWindow;
        this.topicAndPartitionPropertyTable.setProperties(isBlockingAppUINeeded, isShownOnWindow);
        this.kafkaPartitionsTable.setProperties(isBlockingAppUINeeded, isShownOnWindow);
    }

    @RequiredArgsConstructor
    private static class PropertyViewTopicEventSubscriber extends TopicEventSubscriber {
        private final TopicAndPartitionPropertyView topicAndPartitionPropertyView;

        @Override
        protected void handleOnNext(TopicUIEvent item) {
            if (TopicUIEvent.isRefreshTopicEvent(item)) {
                Platform.runLater(() -> {
                    topicAndPartitionPropertyView.loadTopicConfig(item.topic());
                    topicAndPartitionPropertyView.loadTopicPartitions(item.topic());
                });
            }
        }
    }

    @RequiredArgsConstructor
    private static class PropertyViewPartitionEventSubscriber extends PartitionEventSubscriber {
        private final TopicAndPartitionPropertyView topicAndPartitionPropertyView;

        @Override
        protected void handleOnNext(PartitionUIEvent item) {
            if (PartitionUIEvent.isRefreshPartitionEvent(item)) {
                Platform.runLater(() -> {
                    topicAndPartitionPropertyView.loadPartitionConfig(item.partition());
                });
            }
        }
    }
}
