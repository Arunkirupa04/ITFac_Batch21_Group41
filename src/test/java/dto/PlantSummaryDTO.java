package dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlantSummaryDTO {
    private int totalPlants;
    private int lowStockPlants;
    private double totalValue;
    private List<Plant> plants;

    // Getters and Setters
    public int getTotalPlants() {
        return totalPlants;
    }

    public void setTotalPlants(int totalPlants) {
        this.totalPlants = totalPlants;
    }

    public int getLowStockPlants() {
        return lowStockPlants;
    }

    public void setLowStockPlants(int lowStockPlants) {
        this.lowStockPlants = lowStockPlants;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public List<Plant> getPlants() {
        return plants;
    }

    public void setPlants(List<Plant> plants) {
        this.plants = plants;
    }
}
