package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.capabilities.magic.PlayerMagicData;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.spells.SchoolType;
import io.redspace.ironsspellbooks.spells.SpellType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ExtendedSmallFireball extends SmallFireball implements AntiMagicSusceptible {
    private float damage;

    public ExtendedSmallFireball(EntityType<? extends SmallFireball> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ExtendedSmallFireball(LivingEntity shooter, Level level, float speed, float damage, float inaccuracy) {
        this(EntityRegistry.SMALL_FIREBALL_PROJECTILE.get(), level);
        this.damage = damage;
        setOwner(shooter);

        Vec3 power = shooter.getLookAngle().normalize().scale(speed);
        Vec3 inaccuracyVec = new Vec3(
                (random.nextFloat() - .5f) * inaccuracy,
                (random.nextFloat() - .5f) * inaccuracy,
                (random.nextFloat() - .5f) * inaccuracy
        );
        this.xPower = power.x + inaccuracyVec.x;
        this.yPower = power.y + inaccuracyVec.y;
        this.zPower = power.z + inaccuracyVec.z;
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if (!this.level.isClientSide) {
            var target = pResult.getEntity();
            var owner = getOwner();
            if (DamageSources.applyDamage(target, damage, SpellType.BLAZE_STORM_SPELL.getDamageSource(this, owner), SchoolType.FIRE))
                target.setSecondsOnFire(5);
        }
    }

    @Override
    public void onAntiMagic(PlayerMagicData playerMagicData) {
        this.discard();
    }
}
