/*    */ package net.minecraft.advancements.criterion;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*    */ 
/*    */ public final class EnchantmentPredicate extends Record {
/*    */   private final Optional<HolderSet<Enchantment>> enchantments;
/*    */   private final MinMaxBounds.Ints level;
/*    */   
/* 15 */   public EnchantmentPredicate(Optional<HolderSet<Enchantment>> enchantments, MinMaxBounds.Ints level) { this.enchantments = enchantments; this.level = level; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/EnchantmentPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/EnchantmentPredicate; } public Optional<HolderSet<Enchantment>> enchantments() { return this.enchantments; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/EnchantmentPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/EnchantmentPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/EnchantmentPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/EnchantmentPredicate;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public MinMaxBounds.Ints level() { return this.level; }
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static final Codec<EnchantmentPredicate> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 20 */         RegistryCodecs.homogeneousList(Registries.ENCHANTMENT).optionalFieldOf("enchantments").forGetter(EnchantmentPredicate::enchantments), MinMaxBounds.Ints.CODEC
/* 21 */         .optionalFieldOf("levels", MinMaxBounds.Ints.ANY).forGetter(EnchantmentPredicate::level))
/* 22 */       .apply(i, EnchantmentPredicate::new));
/*    */ 
/*    */   
/* 25 */   public EnchantmentPredicate(Holder<Enchantment> enchantment, MinMaxBounds.Ints level) { this(Optional.of(HolderSet.direct(new Holder[] { enchantment }, )), level); }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public EnchantmentPredicate(HolderSet<Enchantment> enchantments, MinMaxBounds.Ints level) { this(Optional.of(enchantments), level); }
/*    */ 
/*    */   
/*    */   public boolean containedIn(ItemEnchantments itemEnchantments) {
/* 33 */     if (this.enchantments.isPresent()) {
/*    */       
/* 35 */       for (Holder<Enchantment> enchantment : (HolderSet)this.enchantments.get()) {
/* 36 */         if (matchesEnchantment(itemEnchantments, enchantment)) {
/* 37 */           return true;
/*    */         }
/*    */       } 
/* 40 */       return false;
/* 41 */     }  if (this.level != MinMaxBounds.Ints.ANY) {
/*    */       
/* 43 */       for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
/* 44 */         if (this.level.matches(entry.getIntValue())) {
/* 45 */           return true;
/*    */         }
/*    */       } 
/* 48 */       return false;
/*    */     } 
/*    */     
/* 51 */     return !itemEnchantments.isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   private boolean matchesEnchantment(ItemEnchantments itemEnchantments, Holder<Enchantment> enchantment) {
/* 56 */     int level = itemEnchantments.getLevel(enchantment);
/* 57 */     if (level == 0)
/*    */     {
/* 59 */       return false;
/*    */     }
/* 61 */     if (this.level == MinMaxBounds.Ints.ANY)
/*    */     {
/* 63 */       return true;
/*    */     }
/* 65 */     return this.level.matches(level);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\EnchantmentPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */