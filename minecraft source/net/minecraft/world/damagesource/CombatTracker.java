/*     */ package net.minecraft.world.damagesource;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.HoverEvent;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.util.CommonLinks;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ 
/*     */ public class CombatTracker
/*     */ {
/*     */   public static final int RESET_DAMAGE_STATUS_TIME = 100;
/*     */   public static final int RESET_COMBAT_STATUS_TIME = 300;
/*  24 */   private static final Style INTENTIONAL_GAME_DESIGN_STYLE = Style.EMPTY
/*  25 */     .withClickEvent(new ClickEvent.OpenUrl(CommonLinks.INTENTIONAL_GAME_DESIGN_BUG))
/*  26 */     .withHoverEvent(new HoverEvent.ShowText(Component.literal("MCPE-28723"))); private final List<CombatEntry> entries; private final LivingEntity mob; private int lastDamageTime;
/*     */   public CombatTracker(LivingEntity mob) {
/*  28 */     this.entries = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  37 */     this.mob = mob;
/*     */   }
/*     */   private int combatStartTime; private int combatEndTime; private boolean inCombat; private boolean takingDamage;
/*     */   public void recordDamage(DamageSource source, float damage) {
/*  41 */     recheckStatus();
/*     */     
/*  43 */     FallLocation fallLocation = FallLocation.getCurrentFallLocation(this.mob);
/*  44 */     CombatEntry entry = new CombatEntry(source, damage, fallLocation, (float)this.mob.fallDistance);
/*     */     
/*  46 */     this.entries.add(entry);
/*  47 */     this.lastDamageTime = this.mob.tickCount;
/*  48 */     this.takingDamage = true;
/*     */     
/*  50 */     if (!this.inCombat && this.mob.isAlive() && shouldEnterCombat(source)) {
/*  51 */       this.inCombat = true;
/*  52 */       this.combatStartTime = this.mob.tickCount;
/*  53 */       this.combatEndTime = this.combatStartTime;
/*  54 */       this.mob.onEnterCombat();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  59 */   private static boolean shouldEnterCombat(DamageSource source) { return source.getEntity() instanceof LivingEntity; }
/*     */ 
/*     */   
/*     */   private Component getMessageForAssistedFall(Entity attackerEntity, Component attackerName, String messageWithItem, String messageWithoutItem) {
/*  63 */     LivingEntity livingEntity = (LivingEntity)attackerEntity; ItemStack attackerItem = (attackerEntity instanceof LivingEntity) ? livingEntity.getMainHandItem() : ItemStack.EMPTY;
/*     */     
/*  65 */     if (!attackerItem.isEmpty() && attackerItem.has(DataComponents.CUSTOM_NAME)) {
/*  66 */       return Component.translatable(messageWithItem, new Object[] { this.mob.getDisplayName(), attackerName, attackerItem.getDisplayName() });
/*     */     }
/*     */     
/*  69 */     return Component.translatable(messageWithoutItem, new Object[] { this.mob.getDisplayName(), attackerName });
/*     */   }
/*     */   
/*     */   private Component getFallMessage(CombatEntry knockOffEntry, Entity killingEntity) {
/*  73 */     DamageSource knockOffSource = knockOffEntry.source();
/*     */     
/*  75 */     if (knockOffSource.is(DamageTypeTags.IS_FALL) || knockOffSource.is(DamageTypeTags.ALWAYS_MOST_SIGNIFICANT_FALL)) {
/*  76 */       FallLocation fallLocation = (FallLocation)Objects.requireNonNullElse(knockOffEntry.fallLocation(), FallLocation.GENERIC);
/*  77 */       return Component.translatable(fallLocation.languageKey(), new Object[] { this.mob.getDisplayName() });
/*     */     } 
/*     */     
/*  80 */     Component killerName = getDisplayName(killingEntity);
/*  81 */     Entity attackerEntity = knockOffSource.getEntity();
/*  82 */     Component attackerName = getDisplayName(attackerEntity);
/*     */ 
/*     */     
/*  85 */     if (attackerName != null && !attackerName.equals(killerName)) {
/*  86 */       return getMessageForAssistedFall(attackerEntity, attackerName, "death.fell.assist.item", "death.fell.assist");
/*     */     }
/*     */     
/*  89 */     if (killerName != null) {
/*  90 */       return getMessageForAssistedFall(killingEntity, killerName, "death.fell.finish.item", "death.fell.finish");
/*     */     }
/*     */     
/*  93 */     return Component.translatable("death.fell.killer", new Object[] { this.mob.getDisplayName() });
/*     */   }
/*     */ 
/*     */   
/*  97 */   private static Component getDisplayName(Entity entity) { return (entity == null) ? null : entity.getDisplayName(); }
/*     */ 
/*     */   
/*     */   public Component getDeathMessage() {
/* 101 */     if (this.entries.isEmpty()) {
/* 102 */       return Component.translatable("death.attack.generic", new Object[] { this.mob.getDisplayName() });
/*     */     }
/*     */     
/* 105 */     CombatEntry killingBlow = (CombatEntry)this.entries.get(this.entries.size() - 1);
/* 106 */     DamageSource killingSource = killingBlow.source();
/*     */     
/* 108 */     CombatEntry knockOffEntry = getMostSignificantFall();
/*     */     
/* 110 */     DeathMessageType messageType = killingSource.type().deathMessageType();
/* 111 */     if (messageType == DeathMessageType.FALL_VARIANTS && knockOffEntry != null) {
/* 112 */       return getFallMessage(knockOffEntry, killingSource.getEntity());
/*     */     }
/*     */     
/* 115 */     if (messageType == DeathMessageType.INTENTIONAL_GAME_DESIGN) {
/* 116 */       String deathMsg = "death.attack." + killingSource.getMsgId();
/* 117 */       MutableComponent mutableComponent = ComponentUtils.wrapInSquareBrackets(Component.translatable(deathMsg + ".link")).withStyle(INTENTIONAL_GAME_DESIGN_STYLE);
/* 118 */       return Component.translatable(deathMsg + ".message", new Object[] { this.mob.getDisplayName(), mutableComponent });
/*     */     } 
/*     */     
/* 121 */     return killingSource.getLocalizedDeathMessage(this.mob);
/*     */   }
/*     */   
/*     */   private CombatEntry getMostSignificantFall() {
/* 125 */     CombatEntry result = null;
/* 126 */     CombatEntry alternative = null;
/* 127 */     float altDamage = 0.0F;
/* 128 */     float bestFall = 0.0F;
/*     */     
/* 130 */     for (int i = 0; i < this.entries.size(); i++) {
/* 131 */       CombatEntry entry = (CombatEntry)this.entries.get(i);
/* 132 */       CombatEntry previous = (i > 0) ? (CombatEntry)this.entries.get(i - 1) : null;
/*     */       
/* 134 */       DamageSource source = entry.source();
/* 135 */       boolean isFakeFall = source.is(DamageTypeTags.ALWAYS_MOST_SIGNIFICANT_FALL);
/* 136 */       float fallDistance = isFakeFall ? Float.MAX_VALUE : entry.fallDistance();
/* 137 */       if ((source.is(DamageTypeTags.IS_FALL) || isFakeFall) && fallDistance > 0.0F && (result == null || fallDistance > bestFall)) {
/* 138 */         if (i > 0) {
/* 139 */           result = previous;
/*     */         } else {
/* 141 */           result = entry;
/*     */         } 
/* 143 */         bestFall = fallDistance;
/*     */       } 
/*     */       
/* 146 */       if (entry.fallLocation() != null && (alternative == null || entry.damage() > altDamage)) {
/* 147 */         alternative = entry;
/* 148 */         altDamage = entry.damage();
/*     */       } 
/*     */     } 
/*     */     
/* 152 */     if (bestFall > 5.0F && result != null)
/* 153 */       return result; 
/* 154 */     if (altDamage > 5.0F && alternative != null) {
/* 155 */       return alternative;
/*     */     }
/* 157 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCombatDuration() {
/* 162 */     if (this.inCombat) {
/* 163 */       return this.mob.tickCount - this.combatStartTime;
/*     */     }
/* 165 */     return this.combatEndTime - this.combatStartTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void recheckStatus() {
/* 170 */     int reset = this.inCombat ? 300 : 100;
/*     */     
/* 172 */     if (this.takingDamage && (!this.mob.isAlive() || this.mob.tickCount - this.lastDamageTime > reset)) {
/* 173 */       boolean wasInCombat = this.inCombat;
/* 174 */       this.takingDamage = false;
/* 175 */       this.inCombat = false;
/* 176 */       this.combatEndTime = this.mob.tickCount;
/*     */       
/* 178 */       if (wasInCombat) {
/* 179 */         this.mob.onLeaveCombat();
/*     */       }
/* 181 */       this.entries.clear();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\damagesource\CombatTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */