package com.quest.etna.model;

import java.io.Serializable;

public class DeleteResponse implements Serializable {
    private boolean success;

    public DeleteResponse() {}

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
