package mystatistics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

public class MyStatisticsExtendedData {
    private final File file;

    public LocalDate lastClearPlayingTime = LocalDate.of(1999, 4, 6);

    public LocalDate lastSummarizing = LocalDate.of(1999, 4, 6);

    public MyStatisticsExtendedData(File f) {
        this.file = f;
    }

    public void save() {
        try (FileOutputStream out = new FileOutputStream(this.file)) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setLong("lastClearPlayingTime", this.lastClearPlayingTime.toEpochDay());
            tag.setLong("lastSummarizing", this.lastSummarizing.toEpochDay());
            CompressedStreamTools.writeCompressed(tag, out);
        } catch (IOException e) {
            MyStatisticMod.logger.error("Failed to save statistics extended data.", e);
        }
    }

    public void load() {
        if (!this.file.exists())
            return;
        try (FileInputStream in = new FileInputStream(this.file)) {
            NBTTagCompound tag = CompressedStreamTools.readCompressed(in);
            this.lastClearPlayingTime = LocalDate.ofEpochDay(tag.getLong("lastClearPlayingTime"));
            this.lastSummarizing = LocalDate.ofEpochDay(tag.getLong("lastSummarizing"));
        } catch (IOException e) {
            MyStatisticMod.logger.error("Failed to load statistics extended data.", e);
        }
    }
}
