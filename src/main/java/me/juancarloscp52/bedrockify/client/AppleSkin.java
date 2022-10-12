package me.juancarloscp52.bedrockify.client;

import squeek.appleskin.api.AppleSkinApi;
import squeek.appleskin.api.event.HUDOverlayEvent;

public class AppleSkin implements AppleSkinApi {
    @Override
    public void registerEvents() {
        HUDOverlayEvent.Exhaustion.EVENT.register(obj -> obj.y -= BedrockifyClient.getInstance().settings.getScreenSafeArea());
        HUDOverlayEvent.HealthRestored.EVENT.register(obj -> obj.y -= BedrockifyClient.getInstance().settings.getScreenSafeArea());
        HUDOverlayEvent.HungerRestored.EVENT.register(obj -> obj.y -= BedrockifyClient.getInstance().settings.getScreenSafeArea());
        HUDOverlayEvent.Saturation.EVENT.register(obj -> obj.y -= BedrockifyClient.getInstance().settings.getScreenSafeArea());
    }
}
