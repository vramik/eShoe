package com.issuetracker.web.security;

import java.io.Serializable;

public class TrackerSettings implements Serializable {

    private boolean showInternalReleases = false;

    public boolean isShowInternalReleases() {
        return showInternalReleases;
    }

    public void setShowInternalReleases(boolean showInternalReleases) {
        this.showInternalReleases = showInternalReleases;
    }
}