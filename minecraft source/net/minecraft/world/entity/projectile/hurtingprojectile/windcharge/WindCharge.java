/*    */ package net.minecraft.world.entity.projectile.hurtingprojectile.windcharge;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityReference;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.ProjectileDeflection;
/*    */ import net.minecraft.world.level.ExplosionDamageCalculator;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.SimpleExplosionDamageCalculator;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class WindCharge
/*    */   extends AbstractWindCharge
/*    */ {
/* 25 */   private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new SimpleExplosionDamageCalculator(true, false, 
/*    */ 
/*    */       
/* 28 */       Optional.of(Float.valueOf(1.22F)), BuiltInRegistries.BLOCK
/* 29 */       .get(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity()));
/*    */   
/*    */   private static final float RADIUS = 1.2F;
/*    */   
/* 33 */   private static final float MIN_CAMERA_DISTANCE_SQUARED = Mth.square(3.5F);
/*    */   
/* 35 */   private int noDeflectTicks = 5;
/*    */ 
/*    */   
/* 38 */   public WindCharge(EntityType<? extends AbstractWindCharge> type, Level level) { super(type, level); }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public WindCharge(Player player, Level level, double x, double y, double z) { super(EntityType.WIND_CHARGE, level, player, x, y, z); }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public WindCharge(Level level, double x, double y, double z, Vec3 direction) { super(EntityType.WIND_CHARGE, x, y, z, direction, level); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void tick() {
/* 51 */     super.tick();
/* 52 */     if (this.noDeflectTicks > 0) {
/* 53 */       this.noDeflectTicks--;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean deflect(ProjectileDeflection deflection, Entity deflectingEntity, EntityReference<Entity> newOwner, boolean byAttack) {
/* 59 */     if (this.noDeflectTicks > 0) {
/* 60 */       return false;
/*    */     }
/* 62 */     return super.deflect(deflection, deflectingEntity, newOwner, byAttack);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 67 */   protected void explode(Vec3 position) { level().explode(this, null, EXPLOSION_DAMAGE_CALCULATOR, position.x(), position.y(), position.z(), 1.2F, false, Level.ExplosionInteraction.TRIGGER, ParticleTypes.GUST_EMITTER_SMALL, ParticleTypes.GUST_EMITTER_LARGE, WeightedList.of(), SoundEvents.WIND_CHARGE_BURST); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean shouldRenderAtSqrDistance(double distance) {
/* 72 */     if (this.tickCount < 2 && distance < MIN_CAMERA_DISTANCE_SQUARED) {
/* 73 */       return false;
/*    */     }
/* 75 */     return super.shouldRenderAtSqrDistance(distance);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\projectile\hurtingprojectile\windcharge\WindCharge.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */