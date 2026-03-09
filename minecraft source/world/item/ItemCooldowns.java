/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.item.component.UseCooldown;
/*    */ 
/*    */ public class ItemCooldowns
/*    */ {
/* 14 */   private final Map<Identifier, CooldownInstance> cooldowns = Maps.newHashMap();
/*    */   
/*    */   private int tickCount;
/*    */   
/* 18 */   public boolean isOnCooldown(ItemStack item) { return (getCooldownPercent(item, 0.0F) > 0.0F); }
/*    */ 
/*    */   
/*    */   public float getCooldownPercent(ItemStack item, float a) {
/* 22 */     Identifier group = getCooldownGroup(item);
/* 23 */     CooldownInstance cooldown = (CooldownInstance)this.cooldowns.get(group);
/*    */     
/* 25 */     if (cooldown != null) {
/* 26 */       float duration = (cooldown.endTime - cooldown.startTime);
/* 27 */       float remaining = cooldown.endTime - this.tickCount + a;
/* 28 */       return Mth.clamp(remaining / duration, 0.0F, 1.0F);
/*    */     } 
/*    */     
/* 31 */     return 0.0F;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 35 */     this.tickCount++;
/*    */     
/* 37 */     if (!this.cooldowns.isEmpty()) {
/* 38 */       for (Iterator<Map.Entry<Identifier, CooldownInstance>> iterator = this.cooldowns.entrySet().iterator(); iterator.hasNext(); ) {
/* 39 */         Map.Entry<Identifier, CooldownInstance> entry = (Map.Entry)iterator.next();
/* 40 */         if (((CooldownInstance)entry.getValue()).endTime <= this.tickCount) {
/* 41 */           iterator.remove();
/* 42 */           onCooldownEnded((Identifier)entry.getKey());
/*    */         } 
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   public Identifier getCooldownGroup(ItemStack item) {
/* 49 */     UseCooldown useCooldown = (UseCooldown)item.get(DataComponents.USE_COOLDOWN);
/* 50 */     Identifier defaultItemGroup = BuiltInRegistries.ITEM.getKey(item.getItem());
/* 51 */     if (useCooldown == null) {
/* 52 */       return defaultItemGroup;
/*    */     }
/* 54 */     return (Identifier)useCooldown.cooldownGroup().orElse(defaultItemGroup);
/*    */   }
/*    */ 
/*    */   
/* 58 */   public void addCooldown(ItemStack item, int time) { addCooldown(getCooldownGroup(item), time); }
/*    */ 
/*    */   
/*    */   public void addCooldown(Identifier cooldownGroup, int time) {
/* 62 */     this.cooldowns.put(cooldownGroup, new CooldownInstance(this.tickCount, this.tickCount + time));
/* 63 */     onCooldownStarted(cooldownGroup, time);
/*    */   }
/*    */   
/*    */   public void removeCooldown(Identifier cooldownGroup) {
/* 67 */     this.cooldowns.remove(cooldownGroup);
/* 68 */     onCooldownEnded(cooldownGroup);
/*    */   }
/*    */   protected void onCooldownStarted(Identifier cooldownGroup, int duration) {}
/*    */   
/*    */   protected void onCooldownEnded(Identifier cooldownGroup) {}
/*    */   
/*    */   private static final class CooldownInstance extends Record { private final int startTime;
/*    */     private final int endTime;
/*    */     
/* 77 */     private CooldownInstance(int startTime, int endTime) { this.startTime = startTime; this.endTime = endTime; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/ItemCooldowns$CooldownInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #77	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 77 */       //   0	7	0	this	Lnet/minecraft/world/item/ItemCooldowns$CooldownInstance; } public int startTime() { return this.startTime; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/ItemCooldowns$CooldownInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #77	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/ItemCooldowns$CooldownInstance; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/ItemCooldowns$CooldownInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #77	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/item/ItemCooldowns$CooldownInstance;
/* 77 */       //   0	8	1	o	Ljava/lang/Object; } public int endTime() { return this.endTime; } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\ItemCooldowns.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */