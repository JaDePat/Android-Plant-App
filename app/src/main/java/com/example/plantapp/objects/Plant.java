package com.example.plantapp.objects;

public class Plant {

    //Column Value Holders
    private int ID;
    private String Name;
    private String Scientific_Name;
    private String Light;
    private String Water;
    private String Fertilizer;
    private String Temperature;
    private String Humidity;
    private String Flowering;
    private String Pests;
    private String Diseases;
    private String Soil;
    private String Pot_size;
    private String Pruning;
    private String Propagation;
    private String Poisonous_plant_info;

    // Constructors
    public Plant(int ID, String name, String scientific_Name, String light, String water,
                 String fertilizer, String temperature, String humidity, String flowering,
                 String pests, String diseases, String soil, String pot_size, String pruning,
                 String propagation, String poisonous_plant_info) {
        this.ID = ID;
        Name = name;
        Scientific_Name = scientific_Name;
        Light = light;
        Water = water;
        Fertilizer = fertilizer;
        Temperature = temperature;
        Humidity = humidity;
        Flowering = flowering;
        Pests = pests;
        Diseases = diseases;
        Soil = soil;
        Pot_size = pot_size;
        Pruning = pruning;
        Propagation = propagation;
        Poisonous_plant_info = poisonous_plant_info;
    }

    public Plant() {}


    //toString method so that we are able to print contents of class object
    @Override
    public String toString() {
        return "Plant{" +
                "ID=" + ID +
                ", Name='" + Name + '\'' +
                ", Scientific_Name='" + Scientific_Name + '\'' +
                ", Light='" + Light + '\'' +
                ", Water='" + Water + '\'' +
                ", Fertilizer='" + Fertilizer + '\'' +
                ", Temperature='" + Temperature + '\'' +
                ", Humidity='" + Humidity + '\'' +
                ", Flowering='" + Flowering + '\'' +
                ", Pests='" + Pests + '\'' +
                ", Diseases='" + Diseases + '\'' +
                ", Soil='" + Soil + '\'' +
                ", Pot_size='" + Pot_size + '\'' +
                ", Pruning='" + Pruning + '\'' +
                ", Propagation='" + Propagation + '\'' +
                ", Poisonous_plant_info='" + Poisonous_plant_info + '\'' +
                '}';
    }

    //Getters and Setters
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getScientific_Name() {
        return Scientific_Name;
    }

    public void setScientific_Name(String scientific_Name) {
        Scientific_Name = scientific_Name;
    }

    public String getLight() {
        return Light;
    }

    public void setLight(String light) {
        Light = light;
    }

    public String getWater() {
        return Water;
    }

    public void setWater(String water) {
        Water = water;
    }

    public String getFertilizer() {
        return Fertilizer;
    }

    public void setFertilizer(String fertilizer) {
        Fertilizer = fertilizer;
    }

    public String getTemperature() {
        return Temperature;
    }

    public void setTemperature(String temperature) {
        Temperature = temperature;
    }

    public String getHumidity() {
        return Humidity;
    }

    public void setHumidity(String humidity) {
        Humidity = humidity;
    }

    public String getFlowering() {
        return Flowering;
    }

    public void setFlowering(String flowering) {
        Flowering = flowering;
    }

    public String getPests() {
        return Pests;
    }

    public void setPests(String pests) {
        Pests = pests;
    }

    public String getDiseases() {
        return Diseases;
    }

    public void setDiseases(String diseases) {
        Diseases = diseases;
    }

    public String getSoil() {
        return Soil;
    }

    public void setSoil(String soil) {
        Soil = soil;
    }

    public String getPot_size() {
        return Pot_size;
    }

    public void setPot_size(String pot_size) {
        Pot_size = pot_size;
    }

    public String getPruning() {
        return Pruning;
    }

    public void setPruning(String pruning) {
        Pruning = pruning;
    }

    public String getPropagation() {
        return Propagation;
    }

    public void setPropagation(String propagation) {
        Propagation = propagation;
    }

    public String getPoisonous_plant_info() {
        return Poisonous_plant_info;
    }

    public void setPoisonous_plant_info(String poisonous_plant_info) {
        Poisonous_plant_info = poisonous_plant_info;
    }



}
