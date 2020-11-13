package dev.mylesmor.sudosigns.config;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.data.SignMessage;
import dev.mylesmor.sudosigns.data.SudoSign;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageConfig {

    private FileConfiguration signConfig;
    private ConfigManager configManager;

    MessageConfig(ConfigManager configManager) {
        this.configManager = configManager;
        this.signConfig = configManager.getSignConfig();
    }

    public void addMessageToConfig(SudoSign s, SignMessage sm) {
        HashMap<String, Double> map;
        List<Map<?, ?>> messages = signConfig.getMapList("signs." + s.getName() + ".messages");
        map = new HashMap<>();
        map.put(sm.getMessage(), sm.getDelay());
        messages.add(map);
        signConfig.set("signs." + s.getName() + ".messages", messages);
        configManager.save();
    }

    public void deleteMessageFromConfig(SudoSign s, SignMessage sm, double oldDelay) {
        List<Map<?, ?>> mapList = signConfig.getMapList("signs." + s.getName() + ".messages");
        Map<?, ?> found = null;
        for (Map<?, ?> map : mapList) {
            for (Map.Entry<?, ?> messages : map.entrySet()) {
                String key = (String) messages.getKey();
                if (key.equals(sm.getMessage()) && (Double) messages.getValue() == oldDelay) {
                    found = map;
                    break;
                }
            }
        }
        if (found != null) {
            mapList.remove(found);
        }
        signConfig.set("signs." + s.getName() + ".messages", mapList);
        configManager.save();
    }
}
