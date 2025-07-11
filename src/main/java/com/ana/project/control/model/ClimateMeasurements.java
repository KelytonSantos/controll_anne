package com.ana.project.control.model;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "climate_measurements")
public class ClimateMeasurements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "teperature")
    private Double temperature;

    @Column(name = "humidity")
    private Double humidity;

    @Column(name = "time")
    private OffsetDateTime timesTamp;

    public ClimateMeasurements(Double temperature, Double humidity, OffsetDateTime timeStamp) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.timesTamp = timeStamp;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public OffsetDateTime getTimesTamp() {
        return timesTamp;
    }

    public void setTimesTamp(OffsetDateTime timesTamp) {
        this.timesTamp = timesTamp;
    }

}
