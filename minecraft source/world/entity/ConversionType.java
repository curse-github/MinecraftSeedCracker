/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.monster.zombie.Zombie;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.scores.Scoreboard;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public static final abstract enum ConversionType
/*     */ {
/*     */   SINGLE, SPLIT_ON_DEATH;
/*     */   private static final Set<DataComponentType<?>> COMPONENTS_TO_COPY;
/*     */   private final boolean discardAfterConversion;
/*     */   
/*     */   static  {
/*     */     // Byte code:
/*     */     //   0: new net/minecraft/world/entity/ConversionType$1
/*     */     //   3: dup
/*     */     //   4: ldc_w 'SINGLE'
/*     */     //   7: iconst_0
/*     */     //   8: iconst_1
/*     */     //   9: invokespecial <init> : (Ljava/lang/String;IZ)V
/*     */     //   12: putstatic net/minecraft/world/entity/ConversionType.SINGLE : Lnet/minecraft/world/entity/ConversionType;
/*     */     //   15: new net/minecraft/world/entity/ConversionType$2
/*     */     //   18: dup
/*     */     //   19: ldc_w 'SPLIT_ON_DEATH'
/*     */     //   22: iconst_1
/*     */     //   23: iconst_0
/*     */     //   24: invokespecial <init> : (Ljava/lang/String;IZ)V
/*     */     //   27: putstatic net/minecraft/world/entity/ConversionType.SPLIT_ON_DEATH : Lnet/minecraft/world/entity/ConversionType;
/*     */     //   30: invokestatic $values : ()[Lnet/minecraft/world/entity/ConversionType;
/*     */     //   33: putstatic net/minecraft/world/entity/ConversionType.$VALUES : [Lnet/minecraft/world/entity/ConversionType;
/*     */     //   36: getstatic net/minecraft/core/component/DataComponents.CUSTOM_NAME : Lnet/minecraft/core/component/DataComponentType;
/*     */     //   39: getstatic net/minecraft/core/component/DataComponents.CUSTOM_DATA : Lnet/minecraft/core/component/DataComponentType;
/*     */     //   42: invokestatic of : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/Set;
/*     */     //   45: putstatic net/minecraft/world/entity/ConversionType.COMPONENTS_TO_COPY : Ljava/util/Set;
/*     */     //   48: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #17	-> 0
/*     */     //   #76	-> 15
/*     */     //   #15	-> 30
/*     */     //   #95	-> 36
/*     */   }
/*     */   
/* 103 */   ConversionType(boolean discardAfterConversion) { this.discardAfterConversion = discardAfterConversion; }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public boolean shouldDiscardAfterConversion() { return this.discardAfterConversion; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void convertCommon(Mob from, Mob to, ConversionParams params) {
/* 113 */     to.setAbsorptionAmount(from.getAbsorptionAmount());
/*     */     
/* 115 */     for (MobEffectInstance effect : from.getActiveEffects()) {
/* 116 */       to.addEffect(new MobEffectInstance(effect));
/*     */     }
/*     */     
/* 119 */     if (from.isBaby()) {
/* 120 */       to.setBaby(true);
/*     */     }
/*     */     
/* 123 */     if (from instanceof AgeableMob) { AgeableMob oldAgeable = (AgeableMob)from; if (to instanceof AgeableMob) { AgeableMob convertedAgeable = (AgeableMob)to;
/* 124 */         convertedAgeable.setAge(oldAgeable.getAge());
/* 125 */         convertedAgeable.forcedAge = oldAgeable.forcedAge;
/* 126 */         convertedAgeable.forcedAgeTimer = oldAgeable.forcedAgeTimer; }
/*     */        }
/*     */     
/* 129 */     Brain<?> oldBrain = from.getBrain();
/* 130 */     Brain<?> convertedBrain = to.getBrain();
/*     */     
/* 132 */     if (oldBrain.checkMemory(MemoryModuleType.ANGRY_AT, MemoryStatus.REGISTERED) && oldBrain.hasMemoryValue(MemoryModuleType.ANGRY_AT)) {
/* 133 */       convertedBrain.setMemory(MemoryModuleType.ANGRY_AT, oldBrain.getMemory(MemoryModuleType.ANGRY_AT));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 140 */     if (params.preserveCanPickUpLoot()) {
/* 141 */       to.setCanPickUpLoot(from.canPickUpLoot());
/*     */     }
/*     */     
/* 144 */     to.setLeftHanded(from.isLeftHanded());
/* 145 */     to.setNoAi(from.isNoAi());
/*     */     
/* 147 */     if (from.isPersistenceRequired()) {
/* 148 */       to.setPersistenceRequired();
/*     */     }
/*     */     
/* 151 */     to.setCustomNameVisible(from.isCustomNameVisible());
/*     */     
/* 153 */     to.setSharedFlagOnFire(from.isOnFire());
/*     */     
/* 155 */     to.setInvulnerable(from.isInvulnerable());
/*     */     
/* 157 */     to.setNoGravity(from.isNoGravity());
/*     */     
/* 159 */     to.setPortalCooldown(from.getPortalCooldown());
/*     */     
/* 161 */     to.setSilent(from.isSilent());
/*     */     
/* 163 */     Objects.requireNonNull(to); from.getTags().forEach(to::addTag);
/*     */     
/* 165 */     for (DataComponentType<?> component : COMPONENTS_TO_COPY) {
/* 166 */       copyComponent(from, to, component);
/*     */     }
/*     */     
/* 169 */     if (params.team() != null) {
/* 170 */       Scoreboard scoreboard = to.level().getScoreboard();
/* 171 */       scoreboard.addPlayerToTeam(to.getStringUUID(), params.team());
/*     */ 
/*     */ 
/*     */       
/* 175 */       if (from.getTeam() != null && from.getTeam() == params.team()) {
/* 176 */         scoreboard.removePlayerFromTeam(from.getStringUUID(), from.getTeam());
/*     */       }
/*     */     } 
/*     */     
/* 180 */     if (from instanceof Zombie) { Zombie fromZombie = (Zombie)from; if (fromZombie.canBreakDoors() && to instanceof Zombie) { Zombie toZombie = (Zombie)to;
/* 181 */         toZombie.setCanBreakDoors(true); }
/*     */        }
/*     */   
/*     */   }
/*     */   private static <T> void copyComponent(Mob from, Mob to, DataComponentType<T> componentType) {
/* 186 */     T value = (T)from.get(componentType);
/* 187 */     if (value != null)
/* 188 */       to.setComponent(componentType, value); 
/*     */   }
/*     */   
/*     */   abstract void convert(Mob paramMob1, Mob paramMob2, ConversionParams paramConversionParams);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ConversionType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */