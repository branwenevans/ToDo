package com.branwenevans.todo;

public class ListItem {

    private String label;

    private boolean done;

    public ListItem(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public boolean isDone() {
        return done;
    }

    public void done() {
        this.done = true;
    }
}
