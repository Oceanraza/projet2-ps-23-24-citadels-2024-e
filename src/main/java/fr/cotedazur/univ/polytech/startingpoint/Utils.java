package fr.cotedazur.univ.polytech.startingpoint;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cotedazur.univ.polytech.startingpoint.city.District;
import fr.cotedazur.univ.polytech.startingpoint.city.DistrictColor;
import fr.cotedazur.univ.polytech.startingpoint.player.Player;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import java.security.SecureRandom;

//this class is used for basic methods that only have niche purposes and
// are generally just math.
public class Utils {
    private static final SecureRandom random = new SecureRandom();

    public int generateRandomNumber(int bound) {
        return random.nextInt(bound);
    }

    public static boolean canDestroyDistrict(District d, Player p){
        return ((d.getPrice() -1 < p.getGold())&&(!d.getName().equals("Donjon")));
    }
    public static JsonNode parseJsonFromFile(String filePath) throws IOException {
        // Create an ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        // Read JSON from file into a JsonNode
        return objectMapper.readTree(new File(filePath));
    }
    public static ArrayList<District> convertJsonNodeToDistrictList(JsonNode districtsNode) {

        ArrayList<District> districtList = new ArrayList<>();

        // Iterate through each district in the array
        for (JsonNode districtNode : districtsNode) {
            String name = districtNode.path("name").asText();
            int price = districtNode.path("price").asInt();
            String color = districtNode.path("color").asText();
            int number = districtNode.path("number").asInt();

            // Create a District object and add it to the list
            for (int i = 0; i < number; i++){
                District district = new District(name, price, DistrictColor.valueOf(color));
                districtList.add(district);
            }
        }
        return districtList;
    }

}
