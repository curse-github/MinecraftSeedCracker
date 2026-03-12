/*     */ package net.minecraft.world.level.storage.loot;
/*     */ 
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
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
/*     */ public static enum EntityTarget
/*     */   implements StringRepresentable, LootContextArg.SimpleGetter<Entity>
/*     */ {
/* 115 */   THIS("this", LootContextParams.THIS_ENTITY),
/* 116 */   ATTACKER("attacker", LootContextParams.ATTACKING_ENTITY),
/* 117 */   DIRECT_ATTACKER("direct_attacker", LootContextParams.DIRECT_ATTACKING_ENTITY),
/* 118 */   ATTACKING_PLAYER("attacking_player", LootContextParams.LAST_DAMAGE_PLAYER),
/* 119 */   TARGET_ENTITY("target_entity", LootContextParams.TARGET_ENTITY),
/* 120 */   INTERACTING_ENTITY("interacting_entity", LootContextParams.INTERACTING_ENTITY);
/*     */   
/*     */   static  {
/* 123 */     CODEC = StringRepresentable.fromEnum(EntityTarget::values);
/*     */   }
/*     */   public static final StringRepresentable.EnumCodec<EntityTarget> CODEC;
/*     */   private final String name;
/*     */   private final ContextKey<? extends Entity> param;
/*     */   
/*     */   EntityTarget(String name, ContextKey<? extends Entity> param) {
/* 130 */     this.name = name;
/* 131 */     this.param = param;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 136 */   public ContextKey<? extends Entity> contextParam() { return this.param; }
/*     */ 
/*     */   
/*     */   public static EntityTarget getByName(String name) {
/* 140 */     EntityTarget target = (EntityTarget)CODEC.byName(name);
/* 141 */     if (target != null) {
/* 142 */       return target;
/*     */     }
/* 144 */     throw new IllegalArgumentException("Invalid entity target " + name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 149 */   public String getSerializedName() { return this.name; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\LootContext$EntityTarget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */