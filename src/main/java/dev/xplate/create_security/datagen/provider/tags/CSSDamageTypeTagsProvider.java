package dev.xplate.create_security.datagen.provider.tags;

import dev.xplate.create_security.CSSecurity;
import dev.xplate.create_security.reg.SecurityDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class CSSDamageTypeTagsProvider extends DamageTypeTagsProvider {
    public CSSDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CSSecurity.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(DamageTypeTags.ALWAYS_TRIGGERS_SILVERFISH).add(SecurityDamageTypes.END_SICKNESS);
        tag(DamageTypeTags.BYPASSES_ARMOR).add(SecurityDamageTypes.END_SICKNESS);
        tag(DamageTypeTags.BYPASSES_COOLDOWN).add(SecurityDamageTypes.END_SICKNESS);
        tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).add(SecurityDamageTypes.END_SICKNESS);
        tag(DamageTypeTags.BYPASSES_RESISTANCE).add(SecurityDamageTypes.END_SICKNESS);
        tag(DamageTypeTags.BYPASSES_WOLF_ARMOR).add(SecurityDamageTypes.END_SICKNESS);
        tag(DamageTypeTags.NO_KNOCKBACK).add(SecurityDamageTypes.END_SICKNESS);
        tag(DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES).add(SecurityDamageTypes.END_SICKNESS);
        tag(DamageTypeTags.PANIC_CAUSES).add(SecurityDamageTypes.END_SICKNESS);
    }
}
