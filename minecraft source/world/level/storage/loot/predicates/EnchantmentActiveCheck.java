/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ 
/*    */ public final class EnchantmentActiveCheck extends Record implements LootItemCondition {
/*    */   private final boolean active;
/*    */   
/* 12 */   public EnchantmentActiveCheck(boolean active) { this.active = active; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/EnchantmentActiveCheck;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/EnchantmentActiveCheck; } public boolean active() { return this.active; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/EnchantmentActiveCheck;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/EnchantmentActiveCheck; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/EnchantmentActiveCheck;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/EnchantmentActiveCheck;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 13 */   public static final MapCodec<EnchantmentActiveCheck> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.BOOL
/* 14 */         .fieldOf("active").forGetter(EnchantmentActiveCheck::active))
/* 15 */       .apply(i, EnchantmentActiveCheck::new));
/*    */ 
/*    */ 
/*    */   
/* 19 */   public boolean test(LootContext lootContext) { return (((Boolean)lootContext.getParameter(LootContextParams.ENCHANTMENT_ACTIVE)).booleanValue() == this.active); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public LootItemConditionType getType() { return LootItemConditions.ENCHANTMENT_ACTIVE_CHECK; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public Set<ContextKey<?>> getReferencedContextParams() { return Set.of(LootContextParams.ENCHANTMENT_ACTIVE); }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public static LootItemCondition.Builder enchantmentActiveCheck() { return () -> new EnchantmentActiveCheck(true); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public static LootItemCondition.Builder enchantmentInactiveCheck() { return () -> new EnchantmentActiveCheck(false); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\EnchantmentActiveCheck.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */