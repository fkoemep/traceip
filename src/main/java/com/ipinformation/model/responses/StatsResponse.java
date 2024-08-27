package com.ipinformation.model.responses;

import java.io.Serializable;

public class StatsResponse implements Serializable {

    private Double maxDistance;
    private Double minDistance;
    private Double avgDistance;

    public Double getAvgDistance() {
        return avgDistance;
    }

    public Double getMaxDistance() {
        return maxDistance;
    }

    public Double getMinDistance() {
        return minDistance;
    }

    public void setAvgDistance(Double avgDistance) {
        this.avgDistance = avgDistance;
    }

    public void setMaxDistance(Double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public void setMinDistance(Double minDistance) {
        this.minDistance = minDistance;
    }

}
