package com.example.testmod.spells.fire;

import com.example.testmod.capabilities.magic.PlayerMagicData;
import com.example.testmod.entity.firebolt.FireboltProjectile;
import com.example.testmod.entity.magic_missile.MagicMissileProjectile;
import com.example.testmod.spells.AbstractSpell;
import com.example.testmod.spells.SpellType;
import com.example.testmod.util.Utils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FireboltSpell extends AbstractSpell {
    public FireboltSpell() {
        this(1);
    }

    public final MutableComponent uniqueText;

    public FireboltSpell(int level) {
        super(SpellType.FIREBOLT_SPELL);
        this.level = level;
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 2;
        this.spellPowerPerLevel = 2;
        this.castTime = 0;
        this.baseManaCost = 5;
        this.cooldown = 0;
        uniqueText = Component.translatable("ui.testmod.damage", Utils.stringTruncation(getSpellPower(null), 1));
    }

    @Override
    public void onCast(Level world, Player player, PlayerMagicData playerMagicData) {
        FireboltProjectile firebolt = new FireboltProjectile(world, player);
        firebolt.setPos(player.position().add(0, player.getEyeHeight() - firebolt.getBoundingBox().getYsize() * .5f, 0));
        firebolt.shoot(player.getLookAngle());
        firebolt.setDamage(getSpellPower(player));
        world.addFreshEntity(firebolt);
    }

    @Override
    public MutableComponent getUniqueInfo() {
        return uniqueText;
    }
}
