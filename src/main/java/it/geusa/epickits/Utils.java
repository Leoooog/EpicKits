package it.geusa.epickits;

import it.geusa.epickits.models.Kit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static ArrayList<ItemStack> deserializeItems(String encodedItems) {
        ArrayList<ItemStack> deserializedItems = new ArrayList<>();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedItems.getBytes())) {
            DataInputStream input = new DataInputStream(inputStream);
            int count = input.readInt();
            for (int i = 0; i < count; i++) {
                int length = input.readInt();
                if (length == 0) {
                    // Empty item, add null to the list
                    deserializedItems.add(null);
                    continue;
                }

                byte[] itemBytes = new byte[length];
                int res = input.read(itemBytes);
                if (res != length) {
                    throw new IOException("Error while reading itemstack");
                }
                deserializedItems.add(ItemStack.deserializeBytes(itemBytes));
            }
            return deserializedItems;
        } catch (IOException e) {
            throw new RuntimeException("Error while reading itemstack", e);
        }
    }

    public static ItemStack deserializeItemStack(String encodedItem) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedItem.getBytes())) {
            DataInputStream input = new DataInputStream(inputStream);
            int length = input.readInt();
            if (length == 0) {
                return null;
            }
            byte[] itemBytes = new byte[length];
            int res = input.read(itemBytes);
            if (res != length) {
                throw new IOException("Error while reading itemstack");
            }
            return ItemStack.deserializeBytes(itemBytes);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading itemstack", e);
        }
    }

    public static String getSerializedItems(List<ItemStack> items) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            DataOutput output = new DataOutputStream(outputStream);
            output.writeInt(items.size());

            for (ItemStack item : items) {
                if (item == null) {
                    // Ensure the correct order by including empty/null items
                    output.writeInt(0);
                    continue;
                }

                byte[] bytes = item.serializeAsBytes();
                output.writeInt(bytes.length);
                output.write(bytes);
            }
            return outputStream.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error while writing itemstack", e);
        }
    }

    public static String getSerializedItemStack(ItemStack item) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            DataOutput output = new DataOutputStream(outputStream);
            if (item == null) {
                output.writeInt(0);
                return outputStream.toString();
            }

            byte[] bytes = item.serializeAsBytes();
            output.writeInt(bytes.length);
            output.write(bytes);
            return outputStream.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error while writing itemstack", e);
        }
    }

    public static String getKitString(Kit kit) {
        //create a string with kit's id, display name and description
        //TODO: click event to show kit info
        return "§f" + kit.getId() + " - " + ChatColor.translateAlternateColorCodes('&',
                kit.getDisplayName())
                + "§f" + ChatColor.translateAlternateColorCodes('&', kit.getDescription());
    }
}
