package io.redspace.ironsspellbooks.registries;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.loot.AppendLootModifier;
import io.redspace.ironsspellbooks.loot.RandomizeSpellFunction;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LootRegistry {
    public static final DeferredRegister<LootItemFunctionType> LOOT_FUNCTIONS = DeferredRegister.create(Registry.LOOT_FUNCTION_REGISTRY, IronsSpellbooks.MODID);
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, IronsSpellbooks.MODID);

    public static void register(IEventBus eventBus) {
        LOOT_FUNCTIONS.register(eventBus);
        LOOT_MODIFIER_SERIALIZERS.register(eventBus);
    }

    public static final RegistryObject<LootItemFunctionType> RANDOMIZE_SPELL_FUNCTION = LOOT_FUNCTIONS.register("randomize_spell", () -> new LootItemFunctionType(new RandomizeSpellFunction.Serializer()));

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> APPEND_LOOT_MODIFIER = LOOT_MODIFIER_SERIALIZERS.register("append_loot", AppendLootModifier.CODEC);
}
