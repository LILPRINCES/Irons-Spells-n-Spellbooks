package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.capabilities.magic.PlayerMagicData;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public abstract class AbstractMagicProjectile extends Projectile implements AntiMagicSusceptible {
    protected static final int EXPIRE_TIME = 15 * 20;

    protected int age;
    protected float damage;

    /**
     * Client Side, called every tick
     */
    public abstract void trailParticles();
    /**
     * Server Side, called alongside onHit()
     */
    public abstract void impactParticles(double x, double y, double z);
    public abstract float getSpeed();
    public abstract Optional<SoundEvent> getImpactSound();

    protected AbstractMagicProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public void shoot(Vec3 rotation) {
        setDeltaMovement(rotation.scale(getSpeed()));
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return damage;
    }

    public boolean respectsGravity() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (age > EXPIRE_TIME) {
            discard();
            return;
        }
        if (level.isClientSide) {
            trailParticles();


        }
        HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS) {
            onHit(hitresult);
        }
        setPos(position().add(getDeltaMovement()));

        if (!this.isNoGravity() && respectsGravity()) {
            Vec3 vec34 = this.getDeltaMovement();
            this.setDeltaMovement(vec34.x, vec34.y - (double) 0.05F, vec34.z);
        }

        age++;
    }

    @Override
    protected void onHit(HitResult hitresult) {
        super.onHit(hitresult);
        double x = xOld;
        double y = yOld;
        double z = zOld;

        if (!level.isClientSide) {
            impactParticles(x, y, z);
            getImpactSound().ifPresent((soundEvent -> level.playSound(null, getX(), getY(), getZ(), soundEvent, SoundSource.NEUTRAL, 2, .9f + level.random.nextFloat() * .2f)));
        }
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void onAntiMagic(PlayerMagicData playerMagicData) {
        this.impactParticles(getX(), getY(), getZ());
        this.discard();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putFloat("Damage", this.getDamage());

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.damage = pCompound.getFloat("Damage");
    }
}
