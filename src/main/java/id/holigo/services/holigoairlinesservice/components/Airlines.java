package id.holigo.services.holigoairlinesservice.components;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Airlines {
    public Map<String, String> getAirlines(String airlinesCode) {
        Map<String, String> airlines = new HashMap<>();
        airlines.put("code", airlinesCode);
        switch (airlinesCode) {
            case "IP" -> {
                airlines.put("code", "IP");
                airlines.put("name", "Pelita Air");
                airlines.put("imageUrl", "https://ik.imagekit.io/holigo/transportasi/pelita_eqnbSkua4.png?ik-sdk-version=javascript-1.4.3&updatedAt=1660178242638");
            }
            case "JT" -> {
                airlines.put("code", "JT");
                airlines.put("name", "Lion Air");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/JT.png");
            }
            case "3K" -> {
                airlines.put("name", "JetStar");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/3K.png");
            }
            case "AA" -> {
                airlines.put("name", "American Airlines");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/AA.png");
            }
            case "AK" -> {
                airlines.put("name", "AirAsia");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/AK.png");
            }
            case "BA" -> {
                airlines.put("name", "British Airlines");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/BA.png");
            }
            case "CI" -> {
                airlines.put("name", "China Airlines");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/CI.png");
            }
            case "CX" -> {
                airlines.put("name", "Cathay Pacific");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/CX.png");
            }
            case "CZ" -> {
                airlines.put("name", "China Southen");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/CZ.png");
            }
            case "D7" -> {
                airlines.put("name", "American Airlines");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/D7.png");
            }
            case "EK" -> {
                airlines.put("name", "Emirates");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/EK.png");
            }
            case "EY" -> {
                airlines.put("name", "Etihad Airways");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/EY.png");
            }
            case "FD" -> {
                airlines.put("name", "AirAsia");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/FD.png");
            }
            case "GA" -> {
                airlines.put("name", "Garuda Indonesia");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/GA.png");
            }
            case "I5" -> {
                airlines.put("name", "AirAsia");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/I5.png");
            }
            case "IA" -> {
                airlines.put("name", "International Airlines");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/IA.png");
            }
            case "IL" -> {
                airlines.put("name", "Trigana Air");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/IL.png");
            }
            case "IN" -> {
                airlines.put("name", "NAM Air");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/IN.png");
            }
            case "IU", "IW" -> {
                airlines.put("code", "JT");
                airlines.put("name", "Wings Air");
                airlines.put("imageUrl", "https://ik.imagekit.io/holigo/transportasi/wings_ZFKnGYa3N.png?ik-sdk-version=javascript-1.4.3&updatedAt=1660178242989");
            }
            case "JL" -> {
                airlines.put("name", "Japan Airlines");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/JL.png");
            }
            case "JQ" -> {
                airlines.put("name", "Jetstar");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/JQ.png");
            }
            case "KD" -> {
                airlines.put("name", "Kalstar Aviation");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/KD.png");
            }
            case "KE" -> {
                airlines.put("name", "Korean Air");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/KE.png");
            }
            case "KL" -> {
                airlines.put("name", "KLM");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/KL.png");
            }
            case "MH" -> {
                airlines.put("name", "Malaysia Airlines");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/MH.png");
            }
            case "MI" -> {
                airlines.put("name", "SILKAIR");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/MI.png");
            }
            case "MV" -> {
                airlines.put("name", "Batik Air");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/MV.png");
            }
            case "NH" -> {
                airlines.put("name", "ANA");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/NH.png");
            }
            case "ID" -> {
                airlines.put("code", "JT");
                airlines.put("name", "Batik Air");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/ID.png");
            }
            case "OD" -> {
                airlines.put("name", "Malindo Air");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/OD.png");
            }
            case "QF" -> {
                airlines.put("name", "Qantas");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/QF.png");
            }
            case "QG" -> {
                airlines.put("name", "Citilink");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/QG.png");
            }
            case "QR" -> {
                airlines.put("name", "Qatar Airlines");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/QR.png");
            }
            case "QZ" -> {
                airlines.put("name", "AirAsia");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/QZ.png");
            }
            case "SJ" -> {
                airlines.put("name", "Sriwijaya Air");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/SJ.png");
            }
            case "SL" -> {
                airlines.put("name", "Thai Lion air");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/SL.png");
            }
            case "SQ" -> {
                airlines.put("name", "Singapore Airlines");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/SQ.png");
            }
            case "SV" -> {
                airlines.put("name", "Saudia");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/SV.png");
            }
            case "TG" -> {
                airlines.put("name", "Thai");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/TG.png");
            }
            case "TK" -> {
                airlines.put("name", "Turkish Airlines");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/TK.png");
            }
            case "TL" -> {
                airlines.put("name", "Airnorth");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/TL.png");
            }
            case "TR" -> {
                airlines.put("name", "tigerair");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/TR.png");
            }
            case "TZ" -> {
                airlines.put("name", "scoot");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/TZ.png");
            }
            case "VY" -> {
                airlines.put("name", "vueling");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/VY.png");
            }
            case "XJ" -> {
                airlines.put("name", "AirAsia");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/XJ.png");
            }
            case "XT" -> {
                airlines.put("name", "AirAsia");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/XT.png");
            }
            case "Z2" -> {
                airlines.put("name", "ZestAir");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/Z2.png");
            }
            default -> {
                airlines.put("name", airlinesCode);
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/8B.png");
            }
        }
        return airlines;
    }
}
