package com.nandoothjuuh.throwablefireballs.compat;

import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Legacy version adapter for older Bukkit/Spigot versions
 * Uses reflection to call NMS methods if needed
 * This is a fallback and should rarely be needed on modern Paper
 */
public class LegacyVersionAdapter implements VersionAdapter {

    private Method getHandle;
    private Method sendPacket;
    private Constructor<?> packetConstructor;
    private Constructor<?> chatComponentConstructor;
    private Class<?> chatBaseComponent;
    private boolean initialized = false;
    private boolean initFailed = false;

    public LegacyVersionAdapter() {
        try {
            initializeReflection();
            initialized = true;
        } catch (Exception e) {
            initFailed = true;
            // Reflection failed, will fall back to chat messages
        }
    }

    private void initializeReflection() throws Exception {
        // This is a simplified version that attempts to use reflection
        // In practice, on Paper 1.16.5+, the ModernVersionAdapter should work
        
        // Get server version
        String version = org.bukkit.Bukkit.getServer().getClass().getPackage().getName();
        version = version.substring(version.lastIndexOf('.') + 1);
        
        // Get NMS classes
        Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
        Class<?> entityPlayerClass = Class.forName("net.minecraft.server." + version + ".EntityPlayer");
        Class<?> packetClass = Class.forName("net.minecraft.server." + version + ".Packet");
        Class<?> playerConnectionClass = Class.forName("net.minecraft.server." + version + ".PlayerConnection");
        Class<?> packetPlayOutChatClass = Class.forName("net.minecraft.server." + version + ".PacketPlayOutChat");
        chatBaseComponent = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent");
        Class<?> chatSerializerClass = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent$ChatSerializer");
        
        // Get methods
        getHandle = craftPlayerClass.getMethod("getHandle");
        sendPacket = playerConnectionClass.getMethod("sendPacket", packetClass);
        
        // Get constructors
        packetConstructor = packetPlayOutChatClass.getConstructor(chatBaseComponent, byte.class);
        
        // Chat serializer
        Method serializeMethod = chatSerializerClass.getMethod("a", String.class);
        chatComponentConstructor = null; // We'll use the serializer instead
    }

    @Override
    public void sendActionBar(Player player, String message) {
        if (initFailed || !initialized) {
            // Fallback to regular message
            player.sendMessage(message);
            return;
        }

        try {
            // Try reflection-based action bar
            sendActionBarReflection(player, message);
        } catch (Exception e) {
            // Fallback to chat message
            player.sendMessage(message);
        }
    }

    private void sendActionBarReflection(Player player, String message) throws Exception {
        // Get the player handle
        Object entityPlayer = getHandle.invoke(player);
        
        // Get player connection
        Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
        
        // Create chat component from JSON
        String json = "{\"text\":\"" + message.replace("\"", "\\\"") + "\"}";
        Method chatSerializer = chatBaseComponent.getClasses()[0].getMethod("a", String.class);
        Object chatComponent = chatSerializer.invoke(null, json);
        
        // Create packet (2 = action bar)
        Object packet = packetConstructor.newInstance(chatComponent, (byte) 2);
        
        // Send packet
        sendPacket.invoke(playerConnection, packet);
    }

    @Override
    public String getAdapterName() {
        if (initFailed) {
            return "LegacyVersionAdapter (Reflection - FAILED, using chat fallback)";
        } else if (initialized) {
            return "LegacyVersionAdapter (Reflection - working)";
        } else {
            return "LegacyVersionAdapter (not initialized)";
        }
    }
}
