/*    */ package net.minecraft.world.entity.ai.attributes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AttributeSupplier
/*    */ {
/*    */   private final Map<Holder<Attribute>, AttributeInstance> instances;
/*    */   
/* 15 */   private AttributeSupplier(Map<Holder<Attribute>, AttributeInstance> instances) { this.instances = instances; }
/*    */ 
/*    */   
/*    */   private AttributeInstance getAttributeInstance(Holder<Attribute> attribute) {
/* 19 */     AttributeInstance instance = (AttributeInstance)this.instances.get(attribute);
/* 20 */     if (instance == null) {
/* 21 */       throw new IllegalArgumentException("Can't find attribute " + attribute.getRegisteredName());
/*    */     }
/* 23 */     return instance;
/*    */   }
/*    */ 
/*    */   
/* 27 */   public double getValue(Holder<Attribute> attribute) { return getAttributeInstance(attribute).getValue(); }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public double getBaseValue(Holder<Attribute> attribute) { return getAttributeInstance(attribute).getBaseValue(); }
/*    */ 
/*    */   
/*    */   public double getModifierValue(Holder<Attribute> attribute, Identifier id) {
/* 35 */     AttributeModifier modifier = getAttributeInstance(attribute).getModifier(id);
/* 36 */     if (modifier == null) {
/* 37 */       throw new IllegalArgumentException("Can't find modifier " + String.valueOf(id) + " on attribute " + attribute.getRegisteredName());
/*    */     }
/* 39 */     return modifier.amount();
/*    */   }
/*    */   
/*    */   public AttributeInstance createInstance(Consumer<AttributeInstance> onDirty, Holder<Attribute> attribute) {
/* 43 */     AttributeInstance template = (AttributeInstance)this.instances.get(attribute);
/* 44 */     if (template == null) {
/* 45 */       return null;
/*    */     }
/* 47 */     AttributeInstance result = new AttributeInstance(attribute, onDirty);
/* 48 */     result.replaceFrom(template);
/* 49 */     return result;
/*    */   }
/*    */ 
/*    */   
/* 53 */   public static Builder builder() { return new Builder(); }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public boolean hasAttribute(Holder<Attribute> attribute) { return this.instances.containsKey(attribute); }
/*    */ 
/*    */   
/*    */   public boolean hasModifier(Holder<Attribute> attribute, Identifier modifier) {
/* 61 */     AttributeInstance attributeInstance = (AttributeInstance)this.instances.get(attribute);
/* 62 */     return (attributeInstance != null && attributeInstance.getModifier(modifier) != null);
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 66 */     private final ImmutableMap.Builder<Holder<Attribute>, AttributeInstance> builder = ImmutableMap.builder();
/*    */     private boolean instanceFrozen;
/*    */     
/*    */     private AttributeInstance create(Holder<Attribute> attribute) {
/* 70 */       AttributeInstance result = new AttributeInstance(attribute, attributeInstance -> {
/* 71 */             if (this.instanceFrozen) {
/* 72 */               throw new UnsupportedOperationException("Tried to change value for default attribute instance: " + attribute.getRegisteredName());
/*    */             }
/*    */           });
/* 75 */       this.builder.put(attribute, result);
/* 76 */       return result;
/*    */     }
/*    */     
/*    */     public Builder add(Holder<Attribute> attribute) {
/* 80 */       create(attribute);
/* 81 */       return this;
/*    */     }
/*    */     
/*    */     public Builder add(Holder<Attribute> attribute, double baseValue) {
/* 85 */       AttributeInstance result = create(attribute);
/* 86 */       result.setBaseValue(baseValue);
/* 87 */       return this;
/*    */     }
/*    */     
/*    */     public AttributeSupplier build() {
/* 91 */       this.instanceFrozen = true;
/* 92 */       return new AttributeSupplier(this.builder.buildKeepingLast());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\attributes\AttributeSupplier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */