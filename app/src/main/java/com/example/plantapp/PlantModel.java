package com.example.plantapp;

public class PlantModel {

    // Column titles
    private int id;
    private String name;
    private String scientific_name;
    private String light;
    private String water;
    private String fertilizer;
    private String temperature;
    private String humidity;
    private String flowering;
    private String pests;
    private String diseases;
    private String soil;
    private String pot_size;
    private String pruning;
    private String propagation;
    private String poisonous_plant_info;

    // Constructors

    public PlantModel(int id, String name, String scientific_name, String light, String water, String fertilizer, String temperature, String humidity, String flowering, String pests, String diseases, String soil, String pot_size, String pruning, String propagation, String poisonous_plant_info) {
        this.id = id;
        this.name = name;
        this.scientific_name = scientific_name;
        this.light = light;
        this.water = water;
        this.fertilizer = fertilizer;
        this.temperature = temperature;
        this.humidity = humidity;
        this.flowering = flowering;
        this.pests = pests;
        this.diseases = diseases;
        this.soil = soil;
        this.pot_size = pot_size;
        this.pruning = pruning;
        this.propagation = propagation;
        this.poisonous_plant_info = poisonous_plant_info;
    }

    public PlantModel() {}

    //toString so that we are able to print contents of class object

    @Override
    public String toString() {
        return "PlantModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", scientific_name='" + scientific_name + '\'' +
                ", light='" + light + '\'' +
                ", water='" + water + '\'' +
                ", fertilizer='" + fertilizer + '\'' +
                ", temperature='" + temperature + '\'' +
                ", humidity='" + humidity + '\'' +
                ", flowering='" + flowering + '\'' +
                ", pests='" + pests + '\'' +
                ", diseases='" + diseases + '\'' +
                ", soil='" + soil + '\'' +
                ", pot_size='" + pot_size + '\'' +
                ", pruning='" + pruning + '\'' +
                ", propagation='" + propagation + '\'' +
                ", poisonous_plant_info='" + poisonous_plant_info + '\'' +
                '}';
    }

    //Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScientific_name() {
        return scientific_name;
    }

    public void setScientific_name(String scientific_name) {
        this.scientific_name = scientific_name;
    }

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getFertilizer() {
        return fertilizer;
    }

    public void setFertilizer(String fertilizer) {
        this.fertilizer = fertilizer;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getFlowering() {
        return flowering;
    }

    public void setFlowering(String flowering) {
        this.flowering = flowering;
    }

    public String getPests() {
        return pests;
    }

    public void setPests(String pests) {
        this.pests = pests;
    }

    public String getDiseases() {
        return diseases;
    }

    public void setDiseases(String diseases) {
        this.diseases = diseases;
    }

    public String getSoil() {
        return soil;
    }

    public void setSoil(String soil) {
        this.soil = soil;
    }

    public String getPot_size() {
        return pot_size;
    }

    public void setPot_size(String pot_size) {
        this.pot_size = pot_size;
    }

    public String getPruning() {
        return pruning;
    }

    public void setPruning(String pruning) {
        this.pruning = pruning;
    }

    public String getPropagation() {
        return propagation;
    }

    public void setPropagation(String propagation) {
        this.propagation = propagation;
    }

    public String getPoisonous_plant_info() {
        return poisonous_plant_info;
    }

    public void setPoisonous_plant_info(String poisonous_plant_info) {
        this.poisonous_plant_info = poisonous_plant_info;
    }
}
