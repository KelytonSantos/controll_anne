package com.ana.project.control.service;

import java.time.OffsetDateTime;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.ana.project.control.model.ClimateMeasurements;
import com.ana.project.control.repository.ClimateMeasurementsRepository;

//import jakarta.annotation.PostConstruct;

@Service
public class MqttService implements MqttCallback, ApplicationListener<ApplicationReadyEvent> {

    private final MqttClient mqttClient;
    private double temperature;
    private double humidity;

    @Autowired
    private ClimateMeasurementsRepository climateMeasurementsRepo;

    public MqttService(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            this.mqttClient.setCallback(this);
            this.mqttClient.subscribe("project/control/climate/measurement/teste");
        } catch (MqttException e) {
            System.err.println("erro ao se inscrever: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.err.println("Conexão perdida: " + cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String payload = new String(message.getPayload());

        try {
            String[] campos = payload.split(",");
            if (campos.length >= 2) {
                this.temperature = Double.parseDouble(campos[0].trim());
                this.humidity = Double.parseDouble(campos[1].trim());
                System.out
                        .println("Temp: " + this.temperature + ", Umidade: " + this.humidity);

                performSave();

            } else {
                System.err.println("Payload não contém os campos esperados.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Erro de parsing");
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    private void performSave() {
        double currentTemp = this.temperature;
        double currentHumidity = this.humidity;

        ClimateMeasurements measurement = new ClimateMeasurements(
                currentTemp,
                currentHumidity,
                OffsetDateTime.now());

        climateMeasurementsRepo.save(measurement);
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }
}