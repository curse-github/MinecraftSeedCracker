/*     */ package net.minecraft.world.damagesource;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DamageSource
/*     */ {
/*     */   private final Holder<DamageType> type;
/*     */   private final Entity causingEntity;
/*     */   private final Entity directEntity;
/*     */   private final Vec3 damageSourcePosition;
/*     */   
/*  23 */   public String toString() { return "DamageSource (" + type().msgId() + ")"; }
/*     */ 
/*     */ 
/*     */   
/*  27 */   public float getFoodExhaustion() { return type().exhaustion(); }
/*     */ 
/*     */ 
/*     */   
/*  31 */   public boolean isDirect() { return (this.causingEntity == this.directEntity); }
/*     */ 
/*     */   
/*     */   private DamageSource(Holder<DamageType> type, Entity directEntity, Entity causingEntity, Vec3 damageSourcePosition) {
/*  35 */     this.type = type;
/*  36 */     this.causingEntity = causingEntity;
/*  37 */     this.directEntity = directEntity;
/*  38 */     this.damageSourcePosition = damageSourcePosition;
/*     */   }
/*     */ 
/*     */   
/*  42 */   public DamageSource(Holder<DamageType> type, Entity directEntity, Entity causingEntity) { this(type, directEntity, causingEntity, null); }
/*     */ 
/*     */ 
/*     */   
/*  46 */   public DamageSource(Holder<DamageType> type, Vec3 damageSourcePosition) { this(type, null, null, damageSourcePosition); }
/*     */ 
/*     */ 
/*     */   
/*  50 */   public DamageSource(Holder<DamageType> type, Entity causingEntity) { this(type, causingEntity, causingEntity); }
/*     */ 
/*     */ 
/*     */   
/*  54 */   public DamageSource(Holder<DamageType> type) { this(type, null, null, null); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   public Entity getDirectEntity() { return this.directEntity; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   public Entity getEntity() { return this.causingEntity; }
/*     */ 
/*     */ 
/*     */   
/*  74 */   public ItemStack getWeaponItem() { return (this.directEntity != null) ? this.directEntity.getWeaponItem() : null; }
/*     */ 
/*     */   
/*     */   public Component getLocalizedDeathMessage(LivingEntity victim) {
/*  78 */     String deathMsg = "death.attack." + type().msgId();
/*  79 */     if (this.causingEntity != null || this.directEntity != null) {
/*  80 */       Component name = (this.causingEntity == null) ? this.directEntity.getDisplayName() : this.causingEntity.getDisplayName();
/*  81 */       Entity entity = this.causingEntity; LivingEntity livingEntity = (LivingEntity)entity; ItemStack held = (entity instanceof LivingEntity) ? livingEntity.getMainHandItem() : ItemStack.EMPTY;
/*     */       
/*  83 */       if (!held.isEmpty() && held.has(DataComponents.CUSTOM_NAME)) {
/*  84 */         return Component.translatable(deathMsg + ".item", new Object[] { victim.getDisplayName(), name, held.getDisplayName() });
/*     */       }
/*  86 */       return Component.translatable(deathMsg, new Object[] { victim.getDisplayName(), name });
/*     */     } 
/*     */ 
/*     */     
/*  90 */     LivingEntity source = victim.getKillCredit();
/*  91 */     String playerMsg = deathMsg + ".player";
/*  92 */     if (source != null) {
/*  93 */       return Component.translatable(playerMsg, new Object[] { victim.getDisplayName(), source.getDisplayName() });
/*     */     }
/*  95 */     return Component.translatable(deathMsg, new Object[] { victim.getDisplayName() });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 100 */   public String getMsgId() { return type().msgId(); }
/*     */ 
/*     */   
/*     */   public boolean scalesWithDifficulty() {
/* 104 */     switch (type().scaling()) { default: throw new MatchException(null, null);case NEVER: case WHEN_CAUSED_BY_LIVING_NON_PLAYER: return 
/*     */           
/* 106 */           (this.causingEntity instanceof LivingEntity && !(this.causingEntity instanceof Player));
/*     */       case ALWAYS:
/*     */         break; }
/*     */     
/*     */     return true;
/*     */   } public boolean isCreativePlayer() {
/* 112 */     Entity entity = getEntity(); if (entity instanceof Player) { Player player = (Player)entity; if ((player.getAbilities()).instabuild); }  return false;
/*     */   }
/*     */   
/*     */   public Vec3 getSourcePosition() {
/* 116 */     if (this.damageSourcePosition != null)
/* 117 */       return this.damageSourcePosition; 
/* 118 */     if (this.directEntity != null) {
/* 119 */       return this.directEntity.position();
/*     */     }
/* 121 */     return null;
/*     */   }
/*     */ 
/*     */   
/* 125 */   public Vec3 sourcePositionRaw() { return this.damageSourcePosition; }
/*     */ 
/*     */ 
/*     */   
/* 129 */   public boolean is(TagKey<DamageType> tag) { return this.type.is(tag); }
/*     */ 
/*     */ 
/*     */   
/* 133 */   public boolean is(ResourceKey<DamageType> typeKey) { return this.type.is(typeKey); }
/*     */ 
/*     */ 
/*     */   
/* 137 */   public DamageType type() { return (DamageType)this.type.value(); }
/*     */ 
/*     */ 
/*     */   
/* 141 */   public Holder<DamageType> typeHolder() { return this.type; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\damagesource\DamageSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */