package net.krlite.ivespoken.config.modmenu;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.krlite.ivespoken.IveSpoken;
import net.minecraft.client.gui.screen.Screen;

public class IveSpokenConfigScreen {
    private final ConfigBuilder builder = ConfigBuilder.create()
            .setTitle(IveSpoken.translatable("screen", "config"))
            .transparentBackground()
            .setSavingRunnable(IveSpoken.CONFIG::save);
    private final ConfigEntryBuilder entryBuilder = builder.entryBuilder();

    public IveSpokenConfigScreen(Screen parent) {
        builder.setParentScreen(parent);
        initEntries();
    }

    public Screen build() {
        return builder.build();
    }

    private final ConfigCategory general = builder.getOrCreateCategory(IveSpoken.translatable("config", "category", "general"));

    private void initEntries() {
        general.addEntry(entryBuilder.startLongSlider(
                        IveSpoken.translatable("config", "general", "lasting_time"),
                        IveSpoken.CONFIG.lastingTime(), 0, 1000 * 60
                )
                .setDefaultValue(1000 * 5)
                .setSaveConsumer(IveSpoken.CONFIG::lastingTime)
                .build());

        general.addEntry(entryBuilder.startIntSlider(
                        IveSpoken.translatable("config", "general", "max_width"),
                        IveSpoken.CONFIG.maxWidth(), 0, 1000
                )
                .setDefaultValue(180)
                .setSaveConsumer(IveSpoken.CONFIG::maxWidth)
                .build());
    }
}
