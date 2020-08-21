package me.juancarloscp52.bedrockify.client.gui;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TipTextSupplier {
    private static final Identifier TIPS_RESOURCE = new Identifier("bedrockify", "texts/tips.txt");
    private final Random random = new Random();
    List<String> tipsTexts = new ArrayList<>();

    public TipTextSupplier() {
        load();
    }


    private void load() {
        try {
            Resource resource = MinecraftClient.getInstance().getResourceManager().getResource(TIPS_RESOURCE);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
            tipsTexts = bufferedReader.lines().map(String::trim).filter((string) -> string.hashCode() != 125780783).collect(Collectors.toList());
            bufferedReader.close();
            resource.close();
        } catch (IOException e) {
            BedrockifyClient.LOGGER.error("Could not load Loading Screen tip texts: " + e.getLocalizedMessage());
        }
    }

    public String getRandomTip() {
        return tipsTexts.get(random.nextInt(tipsTexts.size()));
    }
}
