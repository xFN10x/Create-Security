package dev.xplate.create_security.datagen.provider.tags;

import dev.xplate.create_security.CSSecurity;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CSSEntityTagsProvider extends EntityTypeTagsProvider {
    public static final TagKey<EntityType<?>> IMMUNE_TO_END_SICKNESS = TagKey.create(Registries.ENTITY_TYPE, CSSecurity.res("immune_to_end_sickness"));
    public CSSEntityTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, CSSecurity.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(IMMUNE_TO_END_SICKNESS)
                .add(EntityType.ENDER_DRAGON)
                .add(EntityType.ENDERMAN)
                .add(EntityType.ENDERMITE)
                //Alex's Mobs
                .addOptional(ResourceLocation.fromNamespaceAndPath("alexsmobs", "cosmaw"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("alexsmobs", "cosmic_cod"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("alexsmobs", "endergrade"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("alexsmobs", "enderiophage"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("alexsmobs", "farseer"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("alexsmobs", "mimicube"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("alexsmobs", "spectre"))
                .addOptional(ResourceLocation.fromNamespaceAndPath("alexsmobs", "void_worm"))
        ;
    }
}
