package org.easyit.demo.api.model;

import org.easyit.demo.api.CutPoint;

import java.util.List;

public class Config {
    private String version;
    private String supportVersion;
    private List<CutPoint.CutPointImpl> cutPoints;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSupportVersion() {
        return supportVersion;
    }

    public void setSupportVersion(String supportVersion) {
        this.supportVersion = supportVersion;
    }

    public List<CutPoint.CutPointImpl> getCutPoints() {
        return cutPoints;
    }

    public void setCutPoints(List<CutPoint.CutPointImpl> cutPoints) {
        this.cutPoints = cutPoints;
    }
}
