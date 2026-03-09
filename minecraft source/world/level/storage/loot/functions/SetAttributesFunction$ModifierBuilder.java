/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*    */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ModifierBuilder
/*    */ {
/*    */   private final Identifier id;
/*    */   private final Holder<Attribute> attribute;
/*    */   private final AttributeModifier.Operation operation;
/*    */   private final NumberProvider amount;
/*    */   private final Set<EquipmentSlotGroup> slots;
/*    */   
/*    */   public ModifierBuilder(Identifier id, Holder<Attribute> attribute, AttributeModifier.Operation operation, NumberProvider amount) {
/* 83 */     this.slots = EnumSet.noneOf(EquipmentSlotGroup.class);
/*    */ 
/*    */     
/* 86 */     this.id = id;
/* 87 */     this.attribute = attribute;
/* 88 */     this.operation = operation;
/* 89 */     this.amount = amount;
/*    */   }
/*    */   
/*    */   public ModifierBuilder forSlot(EquipmentSlotGroup slot) {
/* 93 */     this.slots.add(slot);
/* 94 */     return this;
/*    */   }
/*    */ 
/*    */   
/* 98 */   public SetAttributesFunction.Modifier build() { return new SetAttributesFunction.Modifier(this.id, this.attribute, this.operation, this.amount, List.copyOf(this.slots)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetAttributesFunction$ModifierBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */