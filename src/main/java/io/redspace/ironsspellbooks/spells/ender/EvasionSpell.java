package io.redspace.ironsspellbooks.spells.ender;

import com.mojang.datafixers.util.Either;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerMagicData;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.spells.AbstractSpell;
import io.redspace.ironsspellbooks.spells.SpellType;
import io.redspace.ironsspellbooks.spells.holy.HealSpell;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.builder.AnimationBuilder;

import java.util.List;
import java.util.Optional;

public class EvasionSpell extends AbstractSpell {
    public EvasionSpell() {
        this(1);
    }

    @Override
    public List<MutableComponent> getUniqueInfo(LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.hits_dodged", (int) getSpellPower(caster)));
    }

    public EvasionSpell(int level) {
        super(SpellType.EVASION_SPELL);
        this.level = level;
        this.manaCostPerLevel = 20;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 40;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.empty();
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }

    @Override
    public void onCast(Level world, LivingEntity entity, PlayerMagicData playerMagicData) {
        entity.addEffect(new MobEffectInstance(MobEffectRegistry.EVASION.get(), 60 * 20, (int) getSpellPower(entity), false, false, true));
        super.onCast(world, entity, playerMagicData);
    }

    @Override
    public Either<AnimationBuilder, ResourceLocation> getCastStartAnimation(Player player) {
        return player == null ? Either.left(HealSpell.ANIMATION_CAST) : Either.right(HealSpell.ANIMATION_CAST_RESOURCE);
    }
}
