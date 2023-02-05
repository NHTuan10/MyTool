package com.example.mytool.ui;

import com.example.mytool.constant.AppConstant;
import com.example.mytool.manager.ClusterManager;
import com.example.mytool.model.kafka.KafkaCluster;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import lombok.Data;
import org.apache.kafka.clients.admin.ConsumerGroupListing;

import java.util.Collection;
import java.util.concurrent.ExecutionException;

public class ConsumerGroupListTreeItem<T> extends TreeItem<T> {
    public ConsumerGroupListTreeItem(T value) {
        super(value);
    }

    public ConsumerGroupListTreeItem(T value, Node graphic) {
        super(value, graphic);
    }

    private boolean loadChildren = true;

    public void reloadChildren() {
        loadChildren = true;
        getChildren();
    }

    @Override
    public ObservableList<TreeItem<T>> getChildren() {
        if (loadChildren) {
            loadChildren = false;

            // First getChildren() call, so we actually go off and
            // determine the children of the File contained in this TreeItem.
            super.getChildren().setAll(buildChildren(this));
        }
        return super.getChildren();
    }

    private ObservableList<TreeItem<T>> buildChildren(TreeItem<T> treeItem) {

        T value = treeItem.getValue();
        if (value instanceof ConsumerGroupListTreeItemValue consumerGroupListTreeItemValue) {
            ObservableList<TreeItem<T>> children = FXCollections.observableArrayList();
            String clusterName = consumerGroupListTreeItemValue.getCluster().getName();
            try {
                Collection<ConsumerGroupListing> consumerGroupListings = ClusterManager.getInstance().getConsumerGroupList(clusterName);
                consumerGroupListings.forEach(consumerGroupListing -> {
                    String consumerGroupId = consumerGroupListing.groupId();
                    StringBuilder displayValSB = new StringBuilder(consumerGroupId);
                    consumerGroupListing.state().ifPresent(state -> {
                        displayValSB.append(" [" + state + "]");
                    });

                    TreeItem<T> consumerGroupItem = (TreeItem<T>) new ConsumerGroupTreeItem(displayValSB.toString(), clusterName, consumerGroupId);
                    children.add(consumerGroupItem);
                });
                // ClusterManager.getInstance().getConsumerGroup(clusterName, consumerGroupListings.stream().map(ConsumerGroupListing::groupId).collect(Collectors.toList()));

                return children;
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return FXCollections.emptyObservableList();
    }

    @Data
    public static class ConsumerGroupListTreeItemValue {
        private KafkaCluster cluster;
        private String display;

        @Override
        public String toString() {
            return display;
        }

        public ConsumerGroupListTreeItemValue(KafkaCluster cluster) {
            this.cluster = cluster;
            this.display = AppConstant.TREE_ITEM_CONSUMER_GROUPS_DISPLAY_NAME;
        }
    }
}
