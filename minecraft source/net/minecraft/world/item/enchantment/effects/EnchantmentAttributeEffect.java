/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ import com.google.common.collect.HashMultimap;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*    */ import net.minecraft.world.item.enchantment.EnchantedItemInUse;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ 
/*    */ public final class EnchantmentAttributeEffect extends Record implements EnchantmentLocationBasedEffect {
/*    */   private final Identifier id;
/*    */   private final Holder<Attribute> attribute;
/*    */   
/* 19 */   public EnchantmentAttributeEffect(Identifier id, Holder<Attribute> attribute, LevelBasedValue amount, AttributeModifier.Operation operation) { this.id = id; this.attribute = attribute; this.amount = amount; this.operation = operation; } private final LevelBasedValue amount; private final AttributeModifier.Operation operation; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/EnchantmentAttributeEffect;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/EnchantmentAttributeEffect; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/EnchantmentAttributeEffect;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/EnchantmentAttributeEffect; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/EnchantmentAttributeEffect;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/EnchantmentAttributeEffect;
/* 19 */     //   0	8	1	o	Ljava/lang/Object; } public Identifier id() { return this.id; } public Holder<Attribute> attribute() { return this.attribute; } public LevelBasedValue amount() { return this.amount; } public AttributeModifier.Operation operation() { return this.operation; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public static final MapCodec<EnchantmentAttributeEffect> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Identifier.CODEC
/* 26 */         .fieldOf("id").forGetter(EnchantmentAttributeEffect::id), Attribute.CODEC
/* 27 */         .fieldOf("attribute").forGetter(EnchantmentAttributeEffect::attribute), LevelBasedValue.CODEC
/* 28 */         .fieldOf("amount").forGetter(EnchantmentAttributeEffect::amount), AttributeModifier.Operation.CODEC
/* 29 */         .fieldOf("operation").forGetter(EnchantmentAttributeEffect::operation))
/* 30 */       .apply(i, EnchantmentAttributeEffect::new));
/*    */ 
/*    */   
/* 33 */   private Identifier idForSlot(StringRepresentable slot) { return this.id.withSuffix("/" + slot.getSerializedName()); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public AttributeModifier getModifier(int level, StringRepresentable slot) { return new AttributeModifier(idForSlot(slot), amount().calculate(level), operation()); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onChangedBlock(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position, boolean becameActive) {
/* 42 */     if (becameActive && entity instanceof LivingEntity) { LivingEntity living = (LivingEntity)entity;
/* 43 */       living.getAttributes().addTransientAttributeModifiers(makeAttributeMap(enchantmentLevel, item.inSlot())); }
/*    */   
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDeactivated(EnchantedItemInUse item, Entity entity, Vec3 position, int level) {
/* 49 */     if (entity instanceof LivingEntity) { LivingEntity living = (LivingEntity)entity;
/* 50 */       living.getAttributes().removeAttributeModifiers(makeAttributeMap(level, item.inSlot())); }
/*    */   
/*    */   }
/*    */   
/*    */   private HashMultimap<Holder<Attribute>, AttributeModifier> makeAttributeMap(int enchantmentLevel, EquipmentSlot slot) {
/* 55 */     HashMultimap<Holder<Attribute>, AttributeModifier> map = HashMultimap.create();
/* 56 */     map.put(this.attribute, getModifier(enchantmentLevel, slot));
/* 57 */     return map;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 62 */   public MapCodec<EnchantmentAttributeEffect> codec() { return CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\effects\EnchantmentAttributeEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */